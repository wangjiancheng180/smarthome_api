package com.wjc.smarthome.websocket;

import cn.hutool.json.JSONUtil;
import com.wjc.smarthome.util.WebSocketUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import java.io.IOException;

import static com.wjc.smarthome.websocket.ProtocolProcess.*;

/**
 * @Author: wjc
 * @Date: 2023-03-30 09:44
 **/
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceHandler implements WebSocketHandler {

    private WebSocketService webSocketService;
    /**
     * 建立连接后
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        addOnlineCount();
        addSession(session);
        log.info("deviceWebSocket在线数量:{}",getOnlineCount());
    }

    /**
     *
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.info("接收到的消息{}",message.getPayload());
    }


    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("连接发生异常！{}",exception.getMessage());
        remove(session);
        closeErrorSession(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        subOnlineCount();           //在线数减1
        log.info("连接关闭！当前连接数为:{},关闭状态:{}", getOnlineCount(), closeStatus);
        remove(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 添加Sesiond到deviceWebsocket修饰器集合
     * @param session
     */
    private void addSession(WebSocketSession session) throws IOException {
        String deviceName = (String) session.getAttributes().get(WebSocketUtil.DEVICE_NAME);
        //session修饰器
        ConcurrentWebSocketSessionDecorator decorator = new ConcurrentWebSocketSessionDecorator(session,
                sendTimeLimit, bufferSizeLimit, defaultStrategy);
        //发送第一条信息
        decorator.sendMessage(new TextMessage(JSONUtil.toJsonStr(webSocketService.findDeviceData(deviceName))));
        //加入集合
        addDtMap(deviceWebSocketDeMp,deviceName,session.getId(),decorator);
    }

    private void remove(WebSocketSession session) {
        String deviceId = (String) session.getAttributes().get(WebSocketUtil.DEVICE_NAME);
        if (StringUtils.isNotBlank(session.getId())) {
            removeDtMap(deviceWebSocketDeMp, deviceId, session.getId());
        }
    }
}
