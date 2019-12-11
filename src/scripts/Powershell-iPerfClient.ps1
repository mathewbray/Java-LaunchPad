#-- INITIAL PROMPTS

    #-- ENTER START IP
            $iPerfFilePath = Read-Host -Prompt 'Full path to iPerf '

#Variables
    $logPath = "$($env:USERPROFILE)\Desktop\IPERF-Tests\"
    $time = (Get-Date -Format yyy-mm-dd-Hmmss)

#--- LOG File locations
    #--- IF PATH DOES NOT EXIST, CREATE IT
    if(!(Test-Path -Path $logPath )){
        New-Item -Force -ItemType directory -Path $logPath
    }


#Script
Clear-Host

$ErrorActionPreference="SilentlyContinue"
Stop-Transcript | out-null
$ErrorActionPreference = "Continue"
Start-Transcript -path $logPath$time.txt -append -Verbose

cd $env:LAUNCHPADPATH\bin\iperf\
$iPerfFilePath -s -V -d

Stop-Transcript

if((Test-Path $logPath$time.txt )){
    ii $logPath$time.txt
}

Invoke-Item $logPath