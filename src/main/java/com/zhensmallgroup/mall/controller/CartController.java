package com.zhensmallgroup.mall.controller;

import com.zhensmallgroup.mall.common.ApiRestResponse;
import com.zhensmallgroup.mall.filter.UserFilter;
import com.zhensmallgroup.mall.model.vo.CartVO;
import com.zhensmallgroup.mall.service.CartService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//use this request mapping, so that all the method inside needs this cart prefix
@RequestMapping("/cart")
public class CartController {
    @Autowired
    CartService cartService;

    @GetMapping("/list")
    @ApiOperation("List cart")
    public ApiRestResponse list(){

        List<CartVO> cartList = cartService.list(UserFilter.currentUser.getId());

        return ApiRestResponse.success(cartList);
    }

    @PostMapping("/add")
    @ApiOperation("add product to cart")
    public ApiRestResponse add(@RequestParam Integer productId, @RequestParam Integer count){

        List<CartVO> cartVOList = cartService.add(UserFilter.currentUser.getId(), productId, count);

        return ApiRestResponse.success(cartVOList);
    }

    @PostMapping("/update")
    @ApiOperation("update cart")
    public ApiRestResponse update(@RequestParam Integer productId, @RequestParam Integer count){

        List<CartVO> cartVOList = cartService.update(UserFilter.currentUser.getId(), productId, count);

        return ApiRestResponse.success(cartVOList);
    }

    @PostMapping("/delete")
    @ApiOperation("delete cart")
    public ApiRestResponse delete(@RequestParam Integer productId){

        List<CartVO> cartVOList = cartService.delete(UserFilter.currentUser.getId(), productId);

        return ApiRestResponse.success(cartVOList);
    }

    @PostMapping("/select")
    @ApiOperation("select/ deselect cart")
    public ApiRestResponse select(@RequestParam Integer productId, @RequestParam Integer selected){

        List<CartVO> cartVOList = cartService.selectOrNot(UserFilter.currentUser.getId(), productId, selected);

        return ApiRestResponse.success(cartVOList);
    }

    @PostMapping("/selectAll")
    @ApiOperation("all select/ all deselect cart")
    public ApiRestResponse selectAll(@RequestParam Integer productId, @RequestParam Integer selected){

        List<CartVO> cartVOList = cartService.selectAllOrNot(UserFilter.currentUser.getId(), selected);

        return ApiRestResponse.success(cartVOList);
    }


}
