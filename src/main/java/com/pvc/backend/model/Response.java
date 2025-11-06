package com.pvc.backend.model;

import java.time.Instant;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Response<T> {

    private int status; // e.g. 200, 400, 500
    private String message; // e.g. "Success", "Bad Request", "Error occurred"

    private T data; // Generic type - can be any object, list, DTO, etc.

    private Instant timestamp; // Useful for logging / API clients
    private String path; // e.g. "/api/users"
    private String errorCode; // For custom error identifiers (optional)

    public Response(int status, String message, T data, Instant timestamp, String path, String errorCode) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = timestamp;
        this.path = path;
        this.errorCode = errorCode;
    }

    // ✅ Constructors
    public Response() {
        this.timestamp = Instant.now();
    }

    public Response(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = Instant.now();
    }

    // ✅ Factory methods for convenience
    public static <T> Response<T> success(T data) {
        return new Response<>(200, "Success", data);
    }

    public static <T> Response<T> created(T data) {
        return new Response<>(201, "Created", data);
    }

    public static <T> Response<T> error(int status, String message) {
        return new Response<>(status, message, null);
    }
}
