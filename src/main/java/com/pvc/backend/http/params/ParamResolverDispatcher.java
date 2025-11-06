package com.pvc.backend.http.params;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import com.pvc.backend.model.RouteParam;

public class ParamResolverDispatcher {
    private final ParamResolverRegistry registry;

    public ParamResolverDispatcher(ParamResolverRegistry registry) {
        this.registry = registry;
    }

    public RouteParam handle(Parameter method) {
        for (Annotation annotation : method.getAnnotations()) {
            ParamResolver resolver = registry.get(annotation.annotationType());
            if (resolver != null) {
                return resolver.handle(method);
            }
        }
        return null;
    }
}
