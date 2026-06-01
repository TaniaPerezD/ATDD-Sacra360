package mx.sacra360.sacramentos;

import mx.sacra360.base.BaseTest;
import mx.sacra360.config.ConfigManager;
import mx.sacra360.pages.sacramentos.SacramentosPage;
import mx.sacra360.utils.ReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SacramentosTest extends BaseTest {

    /*
     * Historia de Usuario:
     *   Como administrador parroquial quiero gestionar los sacramentos
     *   para mantener el registro sacramental de la parroquia actualizado.
     *
     * Para ejecutar solo este módulo:
     *   mvn clean test -Dsuite=sacramentos
     *
     * Para ejecutar un único test:
     *   mvn clean test -Dtest=SacramentosTest#registrarBautizoTest
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

    // =========================================================================
    // TC-850 — Registrar bautizo
    // =========================================================================

    /*
     * Historia de Usuario: Como secretario parroquial quiero registrar
     * un nuevo bautizo para mantener el registro sacramental actualizado.
     *
     * 
     * Verificar que es posible registrar un nuevo bautizo en el sistema.
     *
     * PASO 1. Seleccionar tipo "Bautizo" y abrir pestaña "Agregar Sacramento"
     * PASO 2. Completar todos los campos: Persona, Padrino, Ministro,
     *         Parroquia, Foja, Número y Fecha del Sacramento
     * PASO 3. Hacer click en "Registrar Sacramento"
     *
     * Resultado Esperado: Toast verde con "Sacramento registrado correctamente"
     */
    @Test(priority = 2, description = "TC: Registrar un nuevo bautizo correctamente")
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

    // =========================================================================
    // TC-882 — Buscar sacramento por nombre de persona
    // =========================================================================

    /*
     * Historia de Usuario: Como administrador parroquial quiero buscar
     * un sacramento por nombre de persona para consultar registros existentes.
     *
     * Caso de Prueba TC-882:
     * Verificar que el sistema permite buscar un sacramento por criterios
     * de la persona asociada.
     *
     * PASO 1. Seleccionar tipo "Bautizo" y abrir pestaña "Buscar / Editar"
     * PASO 2. Expandir el panel "Filtros de búsqueda"
     * PASO 3. Ingresar nombre en el campo filtro y hacer click en "Buscar"
     *
     * Resultado Esperado: La tabla muestra solo registros de la persona buscada
     * con columnas: Nombre completo, CI, Fecha, Rol, Foja y Número
     */
    @Test(priority = 3, description = "TC-882: Buscar sacramento por nombre de persona")
    public void buscarSacramentoPorNombreTest() throws InterruptedException {

        /********** Preparación de la prueba **********/

        ReportManager.info("Dado que el usuario está en el módulo Sacramentos pestaña Buscar/Editar");
        sacramentosPage
                .seleccionarTipoBautizo()
                .abrirPestanaBuscarEditar()
                .expandirFiltros();

        /*********** Lógica de la prueba ***********/

        ReportManager.info("Cuando ingresa 'Perez' en el filtro de apellido paterno y hace click en Buscar");
        sacramentosPage
                .ingresarFiltroApellidoPaterno("Perez")
                .clickBuscar();

        Thread.sleep(2500);

        /************ Verificación del resultado esperado — Assert ***************/

        ReportManager.info("Entonces la tabla debe mostrar al menos un resultado que contenga 'Perez'");

        Assert.assertTrue(
                sacramentosPage.cantidadResultados() >= 1,
                "Se esperaba al menos 1 resultado pero la tabla está vacía");

        Assert.assertTrue(
                sacramentosPage.obtenerNombrePrimerResultado().toUpperCase().contains("PEREZ"),
                "El primer resultado no contiene 'Perez'. Se obtuvo: "
                        + sacramentosPage.obtenerNombrePrimerResultado());
    }

    // =========================================================================
    // TC-898 — Validación de campos obligatorios
    // =========================================================================

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
     */
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
