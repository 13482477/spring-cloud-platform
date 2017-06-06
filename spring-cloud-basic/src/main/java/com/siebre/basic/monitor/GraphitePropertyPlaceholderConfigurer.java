package com.siebre.basic.monitor;

import com.siebre.basic.utils.PropertiesUtil;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.Properties;

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