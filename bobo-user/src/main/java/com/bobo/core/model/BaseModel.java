package com.bobo.core.model;

import java.io.Serializable;

import javax.persistence.Column;

import lombok.Data;
@Data
public abstract class BaseModel implements Serializable {
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 创建时间
     */
    @Column(name = "CreateTime")
    private Integer createTime;

    /**
     * 创建人
     */
    @Column(name = "CreateUserId")
    private Integer createUserId;

    /**
     * 修改时间
     */
    @Column(name = "UpdateTime")
    private Integer updateTime;

    /**
     * 修改人
     */
    @Column(name = "UpdateUserId")
    private Integer updateUserId;

}
