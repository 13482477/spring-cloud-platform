package com.siebre.admin.menu.vo;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by AdamTang on 2017/3/14.
 * Project:siebre-cloud-platform
 * Version:1.0
 */
public class MenuDTO {

    private String name;
    private String code;
    private String url;

    private List<MenuDTO> children = new ArrayList<MenuDTO>();

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

    public List<MenuDTO> getChildren() {
        return children;
    }

    public void setChildren(List<MenuDTO> children) {
        this.children = children;
    }

}
