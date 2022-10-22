package com.nttdata.bootcamp.customerservice.model.dto;

import com.nttdata.bootcamp.customerservice.model.TypeCustomer;
import com.nttdata.bootcamp.customerservice.model.TypeProfile;
import lombok.*;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    @Id
    private String id;

    @NotNull(message = "The field Type is required")
    private TypeCustomer typeCustomer;

    private String name;
    private String lastName;
    private String businessName;
    private String emailAddress;

    @NotNull(message = "The field Type is required")
    private String numberDocument;
    private String status;

    // This field can be nulll
    private TypeProfile typeProfile;

    private boolean itsVip;
    private boolean itsPyme;
    private boolean itsCompany;
    private boolean itsPersonal;

}
