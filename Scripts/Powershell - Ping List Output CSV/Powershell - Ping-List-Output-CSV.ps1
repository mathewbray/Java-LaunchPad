$DefaultValue = "../../SessionList.csv"

$IPlist = Read-Host "Enter CSV Path [../../SessionList.csv]"
        if($IPlist -eq $null){ $IPlist = $DefaultValue }
        if($IPlist -eq ""){ $IPlist = $DefaultValue }

$IPlistData = Import-Csv $IPlist -Header HOSTNAME,IP

if (!(Test-Path ".\IP-DEAD-or-ALIVE-List.csv"))
{
   New-Item -name "IP-Alive-List.txt" -type "file" -Force
   New-Item -name "IP-DEAD-List.txt" -type "file" -Force
   New-Item -name "IP-DEAD-or-ALIVE-List.csv" -type "file" -Force
}
else
{
Clear-Content -Path ".\IP-Alive-List.txt" -Force
Clear-Content -Path ".\IP-DEAD-List.txt" -Force
Clear-Content -Path ".\IP-DEAD-or-ALIVE-List.csv" -Force
}



ForEach ($Item in $IPlistData) {
 if($Item.IP) {
    if (Test-Connection $Item.IP -Count 1 -ea 0 -quiet)
        { 
        Write-Host $Item.IP - $Item.HOSTNAME "is up!  Writing to IP-Alive-List.txt..." -ForegroundColor Green
        $Item.IP | Out-File -FilePath .\IP-Alive-List.txt -Append -Force
        $ItemString = $Item.IP
        $ItemHostnameString = $Item.HOSTNAME
        "Alive`t$ItemString`t$ItemHostnameString" | Out-File -FilePath .\IP-DEAD-or-ALIVE-List.csv -Append -Force
        } 
    else { 
        Write-Host $Item.IP - $Item.HOSTNAME "is down!  NOT writing to IP-Alive-List.txt" -ForegroundColor Red
        $Item.IP | Out-File -FilePath .\IP-DEAD-List.txt -Append -Force
        $ItemString = $Item.IP
        $ItemHostnameString = $Item.HOSTNAME
        "DEAD`t$ItemString`t$ItemHostnameString" | Out-File -FilePath .\IP-DEAD-or-ALIVE-List.csv -Append -Force
        }
    }

}

Invoke-Item .\IP-Alive-List.txt
Invoke-Item .\IP-DEAD-List.txt
Invoke-Item .\IP-DEAD-or-ALIVE-List.csv