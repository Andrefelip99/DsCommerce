package com.devsuperior.demo.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsuperior.demo.dto.ProductDTO;

import com.devsuperior.demo.services.ProductService;

@RestController
@RequestMapping(value = "/products")
public class ProductsController {

    @Autowired
    private ProductService service;


    @GetMapping(value = "/{id}")
    public ProductDTO findyById(@PathVariable Long id){
        ProductDTO dto = service.findyById(id);
        return dto;
    }
}
