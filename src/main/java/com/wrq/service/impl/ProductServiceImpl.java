package com.wrq.service.impl;

import com.wrq.dto.CartDto;
import com.wrq.enums.ProductStatusEnum;
import com.wrq.entity.ProductInfo;
import com.wrq.enums.ResultEnum;
import com.wrq.exception.SellException;
import com.wrq.repository.ProductInfoRepository;
import com.wrq.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wang qian on 2019/1/26.
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Override
    public ProductInfo findOne(String productId) {
        return productInfoRepository.findOne(productId);
    }

    @Override
    public List<ProductInfo> findUpAll() {
        /* 查询code为0，即在架的商品。使用枚举解耦和 */
        return productInfoRepository.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return productInfoRepository.findAll(pageable);
    }

    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return productInfoRepository.save(productInfo);
    }

    @Override
    @Transactional // 参数是集合，要么都成功，要么都不成功。
    public void increaseStock(List<CartDto> cartDtoList) {
        for ( CartDto cartDto : cartDtoList) {
            ProductInfo product = productInfoRepository.findOne(cartDto.getProductId());
            if ( product == null ){
                // 商品不存在，抛出异常
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            Integer result = product.getProductStock() + cartDto.getProductQuantity();
            product.setProductStock(result);
            productInfoRepository.save(product);
        }
    }

    @Override
    @Transactional // 参数是集合，要么都成功，要么都不成功。
    public void decreaseStock(List<CartDto> cartDtoList) {
        // 遍历CartDto
        for ( CartDto cartDto : cartDtoList) {
            ProductInfo product = productInfoRepository.findOne(cartDto.getProductId());
            // CartDto中的id对应的商品不存在抛异常
            if ( product == null ){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            Integer result = product.getProductStock() - cartDto.getProductQuantity();
            // 库存不足，抛异常
            if ( result < 0 ){
                throw new SellException(ResultEnum.PRODUCT_STOCK_ERROR);
            }
            product.setProductStock(result);
            productInfoRepository.save(product);
        }
    }

    /**
     * 上架
     * @param productId
     * @return
     */
    @Override
    public ProductInfo onSale(String productId) {
        ProductInfo productInfo = productInfoRepository.findOne(productId);
        if (productInfo == null){
            throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
        }

        if (productInfo.getProductStatusEnum() == ProductStatusEnum.UP){
            throw new SellException(ResultEnum.PRODUCT_STATUS_ERROR);
        }

        // 更新
        productInfo.setProductStatus(ProductStatusEnum.UP.getCode());
        return productInfoRepository.save(productInfo);
    }

    /**
     * 下架
     * @param productId
     * @return
     */
    @Override
    public ProductInfo offSale(String productId) {
        ProductInfo productInfo = productInfoRepository.findOne(productId);
        if (productInfo == null){
            throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
        }

        if (productInfo.getProductStatusEnum() == ProductStatusEnum.DOWN){
            throw new SellException(ResultEnum.PRODUCT_STATUS_ERROR);
        }

        // 更新
        productInfo.setProductStatus(ProductStatusEnum.DOWN.getCode());
        return productInfoRepository.save(productInfo);
    }
}
