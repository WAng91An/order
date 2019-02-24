package com.wrq.service.impl;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import com.lly835.bestpay.utils.JsonUtil;
import com.wrq.dto.OrderDto;
import com.wrq.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * Created by wangqian on 2019/2/15.
 */
@Service
@Slf4j
public class PayServiceImpl implements PayService {

    @Autowired
    private BestPayServiceImpl bestPayService;


    private static String ORDER_NAME = "订单名称";

    @Override
    public PayResponse create(OrderDto orderDto) {

        PayRequest payRequest = new PayRequest();
        payRequest.setOpenid(orderDto.getBuyerOpenid());
        payRequest.setOrderAmount(orderDto.getOrderAmount().doubleValue());
        payRequest.setOrderId(orderDto.getOrderId());
        payRequest.setOrderName(ORDER_NAME);
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);

        log.info("[微信支付] request = {}", JsonUtil.toJson(payRequest));

        // 调用统一下单 api
        PayResponse response = bestPayService.pay(payRequest);

        log.info("[微信支付] response = {}", JsonUtil.toJson(response));

        return response;
        /**
         * response = {
         * "appId":"wxd898fcb01713c658",     //公众号名称，由商户传入
         * "timeStamp":"1499569906",         //时间戳，自1970年以来的秒数
         * "nonceStr":"bVsQpcfsKUAzO8r0", //随机串
         * "package":"prepay_id=wx2017070911112036b51eaddc0529394957",
         * "signType":"MD5",         //微信签名方式：
         * "paySign":"78CA85306AB823156E1032EFB5BB1C76" //微信签名
         }
         把的到的Response 写入到 pay.html中js。
         */
    }
}
