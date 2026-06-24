package com.bfhl.service;

import com.bfhl.dto.BfhlRequest;
import com.bfhl.dto.BfhlResponse;

/**
 * Service interface for BFHL business logic.
 * Defines the contract for processing input data arrays.
 */
public interface BfhlService {

    /**
     * Processes the input data array and returns categorized results.
     *
     * @param request the incoming request containing data array
     * @return BfhlResponse with all computed fields
     */
    BfhlResponse processData(BfhlRequest request);
}
