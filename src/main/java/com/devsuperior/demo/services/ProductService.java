package com.devsuperior.demo.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.demo.dto.ProductDTO;
import com.devsuperior.demo.entities.Product;
import com.devsuperior.demo.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Transactional(readOnly = true)
    public ProductDTO findyById(Long id) {
        Product product = repository.findById(id).get();
        return new ProductDTO(product.getId(), product.getName(), product.getDescription(),
                product.getPrice(),
                product.getImgUrl());
       

    }

}