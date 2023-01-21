package com.zhensmallgroup.mall.service.impl;

import com.zhensmallgroup.mall.common.Constant;
import com.zhensmallgroup.mall.exception.ZhensMallException;
import com.zhensmallgroup.mall.exception.ZhensMallExceptionEnum;
import com.zhensmallgroup.mall.filter.UserFilter;
import com.zhensmallgroup.mall.model.dao.CartMapper;
import com.zhensmallgroup.mall.model.dao.OrderItemMapper;
import com.zhensmallgroup.mall.model.dao.OrderMapper;
import com.zhensmallgroup.mall.model.dao.ProductMapper;
import com.zhensmallgroup.mall.model.pojo.Order;
import com.zhensmallgroup.mall.model.pojo.OrderItem;
import com.zhensmallgroup.mall.model.pojo.Product;
import com.zhensmallgroup.mall.model.request.CreateOrderReq;
import com.zhensmallgroup.mall.model.vo.CartVO;
import com.zhensmallgroup.mall.service.CartService;
import com.zhensmallgroup.mall.service.OrderService;
import com.zhensmallgroup.mall.util.OrderCodeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    CartService cartService;

    @Autowired
    CartMapper cartMapper;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    OrderItemMapper orderItemMapper;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public String create(CreateOrderReq createOrderReq){

        Integer userId = UserFilter.currentUser.getId();

        List<CartVO> cartVOList = cartService.list(userId);
        ArrayList<CartVO> cartVOListTemp = new ArrayList<>();

        for (int i = 0; i < cartVOList.size(); i++) {
            CartVO cartVO =  cartVOList.get(i);
            if(cartVO.getSelected().equals(Constant.Cart.CHECKED)){
                cartVOListTemp.add(cartVO);
            }
            
        }
        cartVOList = cartVOListTemp;

        if(CollectionUtils.isEmpty(cartVOList)){
            throw new ZhensMallException(ZhensMallExceptionEnum.CART_EMPTY);
        }
        
        validSaleStatusAndStock(cartVOList);

        List<OrderItem> orderItemList = cartVOListToOrderItemList(cartVOList);

        for (int i = 0; i < orderItemList.size(); i++) {
            OrderItem orderItem =  orderItemList.get(i);
            Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
            int stock = product.getStock() - orderItem.getQuantity();
            if (stock < 0) {
                throw new ZhensMallException(ZhensMallExceptionEnum.NOT_ENOUGH);
            }
            product.setStock(stock);
            productMapper.updateByPrimaryKeySelective(product);

        }
        
        cleanCart(cartVOList);

        Order order = new Order();

        String orderNo = OrderCodeFactory.getOrderCode(Long.valueOf(userId));
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setTotalPrice(totalPrice(orderItemList));
        order.setReceiverName(createOrderReq.getReceiverName());
        order.setReceiverMobile(createOrderReq.getReceiverMobile());
        order.setReceiverAddress(createOrderReq.getReceiverAddress());

        order.setOrderStatus(Constant.OrderStatusEnum.NOT_PAID.getCode());

        order.setPostage(0);
        order.setPaymentType(1);

        orderMapper.insertSelective(order);

        for (int i = 0; i < orderItemList.size(); i++) {
            OrderItem orderItem =  orderItemList.get(i);
            orderItem.setOrderNo(order.getOrderNo());
            orderItemMapper.insertSelective(orderItem);
        }

        return orderNo;


    }

    private Integer totalPrice(List<OrderItem> orderItemList) {
        Integer totalPrice = 0;
        for (int i = 0; i < orderItemList.size(); i++) {
            OrderItem orderItem =  orderItemList.get(i);
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    private void cleanCart(List<CartVO> cartVOList) {
        for (int i = 0; i < cartVOList.size(); i++) {
            CartVO cartVO =  cartVOList.get(i);
            cartMapper.deleteByPrimaryKey(cartVO.getId());

        }
    }


    private List<OrderItem> cartVOListToOrderItemList(List<CartVO> cartVOList) {
        List<OrderItem> orderItemList = new ArrayList<>();
        for (int i = 0; i < orderItemList.size(); i++) {
            CartVO cartVO = cartVOList.get(i);
            OrderItem orderItem =  new OrderItem();
            orderItem.setProductId(cartVO.getProductId());
            orderItem.setProductName(cartVO.getProductName());
            orderItem.setProductImg(cartVO.getProductImage());
            orderItem.setUnitPrice(cartVO.getPrice());
            orderItem.setQuantity(cartVO.getQuantity());
            orderItem.setTotalPrice(cartVO.getTotalPrice());
            orderItemList.add(orderItem);


        }
        return orderItemList;
    }

    private void validSaleStatusAndStock(List<CartVO> cartVOList) {

        for (int i = 0; i < cartVOList.size(); i++) {
            CartVO cartVO =  cartVOList.get(i);
            Product product = productMapper.selectByPrimaryKey(cartVO.getProductId());
            if (product==null || product.getStatus().equals(Constant.SaleStatus.NOT_SALE)) {
                throw new ZhensMallException(ZhensMallExceptionEnum.NOT_SALE);
            }
            if (cartVO.getQuantity()>product.getStock()) {
                throw new ZhensMallException(ZhensMallExceptionEnum.NOT_ENOUGH);

            }

        }


    }

}
