<#====================================================================
Author(s):		Mathew Bray <mathew.bray@gmail.com>
File: 			Monitaur.ps1
Date:			2015-09-09
Revision: 		2
References:
 Start-Monitor - https://gallery.technet.microsoft.com/scriptcenter/2d537e5c-b5d4-42ca-a23e-2cbce636f58d/
 Set-TopMost - poshcode.org/4070
 Window Sizing - http://blogs.technet.com/b/heyscriptingguy/archive/2006/12/04/how-can-i-expand-the-width-of-the-windows-powershell-console.aspx
 Blink-Message - https://social.technet.microsoft.com/forums/windowsserver/en-US/3e9585cf-bbed-4e8e-80ba-65fd0a3a71d5/flashing-text-in-powershell
 Start-Countdown - http://poshcode.org/3378
 SetForegroundWindow - http://stackoverflow.com/questions/2556872/how-to-set-foreground-window-from-powershell-event-subscriber-action


Release Notes:
 v1 - Resized Start-Monitor and set to clear screen each ping session. 
      Added note to disable QuickEdit (accidental highliting will pause the script).
      Added beep when down and options for speech, sound file, or built in sounds.
      Added blinking text when there is no response, and an "email sent" notification.

      
 v2 - Added an IP translation ability to display any name or description instead of the IP.

====================================================================
#>

#############################################################
#This section sets the properties of the window, size and buffer size, as well as title.
$pshost = get-host
$pswindow = $pshost.ui.rawui
#
$pswindow.WindowTitle = "Monitaur"
#
#$newsize = $pswindow.windowsize
#$newsize.height = 40
#$newsize.width = 30
#$pswindow.windowsize = $newsize
#
#$newsize = $pswindow.buffersize
#$newsize.height = 40
#$newsize.width = 30
#$pswindow.buffersize = $newsize
#
$pswindow.BackgroundColor = "black"
Clear-Host
 
#############################################################
# Main Monitor function - https://gallery.technet.microsoft.com/scriptcenter/2d537e5c-b5d4-42ca-a23e-2cbce636f58d/
function Start-Monitaur {
      
      #Requires -Version 2.0            
      [CmdletBinding()]            
      Param             
      (                       
            [Parameter(Position=0,                         
                       ValueFromPipeline=$true,            
                       ValueFromPipelineByPropertyName=$true)]

            # String for Computer Names
            #[String[]]$ComputerName = $env:COMPUTERNAME,        

            # String for Computer Names
            [String[]]$SessionList = "SessionList.csv",   
            
            # Switch to Enable Email Notifications on First Down
            [Switch]$notifyonServerDown,
            
            # Switch to Enable Email Notifications on Server Online
            [Switch]$notifyonServerBackOnline,
            
            # Switch to Enable Email Notifications on MaxOutageCount
            [Switch]$notifyonMaxOutageCount,
            
            # Switch to Enable all notifications
            [Switch]$notifyAll,

            # Switch to Enable all notifications
            [Switch]$StayOnTop,

            # specify the time you want email notifications resent for hosts that are down
            $EmailTimeOut = 30,
            # specify the time you want to cycle through your host lists.
            $SleepTimeOut = 10,
            # specify the maximum hosts that can be down before the script is aborted
            $MaxOutageCount = 100,

            # speech when unavailable
            $speechdown = "system down",

            # sound when unavailable
            $soundfileDown = 'C:\windows\media\notify.wav',
            
            # specify who gets notified 
            $tonotification = "mathew.bray.1@us.af.mil", 

            # specify where the notifications come from 
            $fromnotification = "Monitor-Script@us.af.mil", 
            # specify the SMTP server 
            $smtpserver = "131.9.25.144",
            
            # reset the lists of hosts prior to looping
            $OutageHosts = @()            
      )#End Param


 # Use end block, to ensure all computers are read in at once, even by pipeline     
 end {


    # If notifyAll option is used, then set all messaging to true
    if ($notifyAll)
    {
        $notifyonMaxOutageCount,$notifyonServerBackOnline,$notifyonServerDown =  $True,$True,$True
    }

    Write-Verbose -Message "computername: $computername"
    Write-Verbose -Message "notifyonMaxOutageCount: $notifyonMaxOutageCount"
    Write-Verbose -Message "notifyonServerBackOnline: $notifyonServerBackOnline"
    Write-Verbose -Message "notifyonServerDown: $notifyonServerDown"

    # Allow
    if ( $Input )
    {
        Write-Verbose -Message "Input: $Input"
        $ComputerName = $Input
    }
    
    #############################################################
    # Stay on Top Script - poshcode.org/4070
      if ($StayOnTop)
      {
        $signature = @"
       
        [DllImport("user32.dll")]  
        public static extern IntPtr FindWindow(string lpClassName, string lpWindowName);  
 
        public static IntPtr FindWindow(string windowName){
                return FindWindow(null,windowName);
        }
 
        [DllImport("user32.dll")]
        public static extern bool SetWindowPos(IntPtr hWnd,
        IntPtr hWndInsertAfter, int X,int Y, int cx, int cy, uint uFlags);
 
        [DllImport("user32.dll")]  
        public static extern bool ShowWindow(IntPtr hWnd, int nCmdShow);
 
        static readonly IntPtr HWND_TOPMOST = new IntPtr(-1);
        static readonly IntPtr HWND_NOTOPMOST = new IntPtr(-2);
 
        const UInt32 SWP_NOSIZE = 0x0001;
        const UInt32 SWP_NOMOVE = 0x0002;
 
        const UInt32 TOPMOST_FLAGS = SWP_NOMOVE | SWP_NOSIZE;
 
        public static void MakeTopMost (IntPtr fHandle)
        {
                SetWindowPos(fHandle, HWND_TOPMOST, 0, 0, 0, 0, TOPMOST_FLAGS);
        }
 
        public static void MakeNormal (IntPtr fHandle)
        {
                SetWindowPos(fHandle, HWND_NOTOPMOST, 0, 0, 0, 0, TOPMOST_FLAGS);
        }
"@
 
 
            $app = Add-Type -MemberDefinition $signature -Name Win32Window -Namespace ScriptFanatic.WinAPI -ReferencedAssemblies System.Windows.Forms -Using System.Windows.Forms -PassThru
 
        function Set-TopMost
            {
                    param(         
                            [Parameter(
                                    Position=0,ValueFromPipelineByPropertyName=$true
                            )][Alias('MainWindowHandle')]$hWnd=0,
                            [Parameter()][switch]$Disable
                     )
                    if($hWnd -ne 0)
                    {
                            if($Disable)
                            {
                                    Write-Verbose "Set process handle :$hWnd to NORMAL state"
                                    $null = $app::MakeNormal($hWnd)
                                    return
                            }
                                Write-Verbose "Set process handle :$hWnd to TOPMOST state"
                            $null = $app::MakeTopMost($hWnd)
                    }
                    else
                    {
                            Write-Verbose "$hWnd is 0"
                    }
            }
        function Get-WindowByTitle($WindowTitle="*")
                {
                        Write-Verbose "WindowTitle is: $WindowTitle"
       
                        if($WindowTitle -eq "*")
                        {
                                Write-Verbose "WindowTitle is *, print all windows title"
                                Get-Process | Where-Object {$_.MainWindowTitle} | Select-Object Id,Name,MainWindowHandle,MainWindowTitle
                        }
                        else
                        {
                                Write-Verbose "WindowTitle is $WindowTitle"
                                Get-Process | Where-Object {$_.MainWindowTitle -like "*$WindowTitle*"} | Select-Object Id,Name,MainWindowHandle,MainWindowTitle
                        }
                }
             gps -Id $pid | Set-TopMost
             }

    #######################################################
    # Bring to foreground Script - http://stackoverflow.com/questions/2556872/how-to-set-foreground-window-from-powershell-event-subscriber-action
    Add-Type @"
        using System;
        using System.Runtime.InteropServices;
        public class Tricks {
            [DllImport("user32.dll")]
            [return: MarshalAs(UnmanagedType.Bool)]
            public static extern bool SetForegroundWindow(IntPtr hWnd);
        }
"@
        $httf = (Get-Process -id $pid).MainWindowHandle

    #############################################################
    # Blinking Message function - https://social.technet.microsoft.com/forums/windowsserver/en-US/3e9585cf-bbed-4e8e-80ba-65fd0a3a71d5/flashing-text-in-powershell
    function Blink-Message {
     param([String]$Message,[int]$Delay,[int]$Count,[ConsoleColor[]]$Colors,$ForeGroundColor) 
        $startColor = [Console]::BackgroundColor
        $startFGColor = [Console]::ForegroundColor
        $startLeft  = [Console]::CursorLeft
        $startTop   = [Console]::CursorTop
        $colorCount = $Colors.Length
        for($i = 0; $i -lt $Count; $i++) {
            [Console]::CursorLeft = $startLeft
            [Console]::CursorTop  = $startTop
            [Console]::BackgroundColor = $Colors[$($i % $colorCount)]
            [Console]::ForegroundColor = $ForeGroundColor
            [Console]::WriteLine($Message)
            Start-Sleep -Milliseconds $Delay
        }
        [Console]::BackgroundColor = $startColor
        [Console]::ForegroundColor = $startFGColor
    }

    #############################################################
    # Ticking Timer - http://poshcode.org/3378
    function Start-Countdown{

    Param(
    [INT]$Seconds = (Read-Host "Enter seconds to countdown from"),
    [Switch]$ProgressBar,
    [String]$Message = "Blast Off!"
    )
    #Clear-Host
    while ($seconds -ge 1){
    If($ProgressBar){
	    Write-Progress -Activity "Countdown" -SecondsRemaining $Seconds -Status "Time Remaining"
	    Start-Sleep -Seconds 1
    }ELSE{
        Write-Host -NoNewline  "."
	    Start-Sleep -Seconds 1
	    #Clear-Host
    }
    $Seconds --
    }
    #Write-Output $Message
    }


      
#############################################################
# start looping here
      Do{
            


            $available = @()
            $notavailable = @()
            Write-Host ""
            Write-Host "    "(Get-Date)
            Write-Host ""
            
            # Read the File with the Hosts every cycle, this way to can add/remove hosts
            # from the list without touching the script/scheduled task, 
            # also hash/comment (#) out any hosts that are going for maintenance or are down.
            $ComputerName | Where-Object {!($_ -match "#")} | 
            #"test1","test2" | Where-Object {!($_ -match "#")} |
            ForEach-Object {
                  if(Test-Connection -ComputerName $_ -Count 1 -ea silentlycontinue)
                  {
                        # if the Host is AVAILABLE then write it to the screen

                            #Check if IP is in the translation list if not then write IP
#                            if (Get-Content Device-Hostname-IP-Translation-List.txt | Select-String "$_" -quiet) {
#                                $hostname = ((get-content Device-Hostname-IP-Translation-List.txt | select-string -pattern "$_" -context 1 ).context.precontext)[0]
#                                Write-Host " " $hostname  -BackgroundColor Black -ForegroundColor Green
#                                }
#                            else{
                                Write-Host " "$_ -BackgroundColor Black -ForegroundColor Green
#                                }
                            
                            # Removed this line for translation list checking
                            # write-host " "$_ -BackgroundColor Black -ForegroundColor Green

                        [String[]]$available += $_
                        
                        # if the Host was out and is now BACK-ONLINE, remove it from the OutageHosts list
                        if ($OutageHosts -ne $Null)
                        {
                              if ($OutageHosts.ContainsKey($_))
                              {
                                  # Back online tone
                                  [console]::beep(3000,200)
                                  [console]::beep(4000,200)
                                  [console]::beep(5000,200)
                                  Blink-Message "<<<<<< Back Online! :) >>>>>>>" 150 7 black,white,green green


                                    $OutageHosts.Remove($_)
                                    $Now = Get-date
                                    if ($notifyonServerBackOnline)
                                    {
                                          $Body = "$_ is back online at $Now"
                                          Send-MailMessage -Body "$body" -to $tonotification -from $fromnotification `
                                          -Subject "Host $_ is up" -SmtpServer $smtpserver
                                    }
                                    
                              }
                        }  
                  }
                  else
                  {
                        # If the host is unavailable, give a warning to screen

                                #Check if IP is in the translation list if not then write IP
#                                if (Get-Content Device-Hostname-IP-Translation-List.txt | Select-String "$_" -quiet) {
#                
#                                    #
#                                    $hostname = ((get-content Device-Hostname-IP-Translation-List.txt | select-string -pattern "$_" -context 1 ).context.precontext)[0]
#                                    Write-Host " " $hostname  "..." -BackgroundColor Black -ForegroundColor Red
#           
#                                    }
#                                else{
                                    Write-Host " "$_ "..."-BackgroundColor Black -ForegroundColor Red
#                                    }
                                

                                # Removed this line for translation list checking
                                # write-host " " $_ "..." -BackgroundColor Black -ForegroundColor Red
                        if(!(Test-Connection -ComputerName $_ -Count 2 -ea silentlycontinue))
                        {
                              # If the host is still unavailable for 4 full pings, play sound, write error, and send email
                              

                              $sig = '[DllImport("user32.dll")] public static extern bool ShowWindowAsync(IntPtr hWnd, int nCmdShow);'
                              Add-Type -MemberDefinition $sig -name NativeMethods -namespace Win32
                              $hwnd = @(Get-Process -id $pid)[0].MainWindowHandle
                              #[Void] [Win32.NativeMethods]::ShowWindowAsync($hwnd, 4)



                        #[Void] [Tricks]::SetForegroundWindow($httf)
                             
                              ############################################################
                              # Optional Sound Types
                              #
                              #System Sound
                              #[System.Media.SystemSounds]::Asterisk.play()
                              #[System.Media.SystemSounds]::Beep.play()
                              #[System.Media.SystemSounds]::Exclamation.play()
                              #[System.Media.SystemSounds]::Hand.play()
                              #
                              #Speech
                              #Add-Type -AssemblyName System.Speech 
                              #$synth = New-Object -TypeName System.Speech.Synthesis.SpeechSynthesizer
                              #$synth.Speak($speechdown)
                              #
                              #Play Sound File
                              #$sound = new-Object System.Media.SoundPlayer;
                              #$sound.SoundLocation=$soundfileDown;
                              #$sound.Play();

                              #Console Beep

                              [console]::beep(5000,200)
                              [console]::beep(4000,200)
                              [console]::beep(3000,200)

                              Blink-Message "<<<<<<< No Response!! >>>>>>>>" 150 7 black,white,red red


                              
                                           
                              #write-host "        "$_ -BackgroundColor Magenta -ForegroundColor White
                              [Array]$notavailable += $_
                              
                              if ($OutageHosts -ne $Null)
                              {
                                    if (!$OutageHosts.ContainsKey($_))
                                    {
                                          # First time down add to the list and send email
                                              #Maximize window and bring to foreground
                                              [Void] [Win32.NativeMethods]::ShowWindowAsync($hwnd, 4)
                                              [Void] [Tricks]::SetForegroundWindow($httf)

                                          #Write-Host "----- Down! Email Sent!! -----"
                                            if ($notifyonServerDown)
                                            {
                                             Blink-Message "///////////////\\\\\\\\\\\\\\\`r<<<<< Down! Email Sent!! >>>>>`r\\\\\\\\\\\\\\\///////////////" 150 7 black,white,red red
                                            }

                                          $OutageHosts.Add($_,(get-date))
                                          $Now = Get-date
                                          if ($notifyonServerDown)
                                          {
                                                $Body = "$_ has not responded for 5 pings at $Now"
                                                Send-MailMessage -Body "$body" -to $tonotification -from $fromnotification `
                                                -Subject "Host $_ is down" -SmtpServer $smtpserver
                                          }
                                    }
                                    else
                                    {
                                          # If the host is in the list do nothing for 1 hour and then remove from the list.
                                          #Write-Host "$_ Is in the OutageHosts list"
                                          if (((Get-Date) - $OutageHosts.Item($_)).TotalMinutes -gt $EmailTimeOut)
                                          {$OutageHosts.Remove($_)}
                                    }
                              }
                              else
                              {
                                    # First time down create the list and send email
                                              # Maximize and bring to foreground
                                              [Void] [Win32.NativeMethods]::ShowWindowAsync($hwnd, 4)
                                              [Void] [Tricks]::SetForegroundWindow($httf)
                                    #Write-Host "----- Down! Email Sent!! -----"
                                    
                                        if ($notifyonServerDown)
                                        {
                                            Blink-Message "///////////////\\\\\\\\\\\\\\\`r<<<<< Down! Email Sent!! >>>>>`r\\\\\\\\\\\\\\\///////////////" 150 7 black,white,red red
                                        }  
                                    $OutageHosts = @{$_=(get-date)}
                                    #$OutageHosts += "$_ @ "(get-date)
                                    $Now = Get-Date
                                    #$OutageHosts += "$_@$Now"
                                    $Now = Get-date
                                    if ($notifyonServerDown)
                                    {
                                          $Body = "$_ has not responded for 5 pings at $Now"
                                          Send-MailMessage -Body "$body" -to $tonotification -from $fromnotification `
                                          -Subject "Host $_ is down" -SmtpServer $smtpserver
                                    }
                              } 
                        }
                  }
            }
            # Report to screen the details
            Write-Host ""
            Write-Host "Up:"$available.count " Down:"$notavailable.count
            if ($OutageHosts)
            {
                  Write-Host "Not available hosts:"
                  $OutageHosts 
            }
            Write-Host ""

            #Write-Host "Recycle in $SleepTimeOut seconds..."
            #Start-Sleep -Seconds $SleepTimeOut
            Start-Countdown -Seconds $SleepTimeOut

            # Clear the Screen cuz Mr. Bray likes it neat.
            cls
            if ($OutageHosts.Count -gt $MaxOutageCount)
            {
                  # If there are more than a certain number of host down in an hour abort the script.
                  $Exit = $True
                  $body = $OutageHosts | Out-String
                  
                  if ($notifyonMaxOutageCount)
                  {
                        Send-MailMessage -Body "$body" -to $tonotification -from $fromnotification `
                        -Subject "More than $MaxOutageCount Hosts down, monitoring aborted" -SmtpServer $smtpServer
                  }
            }
      }
      while ($Exit -ne $True)
}#End     
}#Start-Monitaur


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
Start-Monitaur -ComputerName $computers