package com.wrq.repository;

import com.wrq.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by wangqian on 2019/2/1.
 */
public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {

    /* 获取订单详情 */
    List<OrderDetail> findByOrderId(String orderId);

}
