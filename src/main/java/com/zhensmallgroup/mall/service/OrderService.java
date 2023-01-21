package com.zhensmallgroup.mall.service;



import com.zhensmallgroup.mall.model.request.CreateOrderReq;

import java.util.List;

public interface OrderService {

    String create(CreateOrderReq createOrderReq);
}
