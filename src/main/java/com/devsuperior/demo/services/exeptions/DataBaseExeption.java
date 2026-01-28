package com.devsuperior.demo.services.exeptions;

public class DataBaseExeption extends RuntimeException{

    public DataBaseExeption(String msg){
        super(msg);
    }
    
}