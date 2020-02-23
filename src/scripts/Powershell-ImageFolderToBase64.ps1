do {
    #Clear-Host
    write-host ""
    write-host "                  Image Folder to Base64! " -ForegroundColor Green
    write-host ""
    write-host ""
    $path = Read-Host -Prompt 'Source Folder'
    $destination = Read-Host -Prompt 'Destination File'
    $path = $path.Replace('"','')
    $destination = $destination.Replace('"','')

    Get-ChildItem $path | 
    Foreach-Object {
        $fullname = $_.FullName
        $filename = $_.Name

        Write-Host $fullname
        Write-Host $filename

        [String]$base64 = [convert]::ToBase64String((get-content $fullname -encoding byte))
        #Write-Output ($base64) | Out-File $destination -Append

        #-- For LaunchPad PS Powershell variables
        Write-Output ("`$base64" + $filename.Replace(".png","") + " = `"" + $base64 + "`"") | Out-File $destination -Append

    }
        
    write-host ""
    pause

} until ( $path -match "exit" )
