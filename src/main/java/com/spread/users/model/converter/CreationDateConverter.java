package com.spread.users.model.converter;

import com.spread.users.model.primitive.CreationDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.ZoneId;
import java.util.Date;

@Converter
public class CreationDateConverter implements AttributeConverter<CreationDate, Date> {
    private static final Logger logger =
            LoggerFactory.getLogger(CreationDateConverter.class);

    @Override
    public Date convertToDatabaseColumn(CreationDate creationDate) {
        return Date.from(
                creationDate.getValue()
                        .atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    @Override
    public CreationDate convertToEntityAttribute(Date creationDate) {
        return new CreationDate(creationDate
                .toInstant().atZone(ZoneId.systemDefault())
                .toLocalDateTime());
    }
}
