package com.hughesportal.dsinsb.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomHttpPropertyConfigurer {

    @Bean
    @ConfigurationProperties("downstream.api.one")
    public HttpRequestProperties httpRequestApiOne(){
        return new HttpRequestProperties();
    }

    @Bean
    @ConfigurationProperties("downstream.api.two")
    public HttpRequestProperties httpRequestApiTwo(){
        return new HttpRequestProperties();
    }

}

