package com.pvc.backend.http.params;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import com.pvc.backend.annotations.PathVariable;
import com.pvc.backend.annotations.RequestBody;
import com.pvc.backend.annotations.RequestParam;

public class ParamResolverRegistry {
    private final Map<Class<? extends Annotation>, ParamResolver> registry = new HashMap<>();

    public ParamResolverRegistry() {
        registry.put(PathVariable.class, new PathParamResolver());
        registry.put(RequestParam.class, new RequestParamResolver());
        registry.put(RequestBody.class, new RequestBodyParamResolver());
    }

    public void register(Class<? extends Annotation> annotation, ParamResolver resolver) {
        registry.put(annotation, resolver);
    }

    public ParamResolver get(Class<? extends Annotation> annotation) {
        return registry.get(annotation);
    }

    public boolean contains(Class<? extends Annotation> annotation) {
        return registry.containsKey(annotation);
    }
}
