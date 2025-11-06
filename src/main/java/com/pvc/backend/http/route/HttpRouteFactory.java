package com.pvc.backend.http.route;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.pvc.backend.Route;
import com.pvc.backend.annotations.Controller;
import com.pvc.backend.http.params.ParamResolverFactory;
import com.pvc.backend.model.RouteParam;

public class HttpRouteFactory {

    private final ParamResolverFactory paramResolverFactory;
    private final HttpMethodDispatcher routeDispatcher;

    public HttpRouteFactory(ParamResolverFactory paramResolverFactory, HttpMethodDispatcher routeDispatcher) {
        this.paramResolverFactory = Objects.requireNonNull(paramResolverFactory);
        this.routeDispatcher = Objects.requireNonNull(routeDispatcher);
    }

    public Route build(Object controllerInstance, Method method) {
        Objects.requireNonNull(controllerInstance);
        Objects.requireNonNull(method);
        Controller controller = controllerInstance.getClass().getAnnotation(Controller.class);
        String basePath = controller.path();
        Route meta = routeDispatcher.handle(method, basePath);
        if (meta == null) {
            throw new IllegalStateException("No route metadata for method: " + method);
        }
        List<RouteParam> routeParams = paramResolverFactory.build(method);
        if (routeParams == null) {
            routeParams = Collections.emptyList();
        }
        return meta.toBuilder().routeParams(routeParams).build();
    }
}
