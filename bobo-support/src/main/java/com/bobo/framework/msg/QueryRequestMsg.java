package com.bobo.framework.msg;

public class QueryRequestMsg<T> extends RequestMsg<T> {
	private static final long serialVersionUID = 1L;
	/**
	 * 第几页，1为基�?
	 */
	int pageno;

	/**
	 * 分页大小�?<=0不分页，
	 */
	int pagesize = 20;

	public int getPageno() {
		return pageno;
	}

	public void setPageno(int pageno) {
		this.pageno = pageno;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

}
