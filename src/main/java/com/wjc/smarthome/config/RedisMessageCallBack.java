package com.wjc.smarthome.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.wjc.smarthome.dto.iot.DataRedisDto;
import com.wjc.smarthome.util.WebSocketUtil;
import com.wjc.smarthome.websocket.ProtocolProcess;
import com.wjc.smarthome.websocket.WSMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author: wjc
 * @Date: 2023-03-30 17:29
 **/
@Component
public class RedisMessageCallBack {


    public void deviceInfo(String message) throws IOException {
        List<DataRedisDto> dataRedisDtos = JSONUtil.toList(message, DataRedisDto.class);
        if (CollUtil.isEmpty(dataRedisDtos)){
            return;
        }
        Map<String, ConcurrentWebSocketSessionDecorator> decoratorMap = ProtocolProcess.deviceWebSocketDeMp.get(dataRedisDtos.get(0).getDeviceName());
        sendDtMsg(decoratorMap,new WSMessage(WebSocketUtil.DataType.DEVICE_DATA,dataRedisDtos));
    }

    private void sendDtMsg(Map<String, ConcurrentWebSocketSessionDecorator> map, Object msg) throws IOException {
        if (CollUtil.isNotEmpty(map)) {
            TextMessage message = new TextMessage(JSONUtil.toJsonStr(msg));
            for (ConcurrentWebSocketSessionDecorator value : map.values()) {
                if (value.isOpen()) {
                    value.sendMessage(message);
                }
            }
        }
    }
}
