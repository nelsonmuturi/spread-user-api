package com.spread.users.model.primitive;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import static org.apache.commons.lang3.Validate.notNull;

public class CreationDate implements Serializable {

    private final LocalDateTime value;

    public CreationDate(final LocalDateTime creationDate) {
        notNull(creationDate);
        this.value = creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreationDate that = (CreationDate) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "CreationDate{" +
                "value=" + value +
                '}';
    }

    public LocalDateTime getValue() {
        return value;
    }
}
