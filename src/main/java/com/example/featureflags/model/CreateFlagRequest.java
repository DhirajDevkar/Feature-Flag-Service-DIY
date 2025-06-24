package com.example.featureflags.model;

// A Data Transfer Object (DTO) for creating a new feature flag.
public class CreateFlagRequest {
  private String name;
  private String updateDetails;

  // Getters and setters are required for Jackson to deserialize the request body.
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUpdateDetails() {
    return updateDetails;
  }

  public void setUpdateDetails(String updateDetails) {
    this.updateDetails = updateDetails;
  }
}
