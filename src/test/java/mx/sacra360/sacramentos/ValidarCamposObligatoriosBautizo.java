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
     * PASO 1. Abrir el formulario "Agregar Sacramento" con campos vacíos
     * PASO 2. Verificar que el botón "Registrar Sacramento" está deshabilitado
     * PASO 3. Completar todos los campos obligatorios
     * PASO 4. Verificar que el botón se habilita y el registro es exitoso
     *
     * Resultado Esperado: Botón deshabilitado con campos vacíos;
     * se habilita solo cuando todos los campos están completos
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

    @Test(priority = 1, description = "TC-898: El botón Registrar permanece deshabilitado con campos vacíos")
    public void validarCamposObligatoriosTest() {

        /********** Preparación de la prueba **********/

        ReportManager.info("Dado que el usuario abre el formulario Agregar Sacramento con todos los campos vacíos");
        sacramentosPage
                .seleccionarTipoBautizo()
                .abrirPestanaAgregar();

        /*********** Lógica de la prueba — verificación con campos vacíos ***********/

        ReportManager.info("Entonces el botón 'Registrar Sacramento' debe estar deshabilitado");

        Assert.assertFalse(
                sacramentosPage.botonRegistrarHabilitado(),
                "El botón debería estar deshabilitado cuando los campos están vacíos");

        /*********** Lógica de la prueba — completar campos ***********/

        ReportManager.info("Cuando completa todos los campos obligatorios");
        sacramentosPage
                .ingresarPersona("Navarro")
                .ingresarPadrino("Ricardo")
                .ingresarMinistro("Condori")
                .ingresarParroquia("San Pedro")
                .ingresarFoja("10")
                .ingresarNumero("25")
                .ingresarFecha("2025-06-15");

        /************ Verificación del resultado esperado — Assert ***************/

        ReportManager.info("Entonces el botón debe habilitarse cuando todos los campos están completos");

        Assert.assertTrue(
                sacramentosPage.botonRegistrarHabilitado(),
                "El botón debería estar habilitado cuando todos los campos están completos");
    }
}
