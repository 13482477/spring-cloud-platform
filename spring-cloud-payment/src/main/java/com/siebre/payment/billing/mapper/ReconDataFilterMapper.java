package com.siebre.payment.billing.mapper;

import com.siebre.payment.billing.entity.ReconDataFilter;
import org.springframework.stereotype.Repository;

@Repository
public interface ReconDataFilterMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ReconDataFilter record);

    int insertSelective(ReconDataFilter record);

    ReconDataFilter selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReconDataFilter record);

    int updateByPrimaryKey(ReconDataFilter record);
}