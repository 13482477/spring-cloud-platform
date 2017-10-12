package com.siebre.wechat.evaluation.mapper;

import com.siebre.wechat.evaluation.module.Evaluation;

public interface EvaluationMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Evaluation record);

    int insertSelective(Evaluation record);

    Evaluation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Evaluation record);

    int updateByPrimaryKey(Evaluation record);
}