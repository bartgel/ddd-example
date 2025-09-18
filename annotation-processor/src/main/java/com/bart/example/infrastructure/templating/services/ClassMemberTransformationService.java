package com.bart.example.infrastructure.templating.services;

public class ClassMemberTransformationService {
    public static String getFieldName(String simpleClassName) {
        return simpleClassName.substring(0, 1).toLowerCase() + simpleClassName.substring(1);
    }

    public static String getSimpleClassName(String fullClassName) {
        return fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
    }

    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
