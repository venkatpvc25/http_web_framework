package com.pvc.backend.http.params;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import com.pvc.backend.model.RouteParam;

public class ParamResolverFactory {
    private final ParamResolverDispatcher paramResolverDispatcher;

    public ParamResolverFactory(ParamResolverDispatcher paramResolverDispatcher) {
        this.paramResolverDispatcher = paramResolverDispatcher;
    }

    public List<RouteParam> build(Method method) {
        List<RouteParam> routeParams = new ArrayList<>();
        for (Parameter parameter : method.getParameters()) {
            RouteParam routeParam = paramResolverDispatcher.handle(parameter);
            routeParams.add(routeParam);
        }
        return routeParams;
    }
}
