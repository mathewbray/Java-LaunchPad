do {
    Clear-Host
    write-host ""
    write-host "                  Image to Base64! " -ForegroundColor Green
    write-host ""
    write-host ""
    $path = Read-Host -Prompt 'Source File'
    $destination = Read-Host -Prompt 'Destination File'
    $path = $path.Replace('"','')
    $destination = $destination.Replace('"','')

        
    write-host ""
        

    [String]$base64 = [convert]::ToBase64String((get-content $path -encoding byte))
    Write-Output ($base64) >> $destination 

} until ( $path -match "exit" )
