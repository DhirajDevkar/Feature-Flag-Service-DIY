package com.example.featureflags.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.example.featureflags.model.FeatureFlag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FeatureFlagRepositoryTest {

  // Mock the DynamoDBMapper, as we don't want to test the AWS SDK itself.
  @Mock
  private DynamoDBMapper dynamoDBMapper;

  // Inject the mock into our repository instance.
  @InjectMocks
  private FeatureFlagRepository featureFlagRepository;

  private FeatureFlag testFlag;

  @BeforeEach
  void setUp() {
    // Create a sample FeatureFlag object to use in tests.
    testFlag = new FeatureFlag();
    testFlag.setName("test-flag");
  }

  @Test
  void whenSave_thenMapperSaveIsCalled() {
    // Call the method to be tested.
    featureFlagRepository.save(testFlag);

    // Verify that the dynamoDBMapper.save() method was called exactly once
    // with the testFlag object.
    verify(dynamoDBMapper, times(1)).save(testFlag);
  }

  @Test
  void whenGet_thenMapperLoadIsCalled() {
    // Define what the mocked mapper should return when 'load' is called.
    when(dynamoDBMapper.load(FeatureFlag.class, "test-flag")).thenReturn(testFlag);

    // Call the method to be tested.
    FeatureFlag result = featureFlagRepository.get("test-flag");

    // Assert that the returned flag is the one we defined.
    assertEquals("test-flag", result.getName());
    // Verify that the mapper's 'load' method was called.
    verify(dynamoDBMapper, times(1)).load(FeatureFlag.class, "test-flag");
  }

  @Test
  void whenDelete_thenMapperDeleteIsCalled() {
    // Call the method to be tested.
    featureFlagRepository.delete(testFlag);

    // Verify that the dynamoDBMapper.delete() method was called exactly once
    // with the testFlag object.
    verify(dynamoDBMapper, times(1)).delete(testFlag);
  }
}
