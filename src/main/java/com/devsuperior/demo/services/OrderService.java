package com.devsuperior.demo.services;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.demo.dto.OrderDTO;
import com.devsuperior.demo.dto.OrderItemDTO;
import com.devsuperior.demo.entities.Order;
import com.devsuperior.demo.entities.OrderItem;
import com.devsuperior.demo.entities.OrderStatus;
import com.devsuperior.demo.entities.Product;
import com.devsuperior.demo.entities.User;
import com.devsuperior.demo.repository.OrderItemRepository;
import com.devsuperior.demo.repository.OrderRepository;
import com.devsuperior.demo.repository.ProductRepository;
import com.devsuperior.demo.services.exeptions.ResourceNotFoundException;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired 
    private UserService userService;

    @Autowired 
    private AuthService authService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id) {
        Order entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado"));
                authService.validateSelfOrAdmin(entity.getClient().getId());

        return new OrderDTO(entity);
    }

    @Transactional
    public OrderDTO insert(OrderDTO dto) {
        Order order = new Order();
        order.setMoment(Instant.now());
        order.setStatus(OrderStatus.WAITING_PAYMENT);

        User user = userService.authenticated();
        order.setClient(user);

        for( OrderItemDTO itemDTO : dto.getItems()) {
            Product product = productRepository.getReferenceById(itemDTO.getProductId());

            OrderItem item = new OrderItem(order, product, itemDTO.getQuantity(), product.getPrice());
            order.getItems().add(item);
        }
        repository.save(order);
        orderItemRepository.saveAll(order.getItems());
        return new OrderDTO(order);
    }
}