package mx.sacra360.utils;

import mx.sacra360.config.ConfigManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtil {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private ScreenshotUtil() {}

    public static String capturar(WebDriver driver, String nombreTest) {
        String dir = ConfigManager.getScreenshotDir();
        new File(dir).mkdirs();

        String timestamp = LocalDateTime.now().format(FMT);
        String ruta = dir + "/" + nombreTest + "_" + timestamp + ".png";

        try {
            byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Files.write(Paths.get(ruta), bytes);
        } catch (IOException e) {
            System.err.println("No se pudo guardar la captura: " + e.getMessage());
        }
        return ruta;
    }
}
