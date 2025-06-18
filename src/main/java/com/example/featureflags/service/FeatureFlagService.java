package com.example.featureflags.service;

import com.example.featureflags.model.FeatureFlag;
import com.example.featureflags.repository.FeatureFlagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeatureFlagService {

    @Autowired
    private FeatureFlagRepository repository;

    public void saveFlag(FeatureFlag flag) {
        repository.save(flag);
    }

    public FeatureFlag getFlag(String name) {
        return repository.get(name);
    }

    public void deleteFlag(String name) {
        FeatureFlag flag = getFlag(name);
        if (flag != null) {
            repository.delete(flag);
        }
    }
}
