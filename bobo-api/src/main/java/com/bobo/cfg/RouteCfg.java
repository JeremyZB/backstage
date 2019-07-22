package com.bobo.cfg;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.bobo.cfg.bean.RouteCfgBean;

@Configuration
@ConfigurationProperties("route.cfg")
public class RouteCfg
{
	
	
	private List<RouteCfgBean> cfgList;

	public List<RouteCfgBean> getCfgList()
	{
		return cfgList;
	}

	public void setCfgList(List<RouteCfgBean> cfgList)
	{
		this.cfgList = cfgList;
	}

}
