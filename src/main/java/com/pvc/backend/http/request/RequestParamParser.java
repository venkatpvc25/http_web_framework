package com.pvc.backend.http.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

public class RequestParamParser implements RequestDataParser<HttpExchange, Map<String, Object>> {

    @Override
    public Map<String, Object> process(HttpExchange exchange) {
        Map<String, Object> pairs = new LinkedHashMap<>();

        String query = exchange.getRequestURI().getQuery();
        if (query == null || query.isBlank()) {
            return pairs;
        }

        for (String param : query.split("&")) {
            if (param.isBlank())
                continue;
            String[] pair = param.split("=", 2);
            String key = decode(pair[0]);
            String value = pair.length > 1 ? decode(pair[1]) : "";
            pairs.put(key, value);
        }

        return pairs;
    }

    private static String decode(String input) {
        try {
            return java.net.URLDecoder.decode(input, java.nio.charset.StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            return input;
        }
    }

}
