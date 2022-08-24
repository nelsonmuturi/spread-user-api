package com.spread.users.model.converter;

import com.spread.users.model.enums.Roles;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class RoleConverter
        implements AttributeConverter<com.spread.users.model.primitive.Role, String> {

    @Override
    public String convertToDatabaseColumn(com.spread.users.model.primitive.Role role) {
        return role.getValue().toString();
    }

    @Override
    public com.spread.users.model.primitive.Role convertToEntityAttribute(String role) {
        switch (role) {
            case "USER":
            case "ROLE_USER":
                return new com.spread.users.model.primitive.Role(Roles.USER);
            case "OWNER":
            case "ROLE_OWNER":
                return new com.spread.users.model.primitive.Role(Roles.OWNER);
            default:
                throw new IllegalArgumentException("invalid role");
        }
    }
}
