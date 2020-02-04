/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package launchpad;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mathew
 */
public class PropertyHandler{

   private static PropertyHandler instance = null;
    private String pathUserProfile = System.getenv("USERPROFILE");
    private File pathDesktop = new File(System.getProperty("user.home"), "Desktop");
    //private String strPathLaunchPadFolder = pathUserProfile + "\\AppData\\Local\\LaunchPad_Java";
    String strPathLaunchPadFolder = System.getenv("SYSTEMDRIVE") + "\\LaunchPad";
    private String strPathLaunchPadPropertiesFile = strPathLaunchPadFolder + "\\launchpad.properties";   
    private Properties prop = new Properties();
    private InputStream input = null;

   private PropertyHandler() {
       try {
           // Here you could read the file into props object 
           input = new FileInputStream(strPathLaunchPadPropertiesFile);
       } catch (FileNotFoundException ex) {
           Logger.getLogger(PropertyHandler.class.getName()).log(Level.SEVERE, null, ex);
       }

       try {
           // load a properties file
           prop.load(input);
       } catch (IOException ex) {
           Logger.getLogger(PropertyHandler.class.getName()).log(Level.SEVERE, null, ex);
       }
       try {
           input.close();
       } catch (IOException ex) {
           Logger.getLogger(PropertyHandler.class.getName()).log(Level.SEVERE, null, ex);
       }


        if (input != null) {

            try {
                input.close();
            } catch (IOException ex) {
                Logger.getLogger(PropertyHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
	
   }

   public static synchronized PropertyHandler getInstance() {
       if (instance == null) {
           instance = new PropertyHandler();
       }
       return instance;
   }

   public String getValue(String propKey) {
       if (this.prop.getProperty(propKey) == null) {
           PropertyHandler.getInstance().setValue(propKey, "");
       }
        return this.prop.getProperty(propKey);
        
        
   }

  
    public String setValue(String propKey, String propValue) {
        System.out.println("Setting Key:" + propKey + " to " + propValue);
        // Get the current properties
        try {
            input = new FileInputStream(strPathLaunchPadPropertiesFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PropertyHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Load properties
        try {
            this.prop.load(input);
        } catch (IOException ex) {
            Logger.getLogger(PropertyHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Set property
        this.prop.setProperty(propKey, propValue);
        // Set Output file
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(strPathLaunchPadPropertiesFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PropertyHandler.class.getName()).log(Level.SEVERE, null, ex);
        }        
        // Store property
        try {
            this.prop.store(out, null);
        } catch (IOException ex) {
            Logger.getLogger(PropertyHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Get the current properties
        try {
            input = new FileInputStream(strPathLaunchPadPropertiesFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PropertyHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Load the properties to sort
        SortedProperties sp = new SortedProperties();
        try {
            sp.load(input);
        } catch (IOException ex) {
            Logger.getLogger(PropertyHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Set Output file
        out = null;
        try {
            out = new FileOutputStream(strPathLaunchPadPropertiesFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PropertyHandler.class.getName()).log(Level.SEVERE, null, ex);
        }  
        // Store the sorted properties
        try {
            sp.store(out, null);
        } catch (IOException ex) {
            Logger.getLogger(PropertyHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
   }
    
    class SortedProperties extends Properties {
        @Override
        public Enumeration keys() {
            Enumeration keysEnum = super.keys();
            Vector<String> keyList = new Vector<>();
            while(keysEnum.hasMoreElements()){
              keyList.add((String)keysEnum.nextElement());
            }
            Collections.sort(keyList);
            return keyList.elements();
        }
    }


}