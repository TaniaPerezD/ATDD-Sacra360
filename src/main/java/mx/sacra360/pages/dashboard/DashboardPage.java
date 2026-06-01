package mx.sacra360.pages.dashboard;

import mx.sacra360.base.BasePage;
import mx.sacra360.pages.reportes.ReportesPage;
import mx.sacra360.pages.sacramentos.SacramentosPage;
import org.openqa.selenium.By;

public class DashboardPage extends BasePage {

    // Localizadores — ajustar según el HTML real del sistema
    private final By tituloBienvenida  = By.cssSelector("h1.welcome-title");
    private final By menuReportes      = By.linkText("Reportes");
    private final By menuSacramentos   = By.linkText("Sacramentos");
    private final By widgetVentas      = By.id("widget-ventas");

    public String obtenerTitulo() {
        return getText(tituloBienvenida);
    }

    public boolean widgetVentasVisible() {
        return isDisplayed(widgetVentas);
    }

    public ReportesPage irAReportes() {
        click(menuReportes);
        return new ReportesPage();
    }

    public SacramentosPage irASacramentos() {
        click(menuSacramentos);
        return new SacramentosPage();
    }
}
