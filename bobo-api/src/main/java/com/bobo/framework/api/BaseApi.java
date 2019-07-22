package com.bobo.framework.api;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bobo.framework.dubbo.IDubboService;
import com.bobo.framework.spring.IDubboServiceFactory;

@Component
public class BaseApi
{
	@Autowired
	IDubboServiceFactory iDubboServiceFactory;
	
	
	String getBody(HttpServletRequest request) throws Exception
	{
		ServletInputStream in = request.getInputStream();
		byte[] buf = new byte[8 * 1024];
		StringBuffer sbuf = new StringBuffer();
		int result;
		do
		{
			result = in.readLine(buf, 0, buf.length); // does +=
			if (result != -1)
			{
				sbuf.append(new String(buf, 0, result, "UTF-8"));
			}
		} while (result == buf.length); // loop only if the buffer was filled

		if (sbuf.length() == 0)
		{
			return null; // nothing read, must be at the end of stream
		}
		in.close();
		return sbuf.toString();
	}

	protected IDubboService getServiceByCoreName(String coreName)
	{
		return iDubboServiceFactory.getServiceByCoreName(coreName);
	}
	

}
