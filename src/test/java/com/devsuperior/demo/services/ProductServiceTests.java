package com.devsuperior.demo.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.devsuperior.demo.dto.CategoryDTO;
import com.devsuperior.demo.dto.ProductDTO;
import com.devsuperior.demo.entities.Category;
import com.devsuperior.demo.entities.Product;
import com.devsuperior.demo.repository.ProductRepository;
import com.devsuperior.demo.services.exeptions.DatabaseException;
import com.devsuperior.demo.services.exeptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class ProductServiceTests {
    @Mock
    ProductRepository repository;
    @InjectMocks
    ProductService service;

    @Test
    void findByIdMapsEntityAndCategories() {
        Product p = product(1L, "Livro");
        when(repository.findById(1L)).thenReturn(Optional.of(p));
        ProductDTO result = service.findById(1L);
        assertEquals(1L, result.getId());
        assertEquals("Livro", result.getName());
        assertEquals("Categoria", result.getCategories().get(0).getName());
    }

    @Test
    void findByIdWhenMissingThrowsNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
    }

    @Test
    void findAllMapsPageResults() {
        when(repository.searchByName("liv", PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(product(1L, "Livro"))));
        var result = service.findAll("liv", PageRequest.of(0, 10));
        assertEquals(1, result.getTotalElements());
        assertEquals("Livro", result.getContent().get(0).getName());
    }

    @Test
    void insertCopiesFieldsAndCategoryIds() {
        ProductDTO dto = dto("Novo");
        when(repository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));
        ProductDTO result = service.insert(dto);
        assertEquals("Novo", result.getName());
        assertEquals(1L, result.getCategories().get(0).getId());
        verify(repository).save(
                argThat(p -> p.getCategories().size() == 1 && p.getCategories().iterator().next().getId().equals(1L)));
    }

    @Test
    void updateCopiesFieldsOnExistingEntity() {
        Product p = product(4L, "Antigo");
        when(repository.getReferenceById(4L)).thenReturn(p);
        when(repository.save(p)).thenReturn(p);
        ProductDTO result = service.update(4L, dto("Atualizado"));
        assertEquals("Atualizado", result.getName());
        assertEquals(1, p.getCategories().size());
    }

    @Test
    void updateWhenReferenceIsMissingThrowsNotFound() {
        when(repository.getReferenceById(4L)).thenThrow(new EntityNotFoundException());
        assertThrows(ResourceNotFoundException.class, () -> service.update(4L, dto("Atualizado")));
    }

    @Test
    void deleteRemovesExistingProduct() {
        when(repository.existsById(4L)).thenReturn(true);
        service.delete(4L);
        verify(repository).deleteById(4L);
    }

    @Test
    void deleteWhenMissingThrowsNotFound() {
        when(repository.existsById(4L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> service.delete(4L));
        verify(repository, never()).deleteById(anyLong());
    }

    @Test
    void deleteWhenReferencedThrowsDatabaseException() {
        when(repository.existsById(4L)).thenReturn(true);
        doThrow(new DataIntegrityViolationException("constraint")).when(repository).deleteById(4L);
        assertThrows(DatabaseException.class, () -> service.delete(4L));
    }

    private ProductDTO dto(String name) {
        ProductDTO dto = new ProductDTO(1L, name, "Descrição suficientemente longa", 12.5, "img");
        dto.setCategories(List.of(new CategoryDTO(1L, "Categoria")));
        return dto;
    }

    private Product product(Long id, String name) {
        Product p = new Product();
        p.setId(id);
        p.setName(name);
        p.setDescription("Descrição");
        p.setPrice(10.0);
        p.setImgUrl("img");
        Category c = new Category();
        c.setId(1L);
        c.setName("Categoria");
        p.getCategories().add(c);
        return p;
    }
}
