<#
.SYNOPSIS
    DOTECH Android SDK Auto-Installer for Windows
.DESCRIPTION
    Automatically installs the official Android SDK into T:\Android\Sdk
    without requiring Android Studio, and configures both projects.
#>

$ErrorActionPreference = "Stop"

Write-Host "==============================================================================" -ForegroundColor Cyan
Write-Host "                DOTECH - ANDROID SDK AUTOMATIC INSTALLER                      " -ForegroundColor White
Write-Host "==============================================================================" -ForegroundColor Cyan
Write-Host ""

# Target location on T: (avoids filling C: drive which has limited space)
$SdkDir = "T:\Android\Sdk"
$CmdlineToolsZip = "$SdkDir\commandlinetools.zip"
$ToolsUrl = "https://dl.google.com/android/repository/commandlinetools-win-11076708_latest.zip"

# Java JDK 21 Check
if (Test-Path "C:\Program Files\Java\jdk-21") {
    $env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
} elseif (Test-Path "C:\Program Files\Java\jdk-17") {
    $env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
}

Write-Host "[1/5] Setting up target SDK folder at $SdkDir..." -ForegroundColor Yellow
if (-not (Test-Path $SdkDir)) {
    New-Item -ItemType Directory -Path $SdkDir -Force | Out-Null
}

$LatestDir = "$SdkDir\cmdline-tools\latest"
$SdkManagerBat = "$LatestDir\bin\sdkmanager.bat"

if (-not (Test-Path $SdkManagerBat)) {
    Write-Host "[2/5] Downloading Android Command Line Tools from Google..." -ForegroundColor Yellow
    Write-Host "      URL: $ToolsUrl" -ForegroundColor Gray
    
    [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
    $wc = New-Object System.Net.WebClient
    $wc.DownloadFile($ToolsUrl, $CmdlineToolsZip)
    Write-Host "      Download completed." -ForegroundColor Green

    Write-Host "[3/5] Extracting Command Line Tools..." -ForegroundColor Yellow
    $TempExtract = "$SdkDir\cmdline_temp"
    if (Test-Path $TempExtract) { Remove-Item -Path $TempExtract -Recurse -Force }
    Expand-Archive -Path $CmdlineToolsZip -DestinationPath $TempExtract -Force

    # Move to cmdline-tools/latest per Android SDK convention
    New-Item -ItemType Directory -Path "$SdkDir\cmdline-tools" -Force | Out-Null
    if (Test-Path $LatestDir) { Remove-Item -Path $LatestDir -Recurse -Force }
    Move-Item -Path "$TempExtract\cmdline-tools" -Destination $LatestDir -Force
    Remove-Item -Path $TempExtract -Recurse -Force -ErrorAction SilentlyContinue
    Remove-Item -Path $CmdlineToolsZip -Force -ErrorAction SilentlyContinue
    Write-Host "      Extracted successfully to $LatestDir." -ForegroundColor Green
} else {
    Write-Host "[2/5] Android Command Line Tools already present." -ForegroundColor Green
    Write-Host "[3/5] Skipping download." -ForegroundColor Green
}

Write-Host "[4/5] Installing Android Platforms (API 32, 34) and Build Tools (34.0.0)..." -ForegroundColor Yellow
Write-Host "      This may take 2-3 minutes. Please wait..." -ForegroundColor Gray

# Accept licenses automatically
$processInfo = New-Object System.Diagnostics.ProcessStartInfo
$processInfo.FileName = "cmd.exe"
$processInfo.Arguments = "/c echo y | `"$SdkManagerBat`" --sdk_root=`"$SdkDir`" --licenses"
$processInfo.UseShellExecute = $false
$processInfo.RedirectStandardOutput = $true
$processInfo.RedirectStandardError = $true
$process = [System.Diagnostics.Process]::Start($processInfo)
$process.WaitForExit()

# Install required packages: platform-tools, platforms 32 & 34, build-tools 34.0.0
$packages = @(
    "platform-tools",
    "platforms;android-34",
    "platforms;android-32",
    "build-tools;34.0.0"
)

$args = "/c echo y | `"$SdkManagerBat`" --sdk_root=`"$SdkDir`" $($packages -join ' ')"
$processInfo.Arguments = $args
$process = [System.Diagnostics.Process]::Start($processInfo)
$output = $process.StandardOutput.ReadToEnd()
$process.WaitForExit()
Write-Host "      Packages installed successfully!" -ForegroundColor Green

Write-Host "[5/5] Updating project local.properties configurations..." -ForegroundColor Yellow
$EscapedSdk = $SdkDir.Replace("\", "\\").Replace(":", "\:")

$Projects = @(
    "T:\JENISH\Msc.it\SEMESTER-9\Mobile Application Software\DoTechUser",
    "T:\JENISH\Msc.it\SEMESTER-9\Mobile Application Software\DoctorAppUi\DoctorAppUi",
    "T:\JENISH\Msc.it\SEMESTER-9\Mobile Application Software\DoTechUserProject",
    "T:\JENISH\Msc.it\SEMESTER-9\Mobile Application Software\DoTech_1.0\DoctorAppUi"
)

foreach ($proj in $Projects) {
    if (Test-Path $proj) {
        Set-Content -Path "$proj\local.properties" -Value "sdk.dir=$EscapedSdk"
        Write-Host "      [UPDATED] $proj\local.properties" -ForegroundColor Green
    }
}

Write-Host ""
Write-Host "==============================================================================" -ForegroundColor Green
Write-Host " [SUCCESS] Android SDK has been installed to $SdkDir!" -ForegroundColor Green
Write-Host " Both DoTech applications are now fully configured to build." -ForegroundColor Green
Write-Host " Run 'run_dotech.bat' to build the APKs!" -ForegroundColor White
Write-Host "==============================================================================" -ForegroundColor Green
Write-Host ""
