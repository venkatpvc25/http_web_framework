package com.pvc.backend.http.params;

import java.lang.reflect.Parameter;
import java.util.Objects;

import com.pvc.backend.annotations.RequestParam;
import com.pvc.backend.model.RouteParam;

public class RequestParamResolver implements ParamResolver {

    @Override
    public RouteParam handle(Parameter parameter) {
        Objects.requireNonNull(parameter, "parameter must not be null");

        RequestParam annotation = parameter.getAnnotation(RequestParam.class);
        if (annotation == null) {
            return null;
        }

        int index = findParameterIndex(parameter);

        String name = annotation.value();
        if (name == null || name.isBlank()) {
            name = parameter.getName();
        }

        boolean required = true;
        try {
            required = annotation.required();
        } catch (Exception e) {
            // annotation might not have required(), ignore safely
        }

        return RouteParam.builder()
                .index(index)
                .name(name)
                .type(parameter.getType())
                .isRequired(required)
                .build();
    }

    private int findParameterIndex(Parameter parameter) {
        Parameter[] params = parameter.getDeclaringExecutable().getParameters();
        for (int i = 0; i < params.length; i++) {
            if (params[i].equals(parameter)) {
                return i;
            }
        }
        return -1;
    }

}
