package com.zhensmallgroup.mall.service.impl;

import com.zhensmallgroup.mall.common.Constant;
import com.zhensmallgroup.mall.exception.ZhensMallException;
import com.zhensmallgroup.mall.exception.ZhensMallExceptionEnum;
import com.zhensmallgroup.mall.model.dao.CartMapper;
import com.zhensmallgroup.mall.model.dao.ProductMapper;
import com.zhensmallgroup.mall.model.pojo.Cart;
import com.zhensmallgroup.mall.model.pojo.Product;
import com.zhensmallgroup.mall.model.vo.CartVO;
import com.zhensmallgroup.mall.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    ProductMapper productMapper;

    @Autowired
    CartMapper cartMapper;

    @Override
    public List<CartVO> list(Integer userId){
        List<CartVO> cartVOS = cartMapper.selectList(userId);
        for (int i = 0; i < cartVOS.size(); i++) {
            CartVO cartVO =  cartVOS.get(i);
            cartVO.setTotalPrice(cartVO.getPrice() * cartVO.getQuantity());

        }
        return cartVOS;

    }

    @Override
    public List<CartVO> add(Integer userId, Integer productId, Integer count){
        validProduct(productId, count);
        Cart cart = cartMapper.selectCartByuserIdAndProductId(userId, productId);
        if (cart == null) {
            cart = new Cart();
            cart.setProductId(productId);
            cart.setUserId(userId);
            cart.setQuantity(count);
            cart.setSelected(Constant.Cart.CHECKED);
            cartMapper.insertSelective(cart);
        }else {
            count = cart.getQuantity()+count;
            Cart cartNew = new Cart();
            cartNew.setQuantity(count);
            cartNew.setId(cart.getId());
            cartNew.setProductId(cart.getProductId());
            cartNew.setUserId(cart.getUserId());
            cartNew.setSelected(Constant.Cart.CHECKED);
            cartMapper.updateByPrimaryKeySelective(cartNew);
        }
        return this.list(userId);
    }

    private void validProduct(Integer productId, Integer count) {
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product==null || product.getStatus().equals(Constant.SaleStatus.NOT_SALE)) {
            throw new ZhensMallException(ZhensMallExceptionEnum.NOT_SALE);
        }
        if (count>product.getStock()) {
            throw new ZhensMallException(ZhensMallExceptionEnum.NOT_ENOUGH);

        }

    }

    @Override
    public List<CartVO> update(Integer userId, Integer productId, Integer count){
        validProduct(productId, count);
        Cart cart = cartMapper.selectCartByuserIdAndProductId(userId, productId);
        if (cart == null) {
            throw new ZhensMallException(ZhensMallExceptionEnum.UPDATE_FAILED);
        }else {

            Cart cartNew = new Cart();
            cartNew.setQuantity(count);
            cartNew.setId(cart.getId());
            cartNew.setProductId(cart.getProductId());
            cartNew.setUserId(cart.getUserId());
            cartNew.setSelected(Constant.Cart.CHECKED);
            cartMapper.updateByPrimaryKeySelective(cartNew);
        }
        return this.list(userId);
    }

    @Override
    public List<CartVO> delete(Integer userId, Integer productId){

        Cart cart = cartMapper.selectCartByuserIdAndProductId(userId, productId);
        if (cart == null) {
            throw new ZhensMallException(ZhensMallExceptionEnum.DELETE_FAILED);
        }else {

            cartMapper.deleteByPrimaryKey(cart.getId());
        }
        return this.list(userId);
    }

    @Override
    public List<CartVO> selectOrNot(Integer userId, Integer productId, Integer selected){
        Cart cart = cartMapper.selectCartByuserIdAndProductId(userId, productId);
        if (cart == null) {
            throw new ZhensMallException(ZhensMallExceptionEnum.UPDATE_FAILED);
        }else {

            cartMapper.selectOrNot(userId, productId, selected);
        }
        return this.list(userId);

    }

    @Override
    public List<CartVO> selectAllOrNot(Integer userId, Integer selected){
        cartMapper.selectOrNot(userId, null, selected);
        return this.list(userId);
    }
}
