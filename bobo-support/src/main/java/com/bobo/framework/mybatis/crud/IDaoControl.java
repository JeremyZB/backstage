package com.bobo.framework.mybatis.crud;

import java.io.Serializable;
import java.util.List;

import com.bobo.framework.mybatis.Page;

/**
 * @ClassName: ControlMethod
 * @Description: TODO
 * @author: leven
 * @date: 2016年8月2日 下午3:35:04
 * @param <M>
 * @param <PK>
 */
public interface IDaoControl<M, PK extends Serializable> {
	
	
	/**
	 * @Title: selectByPrimaryKey
	 * @Description: TODO
	 * @param id
	 * @return
	 * @return: M
	 */
	public M 					getObjectById(PK id);
	public List<M> 				getListByModel(Object m);
	public Page<M> 				getPageListByModel(Object m, int pagesize, int pageno);
	public M 					getObjectByModel(Object m);
	public Integer 				saveObject(Object m);
	public Integer 				updateObjectById(Object m);
	public Integer 				deleteObjectById(PK id);
}
