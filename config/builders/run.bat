@echo off
setlocal enabledelayedexpansion

rem Set the JavaFX and MySQL Connector library paths
set "JAVAFX_PATH=libs\javafx"
set "DBCONNECTOR_PATH=libs\dbconnector\mysql-connector.jar"
set "MODULES=javafx.controls,javafx.fxml"

rem Initialize an empty variable to collect all .java files
set "javac_command="

rem Collect and compile all .java files found recursively
for /R %%f in (*.java) do (
  rem Extract the file name from the full path
  for %%x in ("%%f") do set "filename=%%~nx"
  echo Compiling !filename!...
  javac --module-path "%JAVAFX_PATH%" --add-modules %MODULES% -cp "%DBCONNECTOR_PATH%" -d . "%%f"
  if !ERRORLEVEL! neq 0 (
    echo Compilation failed for !filename!.
    exit /b !ERRORLEVEL!
  )
)

rem Run the Main class
echo Running...
java --module-path "%JAVAFX_PATH%" --add-modules %MODULES% -cp ".;%DBCONNECTOR_PATH%" app.Main

endlocal
