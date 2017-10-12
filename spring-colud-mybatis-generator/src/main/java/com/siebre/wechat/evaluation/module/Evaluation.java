package com.siebre.wechat.evaluation.module;

import com.siebre.wechat.enums.EvaluationSource;
import com.siebre.wechat.enums.PaymentStatus;
import java.util.Date;

public class Evaluation {
    private Long id;

    private EvaluationSource evaluationSource;

    private Integer score;

    private String openid;

    private String mobile;

    private String username;

    private Date createDate;

    private PaymentStatus paymentStatus;

    private String orderNumber;

    private Date paySuccessDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EvaluationSource getEvaluationSource() {
        return evaluationSource;
    }

    public void setEvaluationSource(EvaluationSource evaluationSource) {
        this.evaluationSource = evaluationSource;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber == null ? null : orderNumber.trim();
    }

    public Date getPaySuccessDate() {
        return paySuccessDate;
    }

    public void setPaySuccessDate(Date paySuccessDate) {
        this.paySuccessDate = paySuccessDate;
    }
}