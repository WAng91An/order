package com.wrq.service.impl;

import com.wrq.dto.OrderDto;
import com.wrq.entity.OrderDetail;
import com.wrq.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by wangqian on 2019/2/2.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class OrderServiceImplTest {

    @Autowired
    private OrderService orderService;


    private static String OPEN_ID = "10010";

    @Test
    public void testCreate() throws Exception {
//    name: "张三"
//    phone: "18868822111"
//    address: "慕课网总部"
//    openid: "ew3euwhd7sjw9diwkq" //用户的微信openid
//    items: [{
//        productId: "1423113435324",
//        productQuantity: 2 //购买数量
//    }]
        OrderDto orderDto = new OrderDto();

        orderDto.setBuyerName("郭靖");
        orderDto.setBuyerAddress("桃花岛");
        orderDto.setBuyerPhone("15659566658");
        orderDto.setBuyerOpenid(OPEN_ID);

        List<OrderDetail> orderDetailList = new ArrayList<>();

        OrderDetail o1 = new OrderDetail();
        o1.setProductId("12345");
        o1.setProductQuantity(5);

        OrderDetail o2 = new OrderDetail();
        o2.setProductId("1234567");
        o2.setProductQuantity(88);

//      orderDetailList.add(o1);
        orderDetailList.add(o2);

        orderDto.setOrderDetailList(orderDetailList);

        OrderDto order = orderService.create(orderDto);
        log.info("创建订单：{}", order);
        Assert.assertNotNull(order);
    }

    @Test
    public void testFindOne() throws Exception {

    }

    @Test
    public void testFindList() throws Exception {

    }

    @Test
    public void testCancel() throws Exception {

    }

    @Test
    public void testFinish() throws Exception {

    }

    @Test
    public void testPaid() throws Exception {

    }
}