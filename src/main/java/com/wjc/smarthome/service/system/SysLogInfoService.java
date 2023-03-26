package com.wjc.smarthome.service.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wjc.smarthome.common.JsonResult;
import com.wjc.smarthome.enetity.system.SysLogInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wjc.smarthome.param.system.LoginInfo;

/**
*@Author: wjc
*@Date: 2023-03-11 12:26
**/    
public interface SysLogInfoService extends IService<SysLogInfo>{


        JsonResult<IPage<SysLogInfo>> queryLogs();

    }
