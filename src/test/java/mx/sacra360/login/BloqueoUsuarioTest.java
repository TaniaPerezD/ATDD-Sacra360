package mx.sacra360.login;

import mx.sacra360.base.BaseTest;
import mx.sacra360.pages.login.LoginPage;
import mx.sacra360.pages.usuarios.UsuarioPage;
import mx.sacra360.utils.ReportManager;
import org.testng.Assert;
import org.testng.annotations.Test;

/****************************************
 * Historia de Usuario:
 * Como sistema de seguridad quiero bloquear automáticamente una cuenta tras
 * múltiples intentos fallidos de inicio de sesión para prevenir ataques de
 * fuerza bruta y proteger las cuentas de los usuarios.
 *
 * Prueba de Aceptación / Caso de Prueba TC-AUTH-03:
 * Bloqueo de cuenta tras 5 intentos fallidos consecutivos
 *
 * PASO 1.   Navegar a la página de inicio de sesión
 * PASO 2-6. Realizar 5 intentos fallidos con contraseña incorrecta
 *           (cada intento dispara el SweetAlert de error)
 * PASO 7.   Verificar que el sistema muestra el mensaje de cuenta bloqueada
 *
 * LIMPIEZA. Desbloquear automáticamente la cuenta vía API para permitir
 *           la re-ejecución de este test sin intervención manual.
 *
 * Resultado Esperado:
 * Tras el 5° intento fallido el sistema bloquea la cuenta y muestra
 * el mensaje de bloqueo en el formulario de login.
 *
 * NOTA: El umbral de bloqueo (max_intentos_fallidos) está configurado
 *       en ConfiguracionSeguridad. El valor por defecto es 5.
 ****************************************/

// Para ejecutar solo esta prueba:
// mvn clean test -Dtest=BloqueoUsuarioTest

public class BloqueoUsuarioTest extends BaseTest {

    private static final String EMAIL_PRUEBA    = "dilan.mamani@ucb.edu.bo";
    private static final String PASS_INCORRECTA = "ClaveInvalida_999!";
    private static final int    MAX_INTENTOS    = 5;

    @Test(priority = 3, description = "TC-AUTH-03: Bloqueo de cuenta tras 5 intentos fallidos de login")
    public void bloqueoTrasMultiplesIntentosFallidos() throws Exception {

        /********** Preparación de la Prueba **********/

        ReportManager.info("PASO 1: Navegando a la página de inicio de sesión");
        LoginPage loginPage = new LoginPage();
        Thread.sleep(2000);

        /********** Lógica de la Prueba — 5 intentos fallidos **********/

        for (int intento = 1; intento <= MAX_INTENTOS; intento++) {
            ReportManager.info(
                "PASO " + (intento + 1) + ": Intento fallido " + intento + " de " + MAX_INTENTOS
            );
            loginPage.iniciarSesion(EMAIL_PRUEBA, PASS_INCORRECTA);
            Thread.sleep(2500); // esperar respuesta de la API y SweetAlert de error

            if (intento < MAX_INTENTOS) {
                loginPage.confirmarSwal(); // cerrar SweetAlert antes del siguiente intento
                Thread.sleep(600);
            }
            // En el último intento se deja el SweetAlert para verificar el bloqueo debajo
        }

        /********** Verificación del Resultado Esperado — Assert **********/

        ReportManager.info("PASO 7: Verificando mensaje de bloqueo de cuenta");
        Assert.assertTrue(
            loginPage.mensajeBloqueadoEsVisible(),
            "No apareció el mensaje de bloqueo tras " + MAX_INTENTOS + " intentos fallidos"
        );
        Assert.assertTrue(
            loginPage.obtenerMensajeBloqueo().toLowerCase().contains("bloquead"),
            "El mensaje de bloqueo no es el esperado. Se obtuvo: "
                + loginPage.obtenerMensajeBloqueo()
        );

        Thread.sleep(800);

        /********** LIMPIEZA: desbloquear cuenta vía API para re-ejecución **********/

        ReportManager.info("LIMPIEZA: Obteniendo token de administrador");
        String token  = UsuarioPage.obtenerToken();

        ReportManager.info("LIMPIEZA: Buscando ID de la cuenta de prueba");
        int idUsuario = UsuarioPage.obtenerIdUsuarioPorEmail(token, EMAIL_PRUEBA);

        ReportManager.info("LIMPIEZA: Desbloqueando cuenta " + EMAIL_PRUEBA);
        UsuarioPage.desbloquearUsuario(token, idUsuario);

        ReportManager.info("LIMPIEZA: Cuenta desbloqueada correctamente — test re-ejecutable");
    }
}
