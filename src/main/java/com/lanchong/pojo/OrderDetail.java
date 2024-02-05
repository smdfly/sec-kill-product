package com.lanchong.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * @program: SeckillProject
 * @description: 秒杀订单数据封装
 **/
@Getter
@Setter
public class OrderDetail {
    private GoodsVo goods;
    private OrderInfo order;
}
