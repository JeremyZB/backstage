package com.bobo.framework.msg;

import java.io.Serializable;

/**
 * @ClassName: BaseRequestMsg
 * @Description: 消息输入基类
 * @author: leven
 * @date: 2016�?9�?21�? 下午4:57:57
 */
public class RequestMsg<T> implements Serializable
{

	private static final long serialVersionUID = 1L;

	/**
	 * @Description: 请求渠道
	 */
	protected String channel;

	/**
	 * accessToken
	 */
	protected String accessToken;
	
	/**
	 * 群Id  生意�? �?0
	 */
	protected Integer grpId;

	/**
	 * 用户代码
	 */
	protected Integer userId;

	/**
	 * 机构代码
	 */
	protected Integer organId;

	/**
	 * 请求流水�?
	 */
	protected String reqSeq;

	/**
	 * ip
	 */
	protected String ip;

	/**
	 * 请求内容
	 */
	protected T param;

	/**
	 * 涉及到的数据是否提交
	 */
	protected boolean isCommit = true;

	public RequestMsg()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public RequestMsg(RequestMsg<?> req)
	{
		super();
		this.setAccessToken(req.getAccessToken());
		this.setOrganId(req.getOrganId());
		this.setReqSeq(req.getReqSeq());
		this.setUserId(req.getUserId());
		this.setChannel(req.getChannel());
		this.setCommit(true);
		this.setIp(req.getIp());
		this.setGrpId(req.getGrpId());
	}

	
	
	public Integer getGrpId() {
		return grpId;
	}

	public void setGrpId(Integer grpId) {
		this.grpId = grpId;
	}

	public boolean isCommit()
	{
		return isCommit;
	}

	public void setCommit(boolean isCommit)
	{
		this.isCommit = isCommit;
	}

	public T getParam()
	{
		return param;
	}

	public void setParam(T param)
	{
		this.param = param;
	}

	public String getAccessToken()
	{
		return accessToken;
	}

	public void setAccessToken(String accessToken)
	{
		this.accessToken = accessToken;
	}

	public Integer getUserId()
	{
		return userId;
	}

	public void setUserId(Integer userId)
	{
		this.userId = userId;
	}

	public Integer getOrganId()
	{
		return organId;
	}

	public void setOrganId(Integer organId)
	{
		this.organId = organId;
	}

	public String getReqSeq()
	{
		return reqSeq;
	}

	public void setReqSeq(String reqSeq)
	{
		this.reqSeq = reqSeq;
	}

	public void setChannel(String channel)
	{
		this.channel = channel;
	}

	public String getChannel()
	{
		return channel;
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	@Override
	public String toString()
	{
		return "RequestMsg [channel=" + channel + ", accessToken=" + accessToken + ", userId=" + userId + ", organId=" + organId + ", reqSeq=" + reqSeq
				+ ", ip=" + ip + ", param=" + param + ", isCommit=" + isCommit + "]";
	}

}
