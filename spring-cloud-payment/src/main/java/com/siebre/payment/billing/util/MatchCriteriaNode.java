package com.siebre.payment.billing.util;

import com.siebre.payment.billing.entity.ReconDataField;
import org.codehaus.jackson.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Huang Tianci
 */
public class MatchCriteriaNode {

    private Logger logger = LoggerFactory.getLogger(MatchCriteriaNode.class);

    //父节点
    private MatchCriteriaNode parent;

    //左节点
    private MatchCriteriaNode leftChild;

    //右节点
    private MatchCriteriaNode rightChild;

    //左右节点关系
    private String leftRightRelation;

    //是否是叶子节点
    private boolean isLeaf;

    //表达式字符串
    private String expressionStr;

    /** 计算表达式 */
    public boolean calculate(JsonNode remoteJN, JsonNode localJN, List<ReconDataField> remoteDataFields, List<ReconDataField> localDataFields) throws Exception {
        if(isLeaf) {
            logger.info("当前节点是叶子节点，表达式：{}", expressionStr);
            return MatchCriteriaEngine.matchWithOutBracket(expressionStr, remoteJN, localJN, remoteDataFields, localDataFields);
        } else {
            logger.info("当前节点是父节点，子节点关系：{}，计算子节点, 左节点表达式：{}，右节点表达式：{}", leftRightRelation, leftChild.getExpressionStr(), rightChild.getExpressionStr());
            if(MatchCriteriaEngine.and.equals(leftRightRelation)) {
                return leftChild.calculate(remoteJN, localJN, remoteDataFields, localDataFields) &&
                        rightChild.calculate(remoteJN, localJN, remoteDataFields, localDataFields);
            } else {
                return leftChild.calculate(remoteJN, localJN, remoteDataFields, localDataFields) ||
                        rightChild.calculate(remoteJN, localJN, remoteDataFields, localDataFields);
            }
        }
    }

    public MatchCriteriaNode getParent() {
        return parent;
    }

    public void setParent(MatchCriteriaNode parent) {
        this.parent = parent;
    }

    public MatchCriteriaNode getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(MatchCriteriaNode leftChild) {
        this.leftChild = leftChild;
    }

    public MatchCriteriaNode getRightChild() {
        return rightChild;
    }

    public void setRightChild(MatchCriteriaNode rightChild) {
        this.rightChild = rightChild;
    }

    public String getLeftRightRelation() {
        return leftRightRelation;
    }

    public void setLeftRightRelation(String leftRightRelation) {
        this.leftRightRelation = leftRightRelation;
    }

    public String getExpressionStr() {
        return expressionStr;
    }

    public void setExpressionStr(String expressionStr) {
        this.expressionStr = expressionStr;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }
}
