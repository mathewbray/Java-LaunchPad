clear-host
function Global:Find-MTU {

    [CmdletBinding(SupportsShouldProcess=$True,ConfirmImpact='Low')]
    Param(
        [parameter(Mandatory = $true, Position = 0)]
        [System.Net.IPAddress]$IPaddress
    )

    $Ping = New-Object System.Net.NetworkInformation.Ping
    $PingOptions = New-Object System.Net.NetworkInformation.PingOptions
    $PingOptions.DontFragment = $true

    [int]$Timeout = 1000
    [int]$SmallMTU=1
    [int]$LargeMTU=35840

    [byte[]]$databuffer = ,0xAC * $LargeMTU


    #action

    While (-not ($SmallMTU -eq ($LargeMTU - 1))) {
        [int]$xTest= ($LargeMTU - $SmallMTU) / 2 + $SmallMTU
        
        $PingReply = $Ping.Send($IPaddress, $Timeout, $($DataBuffer[1..$xTest]), $PingOptions)
        Write-Host "testing $($xTest + 28) byte transmission unit size" 
        if ($PingReply.Status -match "Success"){    
            $SmallMTU = $xTest
            Write-Host "Good" -ForegroundColor Green
        }
        else{
            $LargeMTU = $xTest
            Write-Host "Bad" -ForegroundColor Red
        }
        Start-Sleep -Milliseconds 50
    }
    
    If($SmallMTU -eq 1){
        Write-Error "The IP address $IPaddress does not respond." 
    }else{
        Write-Host ""
        $SmallMTU = $SmallMTU + 28
        Write-Host "Your Max MTU is...  $SmallMTU" -ForegroundColor Green
        Write-Host ""
        Write-Host "(Plz Note: 28 bytes were added to this number " -ForegroundColor Yellow
        Write-Host "because 20 bytes are reserved for the IP header " -ForegroundColor Yellow
        Write-Host "and 8 bytes must be allocated for the ICMP Echo " -ForegroundColor Yellow
        Write-Host "Request header.)" -ForegroundColor Yellow
        Write-Host ""
        #pause
    }
}

while($true)
{

#-- Get IP
    $ipaddress = Read-Host -Prompt 'Enter IP/Hostname '
    if($ipaddress -eq $null){ $ipaddress = "127.0.0.1" }
    if($ipaddress -eq ""){ $ipaddress = "127.0.0.1" }

Find-MTU -IPaddress $ipaddress -Verbose

}