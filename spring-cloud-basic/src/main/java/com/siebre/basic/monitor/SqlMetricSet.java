package com.siebre.basic.monitor;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.MetricSet;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author 李志强
 *
 */
public class SqlMetricSet implements MetricSet {
	private static final String slowQueryReport = "tomcat.jdbc:type=org.apache.tomcat.jdbc.pool.interceptor.SlowQueryReportJmx,name=jdbcPool";
	private final ObjectName objectName;
	private final MBeanServer mBeanServer;
	private static final String attributeName = "SlowQueriesCD";

	public SqlMetricSet() throws Exception {
		this(slowQueryReport, ManagementFactory.getPlatformMBeanServer());
	}

	public SqlMetricSet(String slowQueryReport, MBeanServer mBeanServer) throws Exception {
		this.objectName = new ObjectName(slowQueryReport);
		this.mBeanServer = mBeanServer;
	}

	public Map<String, Metric> getMetrics() {
		Map<String, Metric> map = new HashMap<String, Metric>();

		map.put(MetricRegistry.name("exceptionSql", new String[] { "typeCount" }), new Gauge() {
			public Integer getValue() {
				CompositeData[] cs = null;
				if (SqlMetricSet.this.mBeanServer.isRegistered(SqlMetricSet.this.objectName))
					try {
						cs = (CompositeData[]) SqlMetricSet.this.mBeanServer.getAttribute(SqlMetricSet.this.objectName, attributeName);
					} catch (Exception ignored) {
					}
				int count = 0;
				if (cs != null) {
					for (CompositeData c : cs) {
						count++;
					}
				}
				return Integer.valueOf(count);
			}
		});
		map.put(MetricRegistry.name("exceptionSql", new String[] { "failuresCount" }), new Gauge() {
			public Long getValue() {
				CompositeData[] cs = null;
				if (SqlMetricSet.this.mBeanServer.isRegistered(SqlMetricSet.this.objectName))
					try {
						cs = (CompositeData[]) SqlMetricSet.this.mBeanServer.getAttribute(SqlMetricSet.this.objectName, "SlowQueriesCD");
					} catch (Exception ignored) {
					}
				Long count = Long.valueOf(0L);
				if (cs != null) {
					for (CompositeData c : cs) {
						count = Long.valueOf(count.longValue() + ((Long) c.get("failures")).longValue());
					}
				}
				return count;
			}
		});
		return Collections.unmodifiableMap(map);
	}
}