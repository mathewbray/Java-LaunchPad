
clear-host
do {
        write-host ""
        write-host ""
        write-host "                  Hash Checker! " -ForegroundColor Green
        write-host ""
        write-host ""
        write-host "      Drag file to this window and press enter! " 
        write-host ""
        $checkFile = read-host
        Clear-Host
        write-host ""        
        $checkFile = $checkFile.Replace('"','')
        Write-Host $checkFile

        #--- Powershell v3
        #$md5 = New-Object -TypeName System.Security.Cryptography.MD5CryptoServiceProvider
        #$hash = [System.BitConverter]::ToString($md5.ComputeHash([System.IO.File]::ReadAllBytes($checkFile)))
        #Write-Host $hash
        #Write-Host ""
        #pause

    $Info = "Do you want Response Time (for graphing) or simple UP/DOWN output???"
    $options = [System.Management.Automation.Host.ChoiceDescription[]] @("&MD5", "SHA&1", "SHA&256", "SHA&384", "SHA&512", "3&DES")
    [int]$defaultchoice = 1
    $opt =  $host.UI.PromptForChoice($Title , $Info , $Options,$defaultchoice)

    switch($opt)
    {
        0 { Get-FileHash $checkFile -Algorithm MD5 | Format-List }
        1 { Get-FileHash $checkFile -Algorithm SHA1 | Format-List }
        2 { Get-FileHash $checkFile -Algorithm SHA256 | Format-List }
        3 { Get-FileHash $checkFile -Algorithm SHA384 | Format-List }
        4 { Get-FileHash $checkFile -Algorithm SHA512 | Format-List}
        5 { Get-FileHash $checkFile -Algorithm MACTripleDES | Format-List }

    }


} until ( $checkFile -match "exit" )