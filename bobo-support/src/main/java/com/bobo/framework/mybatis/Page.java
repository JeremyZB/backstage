package com.bobo.framework.mybatis;

import java.io.Serializable;
import java.util.List;

/**
 * dao返回的包含结果集和分页属性的业务对象。
 * 
 */
public class Page<T> implements Serializable {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = -3581998084507564779L;
	/**
	 * 总记录数
	 */
	int total;
	/**
	 * 开始记录号,1为基数
	 */
	int beginnum;

	/**
	 * 结果集
	 */
	private List<T> result;

	public Page() {

	}

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

	/**
	 * 设置结果集
	 * 
	 * @param elements
	 */
	public void setResult(List<T> elements) {
		if (elements == null)
			throw new IllegalArgumentException("'result' must be not null");
		this.result = elements;
	}

	/**
	 * 当前页包含的数据
	 * 
	 * @return 当前页数据源
	 */
	public List<T> getResult() {
		return result;
	}
}
