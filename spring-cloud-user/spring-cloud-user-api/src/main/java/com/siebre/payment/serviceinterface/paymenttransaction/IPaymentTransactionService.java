package com.siebre.payment.serviceinterface.paymenttransaction;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.siebre.basic.query.PageInfo;
import com.siebre.basic.service.ServiceResult;
import com.siebre.payment.entity.enums.PaymentTransactionStatus;
import com.siebre.payment.entity.paymentorder.PaymentOrder;
import com.siebre.payment.entity.paymentorderitem.PaymentOrderItem;
import com.siebre.payment.entity.paymenttransaction.PaymentTransaction;
import org.springframework.transaction.annotation.Transactional;

public interface IPaymentTransactionService {

    public void createPaymentTransaction(PaymentOrder paymentOrder, List<PaymentOrderItem> paymentOrderItems, PaymentTransaction paymentTransaction);

    public List<PaymentTransaction> queryPaymentTransaction(String orderNumber,
                                                            String applicantNumber,
                                                            PaymentTransactionStatus status,
                                                            Long channelId,
                                                            Date startDate,
                                                            Date endDate,
                                                            PageInfo pageInfo);

    public ServiceResult<List<PaymentTransaction>> queryPaymentTransactionRPC(String orderNumber,
                                                                              String applicantNumber,
                                                                              PaymentTransactionStatus status,
                                                                              Long channelId,
                                                                              Date startDate,
                                                                              Date endDate,
                                                                              PageInfo pageInfo);


    ServiceResult<PaymentTransaction> getPaymentTransactionByInternalTransactionNumber(String internalTransactionNumber);

    ServiceResult<PaymentTransaction> paymentConfirm(String internalTransactionNumber, String externalTransactionNumber, String seller_id, BigDecimal total_fee);

    void outOfTime(String orderNumber);
}