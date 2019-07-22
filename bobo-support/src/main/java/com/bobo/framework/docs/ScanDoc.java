package com.bobo.framework.docs;

import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.collections.map.LinkedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.bobo.framework.utils.JsonUtil;

/**
 * @ClassName: ScanNotes
 * @Description: 扫描注释
 * @author: leven
 */
@SuppressWarnings("unchecked")
public class ScanDoc {
	private static Logger logger = LoggerFactory.getLogger(ScanDoc.class);

	public static Map<String, Object> getDoc(String cls, String method) {
		Map<String, Object> docMap = null;
		try {
			Class<?> clazz = Class.forName(cls);
			Method[] mds = clazz.getMethods();
			
			
			Map<String, Object> reqMap = Maps.newLinkedHashMap();
			Map<String, Object> paramMap = Maps.newLinkedHashMap();
			
			Map<String, Object> respMap = Maps.newLinkedHashMap();
			Map<String, Object> resultMap = Maps.newLinkedHashMap();
			
			for (Method md : mds) {
				if (md.getName().equals(method)) {
					docMap =  Maps.newLinkedHashMap();
					 
					//req
					Class<?> inClazz = md.getParameterTypes()[0];
					ScanTools.scanClazz(inClazz, reqMap);
					Class<?> reqClazz = ScanTools.getReqActualClass(md);
					ScanTools.scanClazz(reqClazz, paramMap);
					
					reqMap.remove("param");
					
					reqMap.put("param //请求体", paramMap);
 
					//resp
					Class<?> outClazz = md.getReturnType();
					ScanTools.scanClazz(outClazz, respMap);
					
					Class<?> respClazz = ScanTools.getRespActualClass(md);
					ScanTools.scanClazz(respClazz, resultMap);
					respMap.remove("result");
					respMap.put("result //返回体", resultMap);
					
					
					docMap.put("req", reqMap);
					docMap.put("resp", respMap);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info(JsonUtil.writeValueAsString(docMap));
		return docMap;
	}

}
