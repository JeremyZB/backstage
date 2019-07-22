package com.bobo.framework.mybatis;

import java.sql.Connection;
import java.util.List;

public interface IMyBatisDao {

	public <T> List<T> query(String statementName, Object parameterObject);

	public <T> Page<T> query(String statementName, Object parameterObject,int pagesize,int pageno);

	/**
	 * 插入记录 <功能详细描述>
	 * 
	 * @param statementName
	 * @param parameterObject
	 * @return
	 * @throws DaoException
	 *             抛出数据库访问异常
	 * @see [类、类#方法、类#成员]
	 */
	public int insert(String statementName, Object parameterObject);

	/**
	 * 更新记录 <功能详细描述>
	 * 
	 * @param statementName
	 * @param parameterObject
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public int update(String statementName, Object parameterObject);

	/**
	 * 删除记录 <功能详细描述>
	 * 
	 * @param statementName
	 * @param parameterObject
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public int delete(String statementName, Object parameterObject);

	/**
	 * 执行SQL <功能详细描述>
	 * 
	 * @param statementName
	 * @param parameterObject
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public <T> T excuteForSql(String statementName, Object parameterObject);
	
	/**
	 * 获取conn
	 * @Title: getConn
	 * @Description: TODO
	 * @return
	 * @return: Connection
	 */
	public Connection getConn();

}
