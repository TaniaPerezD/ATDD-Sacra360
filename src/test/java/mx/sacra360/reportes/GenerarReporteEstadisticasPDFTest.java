package mx.sacra360.reportes;

import mx.sacra360.base.BaseTest;
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
import java.util.List;


/****************************************
 * Historia de Usuario:
 * Como administrador del sistema quiero generar y descargar un reporte PDF 
 * de las estadísticas filtrando por un rango específico para analizar los datos.
 *
 * Prueba de Aceptación / Caso de Prueba S360-90:
 * Generar y descargar reporte PDF de estadísticas
 *
 * PASO 1. Iniciar sesión con credenciales válidas
 * PASO 2. Navegar al Dashboard en el menú lateral
 * PASO 3. Ingresar el valor inicial de filtrado (1)
 * PASO 4. Ingresar el valor final de filtrado (1000)
 * PASO 5. Hacer scroll hasta la sección de vista previa
 * PASO 6. Hacer clic en el botón de generar vista previa
 * PASO 7. Verificar que la vista previa se muestre en pantalla
 * PASO 8. Hacer clic en el botón para generar el reporte PDF
 * PASO 9. Hacer clic en el botón para descargar el PDF generado
 * PASO 10. Verificar que se muestre la etiqueta final de confirmación/estado
 *
 * Resultado Esperado:
 * El sistema permite generar la vista previa, descargar el PDF y muestra 
 * la etiqueta de validación correctamente en la pantalla.
 ****************************************/

// Comando para ejecutar esta prueba específica desde la terminal:
// mvn clean compile test -Dtest=GenerarReporteEstadisticasPDFTest

// ===================================================================

public class GenerarReporteEstadisticasPDFTest extends BaseTest {

    @Test(description = "S360-90: Generar y descargar reporte PDF de estadísticas")
    public void generarReporteEstadisticasPDF() throws InterruptedException {

        /********** 1. PREPARACIÓN DE LA PRUEBA (ARRANGE) **********/
        // Todo lo necesario para dejar el sistema listo antes de interactuar
        
        // PASO 1. Iniciar sesión con credenciales válidas
        ReportManager.info("PASO 1: Iniciando sesión en el sistema");
        UsuarioPage usuarioPage = new UsuarioPage();
        Thread.sleep(2000);
        usuarioPage.iniciarSesion("ivonne.colque@ucb.edu.bo", "Wybma20HoG23!");
        Thread.sleep(5000);

        /// PASO 2. Navegar al Dashboard
        ReportManager.info("PASO 2: Navegando al Dashboard");

        // Esperar a que el login complete y el nav esté disponible
        WebDriverWait localWait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement botonDashboard = localWait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"root\"]/div/div/aside/nav/a[3]")
            )
        );
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", botonDashboard);
        Thread.sleep(3000);


        /********** 2. EJECUCIÓN Y LÓGICA DE LA PRUEBA (ACT) **********/
        // Los pasos y clics reales que ejecutan la funcionalidad a probar

        // PASO 3. Ingresar valor inicial
        ReportManager.info("PASO 3: Ingresando valor inicial (1) en el filtro");
        List<WebElement> inputs = localWait.until(
            ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.xpath("//input[@type='number']")
            )
        );
        WebElement inputInicio = inputs.get(0);
        inputInicio.clear();
        inputInicio.sendKeys("1");

        // PASO 4. Ingresar valor final
        ReportManager.info("PASO 4: Ingresando valor final (1000) en el filtro");
        WebElement inputFin = inputs.get(1);
        inputFin.clear();
        inputFin.sendKeys("1000");

        // PASO 5. Scroll hasta vista previa
        ReportManager.info("PASO 5: Haciendo scroll hasta la sección de vista previa");
        WebElement botonVistaPrevia = localWait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'Vista previa') or contains(text(),'vista previa') or contains(text(),'Previsualizar') or contains(text(),'Generar vista')]")
            )
        );
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", botonVistaPrevia);
        Thread.sleep(1500);

        // PASO 6. Generar vista previa
        ReportManager.info("PASO 6: Haciendo clic para generar vista previa");
        botonVistaPrevia.click();
        Thread.sleep(5000);

        // PASO 7. Verificar que la vista previa exista (Verificación Intermedia)
        ReportManager.info("PASO 7: Verificando que la vista previa exista en pantalla");
        WebElement vistaPrevia = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/main/div/div[3]/div/div[3]"));
        Assert.assertTrue(vistaPrevia.isDisplayed(), "La vista previa de estadísticas no fue generada o no es visible");

        // PASO 8. Generar PDF
        ReportManager.info("PASO 8: Haciendo clic en generar reporte PDF");
        WebElement botonPDF = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/main/div/div[3]/div/div[4]/button"));
        botonPDF.click();
        Thread.sleep(10000); // Tiempo extendido para renderización

        // PASO 9. Descargar PDF
        ReportManager.info("PASO 9: Haciendo clic en descargar reporte PDF");
        WebElement botonDescargar = driver.findElement(By.xpath("/html/body/div[2]/div/div[6]/button[1]"));
        botonDescargar.click();
        Thread.sleep(2000);


        /********** VERIFICACIÓN DEL RESULTADO (ASSERT)   **********/
        // Validamos que el resultado final sea exactamente el esperado

        // PASO 10. Verificar la etiqueta final
        ReportManager.info("PASO 10 - VERIFICACIÓN: Confirmando que la etiqueta final aparece correctamente");
        WebElement etiquetaFinal = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/main/div/div[3]/div/div[5]/div/div/div/p[1]"));
        
        Assert.assertTrue(
            etiquetaFinal.isDisplayed(), 
            "La etiqueta de confirmación final no se encuentra visible en la pantalla"
        );

        ReportManager.info("PRUEBA FINALIZADA CORRECTAMENTE");
    }
}