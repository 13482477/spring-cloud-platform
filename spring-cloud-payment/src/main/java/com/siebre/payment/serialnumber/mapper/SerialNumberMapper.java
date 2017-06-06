package com.siebre.payment.serialnumber.mapper;

import com.siebre.payment.serialnumber.entity.SerialNumber;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by AdamTang on 2017/3/29.
 * Project:siebre-cloud-platform
 * Version:1.0
 */
@Repository
public interface SerialNumberMapper {
    int insert(@Param("pojo") SerialNumber pojo);

    int insertSelective(@Param("pojo") SerialNumber pojo);

    int insertList(@Param("pojos") List<SerialNumber> pojo);

    int update(@Param("pojo") SerialNumber pojo);

    List<SerialNumber> selectAll();

    long selectAndUpdate(String name,long value);
    
    String nextValue(@Param("serialName")String serialName);
}
