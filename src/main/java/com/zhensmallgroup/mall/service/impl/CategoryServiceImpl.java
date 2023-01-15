package com.zhensmallgroup.mall.service.impl;

import com.zhensmallgroup.mall.exception.ZhensMallException;
import com.zhensmallgroup.mall.exception.ZhensMallExceptionEnum;
import com.zhensmallgroup.mall.model.dao.CategoryMapper;
import com.zhensmallgroup.mall.model.pojo.Category;
import com.zhensmallgroup.mall.model.request.AddCategoryReq;
import com.zhensmallgroup.mall.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public void add(AddCategoryReq addCategoryReq){
        Category category = new Category();
        BeanUtils.copyProperties(addCategoryReq, category);
        Category categoryOld = categoryMapper.selectByName(addCategoryReq.getName());
        if (categoryOld != null) {
            throw new ZhensMallException(ZhensMallExceptionEnum.NAME_ALREADY_EXISTS);
        }
        int count = categoryMapper.insertSelective(category);
        if (count == 0) {
            throw new ZhensMallException(ZhensMallExceptionEnum.CREATE_FAILED);
        }


    }
}
