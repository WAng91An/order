package com.wrq.common;

import lombok.Data;

/**
 * Created by wangqian on 2019/1/26.
 */
@Data
public class ResponseCode<T> {

    /* 成功状态码 */
    private Integer code;

    /* 成功描述 */
    private String msg;

    /* 具体数据 */
    private T data;
}
