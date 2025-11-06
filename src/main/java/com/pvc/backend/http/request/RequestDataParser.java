package com.pvc.backend.http.request;

public interface RequestDataParser<T, R> {
    /**
     * Perform processing for the component. Implementations decide semantics.
     */
    R process(T component);

    /**
     * Optionally resolve the component to a result of type R.
     * Implementations may return null if not applicable.
     */
    default <R> R resolve(T component) {
        return null;
    }
}
