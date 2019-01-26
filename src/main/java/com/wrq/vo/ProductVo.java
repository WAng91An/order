package com.wrq.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 *
 * Created by wangqian on 2019/1/26.
 */
@Data
public class ProductVo {

    /* 返回的Json字段名是name，定义时为了具有语义。通过下面注解设置返回的字段名 */
    @JsonProperty("name")
    private String categoryName;

    /* 分类编号 */
    @JsonProperty("type")
    private Integer categoryType;

    @JsonProperty("foods")
    private List<ProductInfoVo> productInfoVoList;
}
