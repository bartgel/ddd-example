package com.bart.example.infrastructure.webserving.ports;
import com.bart.example.infrastructure.webserving.handlers.RouteDispatcher;
import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class LightweightServer {
    private final HttpServer server;
    private final ExecutorService executor;

    public LightweightServer(int port, int threadPoolSize, RouteDispatcher routeDispatcher) throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        this.executor = Executors.newFixedThreadPool(threadPoolSize);
        this.server.setExecutor(executor);
        this.server.createContext("/", routeDispatcher);
    }

    public void start() {
        server.start();
        System.out.println("Server started on port " + server.getAddress().getPort());
    }

    public void stop() {
        server.stop(0);
        executor.shutdown();
    }
}
