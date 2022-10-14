package com.nttdata.bootcamp.clientsservice.model;

import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Document(collection = "clients" )
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    public enum EType{
        PERSONAL, COMPANY
    }

    @Id
    private String id;

    @NotNull(message = "El Tipo no debe estar vacio (1 Personal, 2 Empresarial)")
    private EType idType;

    // Id Person y/o Company
    private String idPerson;







}
