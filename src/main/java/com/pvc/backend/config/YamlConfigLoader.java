package com.pvc.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class YamlConfigLoader {

    private final ObjectMapper mapper;

    public YamlConfigLoader() {
        this.mapper = new ObjectMapper(new YAMLFactory());
    }

    /**
     * Load YAML from classpath (e.g. "application.yml" or
     * "config/application.yml").
     * Returns null if not found.
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> loadFromClasspath(String classpathResource) throws IOException {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try (InputStream is = cl.getResourceAsStream(classpathResource)) {
            if (is == null)
                return null;
            return mapper.readValue(is, Map.class);
        }
    }

    /**
     * Load YAML from file system.
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> loadFromFile(File file) throws IOException {
        return mapper.readValue(file, Map.class);
    }

    /**
     * Try classpath first, then fallback to filesystem path.
     * Use like: load("application.yml", "config/application.yml",
     * "/etc/myapp/application.yml")
     */
    public Map<String, Object> loadAny(String... possiblePaths) throws IOException {
        for (String path : possiblePaths) {
            // try classpath
            Map<String, Object> fromClasspath = loadFromClasspath(path);
            if (fromClasspath != null)
                return fromClasspath;

            // try filesystem
            File f = new File(path);
            if (f.exists() && f.isFile()) {
                return loadFromFile(f);
            }
        }
        throw new IOException("None of the config locations found: " + String.join(", ", possiblePaths));
    }

    public <T> T toPojo(Object node, Class<T> clazz) {
        return mapper.convertValue(node, clazz);
    }
}
