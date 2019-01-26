package com.wrq.controller;

import com.wrq.common.ResponseCode;
import com.wrq.entity.ProductCategory;
import com.wrq.entity.ProductInfo;
import com.wrq.service.CategoryService;
import com.wrq.service.ProductService;
import com.wrq.utils.ResponseCodeUtil;
import com.wrq.vo.ProductInfoVo;
import com.wrq.vo.ProductVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by wangqian on 2019/1/26.
 */
@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;


    @GetMapping("/list")
    public ResponseCode list () {
        // 1. 查询所有的上架商品
        List<ProductInfo> productInfoList = productService.findUpAll();

        // 2. 查询类目(一次性查询)
        /* 传统方法 */
        List<Integer> categoryTypeList = new ArrayList<>();
        for ( ProductInfo product : productInfoList ){
            categoryTypeList.add(product.getCategoryType());
        }

        /* java 8 新特性  List<Integer> categoryTypeList = productInfoList.stream().map(e -> e.setCategoryType()).collect(Collectors.toList());*/

        List<ProductCategory> productCategoryList = categoryService.findByCategoryTypeIn(categoryTypeList);

        // 3. 数据拼装出来 data对象
        List<ProductVo> productVoList = new ArrayList<>();

        for (ProductCategory productCategory : productCategoryList){
            ProductVo productVo = new ProductVo();
            productVo.setCategoryType(productCategory.getCategoryType());
            productVo.setCategoryName(productCategory.getCategoryName());

            List<ProductInfoVo> productInfoVoList = new ArrayList<>();

            for (ProductInfo productInfo : productInfoList){
                if ( productInfo.getCategoryType().equals(productCategory.getCategoryType()) ){
                    ProductInfoVo productInfoVo = new ProductInfoVo();
//                    下面写法不优雅
//                    productInfoVo.setProductId(productInfo.getProductId());
//                    productInfoVo.setProductName(productInfo.getProductName());
//                    productInfoVo.setProductPrice(productInfo.getProductPrice());
//                    productInfoVo.setProductDescription(productInfo.getProductDescription());
//                    productInfoVo.setProductIcon(productInfo.getProductIcon());
//                    下面写法优雅
                    BeanUtils.copyProperties(productInfo, productInfoVo);
                    productInfoVoList.add(productInfoVo);
                }
            }
            productVo.setProductInfoVoList(productInfoVoList);
            productVoList.add(productVo);
        }

        // 4. 响应
        return ResponseCodeUtil.success(productVoList);
    }
}
