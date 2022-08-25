package com.leftovers.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCustomerDto {
    private String firstName;

    private String lastName;

    @Email(message = "Email must be valid")
    private String email;

    @Size(min = 10, max = 128, message = "Length must be between 10 and 128 characters")
    private String password;

    @Pattern(regexp = "^\\d{10,15}$", message = "Phone number must be between 10 and 15 numbers")
    private String phoneNo;

    private String addressLine;

    private String unitNumber;

    private String city;

    @Size(min = 2, max = 2, message = "State must consist of only 2 characters")
    private String state;

    @Pattern(regexp = "^\\d{5}(?:[-\\s]\\d{4})?$")
    private String zipcode;
}
