package com.siebre.payment.billing.service;

import com.siebre.basic.utils.JsonUtil;
import com.siebre.payment.billing.base.ReconcileFileManager;
import com.siebre.payment.entity.enums.PaymentTransactionStatus;
import com.siebre.payment.paymentchannel.entity.PaymentChannel;
import com.siebre.payment.paymenthandler.allinpay.sdk.AllinpayConfig;
import com.siebre.payment.paymenthandler.allinpay.sdk.AllinpayConstants;
import com.siebre.payment.paymenthandler.allinpay.sdk.XmlUtils;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
import com.siebre.payment.utils.messageconvert.ConvertToXML;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.codehaus.jackson.JsonNode;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.zeroturnaround.zip.ZipUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 通联对账文件下载类
 * 开发文档地址：http://113.108.182.3:8282/techsp/helper/filedetail/tlt/filedetail140.html
 * 对账文件格式说明：http://113.108.182.3:8282/techsp/helper/filedetail/tlt/filedetail166.html
 */
@Service("allinReconcileFileManager")
public class AllinReconcileFileManager extends ReconcileFileManager {

    private Logger logger = LoggerFactory.getLogger(AllinReconcileFileManager.class);

    @Override
    public File downloadReconcileFile(Date startDate, Date endDate) {
        PaymentTransaction reconcileTransaction = createReconcileTransaction();

        logger.info("generate request message.");
        String requestMessage = generateRequestMessage(reconcileTransaction, startDate, endDate);

        logger.info("requestMessage: {}", requestMessage);
        if(StringUtils.isBlank(requestMessage)) {
            return null;
        }

        byte[] bytes = doRequest(reconcileTransaction, requestMessage);
        transactionService.updateBySelective(reconcileTransaction);

        File file  = convertFile(bytes, startDate, endDate);

        return file;
    }

    protected String generateRequestMessage(PaymentTransaction reconcileTransaction, Date startDate, Date endDate) {
        String requestMessage = "";
        PaymentChannel channel = channelService.queryByChannelCode(AllinpayConfig.CHANNEL_CODE).getData();

        requestMessage = marshalDownloadReconFile(channel, reconcileTransaction, "0", "1", "1", startDate, endDate);

        reconcileTransaction.setPaymentChannel(channel);
        reconcileTransaction.setPaymentChannelId(channel.getId());
        reconcileTransaction.setRequestStr(requestMessage);
        reconcileTransaction.setReceiver(AllinpayConfig.CHANNEL_CODE);
        return requestMessage;
    }

    protected byte[] doRequest(PaymentTransaction reconcileTransaction, String requestMessage) {
        String responseMessage = "";
        String requestUrl = AllinpayConfig.RECONCILE_URL;
        try {
            responseMessage = XmlUtils.post(requestUrl, XmlUtils.sign(requestMessage));
            //reconcileTransaction.setResponseStr(responseMessage);
            reconcileTransaction.setPaymentStatus(PaymentTransactionStatus.RECON_SUCCESS);
            reconcileTransaction.setUpdateDate(new Date());
            logger.info("responseMessage: {}", responseMessage);
        } catch (Exception e) {
            logger.info("请求对账文件异常：", e);
            e.printStackTrace();
            reconcileTransaction.setPaymentStatus(PaymentTransactionStatus.RECON_FAILED);
            reconcileTransaction.setUpdateDate(new Date());
        }
        String zipStr = unmarshalDownloadReconFile(responseMessage);
        byte[] bytes = Base64.decodeBase64(zipStr);
        return bytes;
    }

    protected File convertFile(byte[] bytes, Date startDate, Date endDate) {
        String localDir = ReconcileFileManager.LOCAL_DIR;
        PaymentChannel channel = channelService.queryByChannelCode(AllinpayConfig.CHANNEL_CODE).getData();

        File outputDir = new File(localDir);

        outputDir.mkdirs();
        ZipUtil.unpack(new ByteArrayInputStream(bytes), outputDir);

        StringBuilder filePath = new StringBuilder(localDir);
        filePath.append("PDS");
        filePath.append(channel.getMerchantCode());
        filePath.append(DateFormatUtils.format(startDate, "yyyyMMddHHmmss"));
        filePath.append("-");
        filePath.append(DateFormatUtils.format(endDate, "yyyyMMddHHmmss"));
        filePath.append(".txt");
        return new File(filePath.toString());
    }

    /**
     *
     * @param channel
     * @param status 交易状态条件, 0成功,1失败, 2全部
     * @param type 0.按完成日期 1.按提交日期
     * @param contfee 0.不需手续费，1.包含手续费
     * @param startDate
     * @param endDate
     * @return
     */
    public String marshalDownloadReconFile(PaymentChannel channel, PaymentTransaction reconcileTransaction, String status, String type, String contfee, Date startDate, Date endDate) {
        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding("GBK");
        Element root = document.addElement("AIPG");

        String id = UUID.randomUUID().toString();
        reconcileTransaction.setInternalTransactionNumber(id);

        marshalHeader(AllinpayConstants.TRX_ReconFileDownload, root, id, channel);

        Element trans = root.addElement("QTRANSREQ");
        add(trans, "MERCHANT_ID", channel.getMerchantCode());
        add(trans, "STATUS", status);
        add(trans, "TYPE", type);
        add(trans, "CONTFEE", contfee);
        add(trans, "START_DAY", DateFormatUtils.format(startDate, "yyyyMMddHHmmss"));
        add(trans, "END_DAY", DateFormatUtils.format(endDate, "yyyyMMddHHmmss"));

        return document.asXML();
    }

    private void marshalHeader(String transCode, Element root, String id, PaymentChannel channel) {
        Element info = root.addElement("INFO");
        add(info, "TRX_CODE", transCode);
        add(info, "VERSION", "03");
        add(info, "DATA_TYPE", "2");
        add(info, "LEVEL", "5");
        add(info, "USER_NAME", channel.getMerchantName());
        add(info, "USER_PASS", channel.getMerchantPwd());
        add(info, "REQ_SN", id);
        add(info, "SIGNED_MSG", "");
    }

    private void add(Element e, String name, Object value) {
        if (value != null)
            e.addElement(name).setText(String.valueOf(value));
    }

    public String unmarshalDownloadReconFile(String message) {
        int start = message.indexOf("<CONTENT>") + 9;
        int end = message.indexOf("</CONTENT>");
        return message.substring(start, end);
    }
}
