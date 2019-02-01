package com.wrq.service.impl;

import com.wrq.dto.OrderDto;
import com.wrq.entity.OrderDetail;
import com.wrq.entity.ProductInfo;
import com.wrq.repository.ProductInfoRepository;
import com.wrq.service.OrderService;
import com.wrq.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by wangqian on 2019/2/1.
 */
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductService productService;

    @Override
    public OrderDto create(OrderDto orderDto) {
        // 1. 查询商品（单价、库存）
        List<OrderDetail> orderDetailList = orderDto.getOrderDetailList();
        for (OrderDetail orderDetail : orderDetailList){
            String productId = orderDetail.getProductId();
            ProductInfo productInfo = productService.findOne(productId);
            if (productInfo.isEmpty)
        }

        // 2. 计算总价，库存判断

        // 3. 写入订单数据库（master、detail）

        // 4. 修改库存

        return null;
    }

    @Override
    public OrderDto findOne(String orderId) {
        return null;
    }

    @Override
    public Page<OrderDto> findList(String openid, Pageable pageable) {
        return null;
    }

    @Override
    public OrderDto cancel(OrderDto orderDto) {
        return null;
    }

    @Override
    public OrderDto finish(OrderDto orderDto) {
        return null;
    }

    @Override
    public OrderDto paid(OrderDto orderDto) {
        return null;
    }
}
