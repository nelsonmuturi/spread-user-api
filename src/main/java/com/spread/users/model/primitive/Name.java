package com.spread.users.model.primitive;

import java.io.Serializable;
import java.util.Objects;

import static org.apache.commons.lang3.BooleanUtils.isNotTrue;
import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;

public class Name implements Serializable {

    private final String value;

    public Name(final String name) {
        notNull(name);
        notEmpty(name);
        if (name.trim().length() < 3)
            throw new IllegalArgumentException("name must be at least 3 characters");
        if (name.trim().length() > 30)
            throw new IllegalArgumentException("name must not exceed 30 characters");
        if (isNotTrue(name.matches("[A-Z][-a-zA-Z ]{3,30}")))
            throw new IllegalArgumentException("name must start with capital letter" +
                    " and contain only characters, hyphens and spaces");
        this.value = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Name name = (Name) o;
        return value.equals(name.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Name{" +
                "value='" + value + '\'' +
                '}';
    }

    public String getValue() {
        return value;
    }
}
