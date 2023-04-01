package com.wjc.smarthome;

import com.wjc.smarthome.dto.iot.DataRedisDto;
import com.wjc.smarthome.service.iot.InfluxService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SmarthomeApplicationTests {

    @Autowired
    private InfluxService influxService;

    @Test
    void contextLoads() {
        List<DataRedisDto> data = influxService.findDeviceData("SMART_HOME_COORDINATOR_30");
        System.out.println(data);
    }

}
