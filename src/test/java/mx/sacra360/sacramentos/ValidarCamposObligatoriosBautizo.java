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
 * Como administrador parroquial quiero que el sistema me impida registrar
 * un sacramento incompleto para garantizar la integridad de los datos.
 *
 * Prueba de Aceptación / Caso de Prueba TC-898:
 * El botón Registrar Sacramento se habilita solo cuando todos los campos están completos
 *
 * PASO 1. Iniciar sesión con credenciales de administrador parroquial
 * PASO 2. Navegar al módulo Sacramentos
 * PASO 3. Seleccionar el tipo "Bautizo" y abrir la pestaña "Agregar Sacramento"
 * PASO 4. Verificar que el botón "Registrar Sacramento" está deshabilitado con el formulario vacío
 * PASO 5. Completar solo los campos de búsqueda (Persona, Padrino, Ministro, Parroquia)
 * PASO 6. Verificar que el botón sigue deshabilitado sin Foja, Número y Fecha
 * PASO 7. Completar los campos restantes (Foja, Número y Fecha del Sacramento)
 * PASO 8. Verificar que el botón "Registrar Sacramento" se habilita con todos los campos completos
 *
 * Resultado Esperado:
 * El botón "Registrar Sacramento" se habilita únicamente cuando todos
 * los campos obligatorios están completos, no antes.
 ****************************************/

// Comando para ejecutar esta prueba específica desde la terminal:
// mvn clean test -Dtest=ValidarCamposObligatoriosBautizo

// ===================================================================

public class ValidarCamposObligatoriosBautizo extends BaseTest {

    private SacramentosPage sacramentosPage;

    @BeforeMethod
    public void iniciarSesionEIrASacramentos() throws InterruptedException {
        sacramentosPage = new SacramentosPage();
        Thread.sleep(2000);
        // PASO 1. Iniciar sesión con credenciales de administrador parroquial
        sacramentosPage.iniciarSesion(ConfigManager.getSacramentosUser(), ConfigManager.getSacramentosPassword());
        Thread.sleep(3000);
        // PASO 2. Navegar al módulo Sacramentos
        sacramentosPage.navegarASacramentos();
    }

    @Test(priority = 1, description = "TC-898: El botón Registrar se habilita solo cuando todos los campos están completos")
    public void validarCamposObligatoriosTest() throws InterruptedException {

        /********** Preparación de la Prueba **********/

        // PASO 3. Seleccionar el tipo "Bautizo" y abrir la pestaña "Agregar Sacramento"
        ReportManager.info("PASO 3: Seleccionando tipo Bautizo y abriendo la pestaña Agregar Sacramento");
        sacramentosPage
                .seleccionarTipoBautizo()
                .abrirPestanaAgregar();
        Thread.sleep(1000);

        /********** Lógica de la Prueba **********/

        // PASO 4. Verificar que el botón está deshabilitado con el formulario vacío
        ReportManager.info("PASO 4: Verificando que el botón está deshabilitado con todos los campos vacíos");
        Assert.assertFalse(
                sacramentosPage.botonRegistrarHabilitado(),
                "FALLO: El botón estaba habilitado con el formulario vacío");

        // PASO 5. Completar solo los campos de búsqueda (Persona, Padrino, Ministro, Parroquia)
        ReportManager.info("PASO 5: Completando solo los campos de búsqueda (Persona, Padrino, Ministro, Parroquia)");
        sacramentosPage
                .ingresarPersona("Navarro")
                .ingresarPadrino("Ricardo")
                .ingresarMinistro("Condori")
                .ingresarParroquia("San Pedro");
        Thread.sleep(1000);

        // PASO 6. Verificar que el botón sigue deshabilitado sin Foja, Número y Fecha
        ReportManager.info("PASO 6: Verificando que el botón sigue deshabilitado sin Foja, Número y Fecha");
        Assert.assertFalse(
                sacramentosPage.botonRegistrarHabilitado(),
                "FALLO: El botón se habilitó sin completar Foja, Número y Fecha");

        // PASO 7. Completar los campos restantes (Foja, Número y Fecha del Sacramento)
        ReportManager.info("PASO 7: Completando los campos restantes (Foja, Número y Fecha del Sacramento)");
        sacramentosPage
                .ingresarFoja("10")
                .ingresarNumero("25")
                .ingresarFecha("2025-06-15");
        Thread.sleep(2000);

        /********** Verificación del Resultado Esperado - Assert **********/

        // PASO 8. Verificar que el botón se habilita con todos los campos completos
        ReportManager.info("PASO 8: Verificando que el botón se habilita con todos los campos completos");
        Assert.assertTrue(
                sacramentosPage.botonRegistrarHabilitado(),
                "FALLO: El botón no se habilitó a pesar de tener todos los campos completos");
    }
}
