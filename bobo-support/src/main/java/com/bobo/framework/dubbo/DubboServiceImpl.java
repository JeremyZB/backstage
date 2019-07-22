package com.bobo.framework.dubbo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;













//import org.apache.commons.collections.map.LinkedMap;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.bobo.framework.docs.ScanDoc;
import com.bobo.framework.docs.ScanNotes;
import com.bobo.framework.manager.RetCodeManager;
import com.bobo.framework.manager.TxIdManager;
import com.bobo.framework.msg.RequestMsg;
import com.bobo.framework.msg.ResponseMsg;
import com.bobo.framework.targets.ApiDoc;
import com.bobo.framework.targets.Route;
import com.bobo.framework.targets.RouteBean;
import com.bobo.framework.utils.ClassUtil;
import com.bobo.framework.utils.DateUtil;
import com.bobo.framework.utils.JsonUtil;
import com.bobo.framework.utils.SpringUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import dxr.ClassInfo;
import dxr.Scanner;

@Service("iDubboService")
public class DubboServiceImpl implements IDubboService {

	private List<String> clearList = Arrays.asList("oas016");
	
	@Override
	public DubboOut doPost(DubboIn dubboIn) {
		String txid = dubboIn.getTxId();
		DubboOut dubboOut = new DubboOut();
		MethodBean methodBean = TxIdManager.getGlobal().getMethodBean(txid.toUpperCase());
		if (null == methodBean) {
			// 返回失败对象
			ResponseMsg<?> resp = new ResponseMsg<>();
			RetCodeManager.getGlobal().setErroCode(resp, "SYS-105");
			dubboOut.setParam(JsonUtil.writeValueAsString(resp));
		} else {
			try {
				Class<?> serviceObj = Class.forName(methodBean.getClassName());
				if(!clearList.contains(txid)) {
					System.out.println("-------------------------" + txid + "-------------------------");
					System.out.println("| req:" + dubboIn.getParam());
				}
				// 反序列化参数
				RequestMsg<?> reqObj = (RequestMsg<?>) JsonUtil.deserializeRequstVo(dubboIn.getParam(),
						Class.forName(methodBean.getParamClassName()), Class.forName(methodBean.getReqActualType()));

				System.out.println(reqObj);
				reqObj.setIp(dubboIn.getIp());
				ResponseMsg<?> resp = (ResponseMsg<?>) invokeMethod(serviceObj, methodBean.getMethodName(), reqObj);
				if (resp == null)
					resp = new ResponseMsg<>();

				if (!StringUtils.hasText(reqObj.getReqSeq()))
					reqObj.setReqSeq(String.valueOf(DateUtil.getToday()));
				resp.setReqSeq(reqObj.getReqSeq());
				dubboOut.setParam(JsonUtil.writeValueAsString(resp));
				if(!clearList.contains(txid)) {
					System.out.println("|");
					System.out.println("| resp:" + JsonUtil.writeValueAsString(resp));
					System.out.println("|--------------------------------------------------");
					System.out.println();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return dubboOut;
	}

	@Override
	public List<String> getVersion(String coreName) {
		List<String> list = Lists.newArrayList();
		list.add("_ALL");
		Class<ApiDoc> clazz = ApiDoc.class;
		Field[] fields = clazz.getDeclaredFields();
		Stream.of(fields).filter(f -> f.getName().startsWith("version_")).forEach(f -> {
			String version;
			try {
				version = (String) f.get(clazz);
				list.add(version);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		});
		return list;
	}
	
	
	@Override
	public String getTree(String coreName) {

		List<TreeItem> treeList = new ArrayList<TreeItem>();

		String configTxt = "classpath:/com/bobo/**/service/**/*Service.class";
		// String configTxt =
		// "classpath:/com/jiaparts/**/service/**/AccountsService.class";
		String exclude = "";
		try {
			// 扫描多个package
			String[] temp = configTxt.split(",");

			Map<String, String> ms = new HashMap<String, String>();

			for (int t = 0; t < temp.length; t++) {
				String config = temp[t];
				List<ClassInfo> classes = Scanner.scan(config, exclude);
				for (int i = 0; i < classes.size(); i++) {
					ClassInfo ci = classes.get(i);
					Class<?> c = ci.getC();
					// #pack
					String pack = c.getPackage().getName();
					if (!ms.containsKey(pack)) {
						TreeItem item = new TreeItem(pack, "#", pack.substring(pack.lastIndexOf("."), pack.length()),
								true);
						treeList.add(item);
						ms.put(pack, "0");
					}

					// #service
					String cName = ci.getC().getName();
					String cSimpName = "";

					String cnode = ScanNotes.getMethodNotes(cName);
					if (StringUtils.hasText(cnode))
						cSimpName = ci.getC().getSimpleName() + "(" + cnode + ")";
					else
						cSimpName = ci.getC().getSimpleName();

					//
					TreeItem item = new TreeItem(cName, pack, cSimpName, false);
					treeList.add(item);
					//
					// #methods
					Method[] methods = ci.getC().getDeclaredMethods();
					// List<MethodInfo> methods = ci.getMethods();
					for (int k = 0; k < methods.length; k++) {
						Method method = methods[k];
						String methodName = method.getName();
						String mname = "";
						String mnode = ScanNotes.getMethodNotes(cName, methodName);
						if (StringUtils.hasText(mnode))
							mname = methodName + "(" + mnode + ")";
						else
							mname = methodName;

						TreeItem item2 = new TreeItem(cName + "." + method.getName(), cName, mname, false);
						item2.setTarget("frmDetail");
						item2.setUrl(
								"txinfo/txtest?cls=" + cName + "&coreName=" + coreName + "&method=" + method.getName());
						treeList.add(item2);
					}
				}

				for (int i = 0; i < treeList.size() - 1; i++) {
					for (int j = treeList.size() - 1; j > i; j--) {
						if (treeList.get(j).getId().equals(treeList.get(i).getId())) {
							treeList.remove(j);
						}
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
		// System.out.println(JsonUtil.writeValueAsString(treeList));
		return JsonUtil.writeValueAsString(treeList);

	}

	@Override
	public String getTree(String coreName, String version) {

		String vs = "_ALL";
		if (StringUtils.hasText(version) && !vs.equals(version))
			vs = version;
		

		List<TreeItem> treeList = new ArrayList<TreeItem>();

		String configTxt = "classpath:/com/bobo/**/service/**/*Service.class";
		// String configTxt =
		// "classpath:/com/jiaparts/**/service/**/AccountsService.class";
		String exclude = "";
		try {
			// 扫描多个package
			String[] temp = configTxt.split(",");

			Map<String, String> ms = new HashMap<String, String>();

			for (int t = 0; t < temp.length; t++) {
				String config = temp[t];
				List<ClassInfo> classes = Scanner.scan(config, exclude);
				for (int i = 0; i < classes.size(); i++) {
					ClassInfo ci = classes.get(i);
					Class<?> c = ci.getC();

					boolean isversion = false;
					// �?查是否有api注解
					if (!vs.equals("_ALL")) {
						Method[] methods = c.getMethods();
						for (Method method : methods) {
							ApiDoc apiDoc = method.getAnnotation(ApiDoc.class);
							if (apiDoc != null && apiDoc.version().equals(vs)) {
								isversion = true;
								break;
							}
						}
						if (!isversion)
							continue;
					}

					// #pack
					String pack = c.getPackage().getName();
					if (!ms.containsKey(pack)) {
						TreeItem item = new TreeItem(pack, "#", pack.substring(pack.lastIndexOf("."), pack.length()),
								true);
						treeList.add(item);
						ms.put(pack, "0");
					}

					// #service
					String cName = ci.getC().getName();
					String cSimpName = "";

					String cnode = ScanNotes.getMethodNotes(cName);
					if (org.springframework.util.StringUtils.hasText(cnode))
						cSimpName = ci.getC().getSimpleName() + "(" + cnode + ")";
					else
						cSimpName = ci.getC().getSimpleName();

					//
					TreeItem item = new TreeItem(cName, pack, cSimpName, false);
					treeList.add(item);
					//
					// #methods
					Method[] methods = ci.getC().getDeclaredMethods();
					// List<MethodInfo> methods = ci.getMethods();
					for (int k = 0; k < methods.length; k++) {
						Method method = methods[k];
						if (!vs.equals("_ALL")) {
							ApiDoc apiDoc = method.getAnnotation(ApiDoc.class);
							if (apiDoc == null || !apiDoc.version().equals(vs))
								continue;
							
						}
						String methodName = method.getName();
						String mname = "";
						String mnode = ScanNotes.getMethodNotes(cName, methodName);
						if (StringUtils.hasText(mnode))
							mname = methodName + "(" + mnode + ")";
						else
							mname = methodName;

						TreeItem item2 = new TreeItem(cName + "." + method.getName(), cName, mname, false);
						item2.setTarget("frmDetail");
						item2.setUrl(
								"txinfo/txtest?cls=" + cName + "&coreName=" + coreName + "&method=" + method.getName());
						treeList.add(item2);
					}
				}

				for (int i = 0; i < treeList.size() - 1; i++) {
					for (int j = treeList.size() - 1; j > i; j--) {
						if (treeList.get(j).getId().equals(treeList.get(i).getId())) {
							treeList.remove(j);
						}
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
		// System.out.println(JsonUtil.writeValueAsString(treeList));
		return JsonUtil.writeValueAsString(treeList);

	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getTreeInput(String cName, String mName) {
		Map<String, Object> inputMap =Maps.newLinkedHashMap();

		Map<String, Object> headMap = Maps.newLinkedHashMap();
		Map<String, Object> paramMap =Maps.newLinkedHashMap();
		try {

			Class<?> clazz = Class.forName(cName);
			Method method = null;
			Method[] methods = clazz.getMethods();
			for (Method m : methods) {
				if (m.getName().equals(mName)) {
					method = m;
				}
			}

			Class<?>[] parameterTypes = method.getParameterTypes();
			for (Class<?> clas : parameterTypes) {
				Type reqType = method.getGenericParameterTypes()[0];
				Class<?> reqActualClss = getActualType(reqType);

				String paramClassName = clas.getName();
				// 获取标准�?
				Class<?> headClazz = Class.forName(paramClassName);
				getFiledMap(headClazz, headMap);

				// 获取param
				String reqActualType = reqActualClss.getName();
				Class<?> paramClazz = Class.forName(reqActualType);

				getFiledMap(paramClazz, paramMap);

				headMap.remove("param");
				inputMap.put("head", headMap);
				inputMap.put("param", paramMap);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return inputMap;
		// return JsonUtil.writeValueAsString(inputMap);
	}

	@Override
	public Map<String, Object> getDoc(String cls, String method) {
		return ScanDoc.getDoc(cls, method);
	}

	@Override
	public List<RouteBean> getRouteList(List<String> packages) {

		List<RouteBean> routeList = Lists.newArrayList();
		// 创建�?个扫描处理器，排除内部类 扫描符合条件的类
		ClassUtil handler = new ClassUtil(true, true, new ArrayList<String>());

		Set<Class<?>> calssList = Sets.newLinkedHashSet();
		for (String pack : packages)
			calssList.addAll(handler.getPackageAllClasses(pack, true));

		for (Class<?> clazz : calssList) {
			if (clazz.isInterface()) {
				Method[] methods = clazz.getMethods();
				for (Method method : methods) {
					Route route = method.getAnnotation(Route.class);
					if (route != null) {
						String mName = method.getName();

						RouteBean bean = new RouteBean();
						bean.setCoreName(route.coreName());
						bean.setTxId(!StringUtils.hasText(route.txId()) ? mName : route.txId());
						bean.setDeprecated(route.deprecated());
						bean.setToTxId(mName);
						routeList.add(bean);
					}
				}
			}
		}
		return routeList;
	}

	/**
	 * 
	 * @Title: invokeMethod
	 * @Description: 反射对应服务�?
	 * @param serviceObj 服务类class
	 * @param methodName 方法名称
	 * @param reqObj     请求参数bean
	 * @return
	 * @throws Exception
	 * @return: Object
	 */
	private <T> Object invokeMethod(Class<?> serviceObj, String methodName, Object reqObj) throws Exception {
		try {
			Method f = serviceObj.getMethod(methodName.toLowerCase(), new Class[] { reqObj.getClass() });// 方法名称、方法参数类型；多个参数类型形式如，new
			if (!f.isAccessible()) {
				f.setAccessible(true);
			}
			Object obj = SpringUtils.getBean(serviceObj);
			return f.invoke(obj, reqObj);
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new Exception("接口" + serviceObj.getName() + " 没有找到方法 " + methodName + "()", e);
		}
	}

	@SuppressWarnings("unchecked")
	void getFiledMap(Class<?> paramClazz, Map<String, Object> mmap) {
		List<Field> fields = getFields(paramClazz);
		for (Field field : fields) {
			field.setAccessible(true);
			String fieldName = field.getName();
			if (fieldName.equals("serialVersionUID"))
				continue;

			//String fnode = ScanNotes.getFieldNotes(paramClazz.getName(), fieldName).replace("//", "");
			//if (StringUtils.isNotBlank(fnode))
				//fieldName = fieldName + "<" + fnode + ">";

			Class<?> fieldClass = field.getType();
			if (isPrimitive(fieldClass)) {
				mmap.put(fieldName, fieldClass.getName());
			} else if (isList(fieldClass)) {
				// list
				Type type = field.getGenericType();
				if (type instanceof ParameterizedType) {
					ParameterizedType pt = (ParameterizedType) type;
					Class<?> genericClazz = (Class<?>) pt.getActualTypeArguments()[0];

					if (isPrimitive(genericClazz)) {
						// �?单类�?
						List<String> list = new ArrayList<String>();
						list.add(String.class.getName());
						mmap.put(fieldName, list);
					} else {
						List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
						Map<String, Object> map = Maps.newLinkedHashMap();
						getFiledMap(genericClazz, map);
						list.add(map);
						mmap.put(fieldName, list);
					}

				}
			} else if (isArray(fieldClass)) {
				List<String> list = new ArrayList<String>();
				list.add(String.class.getName());
				mmap.put(fieldName, list);
			} else {
				// 对象
				Map<String, Object> map = Maps.newLinkedHashMap();
				getFiledMap(field.getType(), map); // 递归
				mmap.put(fieldName, map);
			}
		}
	}

	boolean isArray(Class<?> clazz) {
		if (clazz.getSimpleName().indexOf("[]") > -1)
			return true;

		return false;
	}

	List<Field> getFields(Class<?> classz) {
		List<Field> fieldList = new ArrayList<>();
		Class<?> tempClass = classz;
		while (tempClass != null && !tempClass.equals(Object.class)) {// 当父类为null的时候说明到达了�?上层的父�?(Object�?).
			fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
			tempClass = tempClass.getSuperclass(); // 得到父类,然后赋给自己
		}
		return fieldList;
	}

	boolean isList(Class<?> clazz) {
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

	boolean isPrimitive(Class<?> clazz) {
		if (clazz.equals(java.lang.Object.class)) {
			return false;
		}
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

	Class<?> getActualType(Type type) {
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

}
