package com.wrq.convert;

import com.wrq.dto.OrderDto;
import com.wrq.entity.OrderMaster;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by wangqian on 2019/2/4.
 */
public class OrderMaster2OrderDtoConvert {

    public static OrderDto convert (OrderMaster orderMaster){
        OrderDto orderDto = new OrderDto();
        BeanUtils.copyProperties(orderMaster, orderDto);
        return orderDto;
    }

    public static List<OrderDto> convert(List<OrderMaster> orderMasterList){
       return orderMasterList.stream().map(e -> convert(e)).collect(Collectors.toList());
    }

}
