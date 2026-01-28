package com.devsuperior.demo.controllers.hendlers;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.devsuperior.demo.dto.CustomError;
import com.devsuperior.demo.dto.ValidationError;
import com.devsuperior.demo.services.exeptions.DataBaseExeption;
import com.devsuperior.demo.services.exeptions.ResourceNotFouldExeption;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ResourceNotFouldExeption.class)
public ResponseEntity<CustomError> resourceNotFould(ResourceNotFouldExeption e, HttpServletRequest request) {
HttpStatus status = HttpStatus.NOT_FOUND;
CustomError err = new CustomError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
return ResponseEntity.status(status).body(err);
}



    @ExceptionHandler(DataBaseExeption.class)
public ResponseEntity<CustomError> dataBase(DataBaseExeption e, HttpServletRequest request) {
HttpStatus status = HttpStatus.BAD_REQUEST;
CustomError err = new CustomError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
return ResponseEntity.status(status).body(err);
}


 @ExceptionHandler(MethodArgumentNotValidException
.class)
public ResponseEntity<CustomError> methodArgumentNotValid(MethodArgumentNotValidException
e, HttpServletRequest request) {
HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
ValidationError err = new ValidationError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI(), null);
err.addError("name", "Mensagem de teste");
err.addError("price", "Preço invalido");
return ResponseEntity.status(status).body(err);
}
 

}
