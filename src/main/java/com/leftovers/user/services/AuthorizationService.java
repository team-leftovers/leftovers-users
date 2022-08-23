package com.leftovers.user.services;

import com.leftovers.user.exception.NoSuchAccountException;
import com.leftovers.user.model.Account;
import com.leftovers.user.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthorizationService {
    private final AccountService accountService;

    public boolean isAuthorizedWithId(Authentication auth, Long id) {
        try {
            Account account = accountService.getAccountByEmail(auth.getName());
            return Objects.equals(account.getId(), id);
        } catch (NoSuchAccountException ex) {
            return false;
        }
    }

    public boolean isAuthorizedWithEmail(Authentication auth, String email) {
        try {
            Account account = accountService.getAccountByEmail(auth.getName());
            return Objects.equals(account.getEmail(), email);
        } catch (NoSuchAccountException ex) {
            return false;
        }
    }

    public boolean isCustomer(Authentication auth, Long id) {
        return auth
                .getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList())
                .contains("ROLE_CUSTOMER") &&
                isAuthorizedWithId(auth, id);
    }

    public boolean isCustomer(Authentication auth, String email) {
        return auth
                .getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList())
                .contains("ROLE_CUSTOMER") &&
                isAuthorizedWithEmail(auth, email);
    }
}
