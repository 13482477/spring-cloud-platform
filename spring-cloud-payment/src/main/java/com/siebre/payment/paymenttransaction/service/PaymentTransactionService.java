package com.siebre.payment.paymenttransaction.service;

import com.siebre.basic.query.PageInfo;
import com.siebre.basic.service.ServiceResult;
import com.siebre.payment.entity.enums.*;
import com.siebre.payment.paymentchannel.mapper.PaymentChannelMapper;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymentorder.mapper.PaymentOrderMapper;
import com.siebre.payment.paymentorder.service.PaymentOrderService;
import com.siebre.payment.paymentorderitem.entity.PaymentOrderItem;
import com.siebre.payment.paymentorderitem.mapper.PaymentOrderItemMapper;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
import com.siebre.payment.paymenttransaction.mapper.PaymentTransactionMapper;
import com.siebre.payment.paymenttransaction.vo.RefundRecord;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.refundapplication.dto.PaymentRefundResponse;
import com.siebre.payment.refundapplication.entity.RefundApplication;
import com.siebre.payment.refundapplication.mapper.RefundApplicationMapper;
import com.siebre.payment.serialnumber.service.SerialNumberService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PaymentTransactionService {

    private Logger logger = LoggerFactory.getLogger(PaymentTransactionService.class);

    @Autowired
    private PaymentChannelMapper paymentChannelMapper;

    @Autowired
    private PaymentOrderMapper paymentOrderMapper;

    @Autowired
    private PaymentOrderService paymentOrderService;

    @Autowired
    private RefundApplicationMapper refundApplicationMapper;

    @Autowired
    protected SerialNumberService serialNumberService;

    @Autowired
    private PaymentTransactionMapper paymentTransactionMapper;

    public void createTransaction(PaymentTransaction transaction) {
        transaction.setCreateDate(new Date());
        this.paymentTransactionMapper.insert(transaction);
    }

    public void updateBySelective(PaymentTransaction transaction) {
        this.updateBySelective(transaction);
    }

    /**
     * 更新交易和订单状态
     * 1.判断传进来的交易状态和数据库中的状态是否一致
     * 2.如果一致，退出该方法
     * 3.如果不一致，更新交易状态，更新订单状态
     *
     * @param transactionId
     * @param transactionStatus
     * @return
     */
    @Transactional("db")
    public void updateTransactionAndOrderStatus(Long transactionId, PaymentTransactionStatus transactionStatus, Long orderId, PaymentOrderPayStatus orderStatus) {
        PaymentTransaction transaction = paymentTransactionMapper.selectByPrimaryKey(transactionId);
        PaymentOrder paymentOrder = paymentOrderMapper.selectByPrimaryKey(orderId);

        if (PaymentTransactionStatus.PAY_SUCCESS.equals(transactionStatus)) {
            if (!PaymentTransactionStatus.PAY_SUCCESS.equals(transaction.getPaymentStatus())) {
                PaymentTransaction transaction1 = new PaymentTransaction();
                transaction1.setId(transactionId);
                transaction1.setPaymentStatus(transactionStatus);
                paymentTransactionMapper.updateByPrimaryKeySelective(transaction1);

            }
            if (!PaymentOrderPayStatus.PAID.equals(paymentOrder.getStatus())) {
                PaymentOrder paymentOrder1 = new PaymentOrder();
                paymentOrder1.setId(paymentOrder.getId());
                paymentOrder1.setStatus(PaymentOrderPayStatus.PAID);
                paymentOrderMapper.updateByPrimaryKeySelective(paymentOrder1);
            }
        } else if (PaymentTransactionStatus.PAY_FAILED.equals(transactionStatus)) {
            //如果查询结果返回失败，并且订单状态为支付中，更新订单状态为支付失败
            if (PaymentTransactionStatus.PAY_PROCESSING.equals(transaction.getPaymentStatus())) {
                PaymentTransaction transaction1 = new PaymentTransaction();
                transaction1.setId(transactionId);
                transaction1.setPaymentStatus(PaymentTransactionStatus.CLOSED);
                paymentTransactionMapper.updateByPrimaryKeySelective(transaction1);
            }
            if (PaymentOrderPayStatus.PAYING.equals(paymentOrder.getStatus())) {
                PaymentOrder paymentOrder1 = new PaymentOrder();
                paymentOrder1.setId(paymentOrder.getId());
                paymentOrder1.setStatus(PaymentOrderPayStatus.PAYERROR);
                paymentOrderMapper.updateByPrimaryKeySelective(paymentOrder1);
            }
        }

    }

    /**
     * 获取退款流水记录
     *
     * @param refundApplicationId
     * @return
     */
    public List<RefundRecord> getRefundRecordFlow(Long refundApplicationId) {
        String active = "ACTIVE";
        String complete = "COMPLETE";
        List<RefundRecord> result = new ArrayList<>();
        RefundApplication refundApplication = this.refundApplicationMapper.selectByPrimaryKey(refundApplicationId);

        RefundRecord applyRefund = createRefundRecord(complete, refundApplication.getCreateDate(), "退款申请提交");
        result.add(applyRefund);

        if (RefundApplicationStatus.APPLICATION.equals(refundApplication.getStatus())) {
            RefundRecord wait = createRefundRecord(active, null, "等待审核");
            result.add(wait);
            return result;
        }

        if (RefundApplicationStatus.FAILED.equals(refundApplication.getStatus()) && StringUtils.isBlank(refundApplication.getRefundApplicationNumber())) {
            RefundRecord refuse = createRefundRecord(complete, refundApplication.getUpdateDate(), "退款申请未通过");
            result.add(refuse);
            return result;
        }

        PaymentTransaction paymentTransaction = paymentTransactionMapper.selectByInterTradeNo(refundApplication.getRefundApplicationNumber(), null, PaymentInterfaceType.REFUND);

        if (paymentTransaction != null) {
            RefundRecord processRefund = createRefundRecord(complete, paymentTransaction.getCreateDate(), "申请已受理，正在处理中");
            result.add(processRefund);
        }

        if (PaymentTransactionStatus.REFUND_PROCESSING.equals(paymentTransaction.getPaymentStatus())) {
            RefundRecord finish = createRefundRecord(active, null, "预计3-5个工作日完成退款");
            result.add(finish);
        } else if (PaymentTransactionStatus.REFUND_FAILED.equals(paymentTransaction.getPaymentStatus())) {
            RefundRecord finish = createRefundRecord(complete, paymentTransaction.getUpdateDate(), "退款失败，失败原因：" + refundApplication.getResponse());
            result.add(finish);
        } else if (PaymentTransactionStatus.REFUND_SUCCESS.equals(paymentTransaction.getPaymentStatus())) {
            RefundRecord finish = createRefundRecord(complete, paymentTransaction.getUpdateDate(), "退款成功");
            result.add(finish);
        }
        return result;
    }

    private RefundRecord createRefundRecord(String flag, Date time, String desc) {
        RefundRecord applyRefund = new RefundRecord();
        applyRefund.setFlag(flag);
        if (time != null) {
            applyRefund.setTime(DateFormatUtils.format(time, "yyyy-MM-dd HH:mm:ss"));
        }
        applyRefund.setDesc(desc);
        return applyRefund;
    }

    public List<PaymentTransaction> queryRefundTransaction(Long orderId) {
        return paymentTransactionMapper.selectTransaction(orderId, PaymentInterfaceType.REFUND);
    }

    @Transactional("db")
    public PaymentTransaction createRefundPaymentTransaction(PaymentTransaction paymentTransaction, RefundApplication refundApplication) {
        paymentTransactionMapper.insertSelective(paymentTransaction);
        refundApplicationMapper.updateByPrimaryKeySelective(refundApplication);
        return paymentTransaction;
    }

    /**
     * 查找交易成功的PaymentTransaction  PS:交易成功的应该只有一个，应该做好交易状态的管理
     *
     * @param orderNumber
     * @return
     */
    public PaymentTransaction getSuccessPaidPaymentTransaction(String orderNumber) {
        List<PaymentTransaction> paymentTransactions = paymentTransactionMapper.queryPaymentTransaction(orderNumber, null, PaymentTransactionStatus.PAY_SUCCESS, PaymentInterfaceType.PAY, null, null, null, null);
        if (paymentTransactions != null && !paymentTransactions.isEmpty()) {
            return paymentTransactions.get(0);
        }
        return null;
    }

    /**
     * 查找正在支付中的PaymentTransaction   PS:交易中的应该只有一个，应该做好交易状态的管理
     *
     * @param orderNumber
     * @return
     */
    public PaymentTransaction getPayingTransaction(String orderNumber) {
        List<PaymentTransaction> paymentTransactions = paymentTransactionMapper.queryPaymentTransaction(orderNumber, null, PaymentTransactionStatus.PAY_PROCESSING, PaymentInterfaceType.PAY, null, null, null, null);
        if (paymentTransactions != null && !paymentTransactions.isEmpty()) {
            return paymentTransactions.get(0);
        }
        return null;
    }

    /**
     * 订单查询，找到唯一匹配的PaymentTransaction
     * 1.查找是否存在已完成的交易
     * 2.否则,查找是否存在正在交易中的交易
     * 3.否则,返回null
     *
     * @param orderNumber
     * @return
     */
    public PaymentTransaction getPaymentTransactionForQuery(String orderNumber) {
        PaymentTransaction paymentTransaction;
        paymentTransaction = getSuccessPaidPaymentTransaction(orderNumber);
        if (null != paymentTransaction) {
            return paymentTransaction;
        }
        paymentTransaction = getPayingTransaction(orderNumber);
        if (null != paymentTransaction) {
            return paymentTransaction;
        }
        return null;
    }

    /**
     * 记录与第三方支付系统发生的支付交易记录
     */
    @Transactional("db")
    public PaymentTransaction recordPay2ThirdPartyTransaction(PaymentOrder paymentOrder, PaymentWay paymentWay) {
        PaymentTransaction paymentTransaction = new PaymentTransaction();
        paymentTransaction.setPaymentStatus(PaymentTransactionStatus.PAY_PROCESSING);
        //内部交易流水号使用order上的orderNumber
        paymentTransaction.setInternalTransactionNumber(paymentOrder.getOrderNumber());
        paymentTransaction.setPaymentWayId(paymentWay.getId());
        paymentTransaction.setPaymentChannelId(paymentWay.getPaymentChannel().getId());
        paymentTransaction.setCreateUser("lizhiqiang");
        paymentTransaction.setCreateDate(new Date());
        paymentTransaction.setPaymentOrderId(paymentOrder.getId());
        paymentTransaction.setPaymentAmount(paymentOrder.getAmount());
        //创建的是支付的交易
        paymentTransaction.setInterfaceType(PaymentInterfaceType.PAY);
        paymentTransaction.setSender("iPay(" + getLocalHostInfo() + ")");
        paymentTransaction.setReceiver(paymentOrder.getChannelCode());
        paymentTransaction.setCreateDate(new Date());

        this.paymentTransactionMapper.insert(paymentTransaction);
        return paymentTransaction;
    }

    public String getLocalHostInfo() {
        String ip = "";
        String hostName = "";
        try {
            InetAddress netAddress = InetAddress.getLocalHost();
            ip = netAddress.getHostAddress();
            hostName = netAddress.getHostName();
        } catch (UnknownHostException e) {
            logger.error("获取InetAddress失败，原因：{}", e);
        }
        return ip + ":" + hostName;
    }

    /**
     * 支付成功，订单业务回调处理程序
     * 1.判断该笔订单是否已经做过处理
     * 如果没有做过处理，根据订单号internalTransactionNumber在订单系统中查到该笔订单的详细，并执行业务程序
     * 判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
     * 如果有做过处理，不执行业务程序
     *
     * @param internalTransactionNumber 内部交易号(订单号)
     * @param externalTransactionNumber 外部交易流水号
     * @return
     */
    @Transactional("db")
    public ServiceResult<PaymentTransaction> paymentConfirm(String internalTransactionNumber, String externalTransactionNumber, String seller_id, BigDecimal total_fee, Date successDate, String responseStr) {

        //根据内部交易流水号  交易状态为支付中   接口类型为支付接口  查找唯一的transaction
        PaymentTransaction paymentTransaction = this.paymentTransactionMapper.selectByInterTradeNo(internalTransactionNumber, PaymentTransactionStatus.PAY_PROCESSING, PaymentInterfaceType.PAY);

        if (paymentTransaction == null) {
            logger.error("没有找到该条交易记录internalTransactionNumber={}", internalTransactionNumber);
            return ServiceResult.<PaymentTransaction>builder().success(false).message("没有找到该条交易记录internalTransactionNumber=" + internalTransactionNumber).build();
        }
        if (externalTransactionNumber.equals(paymentTransaction.getExternalTransactionNumber())) {
            logger.error("重复回调internalTransactionNumber={}", internalTransactionNumber);
            return ServiceResult.<PaymentTransaction>builder().success(false).message("重复回调internalTransactionNumber=" + internalTransactionNumber).build();
        }
        //校验seller_id
        String merchId = paymentChannelMapper.selectByPrimaryKey(paymentTransaction.getPaymentChannelId()).getMerchantCode();
        if (!merchId.equals(seller_id)) {
            logger.error("商户号不一致seller_id={}，MerchantCode={}", seller_id, merchId);
            return ServiceResult.<PaymentTransaction>builder().success(false).message("商户号不一致seller_id=" + seller_id + ", MerchantCode=" + merchId).build();
        }

        PaymentOrder paymentOrder = this.paymentOrderMapper.selectByPrimaryKey(paymentTransaction.getPaymentOrderId());

        // 校验total_fee
        BigDecimal paymentAmount = paymentOrder.getAmount();
        if (paymentAmount.compareTo(total_fee) != 0) {
            logger.error("订单金额不一致total_fee={}，paymentAmount={}", total_fee, paymentAmount);
            return ServiceResult.<PaymentTransaction>builder().success(false).message("订单金额不一致total_fee=" + seller_id + "，paymentAmount=" + paymentAmount).build();
        }
        //更新transaction状态
        paymentTransaction.setExternalTransactionNumber(externalTransactionNumber);
        paymentTransaction.setPaymentStatus(PaymentTransactionStatus.PAY_SUCCESS);
        paymentTransaction.setResponseJsonStr(responseStr);
        this.paymentTransactionMapper.updateByPrimaryKeySelective(paymentTransaction);

        //更新order状态
        this.paymentOrderService.updateOrderStatus(paymentOrder, PaymentOrderPayStatus.PAID, successDate);
        updateExternalTransactionNumber(externalTransactionNumber, paymentOrder);

        return ServiceResult.<PaymentTransaction>builder().success(true).data(paymentTransaction).build();
    }

    private void updateExternalTransactionNumber(String externalTransactionNumber, PaymentOrder paymentOrder) {
        PaymentOrder orderForUpdate = new PaymentOrder();
        orderForUpdate.setId(paymentOrder.getId());
        orderForUpdate.setExternalOrderNumber(externalTransactionNumber);
        this.paymentOrderMapper.updateByPrimaryKeySelective(orderForUpdate);
    }

    /**
     * 支付失败，更新订单和交易状态
     *
     * @param internalTransactionNumber
     * @param externalTransactionNumber
     * @return
     */
    public ServiceResult<PaymentTransaction> setFailStatus(String internalTransactionNumber, String externalTransactionNumber) {
        PaymentTransaction paymentTransaction = this.paymentTransactionMapper.selectByInterTradeNo(internalTransactionNumber, PaymentTransactionStatus.PAY_PROCESSING, PaymentInterfaceType.PAY);

        if (paymentTransaction == null) {
            logger.error("没有找到该条交易记录internalTransactionNumber={}", internalTransactionNumber);
            return ServiceResult.<PaymentTransaction>builder().success(false).message("没有找到该条交易记录internalTransactionNumber=" + internalTransactionNumber).build();
        }
        if (externalTransactionNumber.equals(paymentTransaction.getExternalTransactionNumber())) {
            logger.error("重复回调internalTransactionNumber={}", internalTransactionNumber);
            return ServiceResult.<PaymentTransaction>builder().success(false).message("重复回调internalTransactionNumber=" + internalTransactionNumber).build();
        }
        //更新transaction状态
        paymentTransaction.setExternalTransactionNumber(externalTransactionNumber);
        paymentTransaction.setPaymentStatus(PaymentTransactionStatus.PAY_FAILED);
        this.paymentTransactionMapper.updateByPrimaryKeySelective(paymentTransaction);

        //更新order状态
        PaymentOrder paymentOrder = this.paymentOrderMapper.selectByPrimaryKey(paymentTransaction.getPaymentOrderId());
        paymentOrder.setStatus(PaymentOrderPayStatus.PAYERROR);
        paymentOrderMapper.updateByPrimaryKeySelective(paymentOrder);

        return ServiceResult.<PaymentTransaction>builder().success(true).data(paymentTransaction).build();
    }

    @Transactional("db")
    public ServiceResult<PaymentTransaction> refundConfirm(String internalTransactionNumber, String externalTransactionNumber) {
        //TODO 完成退款的回调处理同步更新订单，交易，退款申请
        PaymentTransaction paymentTransaction = paymentTransactionMapper.selectByInterTradeNo(internalTransactionNumber, PaymentTransactionStatus.REFUND_PROCESSING, PaymentInterfaceType.REFUND);
        paymentTransaction.setExternalTransactionNumber(externalTransactionNumber);
        paymentTransaction.setPaymentStatus(PaymentTransactionStatus.REFUND_SUCCESS);

        PaymentOrder paymentOrder = this.paymentOrderMapper.selectByPrimaryKey(paymentTransaction.getPaymentOrderId());
        RefundApplication refundApplication = refundApplicationMapper.selectByBusinessNumber(paymentOrder.getOrderNumber(), paymentTransaction.getInternalTransactionNumber());

        //更新订单退款金额
        BigDecimal totalRefundAmount = paymentOrder.getRefundAmount();
        if (totalRefundAmount != null) {
            paymentOrder.setRefundAmount(totalRefundAmount.add(refundApplication.getRefundAmount()));
        } else {
            paymentOrder.setRefundAmount(refundApplication.getRefundAmount());
        }

        //更新订单退款状态
        if (paymentOrder.getRefundAmount().compareTo(paymentOrder.getAmount()) == 0) {
            paymentOrder.setStatus(PaymentOrderPayStatus.FULL_REFUND);
        } else {
            paymentOrder.setStatus(PaymentOrderPayStatus.PART_REFUND);
        }

        //更新退款申请状态
        refundApplication.setStatus(RefundApplicationStatus.SUCCESS);
        refundApplication.setResponse(RefundApplicationStatus.SUCCESS.getDescription());

        paymentOrderMapper.updateByPrimaryKey(paymentOrder);
        refundApplicationMapper.updateByPrimaryKey(refundApplication);
        paymentTransactionMapper.updateByPrimaryKeySelective(paymentTransaction);

        return ServiceResult.<PaymentTransaction>builder().success(true).data(null).build();
    }

    /**
     * 同步退款结果处理
     */
    @Transactional("db")
    public void synchronizedRefundConfirm(PaymentOrder paymentOrder, PaymentRefundResponse paymentRefundResponse) {
        RefundApplication refundApplication = paymentRefundResponse.getRefundApplication();
        PaymentTransaction paymentTransaction = paymentRefundResponse.getRefundTransaction();
        Date current = new Date();

        if (RefundApplicationStatus.SUCCESS.equals(refundApplication.getStatus())) {
            //更新订单退款金额
            BigDecimal totalRefundAmount = paymentOrder.getRefundAmount();
            if (totalRefundAmount != null) {
                paymentOrder.setRefundAmount(totalRefundAmount.add(refundApplication.getRefundAmount()));
            } else {
                paymentOrder.setRefundAmount(refundApplication.getRefundAmount());
            }

            //更新订单退款状态
            if (paymentOrder.getRefundAmount().compareTo(paymentOrder.getAmount()) == 0) {
                paymentOrder.setStatus(PaymentOrderPayStatus.FULL_REFUND);
            } else {
                paymentOrder.setStatus(PaymentOrderPayStatus.PART_REFUND);
            }
            refundApplication.setUpdateDate(current);
            paymentTransaction.setUpdateDate(current);
        } else if (RefundApplicationStatus.FAILED.equals(refundApplication.getStatus())) {
            paymentOrder.setStatus(PaymentOrderPayStatus.REFUNDERROR);
            refundApplication.setUpdateDate(current);
            refundApplication.setResponse(paymentRefundResponse.getReturnMessage());
            paymentTransaction.setUpdateDate(current);
        }

        paymentOrderMapper.updateByPrimaryKey(paymentOrder);
        refundApplicationMapper.updateByPrimaryKey(refundApplication);
        paymentTransactionMapper.updateByPrimaryKeySelective(paymentTransaction);

    }

    @Transactional("db")
    public void outOfTime(String orderNumber) {
        paymentOrderMapper.updateOrderStatusToClose(orderNumber);
        paymentTransactionMapper.updateTransactionStatusToClose(orderNumber);
    }

}
