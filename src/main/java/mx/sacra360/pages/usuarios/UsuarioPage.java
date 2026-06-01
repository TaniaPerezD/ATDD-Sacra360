package mx.sacra360.pages.usuarios;

import mx.sacra360.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class UsuarioPage extends BasePage {

    //  Localizadores LOGIN 
    private final By campoEmail         = By.id("email");
    private final By campoPassword      = By.id("password");
    private final By botonIniciarSesion = By.xpath("//*[@id='root']/div/div[1]/div/div[2]/button");
    private final By botonConfirmarRegistro = By.xpath("//*[@id='root']/div/div/main/div/div[3]/div/div[2]/button[2]");

    // Localizadores FORMULARIO AGREGAR USUARIO 
    private final By tabAgregarUsuario  = By.xpath("//*[@id='root']/div/div/main/div/div[1]/button[1]");
    private final By campoNombre           = By.id("nombre");
    private final By campoApellidoPaterno  = By.id("apellido_paterno");
    private final By campoApellidoMaterno  = By.id("apellido_materno");
    private final By campoFechaNac         = By.id("fecha_nacimiento");
    private final By campoEmailUsuario     = By.id("email");
    private final By selectRol             = By.id("id_rol");
    private final By selectEstado          = By.id("activo");
    private final By botonCrearUsuario     = By.xpath("//*[@id='root']/div/div/main/div/div[2]/form/div[2]/button[1]");
    

    //  Localizadores VERIFICACIÓN 
    private final By labelNombre          = By.xpath("//label[contains(text(),'Nombre')]");
    private final By labelApellidoPaterno = By.xpath("//label[contains(text(),'Apellido paterno')]");
    private final By labelApellidoMaterno = By.xpath("//label[contains(text(),'Apellido materno')]");
    private final By labelFecha           = By.xpath("//label[contains(text(),'Fecha de nacimiento')]");
    private final By labelEmail           = By.xpath("//label[contains(text(),'Email')]");
    private final By labelRol             = By.xpath("//label[contains(text(),'Rol')]");
    private final By labelEstado          = By.xpath("//label[contains(text(),'Estado')]");
    //private final By mensajeExito         = By.xpath("//*[contains(@class,'success') or contains(@class,'toast') or contains(text(),'exitosamente') or contains(text(),'registrado')]");
    private final By mensajeExito = By.xpath("//*[contains(text(),'Usuario creado correctamente') or contains(text(),'Éxito') or contains(text(),'exitosamente')]");
    private final By mensajeError = By.cssSelector(".text-rose-800");

    // Localizadores Buscar/Editar
    private final By tabBuscarEditar     = By.xpath("//*[@id='root']/div/div/main/div/div[1]/button[2]");
    private final By filaUsuario         = By.xpath("//*[@id='root']/div/div/main/div/div[3]/div[2]/div/div/table/tbody/tr[6]");
    private final By campoFechaEdicion   = By.id("fecha_nacimiento");
    private final By selectRolEdicion    = By.id("id_rol");
    private final By botonGuardarCambios = By.xpath("/html/body/div[2]/div/div[3]/button[2]");
    private final By campoNombreReadOnly = By.xpath("//*[@id='nombre' and @readonly]");


    // MÉTODOS DE LOGIN
    public void iniciarSesion(String email, String password) throws InterruptedException {
        Thread.sleep(1000);
        type(campoEmail, email);
        Thread.sleep(800);
        type(campoPassword, password);
        Thread.sleep(600);
        click(botonIniciarSesion);
    }

    // MÉTODOS DE NAVEGACIÓN

    public void navegarAUsuarios() throws InterruptedException {
        driver.get("https://fronttaller0.vercel.app/usuarios");
        Thread.sleep(2000);
    }

    public void abrirTabAgregarUsuario() throws InterruptedException {
        click(tabAgregarUsuario);
        Thread.sleep(1000);
    }


    // MÉTODOS DEL FORMULARIO
    public void ingresarNombre(String nombre) throws InterruptedException {
        type(campoNombre, nombre);
        Thread.sleep(500);
    }

    public void ingresarApellidoPaterno(String apellido) throws InterruptedException {
        type(campoApellidoPaterno, apellido);
        Thread.sleep(500);
    }

    public void ingresarApellidoMaterno(String apellido) throws InterruptedException {
        type(campoApellidoMaterno, apellido);
        Thread.sleep(500);
    }

    public void ingresarFechaNacimiento(String fecha) throws InterruptedException {
        type(campoFechaNac, fecha);
        Thread.sleep(500);
    }

    public void ingresarEmail(String email) throws InterruptedException {
        type(campoEmailUsuario, email);
        Thread.sleep(500);
    }

    public void seleccionarRol() throws InterruptedException {
        WebElement el = waitForVisible(selectRol);
        new Select(el).selectByIndex(6); // option[4]
        Thread.sleep(500);
    }

    public void seleccionarEstado() throws InterruptedException {
        WebElement el = waitForVisible(selectEstado);
        new Select(el).selectByIndex(1); // option[2] = Activo
        Thread.sleep(500);
    }

    public void clickCrearUsuario() throws InterruptedException {
        click(botonCrearUsuario);
        Thread.sleep(500);
    }


    // MÉTODOS DE VERIFICACIÓN
    public boolean formularioEsVisible() {
        return isDisplayed(labelNombre)
            && isDisplayed(labelApellidoPaterno)
            && isDisplayed(labelApellidoMaterno)
            && isDisplayed(labelFecha)
            && isDisplayed(labelEmail)
            && isDisplayed(labelRol)
            && isDisplayed(labelEstado);
    }

    public boolean mensajeExitoEsVisible() {
        return isDisplayed(mensajeExito);
    }

    public String obtenerMensajeExito() {
        return getText(mensajeExito);
    }

    //Confirmar registro
    public void confirmarRegistro() throws InterruptedException {
        click(botonConfirmarRegistro);
        Thread.sleep(2000);
    }

    public boolean mensajeErrorEsVisible() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(mensajeError));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //Para editar
    public void abrirTabBuscarEditar() throws InterruptedException {
        click(tabBuscarEditar);
        Thread.sleep(1000);
    }

    public void seleccionarUsuarioDeLista() throws InterruptedException {
        click(filaUsuario);
        Thread.sleep(1000);
    }

    public boolean camposEstanBloqueados() {
        return isDisplayed(By.id("nombre"));
    }

    public String editarFechaNacimiento(String fecha) throws InterruptedException {
        type(campoFechaEdicion, fecha);
        Thread.sleep(500);
        return fecha;
    }

    public void seleccionarRolEdicion() throws InterruptedException {
        WebElement el = waitForVisible(selectRolEdicion);
        new Select(el).selectByIndex(6);
        Thread.sleep(500);
    }

    public void clickGuardarCambios() throws InterruptedException {
        click(botonGuardarCambios);
        Thread.sleep(500);
    }

    //PAra agreagr usuario eliminar fisico 
    public static String obtenerToken() throws Exception {
        java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
        String body = "{\"email\":\"tania.perez.d@ucb.edu.bo\",\"password\":\"M4rshallLee#\"}";
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
            .uri(java.net.URI.create("https://back-sacramentos.onrender.com/api/usuarios/"))
            .header("Content-Type", "application/json")
            .POST(java.net.http.HttpRequest.BodyPublishers.ofString(body))
            .build();
        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
        String json = response.body();
        System.out.println("RESPONSE LOGIN: " + json);
        int idx = json.indexOf("\"token\":\"") + 9;
        return json.substring(idx, json.indexOf("\"", idx));
    }

    public static void eliminarUsuarioFisico(String token, int idUsuario) throws Exception {
        java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
            .uri(java.net.URI.create("https://back-sacramentos.onrender.com/api/usuarios/eliminar-fisico/" + idUsuario))
            .header("Content-Type", "application/json")
            .header("x-token", token)
            .POST(java.net.http.HttpRequest.BodyPublishers.noBody())
            .build();
        java.net.http.HttpClient.newHttpClient().send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
    }

    public static int obtenerIdUsuarioPorEmail(String token, String email) throws Exception {
        java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
        // buscar en varias páginas
        for (int page = 1; page <= 5; page++) {
            java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create("https://back-sacramentos.onrender.com/api/usuarios/?page=" + page))
                .header("x-token", token)
                .GET()
                .build();
            java.net.http.HttpResponse<String> response = client.send(request,
                java.net.http.HttpResponse.BodyHandlers.ofString());
            String json = response.body();
            int emailIdx = json.indexOf(email);
            if (emailIdx == -1) continue;
            // buscar id_usuario antes del email
            String before = json.substring(0, emailIdx);
            int idIdx = before.lastIndexOf("\"id_usuario\":") + 13;
            String idStr = before.substring(idIdx).trim().replaceAll("[^0-9]", "");
            System.out.println("ID encontrado: " + idStr);
            return Integer.parseInt(idStr.substring(0, idStr.length() > 4 ? 4 : idStr.length()));
        }
        throw new RuntimeException("Usuario no encontrado: " + email);
    }
}