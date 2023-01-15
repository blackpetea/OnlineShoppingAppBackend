package com.zhensmallgroup.mall.controller;
import com.zhensmallgroup.mall.common.ApiRestResponse;
import com.zhensmallgroup.mall.common.Constant;
import com.zhensmallgroup.mall.exception.ZhensMallException;
import com.zhensmallgroup.mall.exception.ZhensMallExceptionEnum;
import com.zhensmallgroup.mall.model.pojo.User;
import com.zhensmallgroup.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    //@Autowired, 按类型, 直接把 userService 抓来给 UserController 当注解
    @Autowired
    UserService userService;

    @GetMapping("/test")
    @ResponseBody
    public User personalPage(){
        return userService.getUser();
    }


    @PostMapping("/register")
    @ResponseBody
    public ApiRestResponse register(@RequestParam("userName") String userName, @RequestParam("password")String password) throws ZhensMallException {

        if(StringUtils.isEmpty(userName)){
            return ApiRestResponse.error(ZhensMallExceptionEnum.NEED_USER_NAME);
        }

        if(StringUtils.isEmpty(password)){
            return ApiRestResponse.error(ZhensMallExceptionEnum.NEED_PASSWORD);
        }

        if(password.length() <8){
            return ApiRestResponse.error(ZhensMallExceptionEnum.PASSWORD_TOO_SHORT);
        }
        userService.register(userName, password);
        return ApiRestResponse.success();
    }

    @PostMapping("/login")
    @ResponseBody
    public ApiRestResponse login(@RequestParam("userName") String userName, @RequestParam("password") String password, HttpSession session) throws ZhensMallException {
        if(StringUtils.isEmpty(userName)){
            return ApiRestResponse.error(ZhensMallExceptionEnum.NEED_USER_NAME);
        }
        if(StringUtils.isEmpty(password)){
            return ApiRestResponse.error(ZhensMallExceptionEnum.NEED_PASSWORD);
        }

        User user = userService.login(userName, password);
        user.setPassword(null);
        session.setAttribute(Constant.ZHENS_MALL_USER, user);
        return ApiRestResponse.success(user);
    }
    @PostMapping("/user/update")
    @ResponseBody
    public ApiRestResponse updateUserInfo(HttpSession session, @RequestParam String signature) throws ZhensMallException {
        User currentUser = (User)session.getAttribute(Constant.ZHENS_MALL_USER);
        if(currentUser == null){
            return ApiRestResponse.error(ZhensMallExceptionEnum.NEED_LOGIN);
        }
        User user = new User();
        user.setId(currentUser.getId());
        user.setPersonalizedSignature(signature);
        userService.updateInformation(user);
        return ApiRestResponse.success();
    }

    @PostMapping("/user/logout")
    @ResponseBody
    public ApiRestResponse logout(HttpSession session){
        session.removeAttribute(Constant.ZHENS_MALL_USER);
        return ApiRestResponse.success();
    }

    @PostMapping("/adminLogin")
    @ResponseBody
    public ApiRestResponse adminLogin(@RequestParam("userName") String userName,
                                      @RequestParam("password") String password, HttpSession session)
            throws ZhensMallException{
        if(StringUtils.isEmpty(userName)){
            return ApiRestResponse.error(ZhensMallExceptionEnum.NEED_USER_NAME);
        }
        if(StringUtils.isEmpty(password)){
            return ApiRestResponse.error(ZhensMallExceptionEnum.NEED_PASSWORD);
        }

        User user = userService.login(userName, password);

        if (userService.checkAdminRole(user)) {
            user.setPassword(null);
            session.setAttribute(Constant.ZHENS_MALL_USER, user);
            return ApiRestResponse.success(user);
        }else{
            return ApiRestResponse.error(ZhensMallExceptionEnum.NEED_ADMIN);
        }

    }


}
