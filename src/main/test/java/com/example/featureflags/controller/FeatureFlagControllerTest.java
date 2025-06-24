package com.example.featureflags.controller;

import com.example.featureflags.model.FeatureFlag;
import com.example.featureflags.service.FeatureFlagService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

// Use @WebMvcTest to test only the controller layer.
@WebMvcTest(FeatureFlagController.class)
class FeatureFlagControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private FeatureFlagService service;

  @Autowired
  private ObjectMapper objectMapper;

  private FeatureFlag testFlag;

  @BeforeEach
  void setUp() {
    testFlag = new FeatureFlag();
    testFlag.setName("new-ui");
    testFlag.setEnvironmentValues(new HashMap<>());
  }

  @Test
  void whenPostFlag_thenReturns200() throws Exception {
    // Perform a POST request to /flags
    mockMvc.perform(post("/flags")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(testFlag)))
        // Expect a 200 OK status.
        .andExpect(status().is2xxSuccessful());
  }

  @Test
  void whenGetFlagByName_thenReturnsFlag() throws Exception {
    // Stub the service method.
    when(service.getFlag("new-ui")).thenReturn(java.util.Optional.ofNullable(testFlag));

    // Perform a GET request.
    mockMvc.perform(get("/flags/{name}", "new-ui"))
        // Expect a 200 OK status.
        .andExpect(status().isOk())
        // Expect the response body to contain a JSON object with the correct name.
        .andExpect(jsonPath("$.name", is("new-ui")));
  }

  @Test
  void whenGetNonExistentFlag_thenReturnsNotFound() throws Exception {
    // --- GIVEN ---
    when(service.getFlag("not-found")).thenReturn(Optional.empty());

    // --- WHEN/THEN ---
    mockMvc.perform(get("/flags/{name}", "not-found"))
        // Expect a 404 Not Found status.
        .andExpect(status().isNotFound())
        // Expect the correct error message in the response body.
        .andExpect(jsonPath("$.message", is("Feature flag not found: not-found")));
  }

  @Test
  void whenDeleteFlag_thenReturns200() throws Exception {
    // Perform a DELETE request.
    mockMvc.perform(delete("/flags/{name}", "new-ui"))
        // Expect a 200 OK status.
        .andExpect(status().is2xxSuccessful());
  }

  @Test
  void whenPatchFlag_thenReturnsUpdatedFlag() throws Exception {
    // --- GIVEN ---
    // 1. A patch request body
    Map<String, Boolean> patchEnvironments = new HashMap<>();
    patchEnvironments.put("prod", true);
    FeatureFlag patchRequest = new FeatureFlag();
    patchRequest.setEnvironmentValues(patchEnvironments);

    // 2. An updated flag that the service will return
    FeatureFlag updatedFlag = new FeatureFlag();
    updatedFlag.setName("new-ui");
    updatedFlag.setEnvironmentValues(patchEnvironments);
    updatedFlag.setUpdateDetails("Updated details");

    // 3. Stub the service call
    when(service.patchFlag(eq("new-ui"), any(FeatureFlag.class))).thenReturn(Optional.of(updatedFlag));

    // --- WHEN/THEN ---
    mockMvc.perform(patch("/flags/{name}", "new-ui")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(patchRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is("new-ui")))
        .andExpect(jsonPath("$.environmentValues.prod", is(true)))
        .andExpect(jsonPath("$.updateDetails", is("Updated details")));
  }

  @Test
  void whenPatchNonExistentFlag_thenReturnsNotFound() throws Exception {
    // --- GIVEN ---
    // 1. A patch request body
    FeatureFlag patchRequest = new FeatureFlag();

    // 2. Stub the service to return an empty Optional, simulating not found
    when(service.patchFlag(eq("non-existent"), any(FeatureFlag.class))).thenReturn(Optional.empty());

    // --- WHEN/THEN ---
    mockMvc.perform(patch("/flags/{name}", "non-existent")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(patchRequest)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message", is("Feature flag not found: non-existent")));
  }
}
