package mx.sacra360.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

    private static final Properties props = new Properties();

    static {
        try (InputStream in = ConfigManager.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (in == null) {
                throw new RuntimeException("No se encontró config.properties en el classpath");
            }
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar config.properties", e);
        }
    }

    private ConfigManager() {}

    public static String get(String key) {
        // Las variables de entorno tienen prioridad sobre config.properties (útil en CI/CD)
        String envValue = System.getenv(key.replace(".", "_").toUpperCase());
        return envValue != null ? envValue : props.getProperty(key);
    }

    public static String getBaseUrl()       { return get("base.url"); }
    public static String getBrowser()       { return get("browser"); }
    public static boolean isHeadless()      { return Boolean.parseBoolean(get("headless")); }
    public static int getImplicitWait()     { return Integer.parseInt(get("implicit.wait")); }
    public static int getExplicitWait()     { return Integer.parseInt(get("explicit.wait")); }
    public static String getTestUser()           { return get("test.user"); }
    public static String getTestPassword()       { return get("test.password"); }
    public static String getSacramentosUser()    { return get("sacramentos.user"); }
    public static String getSacramentosPassword(){ return get("sacramentos.password"); }
    public static String getScreenshotDir() { return get("screenshot.dir"); }
}
