package com.bobo.framework.dubbo;

import java.util.List;
import java.util.Map;

import com.bobo.framework.targets.RouteBean;

public abstract interface IDubboService {
	
	public abstract DubboOut doPost(DubboIn paramDubboInPamamBean);
	public abstract List<String> getVersion(String coreName);
	public abstract String getTree(String coreName,String version);
	public abstract String getTree(String coreName);
	public abstract Map<String, Object> getTreeInput(String cName,String mName);
	public abstract  Map<String, Object>  getDoc(String cName,String mName);
	public abstract	List<RouteBean>  getRouteList(List<String> packages);
}
