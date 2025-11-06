package com.pvc.backend.http.route;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

import com.pvc.backend.Route;

public class HttpMethodDispatcher {
    private final HttpMethodResolverRegistry registry;

    public HttpMethodDispatcher(HttpMethodResolverRegistry registry) {
        this.registry = registry;
    }

    public Route handle(Method method, String basePath) {
        Objects.requireNonNull(method, "annotations must not be null");

        for (Annotation annotation : method.getAnnotations()) {
            Class<? extends Annotation> type = annotation.annotationType();
            Optional<HttpMethodResolver> resolverOpt = registry.get(type);
            if (resolverOpt.isPresent()) {
                return resolverOpt.get().handle(method, basePath);
            }
        }
        return null;
    }

}
