package com.siebre.basic.db;

/**
 * 
 * @author 李志强
 *
 */
public class DataSourceHolder {
	private static final String MASTER = "master";
	private static final String SLAVE = "slave";
	private static final ThreadLocal<String> dataSources = new ThreadLocal<String>();

	private static void setDataSource(String dataSourceKey) {
		dataSources.set(dataSourceKey);
	}

	private static String getDataSource() {
		return (String) dataSources.get();
	}

	public static void setMaster() {
		setDataSource(MASTER);
	}

	public static void setSlave() {
		setDataSource(SLAVE);
	}

	public static boolean isMaster() {
		return MASTER.equals(getDataSource());
	}

	public static boolean isSlave() {
		return SLAVE.equals(getDataSource());
	}

	public static void clearDataSource() {
		dataSources.remove();
	}
}