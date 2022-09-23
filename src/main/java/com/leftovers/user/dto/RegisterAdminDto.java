package com.leftovers.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterAdminDto {
    @NotNull
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email must be valid")
    @Pattern(regexp = "^.*?@smoothstack.com$", message = "Admin email must be a Smoothstack email")
    private String email;

    @NotNull
    @NotBlank(message = "Password is mandatory")
    @Size(min = 10, max = 128, message = "Length must be between 10 and 128 characters")
    private String password;
}
