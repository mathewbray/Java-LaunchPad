/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package launchpad;

import java.awt.AWTException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Mathew
 */
public class LaunchPad {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     * @throws java.net.URISyntaxException
     * @throws java.awt.AWTException
     * @throws java.lang.InterruptedException
     * @throws java.lang.ClassNotFoundException
     * @throws java.lang.InstantiationException
     * @throws java.lang.IllegalAccessException
     * @throws javax.swing.UnsupportedLookAndFeelException
     */
    public static void main(String[] args) throws IOException, FileNotFoundException, URISyntaxException, AWTException, InterruptedException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        //--- Shared Items
        String pathUserProfile = System.getenv("USERPROFILE");
        File pathDesktop = new File(System.getProperty("user.home"), "Desktop");
        //String strPathLaunchPadFolder = pathUserProfile + "\\AppData\\Local\\LaunchPad_Java";
        String strPathLaunchPadFolder = System.getenv("SYSTEMDRIVE") + "\\LaunchPad";
        String strPathLaunchPadPersistantUserFolder = pathUserProfile + "\\AppData\\Local\\LaunchPad_Java_Persistant_User";    
        String strPathLaunchPadPersistantPropertiesFile = strPathLaunchPadPersistantUserFolder + "\\launchpad.properties";
        String strSessionListFavoritesFolder = strPathLaunchPadPersistantUserFolder + "\\FavoritesSessionList";
        String strPathPropertiesFile = strPathLaunchPadFolder + "\\launchpad.properties";

      
        //--- Create folders
        new File(strPathLaunchPadFolder).mkdirs();
        new File(strPathLaunchPadPersistantUserFolder).mkdirs();        
        new File(strSessionListFavoritesFolder).mkdirs();
        

        

        //--- Check for properties file
        File filePropertiesFile = new File(strPathPropertiesFile);
        if (!filePropertiesFile.exists()) {
            System.out.println("Properties file not found.");
            //JOptionPane.showMessageDialog(null, "Properties file not found.", "RIP!", JOptionPane.INFORMATION_MESSAGE);
            //System.exit( 0 );
            filePropertiesFile.createNewFile();
            List<String> lines = Arrays.asList(
"Button.01.Icon=onenote.png",
"Button.01.StrExec=cmd /c start onenote.exe",
"Button.01.ToolTip=Notebook",
"Button.02.Icon=keep.png",
"Button.02.StrExec=cmd /c start chrome.exe https://keep.google.com",
"Button.02.ToolTip=Keep",
"Button.03.Icon=trello.png",
"Button.03.StrExec=cmd /c start chrome.exe https://trello.com",
"Button.03.ToolTip=Trello",
"Button.04.Icon=gmail.png",
"Button.04.StrExec=cmd /c start chrome.exe https://mail.google.com",
"Button.04.ToolTip=Gmail",
"Button.05.Icon=folder-yellow-home.png",
"Button.05.StrExec=cmd /c start explorer.exe \"%USERPROFILE%\\\\Desktop\\\\\"",
"Button.05.ToolTip=Home",
"Button.06.Icon=folder-yellow-development.png",
"Button.06.StrExec=",
"Button.06.ToolTip=Development",
"Button.07.Icon=folder-yellow-favorites-icon.png",
"Button.07.StrExec=",
"Button.07.ToolTip=",
"Button.08.Icon=folder-yellow-important-icon.png",
"Button.08.StrExec=",
"Button.08.ToolTip=",
"Button.09.Icon=infoblox.png",
"Button.09.StrExec=",
"Button.09.ToolTip=",
"Button.10.Icon=ciscoapicem.png",
"Button.10.StrExec=",
"Button.10.ToolTip=",
"Button.11.Icon=alert.png",
"Button.11.StrExec=",
"Button.11.ToolTip=",
"Button.12.Icon=coffee.png",
"Button.12.StrExec=",
"Button.12.ToolTip=",
"Button.13.Icon=prtg.png",
"Button.13.StrExec=",
"Button.13.ToolTip=",
"Button.14.Icon=favorites.png",
"Button.14.StrExec=chrome.exe",
"Button.14.ToolTip=",
"Button.15.Icon=apps.png",
"Button.15.StrExec=",
"Button.15.ToolTip=",
"Button.16.Icon=map.png",
"Button.16.StrExec=",
"Button.16.ToolTip=",
"Button.17.Icon=backup2.png",
"Button.17.StrExec=",
"Button.17.ToolTip=",
"Button.18.Icon=ipconfig.png",
"Button.18.StrExec=cmd.exe /c start powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunIPconfig \\= {Clear-Host; ipconfig /all; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunIPconfig}; &$RunIPconfig}\"",
"Button.18.ToolTip=",
"Button.19.Icon=winscp2.png",
"Button.19.StrExec=",
"Button.19.ToolTip=",
"Button.20.Icon=timezone.png",
"Button.20.StrExec=",
"Button.20.ToolTip=STIG Viewer",
"Button.21.Icon=vmware2.png",
"Button.21.StrExec=",
"Button.21.ToolTip=VMware vCenter",
"Button.22.Icon=windows5.png",
"Button.22.StrExec=mstsc /v:SERVERNAME",
"Button.22.ToolTip=Windows Server",
"Button.23.Icon=splunk.png",
"Button.23.StrExec=",
"Button.23.ToolTip=Splunk",
"Button.24.Icon=solarwinds.png",
"Button.24.StrExec=",
"Button.24.ToolTip=Solarwinds",
"ButtonExecuteFunction.DoubleClick=SSH",
"ButtonExecuteFunction.OnEnterPress=SSH",
"Custom.Link.01.Description=Google",
"Custom.Link.01.Exec=cmd /c start chrome.exe https://www.google.com/",
"Custom.Link.02.Description=Stack Overflow",
"Custom.Link.02.Exec=cmd /c start chrome.exe https://stackoverflow.com/",
"Custom.Link.03.Description=GitHub",
"Custom.Link.03.Exec=cmd /c start chrome.exe https://github.com/",
"Custom.Link.04.Description=Reddit",
"Custom.Link.04.Exec=cmd /c start chrome.exe https://www.reddit.com/r/worldnews/",
"Custom.Link.05.Description=Trello",
"Custom.Link.05.Exec=cmd /c start chrome.exe https://trello.com/",
"Custom.Link.06.Description=RegEx101",
"Custom.Link.06.Exec=cmd /c start chrome.exe https://regex101.com/",
"Custom.Link.07.Description=Pearson Vue",
"Custom.Link.07.Exec=cmd /c start chrome.exe https://home.pearsonvue.com/",
"Custom.Link.08.Description=Wikipedia",
"Custom.Link.08.Exec=cmd /c start chrome.exe https://en.wikipedia.org/wiki/Wikipedia:Wiki_Game",
"Custom.Link.09.Description=Base64 Image Convert",
"Custom.Link.09.Exec=cmd /c start chrome.exe https://www.base64-image.de/",
"Custom.Link.10.Description=Typetester",
"Custom.Link.10.Exec=cmd /c start chrome.exe http://classic.typetester.org/",
"Custom.Link.11.Description=ISC2 CISSP Login",
"Custom.Link.11.Exec=cmd /c start chrome.exe https://www.isc2.org/ISC2Login",
"Custom.Link.12.Description=Cybrary",
"Custom.Link.12.Exec=cmd /c start chrome.exe https://www.cybrary.it/",
"Custom.Link.13.Description=CompTIA Login",
"Custom.Link.13.Exec=cmd /c start chrome.exe https://www.comptia.org/members/my-dashboard",
"Custom.Link.14.Description=ISC2 CPE",
"Custom.Link.14.Exec=cmd /c start chrome.exe https://live.blueskybroadcast.com/bsb/client/CL_DEFAULT.asp?Client=411114&title=Home",
"Custom.Link.15.Description=<html><center>DoD Cyber Exchange<br>DISA Home</center></html>",
"Custom.Link.15.Exec=cmd /c start chrome.exe https://public.cyber.mil/",
"Custom.Link.16.Description=<html><center>DoD Cyber Exchange<br>DISA STIGs</center></html>",
"Custom.Link.16.Exec=cmd /c start chrome.exe https://public.cyber.mil/stigs/",
"Custom.Link.17.Description=DoD SAFE",
"Custom.Link.17.Exec=cmd /c start chrome.exe https://safe.apps.mil/",
"Custom.Link.18.Description=Internet Storm Center",
"Custom.Link.18.Exec=cmd /c start chrome.exe https://isc.sans.edu/",
"Custom.Link.19.Description=Google Security Blog",
"Custom.Link.19.Exec=cmd /c start chrome.exe https://security.googleblog.com/",
"Custom.Link.20.Description=CISA",
"Custom.Link.20.Exec=cmd /c start chrome.exe https://www.us-cert.gov/",
"Custom.Link.21.Description=Reddit - /r/netsec",
"Custom.Link.21.Exec=cmd /c start chrome.exe https://www.reddit.com/r/netsec/",
"Custom.Link.22.Description=Link22",
"Custom.Link.22.Exec=cmd /c start powershell.exe -Command \"Write-Host Link22 ; pause\"",
"Custom.Link.23.Description=Link23",
"Custom.Link.23.Exec=cmd /c start powershell.exe -Command \"Write-Host Link23 ; pause\"",
"Custom.Link.24.Description=Link24",
"Custom.Link.24.Exec=cmd /c start powershell.exe -Command \"Write-Host Link24 ; pause\"",
"Custom.Link.25.Description=Link25",
"Custom.Link.25.Exec=cmd /c start powershell.exe -Command \"Write-Host Link25 ; pause\"",
"Custom.Link.26.Description=Link26",
"Custom.Link.26.Exec=cmd /c start powershell.exe -Command \"Write-Host Link26 ; pause\"",
"Custom.Link.27.Description=Link27",
"Custom.Link.27.Exec=cmd /c start powershell.exe -Command \"Write-Host Link27 ; pause\"",
"Custom.Link.28.Description=Link28",
"Custom.Link.28.Exec=cmd /c start powershell.exe -Command \"Write-Host Link28 ; pause\"",
"Custom.Link.29.Description=Link29",
"Custom.Link.29.Exec=cmd /c start powershell.exe -Command \"Write-Host Link29 ; pause\"",
"Custom.Link.30.Description=Link30",
"Custom.Link.30.Exec=cmd /c start powershell.exe -Command \"Write-Host Link30 ; pause\"",
"Custom.Link.31.Description=Link31",
"Custom.Link.31.Exec=cmd /c start powershell.exe -Command \"Write-Host Link31 ; pause\"",
"Custom.Link.32.Description=Link32",
"Custom.Link.32.Exec=cmd /c start powershell.exe -Command \"Write-Host Link32 ; pause\"",
"Custom.Link.33.Description=Link33",
"Custom.Link.33.Exec=cmd /c start powershell.exe -Command \"Write-Host Link33 ; pause\"",
"Custom.Link.34.Description=Link34",
"Custom.Link.34.Exec=cmd /c start powershell.exe -Command \"Write-Host Link34 ; pause\"",
"Custom.Link.35.Description=Link35",
"Custom.Link.35.Exec=cmd /c start powershell.exe -Command \"Write-Host Link35 ; pause\"",
"Custom.Link.36.Description=MS Edge Link Example",
"Custom.Link.36.Exec=cmd /c start microsoft-edge:https://google.com",
"Custom.Reference.01.Description=<html><center>Example1<br>Second Line</center></html>",
"Custom.Reference.01.Offline=wordpad.exe",
"Custom.Reference.01.Online=cmd /c start chrome.exe https://keep.google.com",
"Custom.Reference.02.Description=Reference2",
"Custom.Reference.02.Offline=cmd /c start powershell.exe -Command \"Write-Host Script2 ; pause\"",
"Custom.Reference.02.Online=cmd /c start chrome.exe https://www.google.com/search?q=2",
"Custom.Reference.03.Description=Reference3",
"Custom.Reference.03.Offline=cmd /c start powershell.exe -Command \"Write-Host Script3 ; pause\"",
"Custom.Reference.03.Online=cmd /c start chrome.exe https://www.google.com/search?q=3",
"Custom.Reference.04.Description=Reference4",
"Custom.Reference.04.Offline=cmd /c start powershell.exe -Command \"Write-Host Script4 ; pause\"",
"Custom.Reference.04.Online=cmd /c start chrome.exe https://www.google.com/search?q=4",
"Custom.Reference.05.Description=Reference5",
"Custom.Reference.05.Offline=cmd /c start powershell.exe -Command \"Write-Host Script5 ; pause\"",
"Custom.Reference.05.Online=cmd /c start chrome.exe https://www.google.com/search?q=5",
"Custom.Reference.06.Description=Reference6",
"Custom.Reference.06.Offline=cmd /c start powershell.exe -Command \"Write-Host Script6 ; pause\"",
"Custom.Reference.06.Online=cmd /c start chrome.exe https://www.google.com/search?q=6",
"Custom.Reference.07.Description=Reference7",
"Custom.Reference.07.Offline=cmd /c start powershell.exe -Command \"Write-Host Script7 ; pause\"",
"Custom.Reference.07.Online=cmd /c start chrome.exe https://www.google.com/search?q=7",
"Custom.Reference.08.Description=Reference8",
"Custom.Reference.08.Offline=cmd /c start powershell.exe -Command \"Write-Host Script8 ; pause\"",
"Custom.Reference.08.Online=cmd /c start chrome.exe https://www.google.com/search?q=8",
"Custom.Reference.09.Description=Reference9",
"Custom.Reference.09.Offline=cmd /c start powershell.exe -Command \"Write-Host Script9 ; pause\"",
"Custom.Reference.09.Online=cmd /c start chrome.exe https://www.google.com/search?q=9",
"Custom.Reference.10.Description=Reference10",
"Custom.Reference.10.Offline=cmd /c start powershell.exe -Command \"Write-Host Script10 ; pause\"",
"Custom.Reference.10.Online=cmd /c start chrome.exe https://www.google.com/search?q=10",
"Custom.Reference.11.Description=Reference11",
"Custom.Reference.11.Offline=cmd /c start powershell.exe -Command \"Write-Host Script11 ; pause\"",
"Custom.Reference.11.Online=cmd /c start chrome.exe https://www.google.com/search?q=11",
"Custom.Reference.12.Description=Reference12",
"Custom.Reference.12.Offline=cmd /c start powershell.exe -Command \"Write-Host Script12 ; pause\"",
"Custom.Reference.12.Online=cmd /c start chrome.exe https://www.google.com/search?q=12",
"Custom.Reference.13.Description=Reference13",
"Custom.Reference.13.Offline=cmd /c start powershell.exe -Command \"Write-Host Script13 ; pause\"",
"Custom.Reference.13.Online=cmd /c start chrome.exe https://www.google.com/search?q=13",
"Custom.Reference.14.Description=Reference14",
"Custom.Reference.14.Offline=cmd /c start powershell.exe -Command \"Write-Host Script14 ; pause\"",
"Custom.Reference.14.Online=cmd /c start chrome.exe https://www.google.com/search?q=14",
"Custom.Reference.15.Description=Reference15",
"Custom.Reference.15.Offline=cmd /c start powershell.exe -Command \"Write-Host Script15 ; pause\"",
"Custom.Reference.15.Online=cmd /c start chrome.exe https://www.google.com/search?q=15",
"Custom.Reference.16.Description=Reference16",
"Custom.Reference.16.Offline=cmd /c start powershell.exe -Command \"Write-Host Script16 ; pause\"",
"Custom.Reference.16.Online=cmd /c start chrome.exe https://www.google.com/search?q=16",
"Custom.Reference.17.Description=Reference17",
"Custom.Reference.17.Offline=cmd /c start powershell.exe -Command \"Write-Host Script17 ; pause\"",
"Custom.Reference.17.Online=cmd /c start chrome.exe https://www.google.com/search?q=17",
"Custom.Reference.18.Description=Reference18",
"Custom.Reference.18.Offline=cmd /c start powershell.exe -Command \"Write-Host Script18 ; pause\"",
"Custom.Reference.18.Online=cmd /c start chrome.exe https://www.google.com/search?q=18",
"Custom.Reference.19.Description=Reference19",
"Custom.Reference.19.Offline=cmd /c start powershell.exe -Command \"Write-Host Script19 ; pause\"",
"Custom.Reference.19.Online=cmd /c start chrome.exe https://www.google.com/search?q=19",
"Custom.Reference.20.Description=Reference20",
"Custom.Reference.20.Offline=cmd /c start powershell.exe -Command \"Write-Host Script20 ; pause\"",
"Custom.Reference.20.Online=cmd /c start chrome.exe https://www.google.com/search?q=20",
"Custom.Reference.21.Description=Reference21",
"Custom.Reference.21.Offline=cmd /c start powershell.exe -Command \"Write-Host Script21 ; pause\"",
"Custom.Reference.21.Online=cmd /c start chrome.exe https://www.google.com/search?q=21",
"Custom.Reference.22.Description=Reference22",
"Custom.Reference.22.Offline=cmd /c start powershell.exe -Command \"Write-Host Script22 ; pause\"",
"Custom.Reference.22.Online=cmd /c start chrome.exe https://www.google.com/search?q=22",
"Custom.Reference.23.Description=Reference23",
"Custom.Reference.23.Offline=cmd /c start powershell.exe -Command \"Write-Host Script23 ; pause\"",
"Custom.Reference.23.Online=cmd /c start chrome.exe https://www.google.com/search?q=23",
"Custom.Reference.24.Description=Reference24",
"Custom.Reference.24.Offline=cmd /c start powershell.exe -Command \"Write-Host Script24 ; pause\"",
"Custom.Reference.24.Online=cmd /c start chrome.exe https://www.google.com/search?q=24",
"Custom.Reference.25.Description=Reference25",
"Custom.Reference.25.Offline=cmd /c start powershell.exe -Command \"Write-Host Script25 ; pause\"",
"Custom.Reference.25.Online=cmd /c start chrome.exe https://www.google.com/search?q=25",
"Custom.Reference.26.Description=Reference26",
"Custom.Reference.26.Offline=cmd /c start powershell.exe -Command \"Write-Host Script26 ; pause\"",
"Custom.Reference.26.Online=cmd /c start chrome.exe https://www.google.com/search?q=26",
"Custom.Reference.27.Description=Reference27",
"Custom.Reference.27.Offline=cmd /c start powershell.exe -Command \"Write-Host Script27 ; pause\"",
"Custom.Reference.27.Online=cmd /c start chrome.exe https://www.google.com/search?q=27",
"Custom.Reference.28.Description=Reference28",
"Custom.Reference.28.Offline=cmd /c start powershell.exe -Command \"Write-Host Script28 ; pause\"",
"Custom.Reference.28.Online=cmd /c start chrome.exe https://www.google.com/search?q=28",
"Custom.Reference.29.Description=Reference29",
"Custom.Reference.29.Offline=cmd /c start powershell.exe -Command \"Write-Host Script29 ; pause\"",
"Custom.Reference.29.Online=cmd /c start chrome.exe https://www.google.com/search?q=29",
"Custom.Reference.30.Description=Reference30",
"Custom.Reference.30.Offline=cmd /c start powershell.exe -Command \"Write-Host Script30 ; pause\"",
"Custom.Reference.30.Online=cmd /c start chrome.exe https://www.google.com/search?q=30",
"Custom.Reference.31.Description=Reference31",
"Custom.Reference.31.Offline=cmd /c start powershell.exe -Command \"Write-Host Script31 ; pause\"",
"Custom.Reference.31.Online=cmd /c start chrome.exe https://www.google.com/search?q=31",
"Custom.Reference.32.Description=Reference32",
"Custom.Reference.32.Offline=cmd /c start powershell.exe -Command \"Write-Host Script32 ; pause\"",
"Custom.Reference.32.Online=cmd /c start chrome.exe https://www.google.com/search?q=32",
"Custom.Reference.33.Description=Reference33",
"Custom.Reference.33.Offline=cmd /c start powershell.exe -Command \"Write-Host Script33 ; pause\"",
"Custom.Reference.33.Online=cmd /c start chrome.exe https://www.google.com/search?q=33",
"Custom.Script.01.Description=Simple Sweep",
"Custom.Script.01.Exec=cmd.exe /c start powershell.exe -ExecutionPolicy Bypass -noexit -Command \"$FirstThreeOctets \\= Read-Host -Prompt 'First Three Octets (Example: 192.168.0)'; $FirstIP \\= Read-Host -Prompt 'Start IP (Example: 1)'; $LastIP \\= Read-Host -Prompt 'End IP (Example: 254)'; $FirstIP..$LastIP | foreach-object { (new-object System.Net.Networkinformation.Ping).Send($FirstThreeOctets + '.' + $_,150) } | where-object {$_.Status -eq 'success'} | select Address; Write-Host 'Done\\! Showing active IPs:'\"",
"Custom.Script.02.Description=netstat -e",
"Custom.Script.02.Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; netstat -e; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"Custom.Script.03.Description=netstat -a",
"Custom.Script.03.Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; netstat -a; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"Custom.Script.04.Description=nbtstat -n",
"Custom.Script.04.Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; nbtstat -n; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"Custom.Script.05.Description=route print",
"Custom.Script.05.Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; route print; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"Custom.Script.06.Description=nslookup",
"Custom.Script.06.Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; nslookup; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"Custom.Script.07.Description=pathping [target_name]",
"Custom.Script.07.Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$TargetName \\= Read-Host -Prompt 'Enter hostname or IP'; $RunCommand = {Clear-Host; pathping $TargetName; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"Custom.Script.08.Description=netstat",
"Custom.Script.08.Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; netstat; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"Custom.Script.09.Description=arp -a",
"Custom.Script.09.Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; arp -a; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"Custom.Script.10.Description=getmac",
"Custom.Script.10.Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; getmac; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"Custom.Script.11.Description=netsh dump",
"Custom.Script.11.Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; netsh dump; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"Custom.Script.12.Description=netsh interface ip show config",
"Custom.Script.12.Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; netsh interface ip show config; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"Custom.Script.13.Description=Test-NetConnection [Destination] -Port [Port]",
"Custom.Script.13.Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$TargetName \\= Read-Host -Prompt 'Enter hostname or IP'; $PortNumber \\= Read-Host -Prompt 'Port to test'; $RunCommand = {Clear-Host; Test-NetConnection $TargetName -Port $PortNumber; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"Custom.Script.14.Description=systeminfo",
"Custom.Script.14.Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; systeminfo; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"Custom.Script.15.Description=systeminfo | find /i \"Boot Time\"",
"Custom.Script.15.Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; systeminfo.exe | find /i \\\\\"Boot Time\\\\\"; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"Custom.Script.16.Description=gwmi win32_operatingsystem | select *",
"Custom.Script.16.Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; gwmi Win32_OperatingSystem | select *; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"Custom.Script.17.Description=wmic product where \"name like '%java%'\" get name",
"Custom.Script.17.Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; wmic product where \\\\\"name like '%java%'\\\\\" get name; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"Custom.Script.18.Description=gwmi win32_computersystem | select *",
"Custom.Script.18.Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; gwmi win32_computersystem | select *; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"Custom.Script.19.Description=gwmi win32_bios | select *",
"Custom.Script.19.Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; gwmi win32_bios | select *; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"Custom.Script.20.Description=gwmi win32_baseboard | select *",
"Custom.Script.20.Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; gwmi win32_baseboard | select *; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"Custom.Script.21.Description=gwmi win32_processor | select *",
"Custom.Script.21.Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; gwmi win32_processor | select *; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"Custom.Script.22.Description=gwmi win32_share | select *",
"Custom.Script.22.Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; gwmi win32_share | select *; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"Custom.Script.23.Description=gwmi win32_logicaldisk | select *",
"Custom.Script.23.Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; gwmi win32_logicaldisk | select *; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"Custom.Script.24.Description=gwmi win32_physicalmemory | select *",
"Custom.Script.24.Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; gwmi win32_physicalmemory | select *; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"Custom.Script.25.Description=gwmi win32_printer | select *",
"Custom.Script.25.Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; gwmi win32_printer | select *; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"Custom.Script.26.Description=gwmi win32_networkadapterconfiguration | select *",
"Custom.Script.26.Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; gwmi win32_networkadapterconfiguration | select *; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"Custom.Script.27.Description=Script27",
"Custom.Script.27.Exec=cmd /c start powershell.exe -Command \"Write-Host Script27 ; pause\"",
"Custom.Script.28.Description=Script28",
"Custom.Script.28.Exec=cmd /c start powershell.exe -Command \"Write-Host Script28 ; pause\"",
"Custom.Script.29.Description=Script29",
"Custom.Script.29.Exec=cmd /c start powershell.exe -Command \"Write-Host Script29 ; pause\"",
"Custom.Script.30.Description=Script30",
"Custom.Script.30.Exec=cmd /c start powershell.exe -Command \"Write-Host Script30 ; pause\"",
"Custom.Script.31.Description=Script31",
"Custom.Script.31.Exec=cmd /c start powershell.exe -Command \"Write-Host Script31 ; pause\"",
"Custom.Script.32.Description=gip",
"Custom.Script.32.Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; gip; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"Custom.Script.33.Description=Get-NetIPConfiguration",
"Custom.Script.33.Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; Get-NetIPConfiguration; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"Custom.Script.34.Description=Get-NetIPAddress",
"Custom.Script.34.Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; Get-NetIPAddress; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"Custom.Script.35.Description=Get-NetAdapter",
"Custom.Script.35.Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; Get-NetAdapter; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"Custom.Script.36.Description=net use",
"Custom.Script.36.Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; net use; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"File.LaunchPad.Local=C:\\\\LaunchPad\\\\old.txt",
"File.LaunchPad.Remote=%USERPROFILE%\\\\Desktop\\\\new.txt",
"File.Update.Script=%USERPROFILE%\\\\Desktop\\\\HelloWorld.ps1",
"NTP.Test.IP=tick.usno.navy.mil",
"Preload.Ping=10.0.",
"Preload.SSH=10.0.",
"SecureCRT.Exec=C:\\\\LaunchPad\\\\Apps\\\\SecureCRT\\\\SecureCRT.exe",
"Setting.Classification=None",
"Setting.ForceRunFromLaunchPadFolder=1",
"Setting.Language=English",
"Setting.LocalPolicyWarning=0",
"Setting.EmailContactIssues=admin@domain.com",
"Setting.PasswordBasedSSHauthDisable=0",
"Setting.PasswordBasedSSHauthDisableAutoReset=0",
"Setting.ShowIpPreferredPrimary=10.2.1.",
"Setting.ShowIpPreferredSecondary=192.168.64.",
"Setting.ShowIpPreferredTertiary=192.168.204.",
"Window.Title=LaunchPad",
"Zip.DefaultDestinationFolder=%USERPROFILE%\\\\Desktop",
"Zip.DefaultSourceFolder=%USERPROFILE%\\\\Desktop");
            Path file = Paths.get(filePropertiesFile.getPath());
            Files.write(file, lines, Charset.forName("UTF-8"));

        } 
        
        //--- Check for local user properties file
        File fileLocalUserPropertiesFile = new File(strPathLaunchPadPersistantPropertiesFile);
        if (!fileLocalUserPropertiesFile.exists()) {
            System.out.println("Local User Properties file not found.");
            //JOptionPane.showMessageDialog(null, "Properties file not found.", "RIP!", JOptionPane.INFORMATION_MESSAGE);
            //System.exit( 0 );
            fileLocalUserPropertiesFile.createNewFile();
            List<String> lines = Arrays.asList(
"Setting.ChangePingOnSessionSelect=0",
"Setting.Language=English",                                
"Setting.TextSize=1");
            Path file = Paths.get(fileLocalUserPropertiesFile.getPath());
            Files.write(file, lines, Charset.forName("UTF-8"));

        } 

        //--- Update or Launch
        //- Items to use
        String StrFileLaunchPadLocal = PropertyHandler.getInstance().getValue("File.LaunchPad.Local");
        StrFileLaunchPadLocal = StrFileLaunchPadLocal.replace("%USERPROFILE%", pathUserProfile);
        System.out.println("Property FileLaunchPadLocal: " + StrFileLaunchPadLocal);
        String StrFileLaunchPadRemote = PropertyHandler.getInstance().getValue("File.LaunchPad.Remote");
        StrFileLaunchPadRemote = StrFileLaunchPadRemote.replace("%USERPROFILE%", pathUserProfile);
        System.out.println("Property FileLaunchPadRemote: " + StrFileLaunchPadRemote);


        //- Set i to -1 - if i becomes greater than 0, update script will run
        int i = -1;
        
        //- Check if files exist
        if(new File(StrFileLaunchPadLocal).isFile()) { 
            
            if(new File(StrFileLaunchPadRemote).isFile()) { 
                Path PathFileLaunchPadLocal = Paths.get(StrFileLaunchPadLocal);
                Path PathFileLaunchPadRemote = Paths.get(StrFileLaunchPadRemote);
                //- Get times on local and remote files
                FileTime FileTimeFileLaunchPadLocal = Files.getLastModifiedTime(PathFileLaunchPadLocal);
                System.out.println("Local Modified: " + FileTimeFileLaunchPadLocal);
                FileTime FileTimeFileLaunchPadRemote = Files.getLastModifiedTime(PathFileLaunchPadRemote);
                System.out.println("Remote Modified: " + FileTimeFileLaunchPadRemote); 
                //- Compare times
                i = FileTimeFileLaunchPadRemote.compareTo(FileTimeFileLaunchPadLocal);
                    System.out.println(i);
            }
            else {
                System.out.println("Property FileLaunchPadRemote: not found");
            }
        }
        else {
            System.out.println("Property FileLaunchPadLocal: not found");
        }
        //- If newer then run update, if older just open App
        if (i > 0) {
            System.out.println("Update status: Update found. Running update stuff.");
            JOptionPane.showMessageDialog(null, "Found a newer version...  Click OK to continue.", "Update Found!", JOptionPane.INFORMATION_MESSAGE);
            String myValue = "cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -File \"" + PropertyHandler.getInstance().getValue("File.Update.Script") + "\"" ;
            System.out.println(myValue);
            try {
                Runtime.getRuntime().exec(myValue);
            }
            catch (IOException e) {
                System.out.println("Oh no!");
                JOptionPane.showMessageDialog(null, "Couldn't run the update script!");
                //--- Launch the form
                LaunchPadForm form = new LaunchPadForm();
                form.setVisible(true);
            }     
        }
        else {
            System.out.println("Update status: No update found.");
            //--- Launch the form
             LaunchPadForm form = new LaunchPadForm();
            form.setVisible(true);
        }
    }
}
