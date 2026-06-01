package mx.sacra360.pages.login;

import mx.sacra360.base.BasePage;
import org.openqa.selenium.By;

public class LoginPage extends BasePage {

    // -------------------------------------------------------------------------
    // Formulario de login
    // -------------------------------------------------------------------------
    private final By campoEmail         = By.id("email");
    private final By campoPassword      = By.id("password");
    private final By botonIniciarSesion = By.xpath("//*[@id='root']/div/div[1]/div/div[2]/button");

    // -------------------------------------------------------------------------
    // SweetAlert2 — popup genérico (aplica tanto a éxito como a error)
    // -------------------------------------------------------------------------
    private final By swalTitle         = By.cssSelector(".swal2-title");
    private final By swalConfirm       = By.cssSelector(".swal2-confirm");
    private final By swalHtmlContainer = By.cssSelector(".swal2-html-container");

    // -------------------------------------------------------------------------
    // Cerrar sesión — botón en el header (UserProfile component)
    // HTML: <button aria-label="Cerrar sesión">...</button>
    // -------------------------------------------------------------------------
    private final By botonLogout = By.xpath("//button[@aria-label='Cerrar sesión']");

    // -------------------------------------------------------------------------
    // Recuperación de contraseña — enlace en el formulario de login
    // HTML: <button ...>¿Olvidaste tu contraseña?</button>
    // -------------------------------------------------------------------------
    private final By linkOlvideContrasena   = By.xpath("//button[contains(text(),'Olvidaste')]");
    private final By campoEmailRecuperacion = By.cssSelector("input[type='email'][placeholder='tu@email.com']");
    private final By botonEnviarEnlace      = By.xpath("//button[contains(text(),'Enviar enlace')]");

    // =========================================================================
    // Login
    // =========================================================================

    public void iniciarSesion(String email, String password) throws InterruptedException {
        Thread.sleep(1000);
        type(campoEmail, email);
        Thread.sleep(500);
        type(campoPassword, password);
        Thread.sleep(400);
        click(botonIniciarSesion);
    }

    // =========================================================================
    // SweetAlert2
    // =========================================================================

    public boolean swalEsVisible() {
        try {
            waitForVisible(swalTitle);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String obtenerTituloSwal() {
        return getText(swalTitle);
    }

    public void confirmarSwal() {
        click(swalConfirm);
    }

    // =========================================================================
    // Bloqueo de cuenta
    // =========================================================================

    public boolean mensajeBloqueadoEsVisible() {
        try {
            waitForVisible(swalHtmlContainer);
            return getText(swalHtmlContainer).toLowerCase().contains("bloquead");
        } catch (Exception e) {
            return false;
        }
    }

    public String obtenerMensajeBloqueo() {
        return getText(swalHtmlContainer);
    }

    // =========================================================================
    // Cierre de sesión
    // =========================================================================

    public void cerrarSesion() throws InterruptedException {
        click(botonLogout);
        Thread.sleep(800);
        click(swalConfirm); // "Sí, salir"
        Thread.sleep(2500); // esperar SweetAlert "¡Sesión cerrada!" (timer 1500ms) + redirección
    }

    // =========================================================================
    // Verificación de URL
    // =========================================================================

    public boolean estaEnLoginPage() {
        return isDisplayed(campoEmail) && isDisplayed(campoPassword);
    }

    // =========================================================================
    // Recuperación de contraseña
    // =========================================================================

    public void clickOlvidasteContrasena() {
        click(linkOlvideContrasena);
    }

    public void ingresarEmailRecuperacion(String email) {
        type(campoEmailRecuperacion, email);
    }

    public void clickEnviarEnlaceRecuperacion() {
        click(botonEnviarEnlace);
    }
}
