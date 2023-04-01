package com.wjc.smarthome.config;


import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;



/**
 * @author 王建成
 * @date 2022/3/24--17:19
 */
@Configuration
public class CorsConfig{

    @Bean
    public FilterRegistrationBean corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        //暴露token头信息
        config.addExposedHeader("Authorization");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean cros = new FilterRegistrationBean(new CorsFilter(source));
        //设置优先级最高
        cros.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return cros;
    }



}
