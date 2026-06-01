package mx.sacra360.auditorias;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

/****************************************
 * Historia de Usuario:
 * Como oficial de seguridad de la información quiero filtrar los registros 
 * de auditoría por estado para verificar únicamente las acciones que fueron exitosas.
 *
 * Prueba de Aceptación / Caso de Prueba S360-88:
 * Filtrar registros de auditoría de seguridad por estado Exitoso
 *
 * PASO 1. Ingresar a la página base y comprobar estado de sesión (cerrar previa si existe)
 * PASO 2. Introducir correo electrónico válido
 * PASO 3. Introducir contraseña válida
 * PASO 4. Hacer clic en el botón de iniciar sesión
 * PASO 5. Navegar directamente a la sección de Auditoría de Seguridad
 * PASO 6. Desplegar el menú superior (si no se encuentra expandido por defecto)
 * PASO 7. Hacer clic en la opción "Seguridad" en el menú lateral (vínculo a[5])
 * PASO 8. Hacer clic en el botón para desplegar el panel de filtros
 * PASO 9. Seleccionar la opción "Exitoso" dentro del componente select de estados
 * PASO 10. Hacer clic en el botón para aplicar el filtro seleccionado
 * PASO 11. Verificar el estado del primer registro de la tabla resultante
 *
 * Resultado Esperado:
 * La tabla se actualiza correctamente mostrando que la columna despliega el mensaje "Exitoso"
 ****************************************/

// Comando para ejecutar esta prueba específica desde la terminal:
// mvn clean compile test -Dtest=AuditoriaSeguridadFiltrosTest

public class AuditoriaSeguridadFiltrosTest {
    
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

    @Test(description = "S360-88: Filtrar registros de auditoría de seguridad por estado Exitoso")
    public void pruebaAuditoriaSeguridad() throws InterruptedException {

        /********** Preparación de la Prueba **********/
        String baseUrl = prop.getProperty("base.url");
        String usuario = prop.getProperty("test.user");
        String contrasenia = prop.getProperty("test.password");

        // PASO 1. Ingresar a la página base y comprobar estado de sesión
        System.out.println("PASO 1: Navegando a la URL base y verificando sesión activa");
        driver.get(baseUrl);
        Thread.sleep(2000);

        List<WebElement> loginInput = driver.findElements(By.xpath("//*[@id=\"email\"]"));
        if (loginInput.isEmpty()) {
            System.out.println("Sesión previa detectada de forma síncrona. Ejecutando logout...");
            driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/main/header/div[2]/div/button")).click();
            Thread.sleep(1000);
            driver.findElement(By.xpath("/html/body/div[2]/div/div[6]/button[3]")).click();
            Thread.sleep(3000);
            driver.get(baseUrl);
            Thread.sleep(2000);
        }

        // PASO 2. Introducir correo electrónico válido
        System.out.println("PASO 2: Introduciendo correo electrónico válido");
        driver.findElement(By.xpath("//*[@id=\"email\"]")).sendKeys(usuario);
        Thread.sleep(800);
        
        // PASO 3. Introducir contraseña válida
        System.out.println("PASO 3: Introduciendo contraseña válida");
        driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys(contrasenia);
        Thread.sleep(800);
        
        // PASO 4. Hacer clic en el botón de iniciar sesión
        System.out.println("PASO 4: Haciendo clic en el botón de iniciar sesión");
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[1]/div/div[2]/button")).click();
        Thread.sleep(3000);

        // PASO 5. Navegar directamente a la sección de Auditoría de Seguridad
        System.out.println("PASO 5: Navegando a la sección de auditoría de seguridad");
        driver.get(baseUrl + "/auditoria-seguridad");
        Thread.sleep(4000);

        // PASO 6. Desplegar el menú superior (si no se encuentra expandido por defecto)
        System.out.println("PASO 6: Gestionando visibilidad del menú superior");
        try {
            driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/main/header/div[1]/button")).click();
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Menú superior ya se encontraba desplegado de forma nativa.");
        }
        
        // PASO 7. Hacer clic en la opción "Seguridad" en el menú lateral (vínculo a[5])
        System.out.println("PASO 7: Seleccionando 'Seguridad' en el menú lateral (a[5])");
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/aside/nav/a[5]")).click();
        Thread.sleep(3000);

        /********** Lógica de la Prueba **********/

        // PASO 8. Hacer clic en el botón para desplegar el panel de filtros
        System.out.println("PASO 8: Desplegando el panel de componentes para filtros");
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/main/div/div/div[1]/button")).click();
        Thread.sleep(2000);
        
        List<WebElement> selects = driver.findElements(By.tagName("select"));
        System.out.println("Cantidad de selects detectados en pantalla: " + selects.size());

        // PASO 9. Seleccionar la opción "Exitoso" dentro del componente select de estados
        System.out.println("PASO 9: Seleccionando el valor de opción 'Exitoso' (div[5])");
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/main/div/div/div[1]/div/div[1]/div[5]/select/option[2]")).click();
        Thread.sleep(1000);

        // PASO 10. Hacer clic en el botón para aplicar el filtro seleccionado
        System.out.println("PASO 10: Presionando el botón para aplicar la consulta");
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/main/div/div/div[1]/div/div[2]/button[1]")).click();
        Thread.sleep(5000);

        /********** Verificación del Resultado Esperado - Assert **********/

        // PASO 11. Verificar el estado del primer registro de la tabla resultante
        System.out.println("PASO 11 - VERIFICACIÓN: Validando estado del primer elemento filtrado");
        WebElement celdaEstado = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/main/div/div/div[2]/div/table/tbody/tr[1]/td[2]/span"));
        String txtResultado = celdaEstado.getText().trim();
        
        Assert.assertEquals(txtResultado, "Exitoso", "La fila recuperada no cuenta con la etiqueta de estado 'Exitoso'");
    }
}