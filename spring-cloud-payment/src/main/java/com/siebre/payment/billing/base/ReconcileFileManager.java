package com.siebre.payment.billing.base;

import java.io.File;
import java.util.Date;

public interface ReconcileFileManager {

    /** 获得指定日期当天的对账文件 */
    public File downloadReconcileFile(Date transDate);

    /** 获得指定时间段的对账文件 */
    public File downloadReconcileFile(Date startDate, Date endDate);

}
