#-- LaunchPad Powershell Update Script Example

#-- Variables
$sourceFolder = "U:\Software\LaunchPad-Java\LaunchPad 1.0"
$destinationFolder = "C:\LaunchPad"

#-- Auto-Start on Login
copy-item -Force "$sourceFolder\Links\LaunchPad\LaunchPad.lnk" "C:\Users\$env:username\AppData\Roaming\Microsoft\Windows\Start Menu\Programs\Startup\"

#-- Add Shortcut to Desktop
copy-item -Force "$sourceFolder\Links\LaunchPad\LaunchPad.lnk" "C:\Users\$env:username\Desktop\"

#-- Create Folder
New-Item -Force -ItemType directory -Path "C:\LaunchPad\"

#-- Copy Files
robocopy $sourceFolder $destinationFolder /MIR /R:0 /W:0

#-- Rename .zip in SecureCRT due to .zip files not being allowed on share
Rename-Item -Force -Path "C:\LaunchPad\Apps\SecureCRT\vpython27.zip.remove" -NewName "vpython27.zip"

#-- Pause
Write-Host "Press any key to launch..." -ForegroundColor Green
cmd /c pause | Out-Null

#-- Re-Launch
Start-Process "C:\Users\$env:username\Desktop\LaunchPad.lnk"

exit