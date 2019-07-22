package com.bobo.framework.manager;

import java.io.Serializable;

public class RetCode implements Serializable {
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;

	public RetCode() {

	}

	public RetCode(String code, String msg) {
		this.code = code;
		this.message = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	String code;

	String message;
}
