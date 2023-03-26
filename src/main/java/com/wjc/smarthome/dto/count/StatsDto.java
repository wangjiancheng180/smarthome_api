package com.wjc.smarthome.dto.count;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: wjc
 * @Date: 2023-03-11 15:14
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatsDto {

    private String row;

    private int num;
}
