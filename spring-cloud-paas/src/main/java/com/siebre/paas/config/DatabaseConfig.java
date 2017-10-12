package com.siebre.paas.config;

import com.siebre.basic.db.DynamicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhonelee on 2017/7/17.
 */
@Configuration
public class DatabaseConfig {


    @Bean
    public DynamicDataSource dataSource(@Autowired DataSource master, @Autowired List<DataSource> slaves) {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setMaster(master);
        dynamicDataSource.setSlaves(slaves);
        return dynamicDataSource;
    }

    @ConditionalOnClass(org.apache.tomcat.jdbc.pool.DataSource.class)
    @ConditionalOnProperty(name = "spring.database.type", havingValue = "org.apache.tomcat.jdbc.pool.DataSource")
    @EnableConfigurationProperties(DataBaseProperties.class)
    static class TomcatDataSourceConfig {

        @Autowired
        private DataBaseProperties dataBaseProperties;

        @Bean
        public org.apache.tomcat.jdbc.pool.DataSource master() {
            org.apache.tomcat.jdbc.pool.DataSource master = this.buildDataSource(this.dataBaseProperties.getMaster());
            return master;
        }

        @Bean
        public List<org.apache.tomcat.jdbc.pool.DataSource> slaves() {
            List<org.apache.tomcat.jdbc.pool.DataSource> slaves = new ArrayList<org.apache.tomcat.jdbc.pool.DataSource>();

            for (DataBaseProperties slaveProperties : this.dataBaseProperties.getSlaves()) {
                org.apache.tomcat.jdbc.pool.DataSource slave = this.buildDataSource(slaveProperties);
                slaves.add(slave);
            }

            return slaves;
        }

        private org.apache.tomcat.jdbc.pool.DataSource buildDataSource(DataBaseProperties dataSourceProperties) {
            org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
            dataSource.setDriverClassName(dataBaseProperties.getDriverClassName());
            dataSource.setUrl(dataSourceProperties.getUrl());
            dataSource.setUsername(dataSourceProperties.getUsername());
            dataSource.setPassword(dataSourceProperties.getPassword());
            dataSource.setInitialSize(dataSourceProperties.getInitialSize());
            dataSource.setMinIdle(dataSourceProperties.getMinIdle());
            dataSource.setMaxIdle(dataSourceProperties.getMaxIdle());
            dataSource.setMaxActive(dataSourceProperties.getMaxActive());
            dataSource.setJdbcInterceptors(dataSourceProperties.getJdbcInterceptors());
            dataSource.setValidationQuery(dataSourceProperties.getValidationQuery());
            dataSource.setTestOnBorrow(dataSourceProperties.getTestOnBorrow());
            dataSource.setTestWhileIdle(dataSourceProperties.getTestWhileIdle());
            dataSource.setTestOnReturn(dataSourceProperties.getTestOnReturn());
            dataSource.setDefaultAutoCommit(dataSourceProperties.getDefaultAutoCommit());
            dataSource.setTimeBetweenEvictionRunsMillis(dataSourceProperties.getTimeBetweenEvictionRunsMillis());
            dataSource.setMinEvictableIdleTimeMillis(dataSourceProperties.getMinEvictableIdleTimeMillis());
            return dataSource;
        }




    }

    @ConfigurationProperties(prefix = "spring.database")
    public static class DataBaseProperties{

        private DataBaseProperties master;

        private DataBaseProperties[] slaves;

        private Class<? extends DataSource> type;

        private String driverClassName;

        private String url;

        private String username;

        private String password;

        private Integer initialSize;

        private Integer minIdle;

        private Integer maxIdle;

        private Integer maxActive;

        private String jdbcInterceptors;

        private String validationQuery;

        private Boolean defaultAutoCommit;

        private Integer timeBetweenEvictionRunsMillis;

        private Integer minEvictableIdleTimeMillis;

        private Boolean testWhileIdle;

        private Boolean testOnBorrow;

        private Boolean testOnReturn;

        private Integer maxPoolPreparedStatementPerConnectionSize = 20;

        private Boolean poolPreparedStatements = Boolean.TRUE;

        public DataBaseProperties getMaster() {
            return master;
        }

        public void setMaster(DataBaseProperties master) {
            this.master = master;
        }

        public DataBaseProperties[] getSlaves() {
            return slaves;
        }

        public void setSlaves(DataBaseProperties[] slaves) {
            this.slaves = slaves;
        }

        public Class<? extends DataSource> getType() {
            return type;
        }

        public void setType(Class<? extends DataSource> type) {
            this.type = type;
        }

        public String getDriverClassName() {
            return driverClassName;
        }

        public void setDriverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Integer getInitialSize() {
            return initialSize;
        }

        public void setInitialSize(Integer initialSize) {
            this.initialSize = initialSize;
        }

        public Integer getMinIdle() {
            return minIdle;
        }

        public void setMinIdle(Integer minIdle) {
            this.minIdle = minIdle;
        }

        public Integer getMaxIdle() {
            return maxIdle;
        }

        public void setMaxIdle(Integer maxIdle) {
            this.maxIdle = maxIdle;
        }

        public Integer getMaxActive() {
            return maxActive;
        }

        public void setMaxActive(Integer maxActive) {
            this.maxActive = maxActive;
        }

        public String getJdbcInterceptors() {
            return jdbcInterceptors;
        }

        public void setJdbcInterceptors(String jdbcInterceptors) {
            this.jdbcInterceptors = jdbcInterceptors;
        }

        public String getValidationQuery() {
            return validationQuery;
        }

        public void setValidationQuery(String validationQuery) {
            this.validationQuery = validationQuery;
        }

        public Boolean getDefaultAutoCommit() {
            return defaultAutoCommit;
        }

        public void setDefaultAutoCommit(Boolean defaultAutoCommit) {
            this.defaultAutoCommit = defaultAutoCommit;
        }

        public Integer getTimeBetweenEvictionRunsMillis() {
            return timeBetweenEvictionRunsMillis;
        }

        public void setTimeBetweenEvictionRunsMillis(Integer timeBetweenEvictionRunsMillis) {
            this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
        }

        public Integer getMinEvictableIdleTimeMillis() {
            return minEvictableIdleTimeMillis;
        }

        public void setMinEvictableIdleTimeMillis(Integer minEvictableIdleTimeMillis) {
            this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
        }

        public Boolean getTestWhileIdle() {
            return testWhileIdle;
        }

        public void setTestWhileIdle(Boolean testWhileIdle) {
            this.testWhileIdle = testWhileIdle;
        }

        public Boolean getTestOnBorrow() {
            return testOnBorrow;
        }

        public void setTestOnBorrow(Boolean testOnBorrow) {
            this.testOnBorrow = testOnBorrow;
        }

        public Boolean getTestOnReturn() {
            return testOnReturn;
        }

        public void setTestOnReturn(Boolean testOnReturn) {
            this.testOnReturn = testOnReturn;
        }

        public Integer getMaxPoolPreparedStatementPerConnectionSize() {
            return maxPoolPreparedStatementPerConnectionSize;
        }

        public void setMaxPoolPreparedStatementPerConnectionSize(Integer maxPoolPreparedStatementPerConnectionSize) {
            this.maxPoolPreparedStatementPerConnectionSize = maxPoolPreparedStatementPerConnectionSize;
        }

        public Boolean getPoolPreparedStatements() {
            return poolPreparedStatements;
        }

        public void setPoolPreparedStatements(Boolean poolPreparedStatements) {
            this.poolPreparedStatements = poolPreparedStatements;
        }
    }

    @Bean
    public JdbcTemplate jdbcTemplate(@Autowired DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
        return jdbcTemplate;
    }



}
