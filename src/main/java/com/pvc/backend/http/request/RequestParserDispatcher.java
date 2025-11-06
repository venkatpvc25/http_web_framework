package com.pvc.backend.http.request;

import com.pvc.backend.constants.RequestDataType;
import com.sun.net.httpserver.HttpExchange;

public class RequestParserDispatcher {
    private final RequestParserRegistry<HttpExchange> registry;

    public RequestParserDispatcher(RequestParserRegistry<HttpExchange> registry) {
        this.registry = registry;
    }

    public Object process(HttpExchange exchange, RequestDataType type) {
        if (!registry.contains(type)) {
            throw new IllegalStateException("No parser for: " + type);
        }
        RequestDataParser<HttpExchange, ?> parser = registry.get(type);
        return parser.process(exchange);
    }
}
