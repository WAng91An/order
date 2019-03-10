package com.wrq.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by wangqian on 2019/1/18.
 */
@Entity
@DynamicUpdate
@Data // lombok的注解，使用它我们就不用使用getter、setter和toString注解
public class ProductCategory {

    /**
     * 标记为主键，且ID自增。
     */
    @Id
    @GeneratedValue
    /* 分类id */
    private Integer categoryId;

    /* 分类名 */
    private  String categoryName;

    /* 分类编号 */
    private Integer categoryType;

    private Date createTime;

    private Date updateTime;

}
