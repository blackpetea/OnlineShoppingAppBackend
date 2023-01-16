package com.zhensmallgroup.mall.service;

import com.github.pagehelper.PageInfo;
import com.zhensmallgroup.mall.model.pojo.Product;
import com.zhensmallgroup.mall.model.request.AddProductReq;
import com.zhensmallgroup.mall.model.request.ProductListReq;

public interface ProductService {


    void add(AddProductReq addProductReq);

    void update(Product updateProduct);

    void delete(Integer id);

    void batchUpdateSellStatus(Integer[] ids, Integer sellStatus);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    Product detail(Integer id);

    PageInfo list(ProductListReq productListReq);
}
