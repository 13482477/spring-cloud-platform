package com.siebre.payment.paymentorder.mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.siebre.basic.query.PageInfo;
import com.siebre.payment.entity.enums.PaymentOrderPayStatus;
import com.siebre.payment.entity.enums.PaymentTransactionStatus;
import com.siebre.payment.paymentorder.entiry.PaymentOrder;
import com.siebre.payment.statistics.vo.PaymentChannelTransactionVo;

@Repository
public interface PaymentOrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PaymentOrder record);

    int insertSelective(PaymentOrder record);

    PaymentOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PaymentOrder record);

    int updateByPrimaryKey(PaymentOrder record);
    
    PaymentOrder selectByOrderNumber(String orderNumber);
    
    PaymentOrder selectByOrderNumberleftjoin(String orderNumber);

    /**
     * 数据库分页方法
     * @param orderNumber
     * @param orderPayStatus
     * @param channelName
     * @param pageInfo
     * @return
     */
    List<PaymentOrder> selectOrderByPage(@Param("orderNumber") String orderNumber, @Param("status") PaymentOrderPayStatus orderPayStatus,@Param("channelName")String channelName,
                                         @Param("startDate")Date startDate,@Param("endDate")Date endDate, PageInfo pageInfo);

    /**
     * Add by Huang Tianci
     * 这个方法不能用于分页，因为会将查询出来的冗余数据给合并
     * @param orderNumber
     * @param applicationNumber
     * @param status
     * @param page
     * @return
     */
    @Deprecated
    List<PaymentOrder> selectOrderJoinTransaction(@Param("orderNumber") String orderNumber, @Param("applicationNumber") String applicationNumber, @Param("status")PaymentTransactionStatus status, PageInfo page);

    List<Map<String,Object>> selectOrderSummery(@Param("orderNumber") String orderNumber,@Param("applicationNumber") String applicationNumber,@Param("channelId") Long channelId);

    int updateOrderStatusToClose(String orderNumber);
    
    public BigDecimal getSuccessedPaymentAmount();
    
    public Integer getSuccessedPaymentCount();
    
    public BigDecimal getFaildPaymentAmount();
    
    public Integer getFaildPaymentCount();
    
    public Integer getCount();
    
    public BigDecimal getTotalAmountByDateRange(@Param("startDate")Date startDate, @Param("endDate")Date endDate);
    
    public List<Map<String, Object>> getChannelSuccessedCount();
    
    public List<Map<String, Object>> getChannelSuccessedAmount();
    
    public List<Map<String, Object>>getPaymentWayCount(@Param("status")PaymentOrderPayStatus status);
    
    public List<PaymentChannelTransactionVo> countPaymentChannelTransaction();
}