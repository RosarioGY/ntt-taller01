@echo off
echo === Microservicio de Inventario - Script de Inicio ===
echo.

REM Verificar MongoDB
echo Verificando MongoDB...
netstat -an | findstr :27017 >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: MongoDB no esta ejecutandose en el puerto 27017
    echo.
    echo Para iniciar MongoDB, ejecuta uno de estos comandos:
    echo   - Si usas MongoDB Compass: Inicia MongoDB Compass
    echo   - Si usas Docker: docker run -d -p 27017:27017 mongo
    echo   - Si tienes MongoDB instalado: net start MongoDB
    echo.
    pause
    exit /b 1
)
echo ✓ MongoDB esta ejecutandose correctamente

echo.
echo Buscando instalaciones de Java y Maven...

REM Buscar Java en ubicaciones comunes
set JAVA_HOME_FOUND=
for /d %%i in ("C:\Program Files\Java\jdk*") do set JAVA_HOME_FOUND=%%i
for /d %%i in ("C:\Program Files\Java\jre*") do set JAVA_HOME_FOUND=%%i
for /d %%i in ("C:\Program Files (x86)\Java\jdk*") do set JAVA_HOME_FOUND=%%i

if defined JAVA_HOME_FOUND (
    echo ✓ Java encontrado en: %JAVA_HOME_FOUND%
    set JAVA_CMD=%JAVA_HOME_FOUND%\bin\java.exe
) else (
    echo ! Java no encontrado en ubicaciones comunes
)

REM Buscar Maven en ubicaciones comunes
set MAVEN_HOME_FOUND=
for /d %%i in ("C:\Program Files\Apache\maven*") do set MAVEN_HOME_FOUND=%%i
for /d %%i in ("C:\Program Files (x86)\Apache\maven*") do set MAVEN_HOME_FOUND=%%i
for /d %%i in ("C:\apache-maven*") do set MAVEN_HOME_FOUND=%%i
for /d %%i in ("C:\tools\apache-maven*") do set MAVEN_HOME_FOUND=%%i

if defined MAVEN_HOME_FOUND (
    echo ✓ Maven encontrado en: %MAVEN_HOME_FOUND%
    set MVN_CMD=%MAVEN_HOME_FOUND%\bin\mvn.cmd
) else (
    echo ! Maven no encontrado en ubicaciones comunes
)

echo.
echo === INSTRUCCIONES PARA LEVANTAR EL MICROSERVICIO ===
echo.
echo Opcion 1 - Si tienes Maven configurado correctamente:
echo   mvn spring-boot:run
echo.
echo Opcion 2 - Si tienes un IDE como IntelliJ IDEA o Eclipse:
echo   1. Abre el proyecto en tu IDE
echo   2. Ejecuta la clase InventoryApplication.java
echo.
echo Opcion 3 - Usando Docker (si esta disponible):
echo   docker-compose up --build
echo.
echo Opcion 4 - Compilacion manual:
echo   1. Asegurate de que JAVA_HOME este configurado
echo   2. Ejecuta: javac -cp "path\to\dependencies\*" src\main\java\com\microservices\inventory\*.java
echo.
echo El microservicio deberia estar disponible en:
echo   http://localhost:8081/api
echo.
echo Para verificar que funciona:
echo   http://localhost:8081/api/actuator/health
echo.

REM Intentar con las rutas encontradas
if defined MVN_CMD (
    echo Intentando iniciar con Maven encontrado...
    echo Ejecutando: %MVN_CMD% spring-boot:run
    "%MVN_CMD%" spring-boot:run
) else (
    echo.
    echo NOTA: Para una configuracion completa, asegurate de tener:
    echo 1. Java 17 instalado y JAVA_HOME configurado
    echo 2. Maven instalado y en el PATH
    echo 3. MongoDB ejecutandose en puerto 27017
    echo.
    pause
)
