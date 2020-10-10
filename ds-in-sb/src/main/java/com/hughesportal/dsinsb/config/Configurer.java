package com.hughesportal.dsinsb.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Configurer {

    @Bean
    @ConfigurationProperties("downstream.system")
    public HttpRequestConfiguration httpRequestSystem(){
        return new HttpRequestConfiguration();
    }

}

