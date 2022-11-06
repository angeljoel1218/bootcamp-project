package com.nttdata.bootcamp.customerservice.model.dto;

import lombok.Data;

import java.util.List;


/**
 * Some javadoc.
 *
 * @since 2022
 */
@Data
public class ApiError {
  private ResponseCode code;
  private String message;
  private List<String> errors;
}
