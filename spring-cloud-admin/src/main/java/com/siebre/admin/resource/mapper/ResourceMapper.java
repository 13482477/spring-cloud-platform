package com.siebre.admin.resource.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.siebre.admin.enums.ResourceType;
import com.siebre.admin.resource.entity.Resource;

@Repository
public interface ResourceMapper {
    int deleteByPrimaryKey(Long id);

    void insert(Resource record);

    int insertSelective(Resource record);

    Resource selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Resource record);

    int updateByPrimaryKey(Resource record);
    
    List<Resource> getChildren(@Param("parentId")Long parentId);

    Long getChildrenCount(@Param("parentId")Long parentId);

    List<Resource> getAllResource();

    List<Resource> getResourceByParent(@Param("parentId")Long parentId,@Param("type") ResourceType type);

    List<Resource> getSortedResourceByParent(@Param("parentId")Long parentId,@Param("type") ResourceType type);

    void insertResourceAuthority(@Param("resourceId")Long resourceId, @Param("authorityId")Long authorityId);
    
    List<Resource> getResourcesByIds(@Param("resourceIds")Long[] resourceIds);
    
    List<Resource> getAllResourceWithAuthority();

    int clearAuthority(Long id);
    
    Resource getResourceByResourceCode(@Param("resourceCode")String resourceCode);
}