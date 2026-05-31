package mx.sacra360.dashboard;

import mx.sacra360.base.BaseTest;
import mx.sacra360.config.ConfigManager;
import mx.sacra360.pages.dashboard.DashboardPage;
import mx.sacra360.pages.login.LoginPage;
import mx.sacra360.utils.ReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DashboardTest extends BaseTest {

    private DashboardPage dashboard;

    @BeforeMethod
    public void iniciarSesion() {
        dashboard = new LoginPage()
                .ingresarUsuario(ConfigManager.getTestUser())
                .ingresarPassword(ConfigManager.getTestPassword())
                .clickIngresar();
    }

    @Test(description = "Dashboard muestra widgets principales al iniciar sesión")
    public void dashboardMuestraWidgetsPrincipales() {
        ReportManager.info("Dado que el usuario inició sesión correctamente");
        ReportManager.info("Entonces debe ver el widget de ventas en el dashboard");

        Assert.assertTrue(dashboard.widgetVentasVisible(),
                "El widget de ventas no está visible en el dashboard");
    }

    @Test(description = "Navegación desde dashboard hacia el módulo de Reportes")
    public void dashboardNavegaHaciaReportes() {
        ReportManager.info("Dado que el usuario está en el dashboard");
        ReportManager.info("Cuando hace clic en el menú Reportes");
        dashboard.irAReportes();

        ReportManager.info("Entonces la URL debe contener 'reportes'");
        Assert.assertTrue(dashboard.getCurrentUrl().contains("reportes"),
                "No se navegó correctamente al módulo de reportes");
    }
}
