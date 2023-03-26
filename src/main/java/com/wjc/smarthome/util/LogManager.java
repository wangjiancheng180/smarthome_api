package com.wjc.smarthome.util;

import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: wjc
 * @Date: 2023-03-11 13:48
 **/
public class LogManager {
    //日志记录操作延时
    private final static int OPERATE_DELAY_TIME = 50;

    //异步操作记录日志的线程池
    private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(10);

    private LogManager() {
    }

    private final static LogManager logManager = new LogManager();

    public static LogManager me() {
        return logManager;
    }

    public void executeLog(TimerTask task) {
        executor.schedule(task, OPERATE_DELAY_TIME, TimeUnit.MILLISECONDS);
    }
}
