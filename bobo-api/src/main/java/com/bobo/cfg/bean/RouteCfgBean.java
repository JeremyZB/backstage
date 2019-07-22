package com.bobo.cfg.bean;

import java.util.List;

public class RouteCfgBean
{

	/**
	 * 要拉取的路由coreName
	 */
	private String coreName;

	/**
	 * 扫描包路径
	 */
	private List<String> packages;

	public String getCoreName()
	{
		return coreName;
	}

	public void setCoreName(String coreName)
	{
		this.coreName = coreName;
	}

	public List<String> getPackages()
	{
		return packages;
	}

	public void setPackages(List<String> packages)
	{
		this.packages = packages;
	}

}
