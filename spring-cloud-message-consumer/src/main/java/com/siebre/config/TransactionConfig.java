package com.siebre.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.siebre.basic.db.DynamicDataSource;
import com.siebre.basic.db.DynamicDataSourceTransactionManager;

@Configuration
@EnableTransactionManagement
public class TransactionConfig {

	@Bean
	@Autowired
	public DynamicDataSourceTransactionManager dynamicDataSourceTransactionManager(DynamicDataSource dataSource) {
		DynamicDataSourceTransactionManager transactionManager = new DynamicDataSourceTransactionManager();
		transactionManager.setDataSource(dataSource);
		transactionManager.setNestedTransactionAllowed(true);
		
		return transactionManager;
	}
}
