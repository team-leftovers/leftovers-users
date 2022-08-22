package com.leftovers.user.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class JWTUtil {
    private final Map<String, String> userSecrets = new HashMap<>();

    public String generateToken(String email) throws IllegalArgumentException, JWTCreationException {
        var secret = Optional.ofNullable(userSecrets.get(email))
                .orElseGet(() -> {
                    String newSecret = KeyGenerators.string().generateKey();
                    userSecrets.put(email, newSecret);
                    return newSecret;
                });
        if (!userSecrets.containsKey(email)) {
            userSecrets.put(email, KeyGenerators.string().generateKey());
        }

        return JWT.create()
                .withSubject("User Details")
                .withClaim("email", email)
                .withIssuedAt(new Date())
                .withIssuer("Leftovers User Issuer")
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateTokenAndRetrieveSubject(String token) throws JWTVerificationException {
        DecodedJWT jwt = JWT.decode(token);

        var secret = Optional.ofNullable(userSecrets.get(jwt.getClaim("email").asString()))
                .orElseThrow(() -> new JWTVerificationException("Invalid JWT Token"));

        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User Details")
                .withIssuer("Leftovers User Issuer")
                .build();

        verifier.verify(jwt);
        return jwt.getClaim("email").asString();
    }

    public void invalidateTokenForUser(String email) {
        userSecrets.remove(email);
    }
}
