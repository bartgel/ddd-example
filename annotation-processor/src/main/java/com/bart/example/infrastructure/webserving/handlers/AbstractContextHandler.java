package com.bart.example.infrastructure.webserving.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class AbstractContextHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {
        try {
            String method = exchange.getRequestMethod().toUpperCase();

            switch (method) {
                case "GET" -> handleGET(exchange);
                case "POST" -> handlePOST(exchange);
                case "PUT" -> handlePUT(exchange);
                case "DELETE" -> handleDELETE(exchange);
                case "PATCH" -> handlePATCH(exchange);
                default -> sendMethodNotAllowed(exchange);
            }
        } catch (Exception e) {
            sendError(exchange, 500, "Internal server error: " + e.getMessage());
        }
    }

    // Abstract methods for each HTTP method
    protected void handleGET(HttpExchange exchange) throws IOException {
        sendMethodNotAllowed(exchange);
    }

    protected void handlePOST(HttpExchange exchange) throws IOException {
        sendMethodNotAllowed(exchange);
    }

    protected void handlePUT(HttpExchange exchange) throws IOException {
        sendMethodNotAllowed(exchange);
    }

    protected void handleDELETE(HttpExchange exchange) throws IOException {
        sendMethodNotAllowed(exchange);
    }

    protected void handlePATCH(HttpExchange exchange) throws IOException {
        sendMethodNotAllowed(exchange);
    }

    // Helper methods
    protected void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        sendResponse(exchange,statusCode,response, "text/plain");
    }

    protected void sendResponseJson(HttpExchange exchange, int statusCode, String response) throws IOException {
        sendResponse(exchange,statusCode,response, "application/json; charset=utf-8");
    }


    protected void sendResponse(HttpExchange exchange, int statusCode, String response, String contentType) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(statusCode, response.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
        exchange.close();
    }

    protected void sendMethodNotAllowed(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(405, -1); // Method Not Allowed
        exchange.close();
    }

    protected void sendError(HttpExchange exchange, int statusCode, String message) {
        try {
            exchange.sendResponseHeaders(statusCode, message.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(message.getBytes());
            }
        } catch (IOException e) {
            // Ignore secondary errors
        } finally {
            exchange.close();
        }
    }
}
