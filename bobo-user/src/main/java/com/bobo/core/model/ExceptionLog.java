package com.bobo.core.model;

import javax.persistence.*;

@Table(name = "exception_log")
public class ExceptionLog {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 请求URL
     */
    private String reqUrl;

    /**
     * 请求参数
     */
    private String reqParam;

    /**
     * 异常日志
     */
    private String remark;

    /**
     * 记录生成时间
     */
    private Long createTime;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取用户id
     *
     * @return userId - 用户id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户id
     *
     * @param userId 用户id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取请求URL
     *
     * @return reqUrl - 请求URL
     */
    public String getReqUrl() {
        return reqUrl;
    }

    /**
     * 设置请求URL
     *
     * @param reqUrl 请求URL
     */
    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl;
    }

    /**
     * 获取请求参数
     *
     * @return reqParam - 请求参数
     */
    public String getReqParam() {
        return reqParam;
    }

    /**
     * 设置请求参数
     *
     * @param reqParam 请求参数
     */
    public void setReqParam(String reqParam) {
        this.reqParam = reqParam;
    }

    /**
     * 获取异常日志
     *
     * @return remark - 异常日志
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置异常日志
     *
     * @param remark 异常日志
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取记录生成时间
     *
     * @return createTime - 记录生成时间
     */
    public Long getCreateTime() {
        return createTime;
    }

    /**
     * 设置记录生成时间
     *
     * @param createTime 记录生成时间
     */
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}