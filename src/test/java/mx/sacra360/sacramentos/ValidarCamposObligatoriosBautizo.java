package mx.sacra360.sacramentos;

import mx.sacra360.base.BaseTest;
import mx.sacra360.config.ConfigManager;
import mx.sacra360.pages.sacramentos.SacramentosPage;
import mx.sacra360.utils.ReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ValidarCamposObligatoriosBautizo extends BaseTest {

    /*
     * Historia de Usuario: Como administrador parroquial quiero que el sistema
     * me impida registrar un sacramento incompleto para garantizar la
     * integridad de los datos.
     *
     * Caso de Prueba TC-898:
     * Verificar que el sistema impide registrar un sacramento si faltan
     * campos obligatorios.
     *
     * PASO 1. Abrir el formulario "Agregar Sacramento" con todos los campos vacíos
     * PASO 2. Verificar que el botón "Registrar Sacramento" está deshabilitado
     * PASO 3. Completar solo los campos de búsqueda (Persona, Padrino, Ministro, Parroquia)
     * PASO 4. Verificar que el botón sigue deshabilitado (faltan Foja, Número y Fecha)
     * PASO 5. Completar los campos restantes (Foja, Número y Fecha)
     * PASO 6. Verificar que el botón se habilita al tener todos los campos completos
     *
     * Resultado Esperado: El botón "Registrar Sacramento" se habilita únicamente
     * cuando todos los campos obligatorios están completos, no antes.
     *
     * Para ejecutar solo este test:
     *   mvn clean test -Dtest=ValidarCamposObligatoriosBautizo
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

    @Test(priority = 1, description = "TC-898: El botón Registrar se habilita solo cuando todos los campos están completos")
    public void validarCamposObligatoriosTest() throws InterruptedException {

        /********** Preparación de la prueba **********/

        ReportManager.info("Dado que el usuario abre el formulario Agregar Sacramento");
        sacramentosPage
                .seleccionarTipoBautizo()
                .abrirPestanaAgregar();

        /*********** VERIFICACIÓN 1 — formulario vacío ***********/

        ReportManager.info("PASO 2 — VERIFICACIÓN: El botón debe estar deshabilitado con todos los campos vacíos");
        Assert.assertFalse(
                sacramentosPage.botonRegistrarHabilitado(),
                "FALLO: El botón estaba habilitado con el formulario vacío");

        /*********** Acción — llenar solo los campos de búsqueda ***********/

        ReportManager.info("PASO 3: Completando solo los campos de búsqueda (Persona, Padrino, Ministro, Parroquia)");
        sacramentosPage
                .ingresarPersona("Navarro")
                .ingresarPadrino("Ricardo")
                .ingresarMinistro("Condori")
                .ingresarParroquia("San Pedro");

        Thread.sleep(1000);

        /*********** VERIFICACIÓN 2 — campos de búsqueda completos, faltan Foja/Número/Fecha ***********/

        ReportManager.info("PASO 4 — VERIFICACIÓN: El botón debe seguir deshabilitado sin Foja, Número y Fecha");
        Assert.assertFalse(
                sacramentosPage.botonRegistrarHabilitado(),
                "FALLO: El botón se habilitó sin completar Foja, Número y Fecha");

        /*********** Acción — completar los campos restantes ***********/

        ReportManager.info("PASO 5: Completando los campos restantes (Foja, Número y Fecha)");
        sacramentosPage
                .ingresarFoja("10")
                .ingresarNumero("25")
                .ingresarFecha("2025-06-15");

        // Pausa para que React procese los cambios y recalcule el estado del botón
        Thread.sleep(2000);

        /*********** VERIFICACIÓN 3 — todos los campos completos ***********/

        ReportManager.info("PASO 6 — VERIFICACIÓN FINAL: El botón debe habilitarse con todos los campos completos");
        Assert.assertTrue(
                sacramentosPage.botonRegistrarHabilitado(),
                "FALLO: El botón no se habilitó a pesar de tener todos los campos completos");
    }
}
