package com.wjc.smarthome.service.min.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.wjc.smarthome.common.BusinessContent;
import com.wjc.smarthome.common.JsonResult;
import com.wjc.smarthome.enetity.iot.Device;
import com.wjc.smarthome.enetity.iot.Person;
import com.wjc.smarthome.mapper.iot.DeviceMapper;
import com.wjc.smarthome.param.min.RegisterInfoBean;
import com.wjc.smarthome.service.iot.DeviceService;
import com.wjc.smarthome.service.iot.PersonService;
import com.wjc.smarthome.service.min.MinProgramService;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @Author: wjc
 * @Date: 2023-03-26 14:00
 **/
@Service
@RequiredArgsConstructor
public class MinProgramServiceImpl implements MinProgramService {

    private final DeviceMapper deviceMapper;

    private final PersonService personService;


    @Value("${security.salt}")
    private String salt;

    @Override
    public JsonResult<Boolean> register(RegisterInfoBean bean) {
        Device device = new LambdaQueryChainWrapper<>(deviceMapper)
                .eq(Device::getDeviceName, BusinessContent.DEVICE_NAME_PREFIX + bean.getDeviceName())
                .one();
        if (device==null) {
            return JsonResult.failure("设备不存在！",false);
        }
        Person p = personService.getByUserName(bean.getUsername());
        if (p!=null) {return JsonResult.failure("手机号已经被绑定了！",false);}
        Md5Hash md5Hash = new Md5Hash(bean.getPassword(),salt,3);
        Person person = new Person();
        person.setUsername(bean.getUsername());
        person.setPassword(md5Hash.toHex());
        person.setPhone(bean.getUsername());

        personService.save(person);
        return JsonResult.success(200,"设备绑定成功",personService.register(person,device));
    }
}
