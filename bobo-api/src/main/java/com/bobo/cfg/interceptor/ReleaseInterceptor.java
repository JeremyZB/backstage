package com.bobo.cfg.interceptor;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.bobo.cfg.RedisClient0;
import com.bobo.framework.msg.ResponseMsg;
import com.bobo.framework.utils.JsonUtil;

public class ReleaseInterceptor implements HandlerInterceptor
{
	private static final Logger logger = LoggerFactory.getLogger(ReleaseInterceptor.class);

	private static final String releaseInterceptor_switch_key = "releaseInterceptor:switch";
	private static final String releaseInterceptor_msg_key = "releaseInterceptor:msg";
	private static final String releaseInterceptor_ips_key = "releaseInterceptor:ips";

	RedisClient0 redisClient0;

	public void setRedisClient0(RedisClient0 redisClient0)
	{
		this.redisClient0 = redisClient0;
	}

	public RedisClient0 getRedisClient0()
	{
		return redisClient0;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
	{
		// 拦截器开关
		String svalue = redisClient0.get(releaseInterceptor_switch_key);
		if (null == svalue || svalue == "" || svalue.equals("0"))
			return true;

		String requestUri = request.getRequestURI();
		String contextPath = request.getContextPath();
		String url = requestUri.substring(contextPath.length());
		String ip = getIpAddr(request);
		logger.info("ip:" + ip + ",url:" + url);
		boolean f = getCheckIpWhiteResult(ip);
		if (f)
			return true;
		// 校验IP
		String mvalue = redisClient0.get(releaseInterceptor_msg_key);
		if (null == mvalue || mvalue == "")
			mvalue = "系统升级中";

		ResponseMsg<?> resp = new ResponseMsg<>();
		resp.setRetCode("SYS-900");
		resp.setRetMsg(mvalue);
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().print(JsonUtil.writeValueAsString(resp));
		response.getWriter().close();
		return false;
	}

	private boolean getCheckIpWhiteResult(String ip)
	{
		if (null != ip && ip != "")
		{
			// at:ip:ip地址
			String tokenKey = releaseInterceptor_ips_key + ":" + ip;
			String value = redisClient0.get(tokenKey);

			if (null != value && value != "")
			{
				if (value.equals("true"))
					return true;
			}
		}
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception
	{
		// TODO Auto-generated method stub

	}

	private String getIpAddr(HttpServletRequest request)
	{
		try
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

		} catch (Exception e)
		{
			// TODO: handle exception
		}
		return "";
	}
}
