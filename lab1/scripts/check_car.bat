@echo off
Setlocal Enabledelayedexpansion

cd ../backEnd

Set _File=pocket.txt
Set /a _Lines=0
For /f %%j in ('Find "" /v /c ^< %_File%') Do Set /a _Lines=%%j

REM we use a number of lines of 2 to detect and error because you cannot buy 2 cars with 30000$, so only data race error would allow such comportment
if %_Lines% equ 2 (
    echo Data race error.. :(
) else (
    echo No data race error :D !
)
