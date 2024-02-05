package com.lanchong.rabbitmq;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @program: SeckillProject
 * @description: 消息队列发送接收测试
 **/
public class RabbitMqTest {
    @Autowired
    private MQSender sender;

    /**
     * 测试简单发送字符串
     */
    @Test
    public void sendMsg() {
        sender.sendStr("string send test...");
    }
}
