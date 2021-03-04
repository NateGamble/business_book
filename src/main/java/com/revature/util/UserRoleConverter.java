package com.revature.util;

import com.revature.models.Role;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class UserRoleConverter implements AttributeConverter<Role, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Role attribute) {
        if (attribute == null) {
            return null;
        }
        switch (attribute) {
            case ADMIN:
                return 1;
            case OWNER:
                return 2;
            case USER:
                return 3;
            default:
                throw new IllegalArgumentException(attribute + " not supported.");
        }
    }

    @Override
    public Role convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        switch (dbData) {
            case 1:
                return Role.ADMIN;
            case 2:
                return Role.OWNER;
            case 3:
                return Role.USER;
            default:
                throw new IllegalArgumentException(dbData + " not supported");
        }
    }

}
