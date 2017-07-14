package com.siebre.payment.billing.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayDataDataserviceBillDownloadurlQueryRequest;
import com.alipay.api.response.AlipayDataDataserviceBillDownloadurlQueryResponse;
import com.siebre.basic.utils.JsonUtil;
import com.siebre.payment.billing.base.ReconcileFileManager;
import com.siebre.payment.paymentchannel.entity.PaymentChannel;
import com.siebre.payment.paymenthandler.alipay.sdk.AlipayConfig;
import com.siebre.payment.paymenttransaction.entity.PaymentTransaction;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.paymentway.mapper.PaymentWayMapper;
import com.siebre.payment.utils.http.HttpTookit;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.jooq.tools.csv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zeroturnaround.zip.ZipUtil;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author Huang Tianci
 *         查询对账单下载地址接口：https://docs.open.alipay.com/api_15/alipay.data.dataservice.bill.downloadurl.query
 */
@Service("alipayReconcileFileManager")
public class AlipayReconcileFileManager extends ReconcileFileManager {

    private Logger logger = LoggerFactory.getLogger(AlipayReconcileFileManager.class);

    @Autowired
    private PaymentWayMapper wayMapper;

    @Override
    public File downloadReconcileFile(Date startDate, Date endDate) throws IOException {
        String dateStr = DateFormatUtils.format(startDate, "yyyy-MM-dd");
        PaymentTransaction reconcileTransaction = createReconcileTransaction();
        File tradeFile = null;
        File signcustomerFile = null;

        logger.info("get trade download url");
        /*String tradeDownloadUrl = generateRequestMessage(dateStr, "trade");
        logger.info("tradeDownloadUrl: {}", tradeDownloadUrl);
        if (StringUtils.isNotBlank(tradeDownloadUrl)) {
            String zipstr = doRequest(tradeDownloadUrl);
            tradeFile = convertFile(zipstr, tradeDownloadUrl);
        }*/

        String signcustomerDownloadUrl = generateRequestMessage(dateStr, "signcustomer");
        logger.info("signcustomerDownloadUrl: {}", signcustomerDownloadUrl);
        if (StringUtils.isNotBlank(signcustomerDownloadUrl)) {
            //String zipstr = doRequest(signcustomerDownloadUrl);
            signcustomerFile = convertFile(null, signcustomerDownloadUrl);
        }

        return mergeFile(tradeFile, signcustomerFile, dateStr);
    }

    /**
     * 将两个CSV文件合并成一个
     */
    private File mergeFile(File tradeFile, File signcustomerFile, String dateStr) throws IOException {
        if (tradeFile == null && signcustomerFile != null) {
            return signcustomerFile;
        }
        if (tradeFile != null && signcustomerFile == null) {
            return tradeFile;
        }
        if (tradeFile == null && signcustomerFile == null) {
            return null;
        }
        PaymentChannel channel = channelService.queryByChannelCode(AlipayConfig.CHANNEL_CODE).getData();
        String localDir = ReconcileFileManager.LOCAL_DIR;
        StringBuilder filePath = new StringBuilder(localDir);
        filePath.append(channel.getMerchantCode()).append("_");
        filePath.append(dateStr);
        filePath.append(".csv");
        File file = new File(filePath.toString());
        file.createNewFile();
        List<String[]> temp = new ArrayList<>();

        readCSV(tradeFile, temp);
        readCSV(signcustomerFile, temp);
        writeToFile(file, temp);
        return file;
    }

    private void writeToFile(File file, List<String[]> temp) throws IOException {
        BufferedWriter csvWtriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"), 1024);
        for (String[] row : temp) {
            List<Object> rowList = Arrays.asList(row);
            writeRow(rowList, csvWtriter);
        }
        csvWtriter.flush();
    }

    private void writeRow(List<Object> row, BufferedWriter csvWriter) throws IOException {
        for (Object data : row) {
            StringBuffer sb = new StringBuffer();
            String rowStr = sb.append("\"").append(data).append("\",").toString();
            csvWriter.write(rowStr);
        }
        csvWriter.newLine();
    }

    private void readCSV(File tradeFile, List<String[]> temp) throws IOException {
        InputStream inputStream = new FileInputStream(tradeFile);
        CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
        List<String[]> lines = reader.readAll();
        int size = lines.size();

        if (size != 0) {
            List<String[]> effLines = lines.subList(5, size - 4);
            for (String[] lineParts : effLines) {
                temp.add(lineParts);
            }
        }
        reader.close();
    }

    //type: trade    signcustomer
    protected String generateRequestMessage(String dateStr, String type) {
        PaymentWay paymentWay = wayMapper.getPaymentWayByCode(AlipayConfig.WAY_TRADE_PAY);
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.TRADE_REQUEST_URL,
                paymentWay.getAppId(), paymentWay.getSecretKey(),
                "json", AlipayConfig.INPUT_CHARSET_UTF,
                paymentWay.getPublicKey(), paymentWay.getEncryptionMode().getDescription());
        AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();

        Map<String, String> params = new HashedMap();
        params.put("bill_type", type);
        params.put("bill_date", dateStr);
        request.setBizContent(JsonUtil.mapToJson(params));

        AlipayDataDataserviceBillDownloadurlQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            logger.error("查询对账单下载地址出错", e);
        }
        if (response.isSuccess()) {
            System.out.println("调用成功");
            System.out.println(response.getBillDownloadUrl());
            return response.getBillDownloadUrl();
        } else {
            System.out.println("调用失败");
            return null;
        }
    }

   /* protected String doRequest(String requestMessage) throws IOException {
        *//*String zipStr = HttpTookit.doGet(requestMessage, null);
        return zipStr;*//*


    }*/

    protected File convertFile(String zipstr, String downloadUrl) throws IOException {

        CloseableHttpClient client = HttpClients.createDefault();
        //发送get请求
        HttpGet request = new HttpGet(downloadUrl);
        HttpResponse response = client.execute(request);
        InputStream inputStream = response.getEntity().getContent();

        String localDir = ReconcileFileManager.LOCAL_DIR;
        File outputDir = new File(localDir);

        outputDir.mkdirs();
        /*Project proj = new Project();
        Expand expand = new Expand();
        expand.setProject(proj);
        expand.setTaskType("unzip");
        expand.setTaskName("unzip");
        expand.setEncoding("GBK");

        expand.setSrc(new File());
        expand.setDest(new File(localDir));
        expand.execute();*/
        //Charset gbk = Charset.forName("gb2312");
        //ZipInputStream in = new ZipInputStream(inputStream, gbk);
        //ZipUtil.unpack(inputStream, outputDir);
        //readZipCvsFile(inputStream);

        String fileName = getFileName(downloadUrl);
        StringBuilder filePath = new StringBuilder(localDir);
        filePath.append(fileName);
        filePath.append("_业务明细.csv");
        return new File(filePath.toString());
    }

    public void readZipCvsFile(InputStream inputStream) throws IOException {
        ZipInputStream in = new ZipInputStream(inputStream);
        //不解压直接读取,加上gbk解决乱码问题
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "gbk"));
        ZipEntry zipFile;
        //循环读取zip中的cvs文件，无法使用jdk自带，因为文件名中有中文
        while ((zipFile = in.getNextEntry()) != null) {
            if (zipFile.isDirectory()) {
                //如果是目录，不处理
            }
            //获得cvs名字
            String fileName = zipFile.getName();
            System.out.println("-----" + fileName);
            //检测文件是否存在
            if (fileName != null && fileName.indexOf(".") != -1) {
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            }
        }
        //关闭流
        br.close();
        in.close();
    }

    private String getFileName(String downloadUrl) {
        int start = StringUtils.indexOf(downloadUrl, "downloadFileName=") + 17;
        int end = StringUtils.indexOf(downloadUrl, ".csv.zip");
        String fileName = StringUtils.substring(downloadUrl, start, end);
        return fileName;
    }
}
