package org.jconf.mx.springday.resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jconf.mx.springday.application.InvalidTransactionException;
import org.jconf.mx.springday.application.ProfileException;
import org.jconf.mx.springday.application.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResourceErrorHandler {

  @ExceptionHandler(ProfileException.class)
  public ResponseEntity<JsonNode> handleNoEngagementError(ProfileException ex) {
    return createErrorResponse(ex.getMessage(), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(InvalidTransactionException.class)
  public ResponseEntity<JsonNode> handleTransactionError(InvalidTransactionException ex) {
    return createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<JsonNode> handleNotFoundError(ResourceNotFoundException ex) {
    return createErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  private ObjectNode createBaseError(Object message) {
    ObjectNode errorResponse = JsonNodeFactory.instance.objectNode();
    errorResponse.putPOJO("message", message);
    return errorResponse;
  }

  private ResponseEntity<JsonNode> createErrorResponse(Object message, HttpStatus status) {
    ObjectNode errorResponse = createBaseError(message);
    return new ResponseEntity<>(errorResponse, status);
  }
}
