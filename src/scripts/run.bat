@echo off
Setlocal Enabledelayedexpansion

cd ..
make
break>backEnd/pocket.txt
echo 30000> backend/wallet.txt
cd scripts
start "Thread 1" buy_car.bat|start "Thread 2" buy_car.bat
REM start buy_candies.bat|start buy_candies.bat|start buy_candies.bat|start buy_candies.bat|start buy_candies.bat|start buy_candies.bat|start buy_candies.bat|start buy_candies.bat|start buy_candies.bat|start buy_candies.bat|start buy_candies.bat|start buy_candies.bat|start buy_candies.bat|start buy_candies.bat
timeout /t 2 /nobreak > NUL
coherence_analysis.bat
