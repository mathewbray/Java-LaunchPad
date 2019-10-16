########################################################################  Powershell Network Monitor Count Script#  Created by Brad Voris
#  This script is used to generate results for the results.htm page#######################################################################
#Script variables$CSS = Get-Content "C:\inetpub\wwwroot\css\theme.css "$Dated = (Get-Date -format F)
#Script commands[array]$i = Get-Content -Path "C:\inetpub\wwwroot\cfg\computers.cfg"[array]$j = Get-Content -Path "C:\inetpub\wwwroot\cfg\porthostsa.cfg"[array]$l = Get-Content -Path "C:\inetpub\wwwroot\cfg\porthostsb.cfg"[array]$k = Get-Content -Path "C:\inetpub\wwwroot\cfg\services.cfg"$ii=$i.length$jj=$j.length$kk=$k.length$ll=$l.length$yy = $jj+$ll
#HTML Header Coding
$HTMLHead = @"
<META http-equiv="refresh" content="30">
<TITLE> 
PSNetMon - Count Module
</TITLE>
<HEAD>
<STYLE>BODY{background-color:#bcbcab;} TABLE{border-width: 1px;border-style: solid;border-color: black;border-collapse: collapse;} TH{border-width: 1px;padding: 3px;border-style: solid;border-color: black;} TD{border-width: 1px;padding: 6px;border-style: solid;border-color: black;}  .myButton { 	-moz-box-shadow:inset 0px 1px 3px 0px #91b8b3; 	-webkit-box-shadow:inset 0px 1px 3px 0px #91b8b3; 	box-shadow:inset 0px 1px 3px 0px #91b8b3; 	background:-webkit-gradient(linear, left top, left bottom, color-stop(0.05, #768d87), color-stop(1, #6c7c7c)); 	background:-moz-linear-gradient(top, #768d87 5%, #6c7c7c 100%); 	background:-webkit-linear-gradient(top, #768d87 5%, #6c7c7c 100%); 	background:-o-linear-gradient(top, #768d87 5%, #6c7c7c 100%); 	background:-ms-linear-gradient(top, #768d87 5%, #6c7c7c 100%); 	background:linear-gradient(to bottom, #768d87 5%, #6c7c7c 100%); 	filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#768d87', endColorstr='#6c7c7c',GradientType=0); 	background-color:#768d87; 	-moz-border-radius:5px; 	-webkit-border-radius:5px; 	border-radius:5px; 	border:1px solid #566963; 	display:inline-block; 	cursor:pointer; 	color:#ffffff; 	font-family:arial; 	font-size:15px; 	font-weight:bold; 	padding:11px 23px; 	text-decoration:none; 	text-shadow:0px -1px 0px #2b665e; } .myButton:hover { 	background:-webkit-gradient(linear, left top, left bottom, color-stop(0.05, #6c7c7c), color-stop(1, #768d87)); 	background:-moz-linear-gradient(top, #6c7c7c 5%, #768d87 100%); 	background:-webkit-linear-gradient(top, #6c7c7c 5%, #768d87 100%); 	background:-o-linear-gradient(top, #6c7c7c 5%, #768d87 100%); 	background:-ms-linear-gradient(top, #6c7c7c 5%, #768d87 100%); 	background:linear-gradient(to bottom, #6c7c7c 5%, #768d87 100%); 	filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#6c7c7c', endColorstr='#768d87',GradientType=0); 	background-color:#6c7c7c; } .myButton:active { 	position:relative; 	top:1px; }</STYLE>
</HEAD><CENTER>"@
#HTML Body Coding
$HTMLBody = @"<CENTER>Monitored Resources<BR /><TABLE><TR bgcolor=#bcbcab><TD>Hosts</TD> <TD>Ports</TD> <TD>Services</TD></TR><TR bgcolor=#d1d0c3
><TD>$ii</TD><TD>$yy</TD><TD>$kk</TD></TR></TABLE><font size=2><I>Script last run:$dated</I></font></CENTER>"@
# Export to CSV for reports#$valarray = ( $Dated,$ii,$yy,$kk)$countresults2=@"$Dated,$ii,$yy,$kk"@$countresults2 | Add-Content "C:\inetpub\wwwroot\reports\count.csv"
# Export to HTML$Script | ConvertTo-HTML -Head $HTMLHead -Body $HTMLBody | Out-file "C:\inetpub\wwwroot\gen\count.html"

