package com.leftovers.user.services;

import com.leftovers.user.exception.NoSuchAccountException;
import com.leftovers.user.model.Account;
import com.leftovers.user.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountDetailService implements UserDetailsService {
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            final Account account = accountService.getAccountByEmail(username);

            return User
                    .withUsername(account.getEmail())
                    .password(passwordEncoder.encode(account.getPassword()))
                    .roles(account.getType().name())
                    .build();
        }
        catch (NoSuchAccountException ex) {
            throw new UsernameNotFoundException(username);
        }
    }
}
