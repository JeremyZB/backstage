package com.bobo.framework.mybatis.dbo;

import java.io.Serializable;

import com.bobo.framework.mybatis.IMyBatisDao;
import com.bobo.framework.mybatis.crud.IDaoControl;
import com.bobo.framework.utils.JpException;
import com.bobo.framework.utils.SpringUtils;

/**
 * @ClassName: DataOperateBean
 * @Description: 数据库操作javabean
 * @author: hanson huang
 */
public class DataOperateBean extends DataOperateAbstractBean {		
	
	private static final long serialVersionUID = 1L;
	
	//数据操作类型
	protected String dbOperation;	
		
	//被操作对象数据（model），同时用于获取model对应的dao
	protected Object data;
	
	//被操作对象数据的主键，根据主键进行删除时需要设置该字段
	protected Serializable primary;
	
	//为保证能够正常序列化，将iMyBatisDao类型改为String，在DataCommit中使用SpringUtil进行bean获取
	protected String iMyBatisDao;
	
	//自定义操作的SQL语句名称
	protected String statementName;
	
	//自定义操作的SQL参数对象
	protected Object parameterObject;
	
	//操作对象的key，用于获取返回结果集合中对应的对象
	protected String dataKey;
	
	//该对象是否需要检查执行结果
	protected boolean isCheckResult;
	
	//该对象为执行的预期结果，如果isCheckResult为true则expectValue为必填
	protected Integer expectValue;
	
	/**
	 * 默认构造方法，需要自己使用setter进行赋值
	 */
	public DataOperateBean(){
		
	}
		
	/**
	 * 如果是基于model进行数据库操作可使用该构造方法
	 * @param data
	 * @param dbOperation
	 * @param primary 主要适用于根据主键进行delete操作，insert、update操作可设置为null
	 * @param dataKey 主要用于标识操作对象，便于从执行结果中进行查找，如果使用默认值可设置为null
	 * @param isCheckResult 如果希望对操作对象的执行结果进行检查，该值可设置为true，同时expectValue为必填
	 * @param expectValue
	 */
	public DataOperateBean(String dbOperation, Object data, Serializable primary, String dataKey, boolean isCheckResult, Integer expectValue){
		this.data = data;
		this.dbOperation = dbOperation;
		if(dbOperation == null || 
				!DataOperateType.INSERT.equals(dbOperation) && !DataOperateType.UPDATE.equals(dbOperation)
				&& !DataOperateType.DELETE.equals(dbOperation)){
			throw new JpException("SYS-102", "DOB组件异常：dbOperation不支持");
		}
		this.primary = primary;
		this.dataKey = dataKey;
		this.isCheckResult = isCheckResult;
		if(isCheckResult){
			if(expectValue == null){
				throw new JpException("SYS-102", dataKey + "DOB组件异常：被设置为需要对结果进行检查，请设置预期结果（expectValue）");
			}
		}
		this.expectValue = expectValue;
	}
	
	/**
	 * 如果是基于model进行数据库操作可使用该简便构造方法
	 * @param dbOperation
	 * @param data
	 */
	public DataOperateBean(String dbOperation, Object data){
		this(dbOperation, data, null, null, false, null);		
	}
	
	/**
	 * 如果是基于自定义sql进行数据库操作可使用该构造方法
	 * @param dbOperation
	 * @param iMyBatisDao
	 * @param statementName
	 * @param parameterObject
	 * @param dataKey
	 * @param isCheckResult
	 * @param expectValue
	 */
	public DataOperateBean(String dbOperation, String iMyBatisDao, String statementName, Object parameterObject, String dataKey, boolean isCheckResult, Integer expectValue){
		this.dbOperation = dbOperation;
		if(dbOperation == null || !DataOperateType.CUSTOM_INSERT.equals(dbOperation)
				&& !DataOperateType.CUSTOM_UPDATE.equals(dbOperation) && !DataOperateType.CUSTOM_DELETE.equals(dbOperation)
				&& !DataOperateType.CUSTOM_SQL.equals(dbOperation)){
			throw new JpException("SYS-102", "DOB组件异常：dbOperation不支持");
		}
		this.iMyBatisDao = iMyBatisDao;
		this.statementName = statementName;
		this.parameterObject = parameterObject;
		this.dataKey = dataKey;
		this.isCheckResult = isCheckResult;
		if(isCheckResult){
			if(expectValue == null){
				throw new JpException("SYS-102", "DOB组件异常：" + dataKey + "被设置为需要对结果进行检查，请设置预期结果（expectValue）");
			}
		}
		this.expectValue = expectValue;
	}		
	
	/**
	 * 如果是基于自定义sql进行数据库操作可使用该简便构造方法
	 * @param dbOperation
	 * @param iMyBatisDao
	 * @param statementName
	 * @param parameterObject
	 */
	public DataOperateBean(String dbOperation, String iMyBatisDao, String statementName, Object parameterObject){
		this(dbOperation, iMyBatisDao, statementName, parameterObject, null, false, null);
	}
	
	public String getiMyBatisDao() {
		return iMyBatisDao;
	}

	public void setiMyBatisDao(String iMyBatisDao) {
		this.iMyBatisDao = iMyBatisDao;
	}

	public String getStatementName() {
		return statementName;
	}

	public void setStatementName(String statementName) {
		this.statementName = statementName;
	}

	public Object getParameterObject() {
		return parameterObject;
	}

	public void setParameterObject(Object parameterObject) {
		this.parameterObject = parameterObject;
	}

	public String getDbOperation() {
		return dbOperation;
	}

	public void setDbOperation(String dbOperation) {
		if(dbOperation == null || 
				!DataOperateType.INSERT.equals(dbOperation) && !DataOperateType.UPDATE.equals(dbOperation)
				&& !DataOperateType.DELETE.equals(dbOperation) && !DataOperateType.CUSTOM_INSERT.equals(dbOperation)
				&& !DataOperateType.CUSTOM_UPDATE.equals(dbOperation) && !DataOperateType.CUSTOM_DELETE.equals(dbOperation)
				&& !DataOperateType.CUSTOM_SQL.equals(dbOperation)){
			throw new JpException("SYS-102", "DOB组件异常：dbOperation不支持");
		}
		this.dbOperation = dbOperation;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Serializable getPrimary() {
		return primary;
	}

	public void setPrimary(Serializable primary) {
		this.primary = primary;
	}

	public String getDataKey() {
		return dataKey;
	}

	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
	}

	public boolean isCheckResult() {
		return isCheckResult;
	}

	public void setCheckResult(boolean isCheckResult) {
		this.isCheckResult = isCheckResult;
	}

	public Integer getExpectValue() {
		return expectValue;
	}

	public void setExpectValue(Integer expectValue) {
		this.expectValue = expectValue;
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
				throw new JpException("SYS-102", "DOB组件异常：dbOperation不支持");
			}
			
			//通过model和primary匹配标准mapper文件进行执行
			if(DataOperateType.INSERT.equals(dbOperation)
					|| DataOperateType.UPDATE.equals(dbOperation)
					|| DataOperateType.DELETE.equals(dbOperation)) {
				//获取对象数据，如果要操作的数据不存在抛出异常
				Object data = getData();
				if(data == null){
					throw new JpException("SYS-102", "DOB组件异常：data不能为空");
				}
				
				//获取要操作数据的主键值，如果是删除操作必须要主键，否则抛出异常
				Serializable primary = getPrimary();
				if(DataOperateType.DELETE.equals(dbOperation) && primary == null){
//							RetCodeManager.getGlobal().throwException("SYS-102");
					throw new JpException("SYS-102", "DOB组件异常：删除操作primary不能为空");
				}
				
				//获取包含包路径的model类名和dao类名
				String dataClassName = data.getClass().getName();		
				String daoClassName = dataClassName.replaceAll(".model.", ".dao.impl.") + "DaoImpl";
				IDaoControl<?, Serializable> daoInstance = (IDaoControl<?, Serializable>) SpringUtils.getBean(Class.forName(daoClassName));
				//获取不包含包路径的model类名和dao类名
//						String dataClassNameSimple = data.getClass().getSimpleName();
//						String daoClassNameSimple = dataClassNameSimple + "DaoImpl";			
//						IDaoControl<?, Serializable> daoInstance = SpringUtils.getBean(toLowerCaseFirstOne(daoClassNameSimple));
				
				switch(dbOperation){
					case DataOperateType.INSERT:
						result = daoInstance.saveObject(data);
						break;
					case DataOperateType.DELETE:
						result = daoInstance.deleteObjectById(primary);
						break;
					case DataOperateType.UPDATE:
						result = daoInstance.updateObjectById(data);
						break;
					default:
						break;
				}
			}
			
			//匹配自定义mapper文件进行执行
			if(DataOperateType.CUSTOM_INSERT.equals(dbOperation)				
					|| DataOperateType.CUSTOM_UPDATE.equals(dbOperation)
					|| DataOperateType.CUSTOM_DELETE.equals(dbOperation)
					|| DataOperateType.CUSTOM_SQL.equals(dbOperation)) {
				//获取IMyBatisDao对象，如果不存在抛出异常
				IMyBatisDao iMyBatisDao = SpringUtils.getBean(getiMyBatisDao());
				if(iMyBatisDao == null){
					throw new JpException("SYS-102", "DOB组件异常：执行自定义sql时iMyBatisDao不能为空");
				}
				
				//获取自定义操作的SQL语句名称，如果不存在抛出异常
				String statementName = getStatementName();
				if(statementName == null || "".equals(statementName)){
					throw new JpException("SYS-102", "DOB组件异常：执行自定义sql时istatementName不能为空");
				}
				
				//获取自定义操作的SQL参数对象
				Object parameterObject = getParameterObject();
							
				switch(dbOperation){
					case DataOperateType.CUSTOM_INSERT:
						result = iMyBatisDao.insert(statementName, parameterObject);
						break;
					case DataOperateType.CUSTOM_DELETE:			
						result = iMyBatisDao.delete(statementName, parameterObject);
						break;
					case DataOperateType.CUSTOM_UPDATE:
						result = iMyBatisDao.update(statementName, parameterObject);
						break;
					case DataOperateType.CUSTOM_SQL:
						result = iMyBatisDao.excuteForSql(statementName, parameterObject);
						break;
					default:
						break;
				}
			}
		}catch(Exception e){
			throw e;
		}
				
		return result;
	}
	
	

}
