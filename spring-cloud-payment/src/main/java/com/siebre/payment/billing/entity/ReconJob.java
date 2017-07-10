package com.siebre.payment.billing.entity;

public class ReconJob {
    private Long id;

    private String name;

    private String type;

    private String channelCode;

    private Long remoteDataSource;

    private Long paymentDataSource;

    private String processName;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode == null ? null : channelCode.trim();
    }

    public Long getRemoteDataSource() {
        return remoteDataSource;
    }

    public void setRemoteDataSource(Long remoteDataSource) {
        this.remoteDataSource = remoteDataSource;
    }

    public Long getPaymentDataSource() {
        return paymentDataSource;
    }

    public void setPaymentDataSource(Long paymentDataSource) {
        this.paymentDataSource = paymentDataSource;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName == null ? null : processName.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
}