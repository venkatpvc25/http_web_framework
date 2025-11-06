package com.pvc.backend;

import com.pvc.backend.config.YamlConfigLoader;
import com.pvc.backend.utils.ConfigUtils;
import com.pvc.backend.utils.impl.ResponseUtilsImpl;
import com.sun.net.httpserver.HttpServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;

public class WebServer {
    private static final Logger log = LoggerFactory.getLogger(WebServer.class);
    private final int port;
    private final HttpServer server;
    private final Map<String, Object> appConfig;
    private final RouteHandler routeHandler;

    public WebServer(String packageName) throws IOException {
        this.appConfig = new YamlConfigLoader().loadAny("application.yml", "/application.yml");
        this.port = appConfig.get("port") == null ? 8081 : (int) appConfig.get("port");
        ensurePortIsAvailable();
        this.routeHandler = new RouteHandler(packageName);
        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        log.info("Server initialized on port {}", port);
        start();
    }

    private void ensurePortIsAvailable() throws IOException {

        if (port < 1 || port > 65535) {
            log.error("Invalid port number: {}. Port must be between 1 and 65535.", port);
            throw new IllegalArgumentException("Port number must be between 1 and 65535.");
        }

        if (port < 1024) {
            log.warn("Port {} is a privileged port. Ensure you have the necessary permissions.", port);
        }

        if (!isPortAvailable(port)) {
            log.error("Port {} is already in use. Cannot start the server.", port);
            throw new IOException("Port " + port + " is already in use.");
        }
    }

    private boolean isPortAvailable(int port) {
        try (var socket = new java.net.ServerSocket(port)) {
            socket.setReuseAddress(true);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void start() throws IOException {
        String appContext = "/";
        if (appConfig != null) {
            appContext = ConfigUtils.getString(appConfig, "app.context", "/");
        }
        log.info("app context: {}", appContext);
        server.createContext(appContext,
                new HttpRequestHandler(routeHandler, new ResponseUtilsImpl(), appConfig));
        server.start();
        log.info("✅ HTTP server started at http://localhost:{}", port);
    }
}