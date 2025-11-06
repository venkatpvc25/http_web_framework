package com.pvc.backend.utils;

import com.sun.net.httpserver.HttpExchange;
import com.pvc.backend.model.Response;
import java.io.IOException;

public interface ResponseUtils {

    /**
     * Sends a generic response as JSON to the client.
     *
     * @param exchange HttpExchange object representing the HTTP connection
     * @param response Response object containing status, message, and data
     * @param <T>      Type of the data in the response
     * @throws IOException if sending fails
     */
    <T> void send(HttpExchange exchange, Response<T> response) throws IOException;

    /**
     * Sends a success response with a given data object.
     *
     * @param exchange HttpExchange
     * @param data     Any data to send
     * @param <T>      Type of data
     * @throws IOException if sending fails
     */
    <T> void sendSuccess(HttpExchange exchange, T data) throws IOException;

    /**
     * Sends an error response with a custom status code and message.
     *
     * @param exchange HttpExchange
     * @param status   HTTP status code (e.g., 400, 500)
     * @param message  Human-readable error message
     * @throws IOException if sending fails
     */
    void sendError(HttpExchange exchange, int status, String message) throws IOException;

    /**
     * Sends a plain text message (for debugging or health checks).
     *
     * @param exchange HttpExchange
     * @param message  Plain text message
     * @param status   HTTP status code
     * @throws IOException if sending fails
     */
    void sendText(HttpExchange exchange, String message, int status) throws IOException;
}
