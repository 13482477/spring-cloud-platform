package com.siebre.payment.billing.entity;

public class ReconMatchRule {
    private Long id;

    private Long jobId;

    private String name;

    private String type;

    private String matchCriteria;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
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

    public String getMatchCriteria() {
        return matchCriteria;
    }

    public void setMatchCriteria(String matchCriteria) {
        this.matchCriteria = matchCriteria == null ? null : matchCriteria.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
}