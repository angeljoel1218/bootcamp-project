package com.nttdata.bootcamp.customerservice.model;

import com.nttdata.bootcamp.customerservice.model.constant.TypeCustomer;
import com.nttdata.bootcamp.customerservice.model.constant.TypeProfile;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * Some javadoc.
 * Customer
 */
@Document(collection = "costumer")
@Data
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