
#requires -version 2.0

Param (
[string[]]$computers=@($env:computername),
[string]$Path="C:\inetpub\wwwroot\gen\drivereport.htm"
)

$Title="Drive Report"

#embed a stylesheet in the html header
$head = @"

 <link rel="stylesheet" type="text/css" href="./css/theme.css"> 

"@

#define an array for html fragments
$fragments=@()

#get the drive data
$data=get-wmiobject -Class Win32_logicaldisk -filter "drivetype=3" -computer $computers

#group data by computername
$groups=$Data | Group-Object -Property SystemName

#this is the graph character
[string]$g=[char]9608

#create html fragments for each computer
#iterate through each group object

ForEach ($computer in $groups) {

$fragments+="
$($computer.Name)
"

#define a collection of drives from the group object
$Drives=$computer.group

#create an html fragment
$html=$drives | Select @{Name="Drive";Expression={$_.DeviceID}},
@{Name="SizeGB";Expression={$_.Size/1GB -as [int]}},
@{Name="UsedGB";Expression={"{0:N2}" -f (($_.Size - $_.Freespace)/1GB) }},
@{Name="FreeGB";Expression={"{0:N2}" -f ($_.FreeSpace/1GB) }},
@{Name="Usage";Expression={
$UsedPer= (($_.Size - $_.Freespace)/$_.Size)*100
$UsedGraph=$g * ($UsedPer/2)
$FreeGraph=$g* ((100-$UsedPer)/2)
#I'm using place holders for the < and > characters
"xopenFont color=Redxclose{0}xopen/FontxclosexopenFont Color=Greenxclose{1}xopen/fontxclose" -f $usedGraph,$FreeGraph
}} | ConvertTo-Html -Fragment

#replace the tag place holders. It is a hack but it works.
$html=$html -replace "xopen","<"
$html=$html -replace "xclose",">"

#add to fragments
$Fragments+=$html

#insert a return between each computer
$fragments+="
"

} #foreach computer

#add a footer
$footer=("
Report run {0} by {1}\{2}" -f (Get-Date -displayhint date),$env:userdomain,$env:username)
$fragments+=$footer

#write the result to a file
ConvertTo-Html -head $head -body $fragments | Out-File $Path