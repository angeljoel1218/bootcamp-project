package com.nttdata.bootcamp.clientsservice.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.*;

@Document(collection = "companies")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Company {
    @Id
    private String id;

    @NotBlank(message = "El RUC no debe estar vacio" )
    @Indexed(unique = true, background = true)
    private String ruc;

    @NotEmpty(message = "La nombre no debe estar vacio")
    private String name;



}
