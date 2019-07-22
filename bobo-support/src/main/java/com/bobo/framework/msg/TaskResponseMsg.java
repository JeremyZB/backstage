package com.bobo.framework.msg;

import java.io.Serializable;

public class TaskResponseMsg implements Serializable {
	private static final long serialVersionUID = -5869777051464625703L;
	
	/**
	 * 线程名称
	 */
	private String threadName;

	/**
	 * 执行结果：成�?-success；失�?-fail
	 */
	private String execResult;
	
	/**
	 * 执行结果备注
	 */
	private String execRemark;

	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

	public String getExecResult() {
		return execResult;
	}

	public void setExecResult(String execResult) {
		this.execResult = execResult;
	}

	public String getExecRemark() {
		return execRemark;
	}

	public void setExecRemark(String execRemark) {
		this.execRemark = execRemark;
	}
}
