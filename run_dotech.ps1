<#
.SYNOPSIS
    DOTECH Healthcare Suite - PowerShell Build & Run Manager
.DESCRIPTION
    Interactive PowerShell management script for building, installing, and running
    the DOTECH Patient App (DoTechUser) and Doctor/Admin App (DoctorAppUi).
#>

$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path

# 1. Determine Java Home (JDK 17 or 21 required)
if (Test-Path "C:\Program Files\Java\jdk-21") {
    $env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
} elseif (Test-Path "C:\Program Files\Java\jdk-17") {
    $env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
}

# 2. Determine Android SDK
$SdkPath = "T:\Android\Sdk"
if (-not (Test-Path $SdkPath) -and $env:ANDROID_HOME -and (Test-Path $env:ANDROID_HOME)) {
    $SdkPath = $env:ANDROID_HOME
} elseif (-not (Test-Path $SdkPath) -and (Test-Path "$env:LOCALAPPDATA\Android\Sdk")) {
    $SdkPath = "$env:LOCALAPPDATA\Android\Sdk"
}

# 3. Locate ADB
$AdbExe = $null
if (Test-Path "$SdkPath\platform-tools\adb.exe") {
    $AdbExe = "$SdkPath\platform-tools\adb.exe"
} else {
    $cmd = Get-Command adb -ErrorAction SilentlyContinue
    if ($cmd) { $AdbExe = "adb" }
}

function Show-Header {
    Clear-Host
    Write-Host "==============================================================================" -ForegroundColor Cyan
    Write-Host "                   DOTECH - HEALTHCARE APPLICATION SUITE                      " -ForegroundColor White
    Write-Host "==============================================================================" -ForegroundColor Cyan
    Write-Host " JAVA_HOME   : $env:JAVA_HOME" -ForegroundColor Gray
    if (Test-Path $SdkPath) {
        Write-Host " Android SDK : $SdkPath [INSTALLED]" -ForegroundColor Green
    } else {
        Write-Host " Android SDK : $SdkPath [NOT FOUND - Option S to install]" -ForegroundColor Yellow
    }
    if ($AdbExe) {
        Write-Host " ADB Status  : Available ($AdbExe)" -ForegroundColor Green
    } else {
        Write-Host " ADB Status  : Not Found (Install Android SDK or Platform Tools)" -ForegroundColor Yellow
    }
    Write-Host "==============================================================================" -ForegroundColor Cyan
    Write-Host ""
}

function Build-App($appPath, $appName) {
    if (-not (Test-Path $SdkPath)) {
        Write-Host "`n[ERROR] Android SDK not found at $SdkPath." -ForegroundColor Red
        Write-Host "Please select option [S] to install the Android SDK first.`n" -ForegroundColor Yellow
        return
    }
    Write-Host "`n>>> Building $appName..." -ForegroundColor Cyan
    Push-Location (Join-Path $ScriptDir $appPath)
    try {
        cmd /c "gradlew.bat assembleDebug"
        if ($LASTEXITCODE -eq 0) {
            Write-Host "`n[SUCCESS] $appName built successfully!" -ForegroundColor Green
        } else {
            Write-Host "`n[ERROR] Build failed for $appName (Exit Code: $LASTEXITCODE)" -ForegroundColor Red
        }
    } finally {
        Pop-Location
    }
}

function Run-App($appPath, $packageName, $activityName, $appName) {
    if (-not $AdbExe) {
        Write-Host "`n[ERROR] ADB is required to install and launch apps on an Android device or emulator." -ForegroundColor Red
        return
    }
    
    $apkPath = Join-Path $ScriptDir "$appPath\app\build\outputs\apk\debug\app-debug.apk"
    if (-not (Test-Path $apkPath)) {
        Write-Host "`nAPK not found. Building debug APK first..." -ForegroundColor Yellow
        Build-App $appPath $appName
    }

    if (Test-Path $apkPath) {
        Write-Host "`n>>> Installing $appName to device..." -ForegroundColor Cyan
        & $AdbExe install -r $apkPath
        Write-Host ">>> Launching $activityName..." -ForegroundColor Cyan
        & $AdbExe shell am start -n "$packageName/$activityName"
        Write-Host "`n[OK] $appName launched successfully!" -ForegroundColor Green
    } else {
        Write-Host "`n[ERROR] Could not find or build APK." -ForegroundColor Red
    }
}

do {
    Show-Header
    Write-Host " --- SETUP SDK ---" -ForegroundColor Yellow
    Write-Host " [S] Install Android SDK automatically to T:\Android\Sdk"
    Write-Host ""
    Write-Host " --- BUILD OPTIONS ---" -ForegroundColor Yellow
    Write-Host " [1] Build Patient App APK (DoTechUser)"
    Write-Host " [2] Build Doctor & Admin App APK (DoctorAppUi)"
    Write-Host " [3] Build Both Applications"
    Write-Host ""
    Write-Host " --- RUN / DEPLOY ---" -ForegroundColor Yellow
    Write-Host " [4] Install & Launch Patient App on Connected Device"
    Write-Host " [5] Install & Launch Doctor App on Connected Device"
    Write-Host ""
    Write-Host " --- MAINTENANCE ---" -ForegroundColor Yellow
    Write-Host " [6] Clean Build Caches (gradlew clean)"
    Write-Host " [7] Run Environment Setup & Repair local.properties"
    Write-Host " [0] Exit"
    Write-Host ""
    
    $choice = Read-Host "Select an option [0-7 or S]"

    switch -Regex ($choice) {
        "(?i)^s$" {
            & (Join-Path $ScriptDir "install_android_sdk.ps1")
            $global:SdkPath = "T:\Android\Sdk"
            if (Test-Path "$SdkPath\platform-tools\adb.exe") {
                $global:AdbExe = "$SdkPath\platform-tools\adb.exe"
            }
            Read-Host "`nPress Enter to return to menu..."
        }
        "^1$" {
            Build-App "DoTechUser" "Patient App"
            Read-Host "`nPress Enter to return to menu..."
        }
        "^2$" {
            Build-App "DoctorAppUi\DoctorAppUi" "Doctor & Admin App"
            Read-Host "`nPress Enter to return to menu..."
        }
        "^3$" {
            Build-App "DoTechUser" "Patient App"
            Build-App "DoctorAppUi\DoctorAppUi" "Doctor & Admin App"
            Read-Host "`nPress Enter to return to menu..."
        }
        "^4$" {
            Run-App "DoTechUser" "com.example.dotechuser" ".SplashScreenActivity" "Patient App"
            Read-Host "`nPress Enter to return to menu..."
        }
        "^5$" {
            Run-App "DoctorAppUi\DoctorAppUi" "com.example.doctorappui" ".SplashScreenActivity" "Doctor & Admin App"
            Read-Host "`nPress Enter to return to menu..."
        }
        "^6$" {
            Write-Host "`nCleaning caches..." -ForegroundColor Cyan
            Push-Location (Join-Path $ScriptDir "DoTechUser")
            cmd /c "gradlew.bat clean"
            Pop-Location
            Push-Location (Join-Path $ScriptDir "DoctorAppUi\DoctorAppUi")
            cmd /c "gradlew.bat clean"
            Pop-Location
            Write-Host "`n[OK] Clean complete!" -ForegroundColor Green
            Read-Host "`nPress Enter to return to menu..."
        }
        "^7$" {
            cmd /c "$ScriptDir\setup_environment.bat"
        }
        "^0$" {
            Write-Host "`nGoodbye!" -ForegroundColor Green
            break
        }
        default {
            Write-Host "`nInvalid selection." -ForegroundColor Red
            Start-Sleep -Seconds 1
        }
    }
} while ($choice -ne "0")
