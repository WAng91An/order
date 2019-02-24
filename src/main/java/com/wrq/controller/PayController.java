package com.wrq.controller;

import com.lly835.bestpay.model.PayResponse;
import com.wrq.dto.OrderDto;
import com.wrq.enums.ResultEnum;
import com.wrq.exception.SellException;
import com.wrq.service.OrderService;
import com.wrq.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * Created by wangqian on 2019/2/15.
 */
@Controller
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PayService payService;

    @GetMapping("/create")
    public ModelAndView pay (@RequestParam("orderId") String orderId, @RequestParam("returnUrl") String returnUrl, Map<String, Object> map){

        /**
         * 在微信环境请求 /pay/create?orderId=xxxxx&returnUrl=xxxxx  接口
         * 1. 根据订单号查询订单
         * 2. 根据查到的订单发起支付
         * 3. 发起支付调用 pay方法会获取到 预支付信息response
         * 4. 携带预支付信息 response 跳转到 /pay/create.html
         * 5. 在微信环境就可跳转微信支付页面
         */
        // 查订单
        OrderDto orderDto = orderService.findOne(orderId);
        if (orderDto == null){
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        // 发起支付
        PayResponse payResponse = payService.create(orderDto);

        map.put("payResponse", payResponse);
        map.put("returnUrl", returnUrl);

        return new ModelAndView("/pay/create");
    }

}
