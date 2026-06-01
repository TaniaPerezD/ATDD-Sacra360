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
 * Como Digitador quiero que el sistema valide correctamente los campos
 * requeridos en el formulario de Agregar Persona, impidiendo el registro
 * si los campos obligatorios no están llenos.
 *
 * Prueba de Aceptación / Caso de Prueba TC-40:
 * Validación de campos requeridos en Agregar Persona
 *
 * Precondiciones:
 *   - Usuario autenticado con rol DIGITADOR.
 *   - Se cuenta con los datos de una persona a registrar con un CI no registrado.
 *
 * PASO 1. Iniciar sesión con credenciales del DIGITADOR
 * PASO 2. Hacer clic en 'Personas' en el menú lateral
 * PASO 3. Ingresar un CI válido y dejar todos los demás campos vacíos
 * PASO 4. Hacer clic en 'Agregar Persona' → botón debe permanecer bloqueado
 * PASO 5. (omitido en TestLink, sin número 5 explícito)
 * PASO 6. Ingresar apellido paterno, apellido materno, nombre, lugar de nacimiento,
 *         nombre del padre, nombre de la madre, CI y cambiar estado a Activo
 * PASO 7. Hacer clic en 'Agregar Persona' → botón debe permanecer bloqueado
 *         (falta fecha de nacimiento y estado de verificación)
 * PASO 8. Presionar 'Limpiar' y verificar que todos los campos se vacían
 *
 * Resultado Esperado:
 *   El botón 'Agregar Persona' permanece deshabilitado cuando faltan campos
 *   obligatorios. Todos los campos se vacían al presionar 'Limpiar'.
 *
 * Para ejecutar solo este test:
 *   mvn clean test -Dtest=ValidarCamposRequeridosAgregarPersonaTest
 ****************************************/

public class ValidarCamposRequeridosAgregarPersonaTest extends BaseTest {


    /********** Preparación de la Prueba **********/

    private PersonaPage personaPage;

    // CI único para esta prueba (no debe estar registrado)
    private static final String CI_VALIDO = "272886732";

    @BeforeMethod
    public void iniciarSesionEIrAPersonas() throws InterruptedException {
        personaPage = new PersonaPage();
        Thread.sleep(2000);

        // PASO 1. Iniciar sesión con credenciales del Digitador
        ReportManager.info("PASO 1: Iniciando sesión con credenciales del Digitador");
        personaPage.iniciarSesion(
            ConfigManager.getSacramentosUser(),
            ConfigManager.getSacramentosPassword()
        );
        Thread.sleep(3000);
    }

    @Test(description = "TC-40: Validación de campos requeridos en Agregar Persona")
    public void validarCamposRequeridosTest() throws InterruptedException {
      

        // PASO 2. Hacer clic en 'Personas' en el menú lateral
        ReportManager.info("PASO 2: Navegando al módulo Personas, pestaña Agregar Persona activa");
        personaPage.navegarAPersonas();
        Thread.sleep(1500);
        // La pestaña 'Agregar Persona' está activa por defecto al cargar /personas
        // Verificamos que el formulario cargó
        personaPage.abrirTabAgregarPersona();
        Thread.sleep(1000);

        /********** Lógica de la Prueba — Parte 1 **********/

        // PASO 3. Ingresar solo el CI y dejar todos los demás campos vacíos
        ReportManager.info("PASO 3: Ingresando solo el CI (" + CI_VALIDO + "), resto de campos vacíos");
        personaPage.ingresarCiForm(CI_VALIDO);
        Thread.sleep(600);

        // PASO 4. Hacer clic en 'Agregar Persona' — el botón debe estar bloqueado
        ReportManager.info("PASO 4: Verificando que el botón 'Agregar Persona' está deshabilitado con campos incompletos");
        
        /********** Verificación - Parte 1 — Boton Inactivo **********/

        Assert.assertTrue(
            personaPage.botonAgregarEstaDeshabilitado(),
            "El botón 'Agregar Persona' debería estar deshabilitado cuando faltan campos obligatorios, " +
            "pero aparece habilitado"
        );

        /********** Lógica de la Prueba — Parte 2 **********/

        // PASO 6. Ingresar apellido paterno, apellido materno, nombre, lugar de nacimiento,
        //         nombre del padre, nombre de la madre, CI y cambiar estado a Activo.
        //         Los campos vacíos que quedan son: fecha de nacimiento y estado de verificación.
        ReportManager.info("PASO 6: Ingresando todos los campos excepto fecha de nacimiento y estado de verificación");

        personaPage.ingresarApellidoPaternoForm("Mamani");
        personaPage.ingresarApellidoMaternoForm("Condori");
        personaPage.ingresarNombreForm("Juan");
        personaPage.ingresarLugarNacForm("La Paz");
        personaPage.ingresarNombrePadreForm("Pedro Mamani");
        personaPage.ingresarNombreMadreForm("Rosa Condori");
        personaPage.ingresarCiForm(CI_VALIDO);

        // Cambiar estado a 'Activo' (value="true")
        personaPage.seleccionarEstadoForm("true");
        Thread.sleep(600);

        // PASO 7. Hacer clic en 'Agregar Persona' — aún debe estar bloqueado
        //         porque faltan fecha_nacimiento y estado (verificación)
        ReportManager.info("PASO 7: Verificando que el botón permanece deshabilitado " +
                           "cuando faltan fecha de nacimiento y estado de verificación");

        /********** Verificación - Parte 2 — Boton Inactivo **********/

        Assert.assertTrue(
            personaPage.botonAgregarEstaDeshabilitado(),
            "El botón 'Agregar Persona' debería seguir deshabilitado cuando faltan " +
            "'fecha de nacimiento' y 'estado de verificación', pero aparece habilitado"
        );


        /********** Lógica de la Prueba — Parte Final **********/

        // PASO 8. Presionar 'Limpiar' y verificar que todos los campos se vacían
        ReportManager.info("PASO 8: Presionando el botón Limpiar y verificando que todos los campos se vacían");
        personaPage.clickLimpiarForm();
        Thread.sleep(800);

        /********** Verificación Final — Limpiar **********/

        Assert.assertTrue(
            personaPage.formularioAgregarEstaLimpio(),
            "Tras presionar 'Limpiar', uno o más campos del formulario no quedaron vacíos"
        );
    }
}
