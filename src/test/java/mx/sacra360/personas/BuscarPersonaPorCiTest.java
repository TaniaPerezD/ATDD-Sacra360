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
 * Como Digitador quiero buscar una persona registrada filtrando
 * por número de Carnet de Identidad para consultar sus datos
 * y verificar las columnas que muestra la tabla de resultados.
 *
 * Prueba de Aceptación / Caso de Prueba TC-42:
 * Buscar Persona por Carnet de Identidad
 *
 * Precondiciones:
 *   - Usuario autenticado con rol DIGITADOR.
 *   - Existe al menos una persona registrada con CI conocido.
 *
 * PASO 1. Iniciar sesión con credenciales del DIGITADOR
 * PASO 2. Navegar a 'Personas' y hacer clic en 'Buscar Persona'
 * PASO 3. Expandir el panel de filtros y verificar los campos visibles
 * PASO 4. Ingresar el CI conocido de una persona registrada
 * PASO 5. Hacer clic en el botón 'Buscar'
 * PASO 6. Verificar que la tabla muestra la persona con ese CI
 * PASO 7. Verificar las columnas de la tabla
 * PASO 8. Presionar 'Limpiar' y verificar que el campo CI se vacía
 *
 * Resultado Esperado:
 *   La tabla muestra la persona cuyo CI coincide con el buscado,
 *   con las columnas: Nombre, Apellido Paterno, Apellido Materno,
 *   CI, Fecha Nac., Lugar Nac., Estado y Estado de Verificación.
 *   Al limpiar, el campo CI y todos los filtros quedan vacíos.
 *
 * Para ejecutar solo este test:
 *   mvn clean test -Dtest=BuscarPersonaPorCiTest
 ****************************************/

public class BuscarPersonaPorCiTest extends BaseTest {

    /********** Preparación de la Prueba **********/

    private PersonaPage personaPage;

    // CI de una persona que existe en el sistema de prueba
    private static final String CI_CONOCIDO = "2728867";

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

    @Test(description = "TC-42: Buscar Persona por Carnet de Identidad")
    public void buscarPersonaPorCiTest() throws InterruptedException {

        

        // PASO 2. Navegar a 'Personas' y hacer clic en la pestaña 'Buscar Persona'
        ReportManager.info("PASO 2: Navegando al módulo Personas y abriendo la pestaña Buscar Persona");
        personaPage.navegarAPersonas();
        Thread.sleep(1500);
        personaPage.abrirTabBuscarPersona();
        Thread.sleep(1500);

        // PASO 3. Hacer clic en la sección Buscar Persona para desplegar los campos
        ReportManager.info("PASO 3: Expandiendo el panel de filtros");
        personaPage.expandirFiltros();
        Thread.sleep(1000);

        /********** Lógica de la Prueba **********/

        // PASO 4. Ingresar el CI conocido de una persona registrada
        ReportManager.info("PASO 4: Ingresando el CI conocido en el campo de filtro: " + CI_CONOCIDO);
        personaPage.ingresarFiltroCi(CI_CONOCIDO);

        // PASO 5. Hacer clic en el botón 'Buscar'
        ReportManager.info("PASO 5: Haciendo clic en el botón Buscar");
        personaPage.clickBuscar();
        Thread.sleep(2500);

        /********** Verificación del Resultado Esperado — Assert **********/

        // PASO 6. Verificar que la tabla muestra la persona con ese CI
        ReportManager.info("PASO 6: Verificando que la tabla muestra al menos un resultado");
        Assert.assertTrue(
            personaPage.cantidadResultados() >= 1,
            "Se esperaba al menos 1 resultado pero la tabla está vacía"
        );

        ReportManager.info("PASO 6: Verificando que el CI de la primera fila coincide con el buscado");
        Assert.assertEquals(
            personaPage.obtenerCiPrimerResultado(),
            CI_CONOCIDO,
            "El CI del primer resultado no coincide con el CI buscado"
        );

        // PASO 7. Verificar las columnas mostradas en la tabla
        ReportManager.info("PASO 7: Verificando que las columnas de la tabla son correctas");
        Assert.assertTrue(
            personaPage.columnasCorrectas(),
            "Las columnas de la tabla no coinciden con las esperadas: " +
            "Nombre, Apellido paterno, Apellido materno, CI, Fecha nac., " +
            "Lugar nac., Estado, Estado de verificación"
        );

        // PASO 8. Presionar 'Limpiar' y verificar que el campo CI se vacía
        ReportManager.info("PASO 8: Presionando el botón Limpiar y verificando que los filtros se vacían");
        personaPage.clickLimpiarFiltros();
        Thread.sleep(800);

        Assert.assertTrue(
            personaPage.filtrosEstanLimpios(),
            "Los filtros no se limpiaron correctamente tras presionar el botón Limpiar"
        );
    }
}