package com.wrq.service;

import com.wrq.entity.ProductCategory;

import java.util.List;

/**
 * Created by wangqian on 2019/1/18.
 */
public interface CategoryService {

    ProductCategory findOne (Integer categoryId);

    List<ProductCategory> findAll();

    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);

    ProductCategory save(ProductCategory productCategory);
}
