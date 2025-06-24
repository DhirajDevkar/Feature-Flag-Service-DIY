package com.example.featureflags.security;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApiKeyFilter {

    @Autowired
    private AWSSecretsManager secretsManagerClient;

    @Value("${aws.secret.name}")
    private String secretName;

    public String getApiKey() {
        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
            .withSecretId(secretName);
        GetSecretValueResult getSecretValueResult = secretsManagerClient.getSecretValue(getSecretValueRequest);
        return getSecretValueResult.getSecretString();
    }
}