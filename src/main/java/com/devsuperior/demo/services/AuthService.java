package com.devsuperior.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devsuperior.demo.entities.User;
import com.devsuperior.demo.services.exeptions.ForbiddenException;

@Service
public class AuthService {
    
    @Autowired 
    private UserService userService;

    public void validateSelfOrAdmin(Long userId) {
        // Lógica para validar se o usuário é o próprio ou tem perfil de administrador
        User me = userService.authenticated();
        if (!me.hasRole("ROLE_ADMIN") && !me.getId().equals(userId)) {
            throw new ForbiddenException("Acess Denied");
        }
    }
}