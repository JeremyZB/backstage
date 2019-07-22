package com.bobo.framework.route;

import java.util.Map;

import com.bobo.framework.targets.RouteBean;
import com.google.common.collect.Maps;

public class RouteMgr
{
	public static Map<String, RouteBean> routeMap = Maps.newHashMap();

	public static Map<String, String> routePost(String coreName, String txId)
	{
		Map<String, String> ret = Maps.newHashMap();
		RouteBean routeBean = RouteMgr.routeMap.get(coreName + "_" + txId);
		if (routeBean != null)
		{
			if (routeBean.isDeprecated())
				return null;
			ret.put("coreName", routeBean.getToCoreName());
			ret.put("txId", routeBean.getToTxId());

		} else
		{
			ret.put("coreName", coreName);
			ret.put("txId", txId);
		}
		return ret;
	}
}
