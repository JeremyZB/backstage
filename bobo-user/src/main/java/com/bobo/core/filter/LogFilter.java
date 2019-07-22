package com.bobo.core.filter;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.util.StringUtils;

import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Stopwatch;
import com.bobo.framework.dubbo.DubboIn;
import com.bobo.framework.dubbo.DubboOut;
import com.bobo.core.filter.Ascii2native;
import com.bobo.framework.msg.ResponseMsg;
import com.bobo.framework.utils.DateUtil;
import com.bobo.framework.utils.JsonUtil;
import com.bobo.framework.utils.SpringUtils;


import com.bobo.core.mapper.master.ExceptionLogMapper;
import com.bobo.core.mapper.master.UserOperateLogMapper;
import com.bobo.core.model.ExceptionLog;
import com.bobo.core.model.UserOperateLog;
import com.bobo.core.tool.Func;
import com.bobo.core.util.Constant;




import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogFilter implements Filter {
	
	ExceptionLogMapper exceptionLogMapper;

	UserOperateLogMapper userOperateLogMapper;

	Func func;
	
	public void setExceptionLogMapper(ExceptionLogMapper exceptionLogMapper) {
		this.exceptionLogMapper = exceptionLogMapper;
	}

	public void setUserOperateLogMapper(UserOperateLogMapper userOperateLogMapper) {
		this.userOperateLogMapper = userOperateLogMapper;
	}

	public void setFunc(Func func) {
		this.func = func;
	}

	
	/*
	 * ApplicationContext ac = ContextLoader.getCurrentWebApplicationContext();
	 * ExceptionLogDao exceptionLogDao = ac.getBean(ExceptionLogDao.class);
	 */
	@SuppressWarnings({ "unchecked", "unused", "deprecation", "rawtypes" })
	@Override
	public Result invoke(Invoker<?> arg0, Invocation arg1) throws RpcException {
		Stopwatch stw = Stopwatch.createStarted();
		Long beginmillis = System.currentTimeMillis();
		
		// 执行结束
		Result result = arg0.invoke(arg1);
		Long endmillis = System.currentTimeMillis();

		try {

			String mn = arg1.getMethodName().toLowerCase();

			String txId = "";
			String ip = "";
			String param = "";
			ResponseMsg msg = new ResponseMsg<>();
			if ("dopost".equals(mn)) {// 当前工程内调用
				DubboIn req = (DubboIn) arg1.getArguments()[0];
				ip = req.getIp();
				param = req.getParam();
				txId = req.getTxId();
				DubboOut dout = (DubboOut) result.getResult();
				String oparam = dout.getParam();
				try {
					msg = JsonUtil.deserializeRequstVo(oparam, ResponseMsg.class);
				} catch (Exception e) {
					msg.setRetCode("400");
				}

			} else if (!"gettree".equals(mn) && !"gettreeinput".equals(mn)  &&!"getdoc".equals(mn)) {// 远程调用
				txId = mn;
				ObjectMapper om = new ObjectMapper();
				String jsonstr = om.writeValueAsString(arg1.getArguments()[0]);
				if ("\"rep\"".equals(jsonstr)) {
					return result;
				}
				Map<String, Object> jsonMap = om.readValue(jsonstr, Map.class);
				if (jsonMap != null) {
					ip = (String) jsonMap.get("ip");
					Map<String, Object> paramMap = (Map<String, Object>) jsonMap.get("param");
					param = om.writeValueAsString(arg1.getArguments()[0]);
					System.out.println(ip + "=" + param);
				}
				msg = (ResponseMsg) result.getResult();

			}else {
				return result;
			}
			log.info("================={}总耗时{}==================\r\n",txId, stw.toString());

			Integer organId = null;
			Integer userId = null;
			JSONObject reqParamObject = new JSONObject();
			if (StringUtils.hasText(param)) {
				JSONObject oo = new JSONObject();
				try {
					oo = JSONObject.parseObject(Ascii2native.ascii2native(param));
				} catch (Exception x) {
					oo = JSONObject.parseObject(param);
				}
				reqParamObject = getNotNull(oo);
				//organId = Integer.valueOf(Optional.ofNullable(reqParamObject.get("organId")).orElse("0").toString());
				userId = Integer.valueOf(Optional.ofNullable(reqParamObject.get("userId")).orElse("0").toString());
			}

			if (func == null) {
				/*ApplicationContext context= ServiceBean.getSpringContext();
				baseRule = context.getBean(BaseRule.class);*/
				//func = (Func) SpringUtils.getBean("func");
	             log.info("==LogFilter中func对象为空==");
			}
			
			if (StringUtils.hasText(msg.getRetCode())
					&& !Constant.RetCode.SUCCESS_CODE.getCode().equals(msg.getRetCode())) {

				ExceptionLog exceptionLog = new ExceptionLog();
				exceptionLog.setCreateTime(Long.parseLong(DateUtil.getToday().toString()));
				exceptionLog.setUserId(userId);
				exceptionLog.setReqUrl(txId);
				exceptionLog.setReqParam(JSON.toJSONString(reqParamObject));
				exceptionLog.setRemark(!StringUtils.hasText(msg.getErrMsg()) ? msg.getRetMsg() : msg.getErrMsg());

				if (exceptionLogMapper == null) {
					exceptionLogMapper = (ExceptionLogMapper) SpringUtils.getBean("exceptionLogMapper");
		             log.info("==LogFilter中exceptionLogMapper对象为空==");
				}
				
				Function<ExceptionLog, ExceptionLog> fun = C -> {
					exceptionLogMapper.insertSelective(C);
					return null;
				};
				//func.asynData(fun, exceptionLog);

			} else {
					// 记录操作日志
					UserOperateLog coreLogs = new UserOperateLog();
					coreLogs.setReqTime((long)DateUtil.getToday());
					coreLogs.setIp(ip);
					//coreLogs.setCostTime((long)endmillis-beginmillis);
					coreLogs.setUserId(userId);
					coreLogs.setReqUrl(txId);
					coreLogs.setUserId(userId);
					coreLogs.setReqParam(JSON.toJSONString(reqParamObject));
					//coreLogs.setName(JsonConfig.get(txId));
					if (userOperateLogMapper == null) {
						userOperateLogMapper = (UserOperateLogMapper) SpringUtils.getBean("userOperateLogMapper");
			             log.info("==LogFilter中userOperateLogMapper对象为空==");
					}
					Function<UserOperateLog, ExceptionLog> fun = C -> {
						userOperateLogMapper.insertSelective(C);
						return null;
					};
					//func.asynData(fun, coreLogs);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/***
	 * 去掉json中所有null和“”
	 * 
	 * @param oo
	 * @return
	 */

	public static JSONObject getNotNull(JSONObject oo) {
		JSONObject jsons = new JSONObject();
		for (String str : oo.keySet()) {
			Object values = oo.get(str);
			if ("param".equals(str)) {
				JSONObject jsons1 = new JSONObject();
				JSONObject oo2 = JSONObject.parseObject(oo.getString("param"));
				for (String str2 : oo2.keySet()) {
					JSONObject json = getNotNull(str2, oo2.get(str2));
					jsons1.putAll(json);
				}
				jsons.put("param", jsons1);
			} else if ("log".equals(str)) {
				// JSONObject jsons1 = new JSONObject();
				JSONObject oo2 = JSONObject.parseObject(oo.getString("log"));
				/*
				 * for(String str2:oo2.keySet()){ JSONObject
				 * json=getNotNull(str2,oo2.get(str2)); jsons1.putAll(json); }
				 */
				jsons.put("log", oo2);
			} else {
				JSONObject json = getNotNull(str, values);
				jsons.putAll(json);
			}

		}
		return jsons;
	}

	public static JSONObject getNotNull(String str, Object values) {
		JSONObject json = new JSONObject();
		if (values != null) {
			String str1 = values.toString();
			if (org.springframework.util.StringUtils.hasText(str1)) {
				if (!"[]".equals(str1) && !"null".equals(str1)) {
					json.put(str, values);
				}
			}
		}

		return json;

	}

	/**
	 * 获取反参
	 *
	 */
	/*
	 * public UserOperateLog getUserOperateLogEntity(JSONObject oJsonObject) {
	 * UserOperateLog coreLogs = new UserOperateLog(); List<String> listIds =
	 * Lists.newArrayList(); int total =
	 * Integer.valueOf(Optional.ofNullable(oJsonObject.get("total")).orElse(0).
	 * toString()); coreLogs.setTotal(total); String result11 =
	 * Optional.ofNullable(oJsonObject.get("result")).orElse("").toString(); if
	 * (org.springframework.util.StringUtils.hasText(result11)) { try { JSONObject
	 * dataObject = JSONObject.parseObject(result11); String data =
	 * Optional.ofNullable(dataObject.getString("data")).orElse(dataObject.getString
	 * ("list")); if (data == null) { for (String str : dataObject.keySet()) {
	 * String values = dataObject.getString(str); if (values != null &&
	 * values.contains("[{")) { data = dataObject.getString(str); } } }
	 * 
	 * JSONArray jsonArray = JSON.parseArray(data); for (Object obj : jsonArray) {
	 * JSONObject json = JSONObject.parseObject(obj.toString()); String id =
	 * json.getString("goodsSaleId"); if
	 * (org.springframework.util.StringUtils.hasText(id)) { listIds.add(id); } id =
	 * json.getString("goodsSaleID"); if
	 * (org.springframework.util.StringUtils.hasText(id)) { listIds.add(id); } }
	 * String RespParam = String.join(",", listIds);
	 * 
	 * if(RespParam!=null&&RespParam.length()>600) {
	 * RespParam=RespParam.substring(0, 600); }
	 * 
	 * coreLogs.setRespParam(RespParam);
	 * 
	 * } catch (Exception e) {
	 * 
	 * } }
	 * 
	 * return coreLogs; }
	 * 
	 *//**
		 * 通过入参获取参数解释
		 *
		 */
	/*
	 * public String getTranParam(JSONObject paramJson, String txId) { StringBuffer
	 * sb = new StringBuffer(); try { MethodBean methodBean =
	 * TxIdManager.getGlobal().getMethodBean(txId.toUpperCase()); Class c =
	 * Class.forName(methodBean.getReqActualType()); Map<String, ParamExp>
	 * map=annoMap(null, c, Maps.newHashMap()); // Field[] fields =
	 * c.getDeclaredFields();
	 * 
	 * Map<String, ParamExp> map=Maps.newHashMap(); for(Field field:fields) {
	 * ParamExp e = field.getDeclaredAnnotation(ParamExp.class); if (null == e) {
	 * continue; } Class<?> classCc=getParameterizedType(field); if(classCc!=null) {
	 * Field[] fds = classCc.getDeclaredFields(); for(Field fd:fds) { ParamExp fde =
	 * fd.getDeclaredAnnotation(ParamExp.class); if (null != fde) {
	 * if("".equals(e.isList())) { map.put(fd.getName(), fde);
	 * 
	 * }else { map.put(e.isList()+":"+fd.getName(), fde);
	 * 
	 * } }
	 * 
	 * }
	 * 
	 * } map.put(field.getName(), e);
	 * 
	 * } if (!map.isEmpty()) { sb=getStringBuffer( paramJson, map , sb,null);
	 * StringBuffer sbLists=new StringBuffer(); map.forEach((key,value)->{
	 * if(org.springframework.util.StringUtils.hasText(value.isList())) { JSONArray
	 * listObject=JSON.parseArray(paramJson.getString(value.isList()));
	 * if(listObject!=null) { StringBuffer sbList=new StringBuffer();
	 * sbList.append(value.keyDesc()+":["); for(Object obj:listObject) {
	 * sbList.append("{"); JSONObject json = JSONObject.parseObject(obj.toString());
	 * sbList=getStringBuffer( json, map , sbList,value.isList());
	 * sbList.append("}");
	 * 
	 * } sbList.append("]"); sbLists.append(sbList); } } }); sb.append(sbLists);
	 * 
	 * 
	 * }
	 * 
	 * } catch (Exception e) { e.printStackTrace(); } return sb.toString(); }
	 *//**
		 * 通过field获取字段名对应注解的map
		 *
		 */
	/*
	 * private static Map<String, ParamExp> annoMap(Field field, Class<?> classc,
	 * Map<String, ParamExp> map) { Field[] fds = classc.getDeclaredFields(); for
	 * (Field fd : fds) { ParamExp fde = fd.getDeclaredAnnotation(ParamExp.class);
	 * if (null != fde) { Class<?> classCc = getParameterizedType(fd); if (classCc
	 * != null) {//为数组比如List<CcAdaptParam> standardParam annoMap(fd, classCc,
	 * map);//fd为standardParam classCc为CcAdaptParam map.put(fd.getName(), fde); }
	 * else { if (field == null) { map.put(fd.getName(), fde); } else {
	 * map.put(field.getName() + ":" + fd.getName(), fde); } }
	 * 
	 * } } return map; }
	 * 
	 *//**
		 * 
		 * 获取某一个字段上面的泛型参数数组  
		 * 
		 * @param f
		 * @return
		 */
	/*
	 * public static Class<?> getParameterizedType(Field f) {
	 * 
	 * // 获取f字段的通用类型 Type fc = f.getGenericType(); // 关键的地方得到其Generic的类型
	 * 
	 * // 如果不为空并且是泛型参数的类型 if (fc != null && fc instanceof ParameterizedType) {
	 * 
	 * //Class<?> genericClazz = (Class<?>)
	 * DrawDoc.getSuperClassGenricType(f.getType(), 0);
	 * 
	 * ParameterizedType pt = (ParameterizedType) fc; Class<?> genericClazz1 =
	 * (Class<?>) pt.getActualTypeArguments()[0];
	 * 
	 * return genericClazz1;
	 * 
	 * 
	 * 
	 * 
	 * ParameterizedType pt = (ParameterizedType) fc;
	 * 
	 * Type[] types = pt.getActualTypeArguments();
	 * 
	 * if (types != null && types.length > 0) { Class<?>[] classes = new
	 * Class<?>[types.length]; for (int i = 0; i < classes.length; i++) { classes[i]
	 * = (Class<?>) types[i]; } return classes[0]; }
	 * 
	 * } return null; }
	 * 
	 *//**
		 * 通过入参获取参数解释
		 *
		 */
	/*
	 * public StringBuffer getStringBuffer(JSONObject paramJson,Map<String,
	 * ParamExp> map ,StringBuffer sb,String isList) { try { for (String key :
	 * paramJson.keySet()) {
	 * 
	 * String orValue = paramJson.get(key).toString(); if(isList!=null) {
	 * key=isList+":"+key; } ParamExp exp = map.get(key); if (exp == null) {
	 * continue; } String[] valueDesc = exp.valueDesc();// valueDesc
	 * ={"1:全部","2:好友","3:生意群"}
	 * 
	 * Boolean hasValue = false; for (String value : valueDesc) { if
	 * (value.contains(":")) { String[] val = value.split(":"); if
	 * (val[0].equals(orValue)) { sb.append(exp.keyDesc() + ":" + val[1] + ",");
	 * hasValue = true; break; } } }
	 * 
	 * if (!hasValue&&exp.isList().equals("")) { String expMethod =
	 * exp.method();// @ParamExp(keyDesc = "生意群",method ="getGrpNameById" )
	 * 
	 * if (org.springframework.util.StringUtils.hasText(expMethod)) { Method setFunc
	 * = logMethod.getClass().getMethod(exp.method(), String.class); //
	 * 取得方法后即可通过invoke方法调用，传入被调用方法所在类的对象和实参,对象可以通过cls.newInstance取得 Object invoValue
	 * = setFunc.invoke(logMethod, orValue); sb.append(exp.keyDesc() + ":" +
	 * invoValue + ",");
	 * 
	 * } else { sb.append(exp.keyDesc() + ":" + orValue + ","); }
	 * 
	 * }
	 * 
	 * } } catch (Exception e) { e.printStackTrace(); }
	 * 
	 * return sb; }
	 * 
	 *//**
		 * 通过入参获取参数解释 备用
		 *//*
			 * public String getTranParam2(JSONObject paramJson, String txId) { StringBuffer
			 * sb = new StringBuffer(); try { MethodBean methodBean =
			 * TxIdManager.getGlobal().getMethodBean(txId.toUpperCase()); Class clazz =
			 * Class.forName(methodBean.getReqActualType());
			 * 
			 * String str =
			 * "{'test':'大幅度','list':[{'realseId':'29','pubOrganId':'165643','num':'1','goodsSaleId':'3294484'},"
			 * +
			 * "{'realseId':'299','pubOrganId':'1656439','num':'19','goodsSaleId':'32944849'}]"
			 * +
			 * ",'addShopCarExt':{'realseId':'29','pubOrganId':'165643','num':'1','goodsSaleId':'3294484'}"
			 * + ",'list2':[1,2,3,4]}"; Object jsonObject = JSONObject.parseObject(str);
			 * paramJson = (JSONObject)jsonObject;
			 * 
			 * for (String key : paramJson.keySet()) {
			 * 
			 * Object value = paramJson.get(key); //value的值类型时不确定的，得加以判断处理
			 * 
			 * sb = recursionData(sb, clazz, key, value);
			 * 
			 * }
			 * 
			 * } catch (Exception e) { e.printStackTrace(); } return sb.toString(); }
			 * 
			 * private StringBuffer recursionData(StringBuffer sb, Class clazz, String key,
			 * Object value) throws NoSuchFieldException, NoSuchMethodException,
			 * IllegalAccessException, InvocationTargetException {
			 * 
			 * Field field = clazz.getDeclaredField(key); if(value instanceof
			 * JSONArray){//数组
			 * 
			 * System.out.println("arr="+value);
			 * 
			 * JSONArray arr = (JSONArray)value;
			 * 
			 * ParamExp exp = field.getDeclaredAnnotation(ParamExp.class); if(exp != null){
			 * sb.append(exp.keyDesc()).append(":");//拼接数组头信息 Type type =
			 * field.getGenericType(); if (type instanceof ParameterizedType) {
			 * ParameterizedType pt = (ParameterizedType) type; Class<?> genericClazz =
			 * (Class<?>) pt.getActualTypeArguments()[0];
			 * System.out.println("==============genericClazz:"+genericClazz.getName()); if
			 * (isPrimitive(genericClazz)) {// 集合泛型为简单类型,直接拼接 sb.append(arr).append(",");
			 * 
			 * } else { sb.append("["); for (Object object : arr) {
			 * 
			 * //JSONObject jjo = (JSONObject)object;
			 * 
			 * 
			 * sb = generateObj(sb, genericClazz, object); } sb.append("]").append(","); }
			 * 
			 * }
			 * 
			 * } }else if(value instanceof JSONObject){
			 * 
			 * ParamExp exp = field.getDeclaredAnnotation(ParamExp.class); if(exp != null){
			 * sb.append(exp.keyDesc()).append(":");//拼接数组头信息 } Class fieldClass =
			 * field.getType(); sb = generateObj(sb, fieldClass, value); } else{
			 * System.out.println("str="+value); String orValue = value.toString();
			 * generateStr(sb, clazz, key, orValue); } return sb; }
			 * 
			 * private StringBuffer generateObj(StringBuffer sb, Class<?> genericClazz,
			 * Object object) throws NoSuchFieldException, NoSuchMethodException,
			 * IllegalAccessException, InvocationTargetException { sb.append("{"); String
			 * subStr = object.toString(); subStr = subStr.replaceAll("\\{|\\}|\"",
			 * "");//替换字符串中的大括号和引号为空 //subStr = subStr.replaceFirst("\\{|\\}", "");
			 * //System.out.println("后："+subStr); String[] subbStrs = subStr.split(","); for
			 * (String subbStr : subbStrs) { String[] subbbStrs = subbStr.split(":"); String
			 * subbbKey = subbbStrs[0]; String subbbValue = subbbStrs[1];
			 * 
			 * //Object subbObj = jjo.get(subbbKey);
			 * 
			 * //recursionData(sb, clazz, key, value);
			 * 
			 * generateStr(sb, genericClazz, subbbKey, subbbValue); } String tempStr =
			 * sb.substring(0, sb.length()-2);//去掉每项最后的逗号 sb = new StringBuffer(tempStr);
			 * sb.append("},"); return sb; }
			 * 
			 * private void generateStr(StringBuffer sb, Class c, String key, String
			 * orValue) throws NoSuchFieldException, NoSuchMethodException,
			 * IllegalAccessException, InvocationTargetException { Field field =
			 * c.getDeclaredField(key); ParamExp exp =
			 * field.getDeclaredAnnotation(ParamExp.class); if(exp != null){ String[]
			 * valueDescs = exp.valueDesc(); Boolean hasValue = false; for (String valueDesc
			 * : valueDescs) { if (valueDesc.contains(":")) { String[] val =
			 * valueDesc.split(":"); if (val[0].equals(orValue)) { sb.append(exp.keyDesc() +
			 * ":" + val[1] + ","); hasValue = true; break; } } } if (!hasValue) { String
			 * expMethod = exp.method(); if
			 * (org.springframework.util.StringUtils.hasText(expMethod)) { Method setFunc =
			 * logMethod.getClass().getMethod(exp.method(), String.class); //
			 * 取得方法后即可通过invoke方法调用，传入被调用方法所在类的对象和实参,对象可以通过cls.newInstance取得 Object invoValue
			 * = setFunc.invoke(logMethod, orValue); sb.append(exp.keyDesc() + ":" +
			 * invoValue + ",");
			 * 
			 * } else { sb.append(exp.keyDesc() + ":" + orValue + ","); }
			 * 
			 * } } }
			 * 
			 * boolean isPrimitive(Class<?> clazz) { if (clazz.isPrimitive()) { return true;
			 * } if (clazz.getName().startsWith("java.lang.")) { return true; } if
			 * (clazz.equals(java.util.Date.class)) { return true; } if
			 * (clazz.equals(java.sql.Date.class)) { return true; } if
			 * (clazz.equals(BigDecimal.class)) { return true; } if
			 * (clazz.equals(BigInteger.class)) { return true; } return false; }
			 */
}
