package com.wjc.smarthome.websocket;

import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Author: wjc
 * @Date: 2023-03-30 10:52
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class ProtocolProcess {

    /**
     * 在线数量
     */
    private volatile static int onlineCount = 0;

    /**
     * 已连接的ws列表
     */
    public static Map<String, WebSocketSession> WS_MAPS = new ConcurrentHashMap<>();

    /**
     * The constant sendTimeLimit.
     * 发送一条消息的最长时间
     */
    public static final int sendTimeLimit = (int) TimeUnit.SECONDS.toMillis(10);
    /**
     * The constant bufferSizeLimit.
     * 发送一条消息最长的长度
     */
    public static final int bufferSizeLimit = 1024 * 1024 * 1024;

    private final WebSocketService webSocketService;
    /**
     * The constant defaultStrategy.
     *不满足上面要求的情况下对消息处理的策略
     */
    public static ConcurrentWebSocketSessionDecorator.OverflowStrategy defaultStrategy = ConcurrentWebSocketSessionDecorator.OverflowStrategy.DROP;

    public static Map<String,Map<String,ConcurrentWebSocketSessionDecorator>> deviceWebSocketDeMp = new ConcurrentHashMap<>();


    /**
     * Add dt map.
     *
     * @param map       the map
     * @param key       the key
     * @param sessionId the session id
     * @param decorator the decorator
     */
    public static void addDtMap(Map<String, Map<String, ConcurrentWebSocketSessionDecorator>> map, String key,
                                String sessionId, ConcurrentWebSocketSessionDecorator decorator) {
        Map<String, ConcurrentWebSocketSessionDecorator> value = map.get(key);
        if (CollUtil.isEmpty(value)) {
            value = new ConcurrentHashMap<>();
        }
        value.put(sessionId, decorator);
        map.put(key, value);
    }

    /**
     * Remove dt map.
     *
     * @param map       the map
     * @param key       the key
     * @param sessionId the session id
     */
    public static void removeDtMap(Map<String, Map<String, ConcurrentWebSocketSessionDecorator>> map, String key, String sessionId) {
        if (map.containsKey(key)) {
            Map<String, ConcurrentWebSocketSessionDecorator> value = map.get(key);
            value.remove(sessionId);
            if (CollUtil.isEmpty(value)) {
                map.remove(key);
            }
        }
    }

    private DeviceHandler deviceHandler;

    /**
     * Device handler device handler.
     *
     * @return the device handler
     */
    public DeviceHandler deviceHandler() {
        if (deviceHandler == null) {
            deviceHandler = new DeviceHandler(webSocketService);
        }
        return deviceHandler;
    }

    /**
     * Gets online count.
     *
     * @return the online count
     */
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    /**
     * Add online count.
     */
    public static synchronized void addOnlineCount() {
        onlineCount++;
    }

    /**
     * Sub online count.
     */
    public static synchronized void subOnlineCount() {
        onlineCount--;
    }

    public static synchronized void closeErrorSession(WebSocketSession session) {
        if (session.isOpen()) {
            try {
                session.close();
            } catch (Exception e) {
                log.error("连接异常关闭", e);
            }
        }

        subOnlineCount();           //在线数减1
        log.info("连接关闭！当前连接数为:{}", getOnlineCount());
    }


}
