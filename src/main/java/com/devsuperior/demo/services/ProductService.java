package com.devsuperior.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.demo.dto.ProductDTO;
import com.devsuperior.demo.entities.Product;
import com.devsuperior.demo.repository.ProductRepository;
import com.devsuperior.demo.services.exeptions.ResourceNotFouldExeption;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    // Aqui eu estou buscando o produto por id e lancando uma exceção para caso ele não exista //
    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product product = repository.findById(id).orElseThrow(
                () -> new ResourceNotFouldExeption("Recurso não encontrado"));
        return new ProductDTO(product.getId(), product.getName(), product.getDescription(),
                product.getPrice(),
                product.getImgUrl());

    }
    
    //Aqui eu estou Buscando pelos produtos//
    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable) {
        Page<Product> result = repository.findAll(pageable);
        return result.map(x -> new ProductDTO(x));

    }
    //Aqui eu estou inserindo novos produtos//
    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        Product entity = new Product();
        copyDtoEntity(dto, entity);
        entity = repository.save(entity);
        return new ProductDTO(entity);

    }
    //Aqui estou atualizando os produtos já inseridos//
    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        Product entity = repository.getReferenceById(id);
        copyDtoEntity(dto, entity);
        entity = repository.save(entity);
        return new ProductDTO(entity);

    }
    //Aqui eu estou deletando produtos existentes//
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);

    }
    //Aqui eu criei um metodo para minimizar a escrita do (insert e update)//
    private void copyDtoEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImgUrl(dto.getImgUrl());

    }

}