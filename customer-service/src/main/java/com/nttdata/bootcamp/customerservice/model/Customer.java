package com.nttdata.bootcamp.customerservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;


@Document(collection = "costumer" )
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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