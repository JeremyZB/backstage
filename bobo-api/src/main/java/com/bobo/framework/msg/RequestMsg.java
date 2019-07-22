package com.bobo.framework.msg;

import java.io.Serializable;

/**
 * @ClassName: BaseRequestMsg
 * @Description: 消息输入基类
 * @author: leven
 * 
 */
public class RequestMsg<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * accessToken
	 */
	protected String accessToken;

	/**
	 * 用户代码
	 */
	protected Integer userId;

	/**
	 * 机构代码
	 */
	protected Integer organId;

	/**
	 * 请求流水号
	 */
	protected String reqSeq;

	/**
	 * 请求内容
	 */
	protected T param;

	public T getParam() {
		return param;
	}

	public void setParam(T param) {
		this.param = param;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getOrganId() {
		return organId;
	}

	public void setOrganId(Integer organId) {
		this.organId = organId;
	}

	public String getReqSeq() {
		return reqSeq;
	}

	public void setReqSeq(String reqSeq) {
		this.reqSeq = reqSeq;
	}

}
