package com.siebre.basic.db;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.dao.support.DaoSupport;
import org.springframework.util.Assert;

/**
 * 
 * @author 李志强
 *
 */
public abstract class SqlSessionDaoSupport extends DaoSupport {
	private SqlSession sqlSession;
	private boolean externalSqlSession;

	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		if (!this.externalSqlSession) {
			this.sqlSession = new DynamicSqlSessionTemplate(new SqlSessionTemplate(sqlSessionFactory));
		}
	}

	public void setSqlSessionTemplate(DynamicSqlSessionTemplate sqlSessionTemplate) {
		this.sqlSession = sqlSessionTemplate;
		this.externalSqlSession = true;
	}

	public SqlSession getSqlSession() {
		return this.sqlSession;
	}

	protected void checkDaoConfig() {
		Assert.notNull(this.sqlSession, "Property 'sqlSessionFactory' or 'sqlSessionTemplate' are required");
	}
}