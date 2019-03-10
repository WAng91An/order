package com.wrq.service;

import com.wrq.entity.SellerInfo;

/**
 * Created by wangqian on 2019/3/10.
 */
public interface SellerService {

    SellerInfo findSellerInfoByOpenid(String openid);
}
