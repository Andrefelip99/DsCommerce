package com.devsuperior.demo.services.exeptions;

public class DatabaseException extends RuntimeException{

    public DatabaseException(String msg){
        super(msg);
    }
    
}