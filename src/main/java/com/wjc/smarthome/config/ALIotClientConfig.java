package com.wjc.smarthome.config;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.iot20180120.AsyncClient;
import darabonba.core.client.ClientOverrideConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 王建成
 * @date 2022/12/19--9:35
 */
@Configuration
public class ALIotClientConfig {

    @Value("${iot.accessKey}")
    private String accessKey;

    @Value("${iot.accessSecret}")
    private String accessSecret;

    @Bean(name = "alIotClient")
    public AsyncClient alIotClient() {
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(accessKey)
                .accessKeySecret(accessSecret)
                //.securityToken("<your-token>") // use STS token
                .build());

        // Configure the Client
        AsyncClient client = AsyncClient.builder()
                // 区域 ID
                .region("cn-shanghai")
                //.httpClient(httpClient) // Use the configured HttpClient, otherwise use the default HttpClient (Apache HttpClient)
                .credentialsProvider(provider)
                //.serviceConfiguration(Configuration.create()) // Service-level configuration
                // Client-level configuration rewrite, can set Endpoint, Http request parameters, etc.
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride("iot.cn-shanghai.aliyuncs.com")
                        //.setConnectTimeout(Duration.ofSeconds(30))
                ).build();
        return client;
    }
}
