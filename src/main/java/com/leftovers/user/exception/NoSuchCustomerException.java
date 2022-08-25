package com.leftovers.user.exception;

import java.util.NoSuchElementException;
import java.util.Optional;

public class NoSuchCustomerException extends NoSuchElementException {
    private final Long customerId;
    private final String customerEmail;

    public NoSuchCustomerException(Long id) {
        super("No customer record found for id=" + id);
        this.customerId = id;
        this.customerEmail = null;
    }

    public NoSuchCustomerException(String email) {
        super("No customer record found for email=" + email);
        this.customerEmail = email;
        this.customerId = null;
    }

    public Optional<Long> getCustomerId() {
        return Optional.ofNullable(customerId);
    }

    public Optional<String> getCustomerEmail() {
        return Optional.ofNullable(customerEmail);
    }
}
