@echo off

set launcher=%TMP%\_launcher.bat

if exist "%launcher%" (
	del /q /f "%launcher%"
)

echo.@echo off>>"%launcher%"
echo.ConsoleSize 200 20 200 9999>>"%launcher%"
echo.cd build\install\orchestrator\bin>>"%launcher%"
rem echo.set WEBSERVICE_OPTS=-Dspring.profiles.active=default,dev>>"%launcher%"
echo.call orchestrator.bat>>"%launcher%"

start cmd /c "%launcher%"
