package com.leftovers.user.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginCredentialsDto {
    private String email;
    private String password;
}
