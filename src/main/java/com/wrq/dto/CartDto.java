package com.wrq.dto;

import lombok.Data;

/**
 * Created by wangqian on 2019/2/2.
 */
@Data
public class CartDto {

    /* 商品id */
    private String productId;

    /* 购物车商品的数量 */
    private Integer productQuantity;

    public CartDto(String productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }

}
