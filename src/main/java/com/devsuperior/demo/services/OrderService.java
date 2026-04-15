package com.devsuperior.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.demo.dto.OrderDTO;
import com.devsuperior.demo.entities.Order;
import com.devsuperior.demo.repository.OrderRepository;
import com.devsuperior.demo.services.exeptions.ResourceNotFoundException;


@Service
public class OrderService {

    @Autowired 
    private OrderRepository repository;
    
     @Transactional(readOnly = true)
    public OrderDTO findById(Long id) {
        Order entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado"));
        return new OrderDTO(entity);
    }
}