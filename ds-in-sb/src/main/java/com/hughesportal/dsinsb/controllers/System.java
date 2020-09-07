package com.hughesportal.dsinsb.controllers;

import com.hughesportal.dsinsb.services.DBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys")
public class System {

    @Autowired
    DBService service;

    @RequestMapping(value = "/billionaires", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllBillionaires(){
        return service.getAllBillionaires();
    }



}
