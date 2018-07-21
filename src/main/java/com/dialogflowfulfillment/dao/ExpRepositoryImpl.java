package com.dialogflowfulfillment.dao;

import com.dialogflowfulfillment.model.ExpRepository;
import com.dialogflowfulfillment.model.Experience;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Map;

@Repository
public class ExpRepositoryImpl implements ExpRepository {

    private static final String KEY = "Experience";

    private RedisTemplate<String, Experience> redisTemplate;
    private HashOperations hashOps;

    @Autowired
    public ExpRepositoryImpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init() {
        hashOps = redisTemplate.opsForHash();
    }

    public void saveExperience(Experience Experience) {
        hashOps.put(KEY, Experience.getName(), Experience);
    }

    public void updateExperience(Experience Experience) {
        hashOps.put(KEY, Experience.getName(), Experience);
    }

    public Experience findExperience(String name) {
        return (Experience) hashOps.get(KEY, name);
    }

    public Map<Object, Object> findAllExperiences() {
        return hashOps.entries(KEY);
    }

    public void deleteExperience(String name) {
        hashOps.delete(KEY, name);
    }
}
