package com.pvc.backend.http.info;

public class RequestInfo {
    public final boolean hasPathParams;
    public final boolean hasQueryParams;
    public final boolean hasBody;

    public RequestInfo(boolean hasPathParams, boolean hasQueryParams, boolean hasBody) {
        this.hasPathParams = hasPathParams;
        this.hasQueryParams = hasQueryParams;
        this.hasBody = hasBody;
    }
}
