package com.spread.users.model.primitive;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import static org.apache.commons.lang3.Validate.notNull;

public class ModifiedDate implements Serializable {

    private final LocalDateTime value;

    public ModifiedDate(final LocalDateTime modifiedDate) {
        notNull(modifiedDate);
        this.value = modifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModifiedDate that = (ModifiedDate) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "ModifiedDate{" +
                "value=" + value +
                '}';
    }

    public LocalDateTime getValue() {
        return value;
    }
}
