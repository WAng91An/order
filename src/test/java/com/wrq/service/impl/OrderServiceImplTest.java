package com.wrq.service.impl;

import com.wrq.dto.OrderDto;
import com.wrq.entity.OrderDetail;
import com.wrq.enums.OrderStatusEnum;
import com.wrq.enums.PayStatusEnum;
import com.wrq.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private static String ORDER_ID = "1549112886517970694";

    @Test
    public void testCreate() throws Exception {
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
        OrderDto orderDto = orderService.findOne(ORDER_ID);
        log.info(" 查询单个订单: orderDto={}", orderDto);
        Assert.assertEquals(orderDto.getOrderId(), ORDER_ID);
    }

    @Test
    public void testFindList() throws Exception {
        PageRequest pageRequest = new PageRequest(0, 2);
        Page<OrderDto> orderDtoPage = orderService.findList(OPEN_ID, pageRequest);
        Assert.assertNotEquals(0, orderDtoPage.getTotalElements());
    }

    @Test
    public void testCancel() throws Exception {
        OrderDto orderDto;
        orderDto = orderService.findOne(ORDER_ID);
        OrderDto result = orderService.cancel(orderDto);
        Assert.assertEquals(result.getOrderStatus(), OrderStatusEnum.CANCEL.getCode());
    }

    @Test
    public void testFinish() throws Exception {
        OrderDto orderDto;
        orderDto = orderService.findOne(ORDER_ID);
        OrderDto result = orderService.finish(orderDto);
        Assert.assertEquals(result.getOrderStatus(), OrderStatusEnum.FINISHED.getCode());
    }

    @Test
    public void testPaid() throws Exception {
        OrderDto orderDto;
        orderDto = orderService.findOne(ORDER_ID);
        OrderDto result = orderService.paid(orderDto);
        Assert.assertEquals(result.getPayStatus(), PayStatusEnum.SUCCESS.getCode());
    }
}