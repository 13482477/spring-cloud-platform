/**
 * Created on 2016年10月9日
 * filename: PayItemResultDTO.java
 * Description: 
 *
 */
package com.siebre.payment.vo.unionpayment;

import java.io.Serializable;




/**
 * Title: Class PayItemResultDTO
 * Description:
 *	支付结果对象
 *
 * @author chunling.yan
 * @email    
*  @version 1.0 2017年3月14日 
 */
public class PayResponseVo  implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 6634059401675854907L;

	/**
     * 支付url
     */
    private String url;

    private String payWayCode;//支付方式

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPayWayCode() {
		return payWayCode;
	}

	public void setPayWayCode(String payWayCode) {
		this.payWayCode = payWayCode;
	}


	
    
   
    
    
}
