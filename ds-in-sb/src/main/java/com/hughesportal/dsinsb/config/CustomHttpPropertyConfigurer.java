package com.hughesportal.dsinsb.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomHttpPropertyConfigurer {

    @Bean
    @ConfigurationProperties("downstream.system")
    public HttpRequestProperties httpRequestSystem(){
        return new HttpRequestProperties();
    }

}

