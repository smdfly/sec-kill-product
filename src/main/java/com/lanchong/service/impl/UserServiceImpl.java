package com.lanchong.service.impl;

import com.lanchong.dao.UserMapper;
import com.lanchong.exception.GlobalException;
import com.lanchong.pojo.LoginParam;
import com.lanchong.pojo.User;
import com.lanchong.redis.UserKey;
import com.lanchong.result.CodeMsg;
import com.lanchong.service.UserService;
import com.lanchong.utils.MD5Util;
import com.lanchong.utils.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lanchong.redis.RedisService;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: SeckillProject
 * @description: 用户服务层
 **/
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisService redisService;

    /**
     * 用户登录处理
     * @param loginParam
     * @return
     */
    public String login(HttpServletResponse response,LoginParam loginParam) {
        User user = userMapper.checkPhone(loginParam.getMobile());
        if(user == null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        String dbPwd= user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(loginParam.getPassword(), saltDB);
        if(!StringUtils.equals(dbPwd , calcPass)){
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        user.setPassword(StringUtils.EMPTY);

        //生成cookie
        String token = UUIDUtil.uuid();
        addCookie(response, token, user);
        return token;
    }

    /**
     * 获取token
     * @param response
     * @param token
     * @return
     */
    public User getByToken(HttpServletResponse response, String token) {
        if(StringUtils.isEmpty(token)) {
            return null;
        }
        User user = redisService.get(UserKey.token, token, User.class);
        //延长有效期
        if(user != null) {
            addCookie(response, token, user);
        }
        return user;
    }

    /**
     * 存入cookie
     * @param response
     * @param token
     * @param user
     */
    private void addCookie(HttpServletResponse response, String token, User user) {
        redisService.set(UserKey.token, token, user,UserKey.TOKEN_EXPIRE);
        Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
        cookie.setMaxAge(UserKey.TOKEN_EXPIRE);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
