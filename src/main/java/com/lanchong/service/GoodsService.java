package com.lanchong.service;

import com.lanchong.pojo.GoodsVo;

import java.util.List;

public interface GoodsService {

    /**
     * 查询秒杀商品列表
     * @return
     */
     List<GoodsVo> listGoodVo();

    /**
     * 根据id获取秒杀商品信息
     * @param goodsId
     * @return
     */
     GoodsVo getGoodsVoById(long goodsId);

    /**
     * 减少库存
     * @param goods
     */
     int reduceStock(GoodsVo goods);
}
