package com.zhensmallgroup.mall.controller;

import com.github.pagehelper.PageInfo;
import com.zhensmallgroup.mall.common.ApiRestResponse;
import com.zhensmallgroup.mall.model.pojo.Product;
import com.zhensmallgroup.mall.model.request.ProductListReq;
import com.zhensmallgroup.mall.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @ApiOperation("product detail")
    @GetMapping("/product/detail")
    public ApiRestResponse detail(@RequestParam Integer id){
        Product product = productService.detail(id);
        return ApiRestResponse.success(product);
    }

    @ApiOperation("product list")
    @GetMapping("/product/list")
    public ApiRestResponse list(ProductListReq productListReq){
        PageInfo list = productService.list(productListReq);
        return ApiRestResponse.success(list);
    }




}
