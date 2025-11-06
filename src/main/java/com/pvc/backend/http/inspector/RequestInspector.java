package com.pvc.backend.http.inspector;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

public interface RequestInspector {
    boolean matches(HttpExchange exchange, String templatePath) throws IOException;
}
