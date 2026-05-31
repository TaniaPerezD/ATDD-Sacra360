package mx.sacra360.pages.reportes;

import mx.sacra360.base.BasePage;
import org.openqa.selenium.By;

public class ReportesPage extends BasePage {

    // Localizadores — ajustar según el HTML real del sistema
    private final By filtroFechaDesde = By.id("fecha-desde");
    private final By filtroFechaHasta = By.id("fecha-hasta");
    private final By botonGenerar     = By.id("btn-generar");
    private final By tablaResultados  = By.id("tabla-resultados");
    private final By mensajeSinDatos  = By.cssSelector(".no-data-message");

    public ReportesPage establecerFechaDesde(String fecha) {
        type(filtroFechaDesde, fecha);
        return this;
    }

    public ReportesPage establecerFechaHasta(String fecha) {
        type(filtroFechaHasta, fecha);
        return this;
    }

    public ReportesPage generarReporte() {
        click(botonGenerar);
        return this;
    }

    public boolean tablaResultadosVisible() {
        return isDisplayed(tablaResultados);
    }

    public boolean mensajeSinDatosVisible() {
        return isDisplayed(mensajeSinDatos);
    }
}
