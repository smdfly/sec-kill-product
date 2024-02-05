package com.lanchong.pojo;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: SeckillProject
 * @description: 商品实体类
 **/
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Goods {
    private Long id;

    private String goodsName;

    private String goodsTitle;

    private String goodsImg;

    private BigDecimal goodsPrice;

    private Integer goodsStock;

    private Date createDate;

    private Date updateDate;

    private String goodsDetail;
}