package com.bart.example.infrastructure.webserving.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class RouteDispatcher implements HttpHandler {
    private final Map<String, AbstractContextHandler> contextHandler = new HashMap<>();

    protected abstract  Optional<AbstractContextHandler> getHandler(String path);

    @Override
    public void handle(HttpExchange exchange) {
        try {
            String method = exchange.getRequestMethod().toUpperCase();
            String path = exchange.getRequestURI().getPath();
            Optional<AbstractContextHandler> handler = getHandler(path);
            if (handler.isPresent()) {
                handler.get().handle(exchange);
            } else {
                sendError(exchange, 404, "Not Found: " + method + " " + path);
            }
        } catch (Exception e) {
            sendError(exchange, 500, "Internal Server Error: " + e.getMessage());
        }
    }

    private void sendError(HttpExchange exchange, int statusCode, String message) {
        try {
            exchange.sendResponseHeaders(statusCode, message.length());
            try (var os = exchange.getResponseBody()) {
                os.write(message.getBytes());
            }
        } catch (IOException e) {
            // Ignore
        } finally {
            exchange.close();
        }
    }
}