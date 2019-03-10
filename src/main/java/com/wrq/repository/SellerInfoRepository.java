package com.wrq.repository;

import com.wrq.entity.SellerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by wangqian on 2019/3/10.
 */
public interface SellerInfoRepository extends JpaRepository<SellerInfo, String> {
    /* 根据openid查询信息 */
    SellerInfo findByOpenid(String openid);
}
