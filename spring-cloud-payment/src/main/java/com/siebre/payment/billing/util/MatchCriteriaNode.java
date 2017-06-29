package com.siebre.payment.billing.util;

import com.siebre.payment.billing.entity.ReconDataField;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonNode;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by tianci.huang on 2017/6/29.
 */
public class MatchCriteriaNode {

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
            return MatchCriteriaEngine.matchWithOutBracket(expressionStr, remoteJN, localJN, remoteDataFields, localDataFields);
        } else {
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
