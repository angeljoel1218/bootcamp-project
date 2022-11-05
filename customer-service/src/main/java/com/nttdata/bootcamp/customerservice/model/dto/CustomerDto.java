package com.nttdata.bootcamp.customerservice.model.dto;

import com.nttdata.bootcamp.customerservice.model.constants.TypeCustomer;
import com.nttdata.bootcamp.customerservice.model.constants.TypeProfile;
import javax.validation.constraints.NotNull;

import lombok.*;
import org.springframework.data.annotation.Id;


/**
 * Some javadoc.
 *
 * @author Alex Bejarano
 * @since 2022
 */
@Builder
@Data
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

  @NotNull(message = "numberDocument   is required")
  private String numberDocument;
  private String status;

  // This field can be nulll
  private TypeProfile typeProfile;

  private boolean itsVip;
  private boolean itsPyme;
  private boolean itsCompany;
  private boolean itsPersonal;

  /**
   * Some javadoc.
   */
  public CustomerDto(TypeCustomer typeCustomer, String name, String lastName, String businessName,
           String emailAddress, String numberDocument, TypeProfile typeProfile) {
    this.typeCustomer = typeCustomer;
    this.name = name;
    this.lastName = lastName;
    this.businessName = businessName;
    this.emailAddress = emailAddress;
    this.numberDocument = numberDocument;
    this.typeProfile = typeProfile;
  }

  /**
   * Some javadoc.
   */
  public CustomerDto(String id, TypeCustomer typeCustomer, String name, String lastName,
                     String businessName, String emailAddress, String numberDocument,
                     TypeProfile typeProfile) {
    this.id = id;
    this.typeCustomer = typeCustomer;
    this.name = name;
    this.lastName = lastName;
    this.businessName = businessName;
    this.emailAddress = emailAddress;
    this.numberDocument = numberDocument;
    this.typeProfile = typeProfile;
  }
}
