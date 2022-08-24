package com.spread.users.model.primitive;

import java.io.Serializable;
import java.util.Objects;

import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;

public class Password implements Serializable {

    private final String value;

    public Password(final String password) {
        notNull(password);
        notEmpty(password);
        if (password.trim().length() != 60)
            throw new IllegalArgumentException("invalid password length");
        this.value = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password = (Password) o;
        return value.equals(password.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Password{" +
                "value='" + value + '\'' +
                '}';
    }

    public String getValue() {
        return value;
    }
}
