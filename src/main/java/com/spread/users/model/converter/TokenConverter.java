package com.spread.users.model.converter;

import com.spread.users.model.primitive.Token;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class TokenConverter implements AttributeConverter<Token, String> {

    @Override
    public String convertToDatabaseColumn(Token token) {
        return token.getValue();
    }

    @Override
    public Token convertToEntityAttribute(String token) {
        return new Token(token);
    }
}
