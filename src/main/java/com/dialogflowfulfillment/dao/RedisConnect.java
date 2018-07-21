package com.dialogflowfulfillment.dao;

import com.dialogflowfulfillment.model.Experience;
import com.dialogflowfulfillment.model.ExpRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class RedisConnect {

    @Autowired
    RedisTemplate<String, Experience> redisTemplate;

    @Autowired
    ExpRepository expRepository;

    public void saveExpStatusIntoRedis(List<Experience> expStatusList){

        expStatusList.forEach(experience -> {
            expRepository.saveExperience(experience);
        });

        /*Experience exp = new Experience("COM_Platform", "GREEN");*/

        /*ExpRepository expRepository = new ExpRepositoryImpl(redisTemplate);*/

        /*expRepository.saveExperience(exp);

        System.out.println("successfully inserted");*/

    }

    public Map<Object, Object> getStatusForAllExperiences(){

        Map<Object, Object> resultMap = expRepository.findAllExperiences();
        return resultMap;
    }

    public boolean saveInputExpStatusIntoRedis(Map<String, Experience> inputHealthCheckData){

        boolean insertStatus = false;
        inputHealthCheckData.keySet().forEach(key -> {
            expRepository.saveExperience(inputHealthCheckData.get(key));
        });
        insertStatus = true;

        return insertStatus;
    }

    public boolean deleteExpStatus(Map<String, Experience> inputExp){

        boolean deleteStatus = false;
        inputExp.entrySet().forEach(entry -> {
            expRepository.deleteExperience(entry.getValue().getName());
        });
        deleteStatus = true;

        return deleteStatus;
    }



}