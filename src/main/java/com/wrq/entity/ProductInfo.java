package com.wrq.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wrq.enums.ProductStatusEnum;
import com.wrq.utils.EnumUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 山商品实体类
 * Created by wang qian on 2019/1/26.
 */
@Entity
@Data
@DynamicUpdate
@Slf4j
public class ProductInfo {

    @Id
    private String productId;

    /* 名字 */
    private String productName;

    /* 单价 */
    private BigDecimal productPrice;

    /* 库存 */
    private Integer productStock;

    /* 描述 */
    private String productDescription;

    /* 小图 */
    private String productIcon;

    /* 状态 0：正常 1：下架 */
    private Integer productStatus = ProductStatusEnum.UP.getCode();

    /* 类目编号 */
    private Integer categoryType;

    private Date createTime;

    private Date updateTime;

    @JsonIgnore
    public ProductStatusEnum getProductStatusEnum(){
        log.info("调用了 getProductStatusEnum 方法...");
        return EnumUtil.getByCode(productStatus, ProductStatusEnum.class);
    }
}