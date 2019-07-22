package com.bobo.core.model;

import javax.persistence.*;

@Table(name = "user_operate_log")
public class UserOperateLog {
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
     * 请求时间
     */
    private Long reqTime;

    /**
     * 操作名称
     */
    private String name;

    /**
     * ip地址
     */
    private String ip;

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
     * 获取请求时间
     *
     * @return reqTime - 请求时间
     */
    public Long getReqTime() {
        return reqTime;
    }

    /**
     * 设置请求时间
     *
     * @param reqTime 请求时间
     */
    public void setReqTime(Long reqTime) {
        this.reqTime = reqTime;
    }

    /**
     * 获取操作名称
     *
     * @return name - 操作名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置操作名称
     *
     * @param name 操作名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取ip地址
     *
     * @return ip - ip地址
     */
    public String getIp() {
        return ip;
    }

    /**
     * 设置ip地址
     *
     * @param ip ip地址
     */
    public void setIp(String ip) {
        this.ip = ip;
    }
}