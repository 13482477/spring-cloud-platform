package com.siebre.basic.monitor;

import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.siebre.basic.utils.PropertiesUtil;

/**
 * 
 * @author 李志强
 *
 */
public class GraphitePropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
//	public static final String ip = NetUtils.getLocalHost();
	public static final String appName = (String) PropertiesUtil.getProperty("disconf.properties").get("disconf.app");
//	public static final String metricsPrefix = appName + "." + ip.replaceAll("\\.", "_");

	public void setProperties() {
		Properties properties = new Properties();
//		properties.put("metricsPrefix", metricsPrefix);
		setProperties(properties);
	}
}