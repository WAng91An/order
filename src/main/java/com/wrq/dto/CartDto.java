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

    /*
        name: "张三"
        phone: "18868822111"
        address: "慕课网总部"
        openid: "ew3euwhd7sjw9diwkq" //用户的微信openid
        items: [{
            productId: "1423113435324",
            productQuantity: 2 //购买数量
        }]
     */
}
