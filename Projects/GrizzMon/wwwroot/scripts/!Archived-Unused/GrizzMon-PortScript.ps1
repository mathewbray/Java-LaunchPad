while($true) {
#Variables$CSS = get-content "C:\inetpub\wwwroot\css\theme.css"$Dated = (Get-Date -format F)
#HTML Head
$HTMLHead = @"
<!DOCTYPE html>
<HEAD>
<META charset="UTF-8">
<TITLE>GrizzMon</TITLE>
<CENTER>
<STYLE>$CSS</STYLE></HEAD>
"@
#  Get content from config files$hostida = (get-content  "C:\inetpub\wwwroot\cfg\porthostsa.cfg" | Where {-not ($_.StartsWith('#'))})$portnumbera = (get-content "C:\inetpub\wwwroot\cfg\portcfga.cfg" | Where {-not ($_.StartsWith('#'))})$hostidb = (get-content  "C:\inetpub\wwwroot\cfg\porthostsb.cfg" | Where {-not ($_.StartsWith('#'))})$portnumberb = (get-content "C:\inetpub\wwwroot\cfg\portcfgb.cfg" | Where {-not ($_.StartsWith('#'))})
#Check status of ports single port A$socketa = new-object Net.Sockets.TcpClient$socketresultsa = $socketa.Connect($hostida, $portnumbera)if ($socketa.Connected) {$statusa = “Port Open”$cellcolor = "18a171"$socketa.Close()Write-Host "$hostida port $portnumbera Open" -ForegroundColor Green}else {$statusa = “Port Closed”$cellcolor = "ff0000"Write-Host "$hostida port $portnumbera Closed" -ForegroundColor Red}
#HTML Body Content
$HTMLBody = @"<CENTER><meta http-equiv="refresh" content="30">
<Font size=4><B>Monitored Ports</B></font></BR><font size=2><I>Script last run:$dated</I></font><BR /><TABLE><TR bgcolor=#bcbcab><TH>Hosts</TH> <TH>Port</TH> <TH>Status</TH></TR><TR bgcolor=#$cellcolor><TD width="200">$hostida</TD> <TD width="40">$portnumbera</TD> <TD width="90">$statusa</TD></TR></TABLE></CENTER>"@
#Check status of ports single port B$socketb = new-object Net.Sockets.TcpClient$socketresultsb = $socketb.Connect($hostidb, $portnumberb)if ($socketb.Connected) {$statusb = “Port Open”$cellcolor = "18a171"$socketb.Close()Write-Host "$hostidb port $portnumberb Open" -ForegroundColor Green}else {$statusb = “Port Closed”$cellcolor = "ff0000"Write-Host "$hostidb port $portnumberb Closed" -ForegroundColor Red}
#HTML Body Content
$HTMLBody = $HTMLBody + @"<CENTER><TABLE style="margin-top: -1px;"><TR bgcolor=#$cellcolor><TD width="200">$hostidb</TD> <TD width="40">$portnumberb</TD> <TD width="90">$statusb</TD></TR></TABLE></CENTER>"@
# CSV for reports#$countresults1=@"#$Dated,$hostida,$portnumbera,$statusa,$Dated,$hostidb,$portnumberb,$statusb#"@#$countresults1 | Add-Content "C:\inetpub\wwwroot\reports\ports.csv"
#$countresults2=@"#$Dated,$hostidb,$portnumberb,$statusb#"@#$countresults2 | Add-Content "C:\inetpub\wwwroot\reports\ports.csv"
#Export to HTMLWrite-Host "Creating HTML file..." -ForegroundColor Yellow$statusupdate | ConvertTo-HTML -head $HTMLHead -body $HTMLBody | out-file "C:\inetpub\wwwroot\gen\porthosts.htm "#--- SLEEP 30 SECONDSWrite-Host "Sleeping 30 seconds..." -ForegroundColor YellowStart-Sleep 30}