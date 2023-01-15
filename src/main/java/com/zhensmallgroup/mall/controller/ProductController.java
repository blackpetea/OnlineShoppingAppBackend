package com.zhensmallgroup.mall.controller;

import com.zhensmallgroup.mall.model.pojo.Product;
import com.zhensmallgroup.mall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/test2")
    @ResponseBody
    public Product productPage(){
        return productService.getProduct();
    }

}
