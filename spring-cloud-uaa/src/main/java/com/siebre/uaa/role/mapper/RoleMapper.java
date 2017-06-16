package com.siebre.uaa.role.mapper;

import com.siebre.basic.query.PageInfo;
import com.siebre.uaa.role.entity.Role;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RoleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Long id);

    Role selectByRoleName(String name);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    List<Role> selectByUserID(Long id);

    List<Role> selectAll();
    
    List<Role> findRole(@Param("roleName")String roleName, @Param("roleCode")String roleCode, @Param("pageInfo")PageInfo pageInfo);
    
    Role getRole(Long id);
    
    void removeRoleAuthority(@Param("roleId")Long roleId);
    
    void grantAuthority(@Param("roleId")Long roleId, @Param("authorityIds")Set<Long> authorityIds);
}