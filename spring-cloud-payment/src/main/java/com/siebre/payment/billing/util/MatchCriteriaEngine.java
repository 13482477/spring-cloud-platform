package com.siebre.payment.billing.util;

import com.siebre.payment.billing.entity.ReconDataField;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Huang Tianci 2017/6/29.
 * 对账表达式解析引擎 v1.0
 * 目前只支持表达式中包含一对括号的情况
 */
public class MatchCriteriaEngine {

    private static Logger logger = LoggerFactory.getLogger(MatchCriteriaEngine.class);

    public static String left_bracket = "(";

    public static String right_bracket = ")";

    public static String eq = "=";

    public static String and = "and";

    public static String or = "or";

    /**
     * 解析表达式
     */
    private static MatchCriteriaNode analysisExpression(String expressionStr) {
        logger.info("解析表达式：{}", expressionStr);
        MatchCriteriaNode root = new MatchCriteriaNode();
        root.setExpressionStr(expressionStr);
        root.setParent(null);
        analysis(root);
        return root;
    }

    /** 解析表达式规则 */
    private static void analysis(MatchCriteriaNode root) {
        String expressionStr = root.getExpressionStr();
        if(expressionStr.contains(MatchCriteriaEngine.left_bracket)) { //如果包含括号
            int start = StringUtils.indexOf(expressionStr, MatchCriteriaEngine.left_bracket);
            if(start == 0) {  //前半部分
                int end = StringUtils.indexOf(expressionStr, MatchCriteriaEngine.right_bracket);
                String substring = StringUtils.substring(expressionStr, start + 1, end);
                setLeftNode(substring, root);

                //获取剩下部分的表达式字符串
                String nextStr = StringUtils.substring(expressionStr, end + 1, expressionStr.length());
                nextStr = StringUtils.trim(nextStr);
                //设置左右节点关系
                if(nextStr.startsWith(MatchCriteriaEngine.and)) {
                    root.setLeftRightRelation(MatchCriteriaEngine.and);
                    nextStr = StringUtils.substring(nextStr, 3, nextStr.length());
                } else if (nextStr.startsWith(MatchCriteriaEngine.or)) {
                    root.setLeftRightRelation(MatchCriteriaEngine.or);
                    nextStr = StringUtils.substring(nextStr, 2, nextStr.length());
                }

                //设置右节点
                setRightNode(nextStr, root);
            } else { //后半部分
                String[] temp = expressionStr.split("\\s");
                //左节点
                setLeftNode(temp[0], root);
                root.setLeftRightRelation(temp[1]);
                int subLength = temp[0].length() + temp[1].length() + 1;
                String nextStr = StringUtils.substring(expressionStr, subLength, expressionStr.length());
                nextStr = StringUtils.trim(nextStr);
                nextStr = StringUtils.substring(nextStr, 1, nextStr.length() - 1);
                setRightNode(nextStr, root);
            }
        } else if(expressionStr.contains(and) || expressionStr.contains(or)) { //不包含括号,包含and 或者or
            String[] temp = expressionStr.split("\\s");
            setLeftNode(temp[0], root);
            root.setLeftRightRelation(temp[1]);
            int subLength = temp[0].length() + temp[1].length() + 1;
            String nextStr = StringUtils.substring(expressionStr, subLength, expressionStr.length());
            setRightNode(nextStr, root);
        } else { //仅有一个等号
            root.setLeaf(true);
        }
    }

    /** 设置右节点 */
    private static void setRightNode(String nextStr, MatchCriteriaNode parent) {
        nextStr = StringUtils.trim(nextStr);
        logger.info("左表达式：{}", nextStr);
        MatchCriteriaNode rightNode = new MatchCriteriaNode();
        rightNode.setExpressionStr(nextStr);
        rightNode.setParent(parent);
        parent.setRightChild(rightNode);
        parent.setLeaf(false);
        analysis(rightNode);
    }

    /** 设置左节点 */
    private static void setLeftNode(String substring, MatchCriteriaNode parent) {
        substring = StringUtils.trim(substring);
        logger.info("右表达式：{}", substring);
        MatchCriteriaNode leftNode = new MatchCriteriaNode();
        leftNode.setExpressionStr(substring);
        leftNode.setParent(parent);
        parent.setLeftChild(leftNode);
        parent.setLeaf(false);
        analysis(leftNode);
    }

    /**
     * 匹配计算结果,不在乎是否包含括号
     */
    public static boolean match(String expressionStr,
                                JsonNode remoteJN, JsonNode localJN,
                                List<ReconDataField> remoteDataFields, List<ReconDataField> localDataFields) throws Exception {
        if(StringUtils.isBlank(expressionStr)) {
            throw new Exception("表达式为空");
        }
        MatchCriteriaNode root = analysisExpression(expressionStr);
        logger.info("开始计算");
        return root.calculate(remoteJN, localJN, remoteDataFields, localDataFields);
    }

    /**
     * 仅在表达式没有括号的情况下，匹配计算结果
     */
    public static boolean matchWithOutBracket(String expressionStr,
                                              JsonNode remoteJN, JsonNode localJN,
                                              List<ReconDataField> remoteDataFields, List<ReconDataField> localDataFields) throws Exception {
        if(StringUtils.isBlank(expressionStr)) {
            throw new Exception("表达式为空");
        }
        String[] keys = expressionStr.split(eq);
        String leftKey = keys[0];
        String rightKey = keys[1];
        String remoteKey = "";
        String localKey = "";
        //等号的右边是否是一个固定的值
        boolean eqAvalue = false;
        String value = null;

        if (leftKey.contains("#1")) {
            remoteKey = leftKey.split("\\.")[1];
        } else if (leftKey.contains("#2")) {
            localKey = leftKey.split("\\.")[1];
        }
        if (rightKey.contains("#1")) {
            remoteKey = rightKey.split("\\.")[1];
        } else if (rightKey.contains("#2")) {
            localKey = rightKey.split("\\.")[1];
        } else {
            value = rightKey;
            eqAvalue = true;
        }

        if (eqAvalue) { //等号右边是固定的值
            if (StringUtils.isNotBlank(remoteKey)) {
                return matchStaticValue(remoteJN, remoteDataFields, remoteKey, value);
            } else {
                return matchStaticValue(localJN, localDataFields, localKey, value);
            }
        } else {
            ReconDataField remoteDataField = getDateField(remoteDataFields, remoteKey);
            ReconDataField localDataField = getDateField(localDataFields, localKey);
            if (!remoteDataField.getType().contains("Currency")) {
                if (!remoteDataField.getType().equals(localDataField.getType())) {
                    throw new Exception("远程字段定义的类型与本地字段定义的类型不一致！");
                }
            }

            if ("String".equals(remoteDataField.getType())) {
                String remoteValue = remoteJN.get(remoteKey).getTextValue();
                String localValue = localJN.get(localKey).getTextValue();
                return remoteValue.equalsIgnoreCase(localValue);
            } else if ("Integer".equals(remoteDataField.getType())) {
                int remoteValue = remoteJN.get(remoteKey).getIntValue();
                int localValue = localJN.get(localKey).getIntValue();
                return remoteValue == localValue;
            } else if (remoteDataField.getType().contains("Currency") || "BigDecimal".equals(remoteDataField.getType())) {
                BigDecimal remoteValue = remoteJN.get(remoteKey).getDecimalValue();
                BigDecimal localValue = localJN.get(localKey).getDecimalValue();
                int result = remoteValue.compareTo(localValue);
                return result == 0;
            }
            throw new Exception(remoteKey + "没有匹配的类型");
        }
    }

    private static Boolean matchStaticValue(JsonNode jsonNode, List<ReconDataField> dataFilds, String key, String value) throws Exception {
        ReconDataField dataFild = getDateField(dataFilds, key);
        if("String".equals(dataFild.getType())) {
            String value1 = jsonNode.get(key).getTextValue();
            return value1.equalsIgnoreCase(value);
        } else if("Integer".equals(dataFild.getType())) {
            Integer value1 = Integer.valueOf(jsonNode.get(key).getTextValue());
            Integer r = Integer.valueOf(value);
            return value1.equals(r);
        }
        throw new Exception(key + "没有匹配的类型");
    }

    private static ReconDataField getDateField(List<ReconDataField> dataFields, String key) throws Exception {
        for (ReconDataField field: dataFields) {
            if(field.getName().equalsIgnoreCase(key)) {
                return field;
            }
        }
        throw new Exception(key + "没有匹配的ReconDataField");
    }
}
