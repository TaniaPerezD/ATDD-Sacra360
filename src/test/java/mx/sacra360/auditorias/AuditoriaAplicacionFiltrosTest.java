package mx.sacra360.auditorias;

import mx.sacra360.base.BaseTest;
import mx.sacra360.config.ConfigManager;
import mx.sacra360.pages.usuarios.UsuarioPage;
import mx.sacra360.utils.ReportManager;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

/****************************************
 * Historia de Usuario:
 * Como administrador de la infraestructura tecnológica quiero filtrar los registros de
 * auditoría de aplicación por código de respuesta para evaluar peticiones correctas (200 OK).
 *
 * Prueba de Aceptación / Caso de Prueba S360-89:
 * Filtrar registros de auditoría de aplicación por código de respuesta 200
 *
 * PASO 1. Iniciar sesión con credenciales válidas
 * PASO 2. Navegar directamente a la sección de Auditoría de Aplicación
 * PASO 3. Desplegar el menú superior (si no se encuentra expandido por defecto)
 * PASO 4. Hacer clic en la opción "Aplicación" en el menú lateral (vínculo a[4])
 * PASO 5. Hacer clic en el botón para desplegar el panel de filtros
 * PASO 6. Seleccionar la opción "200" dentro del componente select de respuestas
 * PASO 7. Hacer clic en el botón para aplicar el filtro seleccionado
 * PASO 8. Verificar el código de respuesta del primer registro de la tabla resultante
 *
 * Resultado Esperado:
 * La tabla se actualiza correctamente mostrando la etiqueta con el código de respuesta HTTP "200"
 ****************************************/

// Comando para ejecutar esta prueba específica desde la terminal:
// mvn clean compile test -Dtest=AuditoriaAplicacionFiltrosTest

public class AuditoriaAplicacionFiltrosTest extends BaseTest {

    @Test(description = "S360-89: Filtrar registros de auditoría de aplicación por código de respuesta 200")
    public void pruebaAuditoriaAplicacion() throws InterruptedException {

        /********** Preparación de la Prueba **********/

        // PASO 1. Iniciar sesión con credenciales válidas
        ReportManager.info("PASO 1: Iniciando sesión con credenciales válidas");
        UsuarioPage usuarioPage = new UsuarioPage();
        Thread.sleep(2000);
        usuarioPage.iniciarSesion(ConfigManager.getTestUser(), ConfigManager.getTestPassword());
        Thread.sleep(3000);

        // PASO 2. Navegar directamente a la sección de Auditoría de Aplicación
        ReportManager.info("PASO 2: Navegando a la sección de auditoría de aplicación");
        driver.get(ConfigManager.getBaseUrl() + "/auditoria-aplicacion");
        Thread.sleep(4000);

        // PASO 3. Desplegar el menú superior (si no se encuentra expandido por defecto)
        ReportManager.info("PASO 3: Gestionando visibilidad del menú superior");
        try {
            driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/main/header/div[1]/button")).click();
            Thread.sleep(1000);
        } catch (Exception e) {
            ReportManager.info("Menú superior ya se encontraba desplegado de forma nativa.");
        }

        // PASO 4. Hacer clic en la opción "Aplicación" en el menú lateral (vínculo a[4])
        ReportManager.info("PASO 4: Seleccionando 'Aplicación' en el menú lateral (a[4])");
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/aside/nav/a[4]")).click();
        Thread.sleep(3000);

        /********** Lógica de la Prueba **********/

        // PASO 5. Hacer clic en el botón para desplegar el panel de filtros
        ReportManager.info("PASO 5: Desplegando el panel de componentes para filtros");
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/main/div/div/div[1]/button")).click();
        Thread.sleep(2000);

        // PASO 6. Seleccionar la opción "200" dentro del componente select de respuestas
        ReportManager.info("PASO 6: Seleccionando el valor de opción '200' (div[8])");
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/main/div/div/div[1]/div/div[1]/div[8]/select/option[2]")).click();
        Thread.sleep(1000);

        // PASO 7. Hacer clic en el botón para aplicar el filtro seleccionado
        ReportManager.info("PASO 7: Presionando el botón para aplicar la consulta");
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/main/div/div/div[1]/div/div[2]/button[1]")).click();
        Thread.sleep(5000);

        /********** Verificación del Resultado Esperado - Assert **********/

        // PASO 8. Verificar el código de respuesta del primer registro de la tabla resultante
        ReportManager.info("PASO 8 - VERIFICACIÓN: Validando código del primer elemento filtrado");
        WebElement celdaCodigo = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/main/div/div/div[2]/div/table/tbody/tr[1]/td[1]/div/div/span[1]"));
        String txtCodigoObtenido = celdaCodigo.getText().trim();

        Assert.assertEquals(txtCodigoObtenido, "200", "El código del registro recuperado no corresponde a '200'");
    }
}