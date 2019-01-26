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
   private File pathDesktop = new File(System.getProperty("user.home"), "Desktop");
   private Properties prop = new Properties();
    private InputStream input = null;

   private PropertyHandler() {
       try {
           // Here you could read the file into props object

           input = new FileInputStream(pathDesktop + "\\LaunchPad\\launchpad.properties");
       } catch (FileNotFoundException ex) {
           Logger.getLogger(PropertyHandler.class.getName()).log(Level.SEVERE, null, ex);
       }
System.out.println(input);
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

  
    public String setValue(String propKey, String propValue){
       this.prop.setProperty(propKey, propValue);
       try {
           input = new FileInputStream(pathDesktop + "\\LaunchPad\\launchpad.properties");
       } catch (FileNotFoundException ex) {
           Logger.getLogger(PropertyHandler.class.getName()).log(Level.SEVERE, null, ex);
       }
        SortedProperties sp = new SortedProperties();
       try {
           sp.load(input);
       } catch (IOException ex) {
           Logger.getLogger(PropertyHandler.class.getName()).log(Level.SEVERE, null, ex);
       }
       FileOutputStream out = null;
       try {
           out = new FileOutputStream(pathDesktop + "\\LaunchPad\\launchpad.properties");
       } catch (FileNotFoundException ex) {
           Logger.getLogger(PropertyHandler.class.getName()).log(Level.SEVERE, null, ex);
       }
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