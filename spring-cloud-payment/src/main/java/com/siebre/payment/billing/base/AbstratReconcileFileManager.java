package com.siebre.payment.billing.base;

import java.io.File;
import java.util.Date;

public interface AbstratReconcileFileManager {

	/** 根据指定日期下载对账文件 */
	File downloadReconcileFile(Date transDate);

	/** 根据指定时间段下载对账文件 */
	File downloadReconcileFile(Date startDate, Date endDate);

}