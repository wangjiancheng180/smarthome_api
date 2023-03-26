package com.wjc.smarthome.service.count.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.wjc.smarthome.common.JsonResult;
import com.wjc.smarthome.dto.count.StatsDto;
import com.wjc.smarthome.mapper.count.StatsMapper;
import com.wjc.smarthome.service.count.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author: wjc
 * @Date: 2023-03-11 15:20
 **/
@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsMapper statsMapper;

    @Override
    public JsonResult<Map<String, Integer>> queryDeviceOfWeek(Integer days) {

        Map<String, Integer> result = offSetCurDays(-days);
        List<StatsDto> device = countOfWeek("device", days);
        device.forEach((e)->{
            if (result.containsKey(e.getRow())) {
                result.put(e.getRow(), e.getNum());
            }
        });
        return JsonResult.success(result);
    }

    @Override
    public JsonResult<Map<String, Integer>> queryUserOfWeek(Integer days) {
        Map<String, Integer> result = offSetCurDays(-days);
        List<StatsDto> device = countOfWeek("user_info", days);
        device.stream().map((e) -> {
            if (result.containsKey(e.getRow())) {
                result.put(e.getRow(), e.getNum());
            }
            return e;
        });
        return JsonResult.success(result);
    }

    @Override
    public JsonResult<Integer> queryTotalUser() {
        return JsonResult.success(200,"",getTotal("user_info"));
    }

    @Override
    public JsonResult<Integer> queryTotalDevice() {
        return JsonResult.success(200,"",getTotal("device"));
    }

    private Integer getTotal(String tableName) {
        return statsMapper.getTotal(tableName);
    }

    private List<StatsDto> countOfWeek(String tableName, Integer td) {
        return statsMapper.countOfWeek(tableName, td);
    }

    private Map<String, Integer> offSetCurDays(int end) {
        Date now = new Date();
        Map<String, Integer> stats = new LinkedHashMap<>();
        for (int i = 0; i > end; i--) {
            DateTime dateTime = DateUtil.offsetDay(now, i);
            String format = DateUtil.format(dateTime, "yyyy-MM-dd");
            stats.put(format, 0);
        }
        return stats;
    }

}
