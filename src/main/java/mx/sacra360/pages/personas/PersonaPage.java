package mx.sacra360.pages.personas;

import mx.sacra360.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.List;

/**
 * Page Object para el módulo Gestión de Personas.
 *
 * Cubre las pestañas:
 *   - Agregar Persona  → formulario de alta
 *   - Buscar Persona   → panel de filtros + tabla de resultados
 *
 * Los selectores se basan en el código fuente de:
 *   fronttaller0/src/features/personas/
 *
 * Los inputs del FilterPanel siguen el patrón  id="f-{name}"
 * Los inputs del formulario Agregar Persona siguen el patrón  id="{name}"
 */
public class PersonaPage extends BasePage {

    // -------------------------------------------------------------------------
    // LOGIN
    // -------------------------------------------------------------------------
    private final By campoEmail         = By.id("email");
    private final By campoPassword      = By.id("password");
    private final By botonIniciarSesion = By.xpath("//*[@id='root']/div/div[1]/div/div[2]/button");

    // -------------------------------------------------------------------------
    // NAVEGACIÓN — Pestañas principales
    // HTML: <button> generado por PageTabs con key='agregar' / 'buscar'
    // -------------------------------------------------------------------------
    private final By tabAgregarPersona = By.xpath("//button[normalize-space(text())='Agregar Persona']");
    private final By tabBuscarPersona  = By.xpath("//button[normalize-space(text())='Buscar Persona']");

    // -------------------------------------------------------------------------
    // FORMULARIO — Agregar Persona
    // Los campos usan id={name} según personasForm.js → personaFields
    // -------------------------------------------------------------------------
    private final By campoNombre          = By.id("nombre");
    private final By campoApellidoPaterno = By.id("apellido_paterno");
    private final By campoApellidoMaterno = By.id("apellido_materno");
    private final By campoCi              = By.id("carnet_identidad");
    private final By campoFechaNac        = By.id("fecha_nacimiento");
    private final By campoLugarNac        = By.id("lugar_nacimiento");
    private final By campoNombrePadre     = By.id("nombre_padre");
    private final By campoNombreMadre     = By.id("nombre_madre");
    private final By selectEstadoForm     = By.id("activo");
    private final By selectEstadoVerif    = By.id("estado");

    // Botones del formulario Agregar
    private static final String AGREGAR_FORM_ANCESTOR =
    "//form[ancestor::div[.//h3[normalize-space(text())='Datos Personales']]]";
    // Botones del formulario Agregar
    private final By botonAgregarPersona = By.xpath(
        AGREGAR_FORM_ANCESTOR + "//button[@type='submit']"
    );
    private final By botonLimpiarForm = By.xpath(
        AGREGAR_FORM_ANCESTOR + "//button[normalize-space(text())='Limpiar']"
    );

    // -------------------------------------------------------------------------
    // PANEL DE FILTROS — Buscar Persona
    // El panel usa FilterPanel → los inputs tienen id="f-{name}"
    // El botón que expande/colapsa contiene el título del panel
    // -------------------------------------------------------------------------
    private final By botonPanelFiltros        = By.xpath("//button[.//h3[contains(text(),'Buscar Persona')]]");

    // Inputs de búsqueda (FilterPanel usa id="f-{fieldName}")
    private final By filtroNombre             = By.id("nombre");
    private final By filtroApellidoPaterno    = By.id("apellido_paterno");
    private final By filtroApellidoMaterno    = By.id("apellido_materno");
    private final By filtroCi                 = By.id("carnet_identidad");
    private final By filtroLugarNac           = By.id("lugar_nacimiento");
    private final By filtroNombrePadre        = By.id("nombre_padre");
    private final By filtroNombreMadre        = By.id("nombre_madre");
    private final By filtroEstado             = By.id("activo");
    private final By filtroEstadoVerif        = By.id("estado");

    // Botones del panel de filtros
    private final By botonBuscar              = By.xpath("//button[normalize-space(text())='Buscar']");
    private final By botonLimpiarFiltros      = By.xpath("//button[normalize-space(text())='Limpiar']");

    // -------------------------------------------------------------------------
    // TABLA DE RESULTADOS
    // -------------------------------------------------------------------------
    // Cabeceras de columna
    private final By columnas             = By.xpath("//table//thead/tr/th");
    // Filas de resultados
    private final By filasTabla           = By.xpath("//table//tbody/tr");
    // Primera celda de la primera fila (Nombre)
    private final By primeraFilaNombre    = By.xpath("//table//tbody/tr[1]/td[1]");
    // CI en la primera fila (columna 4 → índice 4)
    private final By primeraFilaCi        = By.xpath("//table//tbody/tr[1]/td[4]");
    // Estado en la primera fila (columna 7)
    private final By primeraFilaEstado    = By.xpath("//table//tbody/tr[1]/td[7]");

    // -------------------------------------------------------------------------
    // TOAST / MENSAJES
    // -------------------------------------------------------------------------
    private final By toastExito  = By.xpath("//div[@role='alert' and contains(@class,'emerald')]");
    private final By toastError  = By.xpath("//div[@role='alert' and contains(@class,'red')]");

    // =========================================================================
    // MÉTODOS DE LOGIN Y NAVEGACIÓN
    // =========================================================================

    public void iniciarSesion(String email, String password) throws InterruptedException {
        Thread.sleep(1000);
        type(campoEmail, email);
        Thread.sleep(800);
        type(campoPassword, password);
        Thread.sleep(600);
        click(botonIniciarSesion);
    }

    public void navegarAPersonas() throws InterruptedException {
        driver.get("https://fronttaller0.vercel.app/personas");
        waitForClickable(tabAgregarPersona);
        Thread.sleep(2000);
    }

    public void abrirTabAgregarPersona() throws InterruptedException {
        click(tabAgregarPersona);
        Thread.sleep(1000);
    }

    public void abrirTabBuscarPersona() throws InterruptedException {
        click(tabBuscarPersona);
        Thread.sleep(1000);
    }

    // =========================================================================
    // MÉTODOS DEL FORMULARIO — AGREGAR PERSONA
    // =========================================================================

    public void ingresarNombreForm(String nombre) throws InterruptedException {
        type(campoNombre, nombre);
        Thread.sleep(400);
    }

    public void ingresarApellidoPaternoForm(String apellido) throws InterruptedException {
        type(campoApellidoPaterno, apellido);
        Thread.sleep(400);
    }

    public void ingresarApellidoMaternoForm(String apellido) throws InterruptedException {
        type(campoApellidoMaterno, apellido);
        Thread.sleep(400);
    }

    public void ingresarCiForm(String ci) throws InterruptedException {
        type(campoCi, ci);
        Thread.sleep(400);
    }

    public void ingresarFechaNacForm(String isoDate) throws InterruptedException {
        setDateValue(campoFechaNac, isoDate);
        Thread.sleep(400);
    }

    public void ingresarLugarNacForm(String lugar) throws InterruptedException {
        type(campoLugarNac, lugar);
        Thread.sleep(400);
    }

    public void ingresarNombrePadreForm(String nombre) throws InterruptedException {
        type(campoNombrePadre, nombre);
        Thread.sleep(400);
    }

    public void ingresarNombreMadreForm(String nombre) throws InterruptedException {
        type(campoNombreMadre, nombre);
        Thread.sleep(400);
    }

    public void seleccionarEstadoForm(String valor) throws InterruptedException {
        WebElement el = waitForVisible(selectEstadoForm);
        new Select(el).selectByValue(valor);
        Thread.sleep(400);
    }

    public void seleccionarEstadoVerifForm(String valor) throws InterruptedException {
        WebElement el = waitForVisible(selectEstadoVerif);
        new Select(el).selectByValue(valor);
        Thread.sleep(400);
    }

    public void clickAgregarPersona() throws InterruptedException {
        click(botonAgregarPersona);
        Thread.sleep(500);
    }

    public void clickLimpiarForm() throws InterruptedException {
        click(botonLimpiarForm);
        Thread.sleep(500);
    }

    // =========================================================================
    // MÉTODOS DEL PANEL DE FILTROS — BUSCAR PERSONA
    // =========================================================================

    public void expandirFiltros() throws InterruptedException {
        click(botonPanelFiltros);
        Thread.sleep(800);
    }

    public void ingresarFiltroNombre(String nombre) throws InterruptedException {
        type(filtroNombre, nombre);
        Thread.sleep(400);
    }

    public void ingresarFiltroApellidoPaterno(String apellido) throws InterruptedException {
        type(filtroApellidoPaterno, apellido);
        Thread.sleep(400);
    }

    public void ingresarFiltroApellidoMaterno(String apellido) throws InterruptedException {
        type(filtroApellidoMaterno, apellido);
        Thread.sleep(400);
    }

    public void ingresarFiltroCi(String ci) throws InterruptedException {
        type(filtroCi, ci);
        Thread.sleep(400);
    }

    public void seleccionarFiltroEstado(String valor) throws InterruptedException {
        WebElement el = waitForVisible(filtroEstado);
        new Select(el).selectByValue(valor);
        Thread.sleep(400);
    }

    public void seleccionarFiltroEstadoVerif(String valor) throws InterruptedException {
        WebElement el = waitForVisible(filtroEstadoVerif);
        new Select(el).selectByValue(valor);
        Thread.sleep(400);
    }

    public void clickBuscar() throws InterruptedException {
        click(botonBuscar);
        Thread.sleep(2500);
    }

    public void clickLimpiarFiltros() throws InterruptedException {
        click(botonLimpiarFiltros);
        Thread.sleep(800);
    }

    // =========================================================================
    // VERIFICACIONES
    // =========================================================================

    /** Cantidad de filas en la tabla de resultados */
    public int cantidadResultados() {
        List<WebElement> filas = driver.findElements(filasTabla);
        return filas.size();
    }

    /** Texto de la celda Nombre de la primera fila */
    public String obtenerNombrePrimerResultado() {
        return getText(primeraFilaNombre);
    }

    /** Texto de la celda CI de la primera fila */
    public String obtenerCiPrimerResultado() {
        return getText(primeraFilaCi);
    }

    /** Texto visible del estado en la primera fila */
    public String obtenerEstadoPrimerResultado() {
        return getText(primeraFilaEstado);
    }

    /**
     * Verifica que la tabla tenga las columnas esperadas para Buscar Persona:
     * Nombre, Apellido paterno, Apellido materno, CI, Fecha nac., Lugar nac.,
     * Estado, Estado de verificación.
     */
    public boolean columnasCorrectas() {
        List<WebElement> ths = driver.findElements(columnas);
        if (ths.size() < 8) return false;
        String[] esperadas = {
            "Nombre", "Apellido paterno", "Apellido materno",
            "CI", "Fecha nac.", "Lugar nac.",
            "Estado", "Estado de verificación"
        };
        for (int i = 0; i < esperadas.length; i++) {
            if (!ths.get(i).getText().trim().equalsIgnoreCase(esperadas[i])) return false;
        }
        return true;
    }

    /**
     * Verifica que el campo CI del filtro esté vacío (valor = "").
     */
    public boolean filtroCiEstaVacio() {
        try {
            WebElement el = waitForVisible(filtroCi);
            return el.getAttribute("value").isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica que todos los inputs del formulario Agregar estén vacíos/en estado inicial.
     */
    public boolean formularioAgregarEstaLimpio() {
        try {
            return driver.findElement(campoNombre).getAttribute("value").isEmpty()
                && driver.findElement(campoApellidoPaterno).getAttribute("value").isEmpty()
                && driver.findElement(campoApellidoMaterno).getAttribute("value").isEmpty()
                && driver.findElement(campoCi).getAttribute("value").isEmpty()
                && driver.findElement(campoLugarNac).getAttribute("value").isEmpty()
                && driver.findElement(campoNombrePadre).getAttribute("value").isEmpty()
                && driver.findElement(campoNombreMadre).getAttribute("value").isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica que el botón Agregar Persona esté deshabilitado (disabled=true).
     * El botón se deshabilita cuando isFormValid === false.
     */
    public boolean botonAgregarEstaDeshabilitado() {
        try {
            WebElement btn = driver.findElement(botonAgregarPersona);
            return btn.getAttribute("disabled") != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica que los campos del panel de filtros estén vacíos/en "Todos".
     */
    public boolean filtrosEstanLimpios() {
        try {
            boolean nombreVacio = driver.findElement(filtroNombre).getAttribute("value").isEmpty();
            boolean apPaternoVacio = driver.findElement(filtroApellidoPaterno).getAttribute("value").isEmpty();
            boolean ciVacio = driver.findElement(filtroCi).getAttribute("value").isEmpty();
            WebElement selEstado = driver.findElement(filtroEstado);
            String estadoVal = new Select(selEstado).getFirstSelectedOption().getAttribute("value");
            WebElement selVerif = driver.findElement(filtroEstadoVerif);
            String verifVal = new Select(selVerif).getFirstSelectedOption().getAttribute("value");
            return nombreVacio && apPaternoVacio && ciVacio
                && (estadoVal == null || estadoVal.isEmpty())
                && (verifVal == null || verifVal.isEmpty());
        } catch (Exception e) {
            return false;
        }
    }

    /** ¿Es visible el toast de éxito? */
    public boolean toastExitoVisible() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(6));
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(toastExito));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
