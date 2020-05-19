@echo off
Setlocal Enabledelayedexpansion

cd %~dp0
make
break>backEnd/pocket.txt
echo 30000> backend/wallet.txt
cd scripts
start buy_candies.bat|start buy_candies.bat|start buy_candies.bat|start buy_candies.bat|start buy_candies.bat|start buy_candies.bat|start buy_candies.bat|start buy_candies.bat|start buy_candies.bat|start buy_candies.bat|start buy_candies.bat|start buy_candies.bat|start buy_candies.bat|start buy_candies.bat
timeout /t 2 /nobreak > NUL
check_candies.bat