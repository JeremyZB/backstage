package com.bobo.framework.mybatis.dbo;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * @ClassName: DataCommit
 * @Description: 数据库集中提交
 * @author: hanson huang
 */
public class DataCommit {		
	
	/**
	 * 批量提交数据并返回每个对象的执行结果集合
	 * @param dol
	 * @throws Exception 
	 */
	public static Map<String,Object> batch(DataOperateList dol) throws Exception{
		if(dol != null && dol.size() > 0){
			Map<String,Object> result = Maps.newHashMap();
			//设置初始默认检查结果为true
			result.put("CHECKRESULT", true);
			int executeResult = 0;
			for(DataOperateAbstractBean dob : dol.getTodoList()){
				try {
					Object obj = dob.commit();
					if(obj!=null){
						executeResult = (Integer)obj;
					}
					result.put(dob.getDataKey(), executeResult);
					if((Boolean)result.get("CHECKRESULT") && dob.isCheckResult()){
						if(executeResult != dob.getExpectValue()){
							result.put("CHECKRESULT", false);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				}
			}
			return result;
		}else{
			return null;
		}
	}	
	
	/**
	 * 批量提交数据，并返回ischeckresult后的结果
	 * @param dol
	 * @return
	 * @throws Exception
	 */
	public static boolean batch2(DataOperateList dol) throws Exception{		
		Map<String,Object> result = batch(dol);
		if(result == null){
			return false;
		}
		return (Boolean)result.get("CHECKRESULT");
	}

	public static void main(String[] args) {
		
	}

}
