package mx.sacra360.sacramentos;

import mx.sacra360.base.BaseTest;
import mx.sacra360.config.ConfigManager;
import mx.sacra360.pages.sacramentos.SacramentosPage;
import mx.sacra360.utils.ReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class BuscarBautizoPorApellidoPaterno extends BaseTest {

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
     *
     * Para ejecutar solo este test:
     *   mvn clean test -Dtest=BuscarBautizoPorApellidoPaterno
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

    @Test(priority = 3, description = "TC-882: Buscar sacramento por nombre de persona")
    public void buscarSacramentoPorNombreTest() throws InterruptedException {

        /********** Preparación de la prueba **********/

        ReportManager.info("Dado que el usuario está en el módulo Sacramentos pestaña Buscar/Editar");
        sacramentosPage
                .seleccionarTipoBautizo()
                .abrirPestanaBuscarEditar()
                .expandirFiltros();

        /*********** Lógica de la prueba ***********/

        ReportManager.info("Cuando ingresa 'Pérez' en el filtro de apellido paterno y hace click en Buscar");
        sacramentosPage
                .ingresarFiltroApellidoPaterno("Pérez")
                .clickBuscar();

        Thread.sleep(2500);

        /************ Verificación del resultado esperado — Assert ***************/

        ReportManager.info("Entonces la tabla debe mostrar al menos un resultado que contenga 'Pérez'");

        Assert.assertTrue(
                sacramentosPage.cantidadResultados() >= 1,
                "Se esperaba al menos 1 resultado pero la tabla está vacía");

        Assert.assertTrue(
                sacramentosPage.obtenerNombrePrimerResultado().contains("Pérez"),
                "El primer resultado no contiene 'Pérez'. Se obtuvo: "
                        + sacramentosPage.obtenerNombrePrimerResultado());
    }
}
