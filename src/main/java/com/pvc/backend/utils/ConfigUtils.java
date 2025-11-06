package com.pvc.backend.utils;

import java.util.List;
import java.util.Map;

public final class ConfigUtils {

    private ConfigUtils() {
    }

    /**
     * Get a nested value using dot-separated path, e.g. "app.port" or
     * "app.server.host".
     * Supports Map<String, Object> and List<Object> nodes.
     *
     * @param root Root configuration map (loaded from YAML or Properties)
     * @param path Dot-separated key path
     * @return The value at that path, or null if not found
     */
    @SuppressWarnings("unchecked")
    public static Object getNested(Map<String, Object> root, String path) {
        if (root == null || path == null || path.isEmpty()) {
            return null;
        }

        String[] parts = path.split("\\.");
        Object current = root;

        for (String part : parts) {
            if (current instanceof Map) {
                current = ((Map<String, Object>) current).get(part);
            } else if (current instanceof List) {
                // Support array-like syntax: "tenants[0].name"
                int index = parseListIndex(part);
                if (index >= 0 && index < ((List<Object>) current).size()) {
                    current = ((List<Object>) current).get(index);
                } else {
                    return null;
                }
            } else {
                // Hit a non-map node before reaching the end
                return null;
            }

            if (current == null) {
                return null;
            }
        }

        return current;
    }

    /**
     * Get a nested string value, with a default if not found.
     */
    public static String getString(Map<String, Object> root, String path, String defaultValue) {
        Object value = getNested(root, path);
        return value != null ? String.valueOf(value) : defaultValue;
    }

    /**
     * Get a nested integer value, with a default if not found or parse error.
     */
    public static int getInt(Map<String, Object> root, String path, int defaultValue) {
        Object value = getNested(root, path);
        if (value instanceof Number)
            return ((Number) value).intValue();
        if (value != null) {
            try {
                return Integer.parseInt(value.toString());
            } catch (NumberFormatException ignored) {
            }
        }
        return defaultValue;
    }

    /**
     * Get a nested boolean value, with a default if not found or invalid.
     */
    public static boolean getBoolean(Map<String, Object> root, String path, boolean defaultValue) {
        Object value = getNested(root, path);
        if (value instanceof Boolean)
            return (Boolean) value;
        if (value != null) {
            String s = value.toString().toLowerCase();
            if (s.equals("true") || s.equals("yes") || s.equals("1"))
                return true;
            if (s.equals("false") || s.equals("no") || s.equals("0"))
                return false;
        }
        return defaultValue;
    }

    /**
     * Helper to parse "key[0]" or "[1]" syntax for list indices.
     */
    private static int parseListIndex(String key) {
        int start = key.indexOf('[');
        int end = key.indexOf(']');
        if (start != -1 && end != -1 && end > start) {
            try {
                return Integer.parseInt(key.substring(start + 1, end));
            } catch (NumberFormatException ignored) {
            }
        }
        return -1;
    }
}
