package com.wjc.smarthome.util;

import cn.hutool.crypto.KeyUtil;
import cn.hutool.jwt.Claims;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.AlgorithmUtil;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * @author 王建成
 * @date 2023/2/23--13:21
 */

@Data
public class TokenUtils {

    /**
    token放置在请求头里的标识
     */
    @Value("${security.token.header}")
    public static String header = "Authorization";

    /**
     * 密钥
     */
    @Value("${security.token.secret}")
    private static String secret = "wjc-key";

    @Value("${security.token.expireTime}")
    private static int expireTime =10080;

    public static String createToken(Map<String,String> payloads){
        long now = System.currentTimeMillis()+(expireTime * 60 * 1000);
        //默认HS265(HmacSHA256)算法
        byte[] key = secret.getBytes();
        JWT jwt = JWT.create();
        payloads.forEach((k,v)->{
            jwt.setPayload(k,v);
        });
        return jwt.setKey(key).setExpiresAt(new Date(now)).sign();
    }


    public static boolean verify(String token){
        byte[] key = secret.getBytes();
        return JWT.of(token).setKey(key).validate(0);
    }

    public static Object parseToken(String token,String payload){
        JWT jwt = JWT.of(token);
        return jwt.getPayload(payload);
    }

}
