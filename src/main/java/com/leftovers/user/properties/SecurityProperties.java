package com.leftovers.user.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "leftovers.security")
@Data
public class SecurityProperties {
    private String secret;
}
