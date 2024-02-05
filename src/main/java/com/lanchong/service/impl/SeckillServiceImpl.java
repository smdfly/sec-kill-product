package com.lanchong.service.impl;

import com.lanchong.pojo.GoodsVo;
import com.lanchong.pojo.OrderInfo;
import com.lanchong.pojo.SeckillOrder;
import com.lanchong.pojo.User;
import com.lanchong.redis.SeckillKey;
import com.lanchong.service.GoodsService;
import com.lanchong.service.OrderService;
import com.lanchong.service.SeckillService;
import com.lanchong.utils.MD5Util;
import com.lanchong.utils.UUIDUtil;
import com.lanchong.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: SeckillProject
 * @description: 秒杀服务
 **/
@Service
public class SeckillServiceImpl implements SeckillService {
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisService redisService;

    /**
     * 秒杀业务：减库存下订单，写入秒杀订单
     * @param user
     * @param goods
     * @return
     */
    @Transactional
    public OrderInfo seckill(User user, GoodsVo goods) {
        //减库存
        int flag = goodsService.reduceStock(goods);
        if(flag == 1)
            //创建订单:order_info  seckill_order
            return orderService.createOrder(user,goods);
        else {
            //已无库存
            setGoodsOver(goods.getId());
            return null;
        }
    }

    /**
     * 轮询查询是否下单成功
     * @param id
     * @param goodsId
     * @return
     */
    public long getSeckillResult(int id, long goodsId) {
        SeckillOrder order = orderService.getOrderByUserIdGoodsId(id, goodsId);
        if(order != null) {
            return order.getOrderId();
        } else {
            //查看秒杀商品是否已经结束
            boolean isOver = getGoodsOver(goodsId);
            if(isOver) {
                return -1;
            }else {
                return 0;
            }
        }
    }

    /*
     * 秒杀商品结束标记
     * */
    private void setGoodsOver(Long goodsId) {
        redisService.set(SeckillKey.isGoodsOver, ""+goodsId, true , 3600);
    }
    /*
     * 查看秒杀商品是否已经结束
     * */
    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(SeckillKey.isGoodsOver, ""+goodsId);
    }


    /**
     * 秒杀地址生成
     * @param user
     * @param goodsId
     * @return
     */
    public String createPath(User user, long goodsId) {
        String str = MD5Util.md5(UUIDUtil.uuid() + "987655");
        redisService.set(SeckillKey.getSeckillPath,""+user.getId()+"_"+goodsId,str,60);
        return str;
    }

    /**
     * 秒杀地址验证
     * @param user
     * @param goodsId
     * @param path
     * @return
     */
    public boolean checkPath(User user, long goodsId, String path) {
        if(user == null || path == null) {
            return false;
        }
        String pathOld = redisService.get(SeckillKey.getSeckillPath, ""+user.getId() + "_"+ goodsId, String.class);
        return path.equals(pathOld);
    }

}
