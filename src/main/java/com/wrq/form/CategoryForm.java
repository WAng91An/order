package com.wrq.form;

import lombok.Data;

/**
 * Created by wangqian
 */
@Data
public class CategoryForm {

    private Integer categoryId;

    /** 类目名字. */
    private String categoryName;

    /** 类目编号. */
    private Integer categoryType;
}
