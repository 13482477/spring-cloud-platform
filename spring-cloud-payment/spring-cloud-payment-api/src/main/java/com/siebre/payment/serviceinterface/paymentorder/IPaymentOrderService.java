package com.siebre.payment.serviceinterface.paymentorder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.siebre.basic.query.PageInfo;
import com.siebre.basic.service.ServiceResult;
import com.siebre.payment.entity.enums.PaymentOrderPayStatus;
import com.siebre.payment.entity.enums.PaymentOrderRefundStatus;
import com.siebre.payment.entity.enums.PaymentTransactionStatus;
import com.siebre.payment.entity.paymentorder.PaymentOrder;
import com.siebre.payment.vo.statistics.PaymentChannelTransactionVo;
import com.siebre.payment.vo.unionpayment.DonutVo;

/**
 * Created by AdamTang on 2017/3/29.
 * Project:siebre-cloud-platform
 * Version:1.0
 */
public interface IPaymentOrderService {

    ServiceResult<List<PaymentOrder>> queryPaymentOrder(PageInfo pageInfo);

    ServiceResult<Map<String,Object>> queryPaymentSummary(PageInfo pageInfo);

    PaymentOrder queryPaymentOrder(String orderNumber);

    ServiceResult<PaymentOrder> queryPaymentOrderRPC(String orderNumber);

    ServiceResult<List<PaymentOrder>> queryPaymentOrderListRPC(String orderNumber, String applicationNumber, PaymentTransactionStatus status, PageInfo page);

    void paymentCheckConfirm(String orderNumber);

    ServiceResult<List<PaymentOrder>> getOrderListForPage(String orderNumber, PaymentOrderPayStatus orderPayStatus, String channelCode, PaymentOrderRefundStatus refundStatus,
                                                                 Date startDate, Date endDate, PageInfo page);
    
    ServiceResult<BigDecimal> getSuccessPaymentAmount();
    
    ServiceResult<Integer> getSuccessPaymentCount();
    
    ServiceResult<BigDecimal> getFaildPaymentAmount();
    
    ServiceResult<Integer> getFaildPaymentCount();
    
    ServiceResult<BigDecimal> getConversionRate();
    
    ServiceResult<BigDecimal[]> getTotalAmountOfThisWeek();
    
    ServiceResult<List<DonutVo>> getChannelSuccessedCount();
    
    ServiceResult<List<DonutVo>> getChannelSuccessedAmount();
    
    ServiceResult<List<List<Integer>>> getPaymentWaySuccessCount();
    
    ServiceResult<List<PaymentChannelTransactionVo>> countPaymentChannelTransaction();

}
