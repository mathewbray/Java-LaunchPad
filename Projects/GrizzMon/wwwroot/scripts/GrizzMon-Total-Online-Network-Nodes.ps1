$pshost = get-host
$pswindow = $pshost.ui.rawui
$pswindow.WindowTitle = "Total Online - Network Nodes"

while($true) {

#--- SET START TIME
$stopWatch = [system.diagnostics.stopwatch]::startNew()
$stopWatch.Start()

$asdf = Get-Content "..\gen\icmp-network-nodes.htm"
$online = ($asdf | Select-String -pattern "online" -AllMatches).matches.count
$offline = ($asdf | Select-String -pattern "offline" -AllMatches).matches.count

#Variables
$CSS = get-content "..\css\theme.css"
$Dated = (Get-Date -format F)
$generatedFile = "..\gen\total-online-network-nodes.htm "
$publishedFile = "..\gen-pub\total-online-network-nodes.htm "

#HTML Head
$HTMLHead = @"
<!DOCTYPE html>
<HEAD>
<META charset="UTF-8">
<CENTER>
<STYLE>
$CSS 
.fonttable, .fonttable TD, .fonttable TH
{
font-size:14pt;
padding:1px;
text-align:center;
}
</STYLE>
</HEAD>
"@

#HTML Body Content
$HTMLBody = @"
<CENTER>
<meta http-equiv="refresh" content="60">
<TABLE   class="fonttable" style="vertical-align:top; margin-left: 0px; margin-top: -8px;  margin-bottom: -5px; width: 90%;"  BORDER=0>
<tr><th colspan="2" style="font-size:9pt">Network Nodes</th></tr>
</TR><TD bgcolor=#7CBB59>$online</TD> <TD bgcolor=#EB2300 >$offline</TD></TR>
</TABLE>
</CENTER>
"@

#Export to HTML
Write-Host "Creating HTML file..." -ForegroundColor Yellow
ConvertTo-HTML -head $HTMLHead -body $HTMLBody | out-file $generatedFile

#--- PUBLISH THE HTML FILE
Copy-Item $generatedFile $publishedFile -Force

#--- DISPLAY TIME THE SCRIPT TOOK TO RUN
$stopWatch.Stop()
Write-Host "Elapsed Runtime:" $stopWatch.Elapsed.Minutes "minutes and" $stopWatch.Elapsed.Seconds "seconds." -ForegroundColor Cyan

#--- SLEEP 60 SECONDS
Write-Host "Sleeping 60 seconds..." -ForegroundColor Yellow
Start-Sleep 60

}