package com.bfhl.service;

import com.bfhl.dto.BfhlRequest;
import com.bfhl.dto.BfhlResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of BfhlService.
 *
 * Processes the input array and:
 * - Separates numbers into odd/even (returned as strings)
 * - Extracts alphabets (converted to uppercase)
 * - Extracts special characters
 * - Computes sum of all numeric values
 * - Builds concat_string: all alphabetical chars extracted from ALL tokens,
 *   reversed, alternating caps (first char = uppercase).
 *
 * USER DETAILS — replace before submission:
 * full_name = your name in lowercase with underscores
 * dob       = your date of birth as ddmmyyyy
 * email     = your email
 * rollNumber= your college roll number
 */
@Service
public class BfhlServiceImpl implements BfhlService {

    
    private static final String FULL_NAME  = "Abhishek Chugh";         
    private static final String DOB        = "07032005";       
    private static final String EMAIL      = "abhishek0179.be23@chitkara.edu.in";   
    private static final String ROLL_NUMBER = "2310990179";        
 

    @Override
    public BfhlResponse processData(BfhlRequest request) {

        List<String> data = request.getData();

        List<String> oddNumbers       = new ArrayList<>();
        List<String> evenNumbers      = new ArrayList<>();
        List<String> alphabets        = new ArrayList<>();
        List<String> specialCharacters = new ArrayList<>();
        long sumValue                 = 0;
        StringBuilder allAlphaChars   = new StringBuilder(); // all alpha chars (from all tokens)

        for (String token : data) {
            if (isNumber(token)) {
                // Numbers — keep as string in response
                long num = Long.parseLong(token);
                sumValue += num;
                if (num % 2 == 0) {
                    evenNumbers.add(token);
                } else {
                    oddNumbers.add(token);
                }
            } else if (isAlpha(token)) {
                // Entire token is alphabetic → uppercase, add to alphabets list
                alphabets.add(token.toUpperCase());
                // Also collect individual chars for concat_string
                for (char c : token.toCharArray()) {
                    if (Character.isLetter(c)) {
                        allAlphaChars.append(c);
                    }
                }
            } else if (isSingleSpecialChar(token)) {
                specialCharacters.add(token);
            } else {
                // Mixed token — classify char by char
                boolean hasAlpha   = false;
                boolean hasSpecial = false;
                for (char c : token.toCharArray()) {
                    if (Character.isLetter(c)) {
                        hasAlpha = true;
                        allAlphaChars.append(c);
                    } else if (!Character.isDigit(c)) {
                        hasSpecial = true;
                    }
                }
                if (hasAlpha) {
                    alphabets.add(token.toUpperCase());
                }
                if (hasSpecial && !hasAlpha && !isNumber(token)) {
                    specialCharacters.add(token);
                }
            }
        }

        String concatStr = buildConcatString(allAlphaChars.toString());
        String userId    = FULL_NAME + "_" + DOB;

        return BfhlResponse.builder()
                .isSuccess(true)
                .userId(userId)
                .email(EMAIL)
                .rollNumber(ROLL_NUMBER)
                .oddNumbers(oddNumbers)
                .evenNumbers(evenNumbers)
                .alphabets(alphabets)
                .specialCharacters(specialCharacters)
                .sum(String.valueOf(sumValue))
                .concatString(concatStr)
                .build();
    }

    // ─── HELPER METHODS ───────────────────────────────────────────────────────

    /**
     * Returns true if the token consists entirely of digits
     * (handles multi-digit numbers like "334").
     */
    private boolean isNumber(String token) {
        if (token == null || token.isEmpty()) return false;
        for (char c : token.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    /**
     * Returns true if the token consists entirely of alphabetic characters.
     */
    private boolean isAlpha(String token) {
        if (token == null || token.isEmpty()) return false;
        for (char c : token.toCharArray()) {
            if (!Character.isLetter(c)) return false;
        }
        return true;
    }

    /**
     * Returns true if the token is a single non-alphanumeric character.
     */
    private boolean isSingleSpecialChar(String token) {
        if (token == null || token.length() != 1) return false;
        char c = token.charAt(0);
        return !Character.isLetterOrDigit(c);
    }

    /**
     * Builds the concat_string:
     * 1. Take all alphabetical characters collected from all input tokens.
     * 2. Reverse them.
     * 3. Apply alternating caps starting with uppercase (index 0 = upper, 1 = lower, ...).
     *
     * Example: input chars "ayb" → reversed "bya" → alternating "ByA"
     */
    private String buildConcatString(String allAlpha) {
        if (allAlpha.isEmpty()) return "";

        // Reverse the collected characters
        String reversed = new StringBuilder(allAlpha).reverse().toString();

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < reversed.length(); i++) {
            char c = reversed.charAt(i);
            if (i % 2 == 0) {
                result.append(Character.toUpperCase(c));  // even index → uppercase
            } else {
                result.append(Character.toLowerCase(c));  // odd index  → lowercase
            }
        }
        return result.toString();
    }
}
