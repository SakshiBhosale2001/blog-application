package com.blog.exception;
import com.blog.playload.ApiResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException resourceNotFoundEx)
    {

        return new ResponseEntity<>(new ApiResponse(resourceNotFoundEx.getMessage(),false)
                , HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> MethodArgumentNotValidException(MethodArgumentNotValidException ex){
        Map<String,String> map=new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError ->{
            String field=fieldError.getField();
            String message=fieldError.getDefaultMessage();
            map.put(field,message);
        });

        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MethodArgumentConversionNotSupportedException.class)
    public ResponseEntity<Map<String,String>> argConversonNotSupportedEx(MethodArgumentConversionNotSupportedException ex) {
        Map<String, String> map = new HashMap<>();

        map.put(ex.getPropertyName(), ex.getLocalizedMessage());
        return new ResponseEntity<>(map, HttpStatus.NOT_IMPLEMENTED);

    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<Map<String,String>> handleTransactionSystemException(TransactionSystemException ex) {

        Map<String, String> map = new HashMap<>();

        map.put(ex.getMostSpecificCause().toString(), ex.getLocalizedMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(map);
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String,String>> DataIntegrityViolationException(DataIntegrityViolationException ex) {

        Map<String, String> errorDetails = new HashMap<>();

        errorDetails.put("error", "Data integrity violation");
        errorDetails.put("details", "A constraint was violated, such as a duplicate entry or a foreign key constraint. Or Entity related problem ");

        errorDetails.put("message", ex.getRootCause().getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorDetails);
    }



}
