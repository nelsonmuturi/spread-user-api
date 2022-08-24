package com.spread.users.model.primitive;

import com.spread.users.model.enums.Roles;

import java.io.Serializable;
import java.util.Objects;

import static org.apache.commons.lang3.Validate.notNull;

public class Role implements Serializable {

    private final com.spread.users.model.enums.Roles value;

    public Role(final com.spread.users.model.enums.Roles role) {
        notNull(role);
        this.value = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return value == role.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Role{" +
                "value=" + value +
                '}';
    }

    public Roles getValue() {
        return value;
    }

    public static com.spread.users.model.enums.Roles convertFromString(String role) {
        switch (role) {
            case "ROLE_OWNER":
                return Roles.OWNER;
            case "ROLE_USER":
                return Roles.USER;
            default:
                throw new IllegalArgumentException("invalid role " + role);
        }
    }
}
