package com.zhensmallgroup.mall.controller;

import com.github.pagehelper.PageInfo;
import com.zhensmallgroup.mall.common.ApiRestResponse;
import com.zhensmallgroup.mall.common.Constant;
import com.zhensmallgroup.mall.exception.ZhensMallExceptionEnum;
import com.zhensmallgroup.mall.model.pojo.Category;
import com.zhensmallgroup.mall.model.pojo.User;
import com.zhensmallgroup.mall.model.request.AddCategoryReq;
import com.zhensmallgroup.mall.model.request.UpdateCategoryReq;
import com.zhensmallgroup.mall.model.vo.CategoryVO;
import com.zhensmallgroup.mall.service.CategoryService;
import com.zhensmallgroup.mall.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

/**
 *
 */
@Controller
public class CategoryController {
    @Autowired
    UserService userService;

    @Autowired
    CategoryService categoryService;

    @ApiOperation("add category at backend")
    @PostMapping("admin/category/add")
    @ResponseBody
    public ApiRestResponse addCategory(HttpSession session, @Valid @RequestBody AddCategoryReq addCategoryReq){

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

    @ApiOperation("update category at backend")
    @PostMapping("admin/category/update")
    @ResponseBody
    public ApiRestResponse updateCategory(@Valid @RequestBody UpdateCategoryReq updateCategoryReq, HttpSession session){
        User currentUser = (User)session.getAttribute((Constant.ZHENS_MALL_USER));
        if(currentUser == null){
            return ApiRestResponse.error(ZhensMallExceptionEnum.NEED_LOGIN);
        }
        boolean adminRole = userService.checkAdminRole(currentUser);

        if(adminRole){
            Category category = new Category();
            BeanUtils.copyProperties(updateCategoryReq, category);
            categoryService.update(category);
            return ApiRestResponse.success();
        }else{
            return ApiRestResponse.error(ZhensMallExceptionEnum.NEED_ADMIN);
        }
    }

    @ApiOperation("后台删除目录")
    @PostMapping("admin/category/delete")
    @ResponseBody
    public ApiRestResponse deleteCategory(@RequestParam Integer id){
        categoryService.delete(id);
        return ApiRestResponse.success();
    }

    @ApiOperation("后台目录列表")
    @PostMapping("admin/category/list")
    @ResponseBody
    public ApiRestResponse listCategoryForAdmin(@RequestParam Integer pageNum, @RequestParam Integer pageSize){
        PageInfo pageInfo = categoryService.listForAdmin(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }

    @ApiOperation("customer目录列表")
    @PostMapping("category/list")
    @ResponseBody
    public ApiRestResponse listCategoryForAdmin(){
        List<CategoryVO> CategoryVOs = categoryService.listCategoryForCustomer(0);
        return ApiRestResponse.success(CategoryVOs);
    }


}
