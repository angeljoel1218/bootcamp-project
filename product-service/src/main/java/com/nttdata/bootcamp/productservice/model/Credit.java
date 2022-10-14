package com.nttdata.bootcamp.productservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Credit {
    @Id
    private String id;

    @NonNull
    private String name;
    private Integer maxNumber;
    @NonNull
    private String coin;
    @NonNull
    private TypeCredit type;
}
