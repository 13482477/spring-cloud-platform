package com.siebre.uaa.openapi.mapper;


import com.siebre.uaa.openapi.entity.OpenApi;
import org.springframework.stereotype.Repository;

@Repository
public interface OpenApiMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OpenApi record);

    int insertSelective(OpenApi record);

    OpenApi selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OpenApi record);

    int updateByPrimaryKey(OpenApi record);
}