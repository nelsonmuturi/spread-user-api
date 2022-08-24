package com.spread.users.model.converter;

import com.spread.users.model.primitive.Name;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class NameConverter implements AttributeConverter<Name, String> {

    @Override
    public String convertToDatabaseColumn(Name name) {
        return name.getValue();
    }

    @Override
    public Name convertToEntityAttribute(String name) {
        return new Name(name);
    }
}
