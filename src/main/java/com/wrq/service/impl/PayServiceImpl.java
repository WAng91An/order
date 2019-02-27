package com.wrq.service.impl;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundRequest;
import com.lly835.bestpay.model.RefundResponse;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import com.lly835.bestpay.utils.JsonUtil;
import com.wrq.dto.OrderDto;
import com.wrq.enums.ResultEnum;
import com.wrq.exception.SellException;
import com.wrq.service.OrderService;
import com.wrq.service.PayService;
import com.wrq.utils.MathUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by wangqian on 2019/2/15.
 */
@Service
@Slf4j
public class PayServiceImpl implements PayService {

    @Autowired
    private BestPayServiceImpl bestPayService;

    @Autowired
    private OrderService orderService;


    private static String ORDER_NAME = "订单名称";

    @Override
    public PayResponse create(OrderDto orderDto) {

        PayRequest payRequest = new PayRequest();
        payRequest.setOpenid(orderDto.getBuyerOpenid());
        payRequest.setOrderAmount(orderDto.getOrderAmount().doubleValue());
        payRequest.setOrderId(orderDto.getOrderId());
        payRequest.setOrderName(ORDER_NAME);
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);

        log.info("[微信支付] 发起支付，request = {}", JsonUtil.toJson(payRequest));

        // 调用统一下单 api
        PayResponse response = bestPayService.pay(payRequest);

        log.info("[微信支付] 发起支付，response = {}", JsonUtil.toJson(response));

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

    @Override
    public PayResponse notify(String notifyData) {
        // 1. 验证签名
        // 2. 支付状态
        // 3. 支付金额
        // 4. 支付人（下单人 == 支付人）

        // SDK帮我们完成了 1 和 2
        PayResponse payResponse = bestPayService.asyncNotify(notifyData);

        log.info("[微信支付] 异步处理，payResponse = {}", JsonUtil.toJson(payResponse));

        // 查询订单
        OrderDto orderDto = orderService.findOne(payResponse.getOrderId());

        // 判断订单是否存在
        if (orderDto == null ){
            log.error("[微信支付] 异步处理，订单不存在");
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        // 判断金额是否一致：金额的差值小于 0.01 ，不可以使用 compareTo 方法，因为 double 转成 BigDecimal 会不精确，而且0.1和0.10无法比较
        if (!MathUtil.equals(orderDto.getOrderAmount().doubleValue(), payResponse.getOrderAmount())){
            log.error("[微信支付] 异步处理，订单金额不一致，orderId={}, 系统金额={}， 微信通知金额={}", orderDto.getOrderId(), orderDto.getOrderAmount(), payResponse.getOrderAmount());
            throw new SellException(ResultEnum.WXPAY_NOTIFY_MONEY_VERIFY_ERROR);
        }

        // 修改订单支付状态
        orderService.paid(orderDto);

        return payResponse;
    }

    @Override
    public RefundResponse refund(OrderDto orderDto) {

        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        refundRequest.setOrderId(orderDto.getOrderId());
        refundRequest.setOrderAmount(orderDto.getOrderAmount().doubleValue());

        log.info("[微信退款] refundRequest = {}", refundRequest);

        RefundResponse response = bestPayService.refund(refundRequest);

        log.info("[微信退款] response = {}", refundRequest);

        return response;
    }
}
