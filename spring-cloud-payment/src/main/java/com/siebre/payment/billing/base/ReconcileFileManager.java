package com.siebre.payment.billing.base;

import com.siebre.payment.entity.enums.PaymentInterfaceType;
import com.siebre.payment.entity.enums.PaymentTransactionStatus;
import com.siebre.payment.paymentchannel.service.PaymentChannelService;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
import com.siebre.payment.paymenttransaction.service.PaymentTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.zeroturnaround.zip.ZipUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Date;

/**
 * Created by tianci.huang on 2017/6/26.
 */
public abstract class ReconcileFileManager implements AbstratReconcileFileManager {

    private Logger logger = LoggerFactory.getLogger(ReconcileFileManager.class);

    @Autowired
    protected PaymentChannelService channelService;

    @Autowired
    protected PaymentTransactionService transactionService;

    @Override
    public File downloadReconcileFile(Date transDate) {
        return null;
    }

    @Override
    public File downloadReconcileFile(Date startDate, Date endDate) {
        PaymentTransaction reconcileTransaction = createReconcileTransaction();

        logger.info("generate request message.");
        String requestMessage = generateRequestMessage(reconcileTransaction, startDate, endDate);
        reconcileTransaction.setRequestJsonStr(requestMessage);
        logger.info("requestMessage: {}", requestMessage);

        byte[] bytes = doRequest(reconcileTransaction, requestMessage);
        
        File file  = convertFile(bytes, startDate, endDate);

        return file;
    }

    private PaymentTransaction createReconcileTransaction() {
        PaymentTransaction reconcileTransaction = new PaymentTransaction();
        reconcileTransaction.setInterfaceType(PaymentInterfaceType.RECONCILIATION);
        reconcileTransaction.setSender("iPay(" + transactionService.getLocalHostInfo() + ")");
        reconcileTransaction.setPaymentStatus(PaymentTransactionStatus.RECON_PROCESSING);
        transactionService.createTransaction(reconcileTransaction);
        return reconcileTransaction;
    }

    protected abstract String generateRequestMessage(PaymentTransaction reconcileTransaction, Date startDate, Date endDate);

    protected abstract byte[] doRequest(PaymentTransaction reconcileTransaction, String requestMessage);

    protected abstract File convertFile(byte[] bytes, Date startDate, Date endDate);

}
