package com.wjc.smarthome.mapper.iot;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wjc.smarthome.enetity.iot.Device;
import org.apache.ibatis.annotations.Param;

public interface DeviceMapper extends BaseMapper<Device> {

    /**
     * 根据用户名返回用户信息（用户名和手机号一致）
     * @param username
     * @return
     */
    Device findByUsername(@Param("username") String username);
}