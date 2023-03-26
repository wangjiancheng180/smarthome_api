package com.wjc.smarthome.mapstruct.iot;

import com.wjc.smarthome.enetity.iot.Device;
import com.wjc.smarthome.param.iot.DeviceCreateBean;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @Author: wjc
 * @Date: 2023-03-01 21:42
 **/
@Mapper
public interface DeviceMap {

    DeviceMap INSTANCE = Mappers.getMapper(DeviceMap.class);

    Device beanToEentity(DeviceCreateBean bean);
}
