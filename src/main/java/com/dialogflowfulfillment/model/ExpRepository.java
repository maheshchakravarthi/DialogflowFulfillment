package com.dialogflowfulfillment.model;

import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface ExpRepository {

    void saveExperience(Experience exp);

    void updateExperience(Experience exp);

    Experience findExperience(String name);

    Map<Object, Object> findAllExperiences();

    void deleteExperience(String name);
}
