package com.siebre.payment.paymentchannel.transfer;

import com.siebre.basic.service.ServiceResult;
import com.siebre.payment.paymentchannel.entity.PaymentChannel;
import com.siebre.payment.paymentchannel.service.PaymentChannelService;
import com.siebre.payment.paymenthandler.allinpay.sdk.AllinpayConstants;
import com.siebre.payment.paymenthandler.wechatpay.sdk.WeChatConfig;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.paymentway.service.PaymentWayService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Huang Tianci
 *         上传和下载证书服务
 */
@Service("uploadAndDownloadCertService")
public class UploadAndDownloadCertService {

    public static final String CER = "apiClientCertCer";

    public static final String PKCS = "apiClientCertPkcs";

    public static final String SUFFIX_CER = "cer";
    public static final String SUFFIX_P12 = "p12";

    private static Map<String, String> suffixsMapping = new HashMap();

    private static Map<String, Map<String, String>> fileNameMapping = new HashMap<>();

    private AtomicBoolean initsuffixsFlag = new AtomicBoolean(false);

    private AtomicBoolean initFileMappingFlag = new AtomicBoolean(false);

    @Autowired
    private PaymentChannelService paymentChannelService;

    @Autowired
    private PaymentWayService paymentWayService;

    /**
     * 上传证书
     *
     * @param file
     * @return
     */
    @Transactional("db")
    public ServiceResult uploadCert(MultipartFile file, String channelCode) throws IOException {
        //校验文件后缀名
        if (!validateFileName(file.getOriginalFilename(), channelCode)) {
            return ServiceResult.builder().success(Boolean.FALSE).message("文件后缀名不正确，支持" + suffixsMapping.get(channelCode)).build();
        }
        String suffix = file.getOriginalFilename().split("\\.")[1];
        PaymentChannel paymentChannel = paymentChannelService.queryByChannelCode(channelCode).getData();
        List<PaymentWay> ways = paymentWayService.getPaymentWayByChannelId(paymentChannel.getId());
        for (PaymentWay way : ways) {
            if (SUFFIX_CER.equals(suffix)) {
                way.setApiClientCertCer(file.getBytes());
            } else if (SUFFIX_P12.equals(suffix)) {
                way.setApiClientCertPkcs(file.getBytes());
            }
            paymentWayService.updatePaymentWay(way);
        }
        return ServiceResult.builder().success(Boolean.TRUE).message(ServiceResult.SUCCESS_MESSAGE).build();
    }

    @Transactional("db")
    public void downloadCert(String channelCode, String filedName, HttpServletResponse response) throws Exception {
        PaymentChannel paymentChannel = paymentChannelService.queryByChannelCode(channelCode).getData();
        List<PaymentWay> ways = paymentWayService.getPaymentWayByChannelId(paymentChannel.getId());
        byte[] data = null;
        for (PaymentWay way : ways) {
            if (CER.equals(filedName)) {
                data = way.getApiClientCertCer();
            } else if (PKCS.equals(filedName)) {
                data = way.getApiClientCertPkcs();
            }
            if (data != null) {
                break;
            }
        }
        String fileName = getFileName(channelCode, filedName);
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream;charset=UTF-8");
        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        outputStream.write(data);
        outputStream.flush();
        outputStream.close();
    }

    /**
     * 获取文件名
     *
     * @param channelCode
     * @param filedName
     * @return
     */
    private String getFileName(String channelCode, String filedName) {
        initFileNameMapping();
        Map<String, String> mapping = fileNameMapping.get(channelCode);
        return mapping.get(filedName);
    }

    /**
     * 校验文件后缀名
     *
     * @return
     */
    private boolean validateFileName(String originalFilename, String channelCode) {
        initSuffixs();
        String suffix = originalFilename.split("\\.")[1];
        String suffixs = suffixsMapping.get(channelCode);
        if (StringUtils.isEmpty(suffixs)) {
            return false;
        }
        return suffixs.contains(suffix);
    }

    private void initSuffixs() {
        if (!initsuffixsFlag.getAndSet(true)) {
            suffixsMapping.put(WeChatConfig.CHANNEL_CODE, SUFFIX_P12);
            suffixsMapping.put(AllinpayConstants.CHANNEL_CODE, SUFFIX_P12 + "," + SUFFIX_CER);
        }
    }

    private void initFileNameMapping() {
        if (!initFileMappingFlag.getAndSet(true)) {
            Map<String, String> wechatMapping = new HashMap<>();
            wechatMapping.put(CER, "apiclient-cert.cer");
            wechatMapping.put(PKCS, "apiclient-cert.p12");
            fileNameMapping.put(WeChatConfig.CHANNEL_CODE, wechatMapping);

            Map<String, String> allinpayMapping = new HashMap<>();
            allinpayMapping.put(CER, "allinpay-pds.cer");
            allinpayMapping.put(PKCS, "20036800000096104.p12");
            fileNameMapping.put(AllinpayConstants.CHANNEL_CODE, allinpayMapping);
        }
    }

}
