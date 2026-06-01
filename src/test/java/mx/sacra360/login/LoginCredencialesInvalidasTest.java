package mx.sacra360.login;

import mx.sacra360.base.BaseTest;
import mx.sacra360.pages.login.LoginPage;
import mx.sacra360.utils.ReportManager;
import org.testng.Assert;
import org.testng.annotations.Test;

/****************************************
 * Historia de Usuario:
 * Como sistema de autenticación quiero rechazar los intentos de acceso con
 * credenciales inválidas y notificar al usuario del error, para proteger
 * las cuentas del sistema.
 *
 * Prueba de Aceptación / Caso de Prueba TC-AUTH-02:
 * Login con contraseña incorrecta — el sistema muestra error y no autentica
 *
 * PASO 1. Navegar a la página de inicio de sesión
 * PASO 2. Ingresar email válido y contraseña incorrecta
 * PASO 3. Hacer clic en "Iniciar Sesión"
 * PASO 4. Verificar que aparece el diálogo de error "Error al iniciar sesión"
 * PASO 5. Cerrar el diálogo de error
 * PASO 6. Verificar que el usuario permanece en la página de login
 *
 * Resultado Esperado:
 * El sistema muestra el diálogo "Error al iniciar sesión" y el usuario
 * permanece en la página de login sin ser autenticado.
 ****************************************/

// Para ejecutar solo esta prueba:
// mvn clean test -Dtest=LoginCredencialesInvalidasTest

public class LoginCredencialesInvalidasTest extends BaseTest {

    @Test(priority = 2, description = "TC-AUTH-02: Login con contraseña incorrecta muestra error y no autentica")
    public void loginConContrasenaIncorrecta() throws InterruptedException {

        /********** Preparación de la Prueba **********/

        ReportManager.info("PASO 1: Navegando a la página de inicio de sesión");
        LoginPage loginPage = new LoginPage();
        Thread.sleep(2000);

        /********** Lógica de la Prueba **********/

        ReportManager.info("PASO 2 y 3: Ingresando email válido con contraseña incorrecta");
        loginPage.iniciarSesion("dilan.mamani@ucb.edu.bo", "ContraseñaIncorrecta123!");

        /********** Verificación del Resultado Esperado — Assert **********/

        ReportManager.info("PASO 4: Verificando que aparece el diálogo de error");
        Assert.assertTrue(
            loginPage.swalEsVisible(),
            "No apareció el diálogo de error tras ingresar credenciales incorrectas"
        );
        Assert.assertTrue(
            loginPage.obtenerTituloSwal().contains("Error"),
            "El título del diálogo no indica un error. Se obtuvo: " + loginPage.obtenerTituloSwal()
        );

        ReportManager.info("PASO 5: Cerrando el diálogo de error");
        loginPage.confirmarSwal();
        Thread.sleep(600);

        ReportManager.info("PASO 6: Verificando que el usuario permanece en la página de login");
        Assert.assertTrue(
            loginPage.estaEnLoginPage(),
            "El sistema autenticó al usuario a pesar de la contraseña incorrecta"
        );
    }
}
