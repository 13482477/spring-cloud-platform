package com.siebre.messageconsumer.mapper;

import com.siebre.basic.query.PageInfo;
import com.siebre.messageconsumer.entity.MessageObject;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageObjectMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MessageObject record);

    int insertSelective(MessageObject record);

    MessageObject selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MessageObject record);

    int updateByPrimaryKey(MessageObject record);
    
    List<MessageObject> selectByQuery(@Param("pageInfo")PageInfo pageInfo);
}