package com.wrq.service.impl;

import com.wrq.entity.ProductCategory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;


/**
 * 测试
 * Created by wangqian on 2019/1/18.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceImplTest {

    @Autowired
    private CategoryServiceImpl categoryService;

    @Test
    public void testFindOne() throws Exception {
        ProductCategory productCategory = categoryService.findOne(3);
        Assert.assertEquals(new Integer(3), productCategory.getCategoryId());
    }

    @Test
    public void testFindAll() throws Exception {
        List<ProductCategory> categories = categoryService.findAll();
        Assert.assertNotEquals(0, categories.size());
    }

    @Test
    public void testFindByCategoryTypeIn() throws Exception {
        List<ProductCategory> categories = categoryService.findByCategoryTypeIn(Arrays.asList(1, 2 , 3));
        Assert.assertNotEquals(0, categories.size());
    }

    @Test
    public void testSave() throws Exception {
        ProductCategory productCategory = new ProductCategory();

        productCategory.setCategoryName("男生专享");
        productCategory.setCategoryType(5);
        ProductCategory category = categoryService.save(productCategory);
        Assert.assertNotNull(category);
    }
}