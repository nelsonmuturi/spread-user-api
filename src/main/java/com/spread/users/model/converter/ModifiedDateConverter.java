package com.spread.users.model.converter;

import com.spread.users.model.primitive.ModifiedDate;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.ZoneId;
import java.util.Date;

@Converter
public class ModifiedDateConverter implements AttributeConverter<ModifiedDate, Date> {

    @Override
    public Date convertToDatabaseColumn(ModifiedDate modifiedDate) {
        return Date.from(
                modifiedDate.getValue()
                        .atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    @Override
    public ModifiedDate convertToEntityAttribute(Date modifiedDate) {
        return new ModifiedDate(modifiedDate
                .toInstant().atZone(ZoneId.systemDefault())
                .toLocalDateTime());
    }
}
