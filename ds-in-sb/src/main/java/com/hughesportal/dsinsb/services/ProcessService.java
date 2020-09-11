package com.hughesportal.dsinsb.services;

import com.hughesportal.dsinsb.Utilities;
import com.hughesportal.dsinsb.config.HttpRequestProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;

@Service
public class ProcessService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    HttpRequestProperties httpRequestSystem;

    private final WebClient webClient;

    @Autowired
    public ProcessService() {
        this.webClient = (WebClient.builder().build());
    }

    //constructor used for testing
    public ProcessService(WebClient client, HttpRequestProperties... props ){
        this.webClient = client;
        this.httpRequestSystem = props[0];
    }


    public ResponseEntity<?> getAllBillionaires(){
        String response = httpRequestSystem.apiCall_get(webClient,log,"/billionaires").getBody();

        String transformed = Utilities.datasonnetMappingString(
                "std.map(function(it)\n" +
                        "{\n" +
                        "    id:it.id,\n" +
                        "    name:it.first_name + ' ' + it.last_name,\n" +
                        "    career: it.career\n" +
                        "}, payload)",
                response);
        return ResponseEntity.status(200).body(transformed);
    }

    public ResponseEntity<?> createNewBillionaire(String payload, String locationData){
        String transformed = Utilities.datasonnetMappingString(
                "local names=std.split(payload.name, \" \");\n" +
                        "{\n" +
                        "    first_name: names[0],\n" +
                        "    last_name: names[1],\n" +
                        "    career: payload.career\n" +
                        "}", payload);
        String location = httpRequestSystem.apiCall_post(webClient,log,"/billionaires",transformed).getHeaders().getLocation().toString();

        return ResponseEntity.status(201).header("Location", locationData + location.substring(location.lastIndexOf('/'))).body(null);
    }

    public ResponseEntity<?> getBillionaireByID(int id){
        String response = httpRequestSystem.apiCall_get(webClient,log,"/billionaires/{id}", String.valueOf(id)).getBody();
        String transformed = Utilities.datasonnetMappingString(
                        "{\n" +
                        "    id:payload.id,\n" +
                        "    name:payload.first_name + ' ' + payload.last_name,\n" +
                        "    career: payload.career\n" +
                        "}",
                response);
        return ResponseEntity.status(200).body(transformed);
    }

    public ResponseEntity<?> updateBillionaireByID(int id, String payload){
        String transformed = Utilities.datasonnetMappingString("local names=\n" +
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
                "})", payload);
        httpRequestSystem.apiCall_patch(webClient,log,"/billionaires/{id}",transformed,String.valueOf(id));
        return ResponseEntity.status(204).body(null);
    }

    public ResponseEntity<?> deleteBillionaireByID(int id){
        httpRequestSystem.apiCall_delete(webClient,log,"/billionaires/{id}","", String.valueOf(id));
        return ResponseEntity.status(204).body(null);
    }
}
