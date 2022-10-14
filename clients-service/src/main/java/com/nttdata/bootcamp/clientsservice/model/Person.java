package com.nttdata.bootcamp.clientsservice.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Document(collection =  "persons")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Person {

    @Id
    private String id;

    @NotBlank(message = "El DNI no debe estar vacio" )
    @Indexed(unique = true, background = true)
    private String dni;

    @NotBlank(message = "El nombre no debe estar vacio" )
    private String name;

    @NotBlank(message = "El apellido no debe estar vacio" )
    private String lastName;

    @Email(regexp = ".+[@].+[\\.].+")
    private String email;


}
