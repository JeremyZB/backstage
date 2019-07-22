package com.bobo.framework.mybatis;

import java.sql.Connection;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.bobo.framework.utils.JpException;

public class MyBatisDaoImpl extends SqlSessionDaoSupport implements IMyBatisDao {

	public void setSuperSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		super.setSqlSessionFactory(sqlSessionFactory);
	}

	@Override
	public <T> List<T> query(String statementName, Object parameterObject) {
		try {
			return getSqlSession().selectList(statementName, parameterObject);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JpException(e);
		}
	}

	public <T> Page<T> query(String statementName, Object parameterObject, int pagesize, int pageno) {
		RowBounds rbs = null;
		List<T> result;
		Page<T> page = new Page<T>();
		try {
			if (pagesize > 0) {
				int start = 0;
				int end = 0;

				if (pageno == (RowBounds.NO_ROW_LIMIT - 1))// 自定义分页，没有pageNo
				{
					start = pagesize;
					end = pageno;
				} else {
					if (pageno < 1)
						pageno = 1;

					start = (pageno - 1) * pagesize + 1;
					end = start + pagesize - 1;
				}

				rbs = new RowBounds(start, end);
				result = getSqlSession().selectList(statementName, parameterObject, rbs);
				page.setTotal(PageInterceptor.getTotalRowCount());
				page.setBeginnum(start);
			} else {
				result = getSqlSession().selectList(statementName, parameterObject);
				page.setTotal(result.size());
				page.setBeginnum(1);
			}
			page.setResult(result);
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			throw new JpException(e);
		}
	}

	@Override
	public int insert(String statementName, Object parameterObject) {
		try {
			return getSqlSession().insert(statementName, parameterObject);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JpException(e);
		}
	}

	@Override
	public int update(String statementName, Object parameterObject) {
		try {
			return getSqlSession().update(statementName, parameterObject);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JpException(e);
		}
	}

	@Override
	public int delete(String statementName, Object parameterObject) {
		try {
			return getSqlSession().delete(statementName, parameterObject);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JpException(e);
		}
	}

	@Override
	public <T> T excuteForSql(String statementName, Object parameterObject) {
		try {
			return getSqlSession().selectOne(statementName, parameterObject);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JpException(e);
		}
	}

	@Override
	public Connection getConn() {
		try {
			return getSqlSession().getConnection();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JpException(e);
		}
	}
}
