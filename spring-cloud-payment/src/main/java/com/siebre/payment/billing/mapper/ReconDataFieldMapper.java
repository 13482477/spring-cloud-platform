package com.siebre.payment.billing.mapper;

import com.siebre.payment.billing.entity.ReconDataField;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReconDataFieldMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ReconDataField record);

    int insertSelective(ReconDataField record);

    ReconDataField selectByPrimaryKey(Long id);

    List<ReconDataField> selectByDataSourceId(Long dataSourceId);

    int updateByPrimaryKeySelective(ReconDataField record);

    int updateByPrimaryKey(ReconDataField record);
}