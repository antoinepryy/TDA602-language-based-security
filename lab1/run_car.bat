@echo off
Setlocal Enabledelayedexpansion

cd %~dp0
make
break>backEnd/pocket.txt
echo 30000> backend/wallet.txt
cd scripts
start "Thread 1" buy_car.bat|start "Thread 2" buy_car.bat
timeout /t 2 /nobreak > NUL
check_car.bat