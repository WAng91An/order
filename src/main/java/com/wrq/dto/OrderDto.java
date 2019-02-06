package com.wrq.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wrq.entity.OrderDetail;
import com.wrq.enums.OrderStatusEnum;
import com.wrq.enums.PayStatusEnum;
import com.wrq.utils.serializer.Date2LongSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by wangqian on 2019/2/1.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // 返回订单列表的时候,orderDetailList 不需要返回为null,使用此注解则不会返回这个字段
// 上一条注解只适用于 OrderDto 对象,如果全局配置可以在配置文件中: spring.jackson.default-property-inclusion:  进行全局配置
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

    /**
     * 使用自定义的 Date2LongSerializer
     */
    @JsonSerialize( using = Date2LongSerializer.class)
    private Date createTime;

    @JsonSerialize( using = Date2LongSerializer.class)
    private Date updateTime;

    private List<OrderDetail> orderDetailList;
}
