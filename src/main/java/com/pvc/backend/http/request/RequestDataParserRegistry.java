package com.pvc.backend.http.request;

import java.util.EnumMap;
import java.util.Map;

import com.pvc.backend.constants.RequestDataType;

public class RequestDataParserRegistry<T> {
    private final Map<RequestDataType, RequestDataParser<T, ?>> registry = new EnumMap<>(RequestDataType.class);

    public void register(RequestDataType type, RequestDataParser<T, ?> parser) {
        registry.put(type, parser);
    }

    @SuppressWarnings("unchecked")
    public <R> RequestDataParser<T, R> get(RequestDataType type) {
        return (RequestDataParser<T, R>) registry.get(type);
    }

    public boolean contains(RequestDataType type) {
        return registry.containsKey(type);
    }
}
