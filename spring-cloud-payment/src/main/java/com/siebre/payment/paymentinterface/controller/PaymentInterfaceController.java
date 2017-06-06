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
@RequestMapping("/v1/paymentinterface")
@CrossOrigin("*")
public class PaymentInterfaceController {

    @Autowired
    private PaymentInterfaceService paymentInterfaceService;

    /**
     * 根据分页信息查询支付接口
     *
     * @param pageInfo
     * @return
     */
    @ApiOperation(value = "根据分页信息查询支付接口", notes = "根据分页信息查询支付接口")
    @RequestMapping(method = GET)
    public ServiceResult<List<PaymentInterface>> list(PageInfo pageInfo) {
        return paymentInterfaceService.selectByPage(pageInfo);
    }

    /**
     * 创建支付接口
     *
     * @param paymentInterface
     * @return
     */
    @ApiOperation(value = "创建支付接口", notes = "创建支付接口")
    @RequestMapping(method = POST)
    public ServiceResult<PaymentInterface> create(@RequestBody PaymentInterface paymentInterface) {
        return paymentInterfaceService.createPaymentInterface(paymentInterface);
    }

    /**
     * 更新支付接口
     *
     * @param paymentInterface
     * @return
     */
    @ApiOperation(value = "更新支付接口", notes = "更新支付接口")
    @RequestMapping(method = PUT)
    public ServiceResult<PaymentInterface> update(@RequestBody PaymentInterface paymentInterface) {
        return paymentInterfaceService.updatePaymentInterface(paymentInterface);
    }

    /**
     * 查询详细
     *
     * @param id 支付接口id
     * @return
     */
    @ApiOperation(value = "查询支付接口详细信息", notes = "查询支付接口详细信息")
    @RequestMapping(value = "/{id}", method = GET)
    public ServiceResult<PaymentInterface> detail(@PathVariable Long id) {
        return paymentInterfaceService.selectById(id);
    }

    /**
     * 删除支付接口
     *
     * @param id 支付接口id
     * @return
     */
    @ApiOperation(value = "删除支付接口", notes = "删除支付接口")
    @RequestMapping(value = "/{id}", method = DELETE)
    public ServiceResult delete(@PathVariable Long id) {
        return paymentInterfaceService.deletePaymentInterfaceById(id);
    }
}
