package com.lanchong.pojo;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: SeckillProject
 * @description: 秒杀商品实体类
 **/

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SeckillGoods {
    private Long id;

    private Long goodsId;

    private BigDecimal seckilPrice;

    private Integer stockCount;

    private Date startDate;

    private Date endDate;

}