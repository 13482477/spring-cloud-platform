package com.siebre.basic.db;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * 
 * @author 李志强
 *
 */
public class DynamicSqlSessionTemplate implements SqlSession {
	private static Logger logger = LoggerFactory.getLogger(DynamicSqlSessionTemplate.class);
	private static final String SELECT = "select";
	private static final String INSERT = "insert";
	private static final String DELETE = "delete";
	private static final String UPDATE = "update";
	private SqlSessionTemplate sqlSessionTemplate;
	private final SqlSession sqlSessionProxy;

	public DynamicSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
		this.sqlSessionProxy = ((SqlSession) Proxy.newProxyInstance(SqlSessionFactory.class.getClassLoader(), new Class[] { SqlSession.class }, new SqlSessionInterceptor()));
	}

	public <T> T selectOne(String statement) {
		return this.sqlSessionProxy.selectOne(statement);
	}

	public <T> T selectOne(String statement, Object parameter) {
		return this.sqlSessionProxy.selectOne(statement, parameter);
	}

	public <E> List<E> selectList(String statement) {
		return this.sqlSessionProxy.selectList(statement);
	}

	public <E> List<E> selectList(String statement, Object parameter) {
		return this.sqlSessionProxy.selectList(statement, parameter);
	}

	public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
		return this.sqlSessionProxy.selectList(statement, parameter, rowBounds);
	}

	public <K, V> Map<K, V> selectMap(String statement, String mapKey) {
		return this.sqlSessionProxy.selectMap(statement, mapKey);
	}

	public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey) {
		return this.sqlSessionProxy.selectMap(statement, parameter, mapKey);
	}

	public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds) {
		return this.sqlSessionProxy.selectMap(statement, parameter, mapKey, rowBounds);
	}

	public void select(String statement, Object parameter, ResultHandler handler) {
		this.sqlSessionProxy.select(statement, parameter, handler);
	}

	public void select(String statement, ResultHandler handler) {
		this.sqlSessionProxy.select(statement, handler);
	}

	public void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler handler) {
		this.sqlSessionProxy.select(statement, parameter, rowBounds, handler);
	}

	public int insert(String statement) {
		return this.sqlSessionProxy.insert(statement);
	}

	public int insert(String statement, Object parameter) {
		return this.sqlSessionProxy.insert(statement, parameter);
	}

	public int update(String statement) {
		return this.sqlSessionProxy.update(statement);
	}

	public int update(String statement, Object parameter) {
		return this.sqlSessionProxy.update(statement, parameter);
	}

	public int delete(String statement) {
		return this.sqlSessionProxy.delete(statement);
	}

	public int delete(String statement, Object parameter) {
		return this.sqlSessionProxy.delete(statement, parameter);
	}

	public void commit() {
		this.sqlSessionProxy.commit();
	}

	public void commit(boolean force) {
		this.sqlSessionProxy.commit(force);
	}

	public void rollback() {
		this.sqlSessionProxy.rollback();
	}

	public void rollback(boolean force) {
		this.sqlSessionProxy.rollback(force);
	}

	public List<BatchResult> flushStatements() {
		return this.sqlSessionProxy.flushStatements();
	}

	public void close() {
		this.sqlSessionProxy.close();
	}

	public void clearCache() {
		this.sqlSessionProxy.clearCache();
	}

	public Configuration getConfiguration() {
		return this.sqlSessionProxy.getConfiguration();
	}

	public <T> T getMapper(Class<T> type) {
		return getConfiguration().getMapper(type, this);
	}

	public Connection getConnection() {
		return this.sqlSessionProxy.getConnection();
	}

	private class SqlSessionInterceptor implements InvocationHandler {
		private SqlSessionInterceptor() {
		}

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			boolean synchronizationActive = TransactionSynchronizationManager.isSynchronizationActive();
			if (synchronizationActive) {
				return method.invoke(DynamicSqlSessionTemplate.this.sqlSessionTemplate, args);
			}
			String methodName = method.getName();
			if (methodName.startsWith(SELECT)) {
				MappedStatement ms = DynamicSqlSessionTemplate.this.getConfiguration().getMappedStatement((String) args[0]);

				BoundSql boundSql = null;
				if (args.length == 1) {
					boundSql = ms.getSqlSource().getBoundSql(null);
				}
				if (args.length > 1) {
					boundSql = ms.getSqlSource().getBoundSql(args[1]);
				}
				String sql = boundSql.getSql();
				if (sql.startsWith("/*master*/")) {
					DataSourceHolder.setMaster();
					DynamicSqlSessionTemplate.logger.info("Master database is selected by hint");
				} else {
					DataSourceHolder.setSlave();
					DynamicSqlSessionTemplate.logger.info("Slaver database is selected");
				}
			} else if ((methodName.startsWith(INSERT)) || (methodName.startsWith(UPDATE)) || (methodName.startsWith(DELETE))) {
				DataSourceHolder.setMaster();
				DynamicSqlSessionTemplate.logger.info("Master database is selected");
			}
			Object result;
			try {
				result = method.invoke(DynamicSqlSessionTemplate.this.sqlSessionTemplate, args);
			} catch (Exception e) {
				throw e;
			} finally {
				DataSourceHolder.clearDataSource();
			}
			return result;
		}
	}

	@Override
	public <T> Cursor<T> selectCursor(String arg0) {
		return this.sqlSessionProxy.selectCursor(arg0);
	}

	@Override
	public <T> Cursor<T> selectCursor(String arg0, Object arg1) {
		return this.sqlSessionProxy.selectCursor(arg0, arg1);
	}

	@Override
	public <T> Cursor<T> selectCursor(String arg0, Object arg1, RowBounds arg2) {
		return this.sqlSessionProxy.selectCursor(arg0, arg1, arg2);
	}
}