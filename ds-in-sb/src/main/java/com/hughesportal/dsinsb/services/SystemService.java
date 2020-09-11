package com.hughesportal.dsinsb.services;

import com.datasonnet.document.DefaultDocument;
import com.datasonnet.document.MediaType;
import com.hughesportal.dsinsb.Utilities;
import com.hughesportal.dsinsb.repositories.BillionairesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class SystemService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    BillionairesRepository repo;

    public ResponseEntity<?> getAllBillionaires(){
        String values = repo.getAllBillionaires();
        log.info(values);
        return ResponseEntity.ok(values);
    }

    public ResponseEntity<?> createNewBillionaire(String payload, String locationData){
        String firstName = Utilities.datasonnetMappingString("payload.first_name",payload).replaceAll("\"","");
        String lastName = Utilities.datasonnetMappingString("payload.last_name",payload).replaceAll("\"","");
        String career = Utilities.datasonnetMappingString("payload.career",payload).replaceAll("\"","");

        if(repo.insertBillionaire(firstName,lastName,career) > 0){
            return ResponseEntity.status(201).header("Location", locationData+"/"+ repo.getLastInsert()).body(null);
        }
        return ResponseEntity.status(500).body("{\"message\":\"Failed to insert the billionaire.\"}");
    }

    public ResponseEntity<?> getBillionaireByID(int id){
        String billionaire = repo.getBillionaireByID(id);
        if(!billionaire.contains("id")){
            return ResponseEntity.status(404).body("{\"message\":\"Failed to get the billionaire.\"}");
        }
        return ResponseEntity.status(200).body(billionaire);
    }

    public ResponseEntity<?> updateBillionaireByID(int id, String payload){

        String currentBillionaire = repo.getBillionaireByID(id);
        if(!currentBillionaire.contains("id")){
            return ResponseEntity.status(404).body("{\"message\":\"Failed to get the billionaire.\"}");
        }

        String newPayload = Utilities.datasonnetMappingString(
                "local getOrDefault(item)=" +
                        "    if(item in payload) then" +
                        "        payload[item]" +
                        "    else" +
                        "        current[item];" +
                        "{" +
                        "    first_name: getOrDefault(\"first_name\")," +
                        "    last_name: getOrDefault(\"last_name\")," +
                        "    career: getOrDefault(\"career\")" +
                        "}",
                payload, Map.of("current", new DefaultDocument(currentBillionaire, MediaType.parseMediaType("application/json"))));

        String firstName = Utilities.datasonnetMappingString("payload.first_name",newPayload).replaceAll("\"","");
        String lastName = Utilities.datasonnetMappingString("payload.last_name",newPayload).replaceAll("\"","");
        String career = Utilities.datasonnetMappingString("payload.career",newPayload).replaceAll("\"","");

        if(repo.updateBillionaireByID(id,firstName,lastName,career)>0){
            return ResponseEntity.status(204).body(null);
        }
        return ResponseEntity.status(500).body("\"message\":\"TODO\"");
    }

    public ResponseEntity<?> deleteBillionaireByID(int id){
        if(repo.deleteBillionaireByID(id)>0){
            return ResponseEntity.status(204).body(null);
        }
        return ResponseEntity.status(500).body("{\"message\":\"Failed to remove the billionaire.\"}");
    }
}
