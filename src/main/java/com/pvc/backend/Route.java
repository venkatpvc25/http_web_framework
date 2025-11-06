package com.pvc.backend;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.pvc.backend.constants.MethodType;
import com.pvc.backend.exceptions.RouteException;
import com.pvc.backend.model.Response;
import com.pvc.backend.model.RouteParam;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder(toBuilder = true)
public class Route {
    private final String path;
    private final Method handler;
    private final MethodType method;
    private final Object controllerInstance;
    private final List<RouteParam> routeParams;

    public Route(String path, Method handler, MethodType method, Object controllerInstance,
            List<RouteParam> routeParams) {
        this.path = path;
        this.handler = handler;
        this.method = method;
        this.controllerInstance = controllerInstance;
        this.routeParams = routeParams;
    }

    public Response<?> invoke(Object... args) throws RouteException {
        try {
            return (Response<?>) handler.invoke(controllerInstance, args);
        } catch (IllegalAccessException e) {
            throw new RouteException("Illegal access to method");
        } catch (InvocationTargetException e) {
            throw new RouteException("Unable to execute method");
        }
    }
}
