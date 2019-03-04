package com.wrq.service.impl;

import com.wrq.convert.OrderMaster2OrderDtoConvert;
import com.wrq.dto.CartDto;
import com.wrq.dto.OrderDto;
import com.wrq.entity.OrderDetail;
import com.wrq.entity.OrderMaster;
import com.wrq.entity.ProductInfo;
import com.wrq.enums.OrderStatusEnum;
import com.wrq.enums.PayStatusEnum;
import com.wrq.enums.ResultEnum;
import com.wrq.exception.SellException;
import com.wrq.repository.OrderDetailRepository;
import com.wrq.repository.OrderMasterRepository;
import com.wrq.service.OrderService;
import com.wrq.service.PayService;
import com.wrq.service.ProductService;
import com.wrq.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by wangqian on 2019/2/1.
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Autowired
    private PayService payService;

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
        orderDto.setOrderId(orderId);
        BeanUtils.copyProperties(orderDto, orderMaster);
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

    /**
     * 查询订单
     * @param orderId
     * @return
     */
    @Override
    public OrderDto findOne(String orderId) {
        OrderMaster orderMaster = orderMasterRepository.findOne(orderId);
        if ( orderMaster == null ){
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        if ( CollectionUtils.isEmpty(orderDetailList) ) {
            throw new SellException(ResultEnum.ORDER_DETAIL_NOT_EXIST);
        }

        OrderDto orderDto = new OrderDto();
        BeanUtils.copyProperties(orderMaster, orderDto);
        orderDto.setOrderDetailList(orderDetailList);
        return orderDto;
    }

    /**
     * 查询订单列表，实现分页
     * @param openid openid下面的订单描述,不包括详情
     * @param pageable pageable对象
     * @return 查询后订单列表的Page对象
     */
    @Override
    public Page<OrderDto> findList(String openid, Pageable pageable) {
        Page<OrderMaster> orderMasterPage = orderMasterRepository.findByBuyerOpenid(openid, pageable);
        List<OrderMaster> content = orderMasterPage.getContent();

        List<OrderDto> orderDtoList = OrderMaster2OrderDtoConvert.convert(content);

        // 订单 list 不需要订单详情

        Page<OrderDto> orderDtoPage = new PageImpl<OrderDto>(orderDtoList, pageable, orderMasterPage.getTotalElements());

        // 获得的是 Page<OrderMaster>：如何 Page<OrderMaster> -> Page<OrderDto>
        // Page<OrderDto> orderDtoPage = new PageImpl<OrderDto>(orderDtoList, pageable, orderMasterPage.getTotalElements());

        return orderDtoPage;
    }

    /**
     * 取消订单: 只有新订单才可以被取消,取消订单和完结订单状态都不可以取消
     * @param orderDto 被取消的订单：orderDto
     * @return 取消后返回 orderDto
     */
    @Override
    @Transactional
    public OrderDto cancel(OrderDto orderDto) {

        // 判断订单状态
        if ( !orderDto.getOrderStatus().equals(OrderStatusEnum.NEW.getCode()) ) {
            log.info("[取消订单] 修改订单状态错误, orderId:{}, orderStatus", orderDto.getOrderId(), orderDto.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        // 修改订单状态
        OrderMaster orderMaster = new OrderMaster();
        orderDto.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderDto, orderMaster);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if( updateResult == null){
            log.info("[取消订单] 更新订单状态失败, orderMaster = {}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        // 修改库存
        List<OrderDetail> orderDetailList = orderDto.getOrderDetailList();
        if ( CollectionUtils.isEmpty(orderDetailList) ){
            log.info("[取消订单] 订单中无商品详情, orderDto = {}", orderDto);
            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
        }
        // 提取 orderDto.getOrderDetailList() 中的 ProductId 和 ProductQuantity 来生成 CartDto 放在list中
        List<CartDto> cartDtoList = orderDto.getOrderDetailList().stream().map(e -> new CartDto(e.getProductId(), e.getProductQuantity())).collect(Collectors.toList());
        productService.increaseStock(cartDtoList);

        // 如果已经支付，退款
        if ( orderDto.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode()) ){
            payService.refund(orderDto);
        }
        return orderDto;
    }

    /**
     * 订单完结: 新订单才能修改其订单状态为FINISH
     * @param orderDto 待完结订单的 orderDto 对象
     * @return 完结订单的 orderDto 对象
     */
    @Override
    @Transactional
    public OrderDto finish(OrderDto orderDto) {

        // 判断状态

        if ( !orderDto.getOrderStatus().equals(OrderStatusEnum.NEW.getCode()) ) {
            log.info("[订单完结] 订单状态错误, orderId = {}, orderStatus", orderDto.getOrderId(), orderDto.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        // 修改状态码

        OrderMaster orderMaster = new OrderMaster();
        orderDto.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        BeanUtils.copyProperties(orderDto, orderMaster);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if( updateResult == null){
            log.info("[订单完结] 更新订单状态失败, orderMaster = {}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        return orderDto;
    }

    /**
     * 订单支付: 新订单并且支付状态为 WAIT 才可以修改为 SUCCESS
     * @param orderDto 带支付订单的 orderDto 对象
     * @return 修改后支付状态的 orderDto 对象
     */
    @Override
    @Transactional
    public OrderDto paid(OrderDto orderDto) {

        // 判断订单状态:只有新订单才可以修改支付状态成PAID

        if ( !orderDto.getOrderStatus().equals(OrderStatusEnum.NEW.getCode()) ) {
            log.info("[订单支付] 订单状态错误, orderId = {}, orderStatus", orderDto.getOrderId(), orderDto.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        // 判断支付状态

        if ( !orderDto.getPayStatus().equals(PayStatusEnum.WAIT.getCode()) )  {
            log.info("[订单支付] 支付状态错误, orderId = {}, payStatus", orderDto.getOrderId(), orderDto.getPayStatus());
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }

        // 修改支付状态

        OrderMaster orderMaster = new OrderMaster();
        orderDto.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        BeanUtils.copyProperties(orderDto, orderMaster);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if( updateResult == null){
            log.info("[订单支付] 更新支付状态失败, orderMaster = {}", orderMaster);
            throw new SellException(ResultEnum.PAY_UPDATE_FAIL);
        }

        return orderDto;
    }
}
