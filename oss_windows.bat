@echo off

:: One Stop Shop, The Feckless Weasel Deployment System for Windows
:: By: Christian Gunderman

:: This script exists to bypass the system execution policy banning
:: powershell scripts.
powershell.exe -ExecutionPolicy Bypass -Command "etc\oss_windows.ps1" %*
