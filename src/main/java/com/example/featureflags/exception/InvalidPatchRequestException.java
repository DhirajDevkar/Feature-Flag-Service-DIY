package com.example.featureflags.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// This annotation ensures that if this exception is unhandled, it will result in a 400 Bad Request status.
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPatchRequestException extends RuntimeException {
  public InvalidPatchRequestException(String message) {
    super(message);
  }
}
