package com.bobo.framework.mybatis;

import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

@Intercepts(
{ @Signature(type = StatementHandler.class, method = "query", args =
		{ Statement.class, ResultHandler.class }), @Signature(type = StatementHandler.class, method = "update", args =
		{ Statement.class }), @Signature(type = StatementHandler.class, method = "batch", args =
		{ Statement.class }) })
public class ConsoleInterceptor implements Interceptor
{

	private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
	private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
	private static final ReflectorFactory DEFAULT_reflectorFactory = new DefaultReflectorFactory();

	@Override
	public Object intercept(Invocation invocation) throws Throwable
	{
		long startTime = System.currentTimeMillis();

		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
		MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY,
				DEFAULT_reflectorFactory);
		MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
		BoundSql boundSql = statementHandler.getBoundSql();

		try
		{
			return invocation.proceed();

		} finally
		{
			String sqlId = mappedStatement.getId();
			
			if(!sqlId.equals("com.jiaparts.framework.dubbo.filter.savelog.CoreLogsMapper.insertSelective")){
				 
				long endTime = System.currentTimeMillis();
				long sqlCost = endTime - startTime;
				
				Configuration configuration = mappedStatement.getConfiguration();
				String showSql = getSql(configuration, boundSql, sqlId, sqlCost);
				System.out.println(showSql);
			}
		}
	}

	/**
	 * @Title: showSql
	 * @Description: 拼装为最初始sql
	 */

	@Override
	public Object plugin(Object target)
	{
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties arg0)
	{

	}

	/**************************** log用 ********************************/
	/**
	 * @Title: showLogSql
	 * @Description: TODO
	 */
	public static String showLogSql(Configuration configuration, BoundSql boundSql)
	{
		Object parameterObject = boundSql.getParameterObject();
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
		if (parameterMappings.size() > 0 && parameterObject != null)
		{
			TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
			if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass()))
			{
				sql = sql.replaceFirst("\\?", filterDollarStr(getParameterValue(parameterObject)));

			} else
			{
				MetaObject metaObject = configuration.newMetaObject(parameterObject);
				for (ParameterMapping parameterMapping : parameterMappings)
				{
					String propertyName = parameterMapping.getProperty();
					if (metaObject.hasGetter(propertyName))
					{
						Object obj = metaObject.getValue(propertyName);
						sql = sql.replaceFirst("\\?", filterDollarStr(getParameterValue(obj)));
					} else if (boundSql.hasAdditionalParameter(propertyName))
					{
						Object obj = boundSql.getAdditionalParameter(propertyName);
						sql = sql.replaceFirst("\\?", filterDollarStr(getParameterValue(obj)));
					}
				}
			}
		}
		return sql;
	}

	// 用于解決JAVA replace方法源代碼中$出問題
	public static String filterDollarStr(String str)
	{
		String sReturn = "";
		if (str.trim().length() != 0)
		{
			if (str.indexOf("$", 0) > -1)
			{
				while (str.length() > 0)
				{
					if (str.indexOf("$", 0) > -1)
					{
						sReturn += str.subSequence(0, str.indexOf("$", 0));
						sReturn += "\\$";
						str = str.substring(str.indexOf("$", 0) + 1, str.length());
					} else
					{
						sReturn += str;
						str = "";
					}
				}
			} else
			{
				sReturn = str;
			}
		}
		return sReturn;
	}

	public static String getSql(Configuration configuration, BoundSql boundSql, String sqlId, long sqlCost)
	{
		String sql = showLogSql(configuration, boundSql);
		StringBuilder str = new StringBuilder(100);
		str.append("===========================================");
		str.append("[");
		str.append(sqlId);
		str.append("----执行耗时[" + sqlCost + "ms]");
		str.append("]");
		str.append("===========================================");
		str.append("\n");
		str.append(sql);
		str.append("\n");

		return str.toString();
	}

	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static String getParameterValue(Object obj)
	{
		String value = null;
		if (obj instanceof String)
		{
			value = "'" + obj.toString() + "'";
		} else if (obj instanceof Date)
		{
			//DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
			value = "'" + sdf.format(obj) + "'";
		} else
		{
			if (obj != null)
			{
				value = obj.toString();
			} else
			{
				value = "";
			}

		}
		return value;
	}
}
