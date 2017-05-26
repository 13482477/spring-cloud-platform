package com.siebre.payment.service.paymentorder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.siebre.basic.query.PageInfo;
import com.siebre.basic.service.ServiceResult;
import com.siebre.payment.controller.paymentgateway.order.PaymentOrderRequest;
import com.siebre.payment.controller.paymentgateway.order.PaymentOrderResponse;
import com.siebre.payment.entity.enums.PaymentOrderCheckStatus;
import com.siebre.payment.entity.enums.PaymentOrderPayStatus;
import com.siebre.payment.entity.enums.PaymentTransactionStatus;
import com.siebre.payment.entity.enums.PolicyRoleType;
import com.siebre.payment.entity.paymentchannel.PaymentChannel;
import com.siebre.payment.entity.paymentorder.PaymentOrder;
import com.siebre.payment.entity.paymentorderitem.PaymentOrderItem;
import com.siebre.payment.entity.paymentorderitem.PolicyLibility;
import com.siebre.payment.entity.paymentorderitem.PolicyRole;
import com.siebre.payment.mapper.paymentchannel.PaymentChannelMapper;
import com.siebre.payment.mapper.paymentorder.PaymentOrderMapper;
import com.siebre.payment.mapper.paymentorderitem.PaymentOrderItemMapper;
import com.siebre.payment.mapper.paymentorderitem.PolicyLibilityMapper;
import com.siebre.payment.mapper.paymentorderitem.PolicyRoleMapper;
import com.siebre.payment.service.serialnumber.SerialNumberService;
import com.siebre.payment.vo.statistics.PaymentChannelTransactionVo;
import com.siebre.payment.vo.unionpayment.DonutVo;

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

    public ServiceResult<List<PaymentOrder>> queryPaymentOrder(PageInfo pageInfo) {
        return null;
    }

    public ServiceResult<Map<String, Object>> queryPaymentSummary(PageInfo pageInfo) {
        return null;
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
        //保存order
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setPaymentWayCode(orderRequest.getPaymentWayCode());
        paymentOrder.setOrderNumber(serialNumberService.nextValue("sale_order"));
        paymentOrder.setPaymentOrderItems(orderRequest.getPaymentOrderItems());
        //设置order状态为未支付
        paymentOrder.setStatus(PaymentOrderPayStatus.UNPAID);
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
            //save insuredPerson
            if ("n".equalsIgnoreCase(paymentOrderItem.getSamePerson())) {
                PolicyRole insuredPerson = paymentOrderItem.getInsuredPerson();
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
            totalPremium = totalPremium.add(paymentOrderItem.getPremium());
            totalInsuredAmount = totalInsuredAmount.add(paymentOrderItem.getInsuredAmount());
        }
        paymentOrder.setTotalInsuredAmount(totalInsuredAmount);
        paymentOrder.setTotalPremium(totalPremium);
    }

	@Autowired
	private PaymentChannelMapper paymentChannelMapper;

    private void processPolicyLibilityAmount(PaymentOrderItem paymentOrderItem) {
        BigDecimal totalInsuredAmount = BigDecimal.ZERO;
        BigDecimal totalPremium = BigDecimal.ZERO;
        for (PolicyLibility libility : paymentOrderItem.getLibilities()) {
            totalPremium = totalPremium.add(libility.getPremium());
            totalInsuredAmount = totalInsuredAmount.add(libility.getInsuredAmount());
        }
        paymentOrderItem.setPremium(totalPremium);
        paymentOrderItem.setInsuredAmount(totalInsuredAmount);
    }

    public PaymentOrder queryPaymentOrder(String orderNumber) {
        PaymentOrder order = paymentOrderMapper.selectByOrderNumberleftjoin(orderNumber);
        List<PaymentOrderItem> items = paymentOrderItemMapper.selectByPaymentOrderId(order.getId());
        order.setPaymentOrderItems(items);
        return order;
    }

    public ServiceResult<PaymentOrder> queryPaymentOrderRPC(String orderNumber) {
        ServiceResult<PaymentOrder> result = new ServiceResult<PaymentOrder>();
        result.setData(this.queryPaymentOrder(orderNumber));
        return result;
    }

    public ServiceResult<List<PaymentOrder>> getOrderListForPage(String orderNumber, PaymentOrderPayStatus orderPayStatus, String channelName,
                                                                 Date startDate, Date endDate, PageInfo page) {
        ServiceResult<List<PaymentOrder>> result = new ServiceResult<>();
        List<PaymentOrder> orders = paymentOrderMapper.selectOrderByPage(orderNumber, orderPayStatus, channelName, startDate, endDate, page);
        result.setData(orders);
        result.setPageInfo(page);
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

        for (int i = 0; i < datas.length; i ++) {
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
			donutVo.setLabel(this.getLabelName((Long) data.get("paymentChannelId"), paymentChannelList));
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
			donutVo.setLabel(this.getLabelName((Long) data.get("paymentChannelId"), paymentChannelList));
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

	public ServiceResult<List<List<Integer>>> getPaymentWaySuccessCount() {
		String[] paymentWayCodes = {
				"ALIPAY_WEB_PAY",
				"WECHAT_SCAN_PAY",
				"UNIONPAY_ACP_PAY",
				"ALIPAY_TRADE_WAP_PAY",
				"WECHAT_PUBLIC_PAY",
				"ALLIN_ACP_PAY",
		};

		List<Map<String, Object>> paidDatas = this.paymentOrderMapper.getPaymentWayCount(PaymentOrderPayStatus.PAID);
		List<Map<String, Object>> payErrorDatas = this.paymentOrderMapper.getPaymentWayCount(PaymentOrderPayStatus.PAYERROR);
		List<Map<String, Object>> payingDatas = this.paymentOrderMapper.getPaymentWayCount(PaymentOrderPayStatus.PAYING);

		List<List<Integer>> result = new ArrayList<List<Integer>>();

		List<Integer> result1 = new ArrayList<Integer>();
		result1.add(this.getCountByPayWayCode(paidDatas, paymentWayCodes[0]));
		result1.add(this.getCountByPayWayCode(paidDatas, paymentWayCodes[1]));
		result1.add(this.getCountByPayWayCode(paidDatas, paymentWayCodes[2]));
		result1.add(this.getCountByPayWayCode(paidDatas, paymentWayCodes[3]));
		result1.add(this.getCountByPayWayCode(paidDatas, paymentWayCodes[4]));
		result1.add(this.getCountByPayWayCode(paidDatas, paymentWayCodes[5]));
		result.add(result1);

		List<Integer> result2 = new ArrayList<Integer>();
		result2.add(this.getCountByPayWayCode(payErrorDatas, paymentWayCodes[0]));
		result2.add(this.getCountByPayWayCode(payErrorDatas, paymentWayCodes[1]));
		result2.add(this.getCountByPayWayCode(payErrorDatas, paymentWayCodes[2]));
		result2.add(this.getCountByPayWayCode(payErrorDatas, paymentWayCodes[3]));
		result2.add(this.getCountByPayWayCode(payErrorDatas, paymentWayCodes[4]));
		result2.add(this.getCountByPayWayCode(payErrorDatas, paymentWayCodes[5]));
		result.add(result2);

		List<Integer> result3 = new ArrayList<Integer>();
		result3.add(this.getCountByPayWayCode(payingDatas, paymentWayCodes[0]));
		result3.add(this.getCountByPayWayCode(payingDatas, paymentWayCodes[1]));
		result3.add(this.getCountByPayWayCode(payingDatas, paymentWayCodes[2]));
		result3.add(this.getCountByPayWayCode(payingDatas, paymentWayCodes[3]));
		result3.add(this.getCountByPayWayCode(payingDatas, paymentWayCodes[4]));
		result3.add(this.getCountByPayWayCode(payingDatas, paymentWayCodes[5]));
		result.add(result3);

		return ServiceResult.<List<List<Integer>>>builder().success(true).data(result).build();
	}

	private Integer getCountByPayWayCode(List<Map<String, Object>> dataMaps, String paymentWayCode) {
		for (Map<String, Object> dataMap : dataMaps) {
			if (StringUtils.equals(paymentWayCode, (CharSequence) dataMap.get("paymentWayCode"))) {
				return ((Long) dataMap.get("count")).intValue();
			}
		}
		return 0;
	}

	public ServiceResult<List<PaymentChannelTransactionVo>> countPaymentChannelTransaction() {
		List<PaymentChannelTransactionVo> result = this.paymentOrderMapper.countPaymentChannelTransaction();
		return ServiceResult.<List<PaymentChannelTransactionVo>>builder().success(true).data(result).build();
	}

}
