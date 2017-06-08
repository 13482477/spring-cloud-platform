package com.siebre.admin.resource.util;


import java.io.File;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.siebre.admin.enums.ResourceType;
import com.siebre.admin.resource.entity.Resource;
import com.siebre.admin.resource.service.ResourceService;
import com.siebre.basic.utils.JsonUtil;

/** 初始化Resource用
 * Created by AdamTang on 2017/3/22.
 * Project:siebre-cloud-platform
 * Version:1.0
 */
@Service
public class InitResourceTool {
    private static Logger logger = LoggerFactory.getLogger(InitResourceTool.class);

    @Autowired
    private ResourceService service;


    public boolean initResourceFromFile(File xml){
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ResourceDTO.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            ResourceDTO resource = (ResourceDTO) unmarshaller.unmarshal(xml);

            logger.debug(JsonUtil.toJson(resource,true));

            transform(new Resource(),resource,0);

            return true;
        }catch (JAXBException e) {
            logger.error("解析文件失败",e);
            return false;
        }
    }

    private void transform(Resource root,ResourceDTO dto,int index){
        Resource resource = new Resource();
        resource.setResourceName(dto.getName());
        resource.setResourceType(ResourceType.valueOf(dto.getType()));
        resource.setUrl(dto.getUrl());
        resource.setResourceCode(dto.getCode());
        resource.setDescription(dto.getDescription());
        resource.setSequence((index+1)*100);
        resource.setParentId(root.getId());
        resource.setCreateUser("admin");
        resource.setCreateDate(new Date());

        service.createResource(resource);

        List<ResourceDTO> child =dto.getResourceList();
        for(int i=0;i<child.size();i++){
            transform(resource,child.get(i),i);
        }
    }

}
