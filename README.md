# ATDD Sacra360 — Pruebas de Aceptación con Selenium

Proyecto de pruebas de aceptación automatizadas para el sistema **Sacra360**, organizado por módulos para que varios equipos trabajen en paralelo.

## Requisitos

| Herramienta | Versión mínima |
|-------------|----------------|
| Java JDK    | 11             |
| Maven       | 3.8+           |
| Google Chrome / Firefox / Edge | Cualquier versión reciente |

> **WebDriverManager** descarga el driver del navegador automáticamente. No hace falta instalar ChromeDriver manualmente.

---

## Configuración inicial

1. Clona el repositorio:
   ```bash
   git clone https://github.com/tu-org/ATDD-Sacra360.git
   cd ATDD-Sacra360
   ```

2. Abre `src/test/resources/config.properties` y ajusta:
   ```properties
   base.url=https://url-real-del-sistema.com
   browser=chrome          # chrome | firefox | edge
   headless=false          # true para correr sin ventana (CI/CD)
   test.user=tu@email.com
   test.password=tuPassword
   ```

---

## Cómo correr las pruebas

### Suite completa
```bash
mvn test
```

### Solo un módulo
```bash
mvn test -Dsuite=login
mvn test -Dsuite=dashboard
mvn test -Dsuite=reportes
mvn test -Dsuite=personas
mvn test -Dsuite=sacramentos
mvn test -Dsuite=auditorias
```

### Ver el reporte HTML
Después de correr, abre en el navegador:
```
reports/reporte.html
```

---

## Estructura del proyecto

```
ATDD-Sacra360/
├── pom.xml                                      # Dependencias Maven
├── testng-suites/
│   ├── all.xml                                  # Suite completa
│   ├── login.xml
│   ├── dashboard.xml
│   ├── reportes.xml
│   ├── personas.xml
│   ├── sacramentos.xml
│   └── auditorias.xml
└── src/
    ├── main/java/mx/sacra360/
    │   ├── base/
    │   │   ├── BasePage.java                    # Métodos comunes de Selenium
    │   │   └── BaseTest.java                    # Setup/teardown del driver y reportes
    │   ├── config/
    │   │   └── ConfigManager.java               # Lee config.properties
    │   ├── driver/
    │   │   └── DriverFactory.java               # Crea y destruye el WebDriver (thread-safe)
    │   ├── pages/                               # Page Objects por módulo
    │   │   ├── login/LoginPage.java
    │   │   ├── dashboard/DashboardPage.java
    │   │   ├── reportes/ReportesPage.java
    │   │   ├── personas/PersonaPage.java
    │   │   ├── sacramentos/SacramentosPage.java
    │   │   └── usuarios/UsuarioPage.java
    │   └── utils/
    │       ├── ReportManager.java               # Reporte HTML con ExtentReports
    │       └── ScreenshotUtil.java              # Captura automática al fallar
    └── test/java/mx/sacra360/                  # Clases de test por módulo
        ├── login/
        │   ├── LoginExitosoTest.java
        │   ├── LoginCredencialesInvalidasTest.java
        │   └── BloqueoUsuarioTest.java
        ├── reportes/
        │   └── GenerarReporteEstadisticasPDFTest.java
        ├── personas/
        │   ├── BuscarPersonaPorCiTest.java
        │   ├── BuscarPersonaFiltrosCombinados.java
        │   └── ValidarCamposRequeridosAgregarPersonaTest.java
        ├── sacramentos/
        │   ├── RegistrarBautizo.java
        │   ├── BuscarBautizoPorApellidoPaterno.java
        │   └── ValidarCamposObligatoriosBautizo.java
        ├── usuarios/
        │   ├── AgregarUsuarioTest.java
        │   ├── AgregarUsuarioEmailDuplicadoTest.java
        │   └── EditarUsuarioTest.java
        └── auditorias/
            ├── AuditoriaAplicacionFiltrosTest.java
            └── AuditoriaSeguridadFiltrosTest.java
```

---

## Módulos cubiertos

| Módulo       | Casos de prueba                                                                 |
|--------------|---------------------------------------------------------------------------------|
| Login        | Login exitoso, credenciales inválidas, bloqueo de usuario                       |
| Reportes     | Generación de reporte de estadísticas en PDF                                    |
| Personas     | Búsqueda por CI, filtros combinados, validación de campos requeridos            |
| Sacramentos  | Registrar bautizo, búsqueda por apellido paterno, campos obligatorios           |
| Usuarios     | Agregar usuario, email duplicado, editar usuario                                |
| Auditorías   | Filtros de auditoría de aplicación y seguridad                                  |

---

## Agregar un nuevo módulo

1. Crea la Page Object en `src/main/java/mx/sacra360/pages/<modulo>/`.
2. Crea el Test en `src/test/java/mx/sacra360/<modulo>/`, extendiendo `BaseTest`.
3. Crea `testng-suites/<modulo>.xml` copiando uno de los existentes.
4. Agrega el módulo al `testng-suites/all.xml`.

---

## Convenciones

- **Patrón de diseño**: Page Object Model (POM).
- **Idioma de tests**: Los nombres de métodos de test y los `ReportManager.info()` van en español para que los reportes sean legibles para todos.
- **Localizadores**: Siempre en la Page Object, nunca en el test.
- **Capturas automáticas**: Se toma screenshot automáticamente cuando un test falla; se guardan en `reports/screenshots/`.
- **Thread-safety**: `DriverFactory` usa `ThreadLocal`, por lo que los tests pueden correr en paralelo si se habilita `parallel="methods"` en el XML de la suite.
