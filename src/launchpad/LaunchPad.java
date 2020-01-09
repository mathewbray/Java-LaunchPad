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
"Button01Icon=onenote",
"Button01StrExec=onenote.exe",
"Button01ToolTip=Notebook",
"Button02Icon=dailyreport",
"Button02StrExec=explorer.exe \"%USERPROFILE%\\\\Desktop\\\\\"",
"Button02ToolTip=Daily Report",
"Button03Icon=kanban",
"Button03StrExec=chrome.exe https://trello.com",
"Button03ToolTip=KanBan",
"Button04Icon=alert",
"Button04StrExec=",
"Button04ToolTip=Master Station Log",
"Button05Icon=user-yellow-home-icon",
"Button05StrExec=",
"Button05ToolTip=",
"Button06Icon=folder-yellow-visiting-icon",
"Button06StrExec=explorer.exe ",
"Button06ToolTip=",
"Button07Icon=folder-yellow-development-icon",
"Button07StrExec=",
"Button07ToolTip=",
"Button08Icon=folder-yellow-git-icon",
"Button08StrExec=",
"Button08ToolTip=",
"Button09Icon=infoblox",
"Button09StrExec=",
"Button09ToolTip=",
"Button10Icon=ciscodarkblue",
"Button10StrExec=",
"Button10ToolTip=",
"Button11Icon=windowsblue",
"Button11StrExec=mstsc /v:SERVERNAME",
"Button11ToolTip=",
"Button12Icon=coffee",
"Button12StrExec=",
"Button12ToolTip=",
"Button13Icon=circuitboard",
"Button13StrExec=",
"Button13ToolTip=",
"Button14Icon=favorites",
"Button14StrExec=chrome.exe",
"Button14ToolTip=",
"Button15Icon=CygWin",
"Button15StrExec=",
"Button15ToolTip=",
"Button16Icon=map",
"Button16StrExec=",
"Button16ToolTip=",
"Button17Icon=backup",
"Button17StrExec=",
"Button17ToolTip=",
"Button18Icon=ipconfig",
"Button18StrExec=cmd.exe /k powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunIPconfig = {Clear-Host; ipconfig /all; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunIPconfig}; &$RunIPconfig}\"",
"Button18ToolTip=",
"Button18exe-CMD-OPTION=cmd.exe /K \"ipconfig & pause\"",
"Button19Icon=winscp",
"Button19StrExec=",
"Button19ToolTip=",
"Button20Icon=wifi",
"Button20StrExec=",
"Button20ToolTip=",
"ButtonExecuteFunction1=HTTPS",
"ButtonExecuteFunction2=RDP",
"ButtonExecuteFunction3=SSH",
"ButtonExecuteFunctionDoubleClick=3",
"ButtonExecuteFunctionOnEnterPress=3",
"CustomLink01Description=Example",
"CustomLink01Exec=chrome.exe https://www.mathewjbray.com",
"CustomLink02Description=",
"CustomLink02Exec=",
"CustomLink03Description=",
"CustomLink03Exec=",
"CustomLink04Description=",
"CustomLink04Exec=",
"CustomLink05Description=",
"CustomLink05Exec=",
"CustomLink06Description=",
"CustomLink06Exec=",
"CustomLink07Description=",
"CustomLink07Exec=",
"CustomLink08Description=",
"CustomLink08Exec=",
"CustomLink09Description=",
"CustomLink09Exec=",
"CustomLink10Description=",
"CustomLink10Exec=",
"CustomLink11Description=",
"CustomLink11Exec=",
"CustomLink12Description=",
"CustomLink12Exec=",
"CustomLink13Description=",
"CustomLink13Exec=",
"CustomLink14Description=",
"CustomLink14Exec=",
"CustomLink15Description=",
"CustomLink15Exec=",
"CustomLink16Description=",
"CustomLink16Exec=",
"CustomLink17Description=",
"CustomLink17Exec=",
"CustomLink18Description=",
"CustomLink18Exec=",
"CustomLink19Description=",
"CustomLink19Exec=",
"CustomLink20Description=",
"CustomLink20Exec=",
"CustomLink21Description=",
"CustomLink21Exec=",
"CustomLink22Description=",
"CustomLink22Exec=",
"CustomLink23Description=",
"CustomLink23Exec=",
"CustomLink24Description=",
"CustomLink24Exec=",
"CustomLink25Description=",
"CustomLink25Exec=",
"CustomLink26Description=",
"CustomLink26Exec=",
"CustomLink27Description=",
"CustomLink27Exec=",
"CustomLink28Description=",
"CustomLink28Exec=",
"CustomLink29Description=",
"CustomLink29Exec=",
"CustomLink30Description=",
"CustomLink30Exec=",
"CustomLink31Description=",
"CustomLink31Exec=",
"CustomLink32Description=",
"CustomLink32Exec=",
"CustomLink33Description=",
"CustomLink33Exec=",
"CustomLink34Description=",
"CustomLink34Exec=",
"CustomLink35Description=",
"CustomLink35Exec=",
"CustomLink36Description=",
"CustomLink36Exec=",
"CustomReference01Description=Example",
"CustomReference01Offline=wordpad.exe",
"CustomReference01Online=chrome.exe https://keep.google.com",
"CustomReference02Description=",
"CustomReference02Offline=",
"CustomReference02Online=",
"CustomReference03Description=",
"CustomReference03Offline=",
"CustomReference03Online=",
"CustomReference04Description=",
"CustomReference04Offline=",
"CustomReference04Online=",
"CustomReference05Description=",
"CustomReference05Offline=",
"CustomReference05Online=",
"CustomReference06Description=",
"CustomReference06Offline=",
"CustomReference06Online=",
"CustomReference07Description=",
"CustomReference07Offline=",
"CustomReference07Online=",
"CustomReference08Description=",
"CustomReference08Offline=",
"CustomReference08Online=",
"CustomReference09Description=",
"CustomReference09Offline=",
"CustomReference09Online=",
"CustomReference10Description=",
"CustomReference10Offline=",
"CustomReference10Online=",
"CustomReference11Description=",
"CustomReference11Offline=",
"CustomReference11Online=",
"CustomReference12Description=",
"CustomReference12Offline=",
"CustomReference12Online=",
"CustomReference13Description=",
"CustomReference13Offline=",
"CustomReference13Online=",
"CustomReference14Description=",
"CustomReference14Offline=",
"CustomReference14Online=",
"CustomReference15Description=",
"CustomReference15Offline=",
"CustomReference15Online=",
"CustomReference16Description=",
"CustomReference16Offline=",
"CustomReference16Online=",
"CustomReference17Description=",
"CustomReference17Offline=",
"CustomReference17Online=",
"CustomReference18Description=",
"CustomReference18Offline=",
"CustomReference18Online=",
"CustomReference19Description=",
"CustomReference19Offline=",
"CustomReference19Online=",
"CustomReference20Description=",
"CustomReference20Offline=",
"CustomReference20Online=",
"CustomReference21Description=",
"CustomReference21Offline=",
"CustomReference21Online=",
"CustomReference22Description=",
"CustomReference22Offline=",
"CustomReference22Online=",
"CustomReference23Description=",
"CustomReference23Offline=",
"CustomReference23Online=",
"CustomReference24Description=",
"CustomReference24Offline=",
"CustomReference24Online=",
"CustomReference25Description=",
"CustomReference25Offline=",
"CustomReference25Online=",
"CustomReference26Description=",
"CustomReference26Offline=",
"CustomReference26Online=",
"CustomReference27Description=",
"CustomReference27Offline=",
"CustomReference27Online=",
"CustomReference28Description=",
"CustomReference28Offline=",
"CustomReference28Online=",
"CustomReference29Description=",
"CustomReference29Offline=",
"CustomReference29Online=",
"CustomReference30Description=",
"CustomReference30Offline=",
"CustomReference30Online=",
"CustomReference31Description=",
"CustomReference31Offline=",
"CustomReference31Online=",
"CustomReference32Description=",
"CustomReference32Offline=",
"CustomReference32Online=",
"CustomReference33Description=",
"CustomReference33Offline=",
"CustomReference33Online=",
"CustomScript01Description=Example",
"CustomScript01Exec=cmd.exe /k powershell.exe -ExecutionPolicy Bypass -noexit -Command \"$FirstThreeOctets = Read-Host -Prompt 'First Three Octets (192.168.0)'; $FirstIP = Read-Host -Prompt 'Start IP (1)'; $LastIP = Read-Host -Prompt 'End IP (254)'; $FirstIP..$LastIP | foreach-object { (new-object System.Net.Networkinformation.Ping).Send($FirstThreeOctets + '.' + $_,150) } | where-object {$_.Status -eq 'success'} | select Address; Write-Host 'Done!'\"",
"CustomScript02Description=",
"CustomScript02Exec=",
"CustomScript03Description=",
"CustomScript03Exec=",
"CustomScript04Description=",
"CustomScript04Exec=",
"CustomScript05Description=",
"CustomScript05Exec=",
"CustomScript06Description=",
"CustomScript06Exec=",
"CustomScript07Description=",
"CustomScript07Exec=",
"CustomScript08Description=",
"CustomScript08Exec=",
"CustomScript09Description=",
"CustomScript09Exec=",
"CustomScript10Description=",
"CustomScript10Exec=",
"CustomScript11Description=",
"CustomScript11Exec=",
"CustomScript12Description=",
"CustomScript12Exec=",
"CustomScript13Description=",
"CustomScript13Exec=",
"CustomScript14Description=",
"CustomScript14Exec=",
"CustomScript15Description=",
"CustomScript15Exec=",
"CustomScript16Description=",
"CustomScript16Exec=",
"CustomScript17Description=",
"CustomScript17Exec=",
"CustomScript18Description=",
"CustomScript18Exec=",
"CustomScript19Description=",
"CustomScript19Exec=",
"CustomScript20Description=",
"CustomScript20Exec=",
"CustomScript21Description=",
"CustomScript21Exec=",
"CustomScript22Description=",
"CustomScript22Exec=",
"CustomScript23Description=",
"CustomScript23Exec=",
"CustomScript24Description=",
"CustomScript24Exec=",
"CustomScript25Description=",
"CustomScript25Exec=",
"CustomScript26Description=",
"CustomScript26Exec=",
"CustomScript27Description=",
"CustomScript27Exec=",
"CustomScript28Description=",
"CustomScript28Exec=",
"CustomScript29Description=",
"CustomScript29Exec=",
"CustomScript30Description=",
"CustomScript30Exec=",
"CustomScript31Description=",
"CustomScript31Exec=",
"CustomScript32Description=",
"CustomScript32Exec=",
"CustomScript33Description=",
"CustomScript33Exec=",
"CustomScript34Description=",
"CustomScript34Exec=",
"CustomScript35Description=",
"CustomScript35Exec=",
"CustomScript36Description=",
"CustomScript36Exec=",
"FileLaunchPadLocal=%USERPROFILE%\\\\Desktop\\\\LaunchPad\\\\LaunchPad.jar",
"FileLaunchPadRemote=%USERPROFILE%\\\\Desktop\\\\new.txt",
"FileUpdateScript=%USERPROFILE%\\\\Desktop\\\\HelloWorld.ps1",
"PreloadPing=10.0.",
"PreloadSSH=10.0.",
"SecureCRTexe=%USERPROFILE%\\\\AppData\\\\Local\\\\VanDyke Software\\\\SecureCRT\\\\SecureCRT.exe",
"SettingClassification=",
"WindowTitle=LaunchPad Pre-Alpha",
"ZipDefaultDestinationFolder=%USERPROFILE%\\\\Desktop",
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
