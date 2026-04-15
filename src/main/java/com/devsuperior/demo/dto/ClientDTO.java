package com.devsuperior.demo.dto;

import com.devsuperior.demo.entities.User;

import lombok.Getter;

@Getter
public class ClientDTO {
    private Long id;
    private String name;

    public ClientDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public ClientDTO(User entity) {
        id = entity.getId();
        name = entity.getName();
    }

}