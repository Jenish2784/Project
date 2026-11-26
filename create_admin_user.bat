@echo off
title Create DoTech Admin User
cls
echo ==============================================================================
echo                      CREATE DOTECH ADMIN USER
echo ==============================================================================
echo This tool creates or updates an Admin account in Firebase Auth and Realtime DB.
echo Admins can log in using the Doctor ^& Admin App to manage doctors, hospitals,
echo approve new registrations, and view all system activity.
echo.

python "%~dp0create_admin.py"

echo.
echo ==============================================================================
echo Process complete.
echo ==============================================================================
pause
