package com.devsuperior.demo.dto;

import java.time.Instant;

import com.devsuperior.demo.entities.Payment;

import lombok.Getter;

@Getter
public class PaymentDTO {
    private Long id;
    private Instant moment;


    public PaymentDTO(Long id, Instant moment) {
        this.id = id;
        this.moment = moment;
    }

    public PaymentDTO (Payment entity) {
        id = entity.getId();
        moment = entity.getMoment();
    }

  

    
}