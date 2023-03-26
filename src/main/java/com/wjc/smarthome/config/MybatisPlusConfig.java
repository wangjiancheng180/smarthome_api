package com.wjc.smarthome.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: wjc
 * @Date: 2023-03-02 23:30
 **/
@Configuration
public class MybatisPlusConfig {


    @Bean
    @ConditionalOnMissingBean(MetaObjectHandler.class)
    public MetaObjectHandler metaObjectHandler(){
        return new CreateAndUpdateMetaHandler();
    }
    /**
     * 分页插件
     * @return
     */
    @Bean
    public MybatisPlusInterceptor pageInterceptor (){
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return mybatisPlusInterceptor;
    }
}
