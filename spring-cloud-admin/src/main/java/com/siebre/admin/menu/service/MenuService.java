package com.siebre.admin.menu.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.siebre.admin.authority.entity.Authority;
import com.siebre.admin.menu.vo.MenuDTO;
import com.siebre.admin.resource.entity.Resource;
import com.siebre.admin.resource.service.ResourceService;
import com.siebre.admin.role.entity.Role;
import com.siebre.admin.user.entity.User;

/**
 * Created by AdamTang on 2017/3/7.
 * Project:siebre-cloud-platform
 * Version:1.0
 */
@Service
public class MenuService {

    @Autowired
    private ResourceService resourceService;

    public MenuDTO menuTree(User user){
    	
        Set<String> authorityCodes = processAuth(user);

        List<Resource> resources = resourceService.loadAuthorityMenu(authorityCodes);
        MenuDTO root = new MenuDTO();
        
        for (Resource resource : resources) {
        	MenuDTO menu = new MenuDTO();
        	menu.setUrl(resource.getUrl());
        	menu.setCode(resource.getResourceCode());
        	menu.setName(resource.getResourceName());
        	this.initMenu(resource, menu);
        	root.getChildren().add(menu);
        }
        
        return root;
    }
    
    private Set<String> processAuth(User user) {
    	Set<String> authStrs = new HashSet<String>();
    	
    	for (Role role : user.getRoles()) {
    		for (Authority authority : role.getAuthorities()) {
    			authStrs.add(authority.getAuthorityCode());
    		}
    	}
    	
    	return authStrs;
    }

    private void initMenu(Resource resource,MenuDTO menuDTO){
        List<MenuDTO> menus = new ArrayList<>();
        for(Resource tmp:resource.getChildren()){
            if(tmp.getChecked()!=null && tmp.getChecked().equals(Boolean.TRUE)){
                MenuDTO menu = new MenuDTO();
                menu.setUrl(tmp.getUrl());
                menu.setCode(tmp.getResourceCode());
                menu.setName(tmp.getResourceName());
                initMenu(tmp,menu);
                menus.add(menu);
            }
        }
        menuDTO.setChildren(menus);
    }

}
