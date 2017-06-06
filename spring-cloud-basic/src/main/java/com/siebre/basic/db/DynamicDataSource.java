package com.siebre.basic.db;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 
 * @author 李志强
 *
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
	private final Logger log = LoggerFactory.getLogger(getClass());

	private AtomicInteger counter = new AtomicInteger();
	private DataSource master;
	private List<DataSource> slaves = new ArrayList<DataSource>();

	protected Object determineCurrentLookupKey() {
		return null;
	}

	public void afterPropertiesSet() {
	}

	protected javax.sql.DataSource determineTargetDataSource() {
		javax.sql.DataSource returnDataSource = null;
		if (DataSourceHolder.isMaster()) {
			returnDataSource = this.master;
		} else if (DataSourceHolder.isSlave()) {
			int count = this.counter.incrementAndGet();
			if (count > 1000000) {
				this.counter.set(0);
			}
			int n = this.slaves.size();
			int index = count % n;
			returnDataSource = (javax.sql.DataSource) this.slaves.get(index);
			this.log.info("No.{} slave datasource have been chose", Integer.valueOf(index));
		} else {
			returnDataSource = this.master;
			this.log.info("Master datasource have been chose by default");
		}
		if ((returnDataSource instanceof org.apache.tomcat.jdbc.pool.DataSource)) {
			org.apache.tomcat.jdbc.pool.DataSource source = (org.apache.tomcat.jdbc.pool.DataSource) returnDataSource;
			String jdbcUrl = source.getUrl();
			this.log.info("JdbcUrl:{}", jdbcUrl);
		}
		return returnDataSource;
	}

	public DataSource getMaster() {
		return this.master;
	}

	public void setMaster(DataSource master) {
		this.master = master;
	}

	public List<DataSource> getSlaves() {
		return this.slaves;
	}

	public void setSlaves(List<DataSource> slaves) {
		this.slaves = slaves;
	}
	
	public boolean addSlave(DataSource ds) {
		return this.slaves.add(ds);
	}
	
	public boolean removeSlave(DataSource ds) {
		return this.slaves.remove(ds);
	}
	
}