package com.spread.users.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.security.core.GrantedAuthority;

public enum Roles implements GrantedAuthority {

    OWNER(Const.OWNER),
    USER(Const.USER);

    private final String authority;

    Roles(String authority) {
        this.authority = authority;
    }

    @JsonCreator
    public static Roles fromAuthority(String authority) {
        for (Roles b : Roles.values()) {
            if (b.authority.equals(authority)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + authority + "'");
    }

    @Override
    public String toString() {
        return String.valueOf(authority);
    }

    @Override
    @JsonValue
    public String getAuthority() {
        return authority;
    }

    public class Const {

        public static final String OWNER = "ROLE_OWNER";
        public static final String USER = "ROLE_USER";
    }
}
