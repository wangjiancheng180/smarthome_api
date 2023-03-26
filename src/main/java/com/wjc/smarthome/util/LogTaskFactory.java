package com.wjc.smarthome.util;

import com.wjc.smarthome.enetity.system.SysLogInfo;
import com.wjc.smarthome.service.system.SysLogInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.TimerTask;

/**
 * @Author: wjc
 * @Date: 2023-03-11 13:49
 **/
@Component
@Slf4j
@RequiredArgsConstructor
public class LogTaskFactory {

    private final SysLogInfoService sysLogInfoService;
    /**
     * 业务日志
     * @param
     * @return
     */
    public TimerTask bussinessLog(SysLogInfo logInfo) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    sysLogInfoService.save(logInfo);
                } catch (Exception e) {
                    log.error("创建业务日志异常!", e);
                }
            }
        };
    }
}
