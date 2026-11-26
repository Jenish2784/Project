@echo off
setlocal EnableDelayedExpansion

title DOTECH - Build and Run Manager
color 0A

set "SCRIPT_DIR=%~dp0"
cd /d "%SCRIPT_DIR%"

:: Set Java 21 to meet AGP 8.x requirements
if exist "C:\Program Files\Java\jdk-21" (
    set "JAVA_HOME=C:\Program Files\Java\jdk-21"
) else if exist "C:\Program Files\Java\jdk-17" (
    set "JAVA_HOME=C:\Program Files\Java\jdk-17"
)

:: Locate SDK and ADB
set "SDK_PATH="
if exist "T:\Android\Sdk" set "SDK_PATH=T:\Android\Sdk"
if not defined SDK_PATH if defined ANDROID_HOME (
    if exist "%ANDROID_HOME%" set "SDK_PATH=%ANDROID_HOME%"
)
if not defined SDK_PATH if exist "%LOCALAPPDATA%\Android\Sdk" (
    set "SDK_PATH=%LOCALAPPDATA%\Android\Sdk"
)
if not defined SDK_PATH (
    set "SDK_PATH=T:\Android\Sdk"
)

set "ADB_EXE="
if exist "%SDK_PATH%\platform-tools\adb.exe" (
    set "ADB_EXE=%SDK_PATH%\platform-tools\adb.exe"
) else (
    where adb >nul 2>&1
    if !errorlevel! equ 0 set "ADB_EXE=adb"
)

:MENU
cls
echo ==============================================================================
echo                      DOTECH - HEALTHCARE APPLICATION SUITE                    
echo ==============================================================================
echo  Java Home    : %JAVA_HOME%
if exist "%SDK_PATH%" (
    echo  Android SDK  : %SDK_PATH% [INSTALLED]
) else (
    echo  Android SDK  : %SDK_PATH% [NOT FOUND - Select option S to install]
)
if defined ADB_EXE (
    echo  ADB Status   : Available [%ADB_EXE%]
) else (
    echo  ADB Status   : Not Found - install Android SDK or Platform Tools
)
echo ==============================================================================
echo.
echo  --- SETUP SDK (REQUIRED ONCE) ---
echo  [S] Install Android SDK automatically to T:\Android\Sdk
echo.
echo  --- BUILD OPTIONS ---
echo  [1] Build Patient App APK (DoTechUser)
echo  [2] Build Doctor and Admin App APK (DoctorAppUi)
echo  [3] Build Both Apps (Patient + Doctor)
echo.
echo  --- RUN / DEPLOY TO DEVICE / EMULATOR ---
echo  [4] Install and Launch Patient App (com.example.dotechuser)
echo  [5] Install and Launch Doctor App (com.example.doctorappui)
echo  [A] Open Folder with Built APK files
echo.
echo  --- ANDROID STUDIO ---
echo  [6] Open Patient App in Android Studio
echo  [7] Open Doctor App in Android Studio
echo.
echo  --- MAINTENANCE ---
echo  [8] Clean Gradle Build Caches
echo  [9] Run Environment Setup and Repair local.properties
echo  [0] Exit
echo.
echo ==============================================================================
set /p choice="Please select an option [0-9, S, or A]: "

if /i "%choice%"=="s" goto INSTALL_SDK
if /i "%choice%"=="a" goto OPEN_APKS
if "%choice%"=="1" goto BUILD_USER
if "%choice%"=="2" goto BUILD_DOCTOR
if "%choice%"=="3" goto BUILD_BOTH
if "%choice%"=="4" goto RUN_USER
if "%choice%"=="5" goto RUN_DOCTOR
if "%choice%"=="6" goto AS_USER
if "%choice%"=="7" goto AS_DOCTOR
if "%choice%"=="8" goto CLEAN_ALL
if "%choice%"=="9" goto SETUP_ENV
if "%choice%"=="0" goto EXIT_SCRIPT

echo Invalid option, please try again.
timeout /t 2 >nul
goto MENU

:INSTALL_SDK
cls
call "%SCRIPT_DIR%install_android_sdk.bat"
goto MENU

:CHECK_SDK
if not exist "%SDK_PATH%" (
    echo.
    echo ==============================================================================
    echo [ERROR] Android SDK not found at '%SDK_PATH%'!
    echo To build APKs, the Android SDK must be installed.
    echo Press 'Y' to install it automatically now to T:\Android\Sdk, or 'N' to cancel.
    echo ==============================================================================
    set /p inst_choice="Install SDK now? [Y/N]: "
    if /i "!inst_choice!"=="y" (
        call "%SCRIPT_DIR%install_android_sdk.bat"
        set "SDK_PATH=T:\Android\Sdk"
    )
)
goto :eof

:BUILD_USER
cls
call :CHECK_SDK
echo ==============================================================================
echo                  BUILDING PATIENT APP (DoTechUser)                            
echo ==============================================================================
cd /d "%SCRIPT_DIR%DoTechUser"
call gradlew.bat assembleDebug
if %errorlevel% equ 0 (
    echo.
    echo [SUCCESS] Patient APK built successfully!
    echo APK Location: %SCRIPT_DIR%DoTechUser\app\build\outputs\apk\debug\app-debug.apk
) else (
    echo.
    echo [ERROR] Build failed. Please verify that Android SDK is installed.
)
cd /d "%SCRIPT_DIR%"
pause
goto MENU

:BUILD_DOCTOR
cls
call :CHECK_SDK
echo ==============================================================================
echo                BUILDING DOCTOR AND ADMIN APP (DoctorAppUi)                      
echo ==============================================================================
cd /d "%SCRIPT_DIR%DoctorAppUi\DoctorAppUi"
call gradlew.bat assembleDebug
if %errorlevel% equ 0 (
    echo.
    echo [SUCCESS] Doctor and Admin APK built successfully!
    echo APK Location: %SCRIPT_DIR%DoctorAppUi\DoctorAppUi\app\build\outputs\apk\debug\app-debug.apk
) else (
    echo.
    echo [ERROR] Build failed. Please verify that Android SDK is installed.
)
cd /d "%SCRIPT_DIR%"
pause
goto MENU

:BUILD_BOTH
cls
call :CHECK_SDK
echo ==============================================================================
echo                    BUILDING BOTH APPLICATIONS                                 
echo ==============================================================================
echo [1/2] Building Patient App...
cd /d "%SCRIPT_DIR%DoTechUser"
call gradlew.bat assembleDebug
set /a res1=%errorlevel%

echo.
echo [2/2] Building Doctor and Admin App...
cd /d "%SCRIPT_DIR%DoctorAppUi\DoctorAppUi"
call gradlew.bat assembleDebug
set /a res2=%errorlevel%

cd /d "%SCRIPT_DIR%"
echo.
echo ==============================================================================
if %res1% equ 0 (
    echo [OK] Patient App APK: %SCRIPT_DIR%DoTechUser\app\build\outputs\apk\debug\app-debug.apk
) else (
    echo [FAILED] Patient App build failed.
)

if %res2% equ 0 (
    echo [OK] Doctor App APK: %SCRIPT_DIR%DoctorAppUi\DoctorAppUi\app\build\outputs\apk\debug\app-debug.apk
) else (
    echo [FAILED] Doctor App build failed.
)
echo ==============================================================================
pause
goto MENU

:RUN_USER
cls
echo ==============================================================================
echo               INSTALL AND LAUNCH PATIENT APP (DoTechUser)                       
echo ==============================================================================
if not defined ADB_EXE (
    echo [ERROR] ADB not found. Please run option [S] to install Android SDK.
    pause
    goto MENU
)

cd /d "%SCRIPT_DIR%DoTechUser"
set "USER_APK=%SCRIPT_DIR%DoTechUser\app\build\outputs\apk\debug\app-debug.apk"
if not exist "%USER_APK%" (
    echo APK not found. Building debug APK first...
    call gradlew.bat assembleDebug
)

if not exist "%USER_APK%" (
    echo [ERROR] APK could not be built.
    cd /d "%SCRIPT_DIR%"
    pause
    goto MENU
)

:CHECK_DEVICE_USER
echo Checking for connected Android phone or emulator...
"%ADB_EXE%" get-state >nul 2>&1
if %errorlevel% neq 0 goto NO_DEVICE_USER
goto DEVICE_USER_FOUND

:NO_DEVICE_USER
echo.
echo ==============================================================================
echo [NO DEVICE DETECTED]
echo No Android phone or laptop emulator is currently connected.
echo.
echo To run the application:
echo  1. Connect your Android phone via USB and enable "USB Debugging", OR
echo  2. Start an Android emulator (BlueStacks, LDPlayer, or Android Studio AVD), OR
echo  3. Manually copy or drag-and-drop the APK file to your phone/emulator:
echo     %USER_APK%
echo ==============================================================================
echo.
echo  [R] Retry device detection
echo  [O] Open APK folder in File Explorer
echo  [M] Back to Main Menu
echo.
set /p dev_choice="Select [R/O/M]: "
if /i "!dev_choice!"=="r" goto CHECK_DEVICE_USER
if /i "!dev_choice!"=="o" (
    explorer /select,"%USER_APK%"
    goto CHECK_DEVICE_USER
)
goto MENU

:DEVICE_USER_FOUND
echo [OK] Device detected! Installing Patient App...
"%ADB_EXE%" install -r "%USER_APK%"
if %errorlevel% neq 0 (
    echo Retrying with clean install...
    "%ADB_EXE%" uninstall com.example.dotechuser >nul 2>&1
    "%ADB_EXE%" install -r "%USER_APK%"
)
echo Launching SplashScreenActivity...
"%ADB_EXE%" shell am start -n com.example.dotechuser/.SplashScreenActivity
echo [SUCCESS] Patient App launched on device!
cd /d "%SCRIPT_DIR%"
pause
goto MENU

:RUN_DOCTOR
cls
echo ==============================================================================
echo             INSTALL AND LAUNCH DOCTOR APP (DoctorAppUi)                         
echo ==============================================================================
if not defined ADB_EXE (
    echo [ERROR] ADB not found. Please run option [S] to install Android SDK.
    pause
    goto MENU
)

cd /d "%SCRIPT_DIR%DoctorAppUi\DoctorAppUi"
set "DOCTOR_APK=%SCRIPT_DIR%DoctorAppUi\DoctorAppUi\app\build\outputs\apk\debug\app-debug.apk"
if not exist "%DOCTOR_APK%" (
    echo APK not found. Building debug APK first...
    call gradlew.bat assembleDebug
)

if not exist "%DOCTOR_APK%" (
    echo [ERROR] APK could not be built.
    cd /d "%SCRIPT_DIR%"
    pause
    goto MENU
)

:CHECK_DEVICE_DOCTOR
echo Checking for connected Android phone or emulator...
"%ADB_EXE%" get-state >nul 2>&1
if %errorlevel% neq 0 goto NO_DEVICE_DOCTOR
goto DEVICE_DOCTOR_FOUND

:NO_DEVICE_DOCTOR
echo.
echo ==============================================================================
echo [NO DEVICE DETECTED]
echo No Android phone or laptop emulator is currently connected.
echo.
echo To run the application:
echo  1. Connect your Android phone via USB and enable "USB Debugging", OR
echo  2. Start an Android emulator (BlueStacks, LDPlayer, or Android Studio AVD), OR
echo  3. Manually copy or drag-and-drop the APK file to your phone/emulator:
echo     %DOCTOR_APK%
echo ==============================================================================
echo.
echo  [R] Retry device detection
echo  [O] Open APK folder in File Explorer
echo  [M] Back to Main Menu
echo.
set /p dev_choice="Select [R/O/M]: "
if /i "!dev_choice!"=="r" goto CHECK_DEVICE_DOCTOR
if /i "!dev_choice!"=="o" (
    explorer /select,"%DOCTOR_APK%"
    goto CHECK_DEVICE_DOCTOR
)
goto MENU

:DEVICE_DOCTOR_FOUND
echo [OK] Device detected! Installing Doctor and Admin App...
"%ADB_EXE%" install -r "%DOCTOR_APK%"
if %errorlevel% neq 0 (
    echo Retrying with clean install...
    "%ADB_EXE%" uninstall com.example.doctorappui >nul 2>&1
    "%ADB_EXE%" install -r "%DOCTOR_APK%"
)
echo Launching SplashScreenActivity...
"%ADB_EXE%" shell am start -n com.example.doctorappui/.SplashScreenActivity
echo [SUCCESS] Doctor App launched on device!
cd /d "%SCRIPT_DIR%"
pause
goto MENU

:OPEN_APKS
cls
echo Opening folders with APK files...
set "U_APK=%SCRIPT_DIR%DoTechUser\app\build\outputs\apk\debug\app-debug.apk"
if exist "%U_APK%" (
    explorer /select,"%U_APK%"
) else (
    explorer "%SCRIPT_DIR%DoTechUser"
)
goto MENU

:AS_USER
cls
echo Opening Patient App in Android Studio...
start "" "%SCRIPT_DIR%DoTechUser"
echo Opened folder. If Android Studio is associated, you can open project from file dialog.
timeout /t 2 >nul
goto MENU

:AS_DOCTOR
cls
echo Opening Doctor App in Android Studio...
start "" "%SCRIPT_DIR%DoctorAppUi\DoctorAppUi"
echo Opened folder. If Android Studio is associated, you can open project from file dialog.
timeout /t 2 >nul
goto MENU

:CLEAN_ALL
cls
echo ==============================================================================
echo                     CLEANING GRADLE BUILD CACHES                              
echo ==============================================================================
echo [1/2] Cleaning DoTechUser...
cd /d "%SCRIPT_DIR%DoTechUser"
call gradlew.bat clean

echo.
echo [2/2] Cleaning DoctorAppUi...
cd /d "%SCRIPT_DIR%DoctorAppUi\DoctorAppUi"
call gradlew.bat clean

cd /d "%SCRIPT_DIR%"
echo.
echo [OK] Clean completed.
pause
goto MENU

:SETUP_ENV
call "%SCRIPT_DIR%setup_environment.bat"
goto MENU

:EXIT_SCRIPT
cls
echo Exiting DOTECH manager. Have a great day!
exit /b 0
