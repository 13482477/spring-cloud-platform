package com.siebre.uaa.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.siebre.basic.query.PageInfo;
import com.siebre.uaa.user.entity.User;

@Repository
public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    User selectByUsername(String username);

    int selectCountByUsername(String username);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    List<User> selectByRoleID(Long id);

    List<User> getAllUser();

    List<User> selectByPage(PageInfo pageInfo);

    int insertUserRoles(@Param("user")Long userID,@Param("role")List<Long> rolesID);

    int deleteUserRoles(Long id);
    
    User loadUserByUsername(@Param("username")String username);
}