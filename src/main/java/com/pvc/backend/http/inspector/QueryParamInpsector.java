package com.pvc.backend.http.inspector;

import com.sun.net.httpserver.HttpExchange;

public class QueryParamInpsector implements RequestInspector {
    @Override
    public boolean matches(HttpExchange exchange, String templatePath) {
        String q = exchange.getRequestURI().getQuery();
        if (q == null || q.isBlank())
            return false;
        for (String part : q.split("&"))
            if (part.contains("="))
                return true;
        return false;
    }
}
