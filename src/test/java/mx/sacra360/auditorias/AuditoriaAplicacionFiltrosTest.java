package mx.sacra360.auditorias;

import io.github.bonigarcia.wdm.WebDriverManager;
import mx.sacra360.utils.ReportManager;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;
import java.time.Duration;

/****************************************
 * Historia de Usuario:
 * Como administrador de la infraestructura tecnológica quiero filtrar los registros de 
 * auditoría de aplicación por código de respuesta para evaluar peticiones correctas (200 OK).
 *
 * Prueba de Aceptación / Caso de Prueba S360-89:
 * Filtrar registros de auditoría de aplicación por código de respuesta 200
 *
 * PASO 1. Ingresar a la página base y comprobar estado de sesión (cerrar previa si existe)
 * PASO 2. Introducir correo electrónico válido
 * PASO 3. Introducir contraseña válida
 * PASO 4. Hacer clic en el botón de iniciar sesión
 * PASO 5. Navegar directamente a la sección de Auditoría de Aplicación
 * PASO 6. Desplegar el menú superior (si no se encuentra expandido por defecto)
 * PASO 7. Hacer clic en la opción "Aplicación" en el menú lateral (vínculo a[4])
 * PASO 8. Hacer clic en el botón para desplegar el panel de filtros
 * PASO 9. Seleccionar la opción "200" dentro del componente select de respuestas
 * PASO 10. Hacer clic en el botón para aplicar el filtro seleccionado
 * PASO 11. Verificar el código de respuesta del primer registro de la tabla resultante
 *
 * Resultado Esperado:
 * La tabla se actualiza correctamente mostrando la etiqueta con el código de respuesta HTTP "200"
 ****************************************/

// Comando para ejecutar esta prueba específica desde la terminal:
// mvn clean compile test -Dtest=AuditoriaAplicacionFiltrosTest

public class AuditoriaAplicacionFiltrosTest {

    private WebDriver driver;
    private Properties prop;

    @BeforeTest
    public void setDriver() throws Exception {
        prop = new Properties();
        FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
        prop.load(fis);

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @AfterTest
    public void closeDriver() throws Exception {
        if (driver != null) {
            driver.quit();
        }
    }

   @Test(description = "S360-89: Filtrar registros de auditoría de aplicación por código de respuesta 200")
    public void pruebaAuditoriaAplicacion() throws InterruptedException {

        String baseUrl = prop.getProperty("base.url");
        String usuario = prop.getProperty("test.user");
        String contrasenia = prop.getProperty("test.password");

        // PASO 1. Navegar a la URL base
        System.out.println("PASO 1: Navegando a la URL base");
        driver.get(baseUrl);
        Thread.sleep(2000);

        // Cerrar sesión previa si existe
        List<WebElement> loginInput = driver.findElements(By.xpath("//*[@id=\"email\"]"));
        if (loginInput.isEmpty()) {
            System.out.println("Sesión previa detectada. Ejecutando logout...");
            driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/main/header/div[2]/div/button")).click();
            Thread.sleep(1000);
            driver.findElement(By.xpath("/html/body/div[2]/div/div[6]/button[3]")).click();
            Thread.sleep(3000);
            driver.get(baseUrl);
            Thread.sleep(2000);
        }

        // PASO 2. Introducir correo electrónico
        System.out.println("PASO 2: Introduciendo correo electrónico");
        driver.findElement(By.xpath("//*[@id=\"email\"]")).sendKeys(usuario);
        Thread.sleep(800);

        // PASO 3. Introducir contraseña
        System.out.println("PASO 3: Introduciendo contraseña");
        driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys(contrasenia);
        Thread.sleep(800);

        // PASO 4. Hacer clic en iniciar sesión
        System.out.println("PASO 4: Haciendo clic en iniciar sesión");
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[1]/div/div[2]/button")).click();
        Thread.sleep(5000); // esperar login + servidor Render

        // PASO 5. Navegar directamente a auditoría de aplicación
        System.out.println("PASO 5: Navegando a auditoría de aplicación");
        driver.get(baseUrl + "/auditoria-aplicacion");
        Thread.sleep(4000);

        // PASO 6. Desplegar menú superior si está colapsado
        System.out.println("PASO 6: Gestionando visibilidad del menú superior");
        try {
            driver.findElement(By.xpath(
                "//*[@id=\"root\"]/div/div/main/header/div[1]/button")).click();
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Menú ya estaba desplegado.");
        }

        // PASO 7. Hacer clic en "Aplicación" en el menú lateral
        System.out.println("PASO 7: Seleccionando Aplicación en el menú lateral");
        WebDriverWait localWait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement linkAplicacion = localWait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"root\"]/div/div/aside/nav/a[4]")
            )
        );
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", linkAplicacion);
        Thread.sleep(3000);

        // PASO 8. Desplegar panel de filtros
        System.out.println("PASO 8: Desplegando panel de filtros");
        driver.findElement(By.xpath(
            "//*[@id=\"root\"]/div/div/main/div/div/div[1]/button")).click();
        Thread.sleep(2000);

        // PASO 9. Seleccionar opción "200"
        System.out.println("PASO 9: Seleccionando código 200");
        driver.findElement(By.xpath(
            "//*[@id=\"root\"]/div/div/main/div/div/div[1]/div/div[1]/div[8]/select/option[2]")).click();
        Thread.sleep(1000);

        // PASO 10. Aplicar filtro
        System.out.println("PASO 10: Aplicando filtro");
        driver.findElement(By.xpath(
            "//*[@id=\"root\"]/div/div/main/div/div/div[1]/div/div[2]/button[1]")).click();
        Thread.sleep(5000);

        // PASO 11. Verificar resultado
        System.out.println("PASO 11: Verificando código del primer registro");
        WebElement celdaCodigo = driver.findElement(By.xpath(
            "//*[@id=\"root\"]/div/div/main/div/div/div[2]/div/table/tbody/tr[1]/td[1]/div/div/span[1]"));
        Assert.assertEquals(celdaCodigo.getText().trim(), "200",
            "El código del registro no corresponde a '200'");
    } 
}