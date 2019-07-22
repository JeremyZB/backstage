package com.bobo.framework.targets;

import java.io.Serializable;

public class RouteBean implements Serializable {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;

	private String coreName;
	private String txId;
	// 原方法不再使用 也不路由到新的方法
	private boolean deprecated;

	private String toCoreName;
	private String toTxId;

	public boolean isDeprecated() {
		return deprecated;
	}

	public void setDeprecated(boolean deprecated) {
		this.deprecated = deprecated;
	}

	public String getCoreName() {
		return coreName;
	}

	public void setCoreName(String coreName) {
		this.coreName = coreName;
	}

	public String getTxId() {
		return txId;
	}

	public void setTxId(String txId) {
		this.txId = txId;
	}

	public String getToCoreName() {
		return toCoreName;
	}

	public void setToCoreName(String toCoreName) {
		this.toCoreName = toCoreName;
	}

	public String getToTxId() {
		return toTxId;
	}

	public void setToTxId(String toTxId) {
		this.toTxId = toTxId;
	}

	@Override
	public String toString()
	{
		return "RouteBean [coreName=" + coreName + ", txId=" + txId + ", deprecated=" + deprecated + ", toCoreName=" + toCoreName + ", toTxId=" + toTxId + "]";
	}
	
	

}
