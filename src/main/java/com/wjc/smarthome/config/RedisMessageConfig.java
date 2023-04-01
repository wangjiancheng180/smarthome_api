package com.wjc.smarthome.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import static com.wjc.smarthome.common.RedisKeyCounts.MessageChannel.*;

/**
 * @Author: wjc
 * @Date: 2023-03-30 17:27
 **/
@Configuration
public class RedisMessageConfig {

    @Bean
    MessageListenerAdapter deviceMessageListener(RedisMessageCallBack recive){
        //TODO：这里有必要实现redis的消息队列吗？这里其实没必要用，redis消息通讯一般用于模块之间通讯
        //这里是调用recive中的方法
        return new MessageListenerAdapter(recive, DEVICE_INFO);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter deviceMessageListener){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(deviceMessageListener,new PatternTopic(DEVICE_INFO));
        return container;
    }

}
