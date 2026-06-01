package mx.sacra360.sacramentos;

import mx.sacra360.base.BaseTest;
import mx.sacra360.config.ConfigManager;
import mx.sacra360.pages.sacramentos.SacramentosPage;
import mx.sacra360.utils.ReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/****************************************
 * Historia de Usuario:
 * Como secretario parroquial quiero registrar un nuevo bautizo
 * para mantener el registro sacramental actualizado.
 *
 * Prueba de Aceptación / Caso de Prueba TC-850:
 * Registrar un nuevo bautizo con datos válidos
 *
 * PASO 1. Iniciar sesión con credenciales de secretario parroquial
 * PASO 2. Navegar al módulo Sacramentos
 * PASO 3. Seleccionar el tipo de sacramento "Bautizo"
 * PASO 4. Abrir la pestaña "Agregar Sacramento"
 * PASO 5. Ingresar la persona bautizada en el campo de búsqueda
 * PASO 6. Ingresar el padrino en el campo de búsqueda
 * PASO 7. Ingresar el ministro en el campo de búsqueda
 * PASO 8. Ingresar la parroquia en el campo de búsqueda
 * PASO 9. Completar los campos de Foja, Número y Fecha del Sacramento
 * PASO 10. Hacer clic en el botón "Registrar Sacramento"
 *
 * Resultado Esperado:
 * Se muestra un Toast verde con el mensaje "Sacramento registrado correctamente"
 ****************************************/

// Comando para ejecutar esta prueba específica desde la terminal:
// mvn clean test -Dtest=RegistrarBautizo

// ===================================================================

public class RegistrarBautizo extends BaseTest {

    private SacramentosPage sacramentosPage;

    @BeforeMethod
    public void iniciarSesionEIrASacramentos() throws InterruptedException {
        sacramentosPage = new SacramentosPage();
        Thread.sleep(2000);
        // PASO 1. Iniciar sesión con credenciales de secretario parroquial
        sacramentosPage.iniciarSesion(ConfigManager.getSacramentosUser(), ConfigManager.getSacramentosPassword());
        Thread.sleep(3000);
        // PASO 2. Navegar al módulo Sacramentos
        sacramentosPage.navegarASacramentos();
    }

    @Test(priority = 2, description = "TC-850: Registrar un nuevo bautizo correctamente")
    public void registrarBautizoTest() throws InterruptedException {

        /********** Preparación de la Prueba **********/

        // PASO 3. Seleccionar el tipo de sacramento "Bautizo"
        ReportManager.info("PASO 3: Seleccionando el tipo de sacramento Bautizo");
        sacramentosPage.seleccionarTipoBautizo();
        Thread.sleep(1000);

        // PASO 4. Abrir la pestaña "Agregar Sacramento"
        ReportManager.info("PASO 4: Abriendo la pestaña Agregar Sacramento");
        sacramentosPage.abrirPestanaAgregar();
        Thread.sleep(1000);

        /********** Lógica de la Prueba **********/

        // PASO 5. Ingresar la persona bautizada en el campo de búsqueda
        ReportManager.info("PASO 5: Ingresando la persona bautizada en el campo de búsqueda");
        sacramentosPage.ingresarPersona("Orlando");
        Thread.sleep(800);

        // PASO 6. Ingresar el padrino en el campo de búsqueda
        ReportManager.info("PASO 6: Ingresando el padrino en el campo de búsqueda");
        sacramentosPage.ingresarPadrino("Ricardo");
        Thread.sleep(800);

        // PASO 7. Ingresar el ministro en el campo de búsqueda
        ReportManager.info("PASO 7: Ingresando el ministro en el campo de búsqueda");
        sacramentosPage.ingresarMinistro("Condori");
        Thread.sleep(800);

        // PASO 8. Ingresar la parroquia en el campo de búsqueda
        ReportManager.info("PASO 8: Ingresando la parroquia en el campo de búsqueda");
        sacramentosPage.ingresarParroquia("Sagrado");
        Thread.sleep(800);

        // PASO 9. Completar los campos de Foja, Número y Fecha del Sacramento
        ReportManager.info("PASO 9: Completando los campos de Foja, Número y Fecha del Sacramento");
        sacramentosPage
                .ingresarFoja("B-15")
                .ingresarNumero("42")
                .ingresarFecha("2025-03-17");
        Thread.sleep(800);

        // PASO 10. Hacer clic en el botón "Registrar Sacramento"
        ReportManager.info("PASO 10: Haciendo clic en el botón Registrar Sacramento");
        sacramentosPage.clickRegistrar();
        Thread.sleep(2500);

        /********** Verificación del Resultado Esperado - Assert **********/

        ReportManager.info("VERIFICACIÓN: Confirmando que aparece el Toast de éxito con el mensaje correcto");
        Assert.assertTrue(
                sacramentosPage.toastExitoVisible(),
                "No apareció el Toast de éxito tras registrar el bautizo");

        Assert.assertTrue(
                sacramentosPage.obtenerMensajeToast().contains("Sacramento registrado correctamente"),
                "El mensaje del Toast no es el esperado. Se obtuvo: "
                        + sacramentosPage.obtenerMensajeToast());
    }
}
