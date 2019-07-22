package com.bobo.framework.mybatis.dbo;

import java.io.Serializable;

import com.bobo.framework.utils.JpException;

/**
 * @ClassName: DataOperateBean
 * @Description: 数据库操作javabean
 * @author: hanson huang
 */
public abstract class DataOperateAbstractBean implements Serializable {		
	
	private static final long serialVersionUID = 1L;
		
	//操作对象的key，用于获取返回结果集合中对应的对象
	protected String dataKey;
	
	//该对象是否需要检查执行结果
	protected boolean isCheckResult;
	
	//该对象为执行的预期结果，如果isCheckResult为true则expectValue为必填
	protected Integer expectValue;
	
	/**
	 * 默认构造方法，需要自己使用setter进行赋值
	 */
	public DataOperateAbstractBean(){
		
	}
	
	/**
	 * 对子类的共有属性进行初始化
	 * @param dataKey
	 * @param isCheckResult
	 * @param expectValue
	 */
	public void setProperties(String dataKey, boolean isCheckResult, Integer expectValue){
		this.dataKey = dataKey;
		this.isCheckResult = isCheckResult;
		if(isCheckResult){
			if(expectValue == null){
				throw new JpException("SYS-102", dataKey + "DOB组件异常：被设置为需要对结果进行检查，请设置预期结果（expectValue）");
			}
		}
		this.expectValue = expectValue;
	}
		
	public String getDataKey() {
		return dataKey;
	}

	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
	}

	public boolean isCheckResult() {
		return isCheckResult;
	}

	public void setCheckResult(boolean isCheckResult) {
		this.isCheckResult = isCheckResult;
	}

	public Integer getExpectValue() {
		return expectValue;
	}

	public void setExpectValue(Integer expectValue) {
		this.expectValue = expectValue;
	}

	//该方法需要子类进行override
	public abstract Object commit() throws Exception;
	
	//获取方法中的数据对象，主要为了兼容旧版直接使用DataOperateBean的程序
	public abstract Object getData();

}
