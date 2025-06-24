package com.example.featureflags.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import java.util.Map;

// All complex date-related imports have been removed.

@DynamoDBTable(tableName = "FeatureFlag")
public class FeatureFlag {

    private String name;
    private Map<String, Boolean> environmentValues;
    private String lastUpdated; // Changed from Instant to String
    private String updateDetails;

    @DynamoDBHashKey(attributeName = "name")
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @DynamoDBAttribute(attributeName = "environmentValues")
    public Map<String, Boolean> getEnvironmentValues() { return environmentValues; }
    public void setEnvironmentValues(Map<String, Boolean> environmentValues) { this.environmentValues = environmentValues; }

    // The field is now a simple String. No special annotations are needed.
    @DynamoDBAttribute(attributeName = "lastUpdated")
    public String getLastUpdated() {
        return lastUpdated;
    }
    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @DynamoDBAttribute(attributeName = "updateDetails")
    public String getUpdateDetails() {
        // This fixes a recursive bug that would have caused a StackOverflowError.
        return this.updateDetails;
    }
    public void setUpdateDetails(String updateDetails) {
        this.updateDetails = updateDetails;
    }
}
