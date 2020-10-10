package com.hughesportal.dsinsb.services;

import com.datasonnet.Mapper;
import com.hughesportal.dsinsb.Utilities;
import com.hughesportal.dsinsb.config.HttpRequestConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;

@Service
public class ProcessService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    HttpRequestConfiguration httpRequestSystem;

    private final WebClient webClient;

    @Autowired
    public ProcessService() {
        this.webClient = (WebClient.builder().build());
    }

    //constructor used for testing
    public ProcessService(WebClient client, HttpRequestConfiguration... props ){
        this.webClient = client;
        this.httpRequestSystem = props[0];
    }

    Map<String, Mapper> mappers = Map.of(
            "getAllBillionaires", Utilities.getMapper("std.map(function(it)\n" +
                    "{\n" +
                    "    id:it.id,\n" +
                    "    name:it.first_name + ' ' + it.last_name,\n" +
                    "    career: it.career\n" +
                    "}, payload)"),
            "createNewBillionaire", Utilities.getMapper("local names=std.split(payload.name, \" \");\n" +
                    "{\n" +
                    "    first_name: names[0],\n" +
                    "    last_name: names[1],\n" +
                    "    career: payload.career\n" +
                    "}"),
            "getBillionaireByID", Utilities.getMapper("{\n" +
                    "    id:payload.id,\n" +
                    "    name:payload.first_name + ' ' + payload.last_name,\n" +
                    "    career: payload.career\n" +
                    "}"),
            "updateBillionaireByID", Utilities.getMapper("local names=\n" +
                    "    if('name' in payload) then \n" +
                    "        std.split(payload.name, \" \")\n" +
                    "    else [null,null];\n" +
                    "\n" +
                    "std.prune({\n" +
                    "    first_name: names[0],\n" +
                    "    last_name: names[1],\n" +
                    "    career: if('career' in payload) then\n" +
                    "        payload.career \n" +
                    "    else null\n" +
                    "})")
    );


    public ResponseEntity<?> getAllBillionaires(){
        try {
            String response = (String) httpRequestSystem.getRequestBuilder(webClient, "/billionaires")
                    .setClass(String.class).get().getBody();
            String transformed = mappers.get("getAllBillionaires").transform(response);
            return ResponseEntity.status(200).body(transformed);
        } catch(WebClientResponseException ex) {
            return ResponseEntity.status(ex.getRawStatusCode()).body(ex.getResponseBodyAsString());
        }
    }

    public ResponseEntity<?> createNewBillionaire(String payload, String locationData){
        try {
            String transformed = mappers.get("createNewBillionaire").transform(payload);
            String location = httpRequestSystem.getRequestBuilder(webClient, "/billionaires")
                    .setBody(transformed).setHeaders(Map.of("Content-Type", "application/json"))
                    .post().getHeaders().getLocation().toString();
            return ResponseEntity.status(201).header("Location", locationData + location.substring(location.lastIndexOf('/'))).body(null);
        } catch(WebClientResponseException ex) {
            return ResponseEntity.status(ex.getRawStatusCode()).body(ex.getResponseBodyAsString());
        }
    }

    public ResponseEntity<?> getBillionaireByID(int id){
        try {
            String response = httpRequestSystem.getRequestBuilder(webClient, "/billionaires/{id}").setClass(String.class)
                    .setUrlParameters(List.of(String.valueOf(id))).get().getBody().toString();
            String transformed = mappers.get("getBillionaireByID").transform(response);
            return ResponseEntity.status(200).body(transformed);
        } catch(WebClientResponseException ex) {
            return ResponseEntity.status(ex.getRawStatusCode()).body(ex.getResponseBodyAsString());
        }
    }

    public ResponseEntity<?> updateBillionaireByID(int id, String payload){
        try {
            String transformed = mappers.get("updateBillionaireByID").transform(payload);
            httpRequestSystem.getRequestBuilder(webClient, "/billionaires/{id}")
                    .setBody(transformed).setUrlParameters(List.of(String.valueOf(id)))
                    .setHeaders(Map.of("Content-Type", "application/json")).patch();
            return ResponseEntity.status(204).body(null);
        } catch(WebClientResponseException ex) {
            return ResponseEntity.status(ex.getRawStatusCode()).body(ex.getResponseBodyAsString());
        }
    }

    public ResponseEntity<?> deleteBillionaireByID(int id){
        try {
            httpRequestSystem.getRequestBuilder(webClient, "/billionaires/{id}").setUrlParameters(List.of(String.valueOf(id))).delete();
            return ResponseEntity.status(204).body(null);
        } catch(WebClientResponseException ex) {
            return ResponseEntity.status(ex.getRawStatusCode()).body(ex.getResponseBodyAsString());
        }
    }
}
