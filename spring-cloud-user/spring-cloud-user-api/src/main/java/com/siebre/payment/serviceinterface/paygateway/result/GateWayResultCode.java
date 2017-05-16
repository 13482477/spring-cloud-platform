package com.siebre.payment.serviceinterface.paygateway.result;

/**
 * 
 * Title: Class GateWayResultCode
 * Description:
 *	支付系统统一异常码定义
 *
 * @author chunling.yan
 * @email    
*  @version 1.0 2017年3月14日 
 */
public  enum GateWayResultCode {
	
    /******************************公司统一异常********************************************/
    /* 200，success																	  */
    /* 500，fail																		  */
    /**********************************************************************************/
	
	/**成功 标识，公司统一*/
    SUCCESS("200","SUCCESS"),
    
    /**系统失败，公司统一*/
    FAIL("500","FAIL"),
    
    
    /******************************通用异常********************************************/
    /* 系统标识：		p01                                                             */ 
    /* 异常码区间：		0001-1000														*/ 
    /********************************************************************************/
    
    
    /**参数为空*/
    COMMON_PARAM_NULL("P010001","必要参数为空！"),
    
    /**参数无效*/
    COMMON_PARAM_INVALID("P010002","参数无效！"),
    
    
    
    
    /**通知订单异常*/
    COMMON_NOTIFY_ORRDER_ERROR("P010003","通知订单异常！"),
    
    
    
    
    /******************************支付异常********************************************/
    /* 系统标识：		p01																*/
    /* 异常码区间：		1001-2000														*/		
    /* 支付异常：		1001-1500														*/
    /* 支付查询异常：	1501-2000														*/
    /********************************************************************************/
    PAY_DETAIL_STATUS_ERROR("P011001","支付记录状态异常!"),
    PAY_SEQ_NO_REPEAT_ERROR("P011002","支付流水号重复!"),
    PAY_SAVE_DB_ERROR("P011003","支付存储数据出错!"),
    PAY_NOTIFY_PARAM_ERROR("P011004","支付结果通知参数无效!"),
    PAY_ORDER_VALIDATE_ERROR("P011005","支付校验订单失败!"),
    PAY_ORDER_AMOUNT_ERROR("P011006","支付金额大于订单金额!"),
    PAY_DETAIL_ERROR("P011502","支付记录查询异常!"),
    PAY_DETAIL_NOT_FOUND("P011501","支付记录不存在!"),

    
    /******************************退款异常*********************************************/
    /* 系统标识：		p01																*/
 	/* 异常码区间：		2001-3000														*/			
 	/* 退款异常：		2001-2500														*/	
 	/* 退款查询异常：	2501-3000														*/	
 	/********************************************************************************/
    REFUND_ORDER_NOT_FOUND("P012001","退款记录不存在"),

    VALID_REFUND_ERROR("P012002","退款校验失败!"),
    REFUND_INTERNAL_ERROR("P012003","退款内部错误!"),
    REFUND_NOTIFY_VALID_ERROR("P012004","退款通知验证异常!"),
    REFUND_MERORDER_DETAIL_NOT_FOUND("P012005","合并订单明细(商户)不存在!"),
    REFUND_MERORDER_DETAIL_UPDATE_RECORDS_ERROR("P012006","合并订单明细（商户）更新出错，存在多条记录"),
    REFUND_MER_REFUND_TOTAL_NOT_FOUND("P012006","无退款失败交易"),
    REFUND_OMS_INVOKE_ERROR("P012007","OMS接口调用失败!"),
    REFUND_SEQ_NO_REPEAT("P012008","退款流水号重复!"),
	REFUND_BATCH_ERROR("P012009","退款批次失败!"),
	REFUND_TRADE_NO_NULL("P012010","请求退款交易号为空!"),
	REFUND_CALCULATE_ERROR("P012011","退款计算失败!"),
    REFUND_AGAIN_DB_ERROR("P012012","再次退款数据存储失败!"),
    REFUND_COMBINE_DB_ERROR("P012013","整单退款数据存储失败!"),
    REFUND_NOTIFY_DB_ERROR("P012014","退款通知数据库操作失败!"),
    REFUND_CHANNEL_NOT_FOUND("P012015","退款渠道未找到!"),
    REFUND_ORDER_VALIDATE_ERROR("P012016","退款订单号校验失败!"),
    REFUND_OVER_LIMIT("P012017","当天退款次数过多!"),
    REFUND_TRADE_NOT_FOUND("P012018","没有原始支付数据,无法进行退款!"),
    REFUND_ORDER_ERROR("P012019","查询退款记录异常！"),
    REFUND_ORDER_REFUND_FINISH("P012020","订单已经退款完毕!"),
    REFUND_GENERATOR_SEQ_ERROR("P012021","生成退款序列号失败"),

    
    /******************************支付渠道异常********************************************/
    /* 系统标识：		p01																  */		
    /* 异常码区间：		3001-9999														  */	
    /* 支付宝异常：		3001-3100														  */
    /**********************************************************************************/
    ALIPAY_CHANNEL_APP_PAY_ERROR("P013001","支付宝渠道APP付款失败!"),
    ALIPAY_CHANNEL_REFUND_ERROR("P013002","支付宝渠道退款失败!"),
    ALIPAY_CHANNEL_PAY_QUERY_ERROR("P013003","支付宝渠道支付查询失败!"),
    ALIPAY_CHANNEL_REFUND_QUERY_ERROR("P013004","支付宝渠道退款查询失败!"),
    ALIPAY_CHANNEL_PAY_NOTIFY_ERROR("P013005","支付宝渠道APP支付回调失败!"),
    ALIPAY_CHANNEL_REFUND_NOTIFY_ERROR("P013006","支付宝渠道APP退款回调失败!"),
    ALIPAY_CHANNEL_WEB_PAY_ERROR("P013007","支付宝渠道网页支付失败!"),
    ALIPAY_CHANNEL_WEB_REFUND_ERROR("P013008","支付宝渠道网页退款失败!"),
    ALIPAY_CHANNEL_WEB_PAY_NOTIFY_ERROR("P013009","支付宝渠道网页支付回调失败!"),
    ALIPAY_CHANNEL_WEB_REFUND_NOTIFY_ERROR("P013010","支付宝渠道网页退款回调失败!"),
    ALIPAY_CHANNEL_VERIFY_ERROR("P013011","支付宝验签失败!"),
	WEIXIN_CHANNEL_VERIFY_ERROR("P013012","微信验签失败!"),
    WEIXIN_CHANNEL_PAY_QUERY_ERROR("P013013","微信渠道支付查询失败!"),
    WEIXIN_CHANNEL_REFUND_QUERY_ERROR("P013014","微信渠道退款查询失败!"),
    ALIPAY_CHANNEL_BALANCE_QUERY_ERROR("P013012","支付宝用户余额查询失败!"),
    UNIONPAY_CHANNEL_PARAM_ERR("P014001","银联支付构造参数异常!"),
    UNIONPAY_CHANNEL_SEND_ERR("P014002","银联支付发送请求异常!"),
    UNIONPAY_CHANNEL_RETURN_ERR("P014003","银联支付返回错误!"),
    UNIONPAY_CHANNEL_NOTIFY_VALID_ERR("P014004","银联支付回调校验错误!");;

    public String code;
    public String message;


    private GateWayResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
