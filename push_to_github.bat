@echo off
setlocal
cd /d "%~dp0"

set "PATH=C:\Users\DELL\AppData\Local\GitHubDesktop\app-3.5.4\resources\app\git\cmd;C:\Users\DELL\AppData\Local\GitHubDesktop\app-3.5.4\resources\app\git\mingw64\bin;%PATH%"

echo ======================================================================
echo           Push DoTech Mobile Application Software to GitHub
echo ======================================================================
echo.
echo Target Repository: https://github.com/Jenish2784/Project.git
echo Local Branch:      main
echo.
echo Please choose how you want to authenticate:
echo [1] Browser Sign-In (Recommended - Git will open a browser login popup)
echo [2] GitHub Personal Access Token (PAT)
echo.
set /p auth_choice="Select option [1 or 2, default is 1]: "

if "%auth_choice%"=="2" (
    echo.
    set /p git_token="Paste your GitHub Personal Access Token: "
    if defined git_token (
        echo.
        echo Pushing to GitHub using Personal Access Token...
        git push https://%git_token%@github.com/Jenish2784/Project.git main
    ) else (
        echo [ERROR] No token entered! Aborting.
    )
) else (
    echo.
    echo Pushing to GitHub (Sign in on the browser popup if prompted)...
    git push -u origin main
)

if %errorlevel% equ 0 (
    echo.
    echo ======================================================================
    echo [SUCCESS] Project successfully pushed to:
    echo           https://github.com/Jenish2784/Project
    echo ======================================================================
) else (
    echo.
    echo [ERROR] Push failed. If authentication failed or the browser did not open:
    echo 1. Generate a Personal Access Token on GitHub:
    echo    https://github.com/settings/tokens (select 'repo' scope)
    echo 2. Run this script again and choose Option 2.
)

echo.
pause
