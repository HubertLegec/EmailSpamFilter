package com.legec.tkom.core.configuration;


import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GlobalConfig {
    private static Configuration configuration = null;
    private static final String CONFIG_FILE = "config.conf";


    public static void loadConfiguration() throws IOException{
        Gson gson = new Gson();
        BufferedReader br = new BufferedReader(new FileReader(CONFIG_FILE));
        configuration = gson.fromJson(br, Configuration.class);
    }

    public static void saveConfiguration() throws IOException{
        Gson gson = new Gson();
        String mappedObject = gson.toJson(configuration);
        Files.write(Paths.get(CONFIG_FILE), mappedObject.getBytes());
    }

    public static Configuration getConfiguration(){
        if(configuration == null) {
            configuration = new Configuration();
        }
        return configuration;
    }

    public static void setConfiguration(Configuration conf){
        configuration =  conf;
    }
}
