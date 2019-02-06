package com.wrq.convert;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wrq.dto.OrderDto;
import com.wrq.entity.OrderDetail;
import com.wrq.enums.ResultEnum;
import com.wrq.exception.SellException;
import com.wrq.form.OrderForm;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangqian on 2019/2/5.
 */
@Slf4j
public class OrderForm2OrderDtoConvert {

    /**
     * orderForm是前端传递过来的 Json 数据,转成 OrderDto 数据.
     * @param orderForm
     * @return
     */
    public static OrderDto convert (OrderForm orderForm){

        Gson gson = new Gson();

        OrderDto orderDto = new OrderDto();

        orderDto.setBuyerName(orderForm.getName());
        orderDto.setBuyerPhone(orderForm.getPhone());
        orderDto.setBuyerAddress(orderForm.getAddress());
        orderDto.setBuyerOpenid(orderForm.getOpenid());

        List<OrderDetail> orderDetailList = new ArrayList<>();

        // 前端的传递过来的 Json 数组, 转化成list 1. 获得type 2. 转化
        Type type = new TypeToken<List<OrderDetail>>(){}.getType();

        try{
            orderDetailList = gson.fromJson(orderForm.getItems(), type);
        } catch (Exception e){
            log.info("[数据转换] 数据转换失败, String = {}", orderForm.getItems());
            throw new SellException(ResultEnum.PARAM_ERROR);
        }

        orderDto.setOrderDetailList(orderDetailList);

        return orderDto;
    }

}
