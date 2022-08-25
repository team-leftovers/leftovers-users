package com.leftovers.user.controller;

import com.leftovers.user.dto.LoginCredentialsDto;
import com.leftovers.user.model.Account;
import com.leftovers.user.model.AccountType;
import com.leftovers.user.security.JWTUtil;
import com.leftovers.user.services.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthorityController {
    private final AccountService accountService;
    private final AuthenticationManager authManager;
    private final JWTUtil jwtUtil;

    @RequestMapping(value = "/whoami", produces = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getAuthority(Authentication authentication)
    {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Account account = accountService.getAccountByEmail(authentication.getName());
        Map<String, Object> result = new HashMap<>();
        result.put("username", authentication.getName());
        if (account.getType() == AccountType.CUSTOMER || account.getType() == AccountType.DRIVER) {
            result.put("name", account.getFirstName() + " " + account.getLastName());
        }
        result.put("authorities", authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "/logout", produces = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> logoutHandler(Authentication authentication)
    {
        jwtUtil.invalidateTokenForUser(authentication.getName());
        return ResponseEntity.ok(Collections.singletonMap("result", "success"));
    }

    @RequestMapping(value = "/login", produces = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> loginHandler(@RequestBody LoginCredentialsDto body) {
        UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(body.getEmail(), body.getPassword());
        authManager.authenticate(authInputToken);
        String token = jwtUtil.generateToken(body.getEmail());

        return ResponseEntity.ok(Collections.singletonMap("jwt-token", token));
    }
}
