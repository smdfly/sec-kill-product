package com.lanchong.service.impl;

import com.lanchong.dao.OrderMapper;
import com.lanchong.pojo.GoodsVo;
import com.lanchong.pojo.OrderInfo;
import com.lanchong.pojo.SeckillOrder;
import com.lanchong.pojo.User;
import com.lanchong.redis.OrderKey;
import com.lanchong.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lanchong.redis.RedisService;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @program: SeckillProject
 * @description: 订单服务类
 **/
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RedisService redisService;

    /**
     * 根据用户id和商品id查询订单，从redis缓存获取
     * @param userId
     * @param goodsId
     * @return
     */
    public SeckillOrder getOrderByUserIdGoodsId(int userId, long goodsId) {
        //先访问缓存
        SeckillOrder seckillOrder =  redisService.get(OrderKey.getOrderByUidOid,""+userId + "_"+goodsId,SeckillOrder.class);
        if(seckillOrder == null) {
            //缓存不存在，再访问数据库
            return orderMapper.getOrderByUserIdGoodsId(userId,goodsId);
        }
        return seckillOrder;
    }

    /**
     * 生成订单
     * @param user
     * @param goods
     * @return
     */
    @Transactional
    public OrderInfo createOrder(User user, GoodsVo goods) {
        Date d = new Date();
        /*
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNowStr = sdf.format(d);
        */

        //创建orderInfo
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getSeckillPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId((long)user.getId());
        orderInfo.setCreateDate(d);
        orderMapper.insert(orderInfo);

        //创建seckillOrder
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(goods.getId());
        seckillOrder.setOrderId(orderInfo.getId());
        seckillOrder.setUserId((long)user.getId());
        seckillOrder.setCreateDate(d);
        orderMapper.insertSeckillOrder(seckillOrder);

        //把订单放入缓存,过期时间1小时
        redisService.set(OrderKey.getOrderByUidOid,""+user.getId() + "_"+goods.getId(),seckillOrder,3600);
        return orderInfo;
    }

    /**
     * 根据订单id获取订单
     * @param orderId
     * @return
     */
    public OrderInfo getByOrderId(long orderId) {
        return orderMapper.getOrderById(orderId);
    }
}
