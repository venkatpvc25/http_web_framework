package com.pvc.backend.http.info;

import com.sun.net.httpserver.HttpExchange;

public class PathParamInspector implements RequestInspector {
    @Override
    public boolean matches(HttpExchange exchange, String templatePath) {
        if (templatePath == null || templatePath.isBlank())
            return false;
        String requestPath = normalize(exchange.getRequestURI().getPath());
        String tpl = normalize(templatePath);
        String[] rs = requestPath.isEmpty() ? new String[0] : requestPath.split("/");
        String[] ts = tpl.isEmpty() ? new String[0] : tpl.split("/");
        for (int i = 0; i < Math.min(rs.length, ts.length); i++) {
            if (ts[i].startsWith("{") && ts[i].endsWith("}")) {
                String val = rs[i];
                if (val != null && !val.isBlank())
                    return true;
            }
        }
        return false;
    }

    private String normalize(String p) {
        if (p == null || p.isBlank())
            return "";
        String s = p.trim();
        while (s.startsWith("/"))
            s = s.substring(1);
        while (s.endsWith("/"))
            s = s.substring(0, s.length() - 1);
        return s;
    }
}
