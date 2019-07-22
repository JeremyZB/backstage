package com.bobo.framework.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author leven
 *
 */
public class JsonUtil {
	private static ObjectMapper om = new ObjectMapper();
	static {
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	}

	public static <T> T deserializeRequstVo(String param, Class<T> clazz) {
		T t = null;
		try {
			t = (T) om.readValue(param, clazz);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return t;
	}

	public static <T> T deserializeRequstVo(String param, Class<T> clazz, Class<?> ActualClazz) {
		T t = null;
		try {
			JavaType javaType = getCollectionType(clazz, ActualClazz);
			t = om.readValue(param, javaType);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return t;
	}

	public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
		return om.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}

	public static String writeValueAsString(Object obj) {

		try {
			return om.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;

	}
 
}
