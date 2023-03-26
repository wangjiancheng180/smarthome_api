package com.wjc.smarthome.service.iot;

import com.wjc.smarthome.enetity.iot.Device;
import com.wjc.smarthome.enetity.iot.Person;
import com.baomidou.mybatisplus.extension.service.IService;
    /**
*@Author: wjc
*@Date: 2023-03-26 14:38
**/    
public interface PersonService extends IService<Person>{


        Boolean register(Person person, Device device);

        Person getByUserName(String userName);
    }
