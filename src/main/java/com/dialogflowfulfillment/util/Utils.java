package com.dialogflowfulfillment.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Utils {

    private static Logger logger = LoggerFactory.getLogger(Utils.class);
    public boolean isJson(String json){

        Gson gson = new Gson();
        try {
            Object o = gson.fromJson(json, Object.class);
            System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(o));
        } catch (Exception e) {
            logger.info("invalid json format");
            return false;
        }
        return true;
    }
}
