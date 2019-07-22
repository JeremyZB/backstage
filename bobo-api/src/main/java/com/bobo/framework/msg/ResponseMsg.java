package com.bobo.framework.msg;

import java.io.Serializable;

/**
 * @ClassName: BaseResponseMsg
 * @Description: 消息输出基类
 * @author: leven
 * 
 */
public class ResponseMsg<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 错误代码 200:成功
	 */
	protected String retCode;
	/**
	 * 返回信息
	 */
	protected String retMsg;
	/**
	 * 异常堆栈信息
	 */
	protected String errMsg;
	/**
	 * 请求流水�?
	 */
	protected String reqSeq;

	/**
	 * 返回结果
	 */
	protected T result;

	public ResponseMsg() {

	}

	public ResponseMsg(String retCode, String retMsg) {
		super();
		this.retCode = retCode;
		this.retMsg = retMsg;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public String getReqSeq() {
		return reqSeq;
	}

	public void setReqSeq(String reqSeq) {
		this.reqSeq = reqSeq;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

}
