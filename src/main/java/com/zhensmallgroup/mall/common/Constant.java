package com.zhensmallgroup.mall.common;

import com.google.common.collect.Sets;
import com.zhensmallgroup.mall.exception.ZhensMallException;
import com.zhensmallgroup.mall.exception.ZhensMallExceptionEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class Constant {
    public static final String SALT = "2jg8a*jjija9s∆j∆ˆˆˆˆ¥¨¨KJ=w=e;[fwer";

    public static final String ZHENS_MALL_USER = "zhens_mall_user";


    public static String FILE_UPLOAD_DIR;

    @Value("${file.upload.dir}")
    public void setFileUploadDir(String fileUploadDir){
        FILE_UPLOAD_DIR = fileUploadDir;
    }

    public interface ProductListOrderBy{
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price desc", "price asc");
    }

    public interface SaleStatus{
        int NOT_SALE = 0;
        int SALE = 1;
    }

    public interface Cart{
        int UNCHECKED = 0;
        int CHECKED = 1;
    }

    public enum OrderStatusEnum{
        CANCELED(0, "user canceled this order"),
        NOT_PAID(10, "not paid"),

        PAID(20, "is paid"),

        DELIVERED(30, "is sent out"),
        FINISHED(40, "Finished the order");

        private String value;
        private int code;

        OrderStatusEnum(int code, String value){
            this.value = value;
            this.code = code;
        }

        public static OrderStatusEnum codeOf(int code){
            for (OrderStatusEnum orderStatusEnum : values()){
                if(orderStatusEnum.getCode() == code){
                    return orderStatusEnum;
                }
            }
            throw new ZhensMallException(ZhensMallExceptionEnum.NO_ENUM);
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }

}
