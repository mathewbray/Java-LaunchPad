clear-host
#.\Create-DummyFiles.ps1 -amount 20000 -size 50kb -folder 'F:\Temp\createfiles'

#param(
#$amount = $(throw "Please give an amount of files to be created")
#, [int]$size = $(throw "Please give a the size of the files")
#, $folder = $(throw "Please give an output folder where the files need to be created")
#, $name = $(throw "Please enter a filename")
#, $extension = $(throw "Please enter an extension")
#)

#-- INITIAL PROMPTS
        $amount = Read-Host -Prompt 'Number of files to be created '
        [int]$size = Read-Host -Prompt 'Size of each in Megabytes  '
        $folder = Read-Host -Prompt 'Output folder '
        $name = Read-Host -Prompt 'Filename '
        $extension = Read-Host -Prompt 'Extension of the file(s) '

# Convert megabytes to bytes
$size = $size * 1048576

Write-Host

# Check for input
if(Test-Path $folder)
{
if($name -eq $null)
{
Write-Host "No filename given. Using default setting 'dummy'" -ForegroundColor Yellow
$name = 'dummy'
}
 
if($extension -eq $null)
{
Write-Host "No filename extension given. Using default setting '.txt'" -ForegroundColor Yellow
$extension = 'txt'
}
elseif($extension -contains '.')
{
$extension = $extension.Substring(($extension.LastIndexOf(".") + 1), ($extension.Length - 1))
}
 
for($i = 1; $i -le $amount; $i++)
{
$path = $folder + '\' + $name + '_' + $i + '.' + $extension
$file = [io.file]::Create($path)
$file.SetLength($size)
$file.Close
sleep 0.5
}
 
}
else{
Write-Host "The folder $folder doesn't exist" -ForegroundColor Red
Exit(0)
}