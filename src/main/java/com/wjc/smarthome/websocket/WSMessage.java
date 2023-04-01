package com.wjc.smarthome.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: wjc
 * @Date: 2023-04-01 16:42
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WSMessage implements Serializable {
    private String dataType;
    private Object content;
}
