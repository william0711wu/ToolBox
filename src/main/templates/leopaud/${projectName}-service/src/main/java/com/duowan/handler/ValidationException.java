package com.duowan.handler;

import com.duowan.leopard.json.Json;
import com.google.common.collect.Lists;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

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
        List<String> errList = Lists.newArrayList();
        for(FieldError fe : bindingResult.getFieldErrors()){
            errList.add(fe.getDefaultMessage()+"\n");
        }
        return Json.toJson(errList);
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }

    public static void checkBindingResult( BindingResult bindingResult){
        if(bindingResult.hasErrors())
            throw new ValidationException(bindingResult);
    }

}
