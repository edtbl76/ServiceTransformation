package org.emangini.servolution.util.http;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class GradlePropertiesReader {

    private final Properties properties;

    public GradlePropertiesReader() {

        properties = new Properties();

        // Load gradle.properties
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("gradle.properties")) {

            if (input == null) {
                throw new IOException("gradle.properties not found");
            }
            properties.load(input);
        } catch (IOException e) {
            log.error("Unable to load gradle.properties", e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getGradleProperty(String key) {
        return new GradlePropertiesReader().getProperty(key);
    }
}
