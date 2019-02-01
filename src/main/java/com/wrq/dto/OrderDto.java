package com.wrq.dto;

import com.wrq.entity.OrderDetail;
import com.wrq.enums.OrderStatusEnum;
import com.wrq.enums.PayStatusEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by wangqian on 2019/2/1.
 */
@Data
public class OrderDto {

    /* 订单id */
    private String orderId;

    /* 买家姓名  */
    private String buyerName;

    /* 买家电话 */
    private String buyerPhone;

    /* 买家地址 */
    private String buyerAddress;

    /* 买家微信openId */
    private String buyerOpenid;

    /* 订单总金额 */
    private BigDecimal orderAmount;

    /* 订单状态 */
    private Integer orderStatus = OrderStatusEnum.NEW.getCode();

    /* 支付状态 */
    private Integer payStatus = PayStatusEnum.WAIT.getCode();

    private Date createTime;

    private Date updateTime;

    private List<OrderDetail> orderDetailList;
}
