package com.wrq.enums;

import lombok.Getter;

/**
 * Created by wangqian on 2019/2/1.
 */
@Getter
public enum PayStatusEnum implements CodeEnum {

    WAIT(0, "等待支付"),
    SUCCESS(1, "支付成功")
    ;

    /* 状态码 */
    private Integer code;

    private String message;

    PayStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
