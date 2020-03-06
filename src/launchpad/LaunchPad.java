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
"Button01Icon=onenote.png",
"Button01StrExec=cmd /c start onenote.exe",
"Button01ToolTip=Notebook",
"Button02Icon=keep.png",
"Button02StrExec=cmd /c start chrome.exe https://keep.google.com",
"Button02ToolTip=Keep",
"Button03Icon=trello.png",
"Button03StrExec=cmd /c start chrome.exe https://trello.com",
"Button03ToolTip=Trello",
"Button04Icon=gmail.png",
"Button04StrExec=cmd /c start chrome.exe https://mail.google.com",
"Button04ToolTip=Gmail",
"Button05Icon=folder-yellow-home.png",
"Button05StrExec=cmd /c start explorer.exe \"%USERPROFILE%\\\\Desktop\\\\\"",
"Button05ToolTip=Home",
"Button06Icon=folder-yellow-development.png",
"Button06StrExec=",
"Button06ToolTip=Development",
"Button07Icon=folder-yellow-favorites-icon.png",
"Button07StrExec=",
"Button07ToolTip=",
"Button08Icon=folder-yellow-important-icon.png",
"Button08StrExec=",
"Button08ToolTip=",
"Button09Icon=infoblox.png",
"Button09StrExec=",
"Button09ToolTip=",
"Button10Icon=ciscoapicem.png",
"Button10StrExec=",
"Button10ToolTip=",
"Button11Icon=alert.png",
"Button11StrExec=",
"Button11ToolTip=",
"Button12Icon=coffee.png",
"Button12StrExec=",
"Button12ToolTip=",
"Button13Icon=prtg.png",
"Button13StrExec=",
"Button13ToolTip=",
"Button14Icon=favorites.png",
"Button14StrExec=chrome.exe",
"Button14ToolTip=",
"Button15Icon=apps.png",
"Button15StrExec=",
"Button15ToolTip=",
"Button16Icon=map.png",
"Button16StrExec=",
"Button16ToolTip=",
"Button17Icon=backup2.png",
"Button17StrExec=",
"Button17ToolTip=",
"Button18Icon=ipconfig.png",
"Button18StrExec=cmd.exe /c start powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunIPconfig \\= {Clear-Host; ipconfig /all; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunIPconfig}; &$RunIPconfig}\"",
"Button18ToolTip=",
"Button19Icon=winscp2.png",
"Button19StrExec=",
"Button19ToolTip=",
"Button20Icon=timezone.png",
"Button20StrExec=",
"Button20ToolTip=STIG Viewer",
"Button21Icon=vmware2.png",
"Button21StrExec=",
"Button21ToolTip=VMware vCenter",
"Button22Icon=windows5.png",
"Button22StrExec=mstsc /v:SERVERNAME",
"Button22ToolTip=Windows Server",
"Button23Icon=splunk.png",
"Button23StrExec=",
"Button23ToolTip=Splunk",
"Button24Icon=solarwinds.png",
"Button24StrExec=",
"Button24ToolTip=Solarwinds",
"ButtonExecuteFunctionDoubleClick=SSH",
"ButtonExecuteFunctionOnEnterPress=SSH",
"CustomLink01Description=Google",
"CustomLink01Exec=cmd /c start chrome.exe https://www.google.com/",
"CustomLink02Description=Stack Overflow",
"CustomLink02Exec=cmd /c start chrome.exe https://stackoverflow.com/",
"CustomLink03Description=GitHub",
"CustomLink03Exec=cmd /c start chrome.exe https://github.com/",
"CustomLink04Description=Reddit",
"CustomLink04Exec=cmd /c start chrome.exe https://www.reddit.com/r/worldnews/",
"CustomLink05Description=Trello",
"CustomLink05Exec=cmd /c start chrome.exe https://trello.com/",
"CustomLink06Description=RegEx101",
"CustomLink06Exec=cmd /c start chrome.exe https://regex101.com/",
"CustomLink07Description=Pearson Vue",
"CustomLink07Exec=cmd /c start chrome.exe https://home.pearsonvue.com/",
"CustomLink08Description=Wikipedia",
"CustomLink08Exec=cmd /c start chrome.exe https://en.wikipedia.org/wiki/Wikipedia:Wiki_Game",
"CustomLink09Description=Base64 Image Convert",
"CustomLink09Exec=cmd /c start chrome.exe https://www.base64-image.de/",
"CustomLink10Description=Typetester",
"CustomLink10Exec=cmd /c start chrome.exe http://classic.typetester.org/",
"CustomLink11Description=ISC2 CISSP Login",
"CustomLink11Exec=cmd /c start chrome.exe https://www.isc2.org/ISC2Login",
"CustomLink12Description=Cybrary",
"CustomLink12Exec=cmd /c start chrome.exe https://www.cybrary.it/",
"CustomLink13Description=CompTIA Login",
"CustomLink13Exec=cmd /c start chrome.exe https://www.comptia.org/members/my-dashboard",
"CustomLink14Description=ISC2 CPE",
"CustomLink14Exec=cmd /c start chrome.exe https://live.blueskybroadcast.com/bsb/client/CL_DEFAULT.asp?Client=411114&title=Home",
"CustomLink15Description=<html><center>DoD Cyber Exchange<br>DISA Home</center></html>",
"CustomLink15Exec=cmd /c start chrome.exe https://public.cyber.mil/",
"CustomLink16Description=<html><center>DoD Cyber Exchange<br>DISA STIGs</center></html>",
"CustomLink16Exec=cmd /c start chrome.exe https://public.cyber.mil/stigs/",
"CustomLink17Description=DoD SAFE",
"CustomLink17Exec=cmd /c start chrome.exe https://safe.apps.mil/",
"CustomLink18Description=Internet Storm Center",
"CustomLink18Exec=cmd /c start chrome.exe https://isc.sans.edu/",
"CustomLink19Description=Google Security Blog",
"CustomLink19Exec=cmd /c start chrome.exe https://security.googleblog.com/",
"CustomLink20Description=CISA",
"CustomLink20Exec=cmd /c start chrome.exe https://www.us-cert.gov/",
"CustomLink21Description=Reddit - /r/netsec",
"CustomLink21Exec=cmd /c start chrome.exe https://www.reddit.com/r/netsec/",
"CustomLink22Description=Link22",
"CustomLink22Exec=cmd /c start powershell.exe -Command \"Write-Host Link22 ; pause\"",
"CustomLink23Description=Link23",
"CustomLink23Exec=cmd /c start powershell.exe -Command \"Write-Host Link23 ; pause\"",
"CustomLink24Description=Link24",
"CustomLink24Exec=cmd /c start powershell.exe -Command \"Write-Host Link24 ; pause\"",
"CustomLink25Description=Link25",
"CustomLink25Exec=cmd /c start powershell.exe -Command \"Write-Host Link25 ; pause\"",
"CustomLink26Description=Link26",
"CustomLink26Exec=cmd /c start powershell.exe -Command \"Write-Host Link26 ; pause\"",
"CustomLink27Description=Link27",
"CustomLink27Exec=cmd /c start powershell.exe -Command \"Write-Host Link27 ; pause\"",
"CustomLink28Description=Link28",
"CustomLink28Exec=cmd /c start powershell.exe -Command \"Write-Host Link28 ; pause\"",
"CustomLink29Description=Link29",
"CustomLink29Exec=cmd /c start powershell.exe -Command \"Write-Host Link29 ; pause\"",
"CustomLink30Description=Link30",
"CustomLink30Exec=cmd /c start powershell.exe -Command \"Write-Host Link30 ; pause\"",
"CustomLink31Description=Link31",
"CustomLink31Exec=cmd /c start powershell.exe -Command \"Write-Host Link31 ; pause\"",
"CustomLink32Description=Link32",
"CustomLink32Exec=cmd /c start powershell.exe -Command \"Write-Host Link32 ; pause\"",
"CustomLink33Description=Link33",
"CustomLink33Exec=cmd /c start powershell.exe -Command \"Write-Host Link33 ; pause\"",
"CustomLink34Description=Link34",
"CustomLink34Exec=cmd /c start powershell.exe -Command \"Write-Host Link34 ; pause\"",
"CustomLink35Description=Link35",
"CustomLink35Exec=cmd /c start powershell.exe -Command \"Write-Host Link35 ; pause\"",
"CustomLink36Description=MS Edge Link Example",
"CustomLink36Exec=cmd /c start microsoft-edge:https://google.com",
"CustomReference01Description=<html><center>Example1<br>Second Line</center></html>",
"CustomReference01Offline=wordpad.exe",
"CustomReference01Online=cmd /c start chrome.exe https://keep.google.com",
"CustomReference02Description=Reference2",
"CustomReference02Offline=cmd /c start powershell.exe -Command \"Write-Host Script2 ; pause\"",
"CustomReference02Online=cmd /c start chrome.exe https://www.google.com/search?q=2",
"CustomReference03Description=Reference3",
"CustomReference03Offline=cmd /c start powershell.exe -Command \"Write-Host Script3 ; pause\"",
"CustomReference03Online=cmd /c start chrome.exe https://www.google.com/search?q=3",
"CustomReference04Description=Reference4",
"CustomReference04Offline=cmd /c start powershell.exe -Command \"Write-Host Script4 ; pause\"",
"CustomReference04Online=cmd /c start chrome.exe https://www.google.com/search?q=4",
"CustomReference05Description=Reference5",
"CustomReference05Offline=cmd /c start powershell.exe -Command \"Write-Host Script5 ; pause\"",
"CustomReference05Online=cmd /c start chrome.exe https://www.google.com/search?q=5",
"CustomReference06Description=Reference6",
"CustomReference06Offline=cmd /c start powershell.exe -Command \"Write-Host Script6 ; pause\"",
"CustomReference06Online=cmd /c start chrome.exe https://www.google.com/search?q=6",
"CustomReference07Description=Reference7",
"CustomReference07Offline=cmd /c start powershell.exe -Command \"Write-Host Script7 ; pause\"",
"CustomReference07Online=cmd /c start chrome.exe https://www.google.com/search?q=7",
"CustomReference08Description=Reference8",
"CustomReference08Offline=cmd /c start powershell.exe -Command \"Write-Host Script8 ; pause\"",
"CustomReference08Online=cmd /c start chrome.exe https://www.google.com/search?q=8",
"CustomReference09Description=Reference9",
"CustomReference09Offline=cmd /c start powershell.exe -Command \"Write-Host Script9 ; pause\"",
"CustomReference09Online=cmd /c start chrome.exe https://www.google.com/search?q=9",
"CustomReference10Description=Reference10",
"CustomReference10Offline=cmd /c start powershell.exe -Command \"Write-Host Script10 ; pause\"",
"CustomReference10Online=cmd /c start chrome.exe https://www.google.com/search?q=10",
"CustomReference11Description=Reference11",
"CustomReference11Offline=cmd /c start powershell.exe -Command \"Write-Host Script11 ; pause\"",
"CustomReference11Online=cmd /c start chrome.exe https://www.google.com/search?q=11",
"CustomReference12Description=Reference12",
"CustomReference12Offline=cmd /c start powershell.exe -Command \"Write-Host Script12 ; pause\"",
"CustomReference12Online=cmd /c start chrome.exe https://www.google.com/search?q=12",
"CustomReference13Description=Reference13",
"CustomReference13Offline=cmd /c start powershell.exe -Command \"Write-Host Script13 ; pause\"",
"CustomReference13Online=cmd /c start chrome.exe https://www.google.com/search?q=13",
"CustomReference14Description=Reference14",
"CustomReference14Offline=cmd /c start powershell.exe -Command \"Write-Host Script14 ; pause\"",
"CustomReference14Online=cmd /c start chrome.exe https://www.google.com/search?q=14",
"CustomReference15Description=Reference15",
"CustomReference15Offline=cmd /c start powershell.exe -Command \"Write-Host Script15 ; pause\"",
"CustomReference15Online=cmd /c start chrome.exe https://www.google.com/search?q=15",
"CustomReference16Description=Reference16",
"CustomReference16Offline=cmd /c start powershell.exe -Command \"Write-Host Script16 ; pause\"",
"CustomReference16Online=cmd /c start chrome.exe https://www.google.com/search?q=16",
"CustomReference17Description=Reference17",
"CustomReference17Offline=cmd /c start powershell.exe -Command \"Write-Host Script17 ; pause\"",
"CustomReference17Online=cmd /c start chrome.exe https://www.google.com/search?q=17",
"CustomReference18Description=Reference18",
"CustomReference18Offline=cmd /c start powershell.exe -Command \"Write-Host Script18 ; pause\"",
"CustomReference18Online=cmd /c start chrome.exe https://www.google.com/search?q=18",
"CustomReference19Description=Reference19",
"CustomReference19Offline=cmd /c start powershell.exe -Command \"Write-Host Script19 ; pause\"",
"CustomReference19Online=cmd /c start chrome.exe https://www.google.com/search?q=19",
"CustomReference20Description=Reference20",
"CustomReference20Offline=cmd /c start powershell.exe -Command \"Write-Host Script20 ; pause\"",
"CustomReference20Online=cmd /c start chrome.exe https://www.google.com/search?q=20",
"CustomReference21Description=Reference21",
"CustomReference21Offline=cmd /c start powershell.exe -Command \"Write-Host Script21 ; pause\"",
"CustomReference21Online=cmd /c start chrome.exe https://www.google.com/search?q=21",
"CustomReference22Description=Reference22",
"CustomReference22Offline=cmd /c start powershell.exe -Command \"Write-Host Script22 ; pause\"",
"CustomReference22Online=cmd /c start chrome.exe https://www.google.com/search?q=22",
"CustomReference23Description=Reference23",
"CustomReference23Offline=cmd /c start powershell.exe -Command \"Write-Host Script23 ; pause\"",
"CustomReference23Online=cmd /c start chrome.exe https://www.google.com/search?q=23",
"CustomReference24Description=Reference24",
"CustomReference24Offline=cmd /c start powershell.exe -Command \"Write-Host Script24 ; pause\"",
"CustomReference24Online=cmd /c start chrome.exe https://www.google.com/search?q=24",
"CustomReference25Description=Reference25",
"CustomReference25Offline=cmd /c start powershell.exe -Command \"Write-Host Script25 ; pause\"",
"CustomReference25Online=cmd /c start chrome.exe https://www.google.com/search?q=25",
"CustomReference26Description=Reference26",
"CustomReference26Offline=cmd /c start powershell.exe -Command \"Write-Host Script26 ; pause\"",
"CustomReference26Online=cmd /c start chrome.exe https://www.google.com/search?q=26",
"CustomReference27Description=Reference27",
"CustomReference27Offline=cmd /c start powershell.exe -Command \"Write-Host Script27 ; pause\"",
"CustomReference27Online=cmd /c start chrome.exe https://www.google.com/search?q=27",
"CustomReference28Description=Reference28",
"CustomReference28Offline=cmd /c start powershell.exe -Command \"Write-Host Script28 ; pause\"",
"CustomReference28Online=cmd /c start chrome.exe https://www.google.com/search?q=28",
"CustomReference29Description=Reference29",
"CustomReference29Offline=cmd /c start powershell.exe -Command \"Write-Host Script29 ; pause\"",
"CustomReference29Online=cmd /c start chrome.exe https://www.google.com/search?q=29",
"CustomReference30Description=Reference30",
"CustomReference30Offline=cmd /c start powershell.exe -Command \"Write-Host Script30 ; pause\"",
"CustomReference30Online=cmd /c start chrome.exe https://www.google.com/search?q=30",
"CustomReference31Description=Reference31",
"CustomReference31Offline=cmd /c start powershell.exe -Command \"Write-Host Script31 ; pause\"",
"CustomReference31Online=cmd /c start chrome.exe https://www.google.com/search?q=31",
"CustomReference32Description=Reference32",
"CustomReference32Offline=cmd /c start powershell.exe -Command \"Write-Host Script32 ; pause\"",
"CustomReference32Online=cmd /c start chrome.exe https://www.google.com/search?q=32",
"CustomReference33Description=Reference33",
"CustomReference33Offline=cmd /c start powershell.exe -Command \"Write-Host Script33 ; pause\"",
"CustomReference33Online=cmd /c start chrome.exe https://www.google.com/search?q=33",
"CustomScript01Description=Simple Sweep",
"CustomScript01Exec=cmd.exe /c start powershell.exe -ExecutionPolicy Bypass -noexit -Command \"$FirstThreeOctets \\= Read-Host -Prompt 'First Three Octets (Example: 192.168.0)'; $FirstIP \\= Read-Host -Prompt 'Start IP (Example: 1)'; $LastIP \\= Read-Host -Prompt 'End IP (Example: 254)'; $FirstIP..$LastIP | foreach-object { (new-object System.Net.Networkinformation.Ping).Send($FirstThreeOctets + '.' + $_,150) } | where-object {$_.Status -eq 'success'} | select Address; Write-Host 'Done\\! Showing active IPs:'\"",
"CustomScript02Description=netstat -e",
"CustomScript02Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; netstat -e; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"CustomScript03Description=netstat -a",
"CustomScript03Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; netstat -a; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"CustomScript04Description=nbtstat -n",
"CustomScript04Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; nbtstat -n; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"CustomScript05Description=route print",
"CustomScript05Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; route print; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"CustomScript06Description=nslookup",
"CustomScript06Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; nslookup; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"CustomScript07Description=pathping [target_name]",
"CustomScript07Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$TargetName \\= Read-Host -Prompt 'Enter hostname or IP'; $RunCommand = {Clear-Host; pathping $TargetName; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"CustomScript08Description=netstat",
"CustomScript08Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; netstat; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"CustomScript09Description=arp -a",
"CustomScript09Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; arp -a; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"CustomScript10Description=getmac",
"CustomScript10Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; getmac; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"CustomScript11Description=netsh dump",
"CustomScript11Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; netsh dump; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"CustomScript12Description=netsh interface ip show config",
"CustomScript12Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; netsh interface ip show config; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"CustomScript13Description=Test-NetConnection [Destination] -Port [Port]",
"CustomScript13Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$TargetName \\= Read-Host -Prompt 'Enter hostname or IP'; $PortNumber \\= Read-Host -Prompt 'Port to test'; $RunCommand = {Clear-Host; Test-NetConnection $TargetName -Port $PortNumber; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"CustomScript14Description=systeminfo",
"CustomScript14Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; systeminfo; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"CustomScript15Description=systeminfo | find /i \"Boot Time\"",
"CustomScript15Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; systeminfo.exe | find /i \\\\\"Boot Time\\\\\"; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"CustomScript16Description=gwmi win32_operatingsystem | select *",
"CustomScript16Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; gwmi Win32_OperatingSystem | select *; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"CustomScript17Description=wmic product where \"name like '%java%'\" get name",
"CustomScript17Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; wmic product where \\\\\"name like '%java%'\\\\\" get name; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"CustomScript18Description=gwmi win32_computersystem | select *",
"CustomScript18Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; gwmi win32_computersystem | select *; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"CustomScript19Description=gwmi win32_bios | select *",
"CustomScript19Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; gwmi win32_bios | select *; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"CustomScript20Description=gwmi win32_baseboard | select *",
"CustomScript20Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; gwmi win32_baseboard | select *; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"CustomScript21Description=gwmi win32_processor | select *",
"CustomScript21Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; gwmi win32_processor | select *; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"CustomScript22Description=gwmi win32_share | select *",
"CustomScript22Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; gwmi win32_share | select *; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"CustomScript23Description=gwmi win32_logicaldisk | select *",
"CustomScript23Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; gwmi win32_logicaldisk | select *; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"CustomScript24Description=gwmi win32_physicalmemory | select *",
"CustomScript24Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; gwmi win32_physicalmemory | select *; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"CustomScript25Description=gwmi win32_printer | select *",
"CustomScript25Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; gwmi win32_printer | select *; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"CustomScript26Description=gwmi win32_networkadapterconfiguration | select *",
"CustomScript26Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; gwmi win32_networkadapterconfiguration | select *; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"CustomScript27Description=Script27",
"CustomScript27Exec=cmd /c start powershell.exe -Command \"Write-Host Script27 ; pause\"",
"CustomScript28Description=Script28",
"CustomScript28Exec=cmd /c start powershell.exe -Command \"Write-Host Script28 ; pause\"",
"CustomScript29Description=Script29",
"CustomScript29Exec=cmd /c start powershell.exe -Command \"Write-Host Script29 ; pause\"",
"CustomScript30Description=Script30",
"CustomScript30Exec=cmd /c start powershell.exe -Command \"Write-Host Script30 ; pause\"",
"CustomScript31Description=Script31",
"CustomScript31Exec=cmd /c start powershell.exe -Command \"Write-Host Script31 ; pause\"",
"CustomScript32Description=gip",
"CustomScript32Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; gip; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"CustomScript33Description=Get-NetIPConfiguration",
"CustomScript33Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; Get-NetIPConfiguration; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"CustomScript34Description=Get-NetIPAddress",
"CustomScript34Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; Get-NetIPAddress; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"CustomScript35Description=Get-NetAdapter",
"CustomScript35Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; Get-NetAdapter; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"CustomScript36Description=net use",
"CustomScript36Exec=cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunCommand = {Clear-Host; net use; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunCommand}; &$RunCommand}\"",
"FileLaunchPadLocal=C:\\\\LaunchPad\\\\old.txt",
"FileLaunchPadRemote=%USERPROFILE%\\\\Desktop\\\\new.txt",
"FileUpdateScript=%USERPROFILE%\\\\Desktop\\\\HelloWorld.ps1",
"NtpTestIP=tick.usno.navy.mil",
"PreloadPing=10.0.",
"PreloadSSH=10.0.",
"SecureCRTexe=C:\\\\LaunchPad\\\\Apps\\\\SecureCRT\\\\SecureCRT.exe",
"SettingClassification=None",
"SettingForceRunFromLaunchPadFolder=1",
"SettingLanguage=English",
"SettingLocalPolicyWarning=0",
"SettingEmailContactIssues=admin@domain.com",
"SettingPasswordBasedSSHauthDisable=0",
"SettingPasswordBasedSSHauthDisableAutoReset=0",
"SettingShowIpPreferredPrimary=10.2.1.",
"SettingShowIpPreferredSecondary=192.168.64.",
"SettingShowIpPreferredTertiary=192.168.204.",
"WindowTitle=LaunchPad",
"ZipDefaultDestinationFolder=%USERPROFILE%\\\\Desktop",
"ZipDefaultSourceFolder=%USERPROFILE%\\\\Desktop");
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
"SettingChangePingOnSessionSelect=0",
"SettingLanguage=English",                                
"SettingTextSize=1");
            Path file = Paths.get(fileLocalUserPropertiesFile.getPath());
            Files.write(file, lines, Charset.forName("UTF-8"));

        } 

        //--- Update or Launch
        //- Items to use
        String StrFileLaunchPadLocal = PropertyHandler.getInstance().getValue("FileLaunchPadLocal");
        StrFileLaunchPadLocal = StrFileLaunchPadLocal.replace("%USERPROFILE%", pathUserProfile);
        System.out.println("Property FileLaunchPadLocal: " + StrFileLaunchPadLocal);
        String StrFileLaunchPadRemote = PropertyHandler.getInstance().getValue("FileLaunchPadRemote");
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
            String myValue = "cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -File \"" + PropertyHandler.getInstance().getValue("FileUpdateScript") + "\"" ;
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
