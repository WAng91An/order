package com.wrq.repository;

import com.wrq.entity.OrderDetail;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by wangqian on 2019/2/1.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderDetailRepositoryTest {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Test
    public void testSave () {
        OrderDetail orderDetail = OrderDetail.builder().detailId("1").orderId("1").productIcon("www.xxx.com")
                .productId("12345").productName("北京烤鸭").productPrice(new BigDecimal(12)).productQuantity(12).build();
        OrderDetail detail = orderDetailRepository.save(orderDetail);
        Assert.assertEquals(detail.getOrderId(), "1");
    }

    @Test
    public void testFindByOrderId() throws Exception {
        List<OrderDetail> order = orderDetailRepository.findByOrderId("1");
        Assert.assertNotEquals(0, order.size());
    }
}