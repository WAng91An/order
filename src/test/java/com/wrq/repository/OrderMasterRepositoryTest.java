package com.wrq.repository;

import com.wrq.entity.OrderMaster;
import com.wrq.enums.OrderStatusEnum;
import com.wrq.enums.PayStatusEnum;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by wangqian on 2019/2/1.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderMasterRepositoryTest {

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Test
    public void testFindByBuyerOpenid() throws Exception {
        PageRequest pageRequest = new PageRequest(1, 10);
        Page<OrderMaster> orders = orderMasterRepository.findByBuyerOpenid("123456", pageRequest);
        Assert.assertNotNull(orders);
    }

    @Test
    public void testSave () {

        OrderMaster orderMaster = OrderMaster.builder().orderId("1").buyerAddress("山东省济宁市兖州区").buyerName("郭靖").buyerOpenid("123456").buyerPhone("15659499987")
                .createTime(new Date()).orderAmount(new BigDecimal(12.8)).orderStatus(OrderStatusEnum.NEW.getCode()).payStatus(PayStatusEnum.WAIT.getCode()).build();

        OrderMaster order = orderMasterRepository.save(orderMaster);
        Assert.assertEquals(order.getOrderId(), "1");
    }
}