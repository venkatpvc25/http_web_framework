package com.pvc.backend.utils.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.pvc.backend.model.Response;
import com.pvc.backend.utils.JsonUtils;
import com.pvc.backend.utils.ResponseUtils;
import com.sun.net.httpserver.HttpExchange;

public class ResponseUtilsImpl implements ResponseUtils {

    @Override
    public <T> void send(HttpExchange exchange, Response<T> response) throws IOException {
        Map<String, Object> responseWrapper = new HashMap<>();
        responseWrapper.put("status", response.getStatus());
        responseWrapper.put("data", response.getData());

        String json = JsonUtils.toJson(responseWrapper);
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(response.getStatus(), bytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    @Override
    public <T> void sendSuccess(HttpExchange exchange, T data) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendSuccess'");
    }

    @Override
    public void sendError(HttpExchange exchange, int status, String message) throws IOException {
        Map<String, Object> responseWrapper = new HashMap<>();
        responseWrapper.put("status", status);
        responseWrapper.put("error", message);

        String json = JsonUtils.toJson(responseWrapper);
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(status, bytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    @Override
    public void sendText(HttpExchange exchange, String message, int status) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendText'");
    }

}
