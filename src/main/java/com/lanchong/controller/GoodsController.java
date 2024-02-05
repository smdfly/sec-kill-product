package com.lanchong.controller;

import com.lanchong.pojo.GoodsDetail;
import com.lanchong.pojo.GoodsVo;
import com.lanchong.pojo.User;
import com.lanchong.redis.GoodsKey;
import com.lanchong.redis.RedisService;
import com.lanchong.result.Result;
import com.lanchong.service.GoodsService;
import com.lanchong.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @program: SeckillProject
 * @description: 商品表现层
 **/
@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private GoodsService goodsService;

    /**
     * 前端渲染
     */
    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    ApplicationContext applicationContext;

    /**
     * 商品列表:
     * 无缓存：QPS  1395
     * 页面缓存后 ： QPS  1972
     * @param model
     * @param request，response
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public String list(Model model, HttpServletRequest request, HttpServletResponse response) {
        //询秒杀商品列表,无缓存
        /*List<GoodsVo> goodsVos = goodsService.listGoodVo();
        model.addAttribute("goodsVos",goodsVos);
        return "goods_list";*/

        //为了应对在SpringBoot中的高并发及优化访问速度，我们一般会把页面上的数据查询出来，然后放到redis中进行缓存。减少数据库的压力。
        //改进，对页面进行缓存
        //取缓存
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if(!StringUtils.isEmpty(html)) {
            return html;
        }
        //获取数据绑定到model
        List<GoodsVo> goodsVos = goodsService.listGoodVo();
        model.addAttribute("goodsVos", goodsVos);
        WebContext ctx = new WebContext(request,response,request.getServletContext(),request.getLocale(), model.asMap());

        //手动渲染
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        if(!StringUtils.isEmpty(html)) {
            /*
             * 键：GoodsKey.getGoodsList
             * 值：html
             * 过期时间：60秒
             */
            redisService.set(GoodsKey.getGoodsList, "", html , 60);
        }
        return html;
    }

    /**
     * 商品详情,并对页面进行redis缓存
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping("/detail/{goodsId}")
    @ResponseBody
    public String goodsDetail(HttpServletRequest request, HttpServletResponse response,Model model, User user,@PathVariable("goodsId")long goodsId) {
        model.addAttribute("user", user);

        //取缓存
        String html = redisService.get(GoodsKey.getGoodsDetail, ""+goodsId, String.class);
        if(!StringUtils.isEmpty(html)) {
            return html;
        }

        //根据id查询秒杀商品
        GoodsVo goods = goodsService.getGoodsVoById(goodsId);
        model.addAttribute("goods",goods);

        //获取开始、介绍、现在的时间,转换为毫秒
        long start = goods.getStartDate().getTime();
        long end = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        //秒杀状态、倒计时
        int seckillStatus = 0;
        int reSeconds = 0;

        if(now < start) { //秒杀未开始，倒计时
            seckillStatus = 0;
            reSeconds = (int)((start-now)/1000);
        } else if(now > end) {  //秒杀已结束
            seckillStatus = 2;
            reSeconds = -1;
        } else {   //秒杀进行中
            seckillStatus = 1;
            reSeconds = 0;
        }
        model.addAttribute("reSeconds",reSeconds);
        model.addAttribute("seckillStatus",seckillStatus);

        WebContext ctx = new WebContext(request,response,request.getServletContext(),request.getLocale(), model.asMap());

        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
        if(!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsDetail, ""+goodsId, html , 60);
        }
        return html;
    }

    /**
     * 商品详情，对页面进行静态化处理
     * @param goodsId
     * @param user
     * @return
     */
    @RequestMapping("/detailStatic/{goodsId}")
    @ResponseBody
    public Result<GoodsDetail> detailStatic(@PathVariable("goodsId")long goodsId , User user ) {
        //根据id查询秒杀商品
        GoodsVo goods = goodsService.getGoodsVoById(goodsId);
        //获取开始、介绍、现在的时间,转换为毫秒
        long start = goods.getStartDate().getTime();
        long end = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        //秒杀状态、倒计时
        int seckillStatus = 0;
        int reSeconds = 0;

        if(now < start) { //秒杀未开始，倒计时
            seckillStatus = 0;
            reSeconds = (int)((start-now)/1000);
        } else if(now > end) {  //秒杀已结束
            seckillStatus = 2;
            reSeconds = -1;
        } else {   //秒杀进行中
            seckillStatus = 1;
            reSeconds = 0;
        }
        GoodsDetail detail = new GoodsDetail();
        detail.setGoods(goods);
        detail.setUser(user);
        detail.setRemainSeconds(reSeconds);
        detail.setSeckillStatus(seckillStatus);

        return Result.success(detail);
    }
}
