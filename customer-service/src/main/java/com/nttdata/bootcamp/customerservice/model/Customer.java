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

    private TypeCustomer typeCustomer;

    private String name;

    private String lastName;

    private String businessName;

    private String emailAddress;

    private String numberDocument;

    private String status;

    private TypeProfile typeProfile;

}