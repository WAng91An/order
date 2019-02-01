package com.wrq.repository;

import com.wrq.entity.OrderMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by wangqian on 2019 / 2 / 1.
 */
public interface OrderMasterRepository extends JpaRepository<OrderMaster, String> {

    /* 根据openid查询订单，不加pageable会把所有的信息都显示出来 */
    Page<OrderMaster> findByBuyerOpenid(String buyerOpenId, Pageable pageable);
}
