package com.example.featureflags.service;

import com.example.featureflags.exception.InvalidPatchRequestException;
import com.example.featureflags.model.CreateFlagRequest;
import com.example.featureflags.model.Environment;
import com.example.featureflags.model.FeatureFlag;
import com.example.featureflags.repository.FeatureFlagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class FeatureFlagService {

    @Autowired
    private FeatureFlagRepository repository;

    public FeatureFlag createFlag(CreateFlagRequest createRequest) {
        FeatureFlag newFlag = new FeatureFlag();
        newFlag.setName(createRequest.getName());
        newFlag.setUpdateDetails(createRequest.getUpdateDetails());
        newFlag.setLastUpdated(Instant.now().toString());

        Map<String, Boolean> defaultEnvironments = new HashMap<>();
        for (Environment env : Environment.values()) {
            defaultEnvironments.put(env.name(), false);
        }
        newFlag.setEnvironmentValues(defaultEnvironments);

        repository.save(newFlag);
        return newFlag;
    }

    private FeatureFlag saveFlag(FeatureFlag flag) {
        flag.setLastUpdated(Instant.now().toString());
        repository.save(flag);
        return flag;
    }

    public Optional<FeatureFlag> getFlag(String name) {
        return Optional.ofNullable(repository.get(name));
    }

    public boolean deleteFlag(String name) {
        Optional<FeatureFlag> flagOptional = getFlag(name);

        if (flagOptional.isPresent()) {
            repository.delete(flagOptional.get());
            return true;
        } else {
            return false;
        }
    }

    public Optional<FeatureFlag> patchFlag(String name, FeatureFlag patchRequest) {
        Optional<FeatureFlag> existingFlagOptional = getFlag(name);

        if (existingFlagOptional.isPresent()) {
            FeatureFlag existingFlag = existingFlagOptional.get();

            if (patchRequest.getEnvironmentValues() != null) {
                Map<String, Boolean> normalizedPatch = new HashMap<>();
                for (Map.Entry<String, Boolean> entry : patchRequest.getEnvironmentValues().entrySet()) {
                    try {
                        String uppercaseKey = entry.getKey().toUpperCase();
                        Environment.valueOf(uppercaseKey); // Validate the key
                        normalizedPatch.put(uppercaseKey, entry.getValue());
                    } catch (IllegalArgumentException e) {
                        // This is the logic that throws the exception handled by your GlobalExceptionHandler.
                        throw new InvalidPatchRequestException(
                            "Invalid environment key in patch request: '" + entry.getKey() +
                                "'. Valid keys are: " + Arrays.toString(Environment.values()));
                    }
                }
                existingFlag.getEnvironmentValues().putAll(normalizedPatch);
            }

            if (patchRequest.getUpdateDetails() != null) {
                existingFlag.setUpdateDetails(patchRequest.getUpdateDetails());
            }

            FeatureFlag updatedFlag = saveFlag(existingFlag);
            return Optional.of(updatedFlag);
        }

        return Optional.empty();
    }
}
