package com.leftovers.user.converter;

import com.leftovers.user.model.AccountType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class AccountTypeAttributeConverter implements AttributeConverter<AccountType, String> {
    @Override
    public String convertToDatabaseColumn(AccountType accountType) {
        return accountType.toString();
    }

    @Override
    public AccountType convertToEntityAttribute(String s) {
        return AccountType.fromSqlName(s);
    }
}
