package com.pvc.backend.http.route;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.pvc.backend.Route;

public interface HttpMethodResolver {
    Route handle(Method method, String basePath);
}
