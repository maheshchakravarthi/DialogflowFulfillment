package com.dialogflowfulfillment.delegate;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.dialogflowfulfillment.config.RedisConfig;
import com.dialogflowfulfillment.dao.RedisConnect;
import com.dialogflowfulfillment.model.Experience;
import com.dialogflowfulfillment.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Component

public class FulfillmentManager {

    @Autowired
    RedisConnect redisConnect;

    @Autowired
    Utils util;

    @Autowired
    RedisConfig redisConfig;

    public String getResponseFromRedis(Object requestDTO) throws Exception{

//        long start = System.currentTimeMillis();

        return buildResponseFromRequest(requestDTO);
    }

    public String buildResponseFromRequest(Object requestDTO) throws Exception{
        String request = new Gson().toJson(requestDTO);

        /*Request object parsing*/
        JsonParser parser = new JsonParser();
        JsonElement jsonRequestElement = parser.parse(request);

        JsonObject requestObj = jsonRequestElement.getAsJsonObject();
        JsonObject resultObj = requestObj.getAsJsonObject("result");

        JsonObject parametersObj = resultObj.getAsJsonObject("parameters");
        String inputExperience = parametersObj.get("Experience").getAsString();

        String speechResponse = getSpeechResponse(inputExperience);

        requestObj.addProperty("speech", speechResponse);
        requestObj.addProperty("displayText", speechResponse);
        return requestObj.toString();
    }

    public String getCurrentTimeStamp(){
        DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date date = new Date();
        return dFormat.format(date);
    }

    public String getHealthCheckFromRedis() throws Exception{
        Map<Object, Object> redisMap = redisConnect.getStatusForAllExperiences();
        Map<String, String> expStatusMap = new HashMap<>();

        redisMap.keySet().forEach(key->{
            Experience exp = (Experience)redisMap.get(key);
            expStatusMap.put(exp.getName(), exp.getStatus());
        });

        return new Gson().toJson(expStatusMap);
    }

    public String saveInputExpStatusIntoRedis(Object request) throws Exception{

        String inputJson = new Gson().toJson(request);

        if(util.isJson(inputJson)){
            Type type = new TypeToken<Map<String, Experience>>(){}.getType();
            Map<String, Experience> inputMap = new Gson().fromJson(inputJson, type);

            JsonParser parser = new JsonParser();
            JsonElement jsonRequestElement = parser.parse(inputJson);
            JsonObject requestObj = jsonRequestElement.getAsJsonObject();

            if(redisConnect.saveInputExpStatusIntoRedis(inputMap)){
                requestObj.addProperty("insertStatus", "SUCCESS");
                return requestObj.toString();
            }
            else{
                requestObj.addProperty("insertStatus", "FAILED");
                return requestObj.toString();
            }
        }
        else{
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("Error", "Invalid JSON passed");
            return jsonObject.toString();
        }

    }

    public String deleteInputExpStatusFromRedis(Object request) throws Exception{

        String inputJson = new Gson().toJson(request);

        if(util.isJson(inputJson)){
            Type type = new TypeToken<Map<String, Experience>>(){}.getType();
            Map<String, Experience> inputMap = new Gson().fromJson(inputJson, type);

            JsonParser parser = new JsonParser();
            JsonElement jsonRequestElement = parser.parse(inputJson);
            JsonObject requestObj = jsonRequestElement.getAsJsonObject();

            if(redisConnect.deleteExpStatus(inputMap)){
                requestObj.addProperty("deleteStatus", "SUCCESS");
                return requestObj.toString();
            }
            else{
                requestObj.addProperty("deleteStatus", "FAILED");
                return requestObj.toString();
            }
        }
        else{
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("Error", "Invalid JSON passed");
            return jsonObject.toString();
        }

    }


    public String getSpeechResponse(String inputExperience) throws Exception{

        String[] colors = {"RED", "YELLOW", "GREEN"};

        String healthCheck = getHealthCheckFromRedis();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> inputMap = new Gson().fromJson(healthCheck, type);

        StringBuilder speechResponse = new StringBuilder();

        if(inputExperience.equalsIgnoreCase("order management")){
            Map<String, String> colorMap = new HashMap<>();
            inputMap.keySet().forEach(key-> {
                if(colorMap.containsKey(inputMap.get(key))){
                    String exps = colorMap.get(inputMap.get(key));
                    exps = exps + ", " +key;
                    colorMap.put(inputMap.get(key),exps);
                }
                else{
                    colorMap.put(inputMap.get(key), key);
                }
            });

            for (int i=0;i<colors.length;i++) {
                if(colorMap.containsKey(colors[i])){
                    speechResponse.append(colorMap.get(colors[i]).replace("_", " "));
                    if(colorMap.get(colors[i]).contains(",")){
                        speechResponse.append(" are ");
                    }
                    else{
                        speechResponse.append(" is ");
                    }
                    speechResponse.append(colors[i]+". ");
                }
            }
        }
        else{
            Map<String, String> lowerCaseExpsStatus = new HashMap<>();
            inputMap.keySet().forEach(key->{
                lowerCaseExpsStatus.put(key.toLowerCase().replace("_"," "),inputMap.get(key));
            });

            String inputExperienceLowerCase = "com "+inputExperience.toLowerCase();
            if(lowerCaseExpsStatus.containsValue(inputExperienceLowerCase)){
                inputExperience = "COM "+inputExperience;
                speechResponse.append(inputExperience + " status is "+lowerCaseExpsStatus.get(inputExperienceLowerCase));
            }
            else{
                speechResponse.append(inputExperience + " status is NOT available. Please retry with an available ");
            }
        }

        /*speechResponse.append("That would be all.");*/
/*
        speechResponse.append("All the Experiences are in status GREEN except a few ."+inputMap.get(key)+". ");
        if(inputMap.get(key).equalsIgnoreCase("GREEN")){

            speechResponse.append(key.replace("_", " ") + " status is "+inputMap.get(key)+". ");
        }
*/
        return speechResponse.toString();
    }
}
