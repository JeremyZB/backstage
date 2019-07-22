package com.bobo.framework.route;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.bobo.framework.spring.IDubboServiceFactory;
import com.bobo.framework.targets.RouteBean;
import com.bobo.cfg.RouteCfg;
import com.bobo.cfg.bean.RouteCfgBean;
import com.bobo.framework.dubbo.IDubboService;

@Component
@Order(2)
public class RouteInit implements CommandLineRunner

{

	@Autowired
	IDubboServiceFactory iDubboServiceFactory;
	@Autowired
	RouteCfg routeCfg;

	@Override
	public void run(String... args) throws Exception
	{
		List<RouteCfgBean> cfgList = this.routeCfg.getCfgList();

		for (RouteCfgBean bean : cfgList)
		{
			try
			{
				IDubboService iDubboService = iDubboServiceFactory.getServiceByCoreName(bean.getCoreName());
				List<RouteBean> list = iDubboService.getRouteList(bean.getPackages());
				for (RouteBean item : list)
				{
					item.setToCoreName(bean.getCoreName());
					System.out.println();
					RouteMgr.routeMap.put(item.getCoreName() + "_" + item.getTxId(), item);
				}
			} catch (Exception e)
			{
				// TODO: handle exception
			}
		}
	}

}
