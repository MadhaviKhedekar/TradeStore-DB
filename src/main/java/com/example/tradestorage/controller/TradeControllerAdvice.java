package com.example.tradestorage.controller;

import com.example.tradestorage.exceptionHandler.InvalidTradeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class TradeControllerAdvice extends ResponseEntityExceptionHandler{
    @ExceptionHandler(InvalidTradeException.class)
    public ResponseEntity<String> notFoundException(final InvalidTradeException e) {
        return new ResponseEntity<String>(e.getReason(),HttpStatus.BAD_REQUEST);
    }





}
