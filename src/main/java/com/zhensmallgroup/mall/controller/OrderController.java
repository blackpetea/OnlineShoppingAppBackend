package com.zhensmallgroup.mall.controller;

import com.zhensmallgroup.mall.common.ApiRestResponse;
import com.zhensmallgroup.mall.model.request.CreateOrderReq;
import com.zhensmallgroup.mall.service.OrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("order/create")
    @ApiOperation("create order")
    public ApiRestResponse create(@RequestBody CreateOrderReq createOrderReq){
        String orderNo = orderService.create(createOrderReq);

        return ApiRestResponse.success(orderNo);
    }
}
