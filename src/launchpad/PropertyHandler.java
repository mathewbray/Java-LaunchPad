/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package launchpad;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Mathew
 */
public class PropertyHandler{

   private static PropertyHandler instance = null;

   private Properties prop = new Properties();
    private InputStream input = null;

   private PropertyHandler(){
         // Here you could read the file into props object
try {
                File pathDesktop = new File(System.getProperty("user.home"), "Desktop");
		input = new FileInputStream(pathDesktop + "\\launchpad.properties");
System.out.println(input);
		// load a properties file
		prop.load(input);
                input.close();

	} catch (IOException ex) {
	} finally {
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
			}
		}
	}
   }

   public static synchronized PropertyHandler getInstance(){
       if (instance == null)
           instance = new PropertyHandler();
       return instance;
   }

   public String getValue(String propKey){
       return this.prop.getProperty(propKey);
   }
}