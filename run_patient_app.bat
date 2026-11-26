@echo off
setlocal EnableDelayedExpansion

title DOTECH - Patient App Runner
color 0B

set "SCRIPT_DIR=%~dp0"
cd /d "%SCRIPT_DIR%"

:: Set Java 21
if exist "C:\Program Files\Java\jdk-21" (
    set "JAVA_HOME=C:\Program Files\Java\jdk-21"
) else if exist "C:\Program Files\Java\jdk-17" (
    set "JAVA_HOME=C:\Program Files\Java\jdk-17"
)

:: Locate SDK and ADB
set "SDK_PATH=T:\Android\Sdk"
if not exist "%SDK_PATH%" (
    if defined ANDROID_HOME if exist "%ANDROID_HOME%" set "SDK_PATH=%ANDROID_HOME%"
    if exist "%LOCALAPPDATA%\Android\Sdk" set "SDK_PATH=%LOCALAPPDATA%\Android\Sdk"
)

set "ADB_EXE=%SDK_PATH%\platform-tools\adb.exe"
if not exist "%ADB_EXE%" (
    where adb >nul 2>&1
    if !errorlevel! equ 0 set "ADB_EXE=adb"
)

set "APK_PATH=%SCRIPT_DIR%DoTechUser\app\build\outputs\apk\debug\app-debug.apk"

echo ==============================================================================
echo                 DOTECH - PATIENT APPLICATION RUNNER                          
echo ==============================================================================
echo.

:: 1. Verify APK exists or build it
if exist "%APK_PATH%" goto APK_EXISTS
echo [INFO] APK not found. Building Patient App APK now...
cd /d "%SCRIPT_DIR%DoTechUser"
call gradlew.bat assembleDebug
cd /d "%SCRIPT_DIR%"
if not exist "%APK_PATH%" (
    echo.
    echo [ERROR] Failed to build APK. Please check for errors above.
    pause
    exit /b 1
)

:APK_EXISTS
echo [OK] APK Found: %APK_PATH%
echo.

:: 2. Check for connected device or emulator
:CHECK_DEVICE
if not exist "%ADB_EXE%" (
    echo [WARNING] ADB tool not found at %ADB_EXE%.
    echo Please run 'run_dotech.bat' and select option [S] to install SDK.
    pause
    exit /b 1
)

echo Checking for connected Android phone or Emulator...
"%ADB_EXE%" get-state >nul 2>&1
if %errorlevel% neq 0 goto NO_DEVICE
goto DEVICE_FOUND

:NO_DEVICE
echo.
echo ==============================================================================
echo [NO DEVICE DETECTED]
echo No Android phone or laptop emulator is currently connected.
echo.
echo HOW TO RUN THE APP ON YOUR LAPTOP OR PHONE:
echo.
echo METHOD 1: RUN ON YOUR ANDROID PHONE VIA USB CABLE
echo   1. Connect your phone to your laptop using a USB cable.
echo   2. On your phone, enable "Developer Options" and turn ON "USB Debugging".
echo   3. When prompted on your phone screen, tap "Allow USB Debugging".
echo.
echo METHOD 2: RUN ON AN ANDROID EMULATOR ON YOUR LAPTOP
echo   1. Start an Android emulator like BlueStacks, LDPlayer, NoxPlayer,
echo      or the Android Studio Virtual Device (AVD).
echo   2. Keep the emulator running on your laptop.
echo.
echo METHOD 3: INSTALL THE APK DIRECTLY
echo   You can copy the APK file directly to your phone or drag-and-drop
echo   it into BlueStacks / LDPlayer!
echo   APK Location:
echo   %APK_PATH%
echo ==============================================================================
echo.
echo  [R] Retry device detection (after connecting phone or starting emulator)
echo  [O] Open folder containing the APK file
echo  [E] Exit
echo.
set /p choice="Please select an option [R/O/E]: "
if /i "%choice%"=="r" goto CHECK_DEVICE
if /i "%choice%"=="o" (
    explorer /select,"%APK_PATH%"
    goto CHECK_DEVICE
)
exit /b 0

:DEVICE_FOUND
:: 3. Device found - install and launch
echo [OK] Android device / emulator detected!
echo.
echo [1/2] Installing Patient App (DoTechUser) to device...
"%ADB_EXE%" install -r "%APK_PATH%"
if %errorlevel% neq 0 (
    echo.
    echo [WARNING] Standard install failed. Attempting clean install...
    "%ADB_EXE%" uninstall com.example.dotechuser >nul 2>&1
    "%ADB_EXE%" install -r "%APK_PATH%"
)

echo.
echo [2/2] Launching Patient App (SplashScreenActivity)...
"%ADB_EXE%" shell am start -n com.example.dotechuser/.SplashScreenActivity

echo.
echo ==============================================================================
echo [SUCCESS] Patient App launched successfully on your device!
echo ==============================================================================
echo.
pause
exit /b 0
