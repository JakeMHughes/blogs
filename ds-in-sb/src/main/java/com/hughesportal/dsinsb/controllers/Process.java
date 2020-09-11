package com.hughesportal.dsinsb.controllers;

import com.hughesportal.dsinsb.services.ProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/proc")
public class Process {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ProcessService service;

    @RequestMapping(value = "/billionaires", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllBillionaires(){
        return service.getAllBillionaires();
    }

    @RequestMapping(value = "/billionaires", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)//, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNewBillionaire(HttpServletRequest servlet) throws IOException {
        return service.createNewBillionaire(servlet.getReader().lines().collect(Collectors.joining()), servlet.getRequestURL().toString());
    }

    @RequestMapping(value = "/billionaires/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBillionaire(@PathVariable("id") int id){
        return service.getBillionaireByID(id);
    }

    @RequestMapping(value = "/billionaires/{id}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateBillionaire(@PathVariable("id") Integer id, HttpServletRequest servlet) throws IOException {
        return service.updateBillionaireByID(id, servlet.getReader().lines().collect(Collectors.joining()));
    }

    @RequestMapping(value = "/billionaires/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteBillionaire(@PathVariable("id") Integer id){
        return service.deleteBillionaireByID(id);
    }

}
