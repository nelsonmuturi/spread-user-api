package com.spread.users.model.converter;

import com.spread.users.model.primitive.IdChar;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class IdCharConverter implements AttributeConverter<IdChar, String> {

    @Override
    public String convertToDatabaseColumn(IdChar id) {
        return id.getValue();
    }

    @Override
    public IdChar convertToEntityAttribute(String id) {
        if (id == null) return null;
        return new IdChar(id);
    }
}
