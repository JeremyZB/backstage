package com.bobo.framework.dubbo;

public class MethodBean {

	private String className;

	/**
	 * 方法名称
	 */
	private String methodName;

	private String paramClassName;

	/**
	 * 请求泛型
	 */
	private String reqActualType;

	/**
	 * 返回泛型
	 */
	private String respActualType;

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getMethodName() {
		return methodName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getParamClassName() {
		return paramClassName;
	}

	public void setParamClassName(String paramClassName) {
		this.paramClassName = paramClassName;
	}

	public String getReqActualType() {
		return reqActualType;
	}

	public void setReqActualType(String reqActualType) {
		this.reqActualType = reqActualType;
	}

	public String getRespActualType() {
		return respActualType;
	}

	public void setRespActualType(String respActualType) {
		this.respActualType = respActualType;
	}

	@Override
	public String toString() {
		return "MethodBean [className=" + className + ", methodName=" + methodName + ", paramClassName=" + paramClassName + ", reqActualType=" + reqActualType
				+ ", respActualType=" + respActualType + "]";
	}

}
