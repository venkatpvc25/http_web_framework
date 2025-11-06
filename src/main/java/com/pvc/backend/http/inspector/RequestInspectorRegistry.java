package com.pvc.backend.http.inspector;

import java.util.ArrayList;
import java.util.List;

public class RequestInspectorRegistry {
    private final List<RequestInspector> strategies = new ArrayList<>();

    public void register(RequestInspector s) {
        strategies.add(s);
    }

    public void registerAll(List<RequestInspector> strategies) {
        this.strategies.clear();
        this.strategies.addAll(strategies);
    }

    public List<RequestInspector> snapshot() {
        return List.copyOf(strategies);
    }
}
