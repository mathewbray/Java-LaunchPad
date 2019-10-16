#--- SET WINDOW TITLE
$pshost = get-host
$pswindow = $pshost.ui.rawui$pswindow.WindowTitle = "HTML File Update Status"#--- START LOOPwhile($true) {#--- SET START TIME$stopWatch = [system.diagnostics.stopwatch]::startNew()
$stopWatch.Start()

#--- SET VARIABLES
$counter = 0
$Dated = (Get-Date -format F)
$Date = Get-Date
$Date = $Date.AddMinutes(-15)
$CurrentDateStr = $Date.ToString("yyyyMMddhhmmss")
$Files = Get-ChildItem -path C:\inetpub\wwwroot\gen-pub\* -Include *.htm
$CSS = get-content "C:\inetpub\wwwroot\css\theme.css"
$failedFilesArray = @()
$activeFilesArray = @()
$generatedFile = "C:\inetpub\wwwroot\gen\updatestatus.htm"
$publishedFile = "C:\inetpub\wwwroot\gen-pub\updatestatus.htm"


#--- HTML HEAD CONTENT
$HTMLHead = @"
<!DOCTYPE html>
<HEAD>
<META charset="UTF-8">
<STYLE>
$CSS 

.fonttable, .fonttable TD, .fonttable TH
{
font-size:14pt;
padding:1px;
text-align:center;
}@-webkit-keyframes example {
    0% {background-color:red;}
    50%  {background-color:yellow;}
    100% {background-color:red;}

}
@keyframes example {
    0% {background-color:red;}
    50%  {background-color:yellow;}
    100% {background-color:red;}

}
</STYLE></HEAD>
"@




#--- CHECK MODIFIED DATE AND COMPARE TO CURRENT
ForEach ($File in $Files){

    $FileDate = $File.LastWriteTime
    $ModifiedDateStr = $FileDate.ToString("yyyyMMddhhmmss")

    if ($ModifiedDateStr -lt $CurrentDateStr ) {
        write-host "$File is OLD!" -ForegroundColor Red
        $counter++
        $failedFilesArray += $File
    }
    
    if ($ModifiedDateStr -gt $CurrentDateStr ) {
        write-host "$File is FRESH!" -ForegroundColor Green
        $activeFilesArray += $File
    }


}

$failedFilesArray


#--- UPDATE HTML FILE WITH COUNTER AND DATE
if ($counter -gt 0 ){
Write-Host "Files not Updated: $counter" -ForegroundColor Red
#$cellcolor = "bgcolor=#EB2300"
$cellcolor = $flashingbg
}
else{
Write-Host "Files not Updated: $counter" -ForegroundColor Green
$cellcolor = "bgcolor=#7CBB59"
}




$flashingbg = 'style="-webkit-animation-name: example; -webkit-animation-duration: 4s; -webkit-animation-iteration-count: infinite; animation-name: example; animation-duration: 1s; animation-iteration-count: infinite;"'


#--- HTML BODY CONTENT
$HTMLBody = @"<meta http-equiv="refresh" content="60"><TABLE   class="fonttable" style="vertical-align:top; margin-left: 0px; margin-top: -8px;  margin-bottom: -5px; float:left; "  BORDER=0><tr><th style="font-size:10pt">Failed Updates: </th><TD TD width="20" $cellcolor><a href="./updatestatus.htm"  target="index_middle" >$counter</a></TD></TABLE><font size=2>&nbsp;&nbsp;Updated:&nbsp;$Dated</font><br><br>"@#--- 


#--- Export t    Write-Host "Creating HTML file..." -ForegroundColor Yellow    #--- EXPORT TO HTML
        $failedFilesArray | ConvertTo-HTML -head $HTMLHead -body $HTMLBody -Property Name,LastWriteTime | out-file $generatedFile

#--- PUBLISH THE HTML FILE
Copy-Item $generatedFile $publishedFile -Force

#--- DISPLAY TIME THE SCRIPT TOOK TO RUN
$stopWatch.Stop()
Write-Host "Elapsed Runtime:" $stopWatch.Elapsed.Minutes "minutes and" $stopWatch.Elapsed.Seconds "seconds." -ForegroundColor Cyan
#--- SLEEP 60 SECONDSWrite-Host "Sleeping 60 seconds..." -ForegroundColor YellowStart-Sleep 60
}
            