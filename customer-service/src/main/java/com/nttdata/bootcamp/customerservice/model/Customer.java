package com.nttdata.bootcamp.customerservice.model;

import com.nttdata.bootcamp.customerservice.model.constants.TypeCustomer;
import com.nttdata.bootcamp.customerservice.model.constants.TypeProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * Some javadoc.
 * Customer
 */
@Document(collection = "costumer")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

  @Id
  private String id;

  private TypeCustomer typeCustomer;

  private String name;

  private String lastName;

  private String businessName;

  private String emailAddress;

  private String numberDocument;

  private String status;

  private TypeProfile typeProfile;

}