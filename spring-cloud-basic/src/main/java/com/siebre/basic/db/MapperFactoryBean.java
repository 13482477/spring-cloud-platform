package com.siebre.basic.db;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.Configuration;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.Assert;

/**
 * 
 * @author 李志强
 *
 * @param <T>
 */
public class MapperFactoryBean<T> extends SqlSessionDaoSupport implements FactoryBean<T> {
	private Class<T> mapperInterface;
	private boolean addToConfig = true;

	public MapperFactoryBean(Class<T> mapperInterface) {
		this.mapperInterface = mapperInterface;
	}

	public MapperFactoryBean() {
	}

	protected void checkDaoConfig() {
		super.checkDaoConfig();

		Assert.notNull(this.mapperInterface, "Property 'mapperInterface' is required");

		Configuration configuration = getSqlSession().getConfiguration();
		if ((this.addToConfig) && (!configuration.hasMapper(this.mapperInterface)))
			try {
				configuration.addMapper(this.mapperInterface);
			} catch (Exception e) {
				this.logger.error("Error while adding the mapper '" + this.mapperInterface + "' to configuration.", e);
				throw new IllegalArgumentException(e);
			} finally {
				ErrorContext.instance().reset();
			}
	}

	public T getObject() throws Exception {
		return getSqlSession().getMapper(this.mapperInterface);
	}

	public Class<T> getObjectType() {
		return this.mapperInterface;
	}

	public boolean isSingleton() {
		return true;
	}

	public void setMapperInterface(Class<T> mapperInterface) {
		this.mapperInterface = mapperInterface;
	}

	public Class<T> getMapperInterface() {
		return this.mapperInterface;
	}

	public void setAddToConfig(boolean addToConfig) {
		this.addToConfig = addToConfig;
	}

	public boolean isAddToConfig() {
		return this.addToConfig;
	}
}