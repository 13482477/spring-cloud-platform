package com.siebre.uaa.developeraccount.mapper;

import com.siebre.uaa.developeraccount.entity.DeveloperAccount;
import org.springframework.stereotype.Repository;

@Repository
public interface DeveloperAccountMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DeveloperAccount record);

    int insertSelective(DeveloperAccount record);

    DeveloperAccount selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeveloperAccount record);

    int updateByPrimaryKey(DeveloperAccount record);
}