package com.nttdata.bootcamp.cardservice.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class ApiError {
    private ResponseCode code;
    private String message;
    private List<String> errors;
}
