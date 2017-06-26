package com.siebre.payment.billing.base;

import com.siebre.payment.billing.entity.ReconDataSource;
import com.siebre.payment.billing.entity.ReconJob;
import com.siebre.payment.billing.entity.ReconJobInstance;
import com.siebre.payment.billing.mapper.ReconDataSetMapper;
import com.siebre.payment.billing.mapper.ReconDataSourceMapper;
import com.siebre.payment.billing.mapper.ReconJobInstanceMapper;
import com.siebre.payment.billing.mapper.ReconJobMapper;
import com.siebre.payment.utils.DateUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
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
    private ReconJobMapper jobMapper;

    @Autowired
    private ReconJobInstanceMapper jobInstanceMapper;

    @Autowired
    private ReconDataSetMapper dataSetMapper;

    @Autowired
    private ReconDataSourceMapper reconDataSourceMapper;

    /** 由定时器启动对账任务 */
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

    /** 启动对账任务 */
    public void runReconJob(ReconJob reconJob, Map<String, Object> reconJobParams) throws Exception {

        ReconJobInstance jobInstance = createNewJobInstance(reconJob, reconJobParams);

        dataSetMapper.deleteAll();

        ReconDataSource remoteDS = extractRemoteDataSet(reconJob, reconJobParams);
        ReconDataSource localDS = extractLocalDataSet(reconJob, reconJobParams);

       /* ReconResult reconResult = matchDataSets(remoteDS.getChildComponentList("ReconDataSet"), localDS.getChildComponentList("ReconDataSet"), reconJob.getChildComponentList("ReconMatchRule"), reconJobInstance);

        reconJobInstance.set("TransCount", reconResult.getTxnCount());
        reconJobInstance.set("MatchedCount", reconResult.getMatchedCount());
        bmfAggrManager.save(reconJobInstance);

        processReconcileFile(reconJobParams);
        sendReconResultToMail(reconJobParams);
        sendReconResultToCore(reconJobParams);*/

    }

    /** 获得指定日期当天的对账文件 */
    public File downloadReconcileFile(Date transDate) {
        return null;
    }

    /** 获得指定时间段的对账文件 */
    public File downloadReconcileFile(Date startDate, Date endDate) {
        return null;
    }

    /** 创建对账作业实例 */
    private ReconJobInstance createNewJobInstance(ReconJob reconJob, Map<String, Object> reconJobParams) {

        ReconJobInstance jobInstance = new ReconJobInstance();

        jobInstance.setChannelCode(reconJob.getChannelCode());
        jobInstance.setTransDate((Date) reconJobParams.get("TransDate"));
        jobInstance.setReconcileTime(new Date());
        jobInstance.setReconcileStatus("Completed");

        jobInstanceMapper.insert(jobInstance);

        return jobInstance;
    }

    /** 获得远程对账数据集 */
    private ReconDataSource extractRemoteDataSet(ReconJob reconJob, Map<String, Object> reconJobParams) throws IOException {
        /*ReconDataSource remoteDS = reconDataSourceMapper.selectByPrimaryKey(reconJob.getRemoteDataSource());
        String dsDefinition = remoteDS.getDsDefinition();

        Date satrtDate = (Date) reconJobParams.get("StartDate");
        Date endDate = (Date) reconJobParams.get("EndDate");

        InputStream inputStream = null;

        if (reconJobParams.containsKey("reconFile")) {
            MultipartFile multipartFile = (MultipartFile) reconJobParams.get("reconFile");
            inputStream = multipartFile.getInputStream();
        } else {
            ReconcileFileManager reconcileFileManager = BafContext.getBean(dsDefinition, ReconcileFileManager.class);//根据支付方式获取不同的对账数据下载类
            File file = reconcileFileManager.downloadReconcileFile(satrtDate, endDate);
            inputStream = new FileInputStream(file);
        }

        BmfTable bmfTable = bmfAggrService.getSingletonBmfAggr("BillingManagement").getAggrModel().getBmfTable("ReconDataSet");
        List<BmfAggrComponent> childComponentList = remoteDS.getChildComponentList("ReconDataField");

        String type = remoteDS.getString("Type");
        String splitter = remoteDS.getString("SeperatorChar");
        int ingoreFirst = remoteDS.getInt("IngoreFirst");
        int ingoreEnd = remoteDS.getInt("IngoreEnd");

        if ("CSVFile".equalsIgnoreCase(type)) {
            CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
            List<String[]> lines = reader.readAll();
            int size = lines.size();

            if (size != 0) {
                List<String[]> effLines = lines.subList(ingoreFirst, size - ingoreEnd);
                int lineNo = 1;
                for (String[] lineParts : effLines) {
                    BmfAggrComponent reconDataSet = bmfAggrFactory.createChildComponent(remoteDS, bmfTable);

                    reconDataSet.set("LineNo", lineNo);
                    reconDataSet.set("LineContent", Arrays.toString(lineParts));
                    reconDataSet.set("JsonStr", toJsonStr(childComponentList, lineParts));

                    bmfAggrManager.save(reconDataSet);
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
                BmfAggrComponent reconDataSet = bmfAggrFactory.createChildComponent(remoteDS, bmfTable);
                String[] lineParts = line.split(splitter);

                reconDataSet.set("LineNo", lineNo);
                String jsonStr = toJsonStr(childComponentList, lineParts);
                reconDataSet.set("LineContent", jsonStr);
                reconDataSet.set("JsonStr", jsonStr);

                bmfAggrManager.save(reconDataSet);
                lineNo++;
            }
            reader.close();
        }

        inputStream.close();
        return remoteDS;*/
        return null;
    }

    /** 获得本地对账数据集 */
    private ReconDataSource extractLocalDataSet(ReconJob reconJob, Map<String, Object> reconJobParams) {
        /*final BmfAggrComponent localDS = reconJob.getBmfAggrComponent("ReconDataSource2");
        final BmfTable bmfTable = bmfAggrService.getSingletonBmfAggr("BillingManagement").getAggrModel().getBmfTable("ReconDataSet");

        String dsDefinition = localDS.getString("DSDefinition");
        Date endDate = (Date) reconJobParams.get("EndDate");

        try {
            dao.getJdbcTemplate().query(dsDefinition, new RowCallbackHandler() {

                public void processRow(ResultSet rs) throws SQLException {
                    BmfAggrComponent reconDataSet = bmfAggrFactory.createChildComponent(localDS, bmfTable);

                    reconDataSet.set("LineNo", rs.getRow());
                    reconDataSet.set("JsonStr", toJsonStr(localDS.getChildComponentList("ReconDataField"), rs));
                    reconDataSet.set("LineContent", reconDataSet.getString("JsonStr"));

                    bmfAggrManager.save(reconDataSet);
                }

            }, endDate);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return localDS;*/
        return null;
    }
}
