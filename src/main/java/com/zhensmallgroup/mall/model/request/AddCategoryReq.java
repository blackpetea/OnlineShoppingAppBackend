package com.zhensmallgroup.mall.model.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AddCategoryReq {

    @Size(min = 2, max = 5)
    private String name;

    @NotNull
    private Integer type;

    @NotNull(message = "parentId can't be null")
    private Integer parentId;

    @NotNull
    private Integer orderNum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }
}
