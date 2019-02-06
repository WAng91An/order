package com.wrq.service.impl;

import com.wrq.dto.OrderDto;
import com.wrq.enums.ResultEnum;
import com.wrq.exception.SellException;
import com.wrq.service.BuyerService;
import com.wrq.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wangqian on 2019/2/6.
 */
@Service
@Slf4j
public class BuyerServiceImpl implements BuyerService {

    @Autowired
    private OrderService orderService;

    @Override
    public OrderDto findOrderOne(String openid, String orderId) {
        return checkOrderOwner(openid, orderId);
    }

    @Override
    public OrderDto cancelOrder(String openid, String orderId) {

        OrderDto orderDto = checkOrderOwner(openid, orderId);

        if ( orderDto == null ){
            log.error("[取消订单] 此订单不存在,无法取消, orderId = {} ", orderId);
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        return orderService.cancel(orderDto);
    }

    private OrderDto checkOrderOwner(String openid, String orderId){
        OrderDto orderDto = orderService.findOne(orderId);

        if ( orderDto == null ){
            return null;
        }

        if ( !orderDto.getBuyerOpenid().equalsIgnoreCase(openid) ){
            log.error("[查询订单详情] 用户openid不匹配, 请求用户的openid = {}, 此订单实际用户openid = {}", openid, orderDto.getBuyerOpenid());
            throw new SellException(ResultEnum.ORDER_OWNER_ERROR);
        }

        return orderDto;
    }
}
