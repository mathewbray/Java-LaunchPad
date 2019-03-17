#This section sets the properties of the window, size and buffer size, as well as title.
$pshost = get-host
$pswindow = $pshost.ui.rawui

$pswindow.WindowTitle = "Monitaur"



$newsize = $pswindow.windowsize
$newsize.height = 40
$newsize.width = 30
$pswindow.windowsize = $newsize

$newsize = $pswindow.buffersize
$newsize.height = 40
$newsize.width = 30
$pswindow.buffersize = $newsize

$pswindow.BackgroundColor = "black"



Clear-Host

# Dot source the script - Loads into memory
. ./Monitaur.ps1

cls


# load the list of computers
$computers = Get-Content Monitor-List.txt

# QuickEdit mode can cause someone to accidentally pause the script
echo ""
echo "       Please disable"
echo "       QuickEdit Mode"
echo "     before continuing..."
echo ""
echo ""
pause
cls

# call the function
Start-Monitaur -ComputerName $computers -NotifyAll