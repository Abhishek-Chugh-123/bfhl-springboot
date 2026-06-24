package com.bfhl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * Request DTO for /bfhl endpoint.
 * Accepts an array of strings as input data.
 */
@Data
public class BfhlRequest {

    @NotNull(message = "data field is required and cannot be null")
    @JsonProperty("data")
    private List<String> data;
}
