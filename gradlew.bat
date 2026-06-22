@echo off
setlocal enabledelayedexpansion
set GRADLE_VERSION=9.2.0
set DIR=%~dp0
set GRADLE_HOME=%DIR%.gradle-local\gradle-%GRADLE_VERSION%
set GRADLE_BIN=%GRADLE_HOME%\bin\gradle.bat

if exist "%GRADLE_BIN%" goto run_gradle

echo Gradle %GRADLE_VERSION% not found. Downloading...
if not exist "%DIR%.gradle-local" mkdir "%DIR%.gradle-local"
powershell -NoProfile -ExecutionPolicy Bypass -Command "[Net.ServicePointManager]::SecurityProtocol=[Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://services.gradle.org/distributions/gradle-%GRADLE_VERSION%-bin.zip' -OutFile '%DIR%.gradle-local\gradle-%GRADLE_VERSION%-bin.zip'"
if errorlevel 1 exit /b %errorlevel%

echo Extracting Gradle %GRADLE_VERSION%...
powershell -NoProfile -ExecutionPolicy Bypass -Command "Expand-Archive -Path '%DIR%.gradle-local\gradle-%GRADLE_VERSION%-bin.zip' -DestinationPath '%DIR%.gradle-local' -Force"
if errorlevel 1 exit /b %errorlevel%

:run_gradle
call "%GRADLE_BIN%" %*
