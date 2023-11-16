package com.app.lika.exception;

import com.app.lika.payload.response.APIMessageResponse;
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
    public ResponseEntity<APIMessageResponse> resolveException(APIException exception) {
        String message = exception.getMessage();
        HttpStatus status = exception.getStatus();
        Integer code = exception.getCode();

        APIMessageResponse apiMessageResponse = new APIMessageResponse();
        apiMessageResponse.setSuccess(Boolean.FALSE);
        apiMessageResponse.setErrorCode(code);
        apiMessageResponse.setMessage(message);

        return new ResponseEntity<>(apiMessageResponse, status);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<APIMessageResponse> resolveException(BadRequestException exception) {
        APIMessageResponse apiMessageResponse = exception.getApiMessageResponse();

        return new ResponseEntity<>(apiMessageResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ResponseEntity<APIMessageResponse> resolveException(UnauthorizedException exception) {

        APIMessageResponse apiResponse = exception.getApiResponse();

        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    public ResponseEntity<APIMessageResponse> resolveException(ResourceNotFoundException exception) {
        APIMessageResponse apiResponse = exception.getApiResponse();

        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IntegrityConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<APIMessageResponse> resolveException(IntegrityConstraintViolationException exception){
        APIMessageResponse apiResponse = exception.getApiResponse();

        return new ResponseEntity<>(apiResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public ResponseEntity<APIMessageResponse> resolveException(AccessDeniedException exception) {
        APIMessageResponse apiResponse = exception.getApiMessageResponse();

        return new ResponseEntity<>(apiResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<APIMessageResponse> resolveException(org.springframework.security.access.AccessDeniedException exception) {
        APIMessageResponse apiResponse = new APIMessageResponse();
        apiResponse.setMessage(exception.getMessage());
        apiResponse.setError(HttpStatus.FORBIDDEN.getReasonPhrase());

        return new ResponseEntity<>(apiResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionResponse> resolveException(MethodArgumentNotValidException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        List<String> messages = new ArrayList<>(fieldErrors.size());
        for (FieldError error : fieldErrors) {
            messages.add(error.getField() + "-" + error.getDefaultMessage());
        }
        return new ResponseEntity<>(new ExceptionResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST.value(), messages), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionResponse> resolveException(MethodArgumentTypeMismatchException ex) {
        String message = "Parameter '" + ex.getParameter().getParameterName() + "' must be '"
                + Objects.requireNonNull(ex.getRequiredType()).getSimpleName() + "'";
        List<String> messages = new ArrayList<>(1);
        messages.add(message);
        return new ResponseEntity<>(new ExceptionResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                HttpStatus.BAD_REQUEST.value(), messages), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> resolveException(HttpRequestMethodNotSupportedException ex) {
        String message = "Request method '" + ex.getMethod() + "' not supported. List of all supported methods - "
                + ex.getSupportedHttpMethods();
        List<String> messages = new ArrayList<>(1);
        messages.add(message);

        return new ResponseEntity<>(new ExceptionResponse(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(),
                HttpStatus.METHOD_NOT_ALLOWED.value(), messages), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionResponse> resolveException(HttpMessageNotReadableException ex) {
        String message = "Please provide Request Body in valid JSON format";
        List<String> messages = new ArrayList<>(1);
        messages.add(message);
        return new ResponseEntity<>(new ExceptionResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                HttpStatus.BAD_REQUEST.value(), messages), HttpStatus.BAD_REQUEST);
    }
}
