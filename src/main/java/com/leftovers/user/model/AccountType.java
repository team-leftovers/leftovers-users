package com.leftovers.user.model;

import java.util.Objects;

public enum AccountType {
    CUSTOMER("C"),
    DRIVER("D"),
    SITE_ADMIN("S"),
    RESTAURANT_ADMIN("R");

    private final String sqlName;

    AccountType(String sqlName) {
        this.sqlName = sqlName;
    }

    @Override
    public String toString() {
        return sqlName;
    }

    public static AccountType fromSqlName(String name) {
        if (Objects.equals(name, "C")) {
            return AccountType.CUSTOMER;
        } else if (Objects.equals(name, "D")) {
            return AccountType.DRIVER;
        } else if (Objects.equals(name, "S")) {
            return AccountType.SITE_ADMIN;
        } else if (Objects.equals(name, "R")) {
            return AccountType.RESTAURANT_ADMIN;
        }

        return null;
    }
}
