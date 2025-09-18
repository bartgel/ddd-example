package com.bart.example.controller;

import com.bart.example.infrastructure.injector.annotations.Singleton;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;

@Singleton
public class BaseController {
    private void invokeMethod(Method method, HttpExchange exchange) throws IOException {
        try {
            Object result = method.invoke(this, exchange);
            sendResponse(exchange, result);
        } catch (Exception e) {
            exchange.sendResponseHeaders(500, -1);
        }
    }

    private void sendResponse(HttpExchange exchange, Object result) throws IOException {
        try {
            if (result instanceof String) {
                exchange.getResponseHeaders().set("Content-Type", "text/plain");
                exchange.sendResponseHeaders(200, ((String) result).length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(((String) result).getBytes());
                }
            }
            // Handle other response types
        } finally {
            exchange.close();
        }
    }
}
