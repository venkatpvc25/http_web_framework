package com.pvc.backend.http.route;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class HttpMethodResolverRegistry {

    private final ConcurrentMap<Class<? extends Annotation>, HttpMethodResolver> registry = new ConcurrentHashMap<>();

    public void register(Class<? extends Annotation> type, HttpMethodResolver resolver) {
        Objects.requireNonNull(type, "type must not be null");
        Objects.requireNonNull(resolver, "resolver must not be null");
        registry.put(type, resolver);
    }

    public void registerAll(Map<Class<? extends Annotation>, HttpMethodResolver> resolvers) {
        if (resolvers == null)
            return;
        resolvers.forEach(this::register);
    }

    public boolean contains(Class<? extends Annotation> type) {
        Objects.requireNonNull(type, "type must not be null");
        return registry.containsKey(type);
    }

    public Optional<HttpMethodResolver> get(Class<? extends Annotation> type) {
        Objects.requireNonNull(type, "type must not be null");
        return Optional.ofNullable(registry.get(type));
    }

}
