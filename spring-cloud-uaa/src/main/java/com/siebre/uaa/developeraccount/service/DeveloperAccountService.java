package com.siebre.uaa.developeraccount.service;

import com.siebre.uaa.developeraccount.entity.DeveloperAccount;
import com.siebre.uaa.developeraccount.mapper.DeveloperAccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jhonelee on 2017/7/21.
 */
@Service
public class DeveloperAccountService {

    @Autowired
    private DeveloperAccountMapper developerAccountMapper;

    @Transactional
    public void create(DeveloperAccount developerAccount) {
        this.developerAccountMapper.insert(developerAccount);
    }


}
