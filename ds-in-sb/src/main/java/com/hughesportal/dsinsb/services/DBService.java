package com.hughesportal.dsinsb.services;

import com.hughesportal.dsinsb.repositories.BillionairesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DBService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    BillionairesRepository repo;

    public ResponseEntity<?> getAllBillionaires(){
        String values = repo.getAllBillionaires();
        log.info(values);
        return ResponseEntity.ok(values);
    }
}
