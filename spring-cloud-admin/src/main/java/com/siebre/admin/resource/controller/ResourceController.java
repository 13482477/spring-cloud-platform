package com.siebre.admin.resource.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Path;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.siebre.admin.resource.entity.Resource;
import com.siebre.admin.resource.service.ResourceService;
import com.siebre.basic.web.WebResult;

@RestController
public class ResourceController {

    private static Logger logger = LoggerFactory.getLogger(ResourceController.class);

    @Autowired
    private ResourceService resourceService;

    @RequestMapping(value ="/api/v1/resource/{id}", method = {RequestMethod.GET})
    public WebResult<Resource> load(@PathVariable Long id) {
        Resource resource = resourceService.getResourceByID(id);
        return WebResult.<Resource>builder().returnCode("200").data(resource).build();
    }
    
    @RequestMapping(value = "/api/v1/resource", method = {RequestMethod.POST})
    public WebResult<Resource> create(@RequestBody Resource resource) {
        String errorMessage = validateResource(resource);
        if (StringUtils.isNotBlank(errorMessage)) {
        	return WebResult.<Resource>builder().returnCode("500").returnMessage(errorMessage).build();
        }
        resourceService.createResource(resource);
        return WebResult.<Resource>builder().returnCode("200").data(resource).build();
    }

    @RequestMapping(value = "/api/v1/resource/{resourceId}", method = {RequestMethod.PUT})
    public WebResult<Resource> update(@PathVariable Long resourceId, @RequestBody Resource resource) {
        String validateMessage = validateResource(resource);
        if (StringUtils.isBlank(validateMessage)) {
            return WebResult.<Resource>builder().returnCode("500").returnMessage(validateMessage).build();
        }
        resourceService.updateResource(resource);
        return WebResult.<Resource>builder().returnCode("200").data(resource).build();
    }

    private String validateResource(Resource resource) {
        Set<String> errorMessages = new HashSet<>();
        if (resource.getResourceType() == null) {
            errorMessages.add("资源类型不能为空");
        }
        if (StringUtils.isBlank(resource.getResourceCode())) {
            errorMessages.add("资源代码不能为空");
        }
        if (StringUtils.isBlank(resource.getResourceName())) {
            errorMessages.add("资源名称不能为空");
        }
        if (StringUtils.isBlank(resource.getUrl())) {
            errorMessages.add("url不能为空");
        }
        return StringUtils.join(errorMessages, "|");
    }

    @RequestMapping(value = "/api/v1/resource/{resourceId}", method = {RequestMethod.DELETE})
    public WebResult<Resource> delete(@PathVariable Long resourceId) {
        resourceService.deleteResource(resourceId);
        return WebResult.<Resource>builder().returnCode("200").build();
    }

    @RequestMapping(value = "/api/v1/resources/parent/{parentId}", method = {RequestMethod.GET})
    public WebResult<List<Resource>> getChindren(@PathVariable Long parentId) {
        List<Resource> data = this.resourceService.getResourceByParentId(parentId);
        return WebResult.<List<Resource>>builder().returnCode("200").data(data).build();
    }
    
    @RequestMapping(value = "/api/v1/resourceTree/{roleId}", method = RequestMethod.GET)
	public WebResult<List<Resource>> loadResourceTree(@PathVariable Long roleId) {
		List<Resource> roots = this.resourceService.loadResourceTreeByRole(roleId);
		return WebResult.<List<Resource>>builder().returnCode("200").data(roots).build();
	}

}
