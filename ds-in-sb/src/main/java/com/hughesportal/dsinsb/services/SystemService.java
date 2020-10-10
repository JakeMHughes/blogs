package com.hughesportal.dsinsb.services;

import com.datasonnet.Mapper;
import com.datasonnet.document.StringDocument;
import com.hughesportal.dsinsb.Utilities;
import com.hughesportal.dsinsb.repositories.BillionairesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
public class SystemService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    BillionairesRepository repo;

    private final Map<String, Mapper> mappers = Map.of(
            "getFirstName", Utilities.getMapper("payload.first_name"),
            "getLastName", Utilities.getMapper("payload.last_name"),
            "getCareer", Utilities.getMapper("payload.career"),
            "updateInputMap", Utilities.getMapper("local getOrDefault(item)=" +
                    "    if(item in payload) then" +
                    "        payload[item]" +
                    "    else" +
                    "        current[item];" +
                    "{" +
                    "    first_name: getOrDefault(\"first_name\")," +
                    "    last_name: getOrDefault(\"last_name\")," +
                    "    career: getOrDefault(\"career\")" +
                    "}", Set.of("current"))
    );

    public ResponseEntity<?> getAllBillionaires(){
        String values = repo.getAllBillionaires();
        log.info(values);
        return ResponseEntity.ok(values);
    }

    public ResponseEntity<?> createNewBillionaire(String payload, String locationData){
        String firstName = mappers.get("getFirstName").transform(payload).replaceAll("\"","");
        String lastName = mappers.get("getLastName").transform(payload).replaceAll("\"","");
        String career = mappers.get("getCareer").transform(payload).replaceAll("\"","");

        if(repo.insertBillionaire(firstName,lastName,career) > 0){
            return ResponseEntity.status(201).header("Location", locationData+"/"+ repo.getLastInsert()).body(null);
        }
        return ResponseEntity.status(500).body("{\"message\":\"Failed to insert the billionaire.\"}");
    }

    public ResponseEntity<?> getBillionaireByID(int id){
        String billionaire = repo.getBillionaireByID(id);
        if(billionaire == null || !billionaire.contains("id")){
            return ResponseEntity.status(404).body("{\"message\":\"Billionaire does not exist.\"}");
        }
        return ResponseEntity.status(200).header("Content-Type", "application/json").body(billionaire);
    }

    public ResponseEntity<?> updateBillionaireByID(int id, String payload){

        String currentBillionaire = repo.getBillionaireByID(id);
        if(currentBillionaire == null || !currentBillionaire.contains("id")){
            return ResponseEntity.status(404).body("{\"message\":\"Billionaire does not exist.\"}");
        }

        String newPayload = mappers.get("updateInputMap").transform(
                new StringDocument(payload, "application/json"),
                Map.of("current", new StringDocument(currentBillionaire, "application/json"))
        ).getContentsAsString();

        String firstName = mappers.get("getFirstName").transform(newPayload).replaceAll("\"","");
        String lastName = mappers.get("getLastName").transform(newPayload).replaceAll("\"","");
        String career = mappers.get("getCareer").transform(newPayload).replaceAll("\"","");

        if(repo.updateBillionaireByID(id,firstName,lastName,career)>0){
            return ResponseEntity.status(204).body(null);
        }
        return ResponseEntity.status(500).body("\"message\":\"TODO\"");
    }

    public ResponseEntity<?> deleteBillionaireByID(int id){
        if(repo.deleteBillionaireByID(id)>0){
            return ResponseEntity.status(204).body(null);
        }
        return ResponseEntity.status(404).body("{\"message\":\"Billionaire does not exist.\"}");
    }
}
