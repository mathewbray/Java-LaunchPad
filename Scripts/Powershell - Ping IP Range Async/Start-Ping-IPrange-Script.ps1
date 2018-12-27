.\Ping-IPrange.ps1

Remove-Item .\pingoutput.csv
Remove-Item .\pingoutput_sorted.csv
Remove-Item .\pingoutput-before-sort.csv

cls

#-- INITIAL PROMPTS

#-- ENTER START IP
        $startip = Read-Host -Prompt 'Enter the starting IP '
#-- ENTER END IP
        $endip = Read-Host -Prompt '  Enter the ending IP '
#-- ENTER INTERVAL BETWEEN PINGS - 10 default
        $interval = Read-Host -Prompt 'Enter ping interval (Default 10ms): '
        if($interval -eq $null){ $interval = 10 }
        if($interval -eq ""){ $interval = 10 }
#-- CHOOSE TO RESOLVE HOSTNAME
        $title = ""
        $message = "Resolve hostnames (DNS)?"
        $yes = New-Object System.Management.Automation.Host.ChoiceDescription "&Yes", `
            "Resolves hostnames using DNS."
        $no = New-Object System.Management.Automation.Host.ChoiceDescription "&No", `
            "Does NOT resolve hostnames using DNS."
        $options = [System.Management.Automation.Host.ChoiceDescription[]]($yes, $no)
        $result = $host.ui.PromptForChoice($title, $message, $options, 0) 


#-- START SCRIPT
switch ($result)
    {
        0 {
            Ping-IPRange -StartAddress $startip -EndAddress $endip -Interval $interval | 
            select IPAddress,Bytes,Ttl,ResponseTime,@{LABEL="HostName"; EXPRESSION={[System.Net.Dns]::GetHostByAddress($_.IPaddress).Hostname}} | 
            Export-Csv .\pingoutput.csv -NoTypeInformation -Force
        }

        1 {
            Ping-IPRange -StartAddress $startip -EndAddress $endip -Interval $interval | 
            Export-Csv .\pingoutput.csv -NoTypeInformation -Force
        }
    }


Write-Host ""
Write-Host ""
Write-Host ""
Write-Host ""
Write-Host ""
Write-Host ""
Write-Host ""
Write-Host ""
Write-Host ""
Write-Host ""
Write-host "Sorting IPs..."

Copy-Item .\pingoutput.csv .\pingoutput-before-sort.csv -Force

Import-CSV .\pingoutput.csv | Sort-Object {"{0:d3}.{1:d3}.{2:d3}.{3:d3}" -f @([int[]]$_.IPAddress.split('.'))} | Export-Csv .\pingoutput_sorted.csv -NoTypeInformation -Force

Invoke-Item .\pingoutput_sorted.csv

pause
