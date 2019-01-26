package com.wrq.service.impl;

import com.wrq.common.ProductStatusEnum;
import com.wrq.entity.ProductInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by wangqian on 2019/1/26.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceImplTest {

    @Autowired
    private ProductServiceImpl productService;

    @Test
    public void testFindOne() throws Exception {
        ProductInfo one = productService.findOne("123456");
        Assert.assertEquals("123456", one.getProductId());
    }

    @Test
    public void testFindUpAll() throws Exception {
        List<ProductInfo> products = productService.findUpAll();
        Assert.assertNotEquals(0, products.size());
    }

    @Test
    public void testFindAll() throws Exception {
        /* Pageable是一个接口，PageRequest 是其实现类，我们测试需要传入PageRequest  */
        PageRequest request = new PageRequest(0, 2);
        Page<ProductInfo> all = productService.findAll(request);
        System.out.println(all.getTotalElements());
        System.out.println(all.getTotalPages());
        Assert.assertNotEquals(0, all.getTotalElements());
    }

    @Test
    public void testSave() throws Exception {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId("1234567");
        productInfo.setProductName("皮皮虾");
        productInfo.setProductPrice(new BigDecimal(3.2));
        productInfo.setProductStock(15);
        productInfo.setProductDescription("皮皮虾好吃");
        productInfo.setProductIcon("Icon地址");
        productInfo.setProductStatus(ProductStatusEnum.DOWN.getCode());
        productInfo.setCategoryType(2);
        ProductInfo product = productService.save(productInfo);
        Assert.assertEquals("1234567", product.getProductId());
    }
}