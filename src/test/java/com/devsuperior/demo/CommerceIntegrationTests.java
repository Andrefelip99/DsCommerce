package com.devsuperior.demo;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devsuperior.demo.entities.Category;
import com.devsuperior.demo.entities.Product;
import com.devsuperior.demo.dto.ProductDTO;
import com.devsuperior.demo.repository.CategoryRepository;
import com.devsuperior.demo.repository.ProductRepository;
import com.devsuperior.demo.repository.OrderRepository;
import com.devsuperior.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import jakarta.validation.Validator;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CommerceIntegrationTests {
        @Autowired
        MockMvc mockMvc;
        @Autowired
        CategoryRepository categoryRepository;
        @Autowired
        ProductRepository productRepository;
        @Autowired
        Validator validator;
        @Autowired
        OrderRepository orderRepository;
        @Autowired
        UserRepository userRepository;

        @Test
        void categoriesEndpointReturnsPersistedCategories() throws Exception {
                Category category = categoryRepository.findAll().get(0);
                mockMvc.perform(get("/categories"))
                                .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(greaterThan(0))))
                                .andExpect(jsonPath("$[0].id").value(category.getId()));
        }

        @Test
        void productEndpointsReadPersistedProductAndSearchByName() throws Exception {
                Product product = productRepository.save(product("Integration Product", 42.5));
                mockMvc.perform(get("/products/{id}", product.getId()))
                                .andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Integration Product"));
                mockMvc.perform(get("/products").param("name", "INTEGRATION").param("page", "0").param("size", "10"))
                                .andExpect(status().isOk()).andExpect(jsonPath("$.content", hasSize(1)))
                                .andExpect(jsonPath("$.content[0].price").value(42.5));
        }

        @Test
        void adminCanCreateProductAndProductIsPersisted() throws Exception {
                Category category = categoryRepository.findAll().get(0);
                long previousCount = productRepository.count();
                String body = """
                                {"name":"New Product","description":"Description long enough for valid creation","price":35.0,"imgUrl":"image","categories":[{"id":%d,"name":"Category"}]}
                                """
                                .formatted(category.getId());
                mockMvc
                                .perform(post("/products")
                                                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                                                .contentType(MediaType.APPLICATION_JSON).content(body))
                                .andExpect(status().isCreated())
                                .andExpect(header().string("Location", containsString("/products/")))
                                .andExpect(jsonPath("$.id", notNullValue()))
                                .andExpect(jsonPath("$.name").value("New Product"));
                org.junit.jupiter.api.Assertions.assertEquals(previousCount + 1, productRepository.count());
        }

        @Test
        void invalidProductViolatesBusinessInputConstraints() {
                ProductDTO dto = new ProductDTO(1L, "x", "short", -2.0, "image");
                var fields = validator.validate(dto).stream().map(v -> v.getPropertyPath().toString())
                                .collect(java.util.stream.Collectors.toSet());
                assertTrue(fields.containsAll(java.util.Set.of("name", "description", "price", "categories")));
        }

        @Test
        void clientCannotCreateProduct() throws Exception {
                mockMvc.perform(post("/products").with(jwt().authorities(new SimpleGrantedAuthority("ROLE_CLIENT")))
                                .contentType(MediaType.APPLICATION_JSON).content(
                                                "{\"name\":\"Valid product\",\"description\":\"Description long enough\",\"price\":12.0,\"imgUrl\":\"image\",\"categories\":[{\"id\":1,\"name\":\"Livros\"}]}"))
                                .andExpect(status().isForbidden());
        }

        @Test
        void clientCreatesOrderAndReadsItBack() throws Exception {
                long previousCount = orderRepository.count();
                String body = """
                                {"items":[{"productId":1,"name":"ignored by service","price":0.0,"quantity":2,"imageUrl":"ignored"}]}
                                """;
                var client = jwt().jwt(token -> token.claim("username", "maria@gmail.com"))
                                .authorities(new SimpleGrantedAuthority("ROLE_CLIENT"));
                mockMvc.perform(post("/orders").with(client).contentType(MediaType.APPLICATION_JSON).content(body))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.status").value("WAITING_PAYMENT"))
                                .andExpect(jsonPath("$.total").value(181.0));
                long orderId = orderRepository.findAll().stream().map(com.devsuperior.demo.entities.Order::getId)
                                .max(Long::compareTo).orElseThrow();
                org.junit.jupiter.api.Assertions.assertEquals(previousCount + 1, orderRepository.count());
                mockMvc.perform(get("/orders/{id}", orderId).with(client))
                                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(orderId))
                                .andExpect(jsonPath("$.client.email").doesNotExist())
                                .andExpect(jsonPath("$.client.name").value("Maria Brown"));
        }

        @Test
        void missingProductUsesNotFoundErrorHandler() throws Exception {
                mockMvc.perform(get("/products/999999"))
                                .andExpect(status().isNotFound()).andExpect(jsonPath("$.status").value(404))
                                .andExpect(jsonPath("$.path").value("/products/999999"));
        }

        private Product product(String name, double price) {
                Product p = new Product();
                p.setName(name);
                p.setDescription("Test product description");
                p.setPrice(price);
                p.setImgUrl("image");
                return p;
        }
}
