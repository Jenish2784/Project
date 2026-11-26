@echo off
set "JAVA_HOME=C:\Program Files\Java\jdk-21"
set "SDK_PATH=T:\Android\Sdk"

echo [1/2] Building DoTechUser...
cd /d "T:\JENISH\Msc.it\SEMESTER-9\Mobile Application Software\DoTechUser"
call gradlew.bat assembleDebug
if %errorlevel% neq 0 (
    echo [ERROR] DoTechUser build failed!
    exit /b %errorlevel%
)

echo.
echo [2/2] Building DoctorAppUi...
cd /d "T:\JENISH\Msc.it\SEMESTER-9\Mobile Application Software\DoctorAppUi\DoctorAppUi"
call gradlew.bat assembleDebug
if %errorlevel% neq 0 (
    echo [ERROR] DoctorAppUi build failed!
    exit /b %errorlevel%
)

cd /d "T:\JENISH\Msc.it\SEMESTER-9\Mobile Application Software"
copy /y "DoTechUser\app\build\outputs\apk\debug\app-debug.apk" "DoTech_Patient_App.apk"
copy /y "DoctorAppUi\DoctorAppUi\app\build\outputs\apk\debug\app-debug.apk" "DoTech_Doctor_Admin_App.apk"

echo.
echo [SUCCESS] Both applications built and copied successfully!
