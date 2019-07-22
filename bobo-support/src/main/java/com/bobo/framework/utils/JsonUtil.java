package com.bobo.framework.utils;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.collect.Maps;

/**
 * 
 * @author leven
 *
 */
public class JsonUtil
{
	private static ObjectMapper om = new ObjectMapper();
	private static ObjectMapper om2 = new ObjectMapper();
	static
	{
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		om2.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		om2.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		om2.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

		SimpleModule module = new SimpleModule();
		module.addSerializer(String.class, new StringUnicodeSerializer());
		om2.registerModule(module);
	}

	public static <T> T deserializeRequstVo(String param, Class<T> clazz)
	{
		T t = null;
		try
		{
			t = (T) om.readValue(param, clazz);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return t;
	}

	public static <T> T deserializeRequstVo(String param, Class<T> clazz, Class<?> ActualClazz)
	{
		T t = null;
		try
		{
			JavaType javaType = getCollectionType(clazz, ActualClazz);
			t = om.readValue(param, javaType);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return t;
	}

	public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses)
	{
		return om.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}

	public static String writeValueAsString(Object obj)
	{

		try
		{
			return om.writeValueAsString(obj);
		} catch (JsonProcessingException e)
		{
			e.printStackTrace();
		}
		return null;

	}

	public static String writeValueAsUnicodeString(Object obj)
	{

		try
		{
			return om2.writeValueAsString(obj);
		} catch (JsonProcessingException e)
		{
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * @Title: JsonToMap
	 * @Description: 将Json转Map
	 * @param json
	 * @return: Map<String,Object>
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> JsonToMap(String json)
	{

		Map<String, Object> paramMap = Maps.newHashMap();
		try
		{
			paramMap = om.readValue(json, Map.class);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return paramMap;
	}

}
