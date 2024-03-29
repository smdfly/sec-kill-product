package com.lanchong.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * @program: SeckillProject
 * @description: 商品详情页数据的封装
 **/
@Getter
@Setter
public class GoodsDetail {
    private int seckillStatus = 0;
    private int remainSeconds = 0;
    private GoodsVo goods ;
    private User user;
}
