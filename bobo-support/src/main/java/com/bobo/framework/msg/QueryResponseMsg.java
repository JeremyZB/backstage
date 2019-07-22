package com.bobo.framework.msg;

public class QueryResponseMsg<T> extends ResponseMsg<T> {
	private static final long serialVersionUID = 1L;
	/**
	 * 总记录数
	 */
	int total;
	/**
	 * �?始记录号,1为基�?
	 */
	int beginnum;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getBeginnum() {
		return beginnum;
	}

	public void setBeginnum(int beginnum) {
		this.beginnum = beginnum;
	}

}
