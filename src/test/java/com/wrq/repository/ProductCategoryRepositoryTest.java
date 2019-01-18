package com.wrq.repository;

import com.wrq.entity.ProductCategory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;


/**
 * Created by wangqian on 2019/1/18.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryRepositoryTest {

    @Autowired
    private ProductCategoryRepository repository;

    @Test
    public void findOneTest () {
        ProductCategory productCategory = repository.findOne(1);
        System.out.println(productCategory);
    }

    @Test
    @Transactional
    /* @Transactional注解的意思：此插入会执行，但是执行后会回滚，数据没有记录 */
    public void insertOneTest () {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryName("女生最爱");
        productCategory.setCategoryType(4);
        repository.save(productCategory);
    }


    @Test
    public void updateOneTest () {
        ProductCategory productCategory = repository.findOne(2);
        productCategory.setCategoryName("热门爆款修改");
        repository.save(productCategory);
    }

    @Test
    public void findByCategoryTypeInTest () {
        List<ProductCategory> categoryTypeList = repository.findByCategoryTypeIn(Arrays.asList(2, 3, 4));
        Assert.assertNotEquals(0,categoryTypeList.size());
    }

}