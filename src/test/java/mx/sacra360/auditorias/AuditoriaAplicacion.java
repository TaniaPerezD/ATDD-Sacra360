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
import org.openqa.selenium.support.ui.Select;
import java.io.FileInputStream;
import java.util.List;
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

    @Test
    public void pruebaAuditoriaSeguridad() {
        
        /************** 1. Preparación de la prueba (Configuración) ***********/
        String baseUrl = prop.getProperty("base.url");
        String usuario = prop.getProperty("test.user");
        String contrasenia = prop.getProperty("test.password");

        // Ir a la página base para iniciar sesión
        driver.get(baseUrl);

        /************** 2. Lógica de la prueba (Flujo de pasos) ***************/
        
        // Introducir correo electrónico
        driver.findElement(By.xpath("//*[@id=\"email\"]")).sendKeys(usuario);
        
        // Introducir la contraseña
        driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys(contrasenia);
        
        // Presionar el botón de iniciar sesión
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[1]/div/div[2]/button")).click();
        
        // Esperar un momento a que procese la autenticación e ingrese al sistema
        try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }

        // Navegar a la sección de auditoría de seguridad
        driver.get(baseUrl + "/auditoria-seguridad");
        try { TimeUnit.SECONDS.sleep(4); } catch (InterruptedException e) { e.printStackTrace(); }

        // INTENTO DE CLIC EN EL MENÚ SUPERIOR
        // Si la pantalla es grande y el menú ya está abierto, este botón no será interactuable.
        try {
            driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/main/header/div[1]/button")).click();
            TimeUnit.SECONDS.sleep(1);
        } catch (Exception e) {
            System.out.println("El botón del menú superior no era interactuable (probablemente el menú ya está desplegado). Pasando al siguiente paso...");
        }
        
        // Presionar la cuarta opción del menú lateral (a[4])
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/aside/nav/a[5]")).click();
        try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }

        // Presionar el botón para desplegar los filtros  
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/main/div/div/div[1]/button")).click();
        try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }
        List<WebElement> selects =
    driver.findElements(By.tagName("select"));

System.out.println("Cantidad de selects: " + selects.size());
        // Seleccionar directamente la opción "Exitoso" dentro del componente select
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/main/div/div/div[1]/div/div[1]/div[5]/select/option[2]")).click();

        // Presionar el botón para aplicar el filtro
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/main/div/div/div[1]/div/div[2]/button[1]")).click();

        // Esperar a que la tabla recargue con los nuevos datos filtrados
        try { TimeUnit.SECONDS.sleep(5); } catch (InterruptedException e) { e.printStackTrace(); }

        /************ 3. Verificación de la situación esperada - Assert ***************/
        
        // Capturar el elemento de la celda donde sale el estado de la respuesta
        WebElement celdaEstado = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/main/div/div/div[2]/div/table/tbody/tr[1]/td[2]/span"));
        String txtResultado = celdaEstado.getText().trim();
        
        System.out.println("Texto obtenido de la tabla: " + txtResultado);

        // Si el texto de la tabla es "Exitoso", la prueba pasará limpiamente
        Assert.assertEquals(txtResultado, "Exitoso");
    }
}