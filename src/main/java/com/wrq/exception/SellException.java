package com.wrq.exception;

import com.wrq.enums.ResultEnum;

/**
 * Created by wangqian on 2019/2/1.
 */
public class SellException extends RuntimeException {

    private Integer code;

    public SellException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());

        this.code = resultEnum.getCode();
    }
}
