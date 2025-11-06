package com.pvc.backend.http.info;

import java.io.IOException;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;

public class RequestBodyInpsector implements RequestInspector {
    @Override
    public boolean matches(HttpExchange exchange, String templatePath) throws IOException {
        String method = exchange.getRequestMethod();
        if (!List.of("POST", "PUT", "PATCH").contains(method))
            return false;
        String len = exchange.getRequestHeaders().getFirst("Content-Length");
        if (len != null) {
            try {
                return Integer.parseInt(len) > 0;
            } catch (NumberFormatException ignored) {
            }
        }
        return exchange.getRequestBody().available() > 0;
    }
}
