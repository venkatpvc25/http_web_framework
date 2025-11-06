package com.pvc.backend;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pvc.backend.config.YamlConfigLoader;
import com.pvc.backend.model.Response;
import com.pvc.backend.utils.ResponseUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class HttpRequestHandler implements HttpHandler {

    private static final Logger log = LoggerFactory.getLogger(HttpRequestHandler.class);
    private final RouteHandler routeHandler;
    private final ResponseUtils responseUtils;
    private final Map<String, Object> appConfig;

    public HttpRequestHandler(RouteHandler routeHandler, ResponseUtils responseUtils, Map<String, Object> appConfig)
            throws IOException {
        this.routeHandler = routeHandler;
        this.responseUtils = responseUtils;
        this.appConfig = appConfig;
    }

    @Override
    public void handle(HttpExchange request) throws IOException {
        String path = request.getRequestURI().getPath();
        String method = request.getRequestMethod();
        log.info("path: {}", request.getRequestURI().getQuery());
        Route route = routeHandler.findRoute(path, method);
        log.info("route: {}", request.getRequestURI().getPath());
        if (route != null) {
            try {
                Response<?> response = route.invoke();
                responseUtils.send(request, response);
            } catch (Exception e) {
                log.error("Error invoking route handler", e);
                // respond with 500 or similar
            }
        } else {

            responseUtils.sendError(request, 404, "route not found");

        }

        log.info(request.getRequestMethod());
    }
}
