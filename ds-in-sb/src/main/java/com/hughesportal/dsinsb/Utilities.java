package com.hughesportal.dsinsb;

import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class Utilities {

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
