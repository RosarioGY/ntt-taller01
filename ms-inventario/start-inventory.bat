@echo off
setlocal EnableDelayedExpansion

echo =========================================================
echo        MICROSERVICIO DE INVENTARIO - INICIADOR
echo =========================================================
echo.

REM Verificar MongoDB
echo [1/4] Verificando MongoDB...
netstat -an | findstr :27017 >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ ERROR: MongoDB no esta ejecutandose en el puerto 27017
    echo.
    echo Para iniciar MongoDB:
    echo   - MongoDB Compass: Conectar a localhost:27017
    echo   - Docker: docker run -d -p 27017:27017 mongo
    echo   - Servicio Windows: net start MongoDB
    pause
    exit /b 1
)
echo ✅ MongoDB ejecutandose correctamente

REM Buscar Java
echo.
echo [2/4] Buscando Java...
set JAVA_FOUND=0

REM Buscar java.exe en PATH
where java >nul 2>&1
if %errorlevel% == 0 (
    set JAVA_CMD=java
    set JAVA_FOUND=1
    echo ✅ Java encontrado en PATH
    goto :java_found
)

REM Buscar en ubicaciones comunes de Java
for /d %%d in ("C:\Program Files\Java\jdk*") do (
    if exist "%%d\bin\java.exe" (
        set JAVA_CMD="%%d\bin\java.exe"
        set JAVA_FOUND=1
        echo ✅ Java encontrado en: %%d
        goto :java_found
    )
)

for /d %%d in ("C:\Program Files (x86)\Java\jdk*") do (
    if exist "%%d\bin\java.exe" (
        set JAVA_CMD="%%d\bin\java.exe"
        set JAVA_FOUND=1
        echo ✅ Java encontrado en: %%d
        goto :java_found
    )
)

if %JAVA_FOUND% == 0 (
    echo ❌ Java no encontrado
    echo Por favor instala Java 11 o superior desde: https://openjdk.org/
    pause
    exit /b 1
)

:java_found

REM Buscar Maven
echo.
echo [3/4] Buscando Maven...
set MAVEN_FOUND=0

REM Verificar si Maven ya funcionó anteriormente (verificar target/)
if exist "target\classes" (
    echo ✅ Proyecto ya compilado anteriormente
    goto :run_direct
)

REM Buscar mvn.cmd en PATH
where mvn >nul 2>&1
if %errorlevel% == 0 (
    set MVN_CMD=mvn
    set MAVEN_FOUND=1
    echo ✅ Maven encontrado en PATH
    goto :maven_found
)

REM Buscar Maven en la ruta que sabemos que existe
if exist "C:\tools\apache-maven-3.9.11\bin\mvn.cmd" (
    set MVN_CMD="C:\tools\apache-maven-3.9.11\bin\mvn.cmd"
    set MAVEN_FOUND=1
    echo ✅ Maven encontrado en: C:\tools\apache-maven-3.9.11
    goto :maven_found
)

REM Buscar en otras ubicaciones comunes
for /d %%d in ("C:\tools\apache-maven*") do (
    if exist "%%d\bin\mvn.cmd" (
        set MVN_CMD="%%d\bin\mvn.cmd"
        set MAVEN_FOUND=1
        echo ✅ Maven encontrado en: %%d
        goto :maven_found
    )
)

if %MAVEN_FOUND% == 0 (
    echo ❌ Maven no encontrado
    echo Intentando compilacion manual...
    goto :compile_manual
)

:maven_found
echo.
echo [4/4] Compilando e iniciando con Maven...
echo Ejecutando: %MVN_CMD% clean spring-boot:run
call %MVN_CMD% clean spring-boot:run
goto :end

:compile_manual
echo.
echo [4/4] Compilacion manual sin Maven...
echo ⚠️  ATENCION: Esta es una alternativa sin Maven
echo.
echo INSTRUCCIONES MANUALES:
echo 1. Abre tu IDE favorito (IntelliJ IDEA, Eclipse, VS Code)
echo 2. Importa el proyecto como proyecto Maven
echo 3. Ejecuta la clase: InventoryApplication.java
echo.
echo O alternativamente:
echo 1. Descarga Maven desde: https://maven.apache.org/download.cgi
echo 2. Configura MAVEN_HOME y agrega %%MAVEN_HOME%%\bin al PATH
echo 3. Ejecuta nuevamente este script
goto :end

:run_direct
echo.
echo [4/4] Ejecutando directamente desde clases compiladas...
echo Buscando archivo JAR compilado...

if exist "target\ms-inventario-1.0.0.jar" (
    echo ✅ JAR encontrado, ejecutando...
    %JAVA_CMD% -jar target\ms-inventario-1.0.0.jar
) else (
    echo ❌ JAR no encontrado
    echo Usa tu IDE para ejecutar InventoryApplication.java
)

:end
echo.
echo =========================================================
echo El microservicio deberia estar disponible en:
echo   🌐 http://localhost:8081/api
echo   ❤️  Health check: http://localhost:8081/api/actuator/health
echo =========================================================
pause
