package com.bobo.framework.mybatis.crud;

import java.io.Serializable;
import java.util.List;

import com.bobo.framework.mybatis.crud.IDao;
import com.bobo.framework.mybatis.IMyBatisDao;
import com.bobo.framework.mybatis.Page;

public class DaoImpl<M, PK extends Serializable> implements IDao<M, PK> {

	IMyBatisDao iMyBatisDao;

	@Override
	public M getObjectById(PK id, String className) {
		// TODO Auto-generated method stub
		return this.iMyBatisDao.excuteForSql(className + getObjectById, id);
	}

	@Override
	public List<M> getListByModel(Object m, String className) {
		// TODO Auto-generated method stub
		return this.iMyBatisDao.query(className + getListByModel, m);
	}

	@Override
	public Page<M> getPageListByModel(Object m, int pagesize, int pageno, String className) {
		// TODO Auto-generated method stub
		return this.iMyBatisDao.query(className + getPageListByModel, m, pagesize, pageno);
	}

	@Override
	public M getObjectByModel(Object m, String className) {
		// TODO Auto-generated method stub
		return this.iMyBatisDao.excuteForSql(className + getObjectByModel, m);
	}

	@Override
	public Integer saveObject(Object m, String className) {
		// TODO Auto-generated method stub
		return this.iMyBatisDao.insert(className + saveObject, m);
	}

	@Override
	public Integer updateObjectById(Object m, String className) {
		// TODO Auto-generated method stub
		return this.iMyBatisDao.update(className + updateObjectById, m);
	}

	@Override
	public Integer deleteObjectById(PK id, String className) {
		// TODO Auto-generated method stub
		return this.iMyBatisDao.delete(className + deleteObjectById, id);
	}

	public IMyBatisDao getiMyBatisDao() {
		return iMyBatisDao;
	}

	public void setiMyBatisDao(IMyBatisDao iMyBatisDao) {
		this.iMyBatisDao = iMyBatisDao;
	}

}
