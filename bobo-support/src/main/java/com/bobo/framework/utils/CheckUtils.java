package com.bobo.framework.utils;

import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import com.bobo.framework.manager.RetCodeManager;
import com.bobo.framework.service.BaseService;

/**
 * @ClassName: CheckUtils
 * @Description: 参数校验工具类
 * @author: leven
 * @date: 2016年8月15日 上午10:04:16
 */
public class CheckUtils {

	/**
	 * @Title: checkNotEmpty
	 * @Description: 非空校验
	 * @param value
	 * @param fieldName
	 * @throws ApiException
	 * @return: void
	 */
	public static void checkNull(Object v, String fName) throws Exception {
		if (v == null)
			throwException("SYS-201", fName);
		if (v instanceof String) {
			if (v.toString().trim().length() == 0)
				throwException("SYS-201", fName);
		}
		if (v instanceof List) {
			if (((List<?>) v).isEmpty())
				throwException("SYS-201", fName);
		}
	}

	public static void checkType(Object v, String errMsg, Object[] vvs) throws Exception {
		boolean b = false;
		for (Object object : vvs) {
			if (String.valueOf(object).equals(String.valueOf(v))) {
				b = true;
				break;
			}
		}
		if (!b) {
			throwExceptionMsg(errMsg + " " + ArrayUtils.toString(vvs).replace("{", "[").replace("}", "]"));
		}
	}

	/**
	 * @Title: checkMobile
	 * @Description: TODO
	 * @param v
	 * @throws Exception
	 * @return: void
	 */
	public static void checkMobile(Object v, String fName) throws Exception {
		checkNull(v, fName);
		String vs = v.toString().trim();
		if (!ValiUtil.isMobile(vs)) {
			throwException("SYS-202"); // 手机号码格式错误
		}
	}

	public static void checkDate(Object v, String fName) throws Exception {
		if (v != null) {
			String vs = v.toString().trim();
			if (!ValiUtil.isDate(vs)) {
				throwException("SYS-209"); // 手机号码格式错误
			}
		}

	}

	/**
	 * @Title: checkPass
	 * @Description: TODO
	 * @param txpass
	 * @param txpasscf
	 * @throws Exception
	 * @return: void
	 */
	public static void checkPass(String txpass, String txpasscf) throws Exception {
		// 二次密码输入不一致
		if (!txpass.equals(txpasscf)) {
			throwException("SYS-205"); // 二次密码输入不一致
		}
	}

	public static void throwException(String code, String fName) throws Exception {
		RetCodeManager.getGlobal().throwException(code, fName);
	}

	public static void throwException(String code) throws Exception {
		RetCodeManager.getGlobal().throwException(code);
	}

	public static void throwExceptionMsg(String msg) throws Exception {
		throw new JpException(BaseService.fail, msg);
	}
	public static void throwException(String code, String fName[])  throws Exception {
		RetCodeManager.getGlobal().throwException(code, fName);
	}
}
