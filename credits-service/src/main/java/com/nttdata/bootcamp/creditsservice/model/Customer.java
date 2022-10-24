package com.nttdata.bootcamp.creditsservice.model;

import com.nttdata.bootcamp.creditsservice.model.TypeCustomer;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;


@Builder
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    private String id;
    private TypeCustomer idType;
    private String name;
    private String lastName;
    private String bussinesName;
    private String emailAddress;
    private String numberDocument;


}