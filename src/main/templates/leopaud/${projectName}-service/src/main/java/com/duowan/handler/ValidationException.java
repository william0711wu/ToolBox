package com.duowan.handler;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

/**
 *
 */
public class ValidationException extends RuntimeException {
    private BindingResult bindingResult;

    public ValidationException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }

    @Override
    public String getMessage(){
        StringBuilder sb = new StringBuilder();
        for(ObjectError oe : bindingResult.getAllErrors()){
            sb.append(oe.getDefaultMessage()+"<br/>");
        }
        return sb.toString();
    }

    public static void checkBindingResult( BindingResult bindingResult){
        if(bindingResult.hasErrors())
            throw new ValidationException(bindingResult);
    }
}
