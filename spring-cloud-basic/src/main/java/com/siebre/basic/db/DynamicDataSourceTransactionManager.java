package com.siebre.basic.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;

/**
 * 
 * @author 李志强
 *
 */
public class DynamicDataSourceTransactionManager extends DataSourceTransactionManager {
	private static Logger logger = LoggerFactory.getLogger(DynamicDataSourceTransactionManager.class);
	private static final long serialVersionUID = 7160082287881717832L;

	protected void doBegin(Object transaction, TransactionDefinition definition) {
		logger.info("~~~~~~~~~~~~~~~~~~~Transaction begin~~~~~~~~~~~~~~~~~~~");
		boolean readOnly = definition.isReadOnly();
		if (readOnly) {
			DataSourceHolder.setSlave();
			logger.info("Slaver database is selected");
		} else {
			DataSourceHolder.setMaster();
			logger.info("Master database is selected");
		}
		super.doBegin(transaction, definition);
	}

	protected void doCleanupAfterCompletion(Object transaction) {
		super.doCleanupAfterCompletion(transaction);
		DataSourceHolder.clearDataSource();
		logger.info("~~~~~~~~~~~~~~~~~~~Transaction end~~~~~~~~~~~~~~~~~~~");
	}
}
