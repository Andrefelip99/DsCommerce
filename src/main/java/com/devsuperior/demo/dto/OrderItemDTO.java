package com.devsuperior.demo.dto;

import com.devsuperior.demo.entities.OrderItem;

import lombok.Getter;

@Getter
public class OrderItemDTO {
    private Long productId;
    private String name;
    private double price;
    private Integer quantity;


    public OrderItemDTO(Long productId, String name, double price, Integer quantity) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public OrderItemDTO(OrderItem entity) {
        productId = entity.getProduct().getId();
        name = entity.getProduct().getName();
        price = entity.getPrice();
        quantity = entity.getQuantity();
    }

    public OrderItemDTO() {
        
    }

    public double getSubTotal() {
        return price * quantity;
    }
    
}