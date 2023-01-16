package com.zhensmallgroup.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhensmallgroup.mall.exception.ZhensMallException;
import com.zhensmallgroup.mall.exception.ZhensMallExceptionEnum;
import com.zhensmallgroup.mall.model.dao.CategoryMapper;
import com.zhensmallgroup.mall.model.pojo.Category;
import com.zhensmallgroup.mall.model.request.AddCategoryReq;
import com.zhensmallgroup.mall.model.vo.CategoryVO;
import com.zhensmallgroup.mall.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import springfox.documentation.annotations.Cacheable;

import java.util.ArrayList;
import java.util.List;

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
    @Override
    public void update(Category updateCategory){
        if (updateCategory.getName()!=null) {
            Category categoryOld = categoryMapper.selectByName(updateCategory.getName());
            if(categoryOld != null && !categoryOld.getId().equals(updateCategory.getId())){
                throw new ZhensMallException(ZhensMallExceptionEnum.NAME_ALREADY_EXISTS);
            }
        }
        int count = categoryMapper.updateByPrimaryKeySelective(updateCategory);
        if(count == 0){
            throw new ZhensMallException(ZhensMallExceptionEnum.UPDATE_FAILED);
        }
    }
    @Override
    public void delete(Integer id){
        Category categoryOld = categoryMapper.selectByPrimaryKey(id);

        if(categoryOld == null){
            throw new ZhensMallException(ZhensMallExceptionEnum.DELETE_FAILED);
        }
        int count = categoryMapper.deleteByPrimaryKey(id);
        if(count == 0){
            throw new ZhensMallException(ZhensMallExceptionEnum.DELETE_FAILED);
        }
    }

    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum, pageSize, "type, order_num");
        List<Category> categoryList = categoryMapper.selectList();
        PageInfo pageInfo = new PageInfo(categoryList);
        return pageInfo;
    }

    @Override
    @Cacheable(value="listCategoryForCustomer")
    public List<CategoryVO> listCategoryForCustomer(Integer parentId){
        ArrayList<CategoryVO> categoryV0List = new ArrayList<>();
        recursivelyFindCategories(categoryV0List, parentId);
        return categoryV0List;
    }

    private void recursivelyFindCategories(List<CategoryVO> categoryVOList, Integer parentId){
        List<Category> categoryList = categoryMapper.selectCategoriesByParentId(parentId);
        if(!CollectionUtils.isEmpty(categoryList)){
            for (int i = 0; i < categoryList.size(); i++) {
                Category category =  categoryList.get(i);
                CategoryVO categoryVO = new CategoryVO();
                BeanUtils.copyProperties(category, categoryVO);
                categoryVOList.add(categoryVO);
                recursivelyFindCategories(categoryVO.getChildCategory(), categoryVO.getId());
                
            }
        }
    }
}
