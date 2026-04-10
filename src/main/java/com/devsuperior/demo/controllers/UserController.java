package com.devsuperior.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsuperior.demo.dto.UserDto;
import com.devsuperior.demo.services.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService service;

    // Aqui eu estou Buscando por id//
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')") // Somente o admin pode acessar os dados do usuario//
    @GetMapping(value = "/me")
    public ResponseEntity<UserDto> getMe() {
        UserDto dto = service.getMe();
        return ResponseEntity.ok(dto);

    }

}
