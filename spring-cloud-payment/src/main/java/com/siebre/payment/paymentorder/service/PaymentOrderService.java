package com.siebre.payment.paymentorder.service;

import com.siebre.basic.query.PageInfo;
import com.siebre.basic.service.ServiceResult;
import com.siebre.payment.entity.enums.*;
import com.siebre.payment.paymentchannel.entity.PaymentChannel;
import com.siebre.payment.paymentchannel.mapper.PaymentChannelMapper;
import com.siebre.payment.paymentcheck.vo.CheckOrderVo;
import com.siebre.payment.paymentcheck.vo.CheckOverviewResult;
import com.siebre.payment.paymentgateway.vo.PaymentOrderRequest;
import com.siebre.payment.paymentgateway.vo.PaymentOrderResponse;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymentorder.mapper.PaymentOrderMapper;
import com.siebre.payment.paymentorder.vo.OrderQueryParamsVo;
import com.siebre.payment.paymentorder.vo.TradeOrder;
import com.siebre.payment.paymentorder.vo.TradeOrderDetail;
import com.siebre.payment.paymentorder.vo.TradeOrderItem;
import com.siebre.payment.paymentorderitem.entity.PaymentOrderItem;
import com.siebre.payment.paymentorderitem.mapper.PaymentOrderItemMapper;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
import com.siebre.payment.policylibility.entity.PolicyLibility;
import com.siebre.payment.policylibility.mapper.PolicyLibilityMapper;
import com.siebre.payment.policyrole.entity.PolicyRole;
import com.siebre.payment.policyrole.mapper.PolicyRoleMapper;
import com.siebre.payment.serialnumber.service.SerialNumberService;
import com.siebre.payment.statistics.vo.DonutVo;
import com.siebre.payment.statistics.vo.PaymentChannelTransactionVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created by AdamTang on 2017/3/29. Project:siebre-cloud-platform Version:1.0
 */
@Service("paymentOrderService")
public class PaymentOrderService {

    @Autowired
    private PaymentOrderMapper paymentOrderMapper;

    @Autowired
    @Qualifier("serialNumberService")
    private SerialNumberService serialNumberService;

    @Autowired
    private PaymentOrderItemMapper paymentOrderItemMapper;

    @Autowired
    private PolicyRoleMapper policyRoleMapper;

    @Autowired
    private PolicyLibilityMapper policyLibilityMapper;

    @Autowired
    private PaymentChannelMapper paymentChannelMapper;

    public ServiceResult<List<PaymentOrder>> queryPaymentOrder(PageInfo pageInfo) {
        return null;
    }

    public ServiceResult<Map<String, Object>> queryPaymentSummary(PageInfo pageInfo) {
        return null;
    }

    public PaymentOrderPayStatus queryOrderStatus(String orderNumber) {
        PaymentOrder order = paymentOrderMapper.selectByOrderNumber(orderNumber);
        return order.getStatus();
    }

    /**
     * 创建order和order item   libility    applicant   insurePerson
     *
     * @param orderRequest
     * @param request
     * @return
     */
    @Transactional("db")
    public PaymentOrderResponse createPaymentOrderAndItems(PaymentOrderRequest orderRequest, HttpServletRequest request) {
        //幂等性校验
        String messageId = orderRequest.getMessageId();
        PaymentOrder order = paymentOrderMapper.selectByMessageId(messageId);
        if (order != null) {
            List<PaymentOrderItem> items = paymentOrderItemMapper.selectByPaymentOrderId(order.getId());
            return PaymentOrderResponse.SUCCESS("创建成功", order, items);
        }

        //保存order
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setPaymentWayCode(orderRequest.getPaymentWayCode());
        paymentOrder.setOrderNumber(serialNumberService.nextValue("sale_order"));
        paymentOrder.setItems(orderRequest.getPaymentOrderItems());
        if (orderRequest.getSellingChannel() != null) {
            if (orderRequest.getSellingChannel().equals("MOBILE_SALE_APP")) {
                paymentOrder.setSellingChannel(SellingChannel.MOBILE_SALE_APP);//移动展业
            } else {
                paymentOrder.setSellingChannel(SellingChannel.SELF_INSURANCE);//自助投保
            }
        }

        //设置order状态为待支付
        paymentOrder.setStatus(PaymentOrderPayStatus.UNPAID);
        //设置对账状态为未对账
        paymentOrder.setCheckStatus(PaymentOrderCheckStatus.NOT_CONFIRM);
        //设置订单锁定状态为未锁定
        paymentOrder.setLockStatus(PaymentOrderLockStatus.UNLOCK);
        paymentOrder.setCreateTime(new Date());
        this.processTotalAmount(paymentOrder, orderRequest.getPaymentOrderItems());
        this.paymentOrderMapper.insert(paymentOrder);

        for (PaymentOrderItem paymentOrderItem : orderRequest.getPaymentOrderItems()) {
            paymentOrderItem.setPaymentOrderId(paymentOrder.getId());
            //save applicant
            PolicyRole applicant = paymentOrderItem.getApplicant();
            applicant.setPolicyRoleType(PolicyRoleType.POLICY_HOLDER);
            policyRoleMapper.insert(applicant);
            paymentOrderItem.setApplicantId(applicant.getId());
            //save insured
            if ("n".equalsIgnoreCase(paymentOrderItem.getSamePerson())) {
                PolicyRole insuredPerson = paymentOrderItem.getInsured();
                insuredPerson.setPolicyRoleType(PolicyRoleType.INSURED_PERSON);
                policyRoleMapper.insert(insuredPerson);
                paymentOrderItem.setInsuredPersonId(insuredPerson.getId());
            }
            paymentOrderItemMapper.insert(paymentOrderItem);
            //save libilities
            for (PolicyLibility libility : paymentOrderItem.getLibilities()) {
                libility.setOrderItemId(paymentOrderItem.getId());
                policyLibilityMapper.insert(libility);
            }
        }
        return PaymentOrderResponse.SUCCESS("创建成功", paymentOrder, orderRequest.getPaymentOrderItems());
    }

    /**
     * 计算总保额和总保费
     *
     * @param paymentOrder
     * @param paymentOrderItems
     */
    private void processTotalAmount(PaymentOrder paymentOrder, List<PaymentOrderItem> paymentOrderItems) {
        BigDecimal totalInsuredAmount = BigDecimal.ZERO;
        BigDecimal totalPremium = BigDecimal.ZERO;

        for (PaymentOrderItem paymentOrderItem : paymentOrderItems) {
            this.processPolicyLibilityAmount(paymentOrderItem);
            totalPremium = totalPremium.add(paymentOrderItem.getGrossPremium());
            totalInsuredAmount = totalInsuredAmount.add(paymentOrderItem.getInsuredAmount());
        }
        paymentOrder.setTotalInsuredAmount(totalInsuredAmount);
        paymentOrder.setTotalPremium(totalPremium);
    }


    private void processPolicyLibilityAmount(PaymentOrderItem paymentOrderItem) {
        BigDecimal totalInsuredAmount = BigDecimal.ZERO;
        BigDecimal totalPremium = BigDecimal.ZERO;
        for (PolicyLibility libility : paymentOrderItem.getLibilities()) {
            totalPremium = totalPremium.add(libility.getPremium());
            totalInsuredAmount = totalInsuredAmount.add(libility.getInsuredAmount());
        }
        paymentOrderItem.setGrossPremium(totalPremium);
        paymentOrderItem.setInsuredAmount(totalInsuredAmount);
    }

    public CheckOrderVo queryPaymentOrderForCheckDetail(String orderNumber) {
        PaymentOrder order = paymentOrderMapper.selectByOrderNumberleftjoin(orderNumber);
        List<PaymentOrderItem> items = paymentOrderItemMapper.selectByPaymentOrderId(order.getId());
        order.setItems(items);

        CheckOrderVo orderVo = new CheckOrderVo();
        if (order.getCheckTime() != null) {
            String dateStrCheck = DateFormatUtils.format(order.getCheckTime(), "yyyy-MM-dd HH:mm:ss");
            orderVo.setCheckTime(dateStrCheck);
        }
        if ( !order.getRefundStatus().equals(PaymentOrderRefundStatus.FULL_REFUND)) {//非全额退款就属于支付对账类型
            orderVo.setCheckType("支付");
        }else if (order.getRefundStatus().equals(PaymentOrderRefundStatus.FULL_REFUND)){
            orderVo.setCheckType("退款");
        }
        orderVo.setCheckStatus(order.getCheckStatus());

        String dateStrCreate = DateFormatUtils.format(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss");

        //投保信息
        orderVo.setApplicationNumber("1494469393190");
        orderVo.setPremium(order.getTotalPremium());
        orderVo.setApplicationCreateTime(dateStrCreate);
        orderVo.setApplicationPayStatus(order.getStatus());

        //支付信息
        orderVo.setOrderNumber(order.getOrderNumber());
        orderVo.setAmount(order.getAmount());
        orderVo.setCreateTime(dateStrCreate);
        orderVo.setPayStatus(order.getStatus());

        //第三方支付信息
        orderVo.setChannelCode(paymentChannelMapper.selectByPrimaryKey(order.getPaymentChannelId()).getChannelCode());
        orderVo.setChannelName(paymentChannelMapper.selectByPrimaryKey(order.getPaymentChannelId()).getChannelName());
        orderVo.setExternalTransactionNumber(orderNumber);//暂时用订单号
        orderVo.setPayTime(dateStrCreate);
        orderVo.setRealAmount(order.getAmount());
        orderVo.setRealPayStatus(order.getStatus());

        return orderVo;
    }

    public PaymentOrder queryPaymentOrder(String orderNumber) {
        PaymentOrder order = paymentOrderMapper.selectByOrderNumberleftjoin(orderNumber);
        List<PaymentOrderItem> items = paymentOrderItemMapper.selectByPaymentOrderId(order.getId());
        order.setItems(items);
        return order;
    }

    public ServiceResult<PaymentOrder> queryPaymentOrderRPC(String orderNumber) {
        ServiceResult<PaymentOrder> result = new ServiceResult<PaymentOrder>();
        result.setData(this.queryPaymentOrder(orderNumber));
        return result;
    }

    public ServiceResult<List<PaymentOrder>> getOrderListForPage(String orderNumber, List<PaymentOrderPayStatus> orderPayStatusList, List<String> channelCodeList,
                                                                 List<PaymentOrderRefundStatus> refundStatusList,
                                                                 Date startDate, Date endDate, PageInfo page) {
        ServiceResult<List<PaymentOrder>> result = new ServiceResult<>();
        List<PaymentOrder> orders = paymentOrderMapper.selectOrderByPage(orderNumber, orderPayStatusList, channelCodeList, refundStatusList, startDate, endDate, page);
        result.setData(orders);
        result.setPageInfo(page);
        return result;
    }

    public ServiceResult<List<CheckOrderVo>> selectCheckOrderByPage(String orderNumber, List<String> channelCodeList,
                                                                    PaymentOrderRefundStatus refundStatus, List<PaymentOrderCheckStatus> checkStatusList,
                                                                    Date checkStartDate, Date checkEndDate,PageInfo pageInfo) {

        ServiceResult<List<CheckOrderVo>> result = new ServiceResult<>();
        List<PaymentOrder> orders = new ArrayList<>();

        if (PaymentOrderRefundStatus.NOT_REFUND.equals(refundStatus) || PaymentOrderRefundStatus.FULL_REFUND.equals(refundStatus)) {//支付
            orders = paymentOrderMapper.selectCheckOrderByPage(orderNumber, channelCodeList, PaymentOrderPayStatus.PAID, refundStatus, checkStatusList, checkStartDate,checkEndDate,pageInfo);
        } else {
            orders = paymentOrderMapper.selectCheckOrderByPage(orderNumber, channelCodeList, null, null, checkStatusList, checkStartDate,checkEndDate,pageInfo);
        }

        List<CheckOrderVo> checkOrderVos = new ArrayList<>();
        for (PaymentOrder order : orders) {
            CheckOrderVo checkOrderVo = new CheckOrderVo();
            checkOrderVo.setOrderNumber(order.getOrderNumber());
            if (order.getPaymentChannelId() == null)
                continue;
            checkOrderVo.setChannelCode(paymentChannelMapper.selectByPrimaryKey(order.getPaymentChannelId()).getChannelCode());
            checkOrderVo.setChannelName(paymentChannelMapper.selectByPrimaryKey(order.getPaymentChannelId()).getChannelName());
            checkOrderVo.setAmount(order.getAmount());
            checkOrderVo.setCheckStatus(order.getCheckStatus());
            if (order.getCheckTime() != null) {
                String dateStr = DateFormatUtils.format(order.getCheckTime(), "yyyy-MM-dd HH:mm:ss");
                checkOrderVo.setCheckTime(dateStr);
            }
            if (!order.getRefundStatus().equals(PaymentOrderRefundStatus.FULL_REFUND)) {//非全额退款就属于支付对账类型
                checkOrderVo.setCheckType("支付");
            }else if (order.getRefundStatus().equals(PaymentOrderRefundStatus.FULL_REFUND)){
                checkOrderVo.setCheckType("退款");
            }

            checkOrderVos.add(checkOrderVo);
        }

        result.setData(checkOrderVos);
        result.setPageInfo(pageInfo);
        return result;
    }

    public ServiceResult<List<PaymentOrder>> queryPaymentOrderListRPC(String orderNumber, String applicationNumber, PaymentTransactionStatus status, PageInfo page) {
        ServiceResult<List<PaymentOrder>> result = new ServiceResult<List<PaymentOrder>>();
        List<PaymentOrder> orders = paymentOrderMapper.selectOrderJoinTransaction(orderNumber, applicationNumber, status, page);
        result.setData(orders);
        result.setPageInfo(page);
        return result;
    }

    @Transactional("db")
    public void paymentCheckConfirm(String orderNumber) {
        PaymentOrder paymentOrder = this.paymentOrderMapper.selectByOrderNumber(orderNumber);
        paymentOrder.setCheckStatus(PaymentOrderCheckStatus.SUCCESS);
        paymentOrderMapper.updateByPrimaryKeySelective(paymentOrder);
    }

    public ServiceResult<BigDecimal> getSuccessPaymentAmount() {
        BigDecimal result = this.paymentOrderMapper.getSuccessedPaymentAmount();
        return ServiceResult.<BigDecimal>builder().success(true).message("处理成功！").data(result == null ? BigDecimal.ZERO : result).build();
    }

    public ServiceResult<Integer> getSuccessPaymentCount() {
        Integer result = this.paymentOrderMapper.getSuccessedPaymentCount();
        return ServiceResult.<Integer>builder().success(true).message("处理成功！").data(result == null ? 0 : result).build();
    }

    public ServiceResult<BigDecimal> getFaildPaymentAmount() {
        BigDecimal result = this.paymentOrderMapper.getFaildPaymentAmount();
        return ServiceResult.<BigDecimal>builder().success(true).data(result == null ? BigDecimal.ZERO : result).build();
    }

    public ServiceResult<Integer> getFaildPaymentCount() {
        Integer result = this.paymentOrderMapper.getFaildPaymentCount();
        return ServiceResult.<Integer>builder().success(true).data(result == null ? 0 : result).build();
    }

    public ServiceResult<BigDecimal> getConversionRate() {
        Integer totalCount = this.paymentOrderMapper.getCount();
        Integer successedCount = this.paymentOrderMapper.getSuccessedPaymentCount();

        BigDecimal result = new BigDecimal(successedCount).multiply(new BigDecimal(100)).divide(new BigDecimal(totalCount), RoundingMode.HALF_UP);

        return ServiceResult.<BigDecimal>builder().success(true).data(result).build();
    }

    public ServiceResult<BigDecimal[]> getTotalAmountOfThisWeek() {
        Date[] dates = this.getDateOfThisWeek();

        BigDecimal data1 = this.paymentOrderMapper.getTotalAmountByDateRange(dates[0], DateUtils.addDays(dates[0], 1));
        BigDecimal data2 = this.paymentOrderMapper.getTotalAmountByDateRange(dates[1], DateUtils.addDays(dates[1], 1));
        BigDecimal data3 = this.paymentOrderMapper.getTotalAmountByDateRange(dates[2], DateUtils.addDays(dates[2], 1));
        BigDecimal data4 = this.paymentOrderMapper.getTotalAmountByDateRange(dates[3], DateUtils.addDays(dates[3], 1));
        BigDecimal data5 = this.paymentOrderMapper.getTotalAmountByDateRange(dates[4], DateUtils.addDays(dates[4], 1));
        BigDecimal data6 = this.paymentOrderMapper.getTotalAmountByDateRange(dates[5], DateUtils.addDays(dates[5], 1));
        BigDecimal data7 = this.paymentOrderMapper.getTotalAmountByDateRange(dates[6], DateUtils.addDays(dates[6], 1));

        BigDecimal[] datas = {
                data1,
                data2,
                data3,
                data4,
                data5,
                data6,
                data7
        };

        for (int i = 0; i < datas.length; i++) {
            if (datas[i] == null) {
                datas[i] = BigDecimal.ZERO;
            }
        }

        return ServiceResult.<BigDecimal[]>builder().success(true).data(datas).build();
    }

    private Date[] getDateOfThisWeek() {
        Date date1 = this.getFirstDateOfThisWeek();

        Date[] result = {date1, DateUtils.addDays(date1, 1), DateUtils.addDays(date1, 2), DateUtils.addDays(date1, 3), DateUtils.addDays(date1, 4), DateUtils.addDays(date1, 5),
                DateUtils.addDays(date1, 6),};

        return result;
    }

    private Date getFirstDateOfThisWeek() {
        Calendar calendar = Calendar.getInstance();

        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());

        Date date = calendar.getTime();
        return date;
    }

    public ServiceResult<List<DonutVo>> getChannelSuccessedCount() {
        List<PaymentChannel> paymentChannelList = this.paymentChannelMapper.selectAll();

        List<Map<String, Object>> dataList = this.paymentOrderMapper.getChannelSuccessedCount();

        List<DonutVo> result = new ArrayList<DonutVo>();

        for (Map<String, Object> data : dataList) {
            DonutVo donutVo = new DonutVo();
            donutVo.setName(this.getLabelName((Long) data.get("paymentChannelId"), paymentChannelList));
            donutVo.setValue(new BigDecimal((Long) data.get("count")));
            result.add(donutVo);
        }

        return ServiceResult.<List<DonutVo>>builder().success(true).data(result).build();
    }

    public ServiceResult<List<DonutVo>> getChannelFailCount() {
        List<PaymentChannel> paymentChannelList = this.paymentChannelMapper.selectAll();

        List<Map<String, Object>> dataList = this.paymentOrderMapper.getChannelFailCount();

        List<DonutVo> result = new ArrayList<DonutVo>();

        for (Map<String, Object> data : dataList) {
            DonutVo donutVo = new DonutVo();
            donutVo.setName(this.getLabelName((Long) data.get("paymentChannelId"), paymentChannelList));
            donutVo.setValue(new BigDecimal((Long) data.get("count")));
            result.add(donutVo);
        }

        return ServiceResult.<List<DonutVo>>builder().success(true).data(result).build();
    }

    public ServiceResult<List<DonutVo>> getChannelSuccessedAmount() {
        List<PaymentChannel> paymentChannelList = this.paymentChannelMapper.selectAll();

        List<Map<String, Object>> dataList = this.paymentOrderMapper.getChannelSuccessedAmount();

        List<DonutVo> result = new ArrayList<DonutVo>();

        for (Map<String, Object> data : dataList) {
            DonutVo donutVo = new DonutVo();
            donutVo.setName(this.getLabelName((Long) data.get("paymentChannelId"), paymentChannelList));
            donutVo.setValue((BigDecimal) data.get("totalAmount"));
            result.add(donutVo);
        }

        return ServiceResult.<List<DonutVo>>builder().success(true).data(result).build();
    }

    public ServiceResult<List<DonutVo>> getChannelFailAmount() {
        List<PaymentChannel> paymentChannelList = this.paymentChannelMapper.selectAll();

        List<Map<String, Object>> dataList = this.paymentOrderMapper.getChannelFailAmount();

        List<DonutVo> result = new ArrayList<DonutVo>();

        for (Map<String, Object> data : dataList) {
            DonutVo donutVo = new DonutVo();
            donutVo.setName(this.getLabelName((Long) data.get("paymentChannelId"), paymentChannelList));
            donutVo.setValue((BigDecimal) data.get("totalAmount"));
            result.add(donutVo);
        }

        return ServiceResult.<List<DonutVo>>builder().success(true).data(result).build();
    }

    private String getLabelName(Long paymentChannelId, List<PaymentChannel> paymentChannelList) {
        for (PaymentChannel paymentChannel : paymentChannelList) {
            if (paymentChannelId == paymentChannel.getId()) {
                return paymentChannel.getChannelName();
            }
        }
        return null;
    }

    public ServiceResult<Map<String, List<DonutVo>>> getPaymentWaySuccessCount() {
        String[] paymentWayCodes = {
                "ALIPAY_WEB_PAY",
                "WECHAT_SCAN_PAY",
                "UNIONPAY_ACP_PAY",
                "ALIPAY_TRADE_WAP_PAY",
                "WECHAT_PUBLIC_PAY",
                "ALLIN_ACP_PAY",
        };

        List<Map<String, Object>> paidDatas = this.paymentOrderMapper.getPaymentWayCount(PaymentOrderPayStatus.PAID);
        /*List<Map<String, Object>> payErrorDatas = this.paymentOrderMapper.getPaymentWayCount(PaymentOrderPayStatus.PAYERROR);
        List<Map<String, Object>> payingDatas = this.paymentOrderMapper.getPaymentWayCount(PaymentOrderPayStatus.PAYING);*/

        Map<String, List<DonutVo>> result = new HashMap<>();

        //app支付目前没有
        List<DonutVo> result1 = new ArrayList<>();
        result.put("APP支付", result1);

        //手机网页
        List<DonutVo> result2 = new ArrayList<>();
        result2.add(new DonutVo("支付宝手机网关支付", this.getCountByPayWayCode(paidDatas, paymentWayCodes[3])));
        result2.add(new DonutVo("微信公众号支付", this.getCountByPayWayCode(paidDatas, paymentWayCodes[4])));
        result2.add(new DonutVo("通联支付", this.getCountByPayWayCode(paidDatas, paymentWayCodes[5])));
        result.put("手机网页支付", result2);

        //PC网页支付
        List<DonutVo> result3 = new ArrayList<>();
        result3.add(new DonutVo("支付宝即时到账", this.getCountByPayWayCode(paidDatas, paymentWayCodes[0])));
        result3.add(new DonutVo("微信扫码支付", this.getCountByPayWayCode(paidDatas, paymentWayCodes[1])));
        result3.add(new DonutVo("银联支付", this.getCountByPayWayCode(paidDatas, paymentWayCodes[2])));
        result3.add(new DonutVo("通联支付", this.getCountByPayWayCode(paidDatas, paymentWayCodes[5])));
        result.put("PC网页支付", result3);

        return ServiceResult.<Map<String, List<DonutVo>>>builder().success(true).data(result).build();
    }

    private BigDecimal getCountByPayWayCode(List<Map<String, Object>> dataMaps, String paymentWayCode) {
        for (Map<String, Object> dataMap : dataMaps) {
            if (StringUtils.equals(paymentWayCode, (CharSequence) dataMap.get("paymentWayCode"))) {
                return new BigDecimal((Long) dataMap.get("count"));
            }
        }
        return new BigDecimal("0");
    }

    public ServiceResult<List<PaymentChannelTransactionVo>> countPaymentChannelTransaction() {
        List<PaymentChannelTransactionVo> result = this.paymentOrderMapper.countPaymentChannelTransaction();
        return ServiceResult.<List<PaymentChannelTransactionVo>>builder().success(true).data(result).build();
    }

    public CheckOverviewResult getOrdersByChannelAndDate(Long channelId, Date checkStartDate, Date checkEndDate) {

        List<PaymentOrder> orders = this.paymentOrderMapper.getOrdersByChannelAndDate(channelId, checkStartDate, checkEndDate);

        CheckOverviewResult checkOverviewResult = new CheckOverviewResult();
        int checkTotalCount = 0;//对账总笔数
        int successCount = 0;//成功笔数
        int failCount = 0;//失败笔数
        int notCheckTotalCount = 0;//未对账总笔数
        BigDecimal payOrderTotalAmount = BigDecimal.ZERO;//支付信息-订单金额
        BigDecimal payTotalAmount = BigDecimal.ZERO;//支付信息-支付金额
        BigDecimal refundOrderTotalAmount = BigDecimal.ZERO;//退款信息-订单金额
        BigDecimal refundTotalAmount = BigDecimal.ZERO;//退款信息-退款金额

        for (PaymentOrder order : orders) {
            if ( !order.getCheckStatus().equals(PaymentOrderCheckStatus.NOT_CONFIRM)) {//已对账
                checkTotalCount = checkTotalCount + 1;
                if (order.getCheckStatus().equals(PaymentOrderCheckStatus.SUCCESS)) {//对账成功
                    successCount = successCount + 1;
                    if (order.getStatus().equals(PaymentOrderPayStatus.PAID) && order.getRefundStatus().equals(PaymentOrderRefundStatus.NOT_REFUND)) {//支付成功且未退款
                        payOrderTotalAmount = payOrderTotalAmount.add(order.getAmount());
                        payTotalAmount = payTotalAmount.add(order.getAmount());
                    } else if (order.getRefundStatus().equals(PaymentOrderRefundStatus.FULL_REFUND)) {//全额退款成功
                        //payOrderTotalAmount.add(order.getAmount());  TODO 退款成功，是否要累计支付对账总金额
                        //payTotalAmount.add(order.getAmount());
                        refundOrderTotalAmount = refundOrderTotalAmount.add(order.getAmount());
                        refundTotalAmount = refundTotalAmount.add(order.getAmount());
                    }
                } else {//对账失败，异常
                    failCount = failCount + 1;
                    //TODO 对账失败或异常，是否要判断并累计支付/退款方金额
                }
            }else {//未对账
                notCheckTotalCount = notCheckTotalCount +1;
            }
        }

        checkOverviewResult.setCheckTotalCount(checkTotalCount);
        checkOverviewResult.setSuccessCount(successCount);
        checkOverviewResult.setFailCount(failCount);
        checkOverviewResult.setNotCheckTotalCount(notCheckTotalCount);
        checkOverviewResult.setPayOrderTotalAmount(payOrderTotalAmount);
        checkOverviewResult.setPayTotalAmount(payTotalAmount);
        checkOverviewResult.setRefundOrderTotalAmount(refundOrderTotalAmount);
        checkOverviewResult.setRefundTotalAmount(refundTotalAmount);

        return checkOverviewResult;
    }

    public List<TradeOrder> queryOrderByPage(OrderQueryParamsVo paramsVo, PageInfo page) {
        String orderNumber = paramsVo.getOrderNumber();
        Date startDate = paramsVo.getStartDate();
        Date endDate = paramsVo.getEndDate();
        ServiceResult<List<PaymentOrder>> rpcResult = this.getOrderListForPage(orderNumber, paramsVo.getPayStatusList(), paramsVo.getChannelCodeList(),
                paramsVo.getRefundStatusList(), startDate, endDate, page);
        page.setTotalResult(rpcResult.getPageInfo().getTotalResult());
        List<TradeOrder> orders = new ArrayList<>();
        if (rpcResult.getData() == null) {
            return orders;
        }
        for (PaymentOrder paymentOrder : rpcResult.getData()) {
            TradeOrder order = new TradeOrder();
            if (paymentOrder.getCreateTime() != null) {
                String dateStr = DateFormatUtils.format(paymentOrder.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
                order.setOrderCreateDate(dateStr);
            }
            order.setOrderNumber(paymentOrder.getOrderNumber());
            order.setOrderAmount(paymentOrder.getTotalPremium().toString());
            order.setPayState(paymentOrder.getStatus().getDescription());
            if (null != paymentOrder.getPaymentChannel()) {
                order.setPayChannel(paymentOrder.getPaymentChannel().getChannelName());
            }
            if (paymentOrder.getRefundStatus() != null) {
                order.setRefundStatus(paymentOrder.getRefundStatus().getDescription());
            }
            orders.add(order);
        }
        return orders;
    }

    public TradeOrderDetail loadOrderDetail(String number) {
        ServiceResult<PaymentOrder> result = this.queryPaymentOrderRPC(number);
        PaymentOrder paymentOrder = result.getData();
        if (paymentOrder == null) {
            return null;
        }
        TradeOrderDetail detail = new TradeOrderDetail();
        detail.setOrderNumber(paymentOrder.getOrderNumber());
        if(paymentOrder.getSellingChannel() != null) {
            detail.setSaleChannel(paymentOrder.getSellingChannel().getDescription());
        }
        if (paymentOrder.getCreateTime() != null) {
            String dateStr = DateFormatUtils.format(paymentOrder.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
            detail.setOrderCreateDate(dateStr);
            //TODO 需要paymentOrder记录交易成功时间
            detail.setPayConfirmTime(dateStr);
            detail.setPayCallBackTime(dateStr);
            detail.setPayTime(dateStr);
        }
        if (paymentOrder.getStatus() != null) {
            detail.setPayState(paymentOrder.getStatus().getDescription());
        }
        detail.setOrderAmount(paymentOrder.getTotalPremium().toString());
        List<PaymentTransaction> transactions = paymentOrder.getPaymentTransactions();
        for (PaymentTransaction transaction : transactions) {
            if (PaymentInterfaceType.PAY.equals(transaction.getInterfaceType())) {//过滤只有类型为PAY的交易
                if (transaction.getPaymentStatus().equals(PaymentTransactionStatus.SUCCESS)) {
                    detail.setTransactionStatus(transaction.getPaymentStatus().getDescription());
                    detail.setPayAmount(paymentOrder.getTotalPremium().toString());
                    detail.setPayChannel(transaction.getPaymentChannel().getChannelName());
                    detail.setPayMethod(transaction.getPaymentWay().getName());
                    detail.setPaySerialNumber(transaction.getExternalTransactionNumber());
                    if (paymentOrder.getCheckStatus() != null) {
                        detail.setCheckStatus(paymentOrder.getCheckStatus().getDescription());
                    }
                    break;
                } else {
                    detail.setTransactionStatus(transaction.getPaymentStatus().getDescription());
                    detail.setPayAmount(paymentOrder.getTotalPremium().toString());
                    detail.setPayChannel(transaction.getPaymentChannel().getChannelName());
                    detail.setPayMethod(transaction.getPaymentWay().getName());
                    detail.setPaySerialNumber(transaction.getExternalTransactionNumber());
                    if (paymentOrder.getCheckStatus() != null) {
                        detail.setCheckStatus(paymentOrder.getCheckStatus().getDescription());
                    }
                }
            }
        }

        for (PaymentTransaction transaction : transactions) {
            if (PaymentInterfaceType.REFUND.equals(transaction.getInterfaceType())) {
                if (transaction.getPaymentStatus().equals(PaymentTransactionStatus.SUCCESS) || transaction.getPaymentStatus().equals(PaymentTransactionStatus.FAILED)) {
                    detail.setRefundAmount(paymentOrder.getRefundAmount().toString());
                    detail.setRefundNumber(transaction.getInternalTransactionNumber());
                    if (transaction.getCreateDate() != null) {
                        String dateStr = DateFormatUtils.format(transaction.getCreateDate(), "yyyy-MM-dd HH:mm:ss");
                        detail.setRefundTime(dateStr);
                    }
                }
            }
        }

        for (PaymentOrderItem paymentOrderItem : paymentOrder.getItems()) {
            detail.addOrderItems(transItemVo(paymentOrderItem));
        }

        if (paymentOrder.getRefundStatus() != null) {
            detail.setRefundStatus(paymentOrder.getRefundStatus().getDescription());
        }

        return detail;
    }

    private TradeOrderItem transItemVo(PaymentOrderItem paymentOrderItem) {
        TradeOrderItem item = new TradeOrderItem();

        item.setApplicationNumber(paymentOrderItem.getApplicationNumber());
        item.setPolicyNumber(paymentOrderItem.getApplicationNumber());
        item.setPolicyState("未生效");
        item.setPayAmount(paymentOrderItem.getGrossPremium().toString());

        item.setProductName(paymentOrderItem.getProductName());
        item.setApplicantName(paymentOrderItem.getApplicant().getName());
        item.setPayAmount(paymentOrderItem.getGrossPremium().toString());

        item.setApplicantMobile(paymentOrderItem.getApplicant().getPhoneNumber());
        item.setApplicantIdentityType("身份证");
        item.setApplicantIdentityNumber(paymentOrderItem.getApplicant().getIdNumber());

        return item;
    }

}
