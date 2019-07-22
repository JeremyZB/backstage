package com.bobo.framework.mybatis.crud;

import java.io.Serializable;
import java.util.List;

import com.bobo.framework.mybatis.Page;


/**
 * @ClassName: DaoMethod
 * @Description: TODO
 * @author: leven
 * @date: 2016年8月2日 下午3:32:45
 * @param <M>
 * @param <PK>
 */
public interface IDao<M, PK extends Serializable> {

	final static String getObjectById = "Mapper.getObjectById";
	final static String getListByModel = "Mapper.getObjectByModel";
	final static String getPageListByModel = "Mapper.getObjectByModel";
	final static String getObjectByModel = "Mapper.getObjectByModel";
	final static String saveObject = "Mapper.saveObject";
	final static String updateObjectById = "Mapper.updateObjectById";
	final static String deleteObjectById = "Mapper.deleteObjectById";

	public M 					getObjectById(PK id, String namespace);
	public List<M> 				getListByModel(Object m, String namespace);
	public Page<M> 				getPageListByModel(Object m, int pagesize, int pageno,String namespace);
	public M 					getObjectByModel(Object m, String namespace);
	public Integer 				saveObject(Object m, String namespace);
	public Integer 				updateObjectById(Object m, String namespace);
	public Integer 				deleteObjectById(PK id, String namespace);
	
}
