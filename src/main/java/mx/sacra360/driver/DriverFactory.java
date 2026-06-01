package mx.sacra360.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import mx.sacra360.config.ConfigManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

public class DriverFactory {

    private static final ThreadLocal<WebDriver> driverHolder = new ThreadLocal<>();

    private DriverFactory() {}

    public static WebDriver getDriver() {
        if (driverHolder.get() == null) {
            driverHolder.set(createDriver());
        }
        return driverHolder.get();
    }

    public static void quitDriver() {
        if (driverHolder.get() != null) {
            driverHolder.get().quit();
            driverHolder.remove();
        }
    }

    private static WebDriver createDriver() {
        String browser = ConfigManager.getBrowser().toLowerCase();
        boolean headless = ConfigManager.isHeadless();
        WebDriver driver;

        switch (browser) {
            case "firefox": {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions opts = new FirefoxOptions();
                if (headless) opts.addArguments("--headless");
                driver = new FirefoxDriver(opts);
                break;
            }
            case "edge": {
                WebDriverManager.edgedriver().setup();
                EdgeOptions opts = new EdgeOptions();
                if (headless) opts.addArguments("--headless");
                driver = new EdgeDriver(opts);
                break;
            }
            default: {
                WebDriverManager.chromedriver().setup();
                ChromeOptions opts = new ChromeOptions();
                if (headless) opts.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage");
                driver = new ChromeDriver(opts);
            }
        }

        driver.manage().window().maximize();
        return driver;
    }
}
