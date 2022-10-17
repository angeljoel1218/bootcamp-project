package com.nttdata.bootcamp.creditsservice.model;

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
public class Client {

    private String id;
    private TypeClient idType;
    private String idPerson;


}