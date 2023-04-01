package com.wjc.smarthome.websocket;

import cn.hutool.core.util.StrUtil;
import com.wjc.smarthome.enetity.iot.Device;
import com.wjc.smarthome.service.iot.DeviceService;
import com.wjc.smarthome.util.TokenUtils;
import com.wjc.smarthome.util.WebSocketUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;

/**
 * @Author: wjc
 * @Date: 2023-03-30 13:58
 **/
@Component
@Slf4j
@RequiredArgsConstructor
public class AuthWebSocketInterceptor implements HandshakeInterceptor {

    private final DeviceService deviceService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest){
            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
            String token = servletRequest.getParameter(WebSocketUtil.TOKEN);
            if (!TokenUtils.verify(token)){
                log.error("websocket的Token失效！");
                return false;
            }

            //取得username
            String[] split = servletRequest.getRequestURI().split("/");
            log.info("websocket请求资源{}", servletRequest.getRequestURI());
            String username = (String) TokenUtils.parseToken(token, "username");
//            log.info("spilt{}", Arrays.toString(split));
            switch (split[3]){
                case "deviceInfo":
                    Device device = deviceService.findByUsername(username);
                    if (device == null){
                        return false;
                    }
                    attributes.put(WebSocketUtil.DEVICE_NAME,device.getDeviceName());
                    break;
                default:
            }
            return true;
        }

        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
