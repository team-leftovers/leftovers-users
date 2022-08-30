package com.leftovers.user.exception;

import java.util.NoSuchElementException;
import java.util.Optional;

public class NoSuchAccountException extends NoSuchElementException {
    private final Long accountId;
    private final String accountEmail;

    public NoSuchAccountException(Long id) {
        super("No customer record found for id=" + id);
        this.accountId = id;
        this.accountEmail = null;
    }

    public NoSuchAccountException(String email) {
        super("No customer record found for email=" + email);
        this.accountEmail = email;
        this.accountId = null;
    }

    public Optional<Long> getAccountId() {
        return Optional.ofNullable(accountId);
    }

    public Optional<String> getAccountEmail() {
        return Optional.ofNullable(accountEmail);
    }
}
