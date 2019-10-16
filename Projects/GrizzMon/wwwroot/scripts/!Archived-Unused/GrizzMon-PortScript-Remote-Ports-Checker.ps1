$pshost = get-host
$pswindow = $pshost.ui.rawui$pswindow.WindowTitle = "Remote-Ports-Checker"

while($true) {

#--- SET START TIME$stopWatch = [system.diagnostics.stopwatch]::startNew()
$stopWatch.Start()

#Variables$CSS = get-content "C:\inetpub\wwwroot\css\theme.css"$Dated = (Get-Date -format F)$generatedFile = "C:\inetpub\wwwroot\gen\internetchecker.htm"$publishedFile = "C:\inetpub\wwwroot\gen-pub\internetchecker.htm"
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
font-size:10pt;
padding:1px;
}</STYLE></HEAD>
"@
#  Get content from config files$hostida = (get-content  "C:\inetpub\wwwroot\cfg\porthostsa.cfg" | Where {-not ($_.StartsWith('#'))})$portnumbera = (get-content "C:\inetpub\wwwroot\cfg\portcfga.cfg" | Where {-not ($_.StartsWith('#'))})$hostidb = (get-content  "C:\inetpub\wwwroot\cfg\porthostsb.cfg" | Where {-not ($_.StartsWith('#'))})$portnumberb = (get-content "C:\inetpub\wwwroot\cfg\portcfgb.cfg" | Where {-not ($_.StartsWith('#'))})$hostidc = (get-content  "C:\inetpub\wwwroot\cfg\porthostsb.cfg" | Where {-not ($_.StartsWith('#'))})$portnumberc = (get-content "C:\inetpub\wwwroot\cfg\portcfgb.cfg" | Where {-not ($_.StartsWith('#'))})
#Check status of ports single port A$socketa = new-object Net.Sockets.TcpClient$socketresultsa = $socketa.Connect($hostida, $portnumbera)if ($socketa.Connected) {$statusa = “Open”$cellcolor = "7CBB59"$socketa.Close()Write-Host "$hostida port $portnumbera Open" -ForegroundColor Green}else {$statusa = “Closed”$cellcolor = "EB2300"Write-Host "$hostida port $portnumbera Closed" -ForegroundColor Red}
#HTML Body Content
$HTMLBody = @"<CENTER><meta http-equiv="refresh" content="30"><TABLE   class="fonttable" style="vertical-align:top; margin-left: 0px; margin-top: -8px;  margin-bottom: -5px;"  BORDER=0><TR bgcolor=#bcbcab></TR><TR bgcolor=#$cellcolor><TD width="115">$hostida</TD> <TD width="30">$portnumbera</TD> <TD width="60">$statusa</TD></TR></TABLE></CENTER>"@
#Check status of ports single port B$socketb = new-object Net.Sockets.TcpClient$socketresultsb = $socketb.Connect($hostidb, $portnumberb)if ($socketb.Connected) {$statusb = “Open”$cellcolor = "7CBB59"$socketb.Close()Write-Host "$hostidb port $portnumberb Open" -ForegroundColor Green}else {$statusb = “Closed”$cellcolor = "EB2300"Write-Host "$hostidb port $portnumberb Closed" -ForegroundColor Red}
#HTML Body Content
$HTMLBody = $HTMLBody + @"<CENTER><TABLE  class="fonttable" style="vertical-align:top; margin-left: 0px; margin-top: 0px; margin-bottom: -8px; " BORDER=0><TR bgcolor=#$cellcolor><TD width="115">$hostidb</TD> <TD width="30">$portnumberb</TD> <TD width="60">$statusb</TD></TR></TABLE></CENTER><font size=1><I><!--Updated:&nbsp;$dated--></I></font><BR />"@#Check status of ports single port B$socketb = new-object Net.Sockets.TcpClient$socketresultsb = $socketb.Connect($hostidb, $portnumberb)if ($socketb.Connected) {$statusb = “Open”$cellcolor = "7CBB59"$socketb.Close()Write-Host "$hostidb port $portnumberb Open" -ForegroundColor Green}else {$statusb = “Closed”$cellcolor = "EB2300"Write-Host "$hostidb port $portnumberb Closed" -ForegroundColor Red}
#HTML Body Content
$HTMLBody = $HTMLBody + @"<CENTER><TABLE  class="fonttable" style="vertical-align:top; margin-left: 0px; margin-top: 0px; margin-bottom: -8px; " BORDER=0><TR bgcolor=#$cellcolor><TD width="115">$hostidb</TD> <TD width="30">$portnumberb</TD> <TD width="60">$statusb</TD></TR></TABLE></CENTER><font size=1><I><!--Updated:&nbsp;$dated--></I></font><BR />"@
# CSV for reports#$countresults1=@"#$Dated,$hostida,$portnumbera,$statusa,$Dated,$hostidb,$portnumberb,$statusb#"@#$countresults1 | Add-Content "C:\inetpub\wwwroot\reports\ports.csv"
#$countresults2=@"#$Dated,$hostidb,$portnumberb,$statusb#"@#$countresults2 | Add-Content "C:\inetpub\wwwroot\reports\ports.csv"
#Export to HTMLWrite-Host "Creating HTML file..." -ForegroundColor Yellow$statusupdate | ConvertTo-HTML -head $HTMLHead -body $HTMLBody | out-file $generatedFile#--- PUBLISH THE HTML FILE
Copy-Item $generatedFile $publishedFile -Force
#--- DISPLAY TIME THE SCRIPT TOOK TO RUN
$stopWatch.Stop()
Write-Host "Elapsed Runtime:" $stopWatch.Elapsed.Minutes "minutes and" $stopWatch.Elapsed.Seconds "seconds." -ForegroundColor Cyan
#--- SLEEP 30 SECONDSWrite-Host "Sleeping 30 seconds..." -ForegroundColor YellowStart-Sleep 30}