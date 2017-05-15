package com.siebre.basic.query;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.bind.PropertyException;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class PagePlugin implements Interceptor {

	private String dialect = "";

	public Object intercept(Invocation ivk) throws Throwable {

		if (ivk.getTarget() instanceof RoutingStatementHandler) {
			RoutingStatementHandler statementHandler = (RoutingStatementHandler) ivk.getTarget();
			BaseStatementHandler delegate = (BaseStatementHandler) ReflectHelper.getValueByFieldName(statementHandler, "delegate");
			MappedStatement mappedStatement = (MappedStatement) ReflectHelper.getValueByFieldName(delegate, "mappedStatement");

			BoundSql boundSql = delegate.getBoundSql();
			Object parameterObject = boundSql.getParameterObject();

			if (parameterObject != null) {
				PageInfo pageInfo = this.getPageInfo(parameterObject);
	
				if (pageInfo != null) {
					Connection connection = (Connection) ivk.getArgs()[0];
					String sql = boundSql.getSql();
//					String countSql = "select count(0) as  total from (" + sql + ") temp ";
					String countSql = this.resolveCountSql(sql);
					PreparedStatement countStmt = connection.prepareStatement(countSql);
					// BoundSql countBS = new
					// BoundSql(mappedStatement.getConfiguration(), countSql,
					// boundSql.getParameterMappings(), parameterObject);
					setParameters(countStmt, mappedStatement, boundSql, parameterObject);
					ResultSet rs = countStmt.executeQuery();
					int count = 0;
					if (rs.next()) {
						count = rs.getInt(1);
					}
					rs.close();
					countStmt.close();
					pageInfo.setTotalResult(count);
					String pageSql = generatePageSql(sql, pageInfo);
					ReflectHelper.setValueByFieldName(boundSql, "sql", pageSql);
					this.setPageInfo(parameterObject, pageInfo);
				}
			}
		}
		return ivk.proceed();
	}
	
	@SuppressWarnings("unchecked")
	private void setPageInfo(Object parameterObject, PageInfo pageInfo) {
		if (parameterObject instanceof PageInfo) {
			parameterObject = pageInfo;
		}

		else if (parameterObject instanceof Map) {
			Map<Object, Object> objectMap = (Map<Object, Object>) parameterObject;
			for (Object key : objectMap.keySet()) {
				if (objectMap.get(key) != null && objectMap.get(key) instanceof PageInfo) {
					objectMap.put(key, pageInfo);
				}
			}
			
		} else {
			for (Field field : parameterObject.getClass().getDeclaredFields()) {
				if (PageInfo.class.isAssignableFrom(field.getType())) {
					try {
						field.setAccessible(true);
						field.set(parameterObject, pageInfo);
					} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		
	}
	
	private String resolveCountSql(String originalSql) {
		int start = StringUtils.indexOf(originalSql, "select") + 6;
		int end = StringUtils.indexOf(originalSql, "from");
		String replaceString = StringUtils.substring(originalSql, start, end);
		String result = StringUtils.replaceOnce(originalSql, replaceString, " count(*) ");
		return result;
	}

	/**
	 * Get pageinfo by parameterObject
	 * 
	 * @param parameterObject
	 * @return
	 */
	private PageInfo getPageInfo(Object parameterObject) {
		if (parameterObject instanceof PageInfo) {
			return (PageInfo) parameterObject;
		}

		else if (parameterObject instanceof Map) {
			for (Object value : ((Map<?, ?>) parameterObject).values()) {
				if (value instanceof PageInfo) {
					return (PageInfo) value;
				}
			}
		} else {
			for (Field field : parameterObject.getClass().getDeclaredFields()) {
				if (PageInfo.class.isAssignableFrom(field.getType())) {
					try {
						return (PageInfo) ReflectHelper.getValueByFieldName(parameterObject, field.getName());
					} catch (SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * DefaultParameterHandler
	 * 
	 * @param ps
	 * @param mappedStatement
	 * @param boundSql
	 * @param parameterObject
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql, Object parameterObject) throws SQLException {
		ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		if (parameterMappings != null) {
			Configuration configuration = mappedStatement.getConfiguration();
			TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
			MetaObject metaObject = parameterObject == null ? null : configuration.newMetaObject(parameterObject);
			for (int i = 0; i < parameterMappings.size(); i++) {
				ParameterMapping parameterMapping = parameterMappings.get(i);
				if (parameterMapping.getMode() != ParameterMode.OUT) {
					Object value;
					String propertyName = parameterMapping.getProperty();
					PropertyTokenizer prop = new PropertyTokenizer(propertyName);
					if (parameterObject == null) {
						value = null;
					} else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
						value = parameterObject;
					} else if (boundSql.hasAdditionalParameter(propertyName)) {
						value = boundSql.getAdditionalParameter(propertyName);
					} else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX) && boundSql.hasAdditionalParameter(prop.getName())) {
						value = boundSql.getAdditionalParameter(prop.getName());
						if (value != null) {
							value = configuration.newMetaObject(value).getValue(propertyName.substring(prop.getName().length()));
						}
					} else {
						value = metaObject == null ? null : metaObject.getValue(propertyName);
					}
					@SuppressWarnings("rawtypes")
					TypeHandler typeHandler = parameterMapping.getTypeHandler();
					if (typeHandler == null) {
						throw new ExecutorException("There was no TypeHandler found for parameter " + propertyName + " of statement " + mappedStatement.getId());
					}
					typeHandler.setParameter(ps, i + 1, value, parameterMapping.getJdbcType());
				}
			}
		}
	}

	/**
	 * 
	 * 
	 * @param Generate
	 *            page sql for different database;
	 * @param page
	 * @return
	 */
	private String generatePageSql(String sql, PageInfo page) {
		if (page != null && (dialect != null || !("").equals(dialect))) {
			StringBuffer pageSql = new StringBuffer();
			if ("mysql".equals(dialect)) {
				pageSql.append(sql);
				pageSql.append(" limit " + page.getCurrentResult() + "," + page.getShowCount());
			} else if ("oracle".equals(dialect)) {
				pageSql.append("select * from (select tmp_tb.*,ROWNUM row_id from (");
				pageSql.append(sql);
				pageSql.append(")  tmp_tb where ROWNUM<=");
				pageSql.append(page.getCurrentResult() + page.getShowCount());
				pageSql.append(") where row_id>");
				pageSql.append(page.getCurrentResult());
			}
			return pageSql.toString();
		} else {
			return sql;
		}
	}

	public Object plugin(Object arg0) {
		return Plugin.wrap(arg0, this);
	}

	public void setProperties(Properties p) {
		dialect = p.getProperty("dialect");
		if (dialect == null || dialect.equals("")) {
			try {
				throw new PropertyException("dialect property is not found!");
			} catch (PropertyException e) {
				e.printStackTrace();
			}
		}
		if (dialect == null || dialect.equals("")) {
			try {
				throw new PropertyException("pageSqlId property is not found!");
			} catch (PropertyException e) {
				e.printStackTrace();
			}
		}
	}

}