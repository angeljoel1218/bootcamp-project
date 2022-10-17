package com.nttdata.bootcamp.accountservice.model.dto;

import com.nttdata.bootcamp.accountservice.model.TypeClient;
import lombok.*;

@Data
public class Client {
    private String id;
    private TypeClient idType;
    private String idPerson;
}