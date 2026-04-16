package com.devsuperior.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.demo.entities.OrderItem;
import com.devsuperior.demo.entities.OrderItemPK;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK> {

}