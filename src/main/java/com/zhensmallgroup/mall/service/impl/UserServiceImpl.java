package com.zhensmallgroup.mall.service.impl;

import com.zhensmallgroup.mall.exception.ZhensMallException;
import com.zhensmallgroup.mall.exception.ZhensMallExceptionEnum;
import com.zhensmallgroup.mall.model.dao.UserMapper;
import com.zhensmallgroup.mall.model.pojo.User;
import com.zhensmallgroup.mall.service.UserService;
import com.zhensmallgroup.mall.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;


    @Override
    public User getUser() {
        return userMapper.selectByPrimaryKey(1);
    }

    @Override
    public void register(String userName, String password) throws ZhensMallException {

        //check if the user already exists
        User result = userMapper.selectByName(userName);
        if(result != null){
            throw new ZhensMallException(ZhensMallExceptionEnum.NAME_ALREADY_EXISTS);
        }

        User user = new User();
        user.setUsername(userName);
        try {
            user.setPassword(MD5Utils.getMD5Str(password));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
//        user.setPassword(password);
        int count = userMapper.insertSelective(user);
        if(count==0){
            throw new ZhensMallException(ZhensMallExceptionEnum.INSERT_FAILED);
        }
    }

    @Override
    public User login(String userName, String password) throws ZhensMallException {
        String md5Password = null;

            try {
                md5Password = MD5Utils.getMD5Str(password);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            User user = userMapper.selectLogin(userName, md5Password);
            if(user == null){
                throw new ZhensMallException(ZhensMallExceptionEnum.WRONG_PASSWORD);
            }
            return user;

    }

    @Override
    public void updateInformation(User user) throws ZhensMallException {

        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if(updateCount>1){
            throw new ZhensMallException(ZhensMallExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public boolean checkAdminRole(User user){
        return user.getRole().equals(2);
    }


}
