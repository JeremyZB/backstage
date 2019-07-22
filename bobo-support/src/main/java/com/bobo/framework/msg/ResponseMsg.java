package com.bobo.framework.msg;

import java.io.Serializable;
import java.lang.reflect.Field;

import com.bobo.framework.mybatis.dbo.DataOperateList;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @ClassName: BaseResponseMsg
 * @Description: 消息输出基类
 * @author: leven
 * @date: 2016�?9�?21�? 下午4:59:43
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
	
	/**
	 * �?要进行操作的数据清单
	 */
	@JsonIgnore
	protected DataOperateList dataOperateList;	

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

	@SuppressWarnings("unchecked")
    public void setResult(T result) {
	    
	    if(null != result)
	    {
	        Field[] fields = result.getClass().getDeclaredFields();
	        
	        if(fields.length > 0)
	        {
	            if(fields.length == 1 && fields[0].getName().equals("serialVersionUID"))
	            {
	                this.result = (T) "";
	            }
	            else
	            {
	                this.result = result;
	            }
	            
	        }
	    }
	    else
	    {
	        this.result = result;
	    }
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
	
	public DataOperateList getDataOperateList() {
		return dataOperateList;
	}

	public void setDataOperateList(DataOperateList dataOperateList) {
		this.dataOperateList = dataOperateList;
	}

}
