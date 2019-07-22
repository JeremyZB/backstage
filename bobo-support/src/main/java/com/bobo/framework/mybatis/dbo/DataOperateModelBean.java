package com.bobo.framework.mybatis.dbo;

import java.io.Serializable;

import com.bobo.framework.mybatis.crud.IDaoControl;
import com.bobo.framework.utils.JpException;
import com.bobo.framework.utils.SpringUtils;

public class DataOperateModelBean extends DataOperateAbstractBean {

	private static final long serialVersionUID = 1L;
	
	//数据操作类型
	protected String dbOperation;
		
	//被操作对象数据（model），同时用于获取model对应的dao
	protected Object model;
	
	//被操作对象数据的主键，根据主键进行删除时需要设置该字段
	protected Serializable primary;
	
	//默认构造器
	public DataOperateModelBean() {
	}
	
	//简便构造器1
	public DataOperateModelBean(String dbOperation, Object model) {
		if(!DataOperateType.checkDataOperateModelType(dbOperation)){
			throw new JpException("SYS-102", "DOB组件异常：dbOperation不支持");
		}
		if(model == null){
			throw new JpException("SYS-102", "DOB组件异常：model不能为null");
		}
		
		this.dbOperation = dbOperation;
		this.model = model;
	}
	
	//简便构造器2
	public DataOperateModelBean(String dbOperation, Object model, Serializable primary) {
		this(dbOperation, model);
		this.primary = primary;
	}
	
	//全参数构造器
	public DataOperateModelBean(String dbOperation, Object model,
			Serializable primary, String dataKey, boolean isCheckResult,
			Integer expectValue) {
		this(dbOperation, model, primary);
		setProperties(dataKey, isCheckResult, expectValue);
	}

	public String getDbOperation() {
		return dbOperation;
	}

	public void setDbOperation(String dbOperation) {
		this.dbOperation = dbOperation;
	}

	public Object getModel() {
		return model;
	}

	public void setData(Object model) {
		this.model = model;
	}

	public Serializable getPrimary() {
		return primary;
	}

	public void setPrimary(Serializable primary) {
		this.primary = primary;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object commit() throws Exception {
		//执行数据库交互后的结果
		Object result = null;
		try{
			//获取对象数据操作类型，如果操作类型不存在抛出异常
			String dbOperation = getDbOperation();
			if(dbOperation == null || "".equals(dbOperation)){
				throw new JpException("SYS-102", "DOB组件异常：dbOperation不能为空");
			}
			
			Object model = getModel();
			if(model == null){
				throw new JpException("SYS-102", "DOB组件异常：data不能为空");
			}
			
			//获取要操作数据的主键值，如果是删除操作必须要主键，否则抛出异常
			Serializable primary = getPrimary();
			if(DataOperateType.DataOperateModelType.DELETE.equals(dbOperation) && primary == null){
				throw new JpException("SYS-102", "DOB组件异常：删除操作primary不能为空");
			}
			
			//获取包含包路径的model类名和dao类名
			String modelClassName = model.getClass().getName();		
			String daoClassName = modelClassName.replaceAll(".model.", ".dao.impl.") + "DaoImpl";
			IDaoControl<?, Serializable> daoInstance = (IDaoControl<?, Serializable>) SpringUtils.getBean(Class.forName(daoClassName));
			
			switch(dbOperation){
				case DataOperateType.DataOperateModelType.INSERT:
					result = daoInstance.saveObject(model);
					break;
				case DataOperateType.DataOperateModelType.DELETE:			
					result = daoInstance.deleteObjectById(primary);				
					break;
				case DataOperateType.DataOperateModelType.UPDATE:
					result = daoInstance.updateObjectById(model);
					break;
				default:
					break;
			}
						
		}catch(Exception e){
			throw e;
		}
		return result;
	}

	@Override
	public Object getData() {
		return getModel();
	}	
	
}
