package com.wjc.smarthome.service.iot;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wjc.smarthome.common.JsonResult;
import com.wjc.smarthome.enetity.iot.Device;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wjc.smarthome.param.iot.DeviceCreateBean;
import com.wjc.smarthome.param.iot.DeviceQueryBean;

import java.util.List;

/**
 * @author wjc
 */
public interface DeviceService extends IService<Device>{


    JsonResult<Boolean> create(DeviceCreateBean bean);

    JsonResult<Boolean> disable(String iotId);

    JsonResult<Boolean> enable(String iotId);

    JsonResult<Boolean> delete(String iotId);

    JsonResult<Device> queryByIotId(String iotId);

    JsonResult<IPage<Device>> queryPage(DeviceQueryBean bean);

    JsonResult<List<Device>> queryOnlineDevice();

    JsonResult<List<Device>> queryOfflineDevice();

    Device findByUsername(String username);
}
