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
 * Como digitador quiero buscar un sacramento por nombre de
 * persona para consultar registros existentes en el sistema.
 *
 * Prueba de Aceptacion / Caso de Prueba TC-882:
 * Buscar un bautizo por apellido paterno de la persona
 *
 * PASO 1. Iniciar sesion con credenciales de digitador
 * PASO 2. Navegar al modulo Sacramentos
 * PASO 3. Seleccionar el tipo de sacramento "Bautizo"
 * PASO 4. Abrir la pestana "Buscar / Editar"
 * PASO 5. Expandir el panel "Filtros de busqueda"
 * PASO 6. Ingresar "Perez" en el filtro de apellido paterno
 * PASO 7. Hacer clic en el boton "Buscar"
 * PASO 8. Verificar que la tabla muestra al menos un resultado con "Perez"
 *
 * Resultado Esperado:
 * La tabla muestra solo registros donde el apellido paterno contiene "Perez"
 ****************************************/

// Comando para ejecutar esta prueba especifica desde la terminal:
// mvn clean test -Dtest=BuscarBautizoPorApellidoPaterno

// ===================================================================

public class BuscarBautizoPorApellidoPaterno extends BaseTest {

    private SacramentosPage sacramentosPage;

    @BeforeMethod
    public void iniciarSesionEIrASacramentos() throws InterruptedException {
        sacramentosPage = new SacramentosPage();
        Thread.sleep(2000);
        // PASO 1. Iniciar sesion con credenciales de digitador
        sacramentosPage.iniciarSesion(ConfigManager.getSacramentosUser(), ConfigManager.getSacramentosPassword());
        Thread.sleep(3000);
        // PASO 2. Navegar al modulo Sacramentos
        sacramentosPage.navegarASacramentos();
    }

    @Test(priority = 3, description = "Buscar sacramento por apellido paterno de la persona")
    public void buscarSacramentoPorApellidoPaternoTest() throws InterruptedException {

        /********** Preparacion de la Prueba **********/

        // PASO 3. Seleccionar el tipo de sacramento "Bautizo"
        ReportManager.info("PASO 3: Seleccionando el tipo de sacramento Bautizo");
        sacramentosPage.seleccionarTipoBautizo();
        Thread.sleep(1000);

        // PASO 4. Abrir la pestana "Buscar / Editar"
        ReportManager.info("PASO 4: Abriendo la pestana Buscar / Editar");
        sacramentosPage.abrirPestanaBuscarEditar();
        Thread.sleep(1000);

        // PASO 5. Expandir el panel "Filtros de busqueda"
        ReportManager.info("PASO 5: Expandiendo el panel Filtros de busqueda");
        sacramentosPage.expandirFiltros();
        Thread.sleep(1000);

        /********** Logica de la Prueba **********/

        // PASO 6. Ingresar "Perez" en el filtro de apellido paterno
        ReportManager.info("PASO 6: Ingresando 'Perez' en el filtro de apellido paterno");
        sacramentosPage.ingresarFiltroApellidoPaterno("Pérez");
        Thread.sleep(800);

        // PASO 7. Hacer clic en el boton "Buscar"
        ReportManager.info("PASO 7: Haciendo clic en el boton Buscar");
        sacramentosPage.clickBuscar();
        Thread.sleep(2500);

        /********** Verificacion del Resultado Esperado - Assert **********/

        // PASO 8. Verificar que la tabla muestra al menos un resultado con "Perez"
        ReportManager.info("PASO 8: Verificando que la tabla muestra al menos un resultado con 'Perez'");
        Assert.assertTrue(
                sacramentosPage.cantidadResultados() >= 1,
                "Se esperaba al menos 1 resultado pero la tabla esta vacia");

        Assert.assertTrue(
                sacramentosPage.obtenerNombrePrimerResultado().contains("Pérez"),
                "El primer resultado no contiene 'Perez'. Se obtuvo: "
                        + sacramentosPage.obtenerNombrePrimerResultado());
    }
}
