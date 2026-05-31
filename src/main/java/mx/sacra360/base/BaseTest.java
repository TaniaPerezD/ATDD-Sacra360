package mx.sacra360.base;

import mx.sacra360.config.ConfigManager;
import mx.sacra360.driver.DriverFactory;
import mx.sacra360.utils.ReportManager;
import mx.sacra360.utils.ScreenshotUtil;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.lang.reflect.Method;

public abstract class BaseTest {

    protected WebDriver driver;

    @BeforeSuite(alwaysRun = true)
    public void iniciarReporte() {
        ReportManager.init();
    }

    @BeforeMethod(alwaysRun = true)
    public void abrirNavegador(Method method) {
        driver = DriverFactory.getDriver();
        driver.get(ConfigManager.getBaseUrl());
        ReportManager.createTest(method.getName());
    }

    @AfterMethod(alwaysRun = true)
    public void cerrarNavegador(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            String path = ScreenshotUtil.capturar(driver, result.getName());
            ReportManager.addScreenshot(path);
            ReportManager.fail("Test fallido: " + result.getThrowable().getMessage());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            ReportManager.pass("Test exitoso");
        } else {
            ReportManager.skip("Test omitido");
        }
        DriverFactory.quitDriver();
    }

    @AfterSuite(alwaysRun = true)
    public void finalizarReporte() {
        ReportManager.flush();
    }
}
