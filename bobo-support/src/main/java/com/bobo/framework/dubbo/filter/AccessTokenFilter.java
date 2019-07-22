package com.bobo.framework.dubbo.filter;

import java.util.Arrays;

import org.springframework.util.StringUtils;

import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.RpcResult;
import com.google.common.base.Joiner;
import com.bobo.framework.dubbo.DubboIn;
import com.bobo.framework.dubbo.DubboOut;
import com.bobo.framework.manager.AccessTokenWhiteManager;
import com.bobo.framework.manager.RetCode;
import com.bobo.framework.manager.RetCodeManager;
import com.bobo.framework.meta.ChannelMeta;
import com.bobo.framework.msg.RequestMsg;
import com.bobo.framework.msg.ResponseMsg;
import com.bobo.framework.redis.RedisClient0;
import com.bobo.framework.utils.JsonUtil;
import com.bobo.framework.utils.SpringUtils;

/**
 * @ClassName: TokenFilter
 * @Description: Token验证
 * @author: Leven
 * @date: 2017年7月18日 下午5:52:16
 */

public class AccessTokenFilter implements Filter {

	RedisClient0 redisClient0;

	static String errorMsg = "您的登录状态已失效，请重新登录";

	public RedisClient0 getRedisClient0() {
		return redisClient0;
	}

	// set方式注入
	public void setRedisClient0(RedisClient0 redisClient0) {
		this.redisClient0 = redisClient0;
	}

	/**
	 * @Title: invoke
	 * @Description: 针对请求进行token验证
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws RpcException
	 * @see com.alibaba.dubbo.rpc.Filter#invoke(com.alibaba.dubbo.rpc.Invoker,
	 *      com.alibaba.dubbo.rpc.Invocation)
	 */
	@Override
	public Result invoke(Invoker<?> arg0, Invocation arg1) throws RpcException {
		this.redisClient0 = SpringUtils.getBean(RedisClient0.class);
		if (!arg1.getMethodName().equals("doPost")) {
			return arg0.invoke(arg1);
		}
		DubboIn dubboIn = (DubboIn) arg1.getArguments()[0];
		// redis校验IP白名单
		String ip = dubboIn.getIp();
		if (StringUtils.hasText(ip) && ip.contains(",")) {
			// 当服务器有多个IP地址时使用IpUtil.java返回结果中多个IP地址以“,”分隔
			// String[] ipArr = StringUtils.split(ip, ",");
			String[] ipArr = ip.split(",");
			for (int i = 0; i < ipArr.length; i++) {
				if (checkIpWhite(ipArr[i]))
					return arg0.invoke(arg1);
			}
		} else if (checkIpWhite(ip)) {
			return arg0.invoke(arg1);
		}

		RequestMsg<?> requestMsg = JsonUtil.deserializeRequstVo(dubboIn.getParam(), RequestMsg.class);

		String txId = dubboIn.getTxId();
		String channel = requestMsg.getChannel();
		String accessToken = requestMsg.getAccessToken();
		Integer grpId = requestMsg.getGrpId();

		if (!StringUtils.hasText(channel)) {
			RetCode retCode = RetCodeManager.getGlobal().getCode("SYS-106");
			return new RpcResult(doPostFail(retCode.getCode(), retCode.getMessage()));
		}
		// 判断各主要参数是否为空
		if (!StringUtils.hasText(txId)) {
			RetCode retCode = RetCodeManager.getGlobal().getCode("SYS-105");
			return new RpcResult(doPostFail(retCode.getCode(), retCode.getMessage()));
		}

		// // 排除拦截白名单
		boolean flag = AccessTokenWhiteManager.getGlobal().isWhiteList(txId, channel);
		if (flag)
			return arg0.invoke(arg1);

		if (redisClient0.exists("at:dev")) {
			if (accessToken.toUpperCase().equals(ChannelMeta.DEV))
				return arg0.invoke(arg1);
		}

		// 开始验证token
		if (!StringUtils.hasText(accessToken)) 
			return new RpcResult(doPostFail("300", errorMsg));
		
		if (requestMsg.getUserId() == null || requestMsg.getUserId().intValue() == 0) 
			return new RpcResult(doPostFail("300", errorMsg));
		
		// redis验证token ..
		try {
			// at:渠道:userId

			String tokenKey = "";
			if (grpId != null && grpId.intValue() != 0)
				tokenKey = "at:" + grpId + ":" + channel + ":" + requestMsg.getUserId();
			else
				tokenKey = "at:" + channel + ":" + requestMsg.getUserId();
			String value = redisClient0.get(tokenKey);
			if (!StringUtils.hasText(value)) {
				return new RpcResult(doPostFail("300", errorMsg));
			}
			if (!value.toLowerCase().equals(accessToken.toLowerCase()))
				return new RpcResult(doPostFail("300", errorMsg));
		} catch (Exception e) {
			e.printStackTrace();
			return new RpcResult(doPostFail("999", e.toString()));
		}
		return arg0.invoke(arg1);
	}

	public DubboOut doPostFail(String failCode, String failReason) {
		DubboOut op = new DubboOut();
		try {

			ResponseMsg<?> resp = new ResponseMsg<>();
			resp.setRetCode(failCode);
			resp.setRetMsg(failReason);

			op.setParam(JsonUtil.writeValueAsString(resp));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return op;
	}

	public boolean checkIpWhite(String ip) {
		// 判断ip是否在白名单，判断的顺序为：*.*.*.*, 1.*.*.*, 1.2.*.*,1.2.3.*, 1.2.3.4
		if (getCheckIpWhiteResult("*.*.*.*")) {
			return true;
		}
		if (StringUtils.hasText(ip)) {
			String[] ipNodes = ip.split("\\.");
			int len = ipNodes.length;
			if (len != 4) {
				return false;
			}
			for (int i = 1; i < len + 1; i++) {
				StringBuilder sb = new StringBuilder();
				sb.append(Joiner.on(".").join(Arrays.copyOfRange(ipNodes, 0, i)));
				for (int j = 0; j < len - i; j++) {
					sb.append(".*");
				}
				if (getCheckIpWhiteResult(sb.toString())) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean getCheckIpWhiteResult(String ip) {
		if (StringUtils.hasText(ip)) {
			// at:ip:ip地址
			String tokenKey = "at:ip:" + ip;
			String value = redisClient0.get(tokenKey);
			if (StringUtils.hasText(value)) {
				if (value.equals("true"))
					return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {

		String ip = "1.2.3.4";
//		System.out.println(ip);
		// String[] ipNodes = StringUtils.split(ip, ".");
		String[] ipNodes = ip.split("\\.");

		int len = ipNodes.length;
		for (int i = 0; i < len + 1; i++) {
			StringBuilder sb = new StringBuilder();
			if (i > 0) {
				sb.append(Joiner.on(".").join(Arrays.copyOfRange(ipNodes, 0, i)));
				for (int j = 0; j < len - i; j++) {
					sb.append(".*");
				}
			} else {
				sb.append("*.*.*.*");
			}
			System.out.println(sb.toString());
		}
	}
}
