package com.wrq.controller;

import com.wrq.common.ServerResponse;
import com.wrq.convert.OrderForm2OrderDtoConvert;
import com.wrq.dto.OrderDto;
import com.wrq.enums.ResultEnum;
import com.wrq.exception.SellException;
import com.wrq.form.OrderForm;
import com.wrq.service.BuyerService;
import com.wrq.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangqian on 2019/2/2.
 */
@RestController
@RequestMapping("/buyer/order")
@Slf4j
public class BuyerOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BuyerService buyerService;

    /**
     * 创建订单, 如果创建成功返回新订单的 orderId
     * @param orderForm 创建订单需要传递的参数, 需要进行表单验证. 其中 items 是购物车列表
     * @param bindingResult 数据校验
     * @return 新订单id
     */
    @PostMapping("/create")
    public ServerResponse create (@Valid OrderForm orderForm, BindingResult bindingResult){

        // 参数校验
        if ( bindingResult.hasErrors() ){
            log.error("[创建订单参数不正确], orderForm = {}", orderForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }

        // 格式转换, 前端传递过来的 orderForm 对象转换成 orderDto,便于service
        OrderDto orderDto = OrderForm2OrderDtoConvert.convert(orderForm);

        // 创建订单
        if ( CollectionUtils.isEmpty(orderDto.getOrderDetailList()) ){
            log.error("[创建订单] 购物车为空,无法创建订单");
            throw new SellException(ResultEnum.CART_EMPTY);
        }

        OrderDto createResult = orderService.create(orderDto);

        Map<String, String> map = new HashMap<>();
        map.put("orderId", createResult.getOrderId());

        return ServerResponse.createBySuccess("创建订单成功", map);
    }

    /**
     * 订单列表, 实现分页
     * @param openid 待查询列表的用户openid
     * @param page 第几页
     * @param size 每页信息条数
     * @return 订单列表, 不含订单详情
     */
    @GetMapping("/list")
    public ServerResponse list(@RequestParam("openid") String openid, @RequestParam(value = "page", defaultValue = "0") Integer page ,
                               @RequestParam(value = "size", defaultValue = "10") Integer size){
        if ( StringUtils.isEmpty(openid) ){
            log.error("[参数不正确] openid为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }

        PageRequest pageRequest = new PageRequest(page, size);

        Page<OrderDto> orderDtoPage = orderService.findList(openid, pageRequest);

        // content?
        return ServerResponse.createBySuccess("查询订单列表成功", orderDtoPage.getContent());
    }

    /**
     * 订单详情, 简单此订单属不属于当前用户
     * @param openid 请求订单详情的用户 openid
     * @param orderId 订单id
     * @return 订单详情
     */
    @GetMapping("/detail")
    public ServerResponse detail(@RequestParam("openid") String openid, @RequestParam("orderId") String orderId){
        OrderDto orderOne = buyerService.findOrderOne(openid, orderId);
        return ServerResponse.createBySuccess(orderOne);
    }

    /**
     * 取消订单: 订单主人校验
     * @param openid 请求者openid
     * @param orderId 订单id
     * @return 状态
     */
    @GetMapping("/cancel")
    public ServerResponse cancel(@RequestParam("openid") String openid, @RequestParam("orderId") String orderId){
        buyerService.cancelOrder(openid, orderId);
        return ServerResponse.createBySuccess();
    }
}
