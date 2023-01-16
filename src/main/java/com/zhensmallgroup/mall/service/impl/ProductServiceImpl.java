package com.zhensmallgroup.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhensmallgroup.mall.common.Constant;
import com.zhensmallgroup.mall.exception.ZhensMallException;
import com.zhensmallgroup.mall.exception.ZhensMallExceptionEnum;
import com.zhensmallgroup.mall.model.dao.ProductMapper;
import com.zhensmallgroup.mall.model.pojo.Product;
import com.zhensmallgroup.mall.model.query.ProductListQuery;
import com.zhensmallgroup.mall.model.request.AddProductReq;
import com.zhensmallgroup.mall.model.request.ProductListReq;
import com.zhensmallgroup.mall.model.vo.CategoryVO;
import com.zhensmallgroup.mall.service.CategoryService;
import com.zhensmallgroup.mall.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


@Service
public class ProductServiceImpl implements ProductService {

    /**
     * @return
     */
    @Autowired
    ProductMapper productMapper;

    @Autowired
    CategoryService categoryService;

    @Override
    public void add(AddProductReq addProductReq){
        Product product = new Product();
        BeanUtils.copyProperties(addProductReq, product);
        Product productOld = productMapper.selectByName(addProductReq.getName());
        if(productOld != null){
            throw new ZhensMallException(ZhensMallExceptionEnum.NAME_ALREADY_EXISTS);
        }
        int count = productMapper.insertSelective(product);
        if(count == 0){
            throw new ZhensMallException(ZhensMallExceptionEnum.CREATE_FAILED);
        }
    }

    @Override
    public void update(Product updateProduct){
        Product productOld = productMapper.selectByName(updateProduct.getName());
        if (productOld != null && updateProduct.getId().equals(productOld.getId())) {
            throw new ZhensMallException(ZhensMallExceptionEnum.NAME_ALREADY_EXISTS);
        }
        int count = productMapper.updateByPrimaryKeySelective(updateProduct);
        if (count == 0) {
            throw new ZhensMallException(ZhensMallExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public void delete(Integer id){
        Product productOld = productMapper.selectByPrimaryKey(id);
        if (productOld == null) {
            throw new ZhensMallException(ZhensMallExceptionEnum.DELETE_FAILED);
        }
        int count = productMapper.deleteByPrimaryKey(id);
        if (count == 0) {
            throw new ZhensMallException(ZhensMallExceptionEnum.DELETE_FAILED);
        }
    }

    @Override
    public void batchUpdateSellStatus(Integer[] ids, Integer sellStatus){
        productMapper.batchUpdateSellStatus(ids, sellStatus);
    }

    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum, pageSize);
        List<Product> products = productMapper.selectListForAdmin();
        PageInfo pageInfo = new PageInfo(products);
        return pageInfo;

    }

    @Override
    public Product detail(Integer id){
        Product product = productMapper.selectByPrimaryKey(id);
        return product;
    }

    @Override
    public PageInfo list(ProductListReq productListReq){
        ProductListQuery productListQuery = new ProductListQuery();

        if (!StringUtils.isEmpty(productListReq.getKeyword())) {
            String keyword = new StringBuilder().append("%").append(productListReq.getKeyword()).append("%").toString();

        }

        if (productListReq.getCategoryId()!=null) {
            List<CategoryVO> categoryVOList = categoryService.listCategoryForCustomer(productListReq.getCategoryId());
            ArrayList<Integer> categoryIds = new ArrayList<>();
            getCategoryIds(categoryVOList, categoryIds);
            productListQuery.setCategoryIds(categoryIds);
        }
        String orderBy = productListReq.getOrderBy();

        if (Constant.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
            PageHelper.startPage(productListReq.getPageNum(), productListReq.getPageSize(), orderBy);
        }else{
            PageHelper.startPage(productListReq.getPageNum(), productListReq.getPageSize());
        }

        List<Product> productlist = productMapper.selectList(productListQuery);
        PageInfo pageInfo = new PageInfo(productlist);
        return pageInfo;

    }

    private void getCategoryIds(List<CategoryVO> categoryVOList, ArrayList<Integer> categoryIds){
        for (int i = 0; i < categoryVOList.size(); i++) {
            CategoryVO categoryVO =  categoryVOList.get(i);
            if (categoryVO!=null) {
                categoryIds.add(categoryVO.getId());
                getCategoryIds(categoryVO.getChildCategory(), categoryIds);
            }

        }
    }






}
