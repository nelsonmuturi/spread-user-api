package com.spread.users.model.primitive;

import java.io.Serializable;
import java.util.Objects;

import static org.apache.commons.lang3.BooleanUtils.isNotTrue;
import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;

public class Token implements Serializable {

    private final String value;

    public Token(final String token) {
        notNull(token);
        notEmpty(token);
        if (isNotTrue(token.length() == 128))
            throw new IllegalArgumentException("token must be 128 characters");
        this.value = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return value.equals(token.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Token{" +
                "value='" + value + '\'' +
                '}';
    }

    public String getValue() {
        return value;
    }
}
