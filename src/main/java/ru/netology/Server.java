package ru.netology;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Server {
    private final ExecutorService threadPool = Executors.newFixedThreadPool(64);
    private final Map<String, Map<String, Handler>> handlers = new ConcurrentHashMap<>();

    public void addHandler(String method, String path, Handler handler) {
        handlers.computeIfAbsent(method, k -> new ConcurrentHashMap<>()).put(path, handler);
    }

    public void listen(int port) {
        try (final var serverSocket = new ServerSocket(port)) {
            while (true) {
                final var socket = serverSocket.accept();
                threadPool.submit(() -> handleConnection(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }

    private void handleConnection(Socket socket) {
        try (socket;
             final var in = new BufferedInputStream(socket.getInputStream());
             final var out = new BufferedOutputStream(socket.getOutputStream())) {

            final var request = parseRequest(in);
            if (request == null) return;

            final var handlerMap = handlers.get(request.getMethod());
            if (handlerMap == null) {
                sendNotFound(out);
                return;
            }

            final var handler = handlerMap.get(request.getPath());
            if (handler == null) {
                sendNotFound(out);
                return;
            }

            handler.handle(request, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Request parseRequest(InputStream in) throws IOException {

        final var reader = new BufferedReader(new InputStreamReader(in));
        final var requestLine = reader.readLine();
        if (requestLine == null || requestLine.isEmpty()) return null;

        final var parts = requestLine.split(" ");
        if (parts.length != 3) return null;
        final var method = parts[0];
        final var path = parts[1];

        final var headers = new HashMap<String, String>();
        String headerLine;
        while (!(headerLine = reader.readLine()).isEmpty()) {
            final var index = headerLine.indexOf(":");
            if (index > 0) {
                final var key = headerLine.substring(0, index).trim();
                final var value = headerLine.substring(index + 1).trim();
                headers.put(key, value);
            }
        }

        byte[] body = new byte[0];
        if (headers.containsKey("Content-Length")) {
            final var contentLength = Integer.parseInt(headers.get("Content-Length"));
            body = new byte[contentLength];
            for (int i = 0; i < contentLength; i++) {
                body[i] = (byte) reader.read();
            }
        }

        return new Request(method, path, headers, body);
    }

    private void sendNotFound(BufferedOutputStream out) throws IOException {
        out.write((
                "HTTP/1.1 404 Not Found\r\n" +
                        "Content-Length: 0\r\n" +
                        "Connection: close\r\n\r\n"
        ).getBytes());
        out.flush();
    }
}
