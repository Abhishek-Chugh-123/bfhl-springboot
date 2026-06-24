package com.bfhl.controller;

import com.bfhl.dto.BfhlRequest;
import com.bfhl.dto.BfhlResponse;
import com.bfhl.service.BfhlServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test cases for BfhlController.
 * Covers: Example A, Example B, Example C, empty array, null data, invalid JSON.
 */
@WebMvcTest(BfhlController.class)
@Import(BfhlServiceImpl.class)
class BfhlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // ─── Test: Example A ─────────────────────────────────────────────────────

    @Test
    @DisplayName("Example A: mixed numbers, alphabets, special chars")
    void testExampleA() throws Exception {
        String requestJson = """
                {
                  "data": ["a", "1", "334", "4", "R", "$"]
                }
                """;

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.is_success").value(true))
                .andExpect(jsonPath("$.odd_numbers[0]").value("1"))
                .andExpect(jsonPath("$.even_numbers").isArray())
                .andExpect(jsonPath("$.special_characters[0]").value("$"))
                .andExpect(jsonPath("$.sum").value("339"));
    }

    // ─── Test: Example B ─────────────────────────────────────────────────────

    @Test
    @DisplayName("Example B: multiple alphabets, special chars, numbers")
    void testExampleB() throws Exception {
        String requestJson = """
                {
                  "data": ["2", "a", "y", "4", "&", "-", "*", "5", "92", "b"]
                }
                """;

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.is_success").value(true))
                .andExpect(jsonPath("$.odd_numbers[0]").value("5"))
                .andExpect(jsonPath("$.sum").value("103"))
                .andExpect(jsonPath("$.concat_string").value("ByA"));
    }

    // ─── Test: Example C ─────────────────────────────────────────────────────

    @Test
    @DisplayName("Example C: only multi-char alphabetic tokens")
    void testExampleC() throws Exception {
        String requestJson = """
                {
                  "data": ["A", "ABCD", "DOE"]
                }
                """;

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.is_success").value(true))
                .andExpect(jsonPath("$.odd_numbers").isEmpty())
                .andExpect(jsonPath("$.even_numbers").isEmpty())
                .andExpect(jsonPath("$.special_characters").isEmpty())
                .andExpect(jsonPath("$.sum").value("0"))
                .andExpect(jsonPath("$.concat_string").value("EoDdCbAa"));
    }

    // ─── Test: Empty data array ───────────────────────────────────────────────

    @Test
    @DisplayName("Empty data array should return success with empty lists and sum 0")
    void testEmptyDataArray() throws Exception {
        String requestJson = """
                {
                  "data": []
                }
                """;

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.is_success").value(true))
                .andExpect(jsonPath("$.sum").value("0"))
                .andExpect(jsonPath("$.concat_string").value(""))
                .andExpect(jsonPath("$.odd_numbers").isEmpty())
                .andExpect(jsonPath("$.even_numbers").isEmpty())
                .andExpect(jsonPath("$.alphabets").isEmpty());
    }

    // ─── Test: Null data field → 400 ─────────────────────────────────────────

    @Test
    @DisplayName("Missing data field should return 400 Bad Request")
    void testNullDataField() throws Exception {
        String requestJson = "{}";

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.is_success").value(false));
    }

    // ─── Test: Invalid JSON → 400 ────────────────────────────────────────────

    @Test
    @DisplayName("Malformed JSON should return 400 Bad Request")
    void testMalformedJson() throws Exception {
        String badJson = "{ data: [1,2,3] }"; // not valid JSON

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.is_success").value(false));
    }

    // ─── Test: Only numbers ───────────────────────────────────────────────────

    @Test
    @DisplayName("Only numbers input: no alphabets, no specials")
    void testOnlyNumbers() throws Exception {
        String requestJson = """
                {
                  "data": ["3", "6", "9"]
                }
                """;

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sum").value("18"))
                .andExpect(jsonPath("$.odd_numbers[0]").value("3"))
                .andExpect(jsonPath("$.odd_numbers[1]").value("9"))
                .andExpect(jsonPath("$.even_numbers[0]").value("6"))
                .andExpect(jsonPath("$.alphabets").isEmpty())
                .andExpect(jsonPath("$.concat_string").value(""));
    }

    // ─── Test: user_id format ────────────────────────────────────────────────

    @Test
    @DisplayName("user_id should follow full_name_ddmmyyyy format")
    void testUserIdFormat() throws Exception {
        String requestJson = """
                {
                  "data": ["1"]
                }
                """;

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id").isString())
                // user_id should match pattern: word_word_ddmmyyyy
                .andExpect(jsonPath("$.user_id").value(
                        org.hamcrest.Matchers.matchesPattern("[a-z]+_[a-z]+_\\d{8}")));
    }
}
