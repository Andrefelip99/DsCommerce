package com.devsuperior.demo.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.devsuperior.demo.dto.OrderDTO;
import com.devsuperior.demo.dto.OrderItemDTO;
import com.devsuperior.demo.entities.Order;
import com.devsuperior.demo.entities.OrderStatus;
import com.devsuperior.demo.entities.Product;
import com.devsuperior.demo.entities.User;
import com.devsuperior.demo.repository.OrderItemRepository;
import com.devsuperior.demo.repository.OrderRepository;
import com.devsuperior.demo.repository.ProductRepository;
import com.devsuperior.demo.services.exeptions.ForbiddenException;
import com.devsuperior.demo.services.exeptions.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
class OrderServiceTests {
  @Mock
  OrderRepository repository;
  @Mock
  ProductRepository productRepository;
  @Mock
  UserService userService;
  @Mock
  AuthService authService;
  @Mock
  OrderItemRepository orderItemRepository;
  @InjectMocks
  OrderService service;

  @Test
  void findByIdReturnsOrderForAuthorizedOwner() {
    Order order = order(1L, 7L);
    when(repository.findById(Long.valueOf(1L))).thenReturn(Optional.of(order));
    OrderDTO result = service.findById(1L);
    assertEquals(1L, result.getId());
    verify(authService).validateSelfOrAdmin(7L);
  }

  @Test
  void findByIdWhenMissingThrowsNotFound() {
    when(repository.findById(Long.valueOf(1L))).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class, () -> service.findById(1L));
    verifyNoInteractions(authService);
  }

  @Test
  void findByIdPropagatesAuthorizationFailure() {
    when(repository.findById(Long.valueOf(1L))).thenReturn(Optional.of(order(1L, 7L)));
    doThrow(new ForbiddenException("Acess Denied")).when(authService).validateSelfOrAdmin(7L);
    assertThrows(ForbiddenException.class, () -> service.findById(1L));
  }

  @Test
  void insertCreatesWaitingOrderAndPersistsItemsAtProductPrice() {
    User user = new User();
    user.setId(7L);
    user.setName("Cliente");
    when(userService.authenticated()).thenReturn(user);
    Product p = new Product();
    p.setId(3L);
    p.setName("Produto");
    p.setPrice(15.0);
    p.setImgUrl("img");
    when(productRepository.getReferenceById(3L)).thenReturn(p);
    OrderDTO input = new OrderDTO();
    input.getItems().add(new OrderItemDTO(3L, "input", 1.0, 2, "input"));
    when(repository.save(any(Order.class))).thenAnswer(inv -> {
      Order o = inv.getArgument(0);
      o.setId(11L);
      return o;
    });
    OrderDTO result = service.insert(input);
    assertEquals(11L, result.getId());
    assertEquals(OrderStatus.WAITING_PAYMENT, result.getStatus());
    assertEquals(30.0, result.getTotal());
    verify(orderItemRepository)
        .saveAll(argThat(items -> items.iterator().hasNext() && items.iterator().next().getPrice() == 15.0));
  }

  private Order order(Long id, Long clientId) {
    Order o = new Order();
    o.setId(id);
    User c = new User();
    c.setId(clientId);
    c.setName("Cliente");
    o.setClient(c);
    o.setStatus(OrderStatus.PAID);
    return o;
  }
}
