package com.pvc.backend.http;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HttpRequestInspector {

    public static void inspect(HttpExchange exchange, String templatePath) throws IOException {
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        String query = uri.getQuery();
        String method = exchange.getRequestMethod();

        System.out.println("HTTP Method: " + method);
        System.out.println("Path: " + path);
        System.out.println("Query: " + query);

        boolean hasQueryParams = hasQueryParams(query);
        boolean hasPathParams = hasPathParams(path, templatePath);
        boolean hasBody = hasRequestBody(exchange);

        System.out.println("Has path params?   -> " + hasPathParams);
        System.out.println("Has query params?  -> " + hasQueryParams);
        System.out.println("Has request body?  -> " + hasBody);
    }

    public static boolean hasQueryParams(String query) {
        if (query == null || query.isBlank()) {
            return false;
        }

        for (String part : query.split("&")) {
            if (part.contains("=")) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasPathParams(String requestPath, String templatePath) {
        if (templatePath == null || templatePath.isBlank())
            return false;

        String[] reqSegments = normalize(requestPath).split("/");
        String[] tplSegments = normalize(templatePath).split("/");

        boolean templateHasVariables = Arrays.stream(tplSegments)
                .anyMatch(seg -> seg.startsWith("{") && seg.endsWith("}"));

        if (!templateHasVariables)
            return false;

        for (int i = 0; i < Math.min(reqSegments.length, tplSegments.length); i++) {
            String tpl = tplSegments[i];
            if (tpl.startsWith("{") && tpl.endsWith("}")) {
                String value = reqSegments[i];
                if (value != null && !value.isBlank()) {
                    return true;
                }
            }
        }

        return false;
    }

    /** Detects whether the request likely has a body */
    private static boolean hasRequestBody(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (!List.of("POST", "PUT", "PATCH").contains(method)) {
            return false;
        }

        Headers headers = exchange.getRequestHeaders();
        String length = headers.getFirst("Content-Length");
        if (length != null) {
            try {
                if (Integer.parseInt(length) > 0)
                    return true;
            } catch (NumberFormatException ignored) {
            }
        }

        return exchange.getRequestBody().available() > 0;
    }

    private static String normalize(String p) {
        if (p == null || p.isBlank())
            return "";
        String s = p.trim();
        while (s.startsWith("/"))
            s = s.substring(1);
        while (s.endsWith("/"))
            s = s.substring(0, s.length() - 1);
        return s;
    }
}
