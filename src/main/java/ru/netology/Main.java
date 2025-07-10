package ru.netology;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();

        server.addHandler("GET", "/", (request, out) -> {
            String content = "Start server";
            String response = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n" + "Content-Length: " + content.length() + "\r\n" + "Connection: close\r\n\r\n" + content;
            out.write(response.getBytes(StandardCharsets.UTF_8));
            out.flush();
        });

        server.addHandler("GET", "/messages", (request, out) -> {
            String content = "GET messages";
            String response = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/plain\r\n" + "Content-Length: " + content.length() + "\r\n" + "Connection: close\r\n\r\n" + content;
            out.write(response.getBytes(StandardCharsets.UTF_8));
            out.flush();
        });

        server.addHandler("POST", "/messages", (request, out) -> {
            String body = new String(request.getBody(), StandardCharsets.UTF_8);
            String content = "Received: " + body;
            String response = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/plain\r\n" + "Content-Length: " + content.length() + "\r\n" + "Connection: close\r\n\r\n" + content;
            out.write(response.getBytes(StandardCharsets.UTF_8));
            out.flush();
        });

        server.addHandler("GET", "/search", (request, out) -> {
            String query = request.getQueryParam("id");
            String content = "ID: " + query;

            String response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/plain\r\n" +
                    "Content-Length: " + content.length() + "\r\n" +
                    "Connection: close\r\n\r\n" + content;

            out.write(response.getBytes(StandardCharsets.UTF_8));
            out.flush();
        });

        server.addHandler("GET", "/search", (request, out) -> {
            // Получаем все параметры запроса
            Map<String, List<String>> allParams = request.getQueryParams();

            StringBuilder contentBuilder = new StringBuilder();
            contentBuilder.append("Query params:\n");

            for (Map.Entry<String, List<String>> entry : allParams.entrySet()) {
                String key = entry.getKey();
                List<String> values = entry.getValue();
                contentBuilder.append(key).append(": ").append(String.join(", ", values)).append("\n");
            }

            String content = contentBuilder.toString();
            String response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/plain\r\n" +
                    "Content-Length: " + content.length() + "\r\n" +
                    "Connection: close\r\n\r\n" + content;

            out.write(response.getBytes(StandardCharsets.UTF_8));
            out.flush();
        });

        server.listen(9999);
    }
}


