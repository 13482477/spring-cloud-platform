package com.siebre.agreement.dto;

import com.siebre.agreement.dto.annotation.PropertyName;
import com.siebre.agreement.dto.annotation.PropertyValue;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "property")
public class PropertyData {

    private String name;

    private String value;

    public PropertyData(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @XmlAttribute
    @PropertyName
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlValue
    @PropertyValue
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
