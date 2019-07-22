package com.bobo.framework.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import com.bobo.framework.manager.RetCodeManager;
import com.bobo.framework.msg.ResponseMsg;
import com.bobo.framework.utils.JpException;

/**
 * 
 * @ClassName: BaseService
 * @date: 2016年9月2日 下午2:52:49
 */
@Component
public class BaseService {

	public static final String succes = "200";
	public static final String fail = "400";

	/**
	 * @Title: handleException
	 * @Description: 异常处理时返回值处理
	 * @param t
	 * @param e
	 * @return
	 * @return: T
	 */
	protected ResponseMsg<?> handleException(ResponseMsg<?> t, Throwable e) {
		if (e instanceof JpException) {
			return setResCode(t, (JpException) e);
		} else if (e instanceof java.net.UnknownHostException) {
			return setResCode(t, "SYS-101");
		} else if (e instanceof java.net.UnknownServiceException) {
			return setResCode(t, "SYS-101");
		} else if (e instanceof java.sql.SQLException) {
			return setResCode(t, "SYS-102");
		} else if (e instanceof java.sql.SQLFeatureNotSupportedException) {
			return setResCode(t, "SYS-102");
		} else if (e instanceof DuplicateKeyException) {
			return setResCode(t, "SYS-109");
		} else if (e instanceof IOException) {
			return setResCode(t, "SYS-103");
		} else {
			t.setErrMsg(getStackTraceToString(e)); // 上线要去掉
			return setResCode(t, "SYS-104");
		}
	}

	public static String getStackTraceToString(Throwable t) {
		StringWriter sw = new StringWriter();
		t.printStackTrace(new PrintWriter(sw, true));
		return sw.getBuffer().toString();
	}
	
	/**
	 * @Description: TODO
	 */
	protected ResponseMsg<?> setResCode(ResponseMsg<?> t, String code) {
		RetCodeManager.getGlobal().setErroCode(t, code);
		return t;
	}

	protected ResponseMsg<?> setFailRet(ResponseMsg<?> t, String retMsg) {
		return setFailRet(t, fail, retMsg);
	}

	protected ResponseMsg<?> setFailRet(ResponseMsg<?> t, String retCode, String retMsg) {
		t.setRetCode(retCode);
		t.setRetMsg(retMsg);
		return t;
	}

	/**
	 * @Title: setResCode
	 * @Description: 设置返回码
	 */
	protected ResponseMsg<?> setResCode(ResponseMsg<?> t, JpException e) {
		t.setRetCode(e.getErrCode());
		t.setRetMsg(e.getErrMsg());
		return t;
	}
}
