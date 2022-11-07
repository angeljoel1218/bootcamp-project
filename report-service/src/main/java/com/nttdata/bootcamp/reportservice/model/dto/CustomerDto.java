package com.nttdata.bootcamp.reportservice.model.dto;

import lombok.*;


/**
 *
 * @since 2022
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    private String name;
    private String lastName;
    private String businessName;
    private String emailAddress;
    private String numberDocument;
    private boolean itsVip;
    private boolean itsPyme;
    private boolean itsCompany;
    private boolean itsPersonal;

}
