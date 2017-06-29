package com.siebre.uaa.initializer.dto;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/** 资源xml解析bean
 * Created by AdamTang on 2017/3/22.
 * Project:siebre-cloud-platform
 * Version:1.0
 */
@XmlRootElement(name = "resource")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class ResourceDTO {

    @XmlAttribute(name="type")
    private String type;

    @XmlAttribute(name="name")
    private String name;

    @XmlAttribute(name="code")
    private String code;

    @XmlAttribute(name="url")
    private String url;

    @XmlAttribute(name="description")
    private String description;

    @XmlElement(name="resource")
    private List<ResourceDTO> resourceList = new ArrayList<>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ResourceDTO> getResourceList() {
        return resourceList;
    }

    public void setResourceList(List<ResourceDTO> resourceList) {
        this.resourceList = resourceList;
    }

}
