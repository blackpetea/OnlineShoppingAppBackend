package com.zhensmallgroup.mall.service;

import com.zhensmallgroup.mall.exception.ZhensMallException;
import com.zhensmallgroup.mall.model.pojo.User;

public interface UserService {
    User getUser();

    void register(String userName, String password) throws ZhensMallException;


    User login(String userName, String password) throws ZhensMallException;

    void updateInformation(User user) throws ZhensMallException;

    boolean checkAdminRole(User user);
}
