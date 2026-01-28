package com.devsuperior.demo.dto;

public class FieldMessage {

    private String fieldName;
    private String Message;
    public FieldMessage(String fieldName, String message) {
        this.fieldName = fieldName;
        Message = message;
    }
    public String getFieldName() {
        return fieldName;
    }
    public String getMessage() {
        return Message;
    }

    
    
}