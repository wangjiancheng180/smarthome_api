package com.wjc.smarthome;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@MapperScan("com.wjc.smarthome.mapper")
@EnableTransactionManagement
public class SmarthomeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmarthomeApplication.class, args);

    }

}
