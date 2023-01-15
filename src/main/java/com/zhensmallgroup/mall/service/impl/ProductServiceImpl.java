package com.zhensmallgroup.mall.service.impl;

import com.zhensmallgroup.mall.model.dao.ProductMapper;
import com.zhensmallgroup.mall.model.pojo.Product;
import com.zhensmallgroup.mall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    /**
     * @return
     */
    @Autowired
    ProductMapper productMapper;

    @Override
    public Product getProduct() {
        return productMapper.selectByPrimaryKey(2);
    }
}
