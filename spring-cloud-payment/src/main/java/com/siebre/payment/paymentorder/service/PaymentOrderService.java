package com.siebre.payment.paymentorder.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.siebre.basic.query.PageInfo;
import com.siebre.payment.entity.enums.PaymentOrderCheckStatus;
import com.siebre.payment.entity.enums.PaymentOrderPayStatus;
import com.siebre.payment.entity.enums.PolicyRoleType;
import com.siebre.payment.paymentchannel.entity.PaymentChannel;
import com.siebre.payment.paymentchannel.mapper.PaymentChannelMapper;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymentorder.mapper.PaymentOrderMapper;
import com.siebre.payment.paymentorderitem.entity.PaymentOrderItem;
import com.siebre.payment.paymentorderitem.mapper.PaymentOrderItemMapper;
import com.siebre.payment.policylibility.entity.PolicyLibility;
import com.siebre.payment.policylibility.mapper.PolicyLibilityMapper;
import com.siebre.payment.policyrole.entity.PolicyRole;
import com.siebre.payment.policyrole.mapper.PolicyRoleMapper;
import com.siebre.payment.serialnumber.service.SerialNumberService;
import com.siebre.payment.statistics.vo.DonutVo;
import com.siebre.payment.statistics.vo.PaymentChannelTransactionVo;

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

    /**
     * 创建order和order item   libility    applicant   insurePerson
     *
     * @param orderRequest
     * @param request
     * @return
     */
    @Transactional("db")
    public void createPaymentOrderAndItems(String paymetnWayCode, PaymentOrder paymentOrder) {
        //保存order
        paymentOrder.setPaymentWayCode(paymetnWayCode);
        paymentOrder.setOrderNumber(serialNumberService.nextValue("sale_order"));
        //设置order状态为未支付
        paymentOrder.setStatus(PaymentOrderPayStatus.UNPAID);
        paymentOrder.setCreateTime(new Date());
        this.processTotalAmount(paymentOrder);
        this.paymentOrderMapper.insert(paymentOrder);

        for (PaymentOrderItem paymentOrderItem : paymentOrder.getPaymentOrderItems()) {
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
    }

    /**
     * 计算总保额和总保费
     *
     * @param paymentOrder
     * @param paymentOrderItems
     */
    private void processTotalAmount(PaymentOrder paymentOrder) {
        BigDecimal totalInsuredAmount = BigDecimal.ZERO;
        BigDecimal totalPremium = BigDecimal.ZERO;

        for (PaymentOrderItem paymentOrderItem : paymentOrder.getPaymentOrderItems()) {
            this.processPolicyLibilityAmount(paymentOrderItem);
            totalPremium = totalPremium.add(paymentOrderItem.getPremium());
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
        paymentOrderItem.setPremium(totalPremium);
        paymentOrderItem.setInsuredAmount(totalInsuredAmount);
    }

    public PaymentOrder queryPaymentOrder(String orderNumber) {
        PaymentOrder order = paymentOrderMapper.selectByOrderNumberleftjoin(orderNumber);
        List<PaymentOrderItem> items = paymentOrderItemMapper.selectByPaymentOrderId(order.getId());
        order.setPaymentOrderItems(items);
        return order;
    }

    public List<PaymentOrder> getOrderListForPage(String orderNumber, PaymentOrderPayStatus orderPayStatus, String channelName,
                                                                 Date startDate, Date endDate, PageInfo page) {
        List<PaymentOrder> orders = paymentOrderMapper.selectOrderByPage(orderNumber, orderPayStatus, channelName, startDate, endDate, page);
        return orders;
    }

    @Transactional("db")
    public void paymentCheckConfirm(String orderNumber) {
        PaymentOrder paymentOrder = this.paymentOrderMapper.selectByOrderNumber(orderNumber);
        paymentOrder.setCheckStatus(PaymentOrderCheckStatus.SUCCESS);
        paymentOrderMapper.updateByPrimaryKeySelective(paymentOrder);
    }

    public BigDecimal getSuccessPaymentAmount() {
        BigDecimal result = this.paymentOrderMapper.getSuccessedPaymentAmount();
        return result;
    }

    public Integer getSuccessPaymentCount() {
        return this.paymentOrderMapper.getSuccessedPaymentCount();
    }

    public BigDecimal getFaildPaymentAmount() {
        BigDecimal result = this.paymentOrderMapper.getFaildPaymentAmount();
        return result == null ? BigDecimal.ZERO : result;
    }

    public Integer getFaildPaymentCount() {
        Integer result = this.paymentOrderMapper.getFaildPaymentCount();
        return result == null ? 0 : result;
    }

    public BigDecimal getConversionRate() {
        Integer totalCount = this.paymentOrderMapper.getCount();
        Integer successedCount = this.paymentOrderMapper.getSuccessedPaymentCount();
        BigDecimal result = new BigDecimal(successedCount).multiply(new BigDecimal(100)).divide(new BigDecimal(totalCount), RoundingMode.HALF_UP);
        return result;
    }

    public BigDecimal[] getTotalAmountOfThisWeek() {
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

        return datas;
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

	public List<DonutVo> getChannelSuccessedCount() {
		List<PaymentChannel> paymentChannelList = this.paymentChannelMapper.selectAll();

		List<Map<String, Object>> dataList = this.paymentOrderMapper.getChannelSuccessedCount();

		List<DonutVo> result = new ArrayList<DonutVo>();

		for (Map<String, Object> data : dataList) {
			DonutVo donutVo = new DonutVo();
			donutVo.setLabel(this.getLabelName((Long) data.get("paymentChannelId"), paymentChannelList));
			donutVo.setValue(new BigDecimal((Long) data.get("count")));
			result.add(donutVo);
		}

		return result;
	}

	public List<DonutVo> getChannelSuccessedAmount() {
		List<PaymentChannel> paymentChannelList = this.paymentChannelMapper.selectAll();

		List<Map<String, Object>> dataList = this.paymentOrderMapper.getChannelSuccessedAmount();

		List<DonutVo> result = new ArrayList<DonutVo>();

		for (Map<String, Object> data : dataList) {
			DonutVo donutVo = new DonutVo();
			donutVo.setLabel(this.getLabelName((Long) data.get("paymentChannelId"), paymentChannelList));
			donutVo.setValue((BigDecimal) data.get("totalAmount"));
			result.add(donutVo);
		}

		return result;
	}

	private String getLabelName(Long paymentChannelId, List<PaymentChannel> paymentChannelList) {
		for (PaymentChannel paymentChannel : paymentChannelList) {
			if (paymentChannelId == paymentChannel.getId()) {
				return paymentChannel.getChannelName();
			}
		}
		return null;
	}

	public List<List<Integer>> getPaymentWaySuccessCount() {
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

		return result;
	}

	private Integer getCountByPayWayCode(List<Map<String, Object>> dataMaps, String paymentWayCode) {
		for (Map<String, Object> dataMap : dataMaps) {
			if (StringUtils.equals(paymentWayCode, (CharSequence) dataMap.get("paymentWayCode"))) {
				return ((Long) dataMap.get("count")).intValue();
			}
		}
		return 0;
	}

	public List<PaymentChannelTransactionVo> countPaymentChannelTransaction() {
		List<PaymentChannelTransactionVo> result = this.paymentOrderMapper.countPaymentChannelTransaction();
		return result;
	}

}
