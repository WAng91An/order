package com.wrq.service.impl;

import com.wrq.dto.CartDto;
import com.wrq.dto.OrderDto;
import com.wrq.entity.OrderDetail;
import com.wrq.entity.OrderMaster;
import com.wrq.entity.ProductInfo;
import com.wrq.enums.ResultEnum;
import com.wrq.exception.SellException;
import com.wrq.repository.OrderDetailRepository;
import com.wrq.repository.OrderMasterRepository;
import com.wrq.service.OrderService;
import com.wrq.service.ProductService;
import com.wrq.utils.KeyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by wangqian on 2019/2/1.
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    /**
     * 创建订单
     * @param orderDto 前端传递过来订单的信息
     * @return 判断有没有商品后确认订单
     * api：
     *
     * name: "张三"
     * phone: "18868822111"
     * address: "慕课网总部"
     * openid: "ew3euwhd7sjw9diwkq" //用户的微信openid
     * items: [{
     *  productId: "1423113435324",
     *  productQuantity: 2 //购买数量
     *  }]
     *
     */
    @Override
    @Transactional
    public OrderDto create(OrderDto orderDto) {

        // 声明总价，后面使用
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);

        // 订单id，买了鸭腿和粥。鸭腿和粥属于一个订单，但是鸭腿有鸭腿的详情，粥有粥的详情。
        String orderId = KeyUtil.genUniqueKey();

        // 1. 查询商品（单价、库存）
        List<OrderDetail> orderDetailList = orderDto.getOrderDetailList();
        for (OrderDetail orderDetail : orderDetailList){
            String productId = orderDetail.getProductId();
            ProductInfo productInfo = productService.findOne(productId);
            if ( productInfo == null ){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            // 2. 计算订单总价
            orderAmount = productInfo.getProductPrice().multiply(new BigDecimal(orderDetail.getProductQuantity())).add(orderAmount);

            // 3. 写入订单数据库 OrderDetail -> 前端参数：用户名、用户ID、用户openid、购买商品id和数量

            // 一个订单不同产品有不同的详情id
            orderDetail.setDetailId(KeyUtil.genUniqueKey());

            // 一个订单产品再多它们也是同一个订单
            orderDetail.setOrderId(orderId);

            // 前端只传递过来商品的id和数量，detail中其他字段我们自己写
            BeanUtils.copyProperties(productInfo, orderDetail);
            orderDetailRepository.save(orderDetail);

        }

        // 4. 写入订单数据库 OrderMaster
        OrderMaster orderMaster = new OrderMaster();
        // 拷贝的时候 null 值也会被拷贝，所以先拷贝再set。注意拷贝的时候产生的Bug，可以打断点试试
        BeanUtils.copyProperties(orderDto, orderMaster);
        orderMaster.setOrderId(orderId);
        orderMaster.setOrderAmount(orderAmount);
        // orderMaster创建的时候会默认payStatus等为0，拷贝后会 0 -> null，但是我这里没有，如出现异常尝试打断点调试。

        orderMasterRepository.save(orderMaster);

        // 5. 修改库存。
        // 只获取 orderDetailList 中的productId和productQuantity的两个字段放到CartDto中，方便执行减库。
        List<CartDto> cartDtoList =  orderDetailList.stream().map(e -> new CartDto(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());

        // 6. 根据上面获得的CartDto函数，执行减库存方法。
        productService.decreaseStock(cartDtoList);

        return orderDto;
    }

    @Override
    public OrderDto findOne(String orderId) {
        return null;
    }

    @Override
    public Page<OrderDto> findList(String openid, Pageable pageable) {
        return null;
    }

    @Override
    public OrderDto cancel(OrderDto orderDto) {
        return null;
    }

    @Override
    public OrderDto finish(OrderDto orderDto) {
        return null;
    }

    @Override
    public OrderDto paid(OrderDto orderDto) {
        return null;
    }
}
