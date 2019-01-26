package com.wrq.common;

import lombok.Getter;

/**
 * Created by wangqian on 2019/1/26.
 */
@Getter
public enum ProductStatusEnum {
    UP(0, "在架"),
    DOWN(1, "下架")
    ;

    /* 状态码 */
    private Integer code;

    private String message;

    ProductStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
