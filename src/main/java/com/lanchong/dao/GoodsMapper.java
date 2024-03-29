package com.lanchong.dao;

import com.lanchong.pojo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface GoodsMapper {
    /**
     * 查询所有商品
     * @return
     */
    @Select("select g.*,sg.stock_count,sg.start_date,sg.end_date,sg.seckill_price " +
            " from seckill_goods sg left join goods g on sg.goods_id = g.id")
    List<GoodsVo> listGoodsVo();

    /**
     * 根据商品goodsId查询商品信息
     * @param goodsId
     * @return
     */
    @Select("select g.*,sg.stock_count,sg.start_date,sg.end_date,sg.seckill_price " +
            " from seckill_goods sg left join goods g on sg.goods_id = g.id where g.id = #{goodsId}")
    GoodsVo getGoodsVoById(@Param("goodsId") long goodsId);

    /**
     * 减少商品的库存
     * @param goodsId
     */
    @Update("update seckill_goods set stock_count = stock_count-1 where goods_id = #{goodsId} and stock_count > 0")
    int reduceStock(@Param("goodsId")long goodsId);
}
