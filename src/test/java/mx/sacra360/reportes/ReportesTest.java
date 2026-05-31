package mx.sacra360.reportes;

import mx.sacra360.base.BaseTest;
import mx.sacra360.config.ConfigManager;
import mx.sacra360.pages.login.LoginPage;
import mx.sacra360.pages.reportes.ReportesPage;
import mx.sacra360.utils.ReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ReportesTest extends BaseTest {

    private ReportesPage reportesPage;

    @BeforeMethod
    public void iniciarSesionEIrAReportes() {
        reportesPage = new LoginPage()
                .ingresarUsuario(ConfigManager.getTestUser())
                .ingresarPassword(ConfigManager.getTestPassword())
                .clickIngresar()
                .irAReportes();
    }

    @Test(description = "Generar reporte con rango de fechas válido muestra resultados")
    public void generarReporteConFechasValidasMuestraResultados() {
        ReportManager.info("Dado que el usuario está en el módulo de Reportes");

        ReportManager.info("Cuando genera un reporte con rango de fechas válido");
        reportesPage
                .establecerFechaDesde("2024-01-01")
                .establecerFechaHasta("2024-12-31")
                .generarReporte();

        ReportManager.info("Entonces debe ver la tabla de resultados");
        Assert.assertTrue(reportesPage.tablaResultadosVisible(),
                "La tabla de resultados no se mostró");
    }

    @Test(description = "Generar reporte sin datos en el rango muestra mensaje informativo")
    public void generarReporteSinDatosMuestraMensaje() {
        ReportManager.info("Dado que el usuario está en el módulo de Reportes");

        ReportManager.info("Cuando genera un reporte para un rango sin datos");
        reportesPage
                .establecerFechaDesde("2000-01-01")
                .establecerFechaHasta("2000-01-02")
                .generarReporte();

        ReportManager.info("Entonces debe ver el mensaje de sin datos");
        Assert.assertTrue(reportesPage.mensajeSinDatosVisible(),
                "No se mostró el mensaje de sin datos");
    }
}
