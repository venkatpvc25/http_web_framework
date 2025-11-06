package com.pvc.backend.http.request;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import com.sun.net.httpserver.HttpExchange;

import com.pvc.backend.exceptions.RequestDataNotValidException;

public class PathParamDataParser implements RequestDataParser<HttpExchange, Map<String, Object>> {
    private final String tempaltePath;

    PathParamDataParser(String templatePath) {
        this.tempaltePath = templatePath;
    }

    @Override
    public Map<String, Object> process(HttpExchange exchange) {
        String path = Objects.requireNonNull(exchange.getRequestURI().getPath(), "request path must not be null");
        if (tempaltePath == null) {
            throw new RequestDataNotValidException("templatePath must not be null");
        }

        String normalizedRequest = normalize(path);
        String normalizedTemplate = normalize(tempaltePath);

        // quick accept for root path
        if (normalizedTemplate.isEmpty() && normalizedRequest.isEmpty()) {
            return new LinkedHashMap<>();
        }

        String[] segments = splitSegments(normalizedRequest);
        String[] templates = splitSegments(normalizedTemplate);

        return processTemplateWithSegments(templates, segments);
    }

    private Map<String, Object> processTemplateWithSegments(String[] templates, String[] segments) {
        Map<String, Object> params = new LinkedHashMap<>();
        boolean hasWildcard = templates.length > 0 && "**".equals(templates[templates.length - 1]);

        if (!hasWildcard && segments.length != templates.length) {
            throw new RequestDataNotValidException(String.format(
                    "template and request params count not matching: template segments=%d, request segments=%d",
                    templates.length, segments.length));
        }

        for (int i = 0; i < templates.length; i++) {
            String templ = templates[i];

            if ("**".equals(templ)) {
                params.put("wildcard", decode(joinRemainingSegments(segments, i)));
                break;
            }

            if (i >= segments.length) {
                throw new RequestDataNotValidException("request path is shorter than template requires");
            }

            processSegmentTemplate(params, i, templ, segments[i]);
        }

        return params;
    }

    private void processSegmentTemplate(Map<String, Object> params, int index, String templ, String seg) {
        if (isTemplateVariable(templ)) {
            String name = extractVariableName(templ);
            validateVariableName(name, index);
            ensureUniqueParamName(params, name);
            params.put(name, decode(seg));
        } else {
            validateLiteralSegment(templ, seg, index);
        }
    }

    private boolean isTemplateVariable(String templ) {
        return templ.startsWith("{") && templ.endsWith("}");
    }

    private String extractVariableName(String templ) {
        String inner = templ.substring(1, templ.length() - 1).trim();
        int colon = inner.indexOf(':');
        return colon >= 0 ? inner.substring(0, colon).trim() : inner;
    }

    private void validateVariableName(String name, int index) {
        if (name.isEmpty()) {
            throw new RequestDataNotValidException("empty path variable name in template at segment " + index);
        }
    }

    private void ensureUniqueParamName(Map<String, Object> params, String name) {
        if (params.containsKey(name)) {
            throw new RequestDataNotValidException("duplicate path parameter name: " + name);
        }
    }

    private void validateLiteralSegment(String templ, String seg, int index) {
        if (!templ.equals(seg)) {
            throw new RequestDataNotValidException(String.format(
                    "path literal mismatch at segment %d: expected='%s' actual='%s'", index, templ, seg));
        }
    }

    private String[] splitSegments(String normalized) {
        if (normalized.isEmpty())
            return new String[0];
        return normalized.split("/");
    }

    private String joinRemainingSegments(String[] segments, int startIndex) {
        if (startIndex >= segments.length)
            return "";
        return String.join("/", Arrays.copyOfRange(segments, startIndex, segments.length));
    }

    private static String normalize(String p) {
        if (p == null || p.isBlank())
            return "";
        String s = p.trim();
        while (s.startsWith("/"))
            s = s.substring(1);
        while (s.endsWith("/"))
            s = s.substring(0, s.length() - 1);
        return s;
    }

    private static String decode(String raw) {
        if (raw == null)
            return null;
        try {
            return java.net.URLDecoder.decode(raw, java.nio.charset.StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            return raw;
        }
    }

}
