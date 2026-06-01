package mx.sacra360.usuarios;

import mx.sacra360.base.BaseTest;
import mx.sacra360.pages.usuarios.UsuarioPage;
import mx.sacra360.utils.ReportManager;
import org.testng.Assert;
import org.testng.annotations.Test;

/****************************************
 * Historia de Usuario:
 * Como administrador del sistema quiero que el sistema me notifique
 * cuando intento registrar un usuario con un correo ya existente.
 *
 * Prueba de Aceptación / Caso de Prueba S360-46:
 * Agregar Usuario con correo electrónico ya registrado
 *
 * PASO 1. Iniciar sesión con credenciales de OSI
 * PASO 2. Hacer clic en "Usuarios" en el menú lateral
 * PASO 3. Hacer clic en la pestaña "Agregar Usuario"
 * PASO 4. Ingresar un nombre válido
 * PASO 5. Ingresar apellido paterno válido
 * PASO 6. Ingresar apellido materno válido
 * PASO 7. Seleccionar una fecha de nacimiento válida
 * PASO 8. Ingresar un correo electrónico que ya existe en el sistema
 * PASO 9. Seleccionar un rol válido
 * PASO 10. Seleccionar el estado "Activo"
 * PASO 11. Hacer clic en el botón "Crear Usuario"
 * PASO 12. Confirmar el registro
 * PASO 13. Verificar el mensaje mostrado
 *
 * Resultado Esperado:
 * Se muestra un mensaje indicando que el correo electrónico ya existe
 * y que el usuario no pudo ser registrado
 ****************************************/
public class AgregarUsuarioEmailDuplicadoTest extends BaseTest {

    @Test(description = "S360-46: Agregar Usuario con correo electrónico ya registrado")
    public void agregarUsuarioConCorreoYaRegistrado() throws InterruptedException {

        /********** Preparación de la Prueba **********/

        // PASO 1. Iniciar sesión con credenciales de OSI
        ReportManager.info("PASO 1: Iniciando sesión con credenciales de OSI");
        UsuarioPage usuarioPage = new UsuarioPage();
        Thread.sleep(2000);
        usuarioPage.iniciarSesion("tania.perez.d@ucb.edu.bo", "M4rshallLee#");
        Thread.sleep(3000);

        // PASO 2. Navegar al módulo Usuarios
        ReportManager.info("PASO 2: Navegando al módulo Usuarios en el menú lateral");
        usuarioPage.navegarAUsuarios();
        Thread.sleep(2000);

        // PASO 3. Hacer clic en la pestaña "Agregar Usuario"
        ReportManager.info("PASO 3: Abriendo la pestaña Agregar Usuario");
        usuarioPage.abrirTabAgregarUsuario();
        Thread.sleep(1500);

        /********** Lógica de la Prueba **********/

        // PASO 4. Ingresar un nombre válido
        ReportManager.info("PASO 4: Ingresando nombre válido");
        usuarioPage.ingresarNombre("Adriana");
        Thread.sleep(800);

        // PASO 5. Ingresar apellido paterno válido
        ReportManager.info("PASO 5: Ingresando apellido paterno válido");
        usuarioPage.ingresarApellidoPaterno("Rocha");
        Thread.sleep(800);

        // PASO 6. Ingresar apellido materno válido
        ReportManager.info("PASO 6: Ingresando apellido materno válido");
        usuarioPage.ingresarApellidoMaterno("Lopez");
        Thread.sleep(800);

        // PASO 7. Seleccionar una fecha de nacimiento válida
        ReportManager.info("PASO 7: Seleccionando fecha de nacimiento válida");
        usuarioPage.ingresarFechaNacimiento("18-06-2005");
        Thread.sleep(800);

        // PASO 8. Ingresar correo electrónico que ya existe en el sistema
        ReportManager.info("PASO 8: Ingresando correo electrónico ya registrado en el sistema");
        usuarioPage.ingresarEmail("adriana.rocha@ucb.edu.bo");
        Thread.sleep(800);

        // PASO 9. Seleccionar un rol válido
        ReportManager.info("PASO 9: Seleccionando rol válido de la lista");
        usuarioPage.seleccionarRol();
        Thread.sleep(800);

        // PASO 10. Seleccionar estado Activo
        ReportManager.info("PASO 10: Seleccionando estado Activo");
        usuarioPage.seleccionarEstado();
        Thread.sleep(800);

        // PASO 11. Hacer clic en el botón "Crear Usuario"
        ReportManager.info("PASO 11: Haciendo clic en el botón Crear Usuario");
        usuarioPage.clickCrearUsuario();
        Thread.sleep(2000);

        // PASO 12. Confirmar el registro en el modal
        ReportManager.info("PASO 12: Confirmando el registro en el modal");
        usuarioPage.confirmarRegistro();
        Thread.sleep(500);


        /********** Verificación del Resultado Esperado - Assert **********/

        // Resultado Esperado: Se muestra mensaje de error indicando que el correo ya existe
        ReportManager.info("PASO 13 - VERIFICACIÓN: Confirmando que se muestra mensaje de correo ya registrado");
        Assert.assertTrue(
            usuarioPage.mensajeErrorEsVisible(),
            "No se mostró el mensaje de error indicando que el correo ya está registrado"
        );
    }
}