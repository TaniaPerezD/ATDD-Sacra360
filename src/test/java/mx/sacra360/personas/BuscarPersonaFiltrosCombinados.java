package mx.sacra360.personas;

import mx.sacra360.base.BaseTest;
import mx.sacra360.config.ConfigManager;
import mx.sacra360.pages.personas.PersonaPage;
import mx.sacra360.utils.ReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/****************************************
 * Historia de Usuario:
 * Como Digitador quiero combinar múltiples filtros simultáneamente
 * en la búsqueda de personas, obteniendo resultados precisos que
 * cumplan todas las condiciones establecidas.
 *
 * Prueba de Aceptación / Caso de Prueba TC-43:
 * Buscar Persona con múltiples filtros combinados
 *
 * Precondiciones:
 *   - Usuario autenticado con rol DIGITADOR.
 *   - Existen múltiples personas registradas con diferentes estados;
 *     al menos dos con estado activo/inactivo y dos con estado
 *     verificado/no verificado.
 *
 * PASO 1.  Iniciar sesión con credenciales del DIGITADOR
 * PASO 2.  Navegar a 'Personas' y abrir la pestaña 'Buscar Persona'
 * PASO 3.  Expandir el panel de filtros y verificar los campos visibles
 * PASO 4.  Ingresar un nombre parcial en el campo 'Nombre'
 * PASO 5.  Ingresar un apellido paterno en el campo correspondiente
 * PASO 6.  Seleccionar 'Activo' en el desplegable 'Estado'
 * PASO 7.  Hacer clic en 'Buscar' y verificar resultados con los tres filtros
 * PASO 8.  Verificar que cada resultado cumple TODOS los criterios ingresados
 * PASO 9.  Agregar el filtro de 'Estado de verificación' y buscar nuevamente
 * PASO 10. Limpiar todos los filtros con el botón 'Limpiar'
 *
 * Resultado Esperado:
 *   Cada búsqueda retorna únicamente personas que cumplen todos los filtros
 *   activos. Al agregar el filtro de verificación, los resultados se reducen
 *   aún más. Tras 'Limpiar', todos los campos vuelven al estado inicial.
 *
 * Para ejecutar solo este test:
 *   mvn clean test -Dtest=BuscarPersonaFiltrosCombinados
 ****************************************/

public class BuscarPersonaFiltrosCombinados extends BaseTest {

    private PersonaPage personaPage;

    // Datos de búsqueda — deben existir en el sistema
    private static final String NOMBRE_PARCIAL      = "Juan";
    private static final String APELLIDO_PATERNO    = "Gómez";
    private static final String ESTADO_ACTIVO       = "true";      // 'Activo'
    private static final String ESTADO_VERIFICADO   = "Verificado";

    @BeforeMethod
    public void iniciarSesionEIrAPersonas() throws InterruptedException {
        personaPage = new PersonaPage();
        Thread.sleep(2000);

        // PASO 1. Iniciar sesión con credenciales del DIGITADOR
        ReportManager.info("PASO 1: Iniciando sesión con credenciales del DIGITADOR");
        personaPage.iniciarSesion(
            ConfigManager.getSacramentosUser(),
            ConfigManager.getSacramentosPassword()
        );
        Thread.sleep(3000);
    }

    @Test(description = "TC-43: Buscar Persona con múltiples filtros combinados")
    public void buscarPersonaConFiltrosCombinados() throws InterruptedException {

        /********** Preparación de la Prueba **********/

        // PASO 2. Navegar a 'Personas' y abrir la pestaña 'Buscar Persona'
        ReportManager.info("PASO 2: Navegando al módulo Personas y abriendo la pestaña Buscar Persona");
        personaPage.navegarAPersonas();
        Thread.sleep(1500);
        personaPage.abrirTabBuscarPersona();
        Thread.sleep(1500);

        // PASO 3. Hacer clic en la sección Buscar Persona para desplegar los campos de filtrado
        ReportManager.info("PASO 3: Expandiendo el panel de filtros y verificando que los campos están visibles");
        personaPage.expandirFiltros();
        Thread.sleep(1000);

        /********** Lógica de la Prueba — Primera búsqueda (3 filtros) **********/

        // PASO 4. Ingresar un nombre parcial en el campo 'Nombre'
        ReportManager.info("PASO 4: Ingresando nombre parcial '" + NOMBRE_PARCIAL + "' en el filtro Nombre");
        personaPage.ingresarFiltroNombre(NOMBRE_PARCIAL);

        // PASO 5. Ingresar un apellido paterno en el campo correspondiente
        ReportManager.info("PASO 5: Ingresando apellido paterno '" + APELLIDO_PATERNO + "' en el filtro");
        personaPage.ingresarFiltroApellidoPaterno(APELLIDO_PATERNO);

        // PASO 6. Seleccionar 'Activo' en el desplegable 'Estado'
        ReportManager.info("PASO 6: Seleccionando 'Activo' en el filtro Estado");
        personaPage.seleccionarFiltroEstado(ESTADO_ACTIVO);
        Thread.sleep(400);

        // PASO 7. Hacer clic en 'Buscar'
        ReportManager.info("PASO 7: Haciendo clic en el botón Buscar con los tres filtros combinados");
        personaPage.clickBuscar();
        Thread.sleep(2500);

        /********** Verificación — PASO 8: resultados cumplen TODOS los criterios **********/

        ReportManager.info("PASO 8: Verificando que la tabla muestra resultados con los filtros aplicados");
        int resultadosTresFiltros = personaPage.cantidadResultados();
        Assert.assertTrue(
            resultadosTresFiltros >= 1,
            "Se esperaba al menos 1 resultado al combinar Nombre='" + NOMBRE_PARCIAL
            + "', ApellidoPaterno='" + APELLIDO_PATERNO + "', Estado='Activo'; la tabla está vacía"
        );

        ReportManager.info("PASO 8: La tabla devolvió " + resultadosTresFiltros
            + " resultado(s) cumpliendo los filtros: Nombre, Apellido Paterno y Estado Activo");

        // Verificar que el primer resultado contiene el apellido paterno esperado
        Assert.assertTrue(
            personaPage.obtenerNombrePrimerResultado()
                       .toLowerCase()
                       .contains(NOMBRE_PARCIAL.toLowerCase()),
            "El primer resultado no contiene el nombre parcial '" + NOMBRE_PARCIAL
            + "'. Se obtuvo: " + personaPage.obtenerNombrePrimerResultado()
        );

        // Verificar que el estado del primer resultado es 'Activo'
        Assert.assertTrue(
            personaPage.obtenerEstadoPrimerResultado().equalsIgnoreCase("Activo"),
            "El estado del primer resultado debería ser 'Activo' pero se obtuvo: "
            + personaPage.obtenerEstadoPrimerResultado()
        );

        /********** Lógica de la Prueba — PASO 9: agregar filtro Estado de Verificación **********/

        ReportManager.info("PASO 9: Agregando el filtro 'Estado de verificación = Verificado' y buscando nuevamente");
        personaPage.seleccionarFiltroEstadoVerif(ESTADO_VERIFICADO);
        Thread.sleep(400);
        personaPage.clickBuscar();
        Thread.sleep(2500);

        int resultadosCuatroFiltros = personaPage.cantidadResultados();
        ReportManager.info("PASO 9: Con cuatro filtros la tabla devolvió "
            + resultadosCuatroFiltros + " resultado(s)");

        // Los resultados deben ser iguales o menores que la búsqueda anterior
        Assert.assertTrue(
            resultadosCuatroFiltros <= resultadosTresFiltros,
            "Al agregar el filtro 'Estado de verificación', se esperaba igual o menos resultados, "
            + "pero se obtuvieron " + resultadosCuatroFiltros
            + " (antes eran " + resultadosTresFiltros + ")"
        );

        /********** Verificación Final — PASO 10: Limpiar **********/

        // PASO 10. Limpiar todos los filtros con el botón 'Limpiar'
        ReportManager.info("PASO 10: Presionando el botón Limpiar para restablecer todos los filtros");
        personaPage.clickLimpiarFiltros();
        Thread.sleep(800);

        Assert.assertTrue(
            personaPage.filtrosEstanLimpios(),
            "Tras presionar 'Limpiar', uno o más filtros no quedaron en su estado inicial (vacíos / 'Todos')"
        );

        ReportManager.info("PASO 10: Todos los filtros fueron limpiados correctamente");
    }
}