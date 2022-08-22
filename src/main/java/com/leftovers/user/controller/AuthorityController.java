package com.leftovers.user.controller;

import com.leftovers.user.model.Account;
import com.leftovers.user.model.AccountType;
import com.leftovers.user.services.AccountDetailService;
import com.leftovers.user.services.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Temporary controller for easy testing/verification of authentication
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthorityController {
    private final AccountService accountService;
    private final AccountDetailService accountDetailService;

    @RequestMapping(value = "whoami", produces = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getAuthority(Authentication authentication)
    {
        Account account = accountService.getAccountByEmail(authentication.getName());
        Map<String, Object> result = new HashMap<>();
        result.put("username", authentication.getName());
        if (account.getType() == AccountType.CUSTOMER || account.getType() == AccountType.DRIVER) {
            result.put("name", account.getFirstName() + " " + account.getLastName());
        }
        result.put("authorities", authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "logout", produces = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> logout(Authentication authentication)
    {
        authentication.setAuthenticated(false);
        Map<String, Object> result = new HashMap<>();
        result.put("result", "success");
        return ResponseEntity.ok(result);
    }
}
