package ru.netology;

import java.nio.charset.StandardCharsets;

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

        server.listen(9999);
    }
}


