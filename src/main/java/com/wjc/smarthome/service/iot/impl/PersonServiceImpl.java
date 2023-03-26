package com.wjc.smarthome.service.iot.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.wjc.smarthome.enetity.iot.Device;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjc.smarthome.mapper.iot.PersonMapper;
import com.wjc.smarthome.enetity.iot.Person;
import com.wjc.smarthome.service.iot.PersonService;
/**
*@Author: wjc
*@Date: 2023-03-26 14:38
**/    
@Service
public class PersonServiceImpl extends ServiceImpl<PersonMapper, Person> implements PersonService{

    @Override
    public Boolean register(Person person, Device device) {
        int i = baseMapper.register(person, device);
        return i==1 ? true : false;
    }

    @Override
    public Person getByUserName(String userName) {
        return new LambdaQueryChainWrapper<>(baseMapper).eq(Person::getUsername, userName).one();
    }
}
