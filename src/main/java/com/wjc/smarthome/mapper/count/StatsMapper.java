package com.wjc.smarthome.mapper.count;

import com.wjc.smarthome.dto.count.StatsDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: wjc
 * @Date: 2023-03-11 15:21
 **/
public interface StatsMapper {


    List<StatsDto> countOfWeek(@Param("tableName") String tableName,@Param("td") Integer td);

    Integer getTotal(@Param("tableName") String tableName);
}
