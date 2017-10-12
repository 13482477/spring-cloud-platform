package com.siebre.uaa.openapiapplication.mapper;

import com.siebre.uaa.openapiapplication.entity.OpenApiApplication;
import org.springframework.stereotype.Repository;

@Repository
public interface OpenApiApplicationMapper {

    int deleteByPrimaryKey(Long id);

    int insert(OpenApiApplication record);

    int insertSelective(OpenApiApplication record);

    OpenApiApplication selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OpenApiApplication record);

    int updateByPrimaryKey(OpenApiApplication record);
}