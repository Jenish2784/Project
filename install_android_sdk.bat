@echo off
setlocal

title DOTECH - Android SDK Auto-Installer
color 0B

echo ==============================================================================
echo                DOTECH - ANDROID SDK AUTOMATIC INSTALLER                       
echo ==============================================================================
echo.
echo Installing Android SDK into T:\Android\Sdk (avoids filling C: drive)...
echo.

powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0install_android_sdk.ps1"

echo.
pause
