package com.pvc.backend.http.info;

import java.util.ArrayList;
import java.util.List;

public class RequestInspectorRegistry {
    private final List<RequestInspector> strategies = new ArrayList<>();

    public void register(RequestInspector s) {
        strategies.add(s);
    }

    public List<RequestInspector> snapshot() {
        return List.copyOf(strategies);
    }
}
