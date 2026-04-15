package com.devsuperior.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsuperior.demo.dto.OrderDTO;
import com.devsuperior.demo.services.OrderService;

@RestController
@RequestMapping(value = "/orders")
public class OrderController {

    @Autowired
    private OrderService service;

    // Aqui eu estou Buscando por id//
   @PreAuthorize("hasAnyRole('ROLE_ADMIN')") // Somente Admin e Operator podem acessar esse endpoint
    @GetMapping(value = "/{id}")
    public ResponseEntity<OrderDTO> findyById(@PathVariable Long id) {
        OrderDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);

    }
}