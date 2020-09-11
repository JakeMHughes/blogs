package com.hughesportal.dsinsb;

import com.datasonnet.Mapper;
import com.datasonnet.document.DefaultDocument;
import com.datasonnet.document.Document;
import com.datasonnet.document.MediaType;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.stream.Collectors;

public class Utilities {


    public static String datasonnetMapping(
            String resourceLocation,
            String payload
    ) throws Exception{
        return datasonnetMapping(resourceLocation,payload,Map.of());
    }

    public static String datasonnetMapping(
            String resourceLocation,
            String payload,
            Map<String, Document<?>> variables) throws Exception {
        MediaType mimeType = MediaType.parseMediaType("application/json");

        String dataSonnetScript = getFileFromResources(resourceLocation);

        Mapper mapper = new Mapper(dataSonnetScript, variables.keySet());

        return mapper.transform(new DefaultDocument<>(payload, mimeType), variables, mimeType).getContent();
    }

    public static String datasonnetMappingString(
            String dataSonnetScript,
            String payload){
        return datasonnetMappingString(dataSonnetScript,payload, Map.of());
    }

    public static String datasonnetMappingString(
            String dataSonnetScript,
            String payload,
            Map<String, Document<?>> variables)  {
        MediaType mimeType = MediaType.parseMediaType("application/json");

        Mapper mapper = new Mapper(dataSonnetScript, variables.keySet());
        return mapper.transform(new DefaultDocument<>(payload, mimeType), variables, mimeType).getContent();

    }



    //Read files from resources folder
    public static String getFileFromResources(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new ClassPathResource(fileName).getInputStream()));
        return reader.lines().collect(Collectors.joining("\n"));
    }

    public static String formatUri(String path, String... uriParams){

        return String.format(path.replaceAll("\\{(.*?)}", "%s"), (Object[]) uriParams);
    }

}
