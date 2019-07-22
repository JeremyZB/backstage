package com.bobo.framework.mybatis;

import java.sql.Statement;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
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
import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

/**
 * 
 * mybatis 模糊查找中% 拦截器，占时只支持 like concat("%",#{keyWord},"%") 形式的写法。
 * 
 *
 */
@Intercepts(
{ @Signature(type = StatementHandler.class, method = "query", args =
		{ Statement.class, ResultHandler.class }) })
public class LikeInterceptor implements Interceptor
{

	private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
	private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
	private static final ReflectorFactory DEFAULT_reflectorFactory = new DefaultReflectorFactory();

	@Override
	public Object intercept(Invocation invocation) throws Throwable
	{

		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
		MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY,
				DEFAULT_reflectorFactory);

		// 分离代理对象链(由于目标类可能被多个拦截器拦截，从而形成多次代理，通过下面的两次循环可以分离出最原始的的目标类)
		while (metaStatementHandler.hasGetter("h"))
		{
			Object object = metaStatementHandler.getValue("h");
			metaStatementHandler = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_reflectorFactory);
		}
		// 分离最后一个代理对象的目标类
		while (metaStatementHandler.hasGetter("target"))
		{
			Object object = metaStatementHandler.getValue("target");
			metaStatementHandler = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_reflectorFactory);
		}
		BoundSql boundSql = statementHandler.getBoundSql();
		MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
		/***********************************
		 * log start
		 *****************************/
		// String sqlId = mappedStatement.getId();
		// Configuration configuration = mappedStatement.getConfiguration();
		// String showSql = getSql(configuration, boundSql, sqlId);
		// System.out.println(showSql);

		/***********************************
		 * log end
		 *****************************/
		// 判断语句是否为select 开头
		String sql = boundSql.getSql();
		String lowerCaseSql = sql.toLowerCase(); // 全部小写的sql语句
		if (!lowerCaseSql.startsWith("select"))
			return invocation.proceed();
		// 判断语句是否包含like
		if (!lowerCaseSql.toLowerCase().contains("like"))
			return invocation.proceed();

		boundSql = getBoundSql(mappedStatement.getConfiguration(), boundSql);
		metaStatementHandler.setValue("delegate.boundSql.sql", boundSql.getSql());
		return invocation.proceed();
	}

	/**
	 * @Title: getBoundSql
	 * @Description: TODO
	 */
	public static BoundSql getBoundSql(Configuration configuration, BoundSql boundSql)
	{
		Object parameterObject = boundSql.getParameterObject();

		DynamicContext context = new DynamicContext(configuration, parameterObject);
		SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(configuration);
		Class<?> parameterType = parameterObject == null ? Object.class : parameterObject.getClass();

		String sql = modifyLikeSql(showSql(configuration, boundSql), parameterObject);

		SqlSource sqlSource = sqlSourceParser.parse(sql, parameterType, context.getBindings());

		BoundSql newBoundSql = sqlSource.getBoundSql(parameterObject);
		for (Map.Entry<String, Object> entry : context.getBindings().entrySet())
		{
			newBoundSql.setAdditionalParameter(entry.getKey(), entry.getValue());
		}
		return newBoundSql;
	}

	/**
	 * @Title: modifyLikeSql
	 * @Description: 生成最后的like语句
	 */
	@SuppressWarnings(
	{ "unchecked", "rawtypes" })
	public static String modifyLikeSql(String sql, Object parameterObject)
	{

		if (parameterObject instanceof Map)
		{
		} else
		{
			return sql;
		}
		String reg = "\\blike\\b.concat\\(.*?\\)";
		Pattern pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(sql);

		List<String> replaceFiled = new ArrayList<String>();

		while (matcher.find())
		{
			// int n = matcher.groupCount();
			// for (int i = 0; i <= n; i++) {
			// String output = matcher.group(i);
			// if (output != null) {
			// String key = getParameterKey(output);
			// if (replaceFiled.indexOf(key) < 0) {
			// replaceFiled.add(key);
			// }
			// }
			// }
			String output = matcher.group();
			if (output != null)
			{
				String key = getParameterKey(output);
				if (replaceFiled.indexOf(key) < 0)
				{
					replaceFiled.add(key);
				}
			}
		}
		// 修改参数
		Map<String, Object> paramMab = (Map) parameterObject;
		for (String key : replaceFiled)
		{
			Object val = paramMab.get(key);
			if (val != null && val instanceof String && (val.toString().contains("%") || val.toString().contains("_")))
			{
				val = val.toString().replaceAll("%", "\\%").replaceAll("_", "\\_");
				paramMab.put(key.toString(), val);
			}

		}
		return sql;
	}

	private static String getParameterKey(String input)
	{
		String key = "";
		String[] temp = input.split("#");
		if (temp.length > 1)
		{
			key = temp[1];
			key = key.replace("{", "").replace("}", "").split(",")[0];
		}
		return key.trim();
	}

	/**
	 * @Title: showSql
	 * @Description: 拼装为最初始sql
	 */
	public static String showSql(Configuration configuration, BoundSql boundSql)
	{
		Object parameterObject = boundSql.getParameterObject();
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
		if (parameterMappings.size() > 0 && parameterObject != null)
		{
			TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
			if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass()))
			{

			} else
			{
				MetaObject metaObject = configuration.newMetaObject(parameterObject);
				for (ParameterMapping parameterMapping : parameterMappings)
				{
					String propertyName = parameterMapping.getProperty();
					if (metaObject.hasGetter(propertyName))
					{
						sql = sql.replaceFirst("\\?", "#{" + propertyName + "}");
					} else if (boundSql.hasAdditionalParameter(propertyName))
					{
						sql = sql.replaceFirst("\\?", "#{" + propertyName + "}");
					}
				}
			}
		}
		return sql;
	}

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
				sql = sql.replaceFirst("\\?", getParameterValue(parameterObject));

			} else
			{
				MetaObject metaObject = configuration.newMetaObject(parameterObject);
				for (ParameterMapping parameterMapping : parameterMappings)
				{
					String propertyName = parameterMapping.getProperty();
					if (metaObject.hasGetter(propertyName))
					{
						Object obj = metaObject.getValue(propertyName);
						sql = sql.replaceFirst("\\?", getParameterValue(obj));
					} else if (boundSql.hasAdditionalParameter(propertyName))
					{
						Object obj = boundSql.getAdditionalParameter(propertyName);
						sql = sql.replaceFirst("\\?", getParameterValue(obj));
					}
				}
			}
		}
		return sql;
	}

	public static String getSql(Configuration configuration, BoundSql boundSql, String sqlId)
	{
		String sql = showLogSql(configuration, boundSql);
		StringBuilder str = new StringBuilder(100);
		str.append("===========================================");
		str.append("[");
		str.append(sqlId);
		str.append("]");
		str.append("===========================================");
		str.append("\n");
		str.append(sql);
		str.append("\n");
		return str.toString();
	}

	private static String getParameterValue(Object obj)
	{
		String value = null;
		if (obj instanceof String)
		{
			value = "'" + obj.toString() + "'";
		} else if (obj instanceof Date)
		{
			DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
			value = "'" + formatter.format(new Date()) + "'";
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
