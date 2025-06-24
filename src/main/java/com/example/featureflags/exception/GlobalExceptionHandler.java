package com.example.featureflags.exception;

import com.example.featureflags.model.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

// This annotation makes this class a global exception handler for the entire application.
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  // This method specifically handles our custom InvalidPatchRequestException.
  @ExceptionHandler(InvalidPatchRequestException.class)
  public final ResponseEntity<ApiResponse> handleInvalidPatchRequest(InvalidPatchRequestException ex) {
    // Create a consistent API response for the error.
    ApiResponse errorResponse = new ApiResponse(ex.getMessage());
    // Return a 400 Bad Request status with the error message.
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }
}
