package com.hughesportal.dsinsb;

import com.datasonnet.Mapper;
import com.datasonnet.document.Document;
import com.datasonnet.document.StringDocument;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Utilities {

    private static final String mime = "application/json";

    public static Mapper getMapper(String script, Set<String> variables){
        return new Mapper(script, variables, true);
    }

    public static Mapper getMapper(String script){
        return getMapper(script, Set.of());
    }


    //Read files from resources folder
    public static String getFileFromResources(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new ClassPathResource(fileName).getInputStream()));
        return reader.lines().collect(Collectors.joining("\n"));
    }

}
