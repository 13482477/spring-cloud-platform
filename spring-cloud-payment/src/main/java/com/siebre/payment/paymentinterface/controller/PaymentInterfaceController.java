package com.siebre.payment.paymentinterface.controller;

import com.siebre.basic.query.PageInfo;
import com.siebre.basic.service.ServiceResult;
import com.siebre.payment.paymentinterface.entity.PaymentInterface;
import com.siebre.payment.paymentinterface.service.PaymentInterfaceService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * @author Huang Tianci
 *         支付接口
 */
@RestController
public class PaymentInterfaceController {

    @Autowired
    private PaymentInterfaceService paymentInterfaceService;

    @ApiOperation(value = "根据分页信息查询支付接口", notes = "根据分页信息查询支付接口")
    @RequestMapping(value = "/api/v1/paymentInterfaces", method = GET)
    public ServiceResult<List<PaymentInterface>> list(PageInfo pageInfo) {
        return paymentInterfaceService.selectByPage(pageInfo);
    }

    @ApiOperation(value = "创建支付接口", notes = "创建支付接口")
    @RequestMapping(value = "/api/v1/paymentInterfaces", method = POST)
    public ServiceResult<PaymentInterface> create(@RequestBody PaymentInterface paymentInterface) {
        return paymentInterfaceService.createPaymentInterface(paymentInterface);
    }

    @ApiOperation(value = "更新支付接口", notes = "更新支付接口")
    @RequestMapping(value = "/api/v1/paymentInterfaces/{interfaceId}", method = PUT)
    public ServiceResult<PaymentInterface> update(@PathVariable Long interfaceId, @RequestBody PaymentInterface paymentInterface) {
        paymentInterface.setId(interfaceId);
        return paymentInterfaceService.updatePaymentInterface(paymentInterface);
    }

    @ApiOperation(value = "查询支付接口详细信息", notes = "查询支付接口详细信息")
    @RequestMapping(value = "/api/v1/paymentInterfaces/{interfaceId}", method = GET)
    public ServiceResult<PaymentInterface> detail(@PathVariable Long interfaceId) {
        return paymentInterfaceService.selectById(interfaceId);
    }

    @ApiOperation(value = "删除支付接口", notes = "删除支付接口")
    @RequestMapping(value = "/api/v1/paymentInterfaces/{interfaceId}", method = DELETE)
    public ServiceResult delete(@PathVariable Long interfaceId) {
        return paymentInterfaceService.deletePaymentInterfaceById(interfaceId);
    }
}
