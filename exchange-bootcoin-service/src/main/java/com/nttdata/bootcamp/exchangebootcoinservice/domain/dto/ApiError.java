package com.nttdata.bootcamp.exchangebootcoinservice.domain.dto;

import lombok.Data;

import java.util.List;

/**
 *
 * @since 2022
 */
@Data
public class ApiError {
    private ResponseCode code;
    private String message;
    private List<String> errors;
}
