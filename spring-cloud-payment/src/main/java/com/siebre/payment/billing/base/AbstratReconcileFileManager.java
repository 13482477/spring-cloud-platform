package com.siebre.payment.billing.base;

import java.io.File;
import java.util.Date;

public interface AbstratReconcileFileManager {

	/** ���ָ�����ڵ���Ķ����ļ� */
	File downloadReconcileFile(Date transDate);

	/** ���ָ��ʱ��εĶ����ļ� */
	File downloadReconcileFile(Date startDate, Date endDate);

}