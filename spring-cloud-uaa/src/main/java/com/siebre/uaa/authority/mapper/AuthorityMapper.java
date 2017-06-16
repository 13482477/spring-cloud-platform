package com.siebre.uaa.authority.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.siebre.uaa.authority.entity.Authority;

@Repository
public interface AuthorityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Authority record);

    int insertSelective(Authority record);

    Authority selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Authority record);

    int updateByPrimaryKey(Authority record);

    List<Authority> selectByRoleID(Long id);

    List<Authority> selectByResourceID(Long id);
    
    List<Authority> getAllAuthorities();
}