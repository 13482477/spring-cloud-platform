package com.siebre.payment.billing.base;

import java.io.File;
import java.util.Date;

/**
 * Created by tianci.huang on 2017/6/26.
 */
public class ReconcileFileManager implements AbstratReconcileFileManager {
    @Override
    public File downloadReconcileFile(Date transDate) {
        return null;
    }

    @Override
    public File downloadReconcileFile(Date startDate, Date endDate) {
        return null;
    }
}
