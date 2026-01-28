package com.devsuperior.demo.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ValidationError extends CustomError {

    private List<FieldMessage> errors = new ArrayList<>();

    public ValidationError(Instant timeStamp, Integer status, String error, String path, List<FieldMessage> errors) {
        super(timeStamp, status, error, path);
        this.errors = errors;
    }

    public List<FieldMessage> getErrors() {
        return errors;
    }

    public void addError(String FieldName, String Message){
        errors.add(new FieldMessage(FieldName, Message));

    }

    
    
}