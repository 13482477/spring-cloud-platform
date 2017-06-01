package com.siebre.payment.serialnumber.entity;

/**
 * Created by AdamTang on 2017/3/29.
 * Project:siebre-cloud-platform
 * Version:1.0
 */

import com.siebre.basic.model.BaseObject;

/**
 * 序列号对象
 */
public class SerialNumber extends BaseObject {

	private static final long serialVersionUID = 6562970153616243223L;

	/**
     * 序列号名称
     */
    private String name;

    /**
     * 前缀
     */
    private String prefix;

    /**
     * 长度
     */
    private Integer length;

    /**
     * 增长步长
     */
    private Integer step;

    /**
     * 是否需要日期填充
     */
    private Boolean dateFill;

    /**
     * 一次缓存的Step
     */
    private Long cacheStep;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public Boolean getDateFill() {
        return dateFill;
    }

    public void setDateFill(Boolean dateFill) {
        this.dateFill = dateFill;
    }

    public Long getCacheStep() {
        return cacheStep;
    }

    public void setCacheStep(Long cacheStep) {
        this.cacheStep = cacheStep;
    }
}
