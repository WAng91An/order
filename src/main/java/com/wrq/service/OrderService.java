package com.wrq.service;

import com.wrq.dto.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * Created by wangqian on 2019/2/1.
 */
public interface OrderService {

    /* 创建订单 */
    OrderDto create(OrderDto orderDto);

    /* 查询单个订单 */
    OrderDto findOne(String orderId);

    /* 查询个人的订单列表 */
    Page<OrderDto> findList(String openid, Pageable pageable);

    /* 查询订单列表 */
    Page<OrderDto> findList(Pageable pageable);

    /* 取消订单 */
    OrderDto cancel(OrderDto orderDto);

    /* 完结订单 */
    OrderDto finish(OrderDto orderDto);

    /* 支付订单 */
    OrderDto paid(OrderDto orderDto);
}
