package com.wjc.smarthome.config;

import com.wjc.smarthome.websocket.AuthWebSocketInterceptor;
import com.wjc.smarthome.websocket.ProtocolProcess;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;



/**
 * @Author: wjc
 * @Date: 2023-03-29 16:51
 **/
@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final AuthWebSocketInterceptor interceptor;

    private final ProtocolProcess protocolProcess;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(protocolProcess.deviceHandler(),"/ws/deviceInfo")
                .addInterceptors(interceptor)
                .setAllowedOriginPatterns("*");
    }
}
