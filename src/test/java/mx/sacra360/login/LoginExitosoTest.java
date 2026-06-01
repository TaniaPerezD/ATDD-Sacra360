package mx.sacra360.login;

import mx.sacra360.base.BaseTest;
import mx.sacra360.pages.login.LoginPage;
import mx.sacra360.pages.usuarios.UsuarioPage;
import mx.sacra360.utils.ReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/****************************************
 * Historia de Usuario:
 * Como usuario registrado quiero poder iniciar sesión con mis credenciales
 * válidas para acceder al sistema y, al terminar, cerrar mi sesión de forma
 * segura para proteger mi cuenta.
 *
 * Prueba de Aceptación / Caso de Prueba TC-AUTH-01:
 * Login exitoso con credenciales válidas y cierre de sesión
 *
 * PASO 1. Navegar a la página de inicio de sesión
 * PASO 2. Ingresar email válido y contraseña correcta
 * PASO 3. Hacer clic en "Iniciar Sesión"
 * PASO 4. Verificar que aparece el diálogo de bienvenida "¡Bienvenido!"
 * PASO 5. Verificar que el sistema redirige al dashboard (salió de /login)
 * PASO 6. Hacer clic en el botón de cerrar sesión
 * PASO 7. Confirmar el cierre de sesión en el diálogo "¿Cerrar sesión?"
 * PASO 8. Verificar que el sistema regresa a la página de login
 *
 * Resultado Esperado:
 * El sistema autentica al usuario, muestra bienvenida, redirige al dashboard
 * y al cerrar sesión regresa a /login.
 ****************************************/

// Para ejecutar solo esta prueba:
// mvn clean test -Dtest=LoginExitosoTest

public class LoginExitosoTest extends BaseTest {

    @BeforeClass(alwaysRun = true)
    public void desbloquearCuentaDilan() throws Exception {
        String token    = UsuarioPage.obtenerToken();
        int    idUsuario = UsuarioPage.obtenerIdUsuarioPorEmail(token, "dilan.mamani@ucb.edu.bo");
        UsuarioPage.desbloquearUsuario(token, idUsuario);
    }

    @Test(priority = 1, description = "TC-AUTH-01: Login exitoso con credenciales válidas y cierre de sesión")
    public void loginExitosoYCierreSesion() throws InterruptedException {

        /********** Preparación de la Prueba **********/

        ReportManager.info("PASO 1: Navegando a la página de inicio de sesión");
        LoginPage loginPage = new LoginPage();
        Thread.sleep(2000);

        /********** Lógica de la Prueba **********/

        ReportManager.info("PASO 2 y 3: Ingresando credenciales válidas y haciendo clic en Iniciar Sesión");
        loginPage.iniciarSesion("dilan.mamani@ucb.edu.bo", "AVFLash2403.0");

        /********** Verificación — Bienvenida **********/

        ReportManager.info("PASO 4: Verificando que aparece el diálogo de bienvenida");
        Assert.assertTrue(
            loginPage.swalEsVisible(),
            "No apareció el diálogo de bienvenida tras el login exitoso"
        );
        Assert.assertTrue(
            loginPage.obtenerTituloSwal().contains("Bienvenido"),
            "El título del diálogo no es el esperado. Se obtuvo: " + loginPage.obtenerTituloSwal()
        );

        Thread.sleep(2500); // esperar que el SweetAlert auto-cierre (timer: 1500 ms) + redirección

        /********** Verificación — Acceso al sistema **********/

        ReportManager.info("PASO 5: Verificando redirección al dashboard");
        Assert.assertFalse(
            loginPage.estaEnLoginPage(),
            "El sistema no redirigió al dashboard tras el login exitoso"
        );

        /********** Lógica — Cierre de sesión **********/

        ReportManager.info("PASO 6 y 7: Haciendo clic en cerrar sesión y confirmando");
        loginPage.cerrarSesion();

        /********** Verificación — Regreso al login **********/

        ReportManager.info("PASO 8: Verificando que el sistema regresó a la página de login");
        Assert.assertTrue(
            loginPage.estaEnLoginPage(),
            "El sistema no redirigió al login tras cerrar sesión"
        );
    }
}
