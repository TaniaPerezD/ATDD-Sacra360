package mx.sacra360.auditorias;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class AuditoriaAplicacion {

    private WebDriver driver;
    private Properties prop;

    @BeforeTest
    public void setDriver() throws Exception {
        // Cargar el archivo config.properties dinámicamente
        prop = new Properties();
        FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
        prop.load(fis);

        // Inicializar ChromeDriver
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }
    @BeforeMethod
    public void limpiarSesion() {
        driver.manage().deleteAllCookies();
    }

    @AfterTest
    public void closeDriver() throws Exception {
        if (driver != null) {
            driver.quit();
        }
    }

    // --- CASO 1: AUDITORÍA DE SEGURIDAD (El que ya te funciona) ---
    @Test
    public void pruebaAuditoriaSeguridad() {
        String baseUrl = prop.getProperty("base.url");
        String usuario = prop.getProperty("test.user");
        String contrasenia = prop.getProperty("test.password");

        driver.get(baseUrl);

        driver.findElement(By.xpath("//*[@id=\"email\"]")).sendKeys(usuario);
        driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys(contrasenia);
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[1]/div/div[2]/button")).click();
        try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }

        driver.get(baseUrl + "/auditoria-seguridad");
        try { TimeUnit.SECONDS.sleep(4); } catch (InterruptedException e) { e.printStackTrace(); }

        try {
            driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/main/header/div[1]/button")).click();
            TimeUnit.SECONDS.sleep(1);
        } catch (Exception e) {
            System.out.println("Menú superior ya desplegado.");
        }
        
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/aside/nav/a[5]")).click();
        try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }

        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/main/div/div/div[1]/button")).click();
        try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }

        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/main/div/div/div[1]/div/div[1]/div[5]/select/option[2]")).click();
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/main/div/div/div[1]/div/div[2]/button[1]")).click();
        try { TimeUnit.SECONDS.sleep(5); } catch (InterruptedException e) { e.printStackTrace(); }

        WebElement celdaEstado = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/main/div/div/div[2]/div/table/tbody/tr[1]/td[2]/span"));
        String txtResultado = celdaEstado.getText().trim();
        
        Assert.assertEquals(txtResultado, "Exitoso");
    }

    // --- CASO 2: AUDITORÍA DE APLICACIÓN (El nuevo caso) ---
    @Test
    public void pruebaAuditoriaAplicacion() {
        /************** 1. Preparación de la prueba ***********/
        String baseUrl = prop.getProperty("base.url");
        String usuario = prop.getProperty("test.user");
        String contrasenia = prop.getProperty("test.password");

        driver.get(baseUrl);

        /************** 2. Lógica de la prueba ***************/
        // Login
        driver.findElement(By.xpath("//*[@id=\"email\"]")).sendKeys(usuario);
        driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys(contrasenia);
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[1]/div/div[2]/button")).click();
        try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }

        // Navegar a la sección de aplicación
        driver.get(baseUrl + "/auditoria-aplicacion");
        try { TimeUnit.SECONDS.sleep(4); } catch (InterruptedException e) { e.printStackTrace(); }

        // Manejo del menú superior por si acaso
        try {
            driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/main/header/div[1]/button")).click();
            TimeUnit.SECONDS.sleep(1);
        } catch (Exception e) {
            System.out.println("Menú superior ya desplegado.");
        }
        
        // Seleccionar Auditoría de Aplicación en el menú lateral (a[4])
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/aside/nav/a[4]")).click();
        try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }

        // Desplegar Filtros
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/main/div/div/div[1]/button")).click();
        try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }

        // Seleccionar la opción "200" (option[2]) en el select dentro de div[8]
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/main/div/div/div[1]/div/div[1]/div[8]/select/option[2]")).click();

        // Aplicar Filtro
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/main/div/div/div[1]/div/div[2]/button[1]")).click();
        try { TimeUnit.SECONDS.sleep(5); } catch (InterruptedException e) { e.printStackTrace(); }

        /************ 3. Verificación - Assert ***************/
        // Capturar el span de la respuesta que debe contener el código "200"
        WebElement celdaCodigo = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/main/div/div/div[2]/div/table/tbody/tr[1]/td[1]/div/div/span[1]"));
        String txtCodigoObtenido = celdaCodigo.getText().trim();
        
        System.out.println("Código obtenido de la tabla de aplicación: " + txtCodigoObtenido);

        // Si dice "200", pasa la prueba perfectamente
        Assert.assertEquals(txtCodigoObtenido, "200");
    }
}