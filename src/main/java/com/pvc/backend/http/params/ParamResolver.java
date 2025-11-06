package com.pvc.backend.http.params;

import java.lang.reflect.Parameter;

import com.pvc.backend.model.RouteParam;

public interface ParamResolver {
    RouteParam handle(Parameter parameter);
}
