package ru.netology;

import java.util.Collections;
import java.util.List;
import java.util.Map;

class Request {
    private final String method;
    private final String path;
    private final Map<String, String> headers;
    private final byte[] body;
    private final Map<String, List<String>> queryParams;

    public Request(
            String method,
            String path,
            Map<String, String> headers,
            byte[] body,
            Map<String, List<String>> queryParams
    ) {
        this.method = method;
        this.path = path;
        this.headers = Collections.unmodifiableMap(headers);
        this.body = body;
        this.queryParams = Collections.unmodifiableMap(queryParams);
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

    public String getQueryParam(String name) {
        List<String> values = queryParams.get(name);
        if (values == null || values.isEmpty()) {
            return null;
        }
        return values.getFirst();
    }

    public Map<String, List<String>> getQueryParams() {
        return queryParams;
    }
}
