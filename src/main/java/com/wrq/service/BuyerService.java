package com.wrq.service;

import com.wrq.dto.OrderDto;

/**
 * Created by wangqian on 2019/2/6.
 */
public interface BuyerService {

    OrderDto findOrderOne(String openid, String orderId);

    OrderDto cancelOrder(String openid, String orderId);

}
