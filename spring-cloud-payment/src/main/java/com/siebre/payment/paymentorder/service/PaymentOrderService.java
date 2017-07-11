package com.siebre.payment.paymentorder.service;

import com.siebre.basic.query.PageInfo;
import com.siebre.basic.service.ServiceResult;
import com.siebre.payment.entity.enums.*;
import com.siebre.payment.paymentaccount.entity.BankAccount;
import com.siebre.payment.paymentaccount.entity.PaymentAccount;
import com.siebre.payment.paymentaccount.entity.WeChatAccount;
import com.siebre.payment.paymentaccount.service.PaymentAccountService;
import com.siebre.payment.paymentchannel.entity.PaymentChannel;
import com.siebre.payment.paymentchannel.mapper.PaymentChannelMapper;
import com.siebre.payment.paymentcheck.vo.CheckOrderVo;
import com.siebre.payment.paymentcheck.vo.CheckOverviewResult;
import com.siebre.payment.paymentgateway.vo.*;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymentorder.mapper.PaymentOrderMapper;
import com.siebre.payment.paymentorder.vo.OrderQueryParamsVo;
import com.siebre.payment.paymentorder.vo.TradeOrder;
import com.siebre.payment.paymentorder.vo.TradeOrderItem;
import com.siebre.payment.paymentorderitem.entity.PaymentOrderItem;
import com.siebre.payment.paymentorderitem.mapper.PaymentOrderItemMapper;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.paymentway.service.PaymentWayService;
import com.siebre.payment.policylibility.entity.PolicyLibility;
import com.siebre.payment.policylibility.mapper.PolicyLibilityMapper;
import com.siebre.payment.policyrole.entity.PolicyRole;
import com.siebre.payment.policyrole.mapper.PolicyRoleMapper;
import com.siebre.payment.serialnumber.service.SerialNumberService;
import com.siebre.payment.statistics.vo.DonutVo;
import com.siebre.payment.statistics.vo.PaymentChannelTransactionVo;
import com.siebre.payment.utils.MsgUtil;
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
    private PaymentAccountService paymentAccountService;

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

    @Autowired
    private PaymentWayService paymentWayService;

    /**
     * 更新订单为指定状态
     *
     * @param order
     * @param status
     */
    @Transactional("db")
    public void updateOrderStatus(PaymentOrder order, PaymentOrderPayStatus status, Date successDate) {
        PaymentOrder orderForUpdate = new PaymentOrder();
        orderForUpdate.setId(order.getId());
        orderForUpdate.setStatus(status);
        if (PaymentOrderPayStatus.PAID.equals(status)) {
            orderForUpdate.setPayTime(successDate);
            order.setPayTime(successDate);
        }
        this.paymentOrderMapper.updateByPrimaryKeySelective(orderForUpdate);
        order.setStatus(status);
    }

    /**
     * 该方法用于创建order
     * 1.校验必要信息
     * 2.Vo装换成数据库对象模型
     * 3.保存
     *
     * @param unifiedPayRequest
     * @return
     */
    @Transactional("db")
    public PaymentOrderResponse creatPaymentOrder(UnifiedPayRequest unifiedPayRequest) {
        //必填项校验
        MsgUtil validateInfo = validateNecessaryInfo(unifiedPayRequest);
        if (ReturnCode.FAIL.equals(validateInfo.getResult())) {
            return PaymentOrderResponse.FAIL(validateInfo.getMsg());
        }
        //幂等性校验
        MsgUtil mdxMsf = idempotencyValidate(unifiedPayRequest.getMessageId());
        if (ReturnCode.FAIL.equals(mdxMsf.getResult())) {
            PaymentOrder order = (PaymentOrder) mdxMsf.getData();
            PaymentAccount account = this.paymentAccountService.getPaymentAccountById(order.getPaymentAccountId());
            order.setPaymentAccount(account);
            return PaymentOrderResponse.SUCCESS("订单已创建", order);
        }
        //模型转换
        PaymentOrder paymentOrder = transfer(unifiedPayRequest);
        saveNewOrder(paymentOrder);
        return PaymentOrderResponse.SUCCESS("创建成功", paymentOrder);
    }

    private void initOrderDate(PaymentOrder paymentOrder) {
        PaymentWay paymentWay = this.paymentWayService.getPaymentWay(paymentOrder.getPaymentWayCode());
        paymentOrder.setChannelCode(paymentWay.getPaymentChannel().getChannelCode());
        paymentOrder.setOrderNumber(serialNumberService.nextValue("sale_order"));
        //设置order状态为待支付
        paymentOrder.setStatus(PaymentOrderPayStatus.UNPAID);
        //设置对账状态为未对账
        paymentOrder.setCheckStatus(PaymentOrderCheckStatus.NOT_CONFIRM);
        //设置订单锁定状态为未锁定
        paymentOrder.setLockStatus(PaymentOrderLockStatus.UNLOCK);
        paymentOrder.setRefundAmount(BigDecimal.ZERO);
        paymentOrder.setCreateTime(new Date());
    }

    private void saveNewOrder(PaymentOrder paymentOrder) {
        //初始化Order的一些状态
        initOrderDate(paymentOrder);
        this.paymentOrderMapper.insert(paymentOrder);
        if (paymentOrder.getPaymentAccount() != null) {
            this.paymentAccountService.insertPaymentAccount(paymentOrder.getId(), paymentOrder.getPaymentAccount());
            paymentOrder.setPaymentAccountId(paymentOrder.getPaymentAccount().getId());
        }
        for (PaymentOrderItem paymentOrderItem : paymentOrder.getItems()) {
            paymentOrderItem.setPaymentOrderId(paymentOrder.getId());
            //save insured
            PolicyRole insured = paymentOrderItem.getInsured();
            insured.setPolicyRoleType(PolicyRoleType.INSURED);
            policyRoleMapper.insert(insured);
            paymentOrderItem.setInsuredPersonId(insured.getId());
            //save applicant
            if ("SELF".equalsIgnoreCase(insured.getRelatedToApplicant())) {
                PolicyRole applicant = paymentOrderItem.getApplicant();
                applicant.setPolicyRoleType(PolicyRoleType.APPLICANT);
                policyRoleMapper.insert(applicant);
                paymentOrderItem.setApplicantId(applicant.getId());
            }
            //save item
            paymentOrderItemMapper.insert(paymentOrderItem);
        }
    }

    /**
     * 幂等性校验
     *
     * @param messageId
     * @return
     */
    private MsgUtil idempotencyValidate(String messageId) {
        PaymentOrder order = paymentOrderMapper.selectByMessageId(messageId);
        if (order != null) {
            MsgUtil msgUtil = new MsgUtil(ReturnCode.FAIL, "该订单已创建");
            msgUtil.setData(order);
            return msgUtil;
        }
        return new MsgUtil(ReturnCode.SUCCESS, "");
    }

    /**
     * UnifiedPayRequest模型转换为PaymentOrder
     *
     * @param unifiedPayRequest
     * @return
     */
    private PaymentOrder transfer(UnifiedPayRequest unifiedPayRequest) {
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setMessageId(unifiedPayRequest.getMessageId());
        paymentOrder.setNotificationUrl(unifiedPayRequest.getNotificationUrl());
        paymentOrder.setReturnUrl(unifiedPayRequest.getReturnUrl());

        UnifiedPayOrder unifiedPayOrder = unifiedPayRequest.getPaymentOrder();
        paymentOrder.setPaymentWayCode(unifiedPayOrder.getPaymentWayCode());
        paymentOrder.setAmount(unifiedPayOrder.getAmount());
        paymentOrder.setCurrency(unifiedPayOrder.getCurrency());
        paymentOrder.setSummary(unifiedPayOrder.getSummary());

        PaymentAccount paymentAccount = null;
        if (unifiedPayOrder.getBankAccount() != null) {
            paymentAccount = paymentAccountService.transferToPaymentAccount(unifiedPayOrder.getBankAccount());
        } else if (unifiedPayOrder.getWeChatAccount() != null) {
            paymentAccount = paymentAccountService.transferToPaymentAccount(unifiedPayOrder.getWeChatAccount());
        }
        paymentOrder.setPaymentAccount(paymentAccount);

        List<UnifiedPayItem> items = unifiedPayOrder.getItems();
        List<PaymentOrderItem> orderItems = new ArrayList<>();
        for (UnifiedPayItem unifiedPayItem : items) {
            PaymentOrderItem orderItem = new PaymentOrderItem();
            orderItem.setApplicant(unifiedPayItem.getApplicant());
            orderItem.setApplicationNumber(unifiedPayItem.getApplicationNumber());
            orderItem.setGrossPremium(unifiedPayItem.getGrossPremium());
            orderItem.setInceptionDate(unifiedPayItem.getInceptionDate());
            orderItem.setInsured(unifiedPayItem.getInsured());
            orderItem.setPlannedEndDate(unifiedPayItem.getPlannedEndDate());
            orderItem.setPolicyNumber(unifiedPayItem.getPolicyNumber());
            orderItem.setProductCode(unifiedPayItem.getProductCode());
            orderItem.setProductName(unifiedPayItem.getProductName());
            orderItems.add(orderItem);
        }
        paymentOrder.setItems(orderItems);

        return paymentOrder;
    }

    /**
     * 校验前端传递的模型是否缺一些必要信息
     *
     * @param unifiedPayRequest
     * @return
     */
    private MsgUtil validateNecessaryInfo(UnifiedPayRequest unifiedPayRequest) {
        if (StringUtils.isBlank(unifiedPayRequest.getMessageId())) {
            return new MsgUtil(ReturnCode.FAIL, "messageId不能为空");
        }
        if (StringUtils.isBlank(unifiedPayRequest.getPaymentOrder().getPaymentWayCode())) {
            return new MsgUtil(ReturnCode.FAIL, "paymentWayCode不能为空");
        }
        UnifiedPayOrder unifiedPayOrder = unifiedPayRequest.getPaymentOrder();
        if (unifiedPayOrder == null) {
            return new MsgUtil(ReturnCode.FAIL, "订单信息不能为空");
        }
        BankAccount bankAccount = unifiedPayOrder.getBankAccount();
        if (bankAccount != null) {
            if (StringUtils.isBlank(bankAccount.getAcountNumber())) {
                return new MsgUtil(ReturnCode.FAIL, "银行卡账号不能为空");
            }
            if (StringUtils.isBlank(bankAccount.getHolderName())) {
                return new MsgUtil(ReturnCode.FAIL, "银行卡持有人姓名不能为空");
            }
        }
        WeChatAccount weChatAccount = unifiedPayOrder.getWeChatAccount();
        if (weChatAccount != null) {
            if (StringUtils.isBlank(weChatAccount.getOpenid())) {
                return new MsgUtil(ReturnCode.FAIL, "OPENID不能为空");
            }
        }
        return new MsgUtil(ReturnCode.SUCCESS, "");
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
        if (order.getStatus().equals(PaymentOrderPayStatus.PAID)) {
            orderVo.setAmount(order.getAmount());
            orderVo.setRealAmount(order.getAmount());
            orderVo.setCheckType("支付");
        } else {
            orderVo.setAmount(order.getRefundAmount());
            orderVo.setRealAmount(order.getRefundAmount());
            orderVo.setCheckType("退款");
        }
        orderVo.setCheckStatus(order.getCheckStatus().getDescription());

        String dateStrCreate = DateFormatUtils.format(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss");

        //投保信息
        orderVo.setApplicationNumber("1494469393190");
        orderVo.setPremium(order.getAmount());
        orderVo.setApplicationCreateTime(dateStrCreate);
        orderVo.setApplicationPayStatus(order.getStatus().getDescription());

        //支付信息
        orderVo.setOrderNumber(order.getOrderNumber());
        //orderVo.setAmount(order.getAmount());
        orderVo.setCreateTime(dateStrCreate);
        orderVo.setPayStatus(order.getStatus().getDescription());

        //第三方支付信息
        PaymentChannel paymentChannel = paymentChannelMapper.selectByChannelCode(order.getChannelCode());
        orderVo.setChannelCode(paymentChannel.getChannelCode());
        orderVo.setChannelName(paymentChannel.getChannelName());
        orderVo.setExternalTransactionNumber(order.getExternalOrderNumber());//暂时用订单号
        orderVo.setPayTime(dateStrCreate);
        //orderVo.setRealAmount(order.getAmount());
        orderVo.setRealPayStatus(order.getStatus().getDescription());

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
                                                                 Date startDate, Date endDate, PageInfo page) {
        ServiceResult<List<PaymentOrder>> result = new ServiceResult<>();
        List<PaymentOrder> orders = paymentOrderMapper.selectOrderByPage(orderNumber, orderPayStatusList, channelCodeList, startDate, endDate, page);
        result.setData(orders);
        result.setPageInfo(page);
        return result;
    }

    public ServiceResult<List<CheckOrderVo>> selectCheckOrderByPage(String orderNumber, List<String> channelCodeList,
                                                                    List<PaymentOrderPayStatus> payStatusList, List<PaymentOrderCheckStatus> checkStatusList,
                                                                    Date startDate, Date endDate, PageInfo pageInfo) {

        ServiceResult<List<CheckOrderVo>> result = new ServiceResult<>();
        List<PaymentOrder> orders = paymentOrderMapper.selectCheckOrderByPage(orderNumber, channelCodeList, payStatusList, checkStatusList, startDate, endDate, pageInfo);

        List<CheckOrderVo> checkOrderVos = new ArrayList<>();
        for (PaymentOrder order : orders) {
            CheckOrderVo checkOrderVo = new CheckOrderVo();
            checkOrderVo.setOrderNumber(order.getOrderNumber());
            if (order.getChannelCode() == null)
                continue;
            PaymentChannel paymentChannel = paymentChannelMapper.selectByChannelCode(order.getChannelCode());
            checkOrderVo.setChannelCode(paymentChannel.getChannelCode());
            checkOrderVo.setChannelName(paymentChannel.getChannelName());
            if (order.getStatus().equals(PaymentOrderPayStatus.PAID)) {
                checkOrderVo.setAmount(order.getAmount());
                checkOrderVo.setCheckType("支付");
            } else {
                checkOrderVo.setAmount(order.getRefundAmount());
                checkOrderVo.setCheckType("退款");
            }

            checkOrderVo.setPayStatus(order.getStatus().getDescription());
            checkOrderVo.setCheckStatus(order.getCheckStatus().getDescription());
            if (order.getCheckTime() != null) {
                String dateStr = DateFormatUtils.format(order.getCheckTime(), "yyyy-MM-dd HH:mm:ss");
                checkOrderVo.setCheckTime(dateStr);
            }
            if (order.getPayTime() != null) {
                String dateStr = DateFormatUtils.format(order.getPayTime(), "yyyy-MM-dd HH:mm:ss");
                checkOrderVo.setPayTime(dateStr);
            }

            checkOrderVos.add(checkOrderVo);
        }

        result.setData(checkOrderVos);
        result.setPageInfo(pageInfo);
        return result;
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

    public CheckOverviewResult getOrdersOverview(String orderNumber, List<String> channelCodeList,
                                                 List<PaymentOrderPayStatus> payStatusList, List<PaymentOrderCheckStatus> checkStatusList,
                                                 Date checkStartDate, Date checkEndDate) {

        List<PaymentOrder> orders = paymentOrderMapper.selectCheckOrderByPage(orderNumber, channelCodeList, payStatusList, checkStatusList, checkStartDate, checkEndDate, null);

        CheckOverviewResult checkOverviewResult = new CheckOverviewResult();
        int checkTotalCount = 0;//对账总笔数
        int successCount = 0;//成功笔数
        int failCount = 0;//失败笔数
        int unusualCount = 0;//异常笔数
        int notCheckTotalCount = 0;//未对账总笔数
        BigDecimal payOrderTotalAmount = BigDecimal.ZERO;//支付信息-订单金额
        BigDecimal payTotalAmount = BigDecimal.ZERO;//支付信息-支付金额
        BigDecimal refundOrderTotalAmount = BigDecimal.ZERO;//退款信息-订单金额
        BigDecimal refundTotalAmount = BigDecimal.ZERO;//退款信息-退款金额

        for (PaymentOrder order : orders) {
            if (!order.getCheckStatus().equals(PaymentOrderCheckStatus.NOT_CONFIRM)) {//已对账
                checkTotalCount = checkTotalCount + 1;
                if (order.getCheckStatus().equals(PaymentOrderCheckStatus.SUCCESS)) {//对账成功
                    successCount = successCount + 1;
                    if (order.getStatus().equals(PaymentOrderPayStatus.PAID)) {//支付成功且未退款
                        payOrderTotalAmount = payOrderTotalAmount.add(order.getAmount());
                        payTotalAmount = payTotalAmount.add(order.getAmount());
                    } else if (order.getStatus().equals(PaymentOrderPayStatus.PART_REFUND) || order.getStatus().equals(PaymentOrderPayStatus.FULL_REFUND)) {//部分退款，全额退款
                        refundOrderTotalAmount = refundOrderTotalAmount.add(order.getRefundAmount());
                        refundTotalAmount = refundTotalAmount.add(order.getRefundAmount());
                    }
                } else if (order.getCheckStatus().equals(PaymentOrderCheckStatus.FAIL)) {//对账失败
                    failCount = failCount + 1;
                } else if (order.getCheckStatus().equals(PaymentOrderCheckStatus.UNUSUAL)) {//对账异常
                    unusualCount = unusualCount + 1;
                }
            } else {//未对账
                notCheckTotalCount = notCheckTotalCount + 1;
            }
        }

        checkOverviewResult.setCheckTotalCount(checkTotalCount);
        checkOverviewResult.setSuccessCount(successCount);
        checkOverviewResult.setFailCount(failCount);
        checkOverviewResult.setUnusualCount(unusualCount);
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
                startDate, endDate, page);
        page.setTotalResult(rpcResult.getPageInfo().getTotalResult());
        List<TradeOrder> orders = new ArrayList<>();
        if (rpcResult.getData() == null) {
            return orders;
        }
        for (PaymentOrder paymentOrder : rpcResult.getData()) {
            TradeOrder order = new TradeOrder();
            order.setOrderId(paymentOrder.getId());
            order.setOrderNumber(paymentOrder.getOrderNumber());
            order.setOrderCreateDate(DateFormatUtils.format(paymentOrder.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            order.setOrderState(paymentOrder.getStatus().getDescription());
            order.setOrderAmount(paymentOrder.getAmount());
            if (null != paymentOrder.getPaymentChannel()) {
                order.setPayChannel(paymentOrder.getPaymentChannel().getChannelName());
            }
            orders.add(order);
        }
        return orders;
    }

    public TradeOrder loadOrderDetail(String number) {
        ServiceResult<PaymentOrder> result = this.queryPaymentOrderRPC(number);
        PaymentOrder paymentOrder = result.getData();
        if (paymentOrder == null) {
            return null;
        }
        TradeOrder detail = new TradeOrder();
        detail.setOrderId(paymentOrder.getId());
        //订单信息
        detail.setOrderNumber(paymentOrder.getOrderNumber());
        detail.setOrderCreateDate(DateFormatUtils.format(paymentOrder.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        detail.setOrderState(paymentOrder.getStatus().getDescription());
        detail.setOrderAmount(paymentOrder.getAmount());
        detail.setSaleChannel(paymentOrder.getSellingChannel() == null ? SellingChannel.MOBILE_SALE_APP.getDescription() : paymentOrder.getSellingChannel().getDescription());

        //保单摘要
        if (paymentOrder.getItems().size() > 0) {
            PaymentOrderItem item = paymentOrder.getItems().get(0);
            detail.setProductName(item.getProductName());
            detail.setApplicationNumber(item.getApplicationNumber());
            detail.setPolicyNumber(item.getPolicyNumber());
            detail.setApplication(item.getApplicant().getName());
            detail.setPhoneNumber(item.getApplicant().getPhoneNumber());
            detail.setIdType(item.getApplicant().getIdType().getDescription());
            detail.setIdNumber(item.getApplicant().getIdNumber());
        }

        //支付信息
        PaymentTransaction payTransaction = getLastPayTransaction(paymentOrder.getPaymentTransactions());
        if (payTransaction != null) {
            detail.setPayChannel(payTransaction.getPaymentChannel().getChannelName());
            if(paymentOrder.getPayTime() != null) {
                detail.setPayTime(DateFormatUtils.format(paymentOrder.getPayTime(), "yyyy-MM-dd HH:mm:ss"));
            }
            detail.setPayWay(payTransaction.getPaymentWay().getName());
            detail.setPayAmount(payTransaction.getPaymentAmount());
            detail.setPayStatus(payTransaction.getPaymentStatus().getDescription());
        }

        return detail;
    }

    private PaymentTransaction getLastPayTransaction(List<PaymentTransaction> paymentTransactions) {
        List<PaymentTransaction> pays = new ArrayList<>();
        for (PaymentTransaction transaction : paymentTransactions) {
            if (PaymentInterfaceType.PAY.equals(transaction.getInterfaceType())) {//过滤只有类型为PAY的交易
                pays.add(transaction);
            }
        }
        Collections.sort(pays, new Comparator<PaymentTransaction>() {
            @Override
            public int compare(PaymentTransaction o1, PaymentTransaction o2) {
                int flag = o1.getCreateDate().compareTo(o2.getCreateDate());
                return flag;
            }
        });
        return pays.get(pays.size() == 0 ? 0 : pays.size() - 1);
    }

}
