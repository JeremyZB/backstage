package com.bobo.framework.mybatis;

import java.sql.DatabaseMetaData;

import org.apache.ibatis.session.RowBounds;

/**
 * 获取数据库方言，根据数据库返回日期语句的方式和方法util类
 * 
 * @author huangguan
 *
 */
public class DataBaseDialectUtils {
	/**
	 * 分页方言sql
	 * 
	 * @param dbv
	 * @param d
	 * @return
	 */
	public static String dbPageSql(DatabaseMetaData dbMeta, String sql, int skipResults, int maxResults) throws Exception {
		String dbName = dbMeta.getDatabaseProductName();
		if ("MySQL".equals(dbName)) {

			String pageSql = "";

			if (maxResults == (RowBounds.NO_ROW_LIMIT - 1))// 自定义分页，没有pageNo
			{
				pageSql = sql + " limit " + skipResults;
			} else {
				int pagesize = (maxResults - (skipResults - 1));
				if (skipResults > maxResults) {
					pagesize = 0;
				}
				pageSql = sql + " limit " + (skipResults - 1) + " , " + pagesize;
			}

			return pageSql;
		}

		if ("Oracle".equals(dbName)) {
			String pageSql = "SELECT * FROM (SELECT page.*, ROWNUM AS rn FROM (" + sql + ") page) WHERE rn BETWEEN " + (skipResults) + " AND " + maxResults;
			return pageSql;
		}
		return null;
	}

	public static String dbCountSql(DatabaseMetaData dbMeta, String sql) throws Exception {
		String dbName = dbMeta.getDatabaseProductName();
		if ("Oracle".equals(dbName)) {
			return "SELECT count(1) FROM (" + sql + ")";
		} else {
			return "select count(*) from (" + sql + ") as total";
		}
	}

}
