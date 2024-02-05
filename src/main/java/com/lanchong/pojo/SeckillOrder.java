package com.lanchong.pojo;

import lombok.*;

import java.util.Date;

/**
 * @program: SeckillProject
 * @description: 秒杀订单实体类
 **/

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SeckillOrder {
    private Long id;

    private Long userId;

    private Long orderId;

    private Long goodsId;

    private Date createDate;

}