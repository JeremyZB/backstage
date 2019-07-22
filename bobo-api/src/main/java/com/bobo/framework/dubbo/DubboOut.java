package com.bobo.framework.dubbo;

import java.io.Serializable;

public class DubboOut implements Serializable {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 业务ID
	 */
	private String txId;

	/**
	 * 响应时间
	 */
	private String respTime;

	/**
	 * 响应流水�?
	 */
	private String respSeq;

	/**
	 * 返回参数
	 */
	private String param;

	public String getTxId() {
		return txId;
	}

	public void setTxId(String txId) {
		this.txId = txId;
	}

	public String getRespTime() {
		return respTime;
	}

	public void setRespTime(String respTime) {
		this.respTime = respTime;
	}

	public String getRespSeq() {
		return respSeq;
	}

	public void setRespSeq(String respSeq) {
		this.respSeq = respSeq;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

}
