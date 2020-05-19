@echo off
Setlocal Enabledelayedexpansion

cd ../backEnd

set /p texte=< wallet.txt

echo %texte%

REM we bought 14 candies so our 30000$ account should be deduced from 14$ (30000 - 14 = 29986)
if %texte% neq 29986 (
    echo Data race error.. :(
) else (
    echo No data race error :D !
)
