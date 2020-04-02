@echo off
Setlocal Enabledelayedexpansion

cd ../backEnd

Set _File=pocket.txt
Set /a _Lines=0
For /f %%j in ('Find "" /v /c ^< %_File%') Do Set /a _Lines=%%j

if %_Lines% equ 2 (
    echo Data race error : you bought two cars for the price of one.. :(
) else (
    echo No data race error :D !
)







