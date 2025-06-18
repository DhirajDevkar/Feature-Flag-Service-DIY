package com.example.featureflags.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.example.featureflags.model.FeatureFlag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FeatureFlagRepository {

    @Autowired
    private DynamoDBMapper mapper;

    public void save(FeatureFlag flag) {
        mapper.save(flag);
    }

    public FeatureFlag get(String name) {
        return mapper.load(FeatureFlag.class, name);
    }

    public void delete(FeatureFlag flag) {
        mapper.delete(flag);
    }
}
