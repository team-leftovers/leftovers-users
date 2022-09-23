package com.leftovers.user.exception;

public class InvalidAdminEmailException extends IllegalStateException {
    private final String email;

    public InvalidAdminEmailException(String email) {
        super(email + " does not conform to admin email policies.");
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
