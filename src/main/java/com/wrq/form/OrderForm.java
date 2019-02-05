package com.wrq.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;


/**
 * Created by wangqian on 2019/2/2.
 */
@Data
public class OrderForm {

    @NotEmpty(message = "姓名必填")
    private String name;

    @NotEmpty(message = "手机必填")
    private String phone;

    @NotEmpty(message = "地址必填")
    private String address;

    @NotEmpty(message = "openid必填")
    private String openid;

    @NotEmpty(message = "购物车不能为空")
    private String  items;
}
