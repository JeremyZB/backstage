package com.bobo.framework.docs;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.LinkedMap;

import com.google.common.collect.Maps;

@SuppressWarnings("unchecked")
public class ScanTools {

	public static void scanClazz(Class<?> clazz, Map<String, Object> paramMap) throws IntrospectionException {
		List<FieldsInfo> fields = getFieldsInfo(clazz);

		for (FieldsInfo fieldInfo : fields) {
			Field field = fieldInfo.getField();

			field.setAccessible(true);
			String fieldName = field.getName();
			if (fieldName.equals("serialVersionUID"))
				continue;

			Class<?> fieldClass = field.getType();
			if (ScanTools.isPrimitive(fieldClass)) {
				paramMap.put(fieldName, "  " + fieldClass.getSimpleName() + fieldInfo.getFieldNotes());
			} else if (ScanTools.isList(fieldClass)) {
				// list
				Type type = field.getGenericType();
				if (type instanceof ParameterizedType) {
					ParameterizedType pt = (ParameterizedType) type;
					Class<?> genericClazz = (Class<?>) pt.getActualTypeArguments()[0];

					if (ScanTools.isPrimitive(genericClazz)) {
						// 简单类型
						List<String> list = new ArrayList<String>();
						list.add(String.class.getName());
						paramMap.put(fieldName + fieldInfo.getFieldNotes(), list);
					} else {
						List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
						if (!clazz.toString().equals(genericClazz.toString())) {
							Map<String, Object> map = Maps.newLinkedHashMap();
							scanClazz(genericClazz, map);
							list.add(map);
						}
						paramMap.put(fieldName + fieldInfo.getFieldNotes(), list);
					}

				}
			} else if (ScanTools.isArray(fieldClass)) {
				List<String> list = new ArrayList<String>();
				list.add(String.class.getName());
				paramMap.put(fieldName, list);
			} else {
				// 对象
				Map<String, Object> map = Maps.newLinkedHashMap();
				scanClazz(field.getType(), map);
				paramMap.put(fieldName, map);
			}
		}
	}

	static List<FieldsInfo> getFieldsInfo(Class<?> classz) {
		List<FieldsInfo> fieldList = new ArrayList<>();
		Class<?> tempClass = classz;
		while (tempClass != null && !tempClass.equals(Object.class)) {// 当父类为null的时候说明到达了最上层的父类(Object类).
			List<FieldsInfo> tempList = new ArrayList<FieldsInfo>();
			Field[] fields = tempClass.getDeclaredFields();
			for (Field field : fields) {
				FieldsInfo filesInfo = new FieldsInfo();
				filesInfo.setField(field);
				filesInfo.setFieldNotes(ScanNotes.getFieldNotes(tempClass.getName(), field.getName()));
				tempList.add(filesInfo);

			}
			fieldList.addAll(tempList);
			tempClass = tempClass.getSuperclass(); // 得到父类,然后赋给自己
		}
		return fieldList;
	}

	public static boolean isPrimitive(Class<?> clazz) {
		if (clazz.isPrimitive()) {
			return true;
		}
		if (clazz.getName().startsWith("java.lang.")) {
			return true;
		}
		if (clazz.equals(java.util.Date.class)) {
			return true;
		}
		if (clazz.equals(java.sql.Date.class)) {
			return true;
		}
		if (clazz.equals(BigDecimal.class)) {
			return true;
		}
		if (clazz.equals(BigInteger.class)) {
			return true;
		}
		return false;
	}

	public static boolean isList(Class<?> clazz) {
		if ("java.util.List".equals(clazz.getName())) {
			return true;
		}
		Class<?>[] interfaceClazzs = clazz.getInterfaces();
		for (Class<?> interfaceClazz : interfaceClazzs) {
			if ("java.util.List".equals(interfaceClazz.getName())) {
				return true;
			}
		}
		return false;
	}

	public static boolean isArray(Class<?> clazz) {
		if (clazz.getSimpleName().indexOf("[]") > -1)
			return true;

		return false;
	}

	public static Class<?> getReqActualClass(Method method) {
		Type type = method.getGenericParameterTypes()[0];
		Class<?> genericClazz = null;
		ParameterizedType pt = null;
		try {
			pt = (ParameterizedType) type;

			genericClazz = (Class<?>) pt.getActualTypeArguments()[0];
		} catch (Exception e) {
			return Object.class;
		}
		return genericClazz;
	}

	public static Class<?> getRespActualClass(Method method) {
		Type type = method.getGenericReturnType();
		Class<?> genericClazz = null;
		ParameterizedType pt = null;
		try {
			pt = (ParameterizedType) type;

			genericClazz = (Class<?>) pt.getActualTypeArguments()[0];
		} catch (Exception e) {
			// Type et = pt.getActualTypeArguments()[0];
			return Object.class;

		}
		return genericClazz;
	}

}
