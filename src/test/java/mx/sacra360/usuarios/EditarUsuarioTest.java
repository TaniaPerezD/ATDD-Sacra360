package mx.sacra360.usuarios;

import mx.sacra360.base.BaseTest;
import mx.sacra360.pages.usuarios.UsuarioPage;
import mx.sacra360.utils.ReportManager;
import org.testng.Assert;
import org.testng.annotations.Test;

/****************************************
 * Historia de Usuario:
 * Como administrador del sistema quiero editar los datos de un usuario
 * para mantener la información actualizada en el sistema.
 *
 * Prueba de Aceptación / Caso de Prueba S360-53:
 * Editar Usuario con datos válidos
 *
 * PASO 1. Iniciar sesión con credenciales de OSI
 * PASO 2. Acceder al módulo "Usuarios"
 * PASO 3. Seleccionar la pestaña "Buscar / Editar"
 * PASO 4. Seleccionar un usuario registrado
 * PASO 5. Verificar que los campos están bloqueados (solo lectura)
 * PASO 6. Modificar la fecha de nacimiento por una fecha válida
 * PASO 7. Seleccionar un rol diferente al actual
 * PASO 8. Verificar que los cambios son correctos
 * PASO 9. Hacer clic en el botón "Guardar Cambios"
 * PASO 10. Buscar nuevamente al usuario modificado
 * PASO 11. Verificar que los cambios fueron almacenados
 *
 * Resultado Esperado:
 * El sistema actualiza la información y muestra mensaje
 * "Usuario actualizado correctamente."
 ****************************************/
public class EditarUsuarioTest extends BaseTest {

    @Test(description = "S360-53: Editar Usuario con datos válidos")
    public void editarUsuarioConDatosValidos() throws InterruptedException {

        /********** Preparación de la Prueba **********/

        // PASO 1. Iniciar sesión con credenciales de OSI
        ReportManager.info("PASO 1: Iniciando sesión con credenciales de OSI");
        UsuarioPage usuarioPage = new UsuarioPage();
        Thread.sleep(2000);
        usuarioPage.iniciarSesion("tania.perez.d@ucb.edu.bo", "M4rshallLee#");
        Thread.sleep(3000);

        // PASO 2. Acceder al módulo Usuarios
        ReportManager.info("PASO 2: Accediendo al módulo Usuarios");
        usuarioPage.navegarAUsuarios();
        Thread.sleep(2000);

        // PASO 3. Seleccionar la pestaña "Buscar / Editar"
        ReportManager.info("PASO 3: Seleccionando la pestaña Buscar / Editar");
        usuarioPage.abrirTabBuscarEditar();
        Thread.sleep(1500);

        // PASO 4. Seleccionar un usuario registrado de la lista
        ReportManager.info("PASO 4: Seleccionando un usuario registrado de la lista");
        usuarioPage.seleccionarUsuarioDeLista();
        Thread.sleep(2000);

        // PASO 5. Verificar que los campos están bloqueados (solo lectura)
        ReportManager.info("PASO 5: Verificando que los campos están en modo solo lectura");
        Assert.assertTrue(
            usuarioPage.camposEstanBloqueados(),
            "Los campos no están en modo solo lectura como se esperaba"
        );

        /********** Lógica de la Prueba **********/

        // PASO 6. Modificar la fecha de nacimiento
        ReportManager.info("PASO 6: Modificando la fecha de nacimiento por una fecha válida");
        usuarioPage.editarFechaNacimiento("20-06-2006");
        Thread.sleep(800);

        // PASO 7. Seleccionar un rol diferente al actual
        ReportManager.info("PASO 7: Seleccionando un rol diferente al actual");
        usuarioPage.seleccionarRolEdicion();
        Thread.sleep(800);

        // PASO 8. Verificar que los cambios son correctos
        ReportManager.info("PASO 8: Verificando que los cambios se visualizan correctamente");
        Assert.assertFalse(
            usuarioPage.editarFechaNacimiento("20-06-2006").isEmpty(),
            "La fecha de nacimiento no se actualizó correctamente"
        );
        Thread.sleep(800);

        // PASO 9. Hacer clic en el botón "Guardar Cambios"
        ReportManager.info("PASO 9: Haciendo clic en el botón Guardar Cambios");
        usuarioPage.clickGuardarCambios();
        Thread.sleep(3000);

        /********** Verificación del Resultado Esperado - Assert **********/

        // PASO 10 y 11 - Resultado Esperado: mensaje de actualización exitosa
        ReportManager.info("VERIFICACIÓN: Confirmando mensaje de usuario actualizado correctamente");
        Assert.assertTrue(
            usuarioPage.mensajeExitoEsVisible(),
            "No se mostró el mensaje de actualización exitosa del usuario"
        );
    }
}