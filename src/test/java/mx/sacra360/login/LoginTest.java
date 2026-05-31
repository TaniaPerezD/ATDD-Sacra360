package mx.sacra360.login;

import mx.sacra360.base.BaseTest;
import mx.sacra360.config.ConfigManager;
import mx.sacra360.pages.dashboard.DashboardPage;
import mx.sacra360.pages.login.LoginPage;
import mx.sacra360.utils.ReportManager;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(description = "Login exitoso con credenciales válidas")
    public void loginExitosoConCredencialesValidas() {
        ReportManager.info("Dado que el usuario está en la página de login");
        LoginPage loginPage = new LoginPage();

        ReportManager.info("Cuando ingresa credenciales válidas");
        DashboardPage dashboard = loginPage
                .ingresarUsuario(ConfigManager.getTestUser())
                .ingresarPassword(ConfigManager.getTestPassword())
                .clickIngresar();

        ReportManager.info("Entonces debe ver el dashboard");
        Assert.assertTrue(dashboard.getPageTitle().contains("Dashboard"),
                "El título de la página no corresponde al dashboard");
    }

    @Test(description = "Login fallido con contraseña incorrecta")
    public void loginFallidoConPasswordIncorrecto() {
        ReportManager.info("Dado que el usuario está en la página de login");
        LoginPage loginPage = new LoginPage();

        ReportManager.info("Cuando ingresa una contraseña incorrecta");
        loginPage.ingresarUsuario(ConfigManager.getTestUser())
                 .ingresarPassword("contraseñaInvalida123")
                 .clickIngresarEsperandoError();

        ReportManager.info("Entonces debe ver un mensaje de error");
        Assert.assertTrue(loginPage.errorEsVisible(),
                "No se mostró el mensaje de error esperado");
    }

    @Test(description = "Login fallido con campos vacíos")
    public void loginFallidoConCamposVacios() {
        ReportManager.info("Dado que el usuario está en la página de login sin llenar datos");
        LoginPage loginPage = new LoginPage();

        ReportManager.info("Cuando intenta ingresar sin datos");
        loginPage.clickIngresarEsperandoError();

        ReportManager.info("Entonces debe ver un mensaje de error");
        Assert.assertTrue(loginPage.errorEsVisible(),
                "No se mostró el mensaje de validación");
    }
}
