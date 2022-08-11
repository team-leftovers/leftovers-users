package com.leftovers.user.dto;

import javax.validation.constraints.*;

public class CreateCustomerDto {
    @NotNull
    @NotBlank(message = "First name is mandatory")
    public String firstName;

    @NotNull
    @NotBlank(message = "Last name is mandatory")
    public String lastName;

    @NotNull
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be valid")
    public String email;

    @NotNull
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 10, max = 128, message = "Length must be between 10 and 128 characters")
    public String password;

    @NotNull
    @NotBlank
    @Pattern(regexp = "^\\d{10,15}$", message = "Phone number must be between 10 and 15 numbers")
    public String phoneNo;

    @NotNull
    @NotBlank(message = "Address line is mandatory")
    public String addressLine;

    public String houseNumber;

    public String unitNumber;

    @NotNull
    @NotBlank(message = "City is mandatory")
    public String city;

    @NotNull
    @NotBlank(message = "State is mandatory")
    @Size(min = 2, max = 2, message = "State must consist of only 2 characters")
    public String state;

    @NotNull
    @NotBlank(message = "Zipcode is mandatory")
    @Pattern(regexp = "^\\d{5}(?:[-\\s]\\d{4})?$")
    public String zipcode;
}
