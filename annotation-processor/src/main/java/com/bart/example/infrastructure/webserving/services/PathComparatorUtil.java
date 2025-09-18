package com.bart.example.infrastructure.webserving.services;

public abstract class PathComparatorUtil {
    public static String removeTrailingSlash(String path) {
        if (path != null && path.endsWith("/")) {
            return path.substring(0, path.length() - 1);
        }
        return path;
    }

    public static boolean pathsMatch(String patternPath, String actualPath) {
        if (patternPath == null || actualPath == null) {
            return false;
        }

        // Split both paths by slashes
        String[] patternParts = patternPath.split("/");
        String[] actualParts = actualPath.split("/");

        // Paths must have the same number of segments
        if (patternParts.length != actualParts.length) {
            return false;
        }

        // Compare each segment
        for (int i = 0; i < patternParts.length; i++) {
            String patternPart = patternParts[i];
            String actualPart = actualParts[i];

            // Skip empty parts (can happen with leading/trailing slashes)
            if (patternPart.isEmpty() && actualPart.isEmpty()) {
                continue;
            }

            // If pattern part is a parameter (in braces), skip comparison
            if (isParameter(patternPart)) {
                continue;
            }

            // For non-parameter parts, they must match exactly
            if (!patternPart.equals(actualPart)) {
                return false;
            }
        }

        return true;
    }

    private static boolean isParameter(String pathPart) {
        return pathPart.startsWith("{") && pathPart.endsWith("}");
    }

}
