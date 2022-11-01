package com.nttdata.bootcamp.creditsservice.model.dto;

import lombok.*;

/**
 * javadoc.
 * Bank
 * @since 2022
 */
@Builder
@Data
@AllArgsConstructor
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