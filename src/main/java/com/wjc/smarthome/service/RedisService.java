package com.wjc.smarthome.service;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author 王建成
 * @date 2023/2/28--19:34
 */
@Slf4j
@Service
public class RedisService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * hash存储
     * @param hash
     * @param key
     * @param value
     */
    public void save(String hash,String key,Object value){
        Optional.ofNullable(value).ifPresent(e->redisTemplate.opsForHash().put(hash,key, JSONUtil.toJsonStr(e)));
    }

    /**
     * 字符串存储
     * @param key
     * @param value
     */
    public void save(String key,String value){
        Optional.ofNullable(value).ifPresent(e->redisTemplate.opsForValue().set(key,e));

    }

    /**
     * 字符串时间存储
     * @param key
     * @param value
     * @param time
     * @param var5
     */
    public void save(String key,String value,long time, TimeUnit var5){
        redisTemplate.opsForValue().set(key,value,time,var5);
    }

    /**
     * hash删除filed
     * @param hash
     * @param key
     */
    public void delete(String hash,String key){
        redisTemplate.opsForHash().delete(hash,key);
    }

    /**
     * 删除key
     * @param key
     */
    public void delete(String key){
        redisTemplate.delete(key);
    }

    /**
     * 字符串类型获取value值
     * @param key
     * @return
     */
    public String get(String key){
       return redisTemplate.opsForValue().get(key);
    }

    /**
     * 获取Hash类型的value值
     * @param hash
     * @param key
     * @param beanClass
     * @param <T>
     * @return
     */
    public <T> T get(String hash,String key,Class<T> beanClass){
        Object cache = redisTemplate.opsForHash().get(hash, key);

                   //如果cahe对象不为空就用JSON解析 如果cache为空就返回空
        return Optional.ofNullable(cache).map(e -> JSONUtil.toBean((String) e, beanClass)).orElse(null);

              //如果cahe对象不为空就用JSON解析
//        T t =
//        return t;
//        try {
//            return JSONUtil.toBean((String) Optional.ofNullable(cache).get(),beanClass);
//        }catch (NoSuchElementException e){
//            log.warn("redis获取{}-->{}值为空，缓存未命中！",hash,key);
//            return null;
//        }
    }

    /**
     * 获取Hsah类型List
     * @param hash
     * @param key
     * @param beanClass
     * @param <T>
     * @return
     */
    public <T> List<T> getList(String hash,String key,Class<T> beanClass){
        Object cache = redisTemplate.opsForHash().get(hash, key);
        return Optional.ofNullable(cache).map(e -> JSONUtil.toList((String) e, beanClass)).orElse(null);
    }


    public Set<Object> getFileds(String hash) {
      return  redisTemplate.opsForHash().keys(hash);
    }

    /**
     * redis发布消息
     * @param channel
     * @param message
     */
    public void sendMessage(String channel,Object message){
        redisTemplate.convertAndSend(channel,message);
    }
}
