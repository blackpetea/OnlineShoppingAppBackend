package com.zhensmallgroup.mall.model.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class AddProductReq {


    @NotNull(message = "Product name can't be null")
    private String name;

    @NotNull(message = "product image can't be null")
    private String image;

    private String detail;

    @NotNull(message = "product category can't be null")
    private Integer categoryId;

    @NotNull(message = "price can't be null")
    @Min(value = 1, message = "price can't be lower than 1")
    private Integer price;

    @NotNull(message = "stock can't be null")
    @Max(value = 10000, message = "stock can't be more than 10000")
    private Integer stock;

    private Integer status;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
