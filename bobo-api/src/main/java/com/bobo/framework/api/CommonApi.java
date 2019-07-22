package com.bobo.framework.api;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.rpc.RpcException;
import com.bobo.framework.dubbo.DubboIn;
import com.bobo.framework.dubbo.DubboOut;
import com.bobo.framework.dubbo.IDubboService;
import com.bobo.framework.route.RouteMgr;
import com.bobo.framework.utils.JsonUtil;

@Controller
@RequestMapping("")
public class CommonApi extends BaseApi
{

	private static final Logger logger = LoggerFactory.getLogger(CommonApi.class);

	/**
	 * 公共接口入口，反射调用
	 * 
	 * @Title: common
	 * @Description: TODO
	 * @param param
	 * @return
	 * @return: Json
	 */
	@SuppressWarnings(
	{ "unchecked" })
	@RequestMapping("{coreName}/{txId}")
	@ResponseBody
	public Map<String, Object> common(HttpServletRequest request, @PathVariable("coreName") String coreName, @PathVariable("txId") String txId,
			@RequestParam(value = "body", required = false) String body)
	{
		Map<String, Object> resp = null;

		DubboIn dubboIn = new DubboIn();
		DubboOut dubboOut = new DubboOut();
		// 接口调用
		try
		{
			request.setCharacterEncoding("UTF-8");
			String param = "";
			if (body == null)
				body = getBody(request);
			param = body;

			// 路由
			Map<String, String> ret = RouteMgr.routePost(coreName, txId);
			if (ret == null)
			{
				resp = new HashMap<>();
				resp.put("retCode", "9998");
				resp.put("retMsg", "/"+coreName + "/" + txId + "接口已过期!");
				return resp;
			}

			coreName = ret.get("coreName");
			txId = ret.get("txId");

			logger.info("\n coreName:" + coreName + " ,  txid:" + txId);

			logger.info("param:" + param);

			dubboIn.setTxId(txId);
			dubboIn.setParam(param);
			String ip = getIpAddr(request);
			System.out.println("ip====================" + ip);
			dubboIn.setIp(ip);

			IDubboService iDubboService = this.getServiceByCoreName(coreName);
			if (iDubboService == null)
			{
				resp = new HashMap<>();
				resp.put("retCode", "9999");
				resp.put("retMsg", "获取服务异常!");
				return resp;
			}

			dubboOut = iDubboService.doPost(dubboIn);
			System.out.println(dubboOut.getParam());
			resp = JsonUtil.deserializeRequstVo(dubboOut.getParam(), Map.class);
		} catch (RpcException rpcException)
		{
			logger.error("接口调用失败！" + rpcException.getMessage());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		logger.info("ret:" + JsonUtil.writeValueAsString(resp));
		return resp;
	}

	/**
	 * 获取当前网络ip
	 * 
	 * @param request
	 * @return
	 */
	public String getIpAddr(HttpServletRequest request)
	{
		String ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress))
		{
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress))
		{
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress))
		{
			ipAddress = request.getRemoteAddr();
			if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1"))
			{
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try
				{
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e)
				{
					e.printStackTrace();
				}
				ipAddress = inet.getHostAddress();
			}
		}
		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15)
		{ // "***.***.***.***".length()
			// = 15
			if (ipAddress.indexOf(",") > 0)
			{
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}

}
