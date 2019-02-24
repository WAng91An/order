package com.wrq.service;

import com.lly835.bestpay.model.PayResponse;
import com.wrq.dto.OrderDto;

/**
 * Created by wangqian on 2019/2/15.
 */
public interface PayService {

    PayResponse create(OrderDto orderDto);
}
