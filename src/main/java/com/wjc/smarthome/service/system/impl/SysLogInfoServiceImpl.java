package com.wjc.smarthome.service.system.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wjc.smarthome.common.JsonResult;
import com.wjc.smarthome.param.system.LoginInfo;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjc.smarthome.enetity.system.SysLogInfo;
import com.wjc.smarthome.mapper.system.SysLogInfoMapper;
import com.wjc.smarthome.service.system.SysLogInfoService;
/**
*@Author: wjc
*@Date: 2023-03-11 12:26
**/    
@Service
public class SysLogInfoServiceImpl extends ServiceImpl<SysLogInfoMapper, SysLogInfo> implements SysLogInfoService{

    @Override
    public JsonResult<IPage<SysLogInfo>> queryLogs() {
        Page<SysLogInfo> page = new Page<>(1, 30);
        page = new LambdaQueryChainWrapper<>(baseMapper).orderByDesc(SysLogInfo::getCreateTime).page(page);
        return JsonResult.success(page);
    }
}
