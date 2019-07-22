package com.bobo.framework.msg;

import java.io.Serializable;
import java.util.Map;

public class TaskRequestMsg implements Serializable {
	private static final long serialVersionUID = -5869777051464625703L;
	
	/**
	 * 线程名称
	 */
	private String threadName;
	
	/**
	 * 回调地址，http方式请求使用
	 */
	private String httpCallbackUrl;

	/**
	 * 回调core，dubbo方式请求使用
	 */
	private String dubboCallbackCore;

	/**
	 * 回调txid，dubbo方式请求使用
	 */
	private String dubboCallbackTxId;
	
	/**
	 * 自定义参�?
	 */
	private Map<String,String> params;

	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}
		
	public String getDubboCallbackCore() {
		return dubboCallbackCore;
	}

	public void setDubboCallbackCore(String dubboCallbackCore) {
		this.dubboCallbackCore = dubboCallbackCore;
	}

	public String getDubboCallbackTxId() {
		return dubboCallbackTxId;
	}

	public void setDubboCallbackTxId(String dubboCallbackTxId) {
		this.dubboCallbackTxId = dubboCallbackTxId;
	}

	public String getHttpCallbackUrl() {
		return httpCallbackUrl;
	}

	public void setHttpCallbackUrl(String httpCallbackUrl) {
		this.httpCallbackUrl = httpCallbackUrl;
	}

	public Map<String,String> getParams() {
		return params;
	}

	public void setParams(Map<String,String> params) {
		this.params = params;
	}

	
}
