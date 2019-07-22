package com.bobo.framework.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bobo.cfg.bean.RouteCfgBean;
import com.bobo.framework.dubbo.IDubboService;
import com.bobo.framework.route.RouteMgr;
import com.bobo.framework.spring.IDubboServiceFactory;
import com.bobo.framework.targets.RouteBean;
import com.bobo.cfg.RouteCfg;

@Controller
@RequestMapping("")
public class Test extends BaseApi
{

	@Autowired
	IDubboServiceFactory iDubboServiceFactory;
	@Autowired
	RouteCfg routeCfg;

	@RequestMapping(value = "test1")
	public @ResponseBody String test1()
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

		return "test1";
	}

	@RequestMapping(value = "test2")
	public ModelAndView test2()
	{
		ModelAndView mv = new ModelAndView("/index");
		mv.addObject("coreName", "mml");
		return mv;
	}
}
