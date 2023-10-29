package com.sublime.ecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidTotalPriceException extends RuntimeException{
    public InvalidTotalPriceException(String message){
        super(message);
    }
}
