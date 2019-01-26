package com.wrq.repository;

import com.wrq.entity.ProductInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;


/**
 * Created by wangqian on 2019/1/26.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductInfoRepositoryTest {

    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Test
    public void testSave () {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId("12345");
        productInfo.setProductName("皮蛋粥");
        productInfo.setProductPrice(new BigDecimal(3.2));
        productInfo.setProductStock(15);
        productInfo.setProductDescription("皮蛋粥好吃");
        productInfo.setProductIcon("Icon地址");
        productInfo.setProductStatus(1);
        productInfo.setCategoryType(2);
        ProductInfo product = productInfoRepository.save(productInfo);
        Assert.assertNotNull(product);
    }

    @Test
    public void testFindByProductStatus() throws Exception {
        List<ProductInfo> productInfos = productInfoRepository.findByProductStatus(1);
        Assert.assertNotEquals(0, productInfos.size());
    }
}