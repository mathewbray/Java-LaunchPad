do {
        write-host ""
        write-host ""
        write-host "                  MD5 Checker! " -ForegroundColor Green
        write-host ""
        write-host ""
        write-host "      Drag file to this window and press enter! " 
        write-host ""
        $checkFile = read-host
        Clear-Host
        write-host ""
        
    
        #--- FCIV.exe
        #.\fciv.exe $checkFile
        
        #--- Powershell v3
        #$md5 = New-Object -TypeName System.Security.Cryptography.MD5CryptoServiceProvider
        #$hash = [System.BitConverter]::ToString($md5.ComputeHash([System.IO.File]::ReadAllBytes($checkFile)))
        #Write-Host $hash
        #Write-Host ""
        #pause
 
        #--- Powershell v4
        Get-FileHash $checkFile -Algorithm MD5
 
 
} until ( $checkFile -match "exit" )