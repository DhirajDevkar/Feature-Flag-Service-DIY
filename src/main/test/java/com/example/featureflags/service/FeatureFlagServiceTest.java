package com.example.featureflags.service;

import com.example.featureflags.model.CreateFlagRequest;
import com.example.featureflags.model.Environment;
import com.example.featureflags.model.FeatureFlag;
import com.example.featureflags.repository.FeatureFlagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeatureFlagServiceTest {

  @Mock
  private FeatureFlagRepository repository;

  @InjectMocks
  private FeatureFlagService service;

  private FeatureFlag testFlag;

  @BeforeEach
  void setUp() {
    testFlag = new FeatureFlag();
    testFlag.setName("test-flag");
    Map<String, Boolean> environments = new HashMap<>();
    environments.put(Environment.DEV.name(), true);
    environments.put(Environment.QA.name(), true);
    testFlag.setEnvironmentValues(environments);
    testFlag.setUpdateDetails("Initial details");
  }

  // This is the updated test case that replaces 'whenSaveFlag_thenRepositorySaveIsCalled'
  @Test
  void whenCreateFlag_thenRepositorySaveIsCalledWithDefaults() {
    // --- GIVEN ---
    // A request to create a new flag
    CreateFlagRequest createRequest = new CreateFlagRequest();
    createRequest.setName("new-test-flag");
    createRequest.setUpdateDetails("Creating a new flag for testing");

    // --- WHEN ---
    // The create operation is called
    service.createFlag(createRequest);

    // --- THEN ---
    // Capture the flag passed to the repository's save method
    ArgumentCaptor<FeatureFlag> flagCaptor = ArgumentCaptor.forClass(FeatureFlag.class);
    verify(repository).save(flagCaptor.capture());
    FeatureFlag savedFlag = flagCaptor.getValue();

    // Assert that the saved flag has the correct properties
    assertEquals("new-test-flag", savedFlag.getName());
    assertEquals("Creating a new flag for testing", savedFlag.getUpdateDetails());
    assertNotNull(savedFlag.getLastUpdated());
    // Verify that all environments from the Enum are present and set to false
    assertEquals(4, savedFlag.getEnvironmentValues().size());
    assertFalse(savedFlag.getEnvironmentValues().get(Environment.DEV.name()));
    assertFalse(savedFlag.getEnvironmentValues().get(Environment.QA.name()));
    assertFalse(savedFlag.getEnvironmentValues().get(Environment.UAT.name()));
    assertFalse(savedFlag.getEnvironmentValues().get(Environment.PROD.name()));
  }

  @Test
  void whenGetFlag_thenRepositoryGetIsCalled() {
    // Stub the repository's get method to return our test flag.
    when(repository.get("test-flag")).thenReturn(testFlag);

    // Call the service method.
    Optional<FeatureFlag> result = service.getFlag("test-flag");

    // Assert the result is correct.
    assertTrue(result.isPresent());
    assertEquals("test-flag", result.get().getName());
    // Verify the service called the repository's get method.
    verify(repository, times(1)).get("test-flag");
  }

  @Test
  void whenDeleteFlag_thenRepositoryMethodsAreCalled() {
    // Stub the repository's get method, as deleteFlag first fetches the flag.
    when(repository.get("test-flag")).thenReturn(testFlag);

    // Call the service method.
    service.deleteFlag("test-flag");

    // Verify that the service first fetched the flag and then deleted it.
    verify(repository, times(1)).get("test-flag");
    verify(repository, times(1)).delete(testFlag);
  }

  @Test
  void whenPatchFlag_thenExistingFlagIsUpdated() {
    // --- GIVEN ---
    // An existing flag in the database
    when(repository.get("test-flag")).thenReturn(testFlag);

    // A patch request with new values
    FeatureFlag patchRequest = new FeatureFlag();
    Map<String, Boolean> patchEnvironments = new HashMap<>();
    patchEnvironments.put(Environment.QA.name(), false);
    patchEnvironments.put(Environment.PROD.name(), true);
    patchRequest.setEnvironmentValues(patchEnvironments);
    patchRequest.setUpdateDetails("New patch details");

    // --- WHEN ---
    service.patchFlag("test-flag", patchRequest);

    // --- THEN ---
    ArgumentCaptor<FeatureFlag> flagCaptor = ArgumentCaptor.forClass(FeatureFlag.class);
    verify(repository).save(flagCaptor.capture());
    FeatureFlag savedFlag = flagCaptor.getValue();

    // Assert that the merged values are correct
    assertEquals(3, savedFlag.getEnvironmentValues().size());
    assertTrue(savedFlag.getEnvironmentValues().get(Environment.DEV.name()));
    assertFalse(savedFlag.getEnvironmentValues().get(Environment.QA.name()));
    assertTrue(savedFlag.getEnvironmentValues().get(Environment.PROD.name()));
    assertEquals("New patch details", savedFlag.getUpdateDetails());
    assertNotNull(savedFlag.getLastUpdated());
  }
}
