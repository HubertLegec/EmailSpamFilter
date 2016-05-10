package com.legec.tkom.core.configuration;


import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GlobalConfig {
    private static Configuration configuration = null;


    public static void loadConfiguration(String configFilePath) throws IOException{
        Gson gson = new Gson();
        BufferedReader br = new BufferedReader(new FileReader(configFilePath));
        configuration = gson.fromJson(br, Configuration.class);
    }

    public static void saveConfiguration(String destinationPath) throws IOException{
        Gson gson = new Gson();
        String mappedObject = gson.toJson(configuration);
        Files.write(Paths.get(destinationPath), mappedObject.getBytes());
    }

    public static Configuration getConfiguration(){
        return configuration;
    }

    public static boolean isConfigurationLoaded(){
        return configuration != null ? true : false;
    }
}
