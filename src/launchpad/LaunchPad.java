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
        String strPathLaunchPadFolder = pathUserProfile + "\\.launchpad";
        String strSessionListFavoritesFolder = strPathLaunchPadFolder + "\\Favorites";
        String strPathPropertiesFile = strPathLaunchPadFolder + "\\launchpad.properties";

      
        //--- Create folders
        new File(strPathLaunchPadFolder).mkdirs();
        new File(strSessionListFavoritesFolder).mkdirs();

        //--- Check for properties file
        File filePropertiesFile = new File(strPathPropertiesFile);
        if (!filePropertiesFile.exists()) {
            System.out.println("Properties file not found.");
            //JOptionPane.showMessageDialog(null, "Properties file not found.", "RIP!", JOptionPane.INFORMATION_MESSAGE);
            //System.exit( 0 );
            filePropertiesFile.createNewFile();
            List<String> lines = Arrays.asList(
"WindowTitle=LaunchPad Pre-Alpha",                                
"PreloadSSH=10.0.",
"PreloadPing=10.0.",
"Button01ToolTip=Notebook",
"Button01Icon=onenote",
"Button01StrExec=onenote.exe",
"Button02Icon=dailyreport",
"Button02ToolTip=Daily Report",
"Button02StrExec=explorer.exe \"%USERPROFILE%\\\\Desktop\\\\\"",
"Button03Icon=kanban",
"Button03ToolTip=KanBan",
"Button03StrExec=chrome.exe https://trello.com",
"Button04Icon=alert",
"Button04StrExec=explorer.exe \"%USERPROFILE%\\\\Desktop\\\\\"",
"Button04ToolTip=Master Station Log",
"Button05Icon=user-yellow-home-icon",
"Button05StrExec=explorer.exe ",
"Button06Icon=folder-yellow-visiting-icon",
"Button06StrExec=explorer.exe ",
"Button07Icon=folder-yellow-development-icon",
"Button07StrExec=explorer.exe \"%USERPROFILE%\\\\Desktop\\\\\"",
"Button08Icon=folder-yellow-git-icon",
"Button08StrExec=explorer.exe \"%USERPROFILE%\\\\Desktop\\\\\"",
"Button09Icon=infoblox",
"Button09StrExec=chrome.exe https://infoblox.com/",
"Button10Icon=ciscodarkblue",
"Button10StrExec=chrome.exe https://cisco.com/",
"Button11Icon=windowsblue",
"Button11StrExec=mstsc /v:SERVERNAME",
"Button12Icon=coffee",
"Button12StrExec=\"\"",
"Button13Icon=circuitboard",
"Button13StrExec=chrome.exe https://www.wireshark.org/tools/oui-lookup.html",
"Button14Icon=favorites",
"Button14StrExec=chrome.exe",
"Button15Icon=CygWin",
"Button15StrExec=",
"Button16Icon=map",
"Button16StrExec=explorer.exe \"%USERPROFILE%\\\\Desktop\\\\\"",
"Button17Icon=backup",
"Button17StrExec=",
"Button18Icon=ipconfig",
"Button18exe-CMD-OPTION=cmd.exe /K \"ipconfig & pause\"",
"Button18StrExec=cmd.exe /k powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunIPconfig = {Clear-Host; ipconfig /all; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunIPconfig}; &$RunIPconfig}\"",
"Button19Icon=winscp",
"Button19StrExec=",
"Button20Icon=wifi",
"Button20StrExec=\"%USERPROFILE%\\\\Desktop\\\\\"",
"ButtonExecuteFunction1=HTTPS",
"ButtonExecuteFunction2=RDP",
"ButtonExecuteFunction3=SSH",
"ButtonExecuteFunctionDoubleClick=3",
"ButtonExecuteFunctionOnEnterPress=3",
"FileLaunchPadLocal=%USERPROFILE%\\\\Desktop\\\\LaunchPad\\\\LaunchPad.jar",
"FileLaunchPadRemote=%USERPROFILE%\\\\Desktop\\\\new.txt",
"FileUpdateScript=%USERPROFILE%\\\\Desktop\\\\HelloWorld.ps1",
"ScriptBackupShare=%USERPROFILE%\\\\Desktop\\\\HelloWorld.ps1",
"ScriptStandaloneSync=%USERPROFILE%\\\\Desktop\\\\HelloWorld.ps1",
"ScriptCustom01=cmd.exe /k powershell.exe -ExecutionPolicy Bypass -noexit -Command \"$FirstThreeOctets = Read-Host -Prompt 'First Three Octets (192.168.0)'; $FirstIP = Read-Host -Prompt 'Start IP (1)'; $LastIP = Read-Host -Prompt 'End IP (254)'; $FirstIP..$LastIP | foreach-object { (new-object System.Net.Networkinformation.Ping).Send($FirstThreeOctets + '.' + $_,150) } | where-object {$_.Status -eq 'success'} | select Address; Write-Host 'Done!'\"",
"ScriptCustom01Text=Powershell Ping Sweep",
"SecureCRT=C:\\\\Program Files\\\\SecureCRT x64\\\\SecureCRT\\\\SecureCRT.exe",
//"PuTTYexe=%USERPROFILE%\\\\Desktop\\\\kitty.exe",
"ZipDefaultSourceFolder=%USERPROFILE%\\\\Desktop");
            Path file = Paths.get(filePropertiesFile.getPath());
            Files.write(file, lines, Charset.forName("UTF-8"));

        } 
        
        //--- Check for local user properties file
        File fileLocalUserPropertiesFile = new File(strPathPropertiesFile);
        if (!fileLocalUserPropertiesFile.exists()) {
            System.out.println("Local User Properties file not found.");
            //JOptionPane.showMessageDialog(null, "Properties file not found.", "RIP!", JOptionPane.INFORMATION_MESSAGE);
            //System.exit( 0 );
            fileLocalUserPropertiesFile.createNewFile();
            List<String> lines = Arrays.asList(
"TextSize=3",                                
"ChatNickname=");
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
                System.out.println("Property:FileLaunchPadRemote not found");
            }
        }
        else {
            System.out.println("Property:FileLaunchPadLocal not found");
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
                System.out.println("HEY Buddy ! U r Doing Something Wrong ");
                JOptionPane.showMessageDialog(null, "Something is wrong!");
            }     
        }
        else {
            System.out.println("Update status: No update found.");
            //--- Launch the form
             LaunchPadForm form = new LaunchPadForm();
            form.setVisible(true);
        }
    }
      
        //                                            if (!form.isFocused()) {
    //                                                form.setVisible(false);
    //                                                form.setVisible(true);
    //                                            }
}
