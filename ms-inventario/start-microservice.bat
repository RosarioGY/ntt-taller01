@echo off
echo === Levantando Microservicio de Inventario ===

REM Buscar Maven en ubicaciones comunes
set MAVEN_FOUND=0
set MAVEN_CMD=

REM Verificar si mvn está en PATH
where mvn >nul 2>&1
if %errorlevel% == 0 (
    set MAVEN_CMD=mvn
    set MAVEN_FOUND=1
    echo Maven encontrado en PATH
    goto :found_maven
)

REM Buscar en Program Files
if exist "C:\Program Files\Apache\maven\bin\mvn.cmd" (
    set MAVEN_CMD="C:\Program Files\Apache\maven\bin\mvn.cmd"
    set MAVEN_FOUND=1
    echo Maven encontrado en Program Files
    goto :found_maven
)

REM Buscar en Program Files (x86)
if exist "C:\Program Files (x86)\Apache\maven\bin\mvn.cmd" (
    set MAVEN_CMD="C:\Program Files (x86)\Apache\maven\bin\mvn.cmd"
    set MAVEN_FOUND=1
    echo Maven encontrado en Program Files (x86)
    goto :found_maven
)

REM Buscar en ubicaciones de desarrollo comunes
if exist "C:\apache-maven\bin\mvn.cmd" (
    set MAVEN_CMD="C:\apache-maven\bin\mvn.cmd"
    set MAVEN_FOUND=1
    echo Maven encontrado en C:\apache-maven
    goto :found_maven
)

if exist "C:\tools\apache-maven\bin\mvn.cmd" (
    set MAVEN_CMD="C:\tools\apache-maven\bin\mvn.cmd"
    set MAVEN_FOUND=1
    echo Maven encontrado en C:\tools\apache-maven
    goto :found_maven
)

if %MAVEN_FOUND% == 0 (
    echo ERROR: No se pudo encontrar Maven instalado
    echo Por favor, asegurate de que Maven este instalado y configurado
    echo Puedes descargarlo desde: https://maven.apache.org/download.cgi
    pause
    exit /b 1
)

:found_maven
echo.
echo Verificando conexion a MongoDB...
netstat -an | findstr :27017 >nul
if %errorlevel% neq 0 (
    echo ERROR: MongoDB no esta ejecutandose en el puerto 27017
    echo Por favor, inicia MongoDB primero
    pause
    exit /b 1
)
echo MongoDB esta ejecutandose correctamente

echo.
echo Compilando el proyecto...
%MAVEN_CMD% clean compile
if %errorlevel% neq 0 (
    echo ERROR: Fallo la compilacion del proyecto
    pause
    exit /b 1
)

echo.
echo Ejecutando el microservicio de inventario...
echo El servicio estara disponible en: http://localhost:8081/api
echo Para detener el servicio, presiona Ctrl+C
echo.

%MAVEN_CMD% spring-boot:run
