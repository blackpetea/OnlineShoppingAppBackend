package com.zhensmallgroup.mall.controller;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.github.pagehelper.PageInfo;
import com.zhensmallgroup.mall.common.ApiRestResponse;
import com.zhensmallgroup.mall.common.Constant;
import com.zhensmallgroup.mall.exception.ZhensMallException;
import com.zhensmallgroup.mall.exception.ZhensMallExceptionEnum;
import com.zhensmallgroup.mall.model.pojo.Product;
import com.zhensmallgroup.mall.model.request.AddProductReq;
import com.zhensmallgroup.mall.model.request.UpdateProductReq;
import com.zhensmallgroup.mall.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@RestController
public class ProductAdminController {

    @Autowired
    ProductService productService;

    @PostMapping("/admin/product/add")
    public ApiRestResponse addProduct(@Valid @RequestBody AddProductReq addProductReq){
        productService.add(addProductReq);
        return ApiRestResponse.success();
    }

    @PostMapping("/admin/upload/file")
    public ApiRestResponse upload(HttpServletRequest httpServletRequest, MultipartFile file){
        String fileName = file.getOriginalFilename();
        String suffixname = fileName.substring(fileName.lastIndexOf("."));
        UUID uuid = UUID.randomUUID();
        String newFileName = uuid.toString() + suffixname;
        File fileDirectory = new File(Constant.FILE_UPLOAD_DIR);
        File destFile = new File(Constant.FILE_UPLOAD_DIR + newFileName);
        if(!fileDirectory.exists()){
            if (!fileDirectory.mkdirs()) {
                throw new ZhensMallException(ZhensMallExceptionEnum.MKDIR_FAILED);
            }
        }

        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            return ApiRestResponse.success(getHost(new URI(httpServletRequest.getRequestURL()+""))+"/images/"+newFileName);
        } catch (URISyntaxException e) {
            return ApiRestResponse.error(ZhensMallExceptionEnum.UPLOAD_FAILED);
        }
    }

    private URI getHost(URI uri){
        URI effectiveURI;
        try {
            effectiveURI = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), null, null, null);
        } catch (URISyntaxException e) {
            effectiveURI = null;
        }
        return effectiveURI;
    }

    @ApiOperation("update product at back end ")
    @PostMapping("/admin/product/update")
    public ApiRestResponse updateProduct(@Valid @RequestBody UpdateProductReq updateProductReq){
        Product product = new Product();
        BeanUtils.copyProperties(updateProductReq, product);
        productService.update(product);
        return ApiRestResponse.success();
    }


    @ApiOperation("delete product at backend")
    @PostMapping("/admin/product/delete")
    public ApiRestResponse deleteProduct(@RequestParam Integer id){
        productService.delete(id);
        return ApiRestResponse.success();
    }

    @ApiOperation("batchimport product")
    @PostMapping("/admin/product/batchUpdateSellStatus")
    public ApiRestResponse batchUpdateSellStatus(@RequestParam Integer[] ids, @RequestParam Integer sellStatus){
        productService.batchUpdateSellStatus(ids, sellStatus);
        return ApiRestResponse.success();
    }

    @ApiOperation("后台商品列表接口")
    @PostMapping("/admin/product/list")
    public ApiRestResponse list(@RequestParam Integer pageNum, @RequestParam Integer pageSize){
        PageInfo pageInfo = productService.listForAdmin(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);

    }



}
