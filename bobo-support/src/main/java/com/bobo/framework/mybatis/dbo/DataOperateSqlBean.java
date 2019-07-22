package com.bobo.framework.mybatis.dbo;

import org.apache.commons.lang3.StringUtils;

import com.bobo.framework.mybatis.IMyBatisDao;
import com.bobo.framework.utils.JpException;
import com.bobo.framework.utils.SpringUtils;

public class DataOperateSqlBean extends DataOperateAbstractBean {

	private static final long serialVersionUID = 1L;
	
	//数据操作类型
	protected String dbOperation;
		
	//为保证能够正常序列化，将iMyBatisDao类型改为String，在DataCommit中使用SpringUtil进行bean获取
	protected String iMyBatisDao;
	
	//自定义操作的SQL语句名称
	protected String statementName;
	
	//自定义操作的SQL参数对象
	protected Object parameterObject;
	
	//默认构造器
	public DataOperateSqlBean() {
	}
	
	//简便构造器1
	public DataOperateSqlBean(String dbOperation, String iMyBatisDao, String statementName) {
		if(!DataOperateType.checkDataOperateSqlType(dbOperation)){
			throw new JpException("SYS-102", "DOB组件异常：dbOperation不支持");
		}
		if(StringUtils.isEmpty(iMyBatisDao)){
			throw new JpException("SYS-102", "DOB组件异常：iMyBatisDao不能为空");
		}
		if(StringUtils.isEmpty(statementName)){
			throw new JpException("SYS-102", "DOB组件异常：statementName不能为空");
		}
		
		this.dbOperation = dbOperation;
		this.iMyBatisDao = iMyBatisDao;
		this.statementName = statementName;
	}
		
	//简便构造器2
	public DataOperateSqlBean(String dbOperation, String iMyBatisDao, String statementName, Object parameterObject) {
		this(dbOperation, iMyBatisDao, statementName);
		this.parameterObject = parameterObject;
	}		
	
	//全参数构造器
	public DataOperateSqlBean(String dbOperation, String iMyBatisDao, String statementName, Object parameterObject, 
			String dataKey, boolean isCheckResult, Integer expectValue) {
		this(dbOperation, iMyBatisDao, statementName, parameterObject);
		setProperties(dataKey, isCheckResult, expectValue);
	}

	public String getDbOperation() {
		return dbOperation;
	}

	public void setDbOperation(String dbOperation) {
		this.dbOperation = dbOperation;
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
		}catch(Exception e){
			throw e;
		}
				
		return result;
	}

	@Override
	public Object getData() {
		return null;
	}
	
}
