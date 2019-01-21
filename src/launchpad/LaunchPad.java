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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import javax.swing.JOptionPane;

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
     */
    public static void main(String[] args) throws IOException, FileNotFoundException, URISyntaxException, AWTException, InterruptedException {
        // TODO code application logic here
        //--- Shared Items
        File pathDesktop = new File(System.getProperty("user.home"), "Desktop");
        
        //--- Check for properties file
        String StrPropertiesFile = pathDesktop + "\\LaunchPad\\launchpad.properties";
        try {
        if(!new File(StrPropertiesFile).isFile()) { 
            System.out.println("Properties file not found."); 
            JOptionPane.showMessageDialog(null, "Properties file not found.", "RIP!", JOptionPane.INFORMATION_MESSAGE);
            System.exit( 0 );
        } 
        } 
        catch(Exception e) {
            System.out.println("Error: Properties file not found."); 
        }
        
        //--- Update or Launch
        //- Items to use
        Path PathFileLaunchPadLocal = Paths.get(pathDesktop + "\\LaunchPad\\LaunchPad.jar");
        Path PathFileLaunchPadRemote = Paths.get(PropertyHandler.getInstance().getValue("FileLaunchPadRemote"));
        String StrFileLaunchPadLocal = pathDesktop + "\\LaunchPad\\LaunchPad.jar";
        String StrFileLaunchPadRemote = PropertyHandler.getInstance().getValue("FileLaunchPadRemote");
        
        //- Set i to -1 to run app if files don't exist
        int i = -1;
        
        //- Check if files exist
        if(new File(StrFileLaunchPadLocal).isFile()) { 
            if(new File(StrFileLaunchPadRemote).isFile()) { 
                //- Get times on local and remote files
                FileTime FileTimeFileLaunchPadLocal = Files.getLastModifiedTime(PathFileLaunchPadLocal);
                System.out.println("Local Modified: " + FileTimeFileLaunchPadLocal);
                FileTime FileTimeFileLaunchPadRemote = Files.getLastModifiedTime(PathFileLaunchPadRemote);
                System.out.println("Remote Modified: " + FileTimeFileLaunchPadRemote); 
                //- Compare times
                i = FileTimeFileLaunchPadRemote.compareTo(FileTimeFileLaunchPadLocal);
                    System.out.println(i);
            }
        }
        //- If newer then run update, if older just open App
        if (i > 0) {
            System.out.println("Update status: Update found. Running update stuff.");
            JOptionPane.showMessageDialog(null, "Found a newer version...  Click OK to continue.", "Update Found!", JOptionPane.INFORMATION_MESSAGE);
            String myValue = "cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -File \"" + PropertyHandler.getInstance().getValue("FileUpdateScript") + "\"" ;
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
      
    
}
