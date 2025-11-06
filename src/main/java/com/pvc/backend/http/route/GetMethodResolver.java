package com.pvc.backend.http.route;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Objects;

import com.pvc.backend.Route;
import com.pvc.backend.annotations.GetMapping;
import com.pvc.backend.constants.MethodType;

public class GetMethodResolver implements HttpMethodResolver {

    @Override
    public Route handle(Method method, String basePath) {
        Objects.requireNonNull(method, "method must not be null");
        for (Annotation annotation : method.getAnnotations()) {
            if (annotation instanceof GetMapping gm) {
                String path = "/app" + basePath + normalizePath(gm.value());
                return Route.builder()
                        .method(MethodType.GET)
                        .handler(method)
                        .path(path)
                        .build();
            }
        }
        return null;
    }

    private static String normalizePath(String raw) {
        if (raw == null || raw.isBlank())
            return "/";
        String p = raw.trim();
        if (!p.startsWith("/"))
            p = "/" + p;
        return p;
    }

}
