package com.app.lika.exception;

import com.app.lika.payload.response.APIResponse;
import com.app.lika.payload.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ControllerAdvice
public class RestControllerExceptionHandler {

    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse> resolveException(APIException exception) {
        String message = exception.getMessage();
        HttpStatus status = exception.getStatus();
        Integer code = exception.getCode();

        APIResponse apiResponse = new APIResponse(code, message);

        return new ResponseEntity<>(apiResponse, status);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<APIResponse> resolveException(BadRequestException exception) {
        APIResponse apiResponse = exception.getApiResponse();

        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ResponseEntity<APIResponse> resolveException(UnauthorizedException exception) {
        APIResponse apiResponse = exception.getApiResponse();

        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    public ResponseEntity<APIResponse> resolveException(ResourceNotFoundException exception) {
        APIResponse apiResponse = exception.getApiResponse();

        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IntegrityConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<APIResponse> resolveException(IntegrityConstraintViolationException exception) {
        APIResponse apiResponse = exception.getApiResponse();

        return new ResponseEntity<>(apiResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public ResponseEntity<APIResponse> resolveException(AccessDeniedException exception) {
        APIResponse apiResponse = exception.getApiResponse();

        return new ResponseEntity<>(apiResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<APIResponse> resolveException(org.springframework.security.access.AccessDeniedException exception) {
        APIResponse apiResponse = new APIResponse();
        apiResponse.setMessage(exception.getMessage());
        apiResponse.setErrorCode(HttpStatus.FORBIDDEN.value());

        return new ResponseEntity<>(apiResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<APIResponse> resolveException(MethodArgumentNotValidException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        String message = fieldErrors.get(0).getField() + "-" + fieldErrors.get(0).getDefaultMessage();
        APIResponse apiResponse = new APIResponse<>(HttpStatus.BAD_REQUEST.value(), message);

        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<APIResponse> resolveException(MethodArgumentTypeMismatchException ex) {
        String message = "Parameter '" + ex.getParameter().getParameterName() + "' must be '"
                + Objects.requireNonNull(ex.getRequiredType()).getSimpleName() + "'";
        APIResponse apiResponse = new APIResponse<>(HttpStatus.BAD_REQUEST.value(), message);

        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    public ResponseEntity<APIResponse> resolveException(HttpRequestMethodNotSupportedException ex) {
        String message = "Request method '" + ex.getMethod() + "' not supported. List of all supported methods - "
                + ex.getSupportedHttpMethods();
        APIResponse apiResponse = new APIResponse<>(HttpStatus.METHOD_NOT_ALLOWED.value(), message);

        return new ResponseEntity<>(apiResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<APIResponse> resolveException(HttpMessageNotReadableException ex) {
        String message = "Please provide Request Body in valid JSON format";
        APIResponse apiResponse = new APIResponse<>(HttpStatus.BAD_REQUEST.value(), message);

        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }
}
