@echo off
setlocal EnableDelayedExpansion

title DOTECH - Environment Setup and Verification
color 0B

echo ==============================================================================
echo                DOTECH PROJECT ENVIRONMENT SETUP AND REPAIR                      
echo ==============================================================================
echo.

set "SCRIPT_DIR=%~dp0"
cd /d "%SCRIPT_DIR%"

:: 1. Check and Set Java
echo [1/4] Checking Java JDK (JDK 17 or 21 required for AGP 8.x)...
if exist "C:\Program Files\Java\jdk-21" (
    set "JAVA_HOME=C:\Program Files\Java\jdk-21"
    echo   [OK] Found JDK 21 at: !JAVA_HOME!
) else if exist "C:\Program Files\Java\jdk-17" (
    set "JAVA_HOME=C:\Program Files\Java\jdk-17"
    echo   [OK] Found JDK 17 at: !JAVA_HOME!
) else if defined JAVA_HOME (
    echo   [INFO] Using existing JAVA_HOME: %JAVA_HOME%
) else (
    echo   [WARNING] JDK 17/21 not found in default path.
    echo   Please install JDK 17 or JDK 21 if Gradle fails with Java version error.
)

:: 2. Check Android SDK
echo.
echo [2/4] Detecting Android SDK path...
set "DETECTED_SDK="
if exist "T:\Android\Sdk" (
    set "DETECTED_SDK=T:\Android\Sdk"
)
if not defined DETECTED_SDK if defined ANDROID_HOME (
    if exist "%ANDROID_HOME%" set "DETECTED_SDK=%ANDROID_HOME%"
)
if not defined DETECTED_SDK if defined ANDROID_SDK_ROOT (
    if exist "%ANDROID_SDK_ROOT%" set "DETECTED_SDK=%ANDROID_SDK_ROOT%"
)
if not defined DETECTED_SDK (
    if exist "%LOCALAPPDATA%\Android\Sdk" set "DETECTED_SDK=%LOCALAPPDATA%\Android\Sdk"
)
if not defined DETECTED_SDK (
    if exist "C:\Users\%USERNAME%\AppData\Local\Android\Sdk" set "DETECTED_SDK=C:\Users\%USERNAME%\AppData\Local\Android\Sdk"
)

if defined DETECTED_SDK if exist "!DETECTED_SDK!" (
    echo   [OK] Android SDK detected at: !DETECTED_SDK!
) else (
    echo   [WARNING] Android SDK folder does not physically exist yet!
    echo   Run 'install_android_sdk.bat' to download and install it onto T:\Android\Sdk automatically.
    set "DETECTED_SDK=T:\Android\Sdk"
)

:: 3. Generate / Update local.properties in all projects
echo.
echo [3/4] Updating local.properties configuration...
set "ESCAPED_SDK=!DETECTED_SDK:\=\\!"
set "ESCAPED_SDK=!ESCAPED_SDK::=\:!"

if exist "DoTechUser" (
    echo sdk.dir=!ESCAPED_SDK!> "DoTechUser\local.properties"
    echo   [UPDATED] DoTechUser\local.properties
)

if exist "DoctorAppUi\DoctorAppUi" (
    echo sdk.dir=!ESCAPED_SDK!> "DoctorAppUi\DoctorAppUi\local.properties"
    echo   [UPDATED] DoctorAppUi\DoctorAppUi\local.properties
)

if exist "DoTechUserProject" (
    echo sdk.dir=!ESCAPED_SDK!> "DoTechUserProject\local.properties"
    echo   [UPDATED] DoTechUserProject\local.properties
)

if exist "DoTech_1.0\DoctorAppUi" (
    echo sdk.dir=!ESCAPED_SDK!> "DoTech_1.0\DoctorAppUi\local.properties"
    echo   [UPDATED] DoTech_1.0\DoctorAppUi\local.properties
)

:: 4. Check ADB and Connected Devices
echo.
echo [4/4] Checking Android Debug Bridge (ADB) and Connected Devices...
set "ADB_PATH="
if exist "!DETECTED_SDK!\platform-tools\adb.exe" (
    set "ADB_PATH=!DETECTED_SDK!\platform-tools\adb.exe"
) else (
    where adb >nul 2>&1
    if !errorlevel! equ 0 set "ADB_PATH=adb"
)

if defined ADB_PATH (
    echo   [OK] ADB found: !ADB_PATH!
    echo   Scanning for connected devices or emulators:
    "!ADB_PATH!" devices
) else (
    echo   [NOTICE] ADB not found yet. It will be available once the SDK is installed via 'install_android_sdk.bat'.
)

echo.
echo ==============================================================================
echo Setup Complete!
echo ==============================================================================
echo.
pause
