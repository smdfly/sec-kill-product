package com.lanchong.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: SeckillProject
 * @description: 商品与秒杀商品信息数据读取的封装
 **/

@Data
@ToString
public class GoodsVo extends Goods {
    private Integer stockCount;

    private Date startDate;

    private Date endDate;

    private BigDecimal seckillPrice;
}
