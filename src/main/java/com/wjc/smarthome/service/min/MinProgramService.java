package com.wjc.smarthome.service.min;

import com.wjc.smarthome.common.JsonResult;
import com.wjc.smarthome.param.min.RegisterInfoBean;

/**
 * @Author: wjc
 * @Date: 2023-03-26 13:59
 **/
public interface MinProgramService {
    JsonResult<Boolean> register(RegisterInfoBean bean);
}
