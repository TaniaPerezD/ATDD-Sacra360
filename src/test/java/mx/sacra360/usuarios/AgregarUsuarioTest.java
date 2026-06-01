package mx.sacra360.usuarios;

import mx.sacra360.base.BaseTest;
import mx.sacra360.pages.usuarios.UsuarioPage;
import mx.sacra360.utils.ReportManager;
import org.testng.Assert;
import org.testng.annotations.Test;

/****************************************
 * Historia de Usuario:
 * Como administrador del sistema quiero agregar un nuevo usuario
 * para que pueda acceder al sistema con sus credenciales.
 *
 * Prueba de Aceptación / Caso de Prueba S360-45:
 * Agregar Usuario con datos válidos
 *
 * PASO 1. Iniciar sesión con credenciales de OSI y llenar el captcha
 * PASO 2. Navegar al módulo Usuarios
 * PASO 3. Hacer clic en la pestaña "Agregar Usuario"
 * PASO 4. Verificar que todos los campos requeridos estén visibles
 * PASO 5. Ingresar un nombre válido
 * PASO 6. Ingresar un apellido paterno válido
 * PASO 7. Ingresar un apellido materno válido
 * PASO 8. Seleccionar una fecha de nacimiento válida
 * PASO 9. Ingresar un correo electrónico válido y no registrado previamente
 * PASO 10. Seleccionar un rol disponible de la lista
 * PASO 11. Seleccionar un estado disponible (Activo)
 * PASO 12. Hacer clic en el botón "Crear Usuario"
 *
 * Resultado Esperado:
 * Se muestra un mensaje indicando que el usuario fue registrado exitosamente
 ****************************************/
public class AgregarUsuarioTest extends BaseTest {

    @Test(description = "S360-45: Agregar Usuario con datos válidos")
    public void agregarUsuarioConDatosValidos() throws Exception {

        /********** Preparación de la Prueba **********/

        // PASO 1. Iniciar sesión con credenciales de OSI
        ReportManager.info("PASO 1: Iniciando sesión con credenciales de OSI");
        UsuarioPage usuarioPage = new UsuarioPage();
        Thread.sleep(2000);
        usuarioPage.iniciarSesion("tania.perez.d@ucb.edu.bo", "M4rshallLee#");
        Thread.sleep(3000);

        // PASO 2. Navegar al módulo Usuarios
        ReportManager.info("PASO 2: Navegando al módulo Usuarios");
        usuarioPage.navegarAUsuarios();
        Thread.sleep(2000);

        // PASO 3. Hacer clic en la pestaña "Agregar Usuario"
        ReportManager.info("PASO 3: Abriendo la pestaña Agregar Usuario");
        usuarioPage.abrirTabAgregarUsuario();
        Thread.sleep(1500);

        // PASO 4. Verificar que todos los campos requeridos estén visibles
        ReportManager.info("PASO 4: Verificando que todos los campos requeridos están visibles");
        Assert.assertTrue(
            usuarioPage.formularioEsVisible(),
            "No se muestran todos los campos requeridos"
        );

        /********** Lógica de la Prueba **********/

        // PASO 5. Ingresar un nombre válido
        ReportManager.info("PASO 5: Ingresando nombre válido");
        usuarioPage.ingresarNombre("Isabel");
        Thread.sleep(800);

        // PASO 6. Ingresar un apellido paterno válido
        ReportManager.info("PASO 6: Ingresando apellido paterno válido");
        usuarioPage.ingresarApellidoPaterno("Rocha");
        Thread.sleep(800);

        // PASO 7. Ingresar un apellido materno válido
        ReportManager.info("PASO 7: Ingresando apellido materno válido");
        usuarioPage.ingresarApellidoMaterno("Vedia");
        Thread.sleep(800);

        // PASO 8. Seleccionar una fecha de nacimiento válida
        ReportManager.info("PASO 8: Seleccionando fecha de nacimiento válida");
        usuarioPage.ingresarFechaNacimiento("07-04-2007");
        Thread.sleep(800);

        // PASO 9. Ingresar un correo electrónico válido y no registrado previamente
        ReportManager.info("PASO 9: Ingresando correo electrónico válido y no registrado");
        usuarioPage.ingresarEmail("isabel.rocha.v@ucb.edu.bo");
        Thread.sleep(800);

        // PASO 10. Seleccionar un rol disponible de la lista
        ReportManager.info("PASO 10: Seleccionando rol disponible de la lista");
        usuarioPage.seleccionarRol();
        Thread.sleep(800);

        // PASO 11. Seleccionar estado Activo
        ReportManager.info("PASO 11: Seleccionando estado Activo");
        usuarioPage.seleccionarEstado();
        Thread.sleep(800);

        // PASO 12. Hacer clic en el botón "Crear Usuario"
        ReportManager.info("PASO 12: Haciendo clic en el botón Crear Usuario");
        usuarioPage.clickCrearUsuario();
        Thread.sleep(2000);

        // PASO 13. Confirmar el registro en el modal
        ReportManager.info("PASO 13: Confirmando el registro en el modal de confirmación");
        usuarioPage.confirmarRegistro();
        Thread.sleep(3000); // esperar que aparezca el toast

        /********** Verificación del Resultado Esperado - Assert **********/
        ReportManager.info("VERIFICACIÓN: Confirmando que se muestra el mensaje de éxito");
        Assert.assertTrue(
            usuarioPage.mensajeExitoEsVisible(),
            "No se mostró el mensaje de confirmación de registro exitoso del usuario"
        );

        // LIMPIEZA: eliminar usuario creado para permitir re-ejecución
        ReportManager.info("LIMPIEZA: Eliminando usuario de prueba del sistema");
        String token = UsuarioPage.obtenerToken();
        int idUsuario = UsuarioPage.obtenerIdUsuarioPorEmail(token, "isabel.rocha.v@ucb.edu.bo");
        UsuarioPage.eliminarUsuarioFisico(token, idUsuario);
        ReportManager.info("LIMPIEZA: Usuario eliminado correctamente");
        
    }
}