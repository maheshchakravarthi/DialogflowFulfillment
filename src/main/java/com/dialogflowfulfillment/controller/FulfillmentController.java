package com.dialogflowfulfillment.controller;

import com.google.gson.Gson;
import com.dialogflowfulfillment.delegate.FulfillmentManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.GregorianCalendar;

@RestController
public class FulfillmentController {

    private static Logger logger = LoggerFactory.getLogger(FulfillmentController.class);

    @Autowired
    private FulfillmentManager fulfillmentManager;

    @RequestMapping(value="/fulfillmentEngine", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity fulfillmentEngine(@RequestBody Object requestDTO){

        logger.info(GregorianCalendar.getInstance().getTime()+ " Input request : "+new Gson().toJson(requestDTO));
        try{

            String response = fulfillmentManager.getResponseFromRedis(requestDTO);
            logger.info("Response : "+response);
            return ResponseEntity.status(200).body(response);
        }
        catch (Exception e) {
            logger.info("Exception : "+e.getStackTrace());
            e.printStackTrace();
            return ResponseEntity.status(404).body(new Gson().toJson(e.getMessage()));
        }
    }

    @RequestMapping(value= "/getHealthCheck", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getHealthCheck(){

        try{
            String response = fulfillmentManager.getHealthCheckFromRedis();
            logger.info("getHealthCheck Response : "+response);
            return ResponseEntity.status(200).body(response);
        }
        catch (Exception e) {
            return ResponseEntity.status(404).body(new Gson().toJson(e.getMessage()));
        }
    }

    @RequestMapping(value= "/saveHealthCheck", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity saveHealthCheck(@RequestBody Object request){

        logger.info(GregorianCalendar.getInstance().getTime()+ " Input request : "+new Gson().toJson(request));

        try{
            String response = fulfillmentManager.saveInputExpStatusIntoRedis(request);
            logger.info("Response : "+response);
            return ResponseEntity.status(200).body(response);
        }
        catch (Exception e) {
            logger.error("Exception : "+e.getStackTrace());
            e.printStackTrace();
            return ResponseEntity.status(404).body(new Gson().toJson(e.getMessage()));
        }
    }

    @RequestMapping(value="/deleteHealthCheck", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteHealthCheck(@RequestBody Object request){

        logger.info(GregorianCalendar.getInstance().getTime()+ " Input request : "+new Gson().toJson(request));
        try{
            String response = fulfillmentManager.deleteInputExpStatusFromRedis(request);
            logger.info("Response : "+response);
            return ResponseEntity.status(200).body(response);
        }
        catch (Exception e) {
            logger.error("Exception : "+e.getStackTrace());
            e.printStackTrace();
            return ResponseEntity.status(404).body(new Gson().toJson(e.getMessage()));
        }
    }
}
