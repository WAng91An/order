package com.wrq.service.impl;

import com.wrq.entity.SellerInfo;
import com.wrq.repository.SellerInfoRepository;
import com.wrq.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wangqian on 2019/3/10.
 */
@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerInfoRepository sellerInfoRepository;

    @Override
    public SellerInfo findSellerInfoByOpenid(String openid) {
        SellerInfo sellerInfo = sellerInfoRepository.findByOpenid(openid);
        return sellerInfo;
    }
}
