package mx.sacra360.pages.sacramentos;

import mx.sacra360.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class SacramentosPage extends BasePage {

    // -------------------------------------------------------------------------
    // Login — mismos selectores que UsuarioPage (email / password / botón)
    // -------------------------------------------------------------------------
    private final By campoEmail          = By.id("email");
    private final By campoPassword       = By.id("password");
    private final By botonIniciarSesion  = By.xpath("//*[@id='root']/div/div[1]/div/div[2]/button");

    // -------------------------------------------------------------------------
    // Selector de tipo de sacramento
    // El <input type="radio"> tiene className="hidden" — hay que hacer click
    // en el <label> padre que lo envuelve.
    // HTML: <label><input type="radio" name="tipoSacramento" value="bautizo" class="hidden">...</label>
    // -------------------------------------------------------------------------
    private final By labelBautizo = By.xpath(
        "//label[.//input[@name='tipoSacramento' and @value='bautizo']]"
    );

    // -------------------------------------------------------------------------
    // Pestañas
    // HTML: <button>Agregar Sacramento</button>  /  <button>Buscar / Editar</button>
    // -------------------------------------------------------------------------
    private final By pestanaAgregar      = By.xpath("//button[normalize-space(text())='Agregar Sacramento']");
    private final By pestanaBuscarEditar = By.xpath("//button[normalize-space(text())='Buscar / Editar']");

    // -------------------------------------------------------------------------
    // Formulario de registro — SearchField no tiene id, sólo placeholder
    // HTML: <input type="search" placeholder="Buscar persona por nombre o CI">
    // -------------------------------------------------------------------------
    private final By campoPersona   = By.xpath("//input[@placeholder='Buscar persona por nombre o CI']");
    private final By campoPadrino   = By.xpath("//input[@placeholder='Buscar padrino por nombre o CI']");
    private final By campoMinistro  = By.xpath("//input[@placeholder='Buscar ministro por nombre o CI']");
    private final By campoParroquia = By.xpath("//input[@placeholder='Buscar parroquia por nombre']");

    // Dropdown del autocompletado — los resultados son <button type="button"> dentro
    // de un <div class="... max-h-56 overflow-y-auto">, NO <ul>/<li>
    // HTML: <div class="absolute z-50 ... max-h-56 overflow-y-auto"><button type="button">...</button></div>
    private final By primerResultadoDropdown = By.xpath(
        "//div[contains(@class,'max-h-56') and contains(@class,'overflow-y-auto')]//button[@type='button'][1]"
    );

    // -------------------------------------------------------------------------
    // Campos de CamposComunes — sin id, localizamos por placeholder o por label
    // HTML: <input type="text" placeholder="Ej. 123-A">  /  <input type="date">
    // -------------------------------------------------------------------------
    private final By campoFoja   = By.xpath("//input[@placeholder='Ej. 123-A']");
    private final By campoNumero = By.xpath("//input[@placeholder='Ej. 456']");
    private final By campoFecha  = By.xpath(
        "//label[normalize-space(text())='Fecha del Sacramento']/following-sibling::input[@type='date']"
    );

    // Botón Registrar Sacramento — <button type="submit" disabled={!isFormValid}>
    private final By botonRegistrar = By.xpath("//button[normalize-space(text())='Registrar Sacramento']");

    // -------------------------------------------------------------------------
    // Toast — NO tiene clase .toast ni .success
    // HTML: <div role="alert" class="... bg-emerald-50 ...">
    //         <p class="text-sm font-medium text-emerald-800">Éxito</p>
    //         <p class="text-sm text-gray-500 ...">Sacramento registrado correctamente</p>
    //       </div>
    // -------------------------------------------------------------------------
    private final By toastExito   = By.xpath("//div[@role='alert' and contains(@class,'emerald')]");
    private final By toastMensaje = By.xpath(
        "//div[@role='alert' and contains(@class,'emerald')]//p[contains(@class,'text-gray-500')]"
    );

    // -------------------------------------------------------------------------
    // Panel de filtros y búsqueda
    // El botón que colapsa/expande contiene un <h3>Filtros de búsqueda</h3>
    // HTML: <button type="button"><h3>Filtros de búsqueda</h3>...</button>
    // El id real de los inputs de filtro sigue el patrón: f-{campo}
    // HTML: <input id="f-nombre" ...>
    // -------------------------------------------------------------------------
    private final By botonPanelFiltros = By.xpath(
        "//button[.//h3[contains(text(),'Filtros de búsqueda')]]"
    );
    private final By filtroNombre    = By.id("f-nombre");
    private final By botonBuscar     = By.xpath("//button[normalize-space(text())='Buscar']");
    private final By filasTabla      = By.xpath("//table//tbody/tr");
    private final By primerFilaNombre = By.xpath("//table//tbody/tr[1]/td[1]");

    // =========================================================================
    // Login y navegación — igual que UsuarioPage
    // =========================================================================

    public void iniciarSesion(String email, String password) throws InterruptedException {
        Thread.sleep(1000);
        type(campoEmail, email);
        Thread.sleep(800);
        type(campoPassword, password);
        Thread.sleep(600);
        click(botonIniciarSesion);
    }

    public void navegarASacramentos() throws InterruptedException {
        driver.get("https://fronttaller0.vercel.app/sacramentos");
        Thread.sleep(2000);
    }

    // =========================================================================
    // Métodos de interacción
    // =========================================================================

    public SacramentosPage seleccionarTipoBautizo() {
        click(labelBautizo);
        return this;
    }

    public SacramentosPage abrirPestanaAgregar() {
        click(pestanaAgregar);
        return this;
    }

    public SacramentosPage abrirPestanaBuscarEditar() {
        click(pestanaBuscarEditar);
        return this;
    }

    // Cada campo de autocompletado: escribe el texto y espera el dropdown
    public SacramentosPage ingresarPersona(String nombre) {
        type(campoPersona, nombre);
        waitForClickable(primerResultadoDropdown).click();
        return this;
    }

    public SacramentosPage ingresarPadrino(String nombre) {
        type(campoPadrino, nombre);
        waitForClickable(primerResultadoDropdown).click();
        return this;
    }

    public SacramentosPage ingresarMinistro(String nombre) {
        type(campoMinistro, nombre);
        waitForClickable(primerResultadoDropdown).click();
        return this;
    }

    public SacramentosPage ingresarParroquia(String nombre) {
        type(campoParroquia, nombre);
        waitForClickable(primerResultadoDropdown).click();
        return this;
    }

    public SacramentosPage ingresarFoja(String foja) {
        type(campoFoja, foja);
        return this;
    }

    public SacramentosPage ingresarNumero(String numero) {
        type(campoNumero, numero);
        return this;
    }

    public SacramentosPage ingresarFecha(String isoDate) {
        setDateValue(campoFecha, isoDate);
        return this;
    }

    public SacramentosPage clickRegistrar() {
        click(botonRegistrar);
        return this;
    }

    // =========================================================================
    // Verificaciones
    // =========================================================================

    public boolean toastExitoVisible() {
        try {
            waitForVisible(toastExito);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String obtenerMensajeToast() {
        return getText(toastMensaje);
    }

    public boolean botonRegistrarHabilitado() {
        return driver.findElement(botonRegistrar).isEnabled();
    }

    public SacramentosPage expandirFiltros() {
        click(botonPanelFiltros);
        return this;
    }

    public SacramentosPage ingresarFiltroNombre(String nombre) {
        type(filtroNombre, nombre);
        return this;
    }

    public SacramentosPage clickBuscar() {
        click(botonBuscar);
        return this;
    }

    public String obtenerNombrePrimerResultado() {
        return getText(primerFilaNombre);
    }

    public int cantidadResultados() {
        List<WebElement> filas = driver.findElements(filasTabla);
        return filas.size();
    }
}
