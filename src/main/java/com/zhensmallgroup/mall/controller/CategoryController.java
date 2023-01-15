package com.zhensmallgroup.mall.controller;

import com.zhensmallgroup.mall.common.ApiRestResponse;
import com.zhensmallgroup.mall.common.Constant;
import com.zhensmallgroup.mall.exception.ZhensMallExceptionEnum;
import com.zhensmallgroup.mall.model.pojo.User;
import com.zhensmallgroup.mall.model.request.AddCategoryReq;
import com.zhensmallgroup.mall.service.CategoryService;
import com.zhensmallgroup.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 *
 */
@Controller
public class CategoryController {
    @Autowired
    UserService userService;

    @Autowired
    CategoryService categoryService;

    @PostMapping("admin/category/add")
    @ResponseBody
    public ApiRestResponse addCategory(HttpSession session, @Valid @RequestBody AddCategoryReq addCategoryReq){

//        System.out.println("23232323"+addCategoryReq.getName()+addCategoryReq.getParentId()+addCategoryReq.getType()+addCategoryReq.getOrderNum());

//        if(addCategoryReq.getName() == null || addCategoryReq.getParentId() == null || addCategoryReq.getType() == null || addCategoryReq.getOrderNum() == null){
//            return ApiRestResponse.error(ZhensMallExceptionEnum.PARAM_NOT_NULL);
//        }

        User currentUser = (User)session.getAttribute((Constant.ZHENS_MALL_USER));
        if(currentUser == null){
            return ApiRestResponse.error(ZhensMallExceptionEnum.NEED_LOGIN);
        }
        boolean adminRole = userService.checkAdminRole(currentUser);

        if(adminRole){
            categoryService.add(addCategoryReq);
            return ApiRestResponse.success();
        }else{
            return ApiRestResponse.error(ZhensMallExceptionEnum.NEED_ADMIN);
        }
    }
}
