X@echo off
setlocal EnableDelayedExpansion

title DOTECH - Apply Firebase Configuration
color 0B

echo ==============================================================================
echo                DOTECH - APPLY NEW FIREBASE CONFIGURATION                      
echo ==============================================================================
echo.

set "SCRIPT_DIR=%~dp0"
cd /d "%SCRIPT_DIR%"

set "SRC_JSON="
if exist "%USERPROFILE%\Downloads\google-services.json" (
    set "SRC_JSON=%USERPROFILE%\Downloads\google-services.json"
) else if exist "%SCRIPT_DIR%google-services.json" (
    set "SRC_JSON=%SCRIPT_DIR%google-services.json"
)

if not defined SRC_JSON (
    echo [NOTICE] 'google-services.json' was not found in Downloads or this folder.
    echo.
    echo Please download 'google-services.json' from your Firebase project:
    echo https://console.firebase.google.com/project/dotech-7a869/settings/general
    echo.
    echo Save it into: %USERPROFILE%\Downloads\google-services.json
    echo and run this script again.
    echo.
    pause
    exit /b 1
)

echo [1/3] Found new Firebase configuration at:
echo       %SRC_JSON%
echo.

echo [2/3] Copying to both application modules...
copy /y "%SRC_JSON%" "%SCRIPT_DIR%DoTechUser\app\google-services.json" >nul
if %errorlevel% equ 0 (
    echo   [OK] Updated: DoTechUser\app\google-services.json
) else (
    echo   [FAILED] Could not update DoTechUser
)

copy /y "%SRC_JSON%" "%SCRIPT_DIR%DoctorAppUi\DoctorAppUi\app\google-services.json" >nul
if %errorlevel% equ 0 (
    echo   [OK] Updated: DoctorAppUi\DoctorAppUi\app\google-services.json
) else (
    echo   [FAILED] Could not update DoctorAppUi
)

echo.
echo [3/3] Rebuilding both APKs with new Firebase config...
call "%SCRIPT_DIR%run_dotech.bat"

pause
