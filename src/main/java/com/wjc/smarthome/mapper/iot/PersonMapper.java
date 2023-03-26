package com.wjc.smarthome.mapper.iot;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wjc.smarthome.enetity.iot.Device;
import com.wjc.smarthome.enetity.iot.Person;
import org.apache.ibatis.annotations.Param;

/**
*@Author: wjc
*@Date: 2023-03-26 14:38
**/    
public interface PersonMapper extends BaseMapper<Person> {

    /**
     * 人员和设备绑定
     * @param person
     * @param device
     * @return
     */
    int register(@Param("person") Person person, @Param("device") Device device);
}