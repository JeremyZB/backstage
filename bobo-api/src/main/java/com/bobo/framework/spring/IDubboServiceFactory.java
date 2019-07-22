package com.bobo.framework.spring;

import java.util.Map;

import com.bobo.framework.dubbo.IDubboService;

public class IDubboServiceFactory
{

	Map<String, IDubboService> serviceFactory;

	public Map<String, IDubboService> getServiceFactory()
	{
		return serviceFactory;
	}

	public void setServiceFactory(Map<String, IDubboService> serviceFactory)
	{
		this.serviceFactory = serviceFactory;
	}

	public IDubboService getServiceByCoreName(String coreName)
	{
		return this.serviceFactory.get(coreName);
	}

}
