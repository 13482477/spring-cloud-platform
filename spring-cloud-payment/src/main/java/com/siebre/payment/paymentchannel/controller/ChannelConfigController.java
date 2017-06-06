package com.siebre.payment.paymentchannel.controller;

import com.siebre.basic.service.ServiceResult;
import com.siebre.payment.paymentchannel.transfer.*;
import com.siebre.payment.paymentchannel.vo.AliPayConfigVo;
import com.siebre.payment.paymentchannel.vo.AllinPayConfigVo;
import com.siebre.payment.paymentchannel.vo.BaofooConfigVo;
import com.siebre.payment.paymentchannel.vo.WeChatConfigVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * @author Huang Tianci
 *         渠道配置
 */
@RestController
@RequestMapping("/v1/channel")
@CrossOrigin("*")
public class ChannelConfigController {

    @Autowired
    private AlipayConfigTransfer alipayConfigTransfer;

    @Autowired
    private WeChatConfigTransfer weChatConfigTransfer;

    @Autowired
    private AllinpayConfigTransfer allinpayConfigTransfer;

    @Autowired
    private UploadAndDownloadCertService uploadAndDownloadCertService;

    @Autowired
    private BaofooConfigTransfer baofooConfigTransfer;

    @ApiOperation(value = "支付宝配置详情", notes = "支付宝配置详情")
    @RequestMapping(value = "/alipay", method = GET)
    public ServiceResult<AliPayConfigVo> alipayConfigDetail() {
        return alipayConfigTransfer.transferToVo();
    }

    @ApiOperation(value = "支付宝配置接口", notes = "支付宝配置接口")
    @RequestMapping(value = "/alipay", method = PUT)
    public ServiceResult<AliPayConfigVo> alipayConfig(@RequestBody AliPayConfigVo aliPayConfigVo) {
        return alipayConfigTransfer.transfer(aliPayConfigVo);
    }

    @ApiOperation(value = "微信配置详情", notes = "微信配置详情")
    @RequestMapping(value = "/weChat", method = GET)
    public ServiceResult<WeChatConfigVo> weChatConfigDetail() {
        return weChatConfigTransfer.transferToVo();
    }

    @ApiOperation(value = "微信配置接口", notes = "微信配置接口")
    @RequestMapping(value = "/weChat", method = PUT)
    public ServiceResult<WeChatConfigVo> weChatConfig(@RequestBody WeChatConfigVo weChatConfigVo) throws IOException {
        return weChatConfigTransfer.transfer(weChatConfigVo);
    }

    @ApiOperation(value = "通联配置详情", notes = "通联配置详情")
    @RequestMapping(value = "/allinpay", method = GET)
    public ServiceResult<AllinPayConfigVo> allinpayConfigDetail() {
        return allinpayConfigTransfer.detailTransfer();
    }

    @ApiOperation(value = "通联配置接口", notes = "通联配置接口")
    @RequestMapping(value = "/allinpay", method = PUT)
    public ServiceResult<AllinPayConfigVo> allinpayConfig(@RequestBody AllinPayConfigVo allinPayConfigVo) {
        return allinpayConfigTransfer.transfer(allinPayConfigVo);
    }

    @ApiOperation(value = "宝付配置详情(快捷支付)", notes = "宝付配置详情(快捷支付)")
    @RequestMapping(value = "/baofoo", method = GET)
    public ServiceResult<BaofooConfigVo> baofooConfigDetail() {
        return baofooConfigTransfer.detailTransfer();
    }

    @ApiOperation(value = "宝付配置接口(快捷支付)", notes = "宝付配置接口(快捷支付)")
    @RequestMapping(value = "/baofoo", method = PUT)
    public ServiceResult<BaofooConfigVo> baofooConfig(@RequestBody BaofooConfigVo baofooConfigVo) {
        return baofooConfigTransfer.transfer(baofooConfigVo);
    }

    //
    @ApiOperation(value = "上传证书接口", notes = "上传证书接口")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "form", name = "channelCode", dataType = "String", required = true, value = "渠道代码", defaultValue = "WECHAT_PAY", allowableValues = "WECHAT_PAY,ALLIN_PAY,BAOFOO_PAY"),
            @ApiImplicitParam(paramType = "form", name = "file", dataType = "file", required = true, value = "文件")
    })
    @RequestMapping(value = "/uploadCert", method = POST)
    public ServiceResult uploadCert(@RequestParam(value = "file") MultipartFile file, @RequestParam(value = "channelCode") String channelCode) throws IOException {
        return uploadAndDownloadCertService.uploadCert(file, channelCode);
    }

    @ApiOperation(value = "下载证书接口", notes = "下载证书接口")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "channelCode", dataType = "String", required = true, value = "渠道代码", allowableValues = "WECHAT_PAY,ALLIN_PAY,BAOFOO_PAY"),
            @ApiImplicitParam(paramType = "query", name = "filedName", dataType = "String", required = true, value = "字段名", defaultValue = "apiClientCertCer", allowableValues = "apiClientCertCer,apiClientCertPkcs")
    })
    @RequestMapping(value = "/downloadCert", method = GET)
    public void downloadCert(@RequestParam(value = "channelCode") String channelCode, @RequestParam(value = "filedName") String filedName, HttpServletResponse response) throws Exception {
        uploadAndDownloadCertService.downloadCert(channelCode, filedName, response);
    }

}
