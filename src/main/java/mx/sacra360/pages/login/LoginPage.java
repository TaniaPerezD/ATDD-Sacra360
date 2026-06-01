package mx.sacra360.pages.login;

import mx.sacra360.base.BasePage;
import mx.sacra360.pages.dashboard.DashboardPage;
import org.openqa.selenium.By;

public class LoginPage extends BasePage {

    private final By campoUsuario    = By.id("email");
    private final By campoPassword   = By.id("password");
    private final By botonIngresar   = By.xpath("//*[@id='root']/div/div[1]/div/div[2]/button");
    private final By mensajeError    = By.cssSelector(".alert-error");

    public LoginPage ingresarUsuario(String usuario) {
        type(campoUsuario, usuario);
        return this;
    }

    public LoginPage ingresarPassword(String password) {
        type(campoPassword, password);
        return this;
    }

    public DashboardPage clickIngresar() {
        click(botonIngresar);
        return new DashboardPage();
    }

    public LoginPage clickIngresarEsperandoError() {
        click(botonIngresar);
        return this;
    }

    public String obtenerMensajeError() {
        return getText(mensajeError);
    }

    public boolean errorEsVisible() {
        return isDisplayed(mensajeError);
    }
}
