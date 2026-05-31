package mx.sacra360.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ReportManager {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> testHolder = new ThreadLocal<>();

    private ReportManager() {}

    public static void init() {
        ExtentSparkReporter spark = new ExtentSparkReporter("reports/reporte.html");
        spark.config().setDocumentTitle("Reporte ATDD - Sacra360");
        spark.config().setReportName("Suite de Pruebas de Aceptación");

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Proyecto", "Sacra360");
        extent.setSystemInfo("Framework", "Selenium + TestNG");
    }

    public static void createTest(String nombre) {
        testHolder.set(extent.createTest(nombre));
    }

    public static void pass(String mensaje) {
        testHolder.get().pass(mensaje);
    }

    public static void fail(String mensaje) {
        testHolder.get().fail(mensaje);
    }

    public static void skip(String mensaje) {
        testHolder.get().skip(mensaje);
    }

    public static void info(String mensaje) {
        testHolder.get().info(mensaje);
    }

    public static void addScreenshot(String ruta) {
        try {
            testHolder.get().addScreenCaptureFromPath(ruta);
        } catch (Exception e) {
            System.err.println("No se pudo adjuntar captura al reporte: " + e.getMessage());
        }
    }

    public static void flush() {
        if (extent != null) extent.flush();
    }
}
