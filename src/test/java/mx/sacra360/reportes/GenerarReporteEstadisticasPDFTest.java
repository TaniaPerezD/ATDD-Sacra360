package mx.sacra360.reportes;

import mx.sacra360.base.BaseTest;
import mx.sacra360.config.ConfigManager;
import mx.sacra360.pages.usuarios.UsuarioPage;
import mx.sacra360.utils.ReportManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

/****************************************
 * Historia de Usuario:
 * Como administrador del sistema quiero generar y descargar un reporte PDF
 * de las estadísticas de sacramentos para analizar los datos registrados.
 *
 * Prueba de Aceptación / Caso de Prueba S360-90:
 * Generar y descargar reporte PDF de estadísticas
 *
 * PASO 1. Iniciar sesión con credenciales válidas
 * PASO 2. Navegar al módulo de Reportes y confirmar que la página cargó
 * PASO 3. Cargar Vista Previa de Estadísticas (si la opción está habilitada)
 * PASO 4. Verificar que la sección de estadísticas se muestra (verificación intermedia)
 * PASO 5. Hacer clic en el botón "Generar Reporte PDF"
 * PASO 6. Confirmar la descarga en el diálogo emergente (SweetAlert2)
 * PASO 7. Verificar que aparece la sección de descarga disponible
 *
 * Resultado Esperado:
 * El sistema genera el reporte PDF, muestra el diálogo de confirmación y
 * despliega la sección verde con el enlace de descarga disponible.
 ****************************************/

// Comando para ejecutar esta prueba específica desde la terminal:
// mvn clean compile test -Dtest=GenerarReporteEstadisticasPDFTest

// ===================================================================

public class GenerarReporteEstadisticasPDFTest extends BaseTest {

    @Test(description = "S360-90: Generar y descargar reporte PDF de estadísticas")
    public void generarReporteEstadisticasPDF() throws InterruptedException {

        /********** 1. PREPARACIÓN DE LA PRUEBA (ARRANGE) **********/

        // PASO 1. Iniciar sesión con credenciales válidas
        ReportManager.info("PASO 1: Iniciando sesión en el sistema");
        UsuarioPage usuarioPage = new UsuarioPage();
        Thread.sleep(2000);
        usuarioPage.iniciarSesion(ConfigManager.getSacramentosUser(), ConfigManager.getSacramentosPassword());
        Thread.sleep(4000);

        // PASO 2. Navegar al módulo de Reportes y esperar que cargue
        ReportManager.info("PASO 2: Navegando al módulo de Reportes");
        driver.get(ConfigManager.getBaseUrl() + "/reportes");
        // Usamos WebDriverWait para confirmar que la página cargó antes de continuar
        WebDriverWait pageWait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement botonGenerarPDF = pageWait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(., 'Generar Reporte PDF')]")
            )
        );
        Thread.sleep(1000);


        /********** 2. EJECUCIÓN Y LÓGICA DE LA PRUEBA (ACT) **********/

        // PASO 3. Cargar Vista Previa de Estadísticas (si la opción está habilitada)
        ReportManager.info("PASO 3: Intentando cargar Vista Previa de Estadísticas");
        try {
            WebElement botonVistaPrevia = driver.findElement(
                By.xpath("//button[contains(., 'Vista Previa')]")
            );
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", botonVistaPrevia);
            botonVistaPrevia.click();
            Thread.sleep(3000);

            // PASO 4. Verificar que la sección de estadísticas apareció (verificación intermedia)
            WebElement seccionEstadisticas = driver.findElement(
                By.xpath("//div[contains(@class,'bg-blue-50')]")
            );
            Assert.assertTrue(
                seccionEstadisticas.isDisplayed(),
                "La sección de Vista Previa de Estadísticas no apareció en pantalla"
            );
            ReportManager.info("PASO 4: Vista previa de estadísticas cargada correctamente");
        } catch (Exception e) {
            ReportManager.info("PASO 3/4: Opción de vista previa no disponible, continuando con generación directa");
        }

        // PASO 5. Hacer clic en Generar Reporte PDF
        ReportManager.info("PASO 5: Haciendo clic en Generar Reporte PDF");
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", botonGenerarPDF);
        Thread.sleep(500);
        botonGenerarPDF.click();

        // PASO 6. Confirmar descarga en el diálogo SweetAlert2
        // El servidor puede tardar en generar el PDF — esperamos hasta 60 segundos
        ReportManager.info("PASO 6: Esperando el diálogo de confirmación de descarga");
        WebDriverWait swalWait = new WebDriverWait(driver, Duration.ofSeconds(80));
        WebElement botonConfirmar = swalWait.until(
            ExpectedConditions.elementToBeClickable(By.cssSelector(".swal2-confirm"))
        );
        botonConfirmar.click();
        Thread.sleep(3000);


        /********** VERIFICACIÓN DEL RESULTADO (ASSERT) **********/

        // PASO 7. Verificar que aparece la sección de descarga disponible
        ReportManager.info("PASO 7 - VERIFICACIÓN FINAL: Confirmando que la sección de descarga está disponible");
        WebElement seccionDescarga = driver.findElement(
            By.xpath("//div[contains(@class,'bg-green-50')]//p[contains(text(),'Reporte disponible')]")
        );
        Assert.assertTrue(
            seccionDescarga.isDisplayed(),
            "La sección de descarga no apareció tras confirmar en el diálogo"
        );

        ReportManager.info("PRUEBA FINALIZADA CORRECTAMENTE");
    }
}
