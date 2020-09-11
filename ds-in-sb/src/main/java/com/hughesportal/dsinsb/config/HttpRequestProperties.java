package com.hughesportal.dsinsb.config;

import com.hughesportal.dsinsb.Utilities;
import lombok.Data;
import org.slf4j.Logger;
import org.springframework.http.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Data
public class HttpRequestProperties {

    private String hostname;
    private String port;
    private String baseurl;

    public HttpRequestProperties(){

    }

    public HttpRequestProperties(String hostname, String port, String baseurl) {
        this.hostname = hostname;
        this.port = port;
        this.baseurl = baseurl;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getBaseurl() {
        return baseurl;
    }

    public void setBaseurl(String baseurl) {
        this.baseurl = baseurl;
    }

    public String getApiUrl(){
        return hostname+":" +port+""+baseurl;
    }

    public ResponseEntity<String> apiCall_get(WebClient webClient, Logger log, String path, String... uriParams) throws WebClientResponseException{
        String formattedString = Utilities.formatUri(path, uriParams);
        log.info("[API Call] [GET] " + getApiUrl() + formattedString);
        try {
            return webClient.method(HttpMethod.GET)
                    .uri(getApiUrl() + path, (Object[]) uriParams)
                    .headers(httpHeaders -> {

                    })
                    .retrieve().toEntity(String.class)
                    .block();
        }catch (WebClientResponseException ex){
            //Log the error here, then re-throw the error so the caller can do something with it
            log.error("Error retrieving from path {}. Status code is {} and the message is: \n{}",
                    formattedString, ex.getRawStatusCode(), ex.getResponseBodyAsString());
            throw ex;
        }
    }

    public ResponseEntity<String> apiCall_post(WebClient webClient, Logger log, String path, String payload, String... uriParams) throws WebClientResponseException{
        String formattedString = Utilities.formatUri(path, uriParams);
        log.info("[API Call] [POST] " + getApiUrl() + formattedString);
        try {
            return webClient.method(HttpMethod.POST)
                    .uri(getApiUrl() + path, (Object[]) uriParams)
                    .headers(httpHeaders -> {
                        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                    })
                    .body(Mono.just(payload), String.class).retrieve()
                    .toEntity(String.class).block();
        }catch (WebClientResponseException ex){
            //Log the error here, then re-throw the error so the caller can do something with it
            log.error("Error posting path {}. Status code is {} and the message is {}\nPayload attempted:\n{}",
                    formattedString, ex.getRawStatusCode(), ex.getResponseBodyAsString(), payload);
            throw ex;
        }
    }

    public void apiCall_patch(WebClient webClient, Logger log, String path, String payload, String... uriParams) throws WebClientResponseException{
        String formattedString = Utilities.formatUri(path, uriParams);
        log.info("[API Call] [PATCH] " + getApiUrl() + formattedString);
        try {
            webClient.method(HttpMethod.PATCH)
                    .uri(getApiUrl() + path, (Object[]) uriParams)
                    .headers(httpHeaders -> {
                        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                    })
                    .body(Mono.just(payload), String.class).retrieve()
                    .bodyToMono(String.class).block();
        }catch (WebClientResponseException ex){
            //Log the error here, then re-throw the error so the caller can do something with it
            log.error("Error patching path {}. Status code is {} and the message is {}",
                    formattedString, ex.getRawStatusCode(), ex.getResponseBodyAsString());
            throw ex;
        }
    }

    public void apiCall_put(WebClient webClient, Logger log, String path, String payload, String... uriParams) throws WebClientResponseException{
        String formattedString = Utilities.formatUri(path, uriParams);
        log.info("[API Call] [PUT] " + getApiUrl() + formattedString);
        try {
            webClient.method(HttpMethod.PUT)
                    .uri(getApiUrl() + path, (Object[]) uriParams)
                    .headers(httpHeaders -> {
                        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                    })
                    .body(Mono.just(payload), String.class).retrieve()
                    .bodyToMono(String.class).block();
        }catch (WebClientResponseException ex){
            //Log the error here, then re-throw the error so the caller can do something with it
            log.error("Error puting path {}. Status code is {} and the message is {}",
                    formattedString, ex.getRawStatusCode(), ex.getResponseBodyAsString());
            throw ex;
        }
    }

    public void apiCall_delete(WebClient webClient, Logger log, String path, String payload, String... uriParams) throws WebClientResponseException{
        String formattedString = Utilities.formatUri(path, uriParams);
        log.info("[API Call] [DELETE] " + getApiUrl() + formattedString);
        try {
            webClient.method(HttpMethod.DELETE)
                    .uri(getApiUrl() + path, (Object[]) uriParams)
                    .headers(httpHeaders -> {
                        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                    })
                    .body(Mono.just(payload), String.class).retrieve()
                    .bodyToMono(String.class).block();
        }catch (WebClientResponseException ex){
            //Log the error here, then re-throw the error so the caller can do something with it
            log.error("Error deleting from path {}. Status code is {} and the message is {}",
                    formattedString, ex.getRawStatusCode(), ex.getResponseBodyAsString());
            throw ex;
        }
    }
}

