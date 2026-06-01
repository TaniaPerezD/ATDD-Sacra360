package mx.sacra360.auditorias;
//mvn clean compile test -Dtest=AuditoriaSeguridad
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert; // Importación corregida que faltaba
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class AuditoriaSeguridad {
    
    private WebDriver driver;
    private Properties prop;

    @BeforeTest
    public void setDriver() throws Exception {
        // Cargar el archivo config.properties dinámicamente
        prop = new Properties();
        FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
        prop.load(fis);

        // Inicializar ChromeDriver de forma limpia
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

    // =========================================================================
    // CASO 1: AUDITORÍA DE SEGURIDAD
    // =========================================================================
    @Test
    public void pruebaAuditoriaSeguridad() {
        /************** 1. Preparación de la prueba ***********/
        String baseUrl = prop.getProperty("base.url");
        String usuario = prop.getProperty("test.user");
        String contrasenia = prop.getProperty("test.password");

        driver.get(baseUrl);

        /************** 2. Lógica de la prueba ***************/
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
            System.out.println("El botón del menú superior no era interactuable. Pasando...");
        }
        
        // Menú lateral: Seguridad (a[5])
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/aside/nav/a[5]")).click();
        try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }

        // Desplegar filtros
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/main/div/div/div[1]/button")).click();
        try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }
        
        List<WebElement> selects = driver.findElements(By.tagName("select"));
        System.out.println("Cantidad de selects en Seguridad: " + selects.size());

        // Seleccionar opción "Exitoso" (div[5])
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/main/div/div/div[1]/div/div[1]/div[5]/select/option[2]")).click();

        // Aplicar filtro
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/main/div/div/div[1]/div/div[2]/button[1]")).click();
        try { TimeUnit.SECONDS.sleep(5); } catch (InterruptedException e) { e.printStackTrace(); }

        /************ 3. Verificación - Assert ***************/
        WebElement celdaEstado = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/main/div/div/div[2]/div/table/tbody/tr[1]/td[2]/span"));
        String txtResultado = celdaEstado.getText().trim();
        
        System.out.println("Texto obtenido de la tabla Seguridad: " + txtResultado);
        Assert.assertEquals(txtResultado, "Exitoso");
    }
}