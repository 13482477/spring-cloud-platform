package com.siebre.payment.billing.base;

import com.google.common.collect.Lists;
import com.siebre.basic.applicationcontext.SpringContextUtil;
import com.siebre.payment.billing.entity.*;
import com.siebre.payment.billing.mapper.*;
import com.siebre.payment.entity.enums.PaymentOrderCheckStatus;
import com.siebre.payment.entity.enums.PaymentOrderPayStatus;
import com.siebre.payment.paymentorder.entity.PaymentOrder;
import com.siebre.payment.paymentorder.mapper.PaymentOrderMapper;
import com.siebre.payment.utils.DateUtil;
import com.siebre.payment.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.jooq.tools.csv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by tianci.huang on 2017/6/26.
 */
@Service("reconcileManager")
public class ReconcileManager {

    private static Logger logger = LoggerFactory.getLogger(ReconcileManager.class);

    @Autowired
    private PaymentOrderMapper orderMapper;

    @Autowired
    private ReconJobMapper jobMapper;

    @Autowired
    private ReconJobInstanceMapper jobInstanceMapper;

    @Autowired
    private ReconDataSetMapper dataSetMapper;

    @Autowired
    private ReconDataSourceMapper reconDataSourceMapper;

    @Autowired
    private ReconDataFieldMapper dataFieldMapper;

    @Autowired
    private ReconMatchRuleMapper matchRuleMapper;

    @Autowired
    private ReconItemMapper itemMapper;

    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

    /**
     * 由定时器启动对账任务
     */
    public void runReconJob(String reconJobName) {
        Date transDate = new Date();
        transDate = DateUtils.addDays(transDate, -1);
        Date satrtDate = DateUtil.getDayStart(transDate);
        Date endDate = DateUtil.getDayEnd(transDate);

        Map<String, Object> reconParams = new HashMap<String, Object>();
        reconParams.put("TransDate", transDate);
        reconParams.put("StartDate", satrtDate);
        reconParams.put("EndDate", endDate);

        try {
            ReconJob reconJob = jobMapper.selectByJobName(reconJobName);
            runReconJob(reconJob, reconParams);
        } catch (Exception e) {
            logger.debug(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    /**
     * 启动对账任务
     */
    public void runReconJob(ReconJob reconJob, Map<String, Object> reconJobParams) throws Exception {

        ReconJobInstance jobInstance = createNewJobInstance(reconJob, reconJobParams);

        dataSetMapper.deleteAll();

        ReconDataSource remoteDS = extractRemoteDataSet(reconJob, reconJobParams);
        ReconDataSource localDS = extractLocalDataSet(reconJob, reconJobParams);

        List<ReconDataSet> remoteDataSets = dataSetMapper.selectByDateSourceId(remoteDS.getId());
        List<ReconDataSet> localDataSets = dataSetMapper.selectByDateSourceId(localDS.getId());
        List<ReconMatchRule> rules = matchRuleMapper.selectByJobId(reconJob.getId());
        ReconResult reconResult = matchDataSets(remoteDataSets, localDataSets, rules, jobInstance);

        jobInstance.setTransCount(reconResult.getTxnCount());
        jobInstance.setMatchedCount(reconResult.getMatchedCount());
        jobInstanceMapper.insert(jobInstance);

        //TODO 发送邮件通知相关人员 sendReconResultToMail(reconJobParams);

    }

    /**
     * 获得指定日期当天的对账文件
     */
    public File downloadReconcileFile(Date transDate) {
        return null;
    }

    /**
     * 获得指定时间段的对账文件
     */
    public File downloadReconcileFile(Date startDate, Date endDate) {
        return null;
    }

    /**
     * 创建对账作业实例
     */
    private ReconJobInstance createNewJobInstance(ReconJob reconJob, Map<String, Object> reconJobParams) {

        ReconJobInstance jobInstance = new ReconJobInstance();

        jobInstance.setChannelCode(reconJob.getChannelCode());
        jobInstance.setTransDate((Date) reconJobParams.get("TransDate"));
        jobInstance.setReconcileTime(new Date());
        jobInstance.setReconcileStatus("Proceed");

        jobInstanceMapper.insert(jobInstance);

        return jobInstance;
    }

    /**
     * 获得远程对账数据集
     */
    private ReconDataSource extractRemoteDataSet(ReconJob reconJob, Map<String, Object> reconJobParams) throws IOException {
        ReconDataSource remoteDS = reconDataSourceMapper.selectByPrimaryKey(reconJob.getRemoteDataSource());
        String dsDefinition = remoteDS.getDsDefinition();

        Date satrtDate = (Date) reconJobParams.get("StartDate");
        Date endDate = (Date) reconJobParams.get("EndDate");

        InputStream inputStream = null;

        if (reconJobParams.containsKey("reconFile")) {
            MultipartFile multipartFile = (MultipartFile) reconJobParams.get("reconFile");
            inputStream = multipartFile.getInputStream();
        } else {
            ReconcileFileManager reconcileFileManager = (ReconcileFileManager) SpringContextUtil.getBean(dsDefinition);//根据支付方式获取不同的对账数据下载类
            File file = reconcileFileManager.downloadReconcileFile(satrtDate, endDate);
            inputStream = new FileInputStream(file);
        }

        List<ReconDataField> dataFields = dataFieldMapper.selectByDataSourceId(remoteDS.getId());

        String type = remoteDS.getType();
        String splitter = remoteDS.getSeperatorChar();
        int ingoreFirst = remoteDS.getIngoreFirst();
        int ingoreEnd = remoteDS.getIngoreEnd();

        if ("CSVFile".equalsIgnoreCase(type)) {
            CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
            List<String[]> lines = reader.readAll();
            int size = lines.size();

            if (size != 0) {
                List<String[]> effLines = lines.subList(ingoreFirst, size - ingoreEnd);
                int lineNo = 1;
                for (String[] lineParts : effLines) {
                    ReconDataSet reconDataSet = new ReconDataSet();

                    reconDataSet.setDataSourceId(remoteDS.getId());
                    reconDataSet.setLineNo(lineNo);
                    reconDataSet.setLineContent(Arrays.toString(lineParts));
                    reconDataSet.setJsonStr(toJsonStr(dataFields, lineParts));

                    dataSetMapper.insert(reconDataSet);
                    lineNo++;
                }
            }
            reader.close();
        } else if ("TXTFile".equalsIgnoreCase(type)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            while (ingoreFirst-- != 0) {
                reader.readLine();
            }

            String line = null;
            int lineNo = 1;
            while ((line = reader.readLine()) != null) {
                ReconDataSet reconDataSet = new ReconDataSet();
                String[] lineParts = line.split(splitter);

                reconDataSet.setLineNo(lineNo);
                String jsonStr = toJsonStr(dataFields, lineParts);
                reconDataSet.setLineContent(jsonStr);
                reconDataSet.setJsonStr(jsonStr);

                dataSetMapper.insert(reconDataSet);
                lineNo++;
            }
            reader.close();
        }

        inputStream.close();
        return remoteDS;
    }

    /**
     * 获得本地对账数据集
     */
    private ReconDataSource extractLocalDataSet(ReconJob reconJob, Map<String, Object> reconJobParams) {
        ReconDataSource localDS = reconDataSourceMapper.selectByPrimaryKey(reconJob.getPaymentDataSource());

        String dsDefinition = localDS.getDsDefinition();
        Date satrtDate = (Date) reconJobParams.get("StartDate");
        Date endDate = (Date) reconJobParams.get("EndDate");

        List<ReconDataField> dataFields = dataFieldMapper.selectByDataSourceId(localDS.getId());

        try {

            jdbcTemplate.query(dsDefinition, new RowCallbackHandler() {

                public void processRow(ResultSet rs) throws SQLException {
                    ReconDataSet reconDataSet = new ReconDataSet();

                    reconDataSet.setDataSourceId(localDS.getId());
                    reconDataSet.setLineNo(rs.getRow());
                    reconDataSet.setJsonStr(toJsonStr(dataFields, rs));
                    reconDataSet.setLineContent(reconDataSet.getJsonStr());

                    dataSetMapper.insert(reconDataSet);
                }

            }, satrtDate, endDate);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return localDS;
    }

    /**
     * 执行两个数据集的比对
     *
     * @param remoteDS    远程数据集
     * @param localDS     本地数据集
     * @param rules       比对的规则
     * @param jobInstance 对账对象实例
     * @return reconResult 对账结果
     * @throws Exception 对账过程中出现的问题
     */
    private ReconResult matchDataSets(List<ReconDataSet> remoteDS, List<ReconDataSet> localDS, List<ReconMatchRule> rules, ReconJobInstance jobInstance) throws Exception {
        ReconResult reconResult = new ReconResult();

        //远程对账数据集
        Map<JsonNode, ReconDataSet> unMatchRemoteDS = new HashMap<>();
        for (ReconDataSet record : remoteDS) {
            JsonNode node = toJsonNode(record);
            if (node != null)
                unMatchRemoteDS.put(node, record);
        }

        //本地对账数据集
        Map<JsonNode, ReconDataSet> unMatchLocalDS = new HashMap<>();
        for (ReconDataSet record : localDS) {
            JsonNode node = toJsonNode(record);
            if (node != null)
                unMatchLocalDS.put(node, record);
        }

        ReconMatchRule pairRule = getPairRule(rules);
        String matchCriteria = pairRule.getMatchCriteria();
        String[] pairRuleParts = matchCriteria.split("=");

        List<ReconMatchRule> matchRules = getMatchRules(rules);

        Iterator<Map.Entry<JsonNode, ReconDataSet>> it = unMatchRemoteDS.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<JsonNode, ReconDataSet> entry = it.next();
            JsonNode remoteJN = entry.getKey();
            JsonNode localJN = getLocalRecord(reconResult, unMatchLocalDS, remoteJN, pairRuleParts);

            if (localJN == null)
                continue;

            PaymentOrder paymentOrder = orderMapper.selectByOrderNumber(localJN.get("orderNumber").getTextValue());
            if (PaymentOrderPayStatus.PAID.equals(paymentOrder.getStatus())) {
                String result = match(remoteJN, localJN, matchRules);
                if (result == null) {
                    createReconItem(jobInstance, remoteJN, localJN, "MATCH");
                    paymentOrder.setCheckStatus(PaymentOrderCheckStatus.SUCCESS);
                } else {
                    createReconItem(jobInstance, remoteJN, localJN, "UNMATCH");
                    paymentOrder.setCheckStatus(PaymentOrderCheckStatus.FAIL);
                }
                paymentOrder.setCheckTime(new Date());
            } else {
                createReconItem(jobInstance, remoteJN, localJN, "UNMATCH");
                paymentOrder.setCheckStatus(PaymentOrderCheckStatus.FAIL);
                //TODO why
                //paymentOrder.set("Description", "远端成功，本地失败。");
            }

            orderMapper.updateByPrimaryKeySelective(paymentOrder);

            remoteDS.remove(unMatchRemoteDS.get(remoteJN));
            localDS.remove(unMatchLocalDS.get(localJN));
            it.remove();
            unMatchLocalDS.remove(localJN);
        }

        for (Map.Entry<JsonNode, ReconDataSet> record : unMatchRemoteDS.entrySet()) {
            createReconItem(jobInstance, record.getKey(), null, "UNMATCH");
            reconResult.setTxnCount(reconResult.getTxnCount() + 1);
        }

        for (Map.Entry<JsonNode, ReconDataSet> record : unMatchLocalDS.entrySet()) {
            JsonNode localJN = record.getKey();
            createReconItem(jobInstance, null, localJN, "UNMATCH");
            reconResult.setTxnCount(reconResult.getTxnCount() + 1);

            PaymentOrder paymentOrder = orderMapper.selectByOrderNumber(localJN.get("orderNumber").getTextValue());
            if (PaymentOrderPayStatus.PAID.equals(paymentOrder.getStatus())) {
                paymentOrder.setCheckStatus(PaymentOrderCheckStatus.FAIL);
                //paymentOrder.set("Description", "无远端记录");
            } else {
                paymentOrder.setCheckStatus(PaymentOrderCheckStatus.SUCCESS);
                //paymentOrder.set("Description", "对账成功");
            }
            orderMapper.updateByPrimaryKeySelective(paymentOrder);
        }

        return reconResult;
    }

    /**
     * 创建对账明细（结果）
     */
    private void createReconItem(ReconJobInstance reconJobInstance, JsonNode remoteJS, JsonNode localJS, String reconResult) {
        ReconItem reconItem = new ReconItem();

        if (localJS != null) {
            reconItem.setOrderNumber(getTextValue(localJS, "OrderNumber"));
            /*reconItem.set("PaymentRequestId", getTextValue(localJS, "Oid"));
            reconItem.set("TransTime", getTextValue(localJS, "StartTime"));
            reconItem.set("PaySuccessTime", getTextValue(localJS, "EndTime"));
            reconItem.set("OutTradeNo", getTextValue(localJS, "OutTradeNo"));
            reconItem.set("PaymentAmount", getTextValue(localJS, "TotalFee"));*/
        } else if (remoteJS != null) {
            reconItem.setOrderNumber(getTextValue(localJS, "OrderNumber"));
            /*reconItem.set("PaymentRequestId", getTextValue(remoteJS, "Oid"));
            reconItem.set("TransTime", getTextValue(remoteJS, "StartTime"));
            reconItem.set("PaySuccessTime", getTextValue(remoteJS, "EndTime"));
            reconItem.set("OutTradeNo", getTextValue(remoteJS, "OutTradeNo"));
            reconItem.set("PaymentAmount", getTextValue(remoteJS, "TotalFee"));*/
        }

        String message = "";
        if (remoteJS == null)
            message = "无远端记录";
        if (localJS == null)
            message = "无本地记录";

        reconItem.setReconResult(reconResult);
        reconItem.setDescription(message);

        itemMapper.insert(reconItem);
    }

    private String getTextValue(JsonNode jsonNode, String key) {
        JsonNode node = jsonNode.get(key);
        return node != null ? node.getTextValue() : null;
    }

    /**
     * 根据所有匹配规则类型为MATCH的规则进行匹配，已确定两笔记录是否相同
     */
    private String match(JsonNode remoteJN, JsonNode localJN, List<ReconMatchRule> matchRules) {
        for (ReconMatchRule matchRule : matchRules) {
            if (!match(remoteJN, localJN, matchRule))
                return matchRule.getName();
        }
        return null;
    }

    /**
     * 对一条规则执行校验
     */
    private boolean match(JsonNode remoteJN, JsonNode localJN, ReconMatchRule matchRule) {
        String matchCriteria = matchRule.getMatchCriteria();
        //TODO 目前表达式规定只能解析一层括号
        if(matchCriteria.contains("(")) {
            int start = StringUtils.indexOf(matchCriteria, "(") + 1;
            int end = StringUtils.indexOf(matchCriteria, ")");

        } else {

        }
        String[] crits = matchCriteria.split("=");

        String remoteValue = remoteJN.get(crits[0]).getTextValue();
        String localValue = localJN.get(crits[1]).getTextValue();

        return remoteValue.equals(localValue);
    }

    /**
     * 获得本地对应的对账数据
     */
    private JsonNode getLocalRecord(ReconResult reconResult, Map<JsonNode, ReconDataSet> unMatchLocalDS, JsonNode remoteJN, String[] keys) {
        String remoteKey = keys[0];
        String localKey = keys[1];

        for (int i = 0; i < keys.length; i++) {
            if (keys[i].contains("#1")) {
                remoteKey = keys[i].split("\\.")[1];
                continue;
            }
            if(keys[i].contains("#2")) {
                localKey = keys[i].split("\\.")[1];
                continue;
            }
        }

        String remoteValue = remoteJN.get(remoteKey).getTextValue();

        for (JsonNode localJS : unMatchLocalDS.keySet()) {
            String localValue = localJS.get(localKey).getTextValue();

            if (remoteValue.equalsIgnoreCase(localValue)) {
                reconResult.setTxnCount(reconResult.getTxnCount() + 1);
                reconResult.setMatchedCount(reconResult.getMatchedCount() + 1);
                return localJS;
            }
        }
        return null;
    }

    /**
     * 获取匹配两条数据是否是同一条的业务数据的规则
     */
    private ReconMatchRule getPairRule(List<ReconMatchRule> matchRules) {
        for (ReconMatchRule rule : matchRules) {
            if ("PAIR".equals(rule.getType())) {
                return rule;
            }
        }
        return null;
    }

    /**
     * 获取用于匹配两条是否匹配的规则集
     */
    private List<ReconMatchRule> getMatchRules(List<ReconMatchRule> matchRules) {
        List<ReconMatchRule> result = Lists.newArrayList();
        for (ReconMatchRule com : matchRules) {
            if ("MATCH".equals(com.getType()))
                result.add(com);
        }
        return result;
    }

    private JsonNode toJsonNode(ReconDataSet record) {
        JsonNode node = null;
        String json = record.getJsonStr();

        if (StringUtils.isBlank(json))
            return node;

        try {
            node = JsonUtils.getJsonNode(json);
        } catch (Exception e) {
            logger.error("Failed to parse json string" + json, e);
            throw new RuntimeException(e);
        }

        return node;
    }

    private String toJsonStr(List<ReconDataField> fields, String[] values) {
        ObjectMapper mapper = JsonUtils.MAPPER;
        ObjectNode rootNode = mapper.createObjectNode();
        StringWriter writer = new StringWriter();

        for (ReconDataField field : fields) {
            int index = field.getFieldNo();
            String name = field.getName();
            String type = field.getType();
            if (values.length >= index) {
                String value = values[index - 1].trim();
                if ("Currency(Fen)".equals(type)) {
                    value = new BigDecimal(value).movePointLeft(2).toString();
                }
                rootNode.put(name, value);
            }
        }
        try {
            mapper.writeValue(writer, rootNode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return writer.toString();
    }

    private String toJsonStr(List<ReconDataField> fields, ResultSet rs) {
        ObjectMapper mapper = JsonUtils.MAPPER;
        ObjectNode rootNode = mapper.createObjectNode();
        StringWriter writer = new StringWriter();

        try {
            for (ReconDataField field : fields) {
                String name = field.getName();
                if (rs.findColumn(name) > 0) {
                    Object value = rs.getObject(name);
                    if (value != null)
                        rootNode.put(name, value.toString());
                    else
                        rootNode.put(name, "");
                }
            }
            mapper.writeValue(writer, rootNode);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return writer.toString();
    }
}
