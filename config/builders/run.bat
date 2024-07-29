@echo off
setlocal enabledelayedexpansion

rem Set the JavaFX, MySQL Connector, and Apache POI library paths
set "JAVAFX_PATH=libs\javafx\windows"
set "DBCONNECTOR_PATH=libs\dbconnector\mysql-connector.jar"
set "POI_PATH=libs\apachepoi"
set "MODULES=javafx.controls,javafx.fxml"

rem Initialize an empty variable to collect all .java files
set "javac_command="

rem Collect all POI JAR files
set "POI_JARS="
for /R "%POI_PATH%" %%i in (*.jar) do (
  set "POI_JARS=!POI_JARS!;%%i"
)

rem Collect and compile all .java files found recursively
for /R %%f in (*.java) do (
  rem Extract the file name from the full path
  for %%x in ("%%f") do set "filename=%%~nx"
  echo Compiling !filename!...
  javac --module-path "%JAVAFX_PATH%" --add-modules %MODULES% -cp "%DBCONNECTOR_PATH%;%POI_JARS%" -d . "%%f"
  if !ERRORLEVEL! neq 0 (
    echo Compilation failed for !filename!.
    exit /b !ERRORLEVEL!
  )
)

rem Run the Main class
echo Running...
java --module-path "%JAVAFX_PATH%" --add-modules %MODULES% -cp ".;%DBCONNECTOR_PATH%;%POI_JARS%" app.Main

endlocal
