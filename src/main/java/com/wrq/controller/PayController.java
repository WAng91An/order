package com.wrq.controller;

import com.lly835.bestpay.model.PayResponse;
import com.wrq.dto.OrderDto;
import com.wrq.enums.ResultEnum;
import com.wrq.exception.SellException;
import com.wrq.service.OrderService;
import com.wrq.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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

        return new ModelAndView("pay/create", map);
    }

    /**
     * 微信异步通知
     * @param notifyData
     */
    @PostMapping("/notify")
    public ModelAndView notify(@RequestBody String notifyData){
        /**
         * 在微信环境请求 /pay/create?orderId=xxxxx&returnUrl=xxxxx  接口
         * 1. 根据订单号查询订单
         * 2. 根据查到的订单发起支付
         * 3. 发起支付调用 pay方法会获取到 预支付信息 response
         * 4. 携带预支付信息 response 自动跳转到 /pay/create.html
         * 5. create.html中的 Js 在微信环境就可跳转微信支付页面
         * 6. 支付成功后会请求 /notify (此url在支付时就当作配置参数传递进去了)
         * 7. 处理 /notify 的方法会接受到一个 XML 的字符串notifyData
         * 8. notifyData中就有流水号等消息
         */
        payService.notify(notifyData);

        // 当处理完毕异步通知，跳转到success.ftl,把 xml 展示给微信。这样微信才不会一直给我们发送异步
        return new ModelAndView("pay/success");
    }

}
