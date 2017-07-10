package com.siebre.payment.billing.mapper;

import com.siebre.payment.billing.entity.ReconItem;
import org.springframework.stereotype.Repository;

@Repository
public interface ReconItemMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ReconItem record);

    int insertSelective(ReconItem record);

    ReconItem selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReconItem record);

    int updateByPrimaryKey(ReconItem record);
}