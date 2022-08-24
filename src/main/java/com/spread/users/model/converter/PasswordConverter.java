package com.spread.users.model.converter;

import com.spread.users.model.primitive.Password;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class PasswordConverter implements AttributeConverter<Password, String> {

    @Override
    public String convertToDatabaseColumn(Password password) {
        return password.getValue();
    }

    @Override
    public Password convertToEntityAttribute(String password) {
        return new Password(password);
    }
}
