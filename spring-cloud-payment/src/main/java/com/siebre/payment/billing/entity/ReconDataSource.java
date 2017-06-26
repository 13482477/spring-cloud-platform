package com.siebre.payment.billing.entity;

public class ReconDataSource {
    private Long id;

    private String name;

    private String type;

    private String dsDefinition;

    private String seperatorChar;

    private String lineSeperatorChar;

    private Integer ingoreFirst;

    private Integer ingoreEnd;

    private Integer bytesLength;

    private String encoding;

    private String startFlag;

    private String endFlag;

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

    public String getDsDefinition() {
        return dsDefinition;
    }

    public void setDsDefinition(String dsDefinition) {
        this.dsDefinition = dsDefinition == null ? null : dsDefinition.trim();
    }

    public String getSeperatorChar() {
        return seperatorChar;
    }

    public void setSeperatorChar(String seperatorChar) {
        this.seperatorChar = seperatorChar == null ? null : seperatorChar.trim();
    }

    public String getLineSeperatorChar() {
        return lineSeperatorChar;
    }

    public void setLineSeperatorChar(String lineSeperatorChar) {
        this.lineSeperatorChar = lineSeperatorChar == null ? null : lineSeperatorChar.trim();
    }

    public Integer getIngoreFirst() {
        return ingoreFirst;
    }

    public void setIngoreFirst(Integer ingoreFirst) {
        this.ingoreFirst = ingoreFirst;
    }

    public Integer getIngoreEnd() {
        return ingoreEnd;
    }

    public void setIngoreEnd(Integer ingoreEnd) {
        this.ingoreEnd = ingoreEnd;
    }

    public Integer getBytesLength() {
        return bytesLength;
    }

    public void setBytesLength(Integer bytesLength) {
        this.bytesLength = bytesLength;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding == null ? null : encoding.trim();
    }

    public String getStartFlag() {
        return startFlag;
    }

    public void setStartFlag(String startFlag) {
        this.startFlag = startFlag == null ? null : startFlag.trim();
    }

    public String getEndFlag() {
        return endFlag;
    }

    public void setEndFlag(String endFlag) {
        this.endFlag = endFlag == null ? null : endFlag.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
}