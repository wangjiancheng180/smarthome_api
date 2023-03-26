package com.wjc.smarthome.service.count;

import com.wjc.smarthome.common.JsonResult;
import java.util.Map;

/**
 * @Author: wjc
 * @Date: 2023-03-11 15:20
 **/
public interface StatsService {

    JsonResult<Map<String, Integer>> queryDeviceOfWeek(Integer days);

    JsonResult<Map<String, Integer>> queryUserOfWeek(Integer days);

    JsonResult<Integer> queryTotalUser();

    JsonResult<Integer> queryTotalDevice();
}
