package com.leftovers.user.exception;

public class DuplicateEmailException extends IllegalStateException {
    private final String email;

    public DuplicateEmailException(String email) {
        super("Duplicate Email: '" + email + "'");
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
