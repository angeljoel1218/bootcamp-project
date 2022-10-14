package com.nttdata.bootcamp.productservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    private String id;
    @NonNull
    private String name;
    private Float maintenance;
    private Integer maxMovements;
    @NonNull
    private String coin;
    @NonNull
    private TypeAccount type;
}
