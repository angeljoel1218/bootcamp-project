package com.nttdata.bootcamp.accountservice.model.dto;

import com.nttdata.bootcamp.accountservice.model.TypeCustomer;
import lombok.*;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;

@Data
public class Customer {
    @Id
    private String id;

    @NotNull(message = "El Tipo no debe estar vacio (1 Personal, 2 Empresarial)")
    private TypeCustomer idType;

    private String name;

    private String lastName;

    private String bussinesName;

    private String emailAddress;

    private String numberDocument;
}