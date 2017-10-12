package com.siebre.uaa.developeraccount.controller;

import com.siebre.basic.web.WebResult;
import com.siebre.uaa.developeraccount.entity.DeveloperAccount;
import com.siebre.uaa.developeraccount.service.DeveloperAccountService;
import com.siebre.uaa.user.entity.User;
import com.siebre.uaa.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jhonelee on 2017/7/21.
 */
@RestController
public class DeveloperAccountController {

    @Autowired
    private DeveloperAccountService developerAccountService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/api/v1/developerAccount", method = RequestMethod.POST)
    public WebResult<DeveloperAccount> createDeveloperAccount(@RequestBody DeveloperAccount developerAccount) {
        User user = this.userService.loadUserByUsername("admin");
        developerAccount.setUserId(user.getId());
        this.developerAccountService.create(developerAccount);

        return null;
    }


}
