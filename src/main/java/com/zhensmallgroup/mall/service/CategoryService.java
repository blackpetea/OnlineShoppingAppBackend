package com.zhensmallgroup.mall.service;

import com.github.pagehelper.PageInfo;
import com.zhensmallgroup.mall.model.pojo.Category;
import com.zhensmallgroup.mall.model.request.AddCategoryReq;
import com.zhensmallgroup.mall.model.vo.CategoryVO;
import springfox.documentation.annotations.Cacheable;

import java.util.List;

public interface CategoryService {

    void add(AddCategoryReq addCategoryReq);

    void update(Category updateCategory);

    void delete(Integer id);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize);


    List<CategoryVO> listCategoryForCustomer(Integer parentId);
}
