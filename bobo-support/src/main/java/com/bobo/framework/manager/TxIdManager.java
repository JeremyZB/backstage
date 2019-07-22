package com.bobo.framework.manager;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;
import com.bobo.framework.dubbo.MethodBean;
import com.bobo.framework.utils.JsonUtil;

import dxr.ClassInfo;
import dxr.Scanner;

/**
 * 
 * @ClassName: MethodToClassManager
 * @Description: 反射工具类，扫描所有的指定报下的service及其方法
 * @date: 2016年5月25日 下午1:27:44
 */
public class TxIdManager implements InitializingBean
{

	private static TxIdManager global = null;

	/**
	 * 定义classMap
	 */
	private Map<String, MethodBean> classMap = new HashMap<String, MethodBean>();

	String config;

	public static TxIdManager getGlobal()
	{
		return global;
	}

	public void afterPropertiesSet() throws Exception
	{
		global = this;
		this.init();
	}

	public Map<String, MethodBean> getClassMap()
	{
		return classMap;
	}

	public void setClassMap(Map<String, MethodBean> classMap)
	{
		this.classMap = classMap;
	}

	public MethodBean getMethodBean(String methodName)
	{
		return classMap.get(methodName);
	}

	public String getConfig()
	{
		return config;
	}

	public void setConfig(String config)
	{
		this.config = config;
	}

	public void init()
	{
		try
		{
			load();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @Title: load
	 * @Description: 初始化，扫描路径下所有的接口类，并将方法放入methodBean中，再放入classMap
	 * @return: void
	 * @throws Exception
	 */
	protected void load() throws Exception
	{
		List<ClassInfo> allClasses = Lists.newArrayList();
 
		String[] temp = config.split(",");
		for (String string : temp) {
			allClasses.addAll(Scanner.scan(string));
		}
		
		for (ClassInfo classInfo : allClasses)
		{
			Class<?> clazz = classInfo.getC();
			if (clazz.isInterface())
			{
				System.out.println("#" + clazz.getName());

				Method[] methods = clazz.getMethods();
				for (Method method : methods)
				{

					try
                    {
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        for (Class<?> clas : parameterTypes)
                        {
                            
                            if(method.getReturnType().getName().equals("void"))
                            {
                                continue;
                            }
                        	Type reqType = method.getGenericParameterTypes()[0];
                        	Type respType = method.getGenericReturnType();
                        	Class<?> reqActualClss = getActualType(reqType);
                        	Class<?> respActualClss = getActualType(respType);

                        	MethodBean methodBean = new MethodBean();
                        	methodBean.setClassName(classInfo.getC().getName());
                        	methodBean.setMethodName(method.getName());
                        	methodBean.setParamClassName(clas.getName());
                        	methodBean.setReqActualType(reqActualClss.getName());
                        	methodBean.setRespActualType(respActualClss.getName());
                        	classMap.put(method.getName().toUpperCase(), methodBean);
                        	System.out.println(method.getName() + "#" + JsonUtil.writeValueAsString(methodBean));
                        }
                    }
                    catch (Exception e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        continue;
                    }

				}
				System.out.println("");
			}
		}

		if (StringUtils.hasText(config))
			return;
		allClasses = Scanner.scan("classpath:/com/jiaparts/**/*Service.class", "");

		for (ClassInfo classInfo : allClasses)
		{
			Class<?> clazz = classInfo.getC();
			if (clazz.isInterface())
			{
				System.out.println("#" + clazz.getName());

				Method[] methods = clazz.getMethods();
				for (Method method : methods)
				{
                    try
                    {
                        if(method.getReturnType().getName().equals("void"))
                        {
                            continue;
                        }
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        for (Class<?> clas : parameterTypes)
                        {
                        	Type reqType = method.getGenericParameterTypes()[0];
                        	Type respType = method.getGenericReturnType();
                        	Class<?> reqActualClss = getActualType(reqType);
                        	Class<?> respActualClss = getActualType(respType);

                        	MethodBean methodBean = new MethodBean();
                        	methodBean.setClassName(classInfo.getC().getName());
                        	methodBean.setMethodName(method.getName());
                        	methodBean.setParamClassName(clas.getName());
                        	methodBean.setReqActualType(reqActualClss.getName());
                        	methodBean.setRespActualType(respActualClss.getName());
                        	classMap.put(method.getName().toUpperCase(), methodBean);
                        	System.out.println(method.getName() + "#" + JsonUtil.writeValueAsString(methodBean));
                        }
                    }
                    catch (Exception e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        continue;
                    }

				}
				System.out.println("");
			}
		}

	}

	protected Class<?> getActualType(Type type)
	{
		Class<?> genericClazz = null;
		ParameterizedType pt = null;
		try
		{
			pt = (ParameterizedType) type;

			genericClazz = (Class<?>) pt.getActualTypeArguments()[0];
		} catch (Exception e)
		{

			//Type et = pt.getActualTypeArguments()[0];
			return Object.class;

		}

		return genericClazz;

	}
}
