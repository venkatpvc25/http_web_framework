package com.pvc.backend.http.inspector;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

public class RequestInspectorOrchestrator {
    private final RequestInspectorRegistry registry;

    public RequestInspectorOrchestrator(RequestInspectorRegistry registry) {
        this.registry = registry;
    }

    public RequestInfo inspect(HttpExchange exchange, String templatePath) throws IOException {
        boolean hasPath = false;
        boolean hasQuery = false;
        boolean hasBody = false;
        for (RequestInspector s : registry.snapshot()) {
            if (s instanceof PathParamInspector && !hasPath) {
                hasPath = s.matches(exchange, templatePath);
            } else if (s instanceof QueryParamInpsector && !hasQuery) {
                hasQuery = s.matches(exchange, templatePath);
            } else if (s instanceof RequestBodyInpsector && !hasBody) {
                hasBody = s.matches(exchange, templatePath);
            } else {
                s.matches(exchange, templatePath);
            }
            if (hasPath && hasQuery && hasBody)
                break; // short-circuit
        }
        return new RequestInfo(hasPath, hasQuery, hasBody);
    }
}
