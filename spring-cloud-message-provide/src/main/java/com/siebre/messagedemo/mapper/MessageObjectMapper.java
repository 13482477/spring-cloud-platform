package com.siebre.messagedemo.mapper;

import com.siebre.messagedemo.entity.MessageObject;

public interface MessageObjectMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MessageObject record);

    int insertSelective(MessageObject record);

    MessageObject selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MessageObject record);

    int updateByPrimaryKey(MessageObject record);
}