package com.bobo.framework.manager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import com.google.common.collect.Sets;
import com.bobo.framework.meta.ChannelMeta;
import com.bobo.framework.targets.AccessTokenWhite;
import com.bobo.framework.targets.PayAccount;
import com.bobo.framework.utils.ClassUtil;

public class AccessTokenWhiteManager implements InitializingBean {

	private static Logger logger = LoggerFactory.getLogger(AccessTokenWhiteManager.class);

	private String packages;

	public void setPackages(String packages) {
		this.packages = packages;
	}

	public String getPackages() {
		return packages;
	}

	private static AccessTokenWhiteManager global = null;

	Map<String, String> whiteMap = new HashMap<String, String>();
	
	Map<String, String> payMap = new HashMap<String, String>();

	public static AccessTokenWhiteManager getGlobal() {
		return global;
	}

	public void afterPropertiesSet() throws Exception {
		global = this;
		this.init();
	}

	public Map<String, String> getWhiteMap() {
		return whiteMap;
	}
	
	public Map<String, String> getPayMap() {
		return payMap;
	}

	public void setWhiteMap(Map<String, String> whiteMap) {
		this.whiteMap = whiteMap;
	}

	public void init() {
		load();
	}

	void load() {
		logger.info("******************初始化拦截器白名单**********************");
		Set<String> allClasses = new HashSet<String>();

		// 自定义过滤规则
		List<String> classFilters = new ArrayList<String>();
		classFilters.add("");
		// 创建一个扫描处理器，排除内部类 扫描符合条件的类
		ClassUtil handler = new ClassUtil(true, true, classFilters);

		String ps = this.getPackages();
		if (!StringUtils.hasText(ps))
			ps = "com.jiaparts";

		String[] p = ps.split(",");
		Set<Class<?>> calssList = Sets.newLinkedHashSet();
		for (String string : p) {
			calssList.addAll(handler.getPackageAllClasses(string, true));
		}

		for (Class<?> cla : calssList) {
			allClasses.add(cla.getName());
		}
		for (String className : allClasses) {
			try {
				Class<?> clazz = Class.forName(className);
				if (clazz.isInterface()) {
					Method[] methods = clazz.getMethods();
					for (Method method : methods) {
						String methodName = method.getName().toUpperCase();
						AccessTokenWhite tokenWhite = method.getAnnotation(AccessTokenWhite.class);
						if (tokenWhite != null) {
							String channels = tokenWhite.channels();
							System.out.println(methodName.toUpperCase() + "   ,   " + channels);
							whiteMap.put(methodName.toUpperCase(), channels);
						}
						PayAccount payAccount = method.getAnnotation(PayAccount.class);
						if (payAccount != null) {
							System.out.println(methodName.toLowerCase() + "   ,   " + payAccount.type());
							payMap.put(methodName.toLowerCase(), payAccount.type());
						}
					}
				}
			} catch (Exception e) {
				logger.error("Init error map failed", e);
				continue;
			}
		}

	}

	/**
	 * @Title: getInterceptor
	 * @Description: 获取所有需要拦截的应用及渠道信息
	 * @param txCode
	 * @return
	 * @return: Map<String,String>
	 */
	public String getInterceptor(String methodName) {
		return this.whiteMap.get(methodName.toUpperCase());
	}

	/**
	 * 
	 * @Title: evaluateTxCode
	 * @Description: 根据应用ID，TXCODE,渠道ID判断是否在拦截白名单中
	 * @param txCode
	 * @param appcode
	 * @param channel
	 * @return
	 * @return: boolean
	 */
	public boolean isWhiteList(String methodName, String channel) {
		try {
			String channels = getInterceptor(methodName);

			if (!StringUtils.hasText(channels)) {
				return false;
			} else {
				if (channels.indexOf(channel.toUpperCase()) != -1 || "_ALL".equals(channels)
						|| ChannelMeta.DEV.equals(channels)) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {
		Set<String> allClasses = new HashSet<String>();

		// 自定义过滤规则
		List<String> classFilters = new ArrayList<String>();
		classFilters.add("");
		// 创建一个扫描处理器，排除内部类 扫描符合条件的类
		ClassUtil handler = new ClassUtil(true, true, classFilters);

		Set<Class<?>> calssList = handler.getPackageAllClasses("com.jiaparts.jps.service", true);
		for (Class<?> cla : calssList) {
			allClasses.add(cla.getName());
		}
		for (String className : allClasses) {
			try {
				Class clazz = Class.forName(className);
				if (clazz.isInterface()) {

					Method[] methods = clazz.getMethods();
					for (Method method : methods) {
						String methodName = method.getName().toUpperCase();
						AccessTokenWhite tokenWhite = method.getAnnotation(AccessTokenWhite.class);
						if (tokenWhite != null) {
							String channel = tokenWhite.channels();
							System.out.println(methodName + "**************" + channel);
						}
					}
				}
			} catch (Exception e) {
				logger.error("Init error map failed", e);
				continue;
			}
		}
	}

}
