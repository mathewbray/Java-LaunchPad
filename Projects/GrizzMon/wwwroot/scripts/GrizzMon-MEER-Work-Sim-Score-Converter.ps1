$pshost = get-host
$pswindow = $pshost.ui.rawui$pswindow.WindowTitle = "MEER Work Scores"while($true) {#Variables#$CSS = get-content "C:\inetpub\wwwroot\css\theme.css"$dated = (Get-Date -format F)$HTMLHead =  "<Style></style>"$HTMLHead = $HTMLHead + "<CENTER><Font size=6><u>M.E.E.R. Work Simulator Scoreboard</u></font><br><br>"$csvFile = "C:\Share\scores.csv"$csvFileSorted = "C:\Share\scores_sorted.csv"$generatedFile = "C:\inetpub\wwwroot\gen\scores.htm"$publishedFile = "C:\inetpub\wwwroot\gen-pub\scores.htm"
#Check content and ping computers
$HTMLBody = @"<meta http-equiv="refresh" content="300">
<style>


BODY{ 	background-color:#d1d0c3;  	font-family: Arial, Helvetica, sans-serif; }  TABLE{  		border-radius: 5px; } TH{  	border-radius: 5px;  	font-weight: normal; 	font-size:14px; }  TD{  	border-radius: 5px; 	padding-left: 2px;  	padding-right: 2px;  	font-size:13px; }   .myButton { 	-moz-box-shadow:inset 0px 1px 3px 0px #91b8b3; 	-webkit-box-shadow:inset 0px 1px 3px 0px #91b8b3; 	box-shadow:inset 0px 1px 3px 0px #91b8b3; 	background:-webkit-gradient(linear, left top, left bottom, color-stop(0.05, #768d87), color-stop(1, #6c7c7c)); 	background:-moz-linear-gradient(top, #768d87 5%, #6c7c7c 100%); 	background:-webkit-linear-gradient(top, #768d87 5%, #6c7c7c 100%); 	background:-o-linear-gradient(top, #768d87 5%, #6c7c7c 100%); 	background:-ms-linear-gradient(top, #768d87 5%, #6c7c7c 100%); 	background:linear-gradient(to bottom, #768d87 5%, #6c7c7c 100%); 	filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#31507f', endColorstr='#6c7c7c',GradientType=0); 	background-color:#768d87; 	-moz-border-radius:2px; 	-webkit-border-radius:2px; 	border-radius:2px; 	border:1px solid #566963; 	display:inline-block; 	cursor:pointer; 	color:#ffffff; 	font-family:arial; 	font-size:15px; 	font-weight:bold; 	padding:11px 23px; 	text-decoration:none; 	text-shadow:0px -1px 0px #2b665e; } .myButton:hover { 	background:-webkit-gradient(linear, left top, left bottom, color-stop(0.05, #6c7c7c), color-stop(1, #768d87)); 	background:-moz-linear-gradient(top, #6c7c7c 5%, #768d87 100%); 	background:-webkit-linear-gradient(top, #6c7c7c 5%, #768d87 100%); 	background:-o-linear-gradient(top, #6c7c7c 5%, #768d87 100%); 	background:-ms-linear-gradient(top, #6c7c7c 5%, #768d87 100%); 	background:linear-gradient(to bottom, #6c7c7c 5%, #768d87 100%); 	filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#6c7c7c', endColorstr='#768d87',GradientType=0); 	background-color:#6c7c7c; } .myButton:active { 	position:relative; 	top:1px; }

body {
    background-image: url('../images/meer.png');
    background-position: right top; 
    background-repeat: no-repeat;
    background-attachment: fixed;
    
}
table {
    margin-right: 500px ;
    margin-left: 50px ;
    background-color: rgba(188,188,171);
    background: rgba(188,188,171);
}
th, td {
    background-color: rgba(188,188,171);
    background: rgba(188,188,171);
}
</style>
"@
# CSV for reports#$countresults1=@()#$dated,$Icmpresults.Server,$Icmpresults.Desc,$Icmpresults.Status | Add-Content "C:\inetpub\wwwroot\reports\icmp.csv"
#  EXPORT TO HTML AND APPLY COLORED CELLS WITH REPLACEWrite-Host "EXPORTING TO HTML..." -ForegroundColor Yellow#--- SORT CSV$csv = Import-Csv $csvFile $csv | % { $_.Score = [int]$_.Score }$csv | Sort-Object Score -Descending | Export-Csv $csvFileSorted -NoTypeInformation#Import-Csv $csvFileSorted | Sort Score -Descending | Export-Csv $csvFileSorted -NoTypeInformationImport-Csv $csvFileSorted | Select Name,Username,Score | ConvertTo-HTML -head $HTMLHead -body $HTMLBody  | Out-File $generatedFile
#--- REMOVE HEADERS
#(Get-Content $generatedFile).replace("<tr><th>IP</th><th>Description</th><th>Status</th></tr>","") | Set-Content $generatedFile -Force

#--- PUBLISH THE HTML FILE
Copy-Item $generatedFile $publishedFile -Force

#--- SLEEPY TIMES
Start-Sleep 60}
            
