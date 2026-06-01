package mx.sacra360.sacramentos;

import mx.sacra360.base.BaseTest;
import mx.sacra360.config.ConfigManager;
import mx.sacra360.pages.sacramentos.SacramentosPage;
import mx.sacra360.utils.ReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RegistrarBautizo extends BaseTest {

    /*
     * Historia de Usuario: Como secretario parroquial quiero registrar
     * un nuevo bautizo para mantener el registro sacramental actualizado.
     *
     * Verificar que es posible registrar un nuevo bautizo en el sistema.
     *
     * PASO 1. Seleccionar tipo "Bautizo" y abrir pestaña "Agregar Sacramento"
     * PASO 2. Completar todos los campos: Persona, Padrino, Ministro,
     *         Parroquia, Foja, Número y Fecha del Sacramento
     * PASO 3. Hacer click en "Registrar Sacramento"
     *
     * Resultado Esperado: Toast verde con "Sacramento registrado correctamente"
     *
     * Para ejecutar solo este test:
     *   mvn clean test -Dtest=RegistrarBautizo
     */

    private SacramentosPage sacramentosPage;

    @BeforeMethod
    public void iniciarSesionEIrASacramentos() throws InterruptedException {
        sacramentosPage = new SacramentosPage();
        Thread.sleep(2000);
        sacramentosPage.iniciarSesion(ConfigManager.getSacramentosUser(), ConfigManager.getSacramentosPassword());
        Thread.sleep(3000);
        sacramentosPage.navegarASacramentos();
    }

    @Test(priority = 2, description = "TC-850: Registrar un nuevo bautizo correctamente")
    public void registrarBautizoTest() throws InterruptedException {

        /********** Preparación de la prueba **********/

        ReportManager.info("Dado que el usuario está en el módulo Sacramentos con tipo Bautizo seleccionado");
        sacramentosPage
                .seleccionarTipoBautizo()
                .abrirPestanaAgregar();

        /*********** Lógica de la prueba ***********/

        ReportManager.info("Cuando completa todos los campos obligatorios y hace click en Registrar Sacramento");
        sacramentosPage
                .ingresarPersona("Orlando")
                .ingresarPadrino("Ricardo")
                .ingresarMinistro("Condori")
                .ingresarParroquia("Sagrado")
                .ingresarFoja("B-15")
                .ingresarNumero("42")
                .ingresarFecha("2025-03-17")
                .clickRegistrar();

        /************ Verificación del resultado esperado — Assert ***************/

        ReportManager.info("Entonces debe aparecer el Toast verde con mensaje de éxito");

        Assert.assertTrue(
                sacramentosPage.toastExitoVisible(),
                "No apareció el Toast de éxito tras registrar el bautizo");

        Assert.assertTrue(
                sacramentosPage.obtenerMensajeToast().contains("Sacramento registrado correctamente"),
                "El mensaje del Toast no es el esperado. Se obtuvo: "
                        + sacramentosPage.obtenerMensajeToast());

        Thread.sleep(2500);
    }
}
