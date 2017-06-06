package com.siebre.payment.paymenthandler.alipay.sdk;

import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.util.*;


/**
 * Title: Class AliAppHandlerService
 * Description:
 * 解析支付宝返回数据
 * @author chunling.yan.ma
 * @version 1.0.0
 * @email chunling.yan.ma@siebresystems.com
 * @date: 2016/9/4 11:14
 */
public class AlipayParse {

    private static Logger logger= LoggerFactory.getLogger(AlipayParse.class);

    /**
     * 解析付款查询接口成功返回数据
     * @author chunling.yan.ma
     * @version 1.0.0
     * @Date 2016/9/4 11:17
     * @param protocolXML
     *  参数 protocolXML，required
     * @return
     */
    public static Map<String,String> payQueryParse(String protocolXML) {
        SAXReader reader = new SAXReader();
        Map<String, String> stringMap = new HashMap<String, String>();
        List<Map<String,String>> list = null ;
        try {
            list = parseElement(protocolXML,"response");
            if(null != list && 0<list.size()) {
                stringMap = list.get(0);
                org.dom4j.Document doc = reader.read(new ByteArrayInputStream(protocolXML.getBytes("UTF-8")));
                org.dom4j.Element root = doc.getRootElement();
                stringMap.put("is_success", root.elementText("is_success"));
                stringMap.put("sign", root.elementText("sign"));
                stringMap.put("sign_type", root.elementText("sign_type"));
            }
        } catch (Exception e) {
            logger.error("解析付款查询接口返回数据失败！原因：",e);
        }
        return stringMap;
    }

    /**
     * 解析xml单级节点
     * @author chunling.yan.ma
     * @version 1.0.0
     * @Date 2016/9/4 11:17
     * @param xml
     *  参数 xml，required
     * @return
     */
    public static Map<String,String> singleNodeParse(String xml){
        if (xml == null || xml.trim().length() == 0){
            return null;
        }
        Map<String,String> map = new HashMap<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new ByteArrayInputStream(xml.getBytes()));
            Element root =  document.getDocumentElement();
            NodeList nodeList = root.getChildNodes();
            int length = nodeList.getLength();
            for (int i=0;i<length;i++){
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE){
                    NodeList valueNode = node.getChildNodes();
                    String value  = "";
                    if (valueNode.getLength() > 0){
                        value = valueNode.item(0).getNodeValue();
                    }
                    map.put(node.getNodeName(),value);
                }
            }
        }catch (Exception e){
            logger.error("解析xml失败!"+xml,e);
        }
        return map;
    }

    /**
     * 解析退款查询接口返回数据
     * @author chunling.yan.ma
     * @version 1.0.0
     * @Date 2016/9/4 11:17
     * @param strResult
     *  参数 strResult，required
     * @return
     */
    public static  Map<String, String> refundQueryParse(String strResult) {
        Map<String,String> map = new HashMap<>();
        //判断查询操作
        try {
            String[] split = strResult.split("&");
            for(int i=0;i<split.length;i++){
                String[] split1 = split[i].split("=");
                map.put(split1[0],split1[1]);
            }
        } catch (Exception e) {
            logger.error("解析退款查询接口返回数据失败！原因：",e);
        }
//        if(!strResult.contains("error_code")){
//            String substring = strResult.substring(0, strResult.indexOf("SUCCESS")+7);
//            String[] split = substring.split("&");
//            map.put("is_success",split[0]);
//            map.put("result_details",split[1]);
//        }else{
//            String[] split = strResult.split("&");
//            map.put("is_success",split[0]);
//            map.put("error_code",split[1]);
//        }
        return map;
    }

    /**
     * 解析多级节点xml
     * @author chunling.yan.ma
     * @version 1.0.0
     * @Date 2016/9/4 11:17
     * @param protocolXML
     *  参数 protocolXML，required
     *  @param str
     *  参数 str，required
     * @return
     */
    public static List<Map<String,String>> parseElement(String protocolXML, String str) throws Exception {
        List<Map<String,String>> list = new ArrayList();
        try {
            Map<String, String> map = null;
            SAXReader reader = new SAXReader();
            org.dom4j.Document doc = reader.read(new ByteArrayInputStream(protocolXML.getBytes("UTF-8")));
            org.dom4j.Element root = doc.getRootElement();
            Iterator it  = getIt(root,str);
            while (it !=null && it.hasNext()){
                map = new HashMap();
                org.dom4j.Element e = (org.dom4j.Element) it.next();
                Iterator its  =  e.elementIterator();
                while (its.hasNext()){
                    org.dom4j.Element ee = (org.dom4j.Element) its.next();
                    e.elementIterator();
                    map.put(ee.getName(), ee.getText());
                }
                list.add(map);
            }
        } catch (Exception e) {
            logger.error("解析多级节点xml失败！原因：", e);
        }
        return list;
    }

    /**
     * 获取迭代器
     * @author chunling.yan.ma
     * @version 1.0.0
     * @Date 2016/9/4 11:17
     * @param ele
     *  参数 ele，required
     *  @param s
     *  参数 s，required
     * @return
     */
    public static Iterator getIt(org.dom4j.Element ele, String s) throws Exception {
        Iterator it  = ele.elementIterator();
        Iterator its = null;
        try {
            while(it.hasNext()){
                org.dom4j.Element element = (org.dom4j.Element) it.next();
                if (element.getName().equals(s)){
                    its = element.elementIterator();
                    break;
                }else{
                    if ((its =getIt(element,s))!=null){
                        break;
                    };
                };
            }
        } catch (Exception e) {
            logger.error("获取迭代器失败！原因：", e);
        }
        return its;
    }

//    /**
//     * 解析退款查询返回数据
//     * @author chunling.yan.ma
//     * @version 1.0.0
//     * @Date 2016/9/4 11:17
//     * @param map2
//     *  参数 map2，required
//     * @return
//     */
//    public static List<RefundItemDTO> parseDetails(Map<String, String> map2) {
//        String resultDetails = map2.get("result_details");
//        List<RefundItemDTO> paramList = new ArrayList<RefundItemDTO>();
//        String[] split = resultDetails.split("#");
//        for (int i = 0; i < split.length; i++) {
//            if (split.length > 0) {
//                for (int j = 0; j < split.length; j++) {
//                    RefundItemDTO re = new RefundItemDTO();
//                    String[] rbr = split[i].split("\\$");
//                    if (rbr.length > 0) {
//                        String param = rbr[0];
//                        if (StringUtils.isNotBlank(param)) {
//                            String[] resultParam = param.split("\\^");
//                            if (resultParam.length > 0 && resultParam.length == 4) {
//                                re.setPaySeqNo(resultParam[1]);
//                                re.setRefundAmt(resultParam[2]);
//                                if (StringUtils.equalsIgnoreCase(resultParam[3], "SUCCESS")) {
//                                    re.setStatus(TradeStatus.REFUND_SUCCESS);
//                                } else if (StringUtils.equalsIgnoreCase(resultParam[3], "FAILED")) {
//                                    re.setStatus(TradeStatus.REFUND_PROCESS);
//                                } else {
//                                    re.setStatus(TradeStatus.REFUND_FAIL);
//                                }
//                                paramList.add(re);
//                            } else {
//                                logger.error("退款查询接口返回数据格式错误! result:{}", param);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        return paramList;
//    }
//
//    /**
//     * 解析退款回调返回数据
//     * @author chunling.yan.ma
//     * @version 1.0.0
//     * @Date 2016/9/4 11:17
//     * @param map2
//     *  参数 map2，required
//     * @return
//     */
//    public static void resolveResultDetails(Map<String, String> notifyMap,RefundItemResultDTO resultDto) {
//        List<RefundItemDTO> paramList = new ArrayList<RefundItemDTO>();
//        String resultDetails = notifyMap.get("result_details");
//        String[] split = resultDetails.split("#");
//        if (split.length > 1) {
//            for (String batchResult : split) {
//                RefundItemDTO re = new RefundItemDTO();
//                String[] batchDetail = batchResult.split("\\^");
//                if (batchDetail.length == 4) {
//                    resultDto.setBatchNo(batchDetail[0]);
//                    re.setPaySeqNo(batchDetail[1]);
//                    re.setRefundAmt(batchDetail[2]);
//                    if(StringUtils.equalsIgnoreCase(batchDetail[3],"SUCCESS")){
//                        re.setStatus(TradeStatus.REFUND_SUCCESS);
//                    }else {
//                        re.setStatus(TradeStatus.REFUND_FAIL);
//                        re.setResponseCode(batchDetail[3]);
//                        re.setResponseMessage(batchDetail[3]);
//                    }
//                    paramList.add(re);
//                }
//                else if(batchDetail.length ==3){
//                    resultDto.setBatchNo(notifyMap.get("batch_no"));
//                    re.setPaySeqNo(batchDetail[0]);
//                    re.setRefundAmt(batchDetail[1]);
//                    if(StringUtils.equalsIgnoreCase(batchDetail[2],"SUCCESS")){
//                        re.setStatus(TradeStatus.REFUND_SUCCESS);
//                    }else {
//                        re.setStatus(TradeStatus.REFUND_FAIL);
//                        re.setResponseCode(batchDetail[2]);
//                        re.setResponseMessage(batchDetail[2]);
//                    }
//                    paramList.add(re);
//                }
//                else{
//                    logger.warn("退款回调数据有误！batchResult:{}", batchResult);
//                }
//            }
//        } else {
//            for (String batchResult : split) {
//                RefundItemDTO re = new RefundItemDTO();
//                String[] batchDetail = batchResult.split("\\^");
//                resultDto.setBatchNo(notifyMap.get("batch_no"));
//                if(batchDetail.length==3){
//                    re.setPaySeqNo(batchDetail[0]);
//                    re.setRefundAmt(batchDetail[1]);
//                    if(StringUtils.equalsIgnoreCase(batchDetail[2],"SUCCESS")){
//                        re.setStatus(TradeStatus.REFUND_SUCCESS);
//                    }else {
//                        re.setStatus(TradeStatus.REFUND_FAIL);
//                        re.setResponseCode(batchDetail[2]);
//                        re.setResponseMessage(batchDetail[2]);
//                    }
//                    paramList.add(re);
//                }
//                else{
//                    logger.warn("退款回调数据有误！batchResult:{}", batchResult);
//                }
//            }
//        }
//        resultDto.setItemList(paramList);
//    }
//
//    /**
//     * 解析web
//     * @author chunling.yan.ma
//     * @version 1.0.0
//     * @Date 2016/9/4 11:17
//     * @param result
//     *  参数 result，required
//     * @return
//     */
//    public static Map<String, String> webPayParse(String result) {
//        return null;
//    }
//
//    /**
//     * 解析账户余额详细数据
//     * @author chunling.yan.ma
//     * @version 1.0.0
//     * @Date 2016/9/4 11:17
//     * @param result
//     *  参数 result，required
//     * @param resultDto
//     * @return
//     */
//    public static BalanceQueryResultDTO parseData(Map<String, String> map2, BalanceQueryResultDTO resultDto) {
//        String param = map2.get("res_data");
//        if (StringUtils.isNotBlank(param)) {
//            String[] resultParam = param.split("\\^");
//            System.out.println("长度==========================="+resultParam.length);
//            if (resultParam.length == 3) {
//                resultDto.setAlipayCount(resultParam[0]);
//                resultDto.setBalance(resultParam[1]);
//                resultDto.setFreezAmt(resultParam[2]);
//                resultDto.setStatus(TradeStatus.QUERY_SUCCESS);
//            } else {
//                logger.error("退款查询接口返回数据格式错误! result:{}", param);
//                resultDto.setAlipayCount(resultParam[0]);
//                resultDto.setStatus(TradeStatus.QUERY_FALI);
//            }
//        }
//        return resultDto;
//    }
}
