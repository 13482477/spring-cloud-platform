package com.siebre.payment.billing.base;

import com.siebre.payment.entity.enums.PaymentInterfaceType;
import com.siebre.payment.entity.enums.PaymentTransactionStatus;
import com.siebre.payment.paymentchannel.service.PaymentChannelService;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
import com.siebre.payment.paymenttransaction.service.PaymentTransactionService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.zeroturnaround.zip.ZipUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

/**
 * Created by tianci.huang on 2017/6/26.
 */
public abstract class ReconcileFileManager implements AbstratReconcileFileManager {

    private Logger logger = LoggerFactory.getLogger(ReconcileFileManager.class);

    //public static String LOCAL_DIR = "/data/app/reconfile/";
    public static String LOCAL_DIR = "d:/reconfile/";

    @Autowired
    protected PaymentChannelService channelService;

    @Autowired
    protected PaymentTransactionService transactionService;

    @Override
    public File downloadReconcileFile(Date transDate) {
        return null;
    }

    @Override
    public abstract File downloadReconcileFile(Date startDate, Date endDate) throws IOException;

    protected PaymentTransaction createReconcileTransaction() {
        PaymentTransaction reconcileTransaction = new PaymentTransaction();
        reconcileTransaction.setInterfaceType(PaymentInterfaceType.RECONCILIATION);
        reconcileTransaction.setSender("iPay(" + transactionService.getLocalHostInfo() + ")");
        reconcileTransaction.setPaymentStatus(PaymentTransactionStatus.RECON_PROCESSING);
        transactionService.createTransaction(reconcileTransaction);
        return reconcileTransaction;
    }

}
