package com.example.featureflags.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

@DynamoDBTable(tableName = "FeatureFlag")
public class FeatureFlag {

    private String name;
    private boolean enabled;

    @DynamoDBHashKey(attributeName = "name")
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @DynamoDBAttribute(attributeName = "enabled")
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
