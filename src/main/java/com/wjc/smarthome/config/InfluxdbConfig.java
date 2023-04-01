package com.wjc.smarthome.config;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author 王建成
 * @date 2022/5/5--12:59
 */
@Configuration
public class InfluxdbConfig {
    @Value("${spring.influx.url}")
    private String influxDBUrl;

    @Value("${spring.influx.user}")
    private String userName;

    @Value("${spring.influx.password}")
    private String password;

    @Value("${spring.influx.database}")
    private String database;

    @Bean(name = "influxdb")
    public InfluxDB influxdb(){
        InfluxDB influxDB = InfluxDBFactory.connect(influxDBUrl, userName, password);
        try{
            /**
             * 异步插入：
             * enableBatch这里第一个是point的个数，第二个是时间，单位毫秒
             * point的个数和时间是联合使用的，如果满10条或者60 * 1000毫秒 //这里为了看成果设置偏小
             * 满足任何一个条件就会发送一次写的请求。
             * 这里设置的是10个point或者1分钟刷新一次
             */
            influxDB.setDatabase(database).enableBatch(10,1000 * 60, TimeUnit.MILLISECONDS);
        }catch (Exception e){
            e.printStackTrace();
        }
//        finally {
////                设置默认策略。在influxdb中设置过就不用设置了
//            influxDB.setRetentionPolicy("2_M");
//        }
        //设置日志输出级别
        influxDB.setLogLevel(InfluxDB.LogLevel.BASIC);
        return influxDB;
    }
}
