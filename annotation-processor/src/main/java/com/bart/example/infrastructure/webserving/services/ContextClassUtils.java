package com.bart.example.infrastructure.webserving.services;

import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class ContextClassUtils {
    public static String generateContextClassName(String path) {
        return Arrays.stream(path.split("/"))
                .filter(part -> !part.isEmpty())
                .map(part -> {
                    if (part.startsWith("{") && part.endsWith("}")) {
                        // Handle path parameters like {id}, {userId}, etc.
                        String paramName = part.substring(1, part.length() - 1);
                        return "Param" +
                                Character.toUpperCase(paramName.charAt(0)) +
                                paramName.substring(1);
                    } else {
                        // Handle regular path segments
                        return Character.toUpperCase(part.charAt(0)) +
                                part.substring(1);
                    }
                })
                .collect(Collectors.joining()) + "ContextHandler";
    }
}
