package utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.InputStream;
import java.io.InputStreamReader;

public class EnvFactory {

    private static EnvConfig config;

    static {
        try {
            InputStream is = EnvFactory.class
                    .getClassLoader()
                    .getResourceAsStream("config/env.json");
            JsonReader reader = new JsonReader(new InputStreamReader(is));
            config = new Gson().fromJson(reader, EnvConfig.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read env.json", e);
        }
    }

    public static String getBrowser() {
        return config.browser;
    }

    public static boolean isHeadless() {
        return config.headless;
    }

    public static String getBaseUrl() {
        return config.baseUrl;
    }

    public static String getUsername() {
        return config.username;
    }

    public static String getPassword() {
        return config.password;
    }

    public static int getImplicitTimeout() {
        return config.timeouts.implicit;
    }

    public static int getPageLoadTimeout() {
        return config.timeouts.pageLoad;
    }

    // internal class, matches JSON structure
    private static class EnvConfig {
        String browser;
        boolean headless;
        String baseUrl;
        String username;
        String password;
        Timeouts timeouts;

        static class Timeouts {
            int implicit;
            int pageLoad;
        }
    }
}
