package com.pvc.backend;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pvc.backend.annotations.Controller;
import com.pvc.backend.annotations.GetMapping;
import com.pvc.backend.annotations.PathVariable;
import com.pvc.backend.annotations.RequestBody;
import com.pvc.backend.annotations.RequestParam;
import com.pvc.backend.constants.RequestDataType;
import com.pvc.backend.http.params.ParamResolverDispatcher;
import com.pvc.backend.http.params.ParamResolverFactory;
import com.pvc.backend.http.params.ParamResolverRegistry;
import com.pvc.backend.http.route.GetMethodResolver;
import com.pvc.backend.http.route.HttpMethodDispatcher;
import com.pvc.backend.http.route.HttpMethodResolver;
import com.pvc.backend.http.route.HttpMethodResolverRegistry;
import com.pvc.backend.http.route.HttpRouteFactory;
import com.pvc.backend.model.RouteParam;
import com.pvc.backend.constants.MethodType;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

public class RouteHandler {
    private List<Route> routes;
    private final String packageName;

    public RouteHandler(String packgeName) {
        this.routes = new ArrayList<>();
        this.packageName = packgeName;
        scanPackage(this.packageName);
    }

    public void scanPackage(String packageName) {
        try (ScanResult scanResult = new ClassGraph().enableAllInfo().acceptPackages(packageName).scan()) {
            for (io.github.classgraph.ClassInfo classInfo : scanResult.getClassesWithAnnotation(Controller.class)) {
                Class<?> cls = classInfo.loadClass();
                Object controllerInstance = registerController(cls); // instantiate & register bean lifecycle
                registerRoutesFromController(controllerInstance);
            }
        }
    }

    private Object registerController(Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate controller: " + controllerClass.getName(), e);
        }
    }

    private void registerRoutesFromController(Object controllerInstance) {
        Class<?> clazz = controllerInstance.getClass();
        ParamResolverDispatcher dispatcher = new ParamResolverDispatcher(new ParamResolverRegistry());

        Map<Class<? extends Annotation>, HttpMethodResolver> hMap = new HashMap<>();
        hMap.put(GetMapping.class, new GetMethodResolver());
        HttpMethodResolverRegistry httpMethodResolverRegistry = new HttpMethodResolverRegistry();
        httpMethodResolverRegistry.registerAll(hMap);
        HttpMethodDispatcher httpMethodResolveDispatcher = new HttpMethodDispatcher(
                httpMethodResolverRegistry);

        HttpRouteFactory routeFactory = new HttpRouteFactory(new ParamResolverFactory(dispatcher),
                httpMethodResolveDispatcher);

        List<RouteParam> routeParams = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            Route route = routeFactory.build(controllerInstance, method);
            routes.add(route.toBuilder().routeParams(routeParams).build());

        }
    }

    public void registerRoute(Object controller) {
        registerRoutesFromController(controller);
    }

    public void addRoute(Route route) {
        routes.add(route);
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public Route findRoute(String path, String method) {
        for (Route route : routes) {
            if (route.getPath().equals(path) && route.getMethod().name().equalsIgnoreCase(method)) {
                return route;
            }
        }
        return null;
    }
}
