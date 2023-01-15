package com.zhensmallgroup.mall.model.dao;

import com.zhensmallgroup.mall.model.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    //用 select 看一下这个 user 存不存在
    User selectByName(String userName);

    User selectLogin(@Param("userName") String userName, @Param("password")String password);
}