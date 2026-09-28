package com.devsuperior.demo.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import com.devsuperior.demo.entities.Category;
import com.devsuperior.demo.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTests {
 @Mock CategoryRepository repository;
 @InjectMocks CategoryService service;
 @Test void findAllMapsCategoriesToDtos() { Category c=new Category(); c.setId(2L); c.setName("Livros"); when(repository.findAll()).thenReturn(List.of(c)); var result=service.findAll(); assertEquals(1,result.size()); assertEquals(2L,result.get(0).getId()); assertEquals("Livros",result.get(0).getName()); }
 @Test void findAllReturnsEmptyListWhenNoCategories() { when(repository.findAll()).thenReturn(List.of()); assertTrue(service.findAll().isEmpty()); }
}
