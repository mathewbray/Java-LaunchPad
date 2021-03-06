/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package launchpad;

import java.awt.AWTException;
import java.awt.Adjustable;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.text.Collator;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.xml.bind.DatatypeConverter;
import static launchpad.LaunchPadForm.HashGenerate.toHex;
import launchpad.Type7Reverse.CiscoVigenere;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.progress.ProgressMonitor;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.NtpUtils;
import org.apache.commons.net.ntp.NtpV3Packet;
import org.apache.commons.net.ntp.TimeInfo;
import org.apache.commons.net.ntp.TimeStamp;

/**
 *
 * @author Mathew
 */
public final class LaunchPadForm extends javax.swing.JFrame {
    
    DefaultListModel defaultListModelFilteredItems = new DefaultListModel();
    File pathWorkingDirectory = new File(System.getProperty("user.dir"));
    File pathDesktop = new File(System.getProperty("user.home"), "Desktop");
    String strPathUserDesktop = System.getProperty("user.home") + "\\Desktop";
    String pathUserProfile = System.getenv("USERPROFILE");
    File pathLogging = new File(pathDesktop + "\\Logging-Output");
    String strPathLoggingFolder = pathDesktop + "\\Logging-Output";    
    String strPathLaunchPadFolder = System.getenv("SYSTEMDRIVE") + "\\LaunchPad";
    String strSessionListDefault = strPathLaunchPadFolder + "\\SessionList.csv";
    String strPathPropertiesFile = strPathLaunchPadFolder + "\\launchpad.properties";
    String strPathLaunchPadPersistantUserFolder = pathUserProfile + "\\AppData\\Local\\LaunchPad_Java_Persistant_User";   
    String strPathLaunchPadPersistantPropertiesFile = strPathLaunchPadPersistantUserFolder + "\\launchpad.properties";
    String strSessionListFavoritesFolder = strPathLaunchPadPersistantUserFolder + "\\FavoritesSessionList";   
    String strSessionListFavorites = strSessionListFavoritesFolder + "\\SessionList.csv";
    
    //- Get date and time
    SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("yyyy-MM-dd_HHmm-ssSSS");
    String dateTime = simpleDateFormat.format(new Date());
    
    //- Icon List
    public static String[] arrIconList;
    



    
    /**
     * Creates new form LaunchPadForm
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException
     * @throws java.net.URISyntaxException
     * @throws java.awt.AWTException
     * @throws java.lang.InterruptedException
     * @throws java.lang.ClassNotFoundException
     * @throws java.lang.InstantiationException
     * @throws java.lang.IllegalAccessException
     * @throws javax.swing.UnsupportedLookAndFeelException
     */
    public LaunchPadForm() throws IOException, FileNotFoundException, URISyntaxException, AWTException, InterruptedException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        loadSettingsMainButtonList();
        initComponents();
        initAddSettingsReferences();
        initAddSettingsScripts();
        setTitle(PropertyHandler.getInstance().getValue("Window.Title"));
        getSessionList();
        loadMainButtonStyles();        
        loadSettingsMainButtonsData();
        loadSettingsLinkData();
        loadSettingsPersonal();
        loadSettingsShared();
        loadHostIPMAC();
        loadClassification();
        setSessionTextSize();

        //- Set the look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("Button.disabledText", new Color(150,150,150));
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }

        //- Copy .jar to LaunchPad Folder if not running from there.
        final File currentJar = new File(LaunchPad.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        System.out.println("Copy Operation - Code source: " + currentJar);
        //System.out.println("Copy Operation - Code source name: + "currentJar.getName());
        //- If currently running jar is not in the LP folder
        if(!currentJar.toString().startsWith("C:\\LaunchPad\\")) {    
            //- If force to run from LP folder is blank
            String strForceRunFromLPFolder = PropertyHandler.getInstance().getValue("Setting.ForceRunFromLaunchPadFolder");
            System.out.println("Copy Operation - SettingForceRunFromLaunchPadFolder: " + strForceRunFromLPFolder);                                                                                                            
            if ("".equals(strForceRunFromLPFolder)) {
                PropertyHandler.getInstance().setValue("Setting.ForceRunFromLaunchPadFolder", "1");  
            //- Else If force to run from LP folder is set to 1
            }else if("1".equals(strForceRunFromLPFolder)) {
                //- If current instance is actually a .jar file
                if(currentJar.getName().endsWith(".jar")) {
                    //- Check if jar already exists
                    File fileDestination = new File(strPathLaunchPadFolder + "\\LaunchPad.jar");
                    boolean fileExists = false;
                    if(fileDestination.exists() && !fileDestination.isDirectory()) {
                        fileExists = Boolean.TRUE;
                    } 
                    System.out.println("Copy Operation - JAR destination exists: " + fileExists);                                                                                                
                    //- Test if file exists and is locked
                    boolean fileIsNotLocked = fileDestination.renameTo(fileDestination);
                    if (!fileIsNotLocked && fileExists) {
                        System.out.println("Copy Operation - JAR destination lock status: " + fileIsNotLocked);                            
                        JOptionPane.showMessageDialog(null, "Unable to copy LaunchPad.jar to: " + strPathLaunchPadFolder + "\r\n\r\n" + "File is locked.","Oh no!",JOptionPane.WARNING_MESSAGE);
                        System.exit(0);
                    } else {
                        System.out.println("Copy Operation - JAR destination lock status: " + fileIsNotLocked);                                                        
                        System.out.println("Copy Operation - Copying JAR to : " + strPathLaunchPadFolder);
                        Files.copy(currentJar.toPath(),  (new File(strPathLaunchPadFolder + "\\" + currentJar.getName())).toPath(), StandardCopyOption.REPLACE_EXISTING);
                        JOptionPane.showMessageDialog(null, "Copied LaunchPad.jar to: " + strPathLaunchPadFolder + "\r\n" + "Shortcut has been added to the desktop.","Good to go!",JOptionPane.INFORMATION_MESSAGE);
                        copyShortcutToDesktop();
                        System.exit(0);
                    }                        
                } else {
                System.out.println("Copy Operation - Run status: ABORT (Code source name does not end in \".jar\")");
                }             
            } else {
            System.out.println("Copy Operation - Run status: ABORT (Not forced to run from LP folder)");
            }
        } else {
            System.out.println("Copy Operation - Run status: ABORT (Code source match)");
        }
        
        //- Don't close the window immediately, prompt to ask
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            //--- Listen for window close
            public void windowClosing(WindowEvent we) { 
                String ObjButtons[] = {"Yes","No"};
                String strExitTitle = "You Sure??";
                String strExitMessage = "Are you sure you want to exit LaunchPad?";
                try {
                    if("Japanese".equals(PropertyHandlerPersonal.getInstance().getValue("Setting.Language"))) {
                        ObjButtons[0] = "はい";
                        ObjButtons[1] = "いいえ";
                        strExitTitle = "本気ですか？";
                        strExitMessage = "LaunchPadを終了してもよろしいですか？";
                    }
                } catch (NullPointerException e) {
                    System.out.println("Language Goofed");
                }
                int PromptResult = JOptionPane.showOptionDialog(null,strExitMessage,strExitTitle,JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,null,ObjButtons,ObjButtons[1]);
                if(PromptResult==JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
            //--- Gray out the username and password when loaded
            @Override
            public void windowOpened(WindowEvent e) {
                jTextFieldConnectUsername.setEnabled(false);
                jPasswordFieldConnectPassword.setEnabled(false);
            }
        });
        
        //--- Listen for credential checkbox
        jCheckBoxAlternateLogin.addItemListener((ItemEvent e) -> {
            if(e.getStateChange() == ItemEvent.SELECTED){
                jTextFieldConnectUsername.setEnabled(true);
                jPasswordFieldConnectPassword.setEnabled(true);
            }
            else if(e.getStateChange() == ItemEvent.DESELECTED){
                jTextFieldConnectUsername.setEnabled(false);
                jPasswordFieldConnectPassword.setEnabled(false);
            }
            validate();
            repaint();
        });
        
        //--- Listen for credential checkbox
        jCheckBoxChangePingOnSessionSelect.addItemListener((ItemEvent e) -> {
            if(e.getStateChange() == ItemEvent.SELECTED){
                PropertyHandlerPersonal.getInstance().setValue("Setting.ChangePingOnSessionSelect", "1");
            }
            else if(e.getStateChange() == ItemEvent.DESELECTED){
                PropertyHandlerPersonal.getInstance().setValue("Setting.ChangePingOnSessionSelect", "0");
            }
            validate();
            repaint();
        });            

        
        //--- Listen for online/offline reference button
        jToggleOnlineOfflineMode.addItemListener((ItemEvent ev) -> {
            if(ev.getStateChange()==ItemEvent.SELECTED){
                System.out.println("Offline is selected");
                jToggleOnlineOfflineMode.setText("Offline");
                //jToggleOnlineOfflineMode.setBackground(new java.awt.Color(236, 180, 180));
                //- Japanese
                try {
                    if("Japanese".equals(PropertyHandlerPersonal.getInstance().getValue("Setting.Language"))) {
                        jToggleOnlineOfflineMode.setText("オフライン");
                    }
                } catch (NullPointerException e) {System.out.println("Language Goofed");
                }             
            } else if(ev.getStateChange()==ItemEvent.DESELECTED){
                System.out.println("Offline is not selected");
                jToggleOnlineOfflineMode.setText("Online");
                //- Had to 
                jToggleOnlineOfflineMode.setBackground(new java.awt.Color(180, 236, 180));
                //- Japanese
                try {
                    if("Japanese".equals(PropertyHandlerPersonal.getInstance().getValue("Setting.Language"))) {
                        jToggleOnlineOfflineMode.setText("オンライン");
                    }
                } catch (NullPointerException e) {System.out.println("Language Goofed");
                }
            }
        });
            
        

            
            

        //--- Apply images to buttons
        Integer buttonHeightWidth = 40;
        ImageIcon icon;
        Image img;
        Image newimg;
        try {
            //Button01
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.01.Icon")));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButtonMainButton1.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed"); StringBuilder sb = new StringBuilder(e.toString());            for (StackTraceElement ste : e.getStackTrace()) {                sb.append("\n\tat ");                sb.append(ste);            }            String trace = sb.toString();            JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);
        }
        try {
            //Button2
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.02.Icon")));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButtonMainButton2.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed"); StringBuilder sb = new StringBuilder(e.toString());            for (StackTraceElement ste : e.getStackTrace()) {                sb.append("\n\tat ");                sb.append(ste);            }            String trace = sb.toString();            JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);
        }
        try {
            //Button3
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.03.Icon")));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButtonMainButton3.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed"); StringBuilder sb = new StringBuilder(e.toString());            for (StackTraceElement ste : e.getStackTrace()) {                sb.append("\n\tat ");                sb.append(ste);            }            String trace = sb.toString();            JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);
        }
        try {
            //Button4
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.04.Icon")));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButtonMainButton4.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed"); StringBuilder sb = new StringBuilder(e.toString());            for (StackTraceElement ste : e.getStackTrace()) {                sb.append("\n\tat ");                sb.append(ste);            }            String trace = sb.toString();            JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);
        }
        try {
            //Button5
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.05.Icon")));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButtonMainButton5.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed"); StringBuilder sb = new StringBuilder(e.toString());            for (StackTraceElement ste : e.getStackTrace()) {                sb.append("\n\tat ");                sb.append(ste);            }            String trace = sb.toString();            JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);
        }
        try {
            //Button6
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.06.Icon")));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButtonMainButton6.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed"); StringBuilder sb = new StringBuilder(e.toString());            for (StackTraceElement ste : e.getStackTrace()) {                sb.append("\n\tat ");                sb.append(ste);            }            String trace = sb.toString();            JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);
        }
        try {
            //Button7
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.07.Icon")));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButtonMainButton7.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed"); StringBuilder sb = new StringBuilder(e.toString());            for (StackTraceElement ste : e.getStackTrace()) {                sb.append("\n\tat ");                sb.append(ste);            }            String trace = sb.toString();            JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);
        }
        try {
            //Button8
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.08.Icon")));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButtonMainButton8.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed"); StringBuilder sb = new StringBuilder(e.toString());            for (StackTraceElement ste : e.getStackTrace()) {                sb.append("\n\tat ");                sb.append(ste);            }            String trace = sb.toString();            JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);
        }
        try {
            //Button9
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.09.Icon")));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButtonMainButton9.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed"); StringBuilder sb = new StringBuilder(e.toString());            for (StackTraceElement ste : e.getStackTrace()) {                sb.append("\n\tat ");                sb.append(ste);            }            String trace = sb.toString();            JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);
        }
        try {
            //Button10
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.10.Icon")));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButtonMainButton10.setIcon(new ImageIcon(newimg));
         } catch (NullPointerException e) {System.out.println("Icon Goofed"); StringBuilder sb = new StringBuilder(e.toString());            for (StackTraceElement ste : e.getStackTrace()) {                sb.append("\n\tat ");                sb.append(ste);            }            String trace = sb.toString();            JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);
        }
        try {
           //Button11
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.11.Icon")));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButtonMainButton11.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed"); StringBuilder sb = new StringBuilder(e.toString());            for (StackTraceElement ste : e.getStackTrace()) {                sb.append("\n\tat ");                sb.append(ste);            }            String trace = sb.toString();            JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);
        }
        try {
            //Button12
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.12.Icon")));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButtonMainButton12.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed"); StringBuilder sb = new StringBuilder(e.toString());            for (StackTraceElement ste : e.getStackTrace()) {                sb.append("\n\tat ");                sb.append(ste);            }            String trace = sb.toString();            JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);
        }
        try {
            //Button13
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.13.Icon")));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButtonMainButton13.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed"); StringBuilder sb = new StringBuilder(e.toString());            for (StackTraceElement ste : e.getStackTrace()) {                sb.append("\n\tat ");                sb.append(ste);            }            String trace = sb.toString();            JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);
        }
        try {
            //Button14
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.14.Icon")));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButtonMainButton14.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed"); StringBuilder sb = new StringBuilder(e.toString());            for (StackTraceElement ste : e.getStackTrace()) {                sb.append("\n\tat ");                sb.append(ste);            }            String trace = sb.toString();            JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);
        }
        try {
            //Button15
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.15.Icon")));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButtonMainButton15.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed"); StringBuilder sb = new StringBuilder(e.toString());            for (StackTraceElement ste : e.getStackTrace()) {                sb.append("\n\tat ");                sb.append(ste);            }            String trace = sb.toString();            JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);
        }
        try {
            //Button16
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.16.Icon")));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButtonMainButton16.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed"); StringBuilder sb = new StringBuilder(e.toString());            for (StackTraceElement ste : e.getStackTrace()) {                sb.append("\n\tat ");                sb.append(ste);            }            String trace = sb.toString();            JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);
        }
        try {
            //Button17
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.17.Icon")));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButtonMainButton17.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed"); StringBuilder sb = new StringBuilder(e.toString());            for (StackTraceElement ste : e.getStackTrace()) {                sb.append("\n\tat ");                sb.append(ste);            }            String trace = sb.toString();            JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);
        }
        try {
            //Button18
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.18.Icon")));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButtonMainButton18.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed"); StringBuilder sb = new StringBuilder(e.toString());            for (StackTraceElement ste : e.getStackTrace()) {                sb.append("\n\tat ");                sb.append(ste);            }            String trace = sb.toString();            JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);
        }
        try {
            //Button19
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.19.Icon")));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButtonMainButton19.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed"); StringBuilder sb = new StringBuilder(e.toString());            for (StackTraceElement ste : e.getStackTrace()) {                sb.append("\n\tat ");                sb.append(ste);            }            String trace = sb.toString();            JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);}
        try {
            //Button20
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.20.Icon")));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButtonMainButton20.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed");StringBuilder sb = new StringBuilder(e.toString());for (StackTraceElement ste : e.getStackTrace()) { sb.append("\n\tat "); sb.append(ste); } String trace = sb.toString(); JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);        }
        try {
            //Button21
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.21.Icon")));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButtonMainButton21.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed");StringBuilder sb = new StringBuilder(e.toString());for (StackTraceElement ste : e.getStackTrace()) { sb.append("\n\tat "); sb.append(ste); } String trace = sb.toString(); JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);        }
        try {
            //Button22
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.22.Icon")));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButtonMainButton22.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed");StringBuilder sb = new StringBuilder(e.toString());for (StackTraceElement ste : e.getStackTrace()) { sb.append("\n\tat "); sb.append(ste); } String trace = sb.toString(); JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);        }
        try {
            //Button23
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.23.Icon")));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButtonMainButton23.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed");StringBuilder sb = new StringBuilder(e.toString());for (StackTraceElement ste : e.getStackTrace()) { sb.append("\n\tat "); sb.append(ste); } String trace = sb.toString(); JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);        }
        try {
            //Button24
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.24.Icon")));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButtonMainButton24.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed");StringBuilder sb = new StringBuilder(e.toString());for (StackTraceElement ste : e.getStackTrace()) { sb.append("\n\tat "); sb.append(ste); } String trace = sb.toString(); JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);        }

        //NETWORK CONNECTIONS BUTTON
        try{
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/networkconnections.png"));
            img = icon.getImage(); newimg = img.getScaledInstance( 20, 20,  java.awt.Image.SCALE_SMOOTH ); jButtonNetworkConnections.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed");StringBuilder sb = new StringBuilder(e.toString());for (StackTraceElement ste : e.getStackTrace()) { sb.append("\n\tat "); sb.append(ste); } String trace = sb.toString(); JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);        }

        //NETWORK REFRESH BUTTON
        try{
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/update.png"));
            img = icon.getImage(); newimg = img.getScaledInstance( 20, 20,  java.awt.Image.SCALE_SMOOTH ); jButtonRefreshHostnameIPMAC.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed");StringBuilder sb = new StringBuilder(e.toString());for (StackTraceElement ste : e.getStackTrace()) { sb.append("\n\tat "); sb.append(ste); } String trace = sb.toString(); JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);        }
 
        
        

//        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.01.Icon"))));
//        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.02.Icon"))));
//        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.03.Icon"))));
//        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.04.Icon"))));
//        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.05.Icon"))));
//        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.06.Icon"))));
//        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.07.Icon"))));
//        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.08.Icon"))));
//        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.09.Icon"))));
//        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.10.Icon"))));
//        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.11.Icon"))));
//        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.12.Icon"))));
//        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.13.Icon"))));
//        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.14.Icon"))));
//        jButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.15.Icon"))));
//        jButton16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.16.Icon"))));
//        jButton17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.17.Icon"))));
//        jButton18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.18.Icon"))));
//        jButton19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.19.Icon"))));
//        jButton20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button.20.Icon"))));

    

        //--- Load preloaded IPs
        jTextFieldConnectHostname.setText(PropertyHandler.getInstance().getValue("Preload.SSH"));
        jTextFieldPingHostname.setText(PropertyHandler.getInstance().getValue("Preload.Ping"));

        //--- Load preloaded Zip Items
        jTextFieldZipSourceFolder.setText(PropertyHandler.getInstance().getValue("Zip.DefaultSourceFolder").replace("%USERPROFILE%", pathUserProfile));
        jTextFieldZipFilename.setText(pathDesktop + "\\Backup_" + dateTime + ".zip");
        jTextFieldZipSourceFile.setText(pathDesktop + "\\Backup_" + dateTime + ".zip");
        jTextFieldZipDestinationFolder.setText(PropertyHandler.getInstance().getValue("Zip.DefaultDestinationFolder").replace("%USERPROFILE%", pathUserProfile));
        
        
        //-- Set to use the pointy finger cursor
        jButtonMainButton1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButtonMainButton2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButtonMainButton3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButtonMainButton4.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButtonMainButton5.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButtonMainButton6.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButtonMainButton7.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButtonMainButton8.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButtonMainButton9.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButtonMainButton10.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButtonMainButton11.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButtonMainButton12.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButtonMainButton13.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButtonMainButton14.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButtonMainButton15.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButtonMainButton16.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButtonMainButton17.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButtonMainButton18.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButtonMainButton19.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButtonMainButton20.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButtonMainButton21.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButtonMainButton22.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButtonMainButton23.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButtonMainButton24.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButtonNetworkConnections.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButtonRefreshHostnameIPMAC.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jLabelLocalIP.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        //- Add the slight buuton movement when hovered over function
        jButtonMainButton1.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButtonMainButton1.getX(); int y = jButtonMainButton1.getY(); jButtonMainButton1.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButtonMainButton1.getX(); int y = jButtonMainButton1.getY(); jButtonMainButton1.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButtonMainButton1.getX(); int y = jButtonMainButton1.getY(); jButtonMainButton1.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButtonMainButton1.getX(); int y = jButtonMainButton1.getY(); jButtonMainButton1.setLocation(x - 1, y - 1); } });
        jButtonMainButton2.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButtonMainButton2.getX(); int y = jButtonMainButton2.getY(); jButtonMainButton2.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButtonMainButton2.getX(); int y = jButtonMainButton2.getY(); jButtonMainButton2.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButtonMainButton2.getX(); int y = jButtonMainButton2.getY(); jButtonMainButton2.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButtonMainButton2.getX(); int y = jButtonMainButton2.getY(); jButtonMainButton2.setLocation(x - 1, y - 1); } });
        jButtonMainButton3.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButtonMainButton3.getX(); int y = jButtonMainButton3.getY(); jButtonMainButton3.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButtonMainButton3.getX(); int y = jButtonMainButton3.getY(); jButtonMainButton3.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButtonMainButton3.getX(); int y = jButtonMainButton3.getY(); jButtonMainButton3.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButtonMainButton3.getX(); int y = jButtonMainButton3.getY(); jButtonMainButton3.setLocation(x - 1, y - 1); } });
        jButtonMainButton4.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButtonMainButton4.getX(); int y = jButtonMainButton4.getY(); jButtonMainButton4.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButtonMainButton4.getX(); int y = jButtonMainButton4.getY(); jButtonMainButton4.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButtonMainButton4.getX(); int y = jButtonMainButton4.getY(); jButtonMainButton4.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButtonMainButton4.getX(); int y = jButtonMainButton4.getY(); jButtonMainButton4.setLocation(x - 1, y - 1); } });
        jButtonMainButton5.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButtonMainButton5.getX(); int y = jButtonMainButton5.getY(); jButtonMainButton5.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButtonMainButton5.getX(); int y = jButtonMainButton5.getY(); jButtonMainButton5.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButtonMainButton5.getX(); int y = jButtonMainButton5.getY(); jButtonMainButton5.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButtonMainButton5.getX(); int y = jButtonMainButton5.getY(); jButtonMainButton5.setLocation(x - 1, y - 1); } });
        jButtonMainButton6.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButtonMainButton6.getX(); int y = jButtonMainButton6.getY(); jButtonMainButton6.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButtonMainButton6.getX(); int y = jButtonMainButton6.getY(); jButtonMainButton6.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButtonMainButton6.getX(); int y = jButtonMainButton6.getY(); jButtonMainButton6.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButtonMainButton6.getX(); int y = jButtonMainButton6.getY(); jButtonMainButton6.setLocation(x - 1, y - 1); } });
        jButtonMainButton7.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButtonMainButton7.getX(); int y = jButtonMainButton7.getY(); jButtonMainButton7.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButtonMainButton7.getX(); int y = jButtonMainButton7.getY(); jButtonMainButton7.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButtonMainButton7.getX(); int y = jButtonMainButton7.getY(); jButtonMainButton7.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButtonMainButton7.getX(); int y = jButtonMainButton7.getY(); jButtonMainButton7.setLocation(x - 1, y - 1); } });
        jButtonMainButton8.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButtonMainButton8.getX(); int y = jButtonMainButton8.getY(); jButtonMainButton8.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButtonMainButton8.getX(); int y = jButtonMainButton8.getY(); jButtonMainButton8.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButtonMainButton8.getX(); int y = jButtonMainButton8.getY(); jButtonMainButton8.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButtonMainButton8.getX(); int y = jButtonMainButton8.getY(); jButtonMainButton8.setLocation(x - 1, y - 1); } });
        jButtonMainButton9.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButtonMainButton9.getX(); int y = jButtonMainButton9.getY(); jButtonMainButton9.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButtonMainButton9.getX(); int y = jButtonMainButton9.getY(); jButtonMainButton9.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButtonMainButton9.getX(); int y = jButtonMainButton9.getY(); jButtonMainButton9.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButtonMainButton9.getX(); int y = jButtonMainButton9.getY(); jButtonMainButton9.setLocation(x - 1, y - 1); } });
        jButtonMainButton10.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButtonMainButton10.getX(); int y = jButtonMainButton10.getY(); jButtonMainButton10.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButtonMainButton10.getX(); int y = jButtonMainButton10.getY(); jButtonMainButton10.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButtonMainButton10.getX(); int y = jButtonMainButton10.getY(); jButtonMainButton10.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButtonMainButton10.getX(); int y = jButtonMainButton10.getY(); jButtonMainButton10.setLocation(x - 1, y - 1); } });
        jButtonMainButton11.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButtonMainButton11.getX(); int y = jButtonMainButton11.getY(); jButtonMainButton11.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButtonMainButton11.getX(); int y = jButtonMainButton11.getY(); jButtonMainButton11.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButtonMainButton11.getX(); int y = jButtonMainButton11.getY(); jButtonMainButton11.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButtonMainButton11.getX(); int y = jButtonMainButton11.getY(); jButtonMainButton11.setLocation(x - 1, y - 1); } });
        jButtonMainButton12.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButtonMainButton12.getX(); int y = jButtonMainButton12.getY(); jButtonMainButton12.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButtonMainButton12.getX(); int y = jButtonMainButton12.getY(); jButtonMainButton12.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButtonMainButton12.getX(); int y = jButtonMainButton12.getY(); jButtonMainButton12.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButtonMainButton12.getX(); int y = jButtonMainButton12.getY(); jButtonMainButton12.setLocation(x - 1, y - 1); } });
        jButtonMainButton13.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButtonMainButton13.getX(); int y = jButtonMainButton13.getY(); jButtonMainButton13.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButtonMainButton13.getX(); int y = jButtonMainButton13.getY(); jButtonMainButton13.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButtonMainButton13.getX(); int y = jButtonMainButton13.getY(); jButtonMainButton13.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButtonMainButton13.getX(); int y = jButtonMainButton13.getY(); jButtonMainButton13.setLocation(x - 1, y - 1); } });
        jButtonMainButton14.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButtonMainButton14.getX(); int y = jButtonMainButton14.getY(); jButtonMainButton14.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButtonMainButton14.getX(); int y = jButtonMainButton14.getY(); jButtonMainButton14.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButtonMainButton14.getX(); int y = jButtonMainButton14.getY(); jButtonMainButton14.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButtonMainButton14.getX(); int y = jButtonMainButton14.getY(); jButtonMainButton14.setLocation(x - 1, y - 1); } });
        jButtonMainButton15.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButtonMainButton15.getX(); int y = jButtonMainButton15.getY(); jButtonMainButton15.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButtonMainButton15.getX(); int y = jButtonMainButton15.getY(); jButtonMainButton15.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButtonMainButton15.getX(); int y = jButtonMainButton15.getY(); jButtonMainButton15.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButtonMainButton15.getX(); int y = jButtonMainButton15.getY(); jButtonMainButton15.setLocation(x - 1, y - 1); } });
        jButtonMainButton16.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButtonMainButton16.getX(); int y = jButtonMainButton16.getY(); jButtonMainButton16.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButtonMainButton16.getX(); int y = jButtonMainButton16.getY(); jButtonMainButton16.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButtonMainButton16.getX(); int y = jButtonMainButton16.getY(); jButtonMainButton16.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButtonMainButton16.getX(); int y = jButtonMainButton16.getY(); jButtonMainButton16.setLocation(x - 1, y - 1); } });
        jButtonMainButton17.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButtonMainButton17.getX(); int y = jButtonMainButton17.getY(); jButtonMainButton17.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButtonMainButton17.getX(); int y = jButtonMainButton17.getY(); jButtonMainButton17.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButtonMainButton17.getX(); int y = jButtonMainButton17.getY(); jButtonMainButton17.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButtonMainButton17.getX(); int y = jButtonMainButton17.getY(); jButtonMainButton17.setLocation(x - 1, y - 1); } });
        jButtonMainButton18.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButtonMainButton18.getX(); int y = jButtonMainButton18.getY(); jButtonMainButton18.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButtonMainButton18.getX(); int y = jButtonMainButton18.getY(); jButtonMainButton18.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButtonMainButton18.getX(); int y = jButtonMainButton18.getY(); jButtonMainButton18.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButtonMainButton18.getX(); int y = jButtonMainButton18.getY(); jButtonMainButton18.setLocation(x - 1, y - 1); } });
        jButtonMainButton19.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButtonMainButton19.getX(); int y = jButtonMainButton19.getY(); jButtonMainButton19.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButtonMainButton19.getX(); int y = jButtonMainButton19.getY(); jButtonMainButton19.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButtonMainButton19.getX(); int y = jButtonMainButton19.getY(); jButtonMainButton19.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButtonMainButton19.getX(); int y = jButtonMainButton19.getY(); jButtonMainButton19.setLocation(x - 1, y - 1); } });
        jButtonMainButton20.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButtonMainButton20.getX(); int y = jButtonMainButton20.getY(); jButtonMainButton20.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButtonMainButton20.getX(); int y = jButtonMainButton20.getY(); jButtonMainButton20.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButtonMainButton20.getX(); int y = jButtonMainButton20.getY(); jButtonMainButton20.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButtonMainButton20.getX(); int y = jButtonMainButton20.getY(); jButtonMainButton20.setLocation(x - 1, y - 1); } });
        jButtonMainButton21.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButtonMainButton21.getX(); int y = jButtonMainButton21.getY(); jButtonMainButton21.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButtonMainButton21.getX(); int y = jButtonMainButton21.getY(); jButtonMainButton21.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButtonMainButton21.getX(); int y = jButtonMainButton21.getY(); jButtonMainButton21.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButtonMainButton21.getX(); int y = jButtonMainButton21.getY(); jButtonMainButton21.setLocation(x - 1, y - 1); } });
        jButtonMainButton22.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButtonMainButton22.getX(); int y = jButtonMainButton22.getY(); jButtonMainButton22.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButtonMainButton22.getX(); int y = jButtonMainButton22.getY(); jButtonMainButton22.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButtonMainButton22.getX(); int y = jButtonMainButton22.getY(); jButtonMainButton22.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButtonMainButton22.getX(); int y = jButtonMainButton22.getY(); jButtonMainButton22.setLocation(x - 1, y - 1); } });
        jButtonMainButton23.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButtonMainButton23.getX(); int y = jButtonMainButton23.getY(); jButtonMainButton23.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButtonMainButton23.getX(); int y = jButtonMainButton23.getY(); jButtonMainButton23.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButtonMainButton23.getX(); int y = jButtonMainButton23.getY(); jButtonMainButton23.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButtonMainButton23.getX(); int y = jButtonMainButton23.getY(); jButtonMainButton23.setLocation(x - 1, y - 1); } });
        jButtonMainButton24.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButtonMainButton24.getX(); int y = jButtonMainButton24.getY(); jButtonMainButton24.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButtonMainButton24.getX(); int y = jButtonMainButton24.getY(); jButtonMainButton24.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButtonMainButton24.getX(); int y = jButtonMainButton24.getY(); jButtonMainButton24.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButtonMainButton24.getX(); int y = jButtonMainButton24.getY(); jButtonMainButton24.setLocation(x - 1, y - 1); } });
        jButtonNetworkConnections.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButtonNetworkConnections.getX(); int y = jButtonNetworkConnections.getY(); jButtonNetworkConnections.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButtonNetworkConnections.getX(); int y = jButtonNetworkConnections.getY(); jButtonNetworkConnections.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButtonNetworkConnections.getX(); int y = jButtonNetworkConnections.getY(); jButtonNetworkConnections.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButtonNetworkConnections.getX(); int y = jButtonNetworkConnections.getY(); jButtonNetworkConnections.setLocation(x - 1, y - 1); } });
        jButtonRefreshHostnameIPMAC.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButtonRefreshHostnameIPMAC.getX(); int y = jButtonRefreshHostnameIPMAC.getY(); jButtonRefreshHostnameIPMAC.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButtonRefreshHostnameIPMAC.getX(); int y = jButtonRefreshHostnameIPMAC.getY(); jButtonRefreshHostnameIPMAC.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButtonRefreshHostnameIPMAC.getX(); int y = jButtonRefreshHostnameIPMAC.getY(); jButtonRefreshHostnameIPMAC.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButtonRefreshHostnameIPMAC.getX(); int y = jButtonRefreshHostnameIPMAC.getY(); jButtonRefreshHostnameIPMAC.setLocation(x - 1, y - 1); } });
        jLabelLocalIP.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jLabelLocalIP.getX(); int y = jLabelLocalIP.getY(); jLabelLocalIP.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jLabelLocalIP.getX(); int y = jLabelLocalIP.getY(); jLabelLocalIP.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jLabelLocalIP.getX(); int y = jLabelLocalIP.getY(); jLabelLocalIP.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jLabelLocalIP.getX(); int y = jLabelLocalIP.getY(); jLabelLocalIP.setLocation(x - 1, y - 1); } });
        
        //- Remove the button borders
        Border emptyBorder = BorderFactory.createEmptyBorder();
        jButtonMainButton1.setBorder(emptyBorder);
        jButtonMainButton2.setBorder(emptyBorder);
        jButtonMainButton3.setBorder(emptyBorder);
        jButtonMainButton4.setBorder(emptyBorder);
        jButtonMainButton5.setBorder(emptyBorder);
        jButtonMainButton6.setBorder(emptyBorder);
        jButtonMainButton7.setBorder(emptyBorder);
        jButtonMainButton8.setBorder(emptyBorder);
        jButtonMainButton9.setBorder(emptyBorder);
        jButtonMainButton10.setBorder(emptyBorder);
        jButtonMainButton11.setBorder(emptyBorder);
        jButtonMainButton12.setBorder(emptyBorder);
        jButtonMainButton13.setBorder(emptyBorder);
        jButtonMainButton14.setBorder(emptyBorder);
        jButtonMainButton15.setBorder(emptyBorder);
        jButtonMainButton16.setBorder(emptyBorder);
        jButtonMainButton17.setBorder(emptyBorder);
        jButtonMainButton18.setBorder(emptyBorder);
        jButtonMainButton19.setBorder(emptyBorder);
        jButtonMainButton20.setBorder(emptyBorder);
        jButtonMainButton21.setBorder(emptyBorder);
        jButtonMainButton22.setBorder(emptyBorder);
        jButtonMainButton23.setBorder(emptyBorder);
        jButtonMainButton24.setBorder(emptyBorder);
        jButtonNetworkConnections.setBorder(emptyBorder);
        jButtonRefreshHostnameIPMAC.setBorder(emptyBorder);
       
        //--- Create directories if not exist
        new File(strPathLoggingFolder).mkdirs();
        
        //--- Load Links button text
        jButtonLinkCustom01.setText(PropertyHandler.getInstance().getValue("Custom.Link.01.Description"));
        jButtonLinkCustom02.setText(PropertyHandler.getInstance().getValue("Custom.Link.02.Description"));
        jButtonLinkCustom03.setText(PropertyHandler.getInstance().getValue("Custom.Link.03.Description"));
        jButtonLinkCustom04.setText(PropertyHandler.getInstance().getValue("Custom.Link.04.Description"));
        jButtonLinkCustom05.setText(PropertyHandler.getInstance().getValue("Custom.Link.05.Description"));
        jButtonLinkCustom06.setText(PropertyHandler.getInstance().getValue("Custom.Link.06.Description"));        
        jButtonLinkCustom07.setText(PropertyHandler.getInstance().getValue("Custom.Link.07.Description"));        
        jButtonLinkCustom08.setText(PropertyHandler.getInstance().getValue("Custom.Link.08.Description"));        
        jButtonLinkCustom09.setText(PropertyHandler.getInstance().getValue("Custom.Link.09.Description"));        
        jButtonLinkCustom10.setText(PropertyHandler.getInstance().getValue("Custom.Link.10.Description"));                
        jButtonLinkCustom11.setText(PropertyHandler.getInstance().getValue("Custom.Link.11.Description"));                
        jButtonLinkCustom12.setText(PropertyHandler.getInstance().getValue("Custom.Link.12.Description"));                
        jButtonLinkCustom13.setText(PropertyHandler.getInstance().getValue("Custom.Link.13.Description"));                
        jButtonLinkCustom14.setText(PropertyHandler.getInstance().getValue("Custom.Link.14.Description"));                
        jButtonLinkCustom15.setText(PropertyHandler.getInstance().getValue("Custom.Link.15.Description"));                
        jButtonLinkCustom16.setText(PropertyHandler.getInstance().getValue("Custom.Link.16.Description"));                
        jButtonLinkCustom17.setText(PropertyHandler.getInstance().getValue("Custom.Link.17.Description"));                
        jButtonLinkCustom18.setText(PropertyHandler.getInstance().getValue("Custom.Link.18.Description"));                
        jButtonLinkCustom19.setText(PropertyHandler.getInstance().getValue("Custom.Link.19.Description"));                
        jButtonLinkCustom20.setText(PropertyHandler.getInstance().getValue("Custom.Link.20.Description"));                
        jButtonLinkCustom21.setText(PropertyHandler.getInstance().getValue("Custom.Link.21.Description"));                
        jButtonLinkCustom22.setText(PropertyHandler.getInstance().getValue("Custom.Link.22.Description"));                
        jButtonLinkCustom23.setText(PropertyHandler.getInstance().getValue("Custom.Link.23.Description"));                
        jButtonLinkCustom24.setText(PropertyHandler.getInstance().getValue("Custom.Link.24.Description"));                
        jButtonLinkCustom25.setText(PropertyHandler.getInstance().getValue("Custom.Link.25.Description"));                
        jButtonLinkCustom26.setText(PropertyHandler.getInstance().getValue("Custom.Link.26.Description"));                
        jButtonLinkCustom27.setText(PropertyHandler.getInstance().getValue("Custom.Link.27.Description"));                
        jButtonLinkCustom28.setText(PropertyHandler.getInstance().getValue("Custom.Link.28.Description"));                
        jButtonLinkCustom29.setText(PropertyHandler.getInstance().getValue("Custom.Link.29.Description"));                
        jButtonLinkCustom30.setText(PropertyHandler.getInstance().getValue("Custom.Link.30.Description"));                
        jButtonLinkCustom31.setText(PropertyHandler.getInstance().getValue("Custom.Link.31.Description"));                
        jButtonLinkCustom32.setText(PropertyHandler.getInstance().getValue("Custom.Link.32.Description"));                
        jButtonLinkCustom33.setText(PropertyHandler.getInstance().getValue("Custom.Link.33.Description"));                
        jButtonLinkCustom34.setText(PropertyHandler.getInstance().getValue("Custom.Link.34.Description"));                
        jButtonLinkCustom35.setText(PropertyHandler.getInstance().getValue("Custom.Link.35.Description"));                
        jButtonLinkCustom36.setText(PropertyHandler.getInstance().getValue("Custom.Link.36.Description"));                
        
        
        //--- Load Reference button text
        jButtonReferenceCustom01.setText(PropertyHandler.getInstance().getValue("Custom.Reference.01.Description"));
        jButtonReferenceCustom02.setText(PropertyHandler.getInstance().getValue("Custom.Reference.02.Description"));
        jButtonReferenceCustom03.setText(PropertyHandler.getInstance().getValue("Custom.Reference.03.Description"));
        jButtonReferenceCustom04.setText(PropertyHandler.getInstance().getValue("Custom.Reference.04.Description"));
        jButtonReferenceCustom05.setText(PropertyHandler.getInstance().getValue("Custom.Reference.05.Description"));
        jButtonReferenceCustom06.setText(PropertyHandler.getInstance().getValue("Custom.Reference.06.Description"));
        jButtonReferenceCustom07.setText(PropertyHandler.getInstance().getValue("Custom.Reference.07.Description"));
        jButtonReferenceCustom08.setText(PropertyHandler.getInstance().getValue("Custom.Reference.08.Description"));
        jButtonReferenceCustom09.setText(PropertyHandler.getInstance().getValue("Custom.Reference.09.Description"));
        jButtonReferenceCustom10.setText(PropertyHandler.getInstance().getValue("Custom.Reference.10.Description"));
        jButtonReferenceCustom11.setText(PropertyHandler.getInstance().getValue("Custom.Reference.11.Description"));
        jButtonReferenceCustom12.setText(PropertyHandler.getInstance().getValue("Custom.Reference.12.Description"));
        jButtonReferenceCustom13.setText(PropertyHandler.getInstance().getValue("Custom.Reference.13.Description"));
        jButtonReferenceCustom14.setText(PropertyHandler.getInstance().getValue("Custom.Reference.14.Description"));
        jButtonReferenceCustom15.setText(PropertyHandler.getInstance().getValue("Custom.Reference.15.Description"));
        jButtonReferenceCustom16.setText(PropertyHandler.getInstance().getValue("Custom.Reference.16.Description"));
        jButtonReferenceCustom17.setText(PropertyHandler.getInstance().getValue("Custom.Reference.17.Description"));
        jButtonReferenceCustom18.setText(PropertyHandler.getInstance().getValue("Custom.Reference.18.Description"));
        jButtonReferenceCustom19.setText(PropertyHandler.getInstance().getValue("Custom.Reference.19.Description"));
        jButtonReferenceCustom20.setText(PropertyHandler.getInstance().getValue("Custom.Reference.20.Description"));
        jButtonReferenceCustom21.setText(PropertyHandler.getInstance().getValue("Custom.Reference.21.Description"));
        jButtonReferenceCustom22.setText(PropertyHandler.getInstance().getValue("Custom.Reference.22.Description"));
        jButtonReferenceCustom23.setText(PropertyHandler.getInstance().getValue("Custom.Reference.23.Description"));
        jButtonReferenceCustom24.setText(PropertyHandler.getInstance().getValue("Custom.Reference.24.Description"));
        jButtonReferenceCustom25.setText(PropertyHandler.getInstance().getValue("Custom.Reference.25.Description"));
        jButtonReferenceCustom26.setText(PropertyHandler.getInstance().getValue("Custom.Reference.26.Description"));
        jButtonReferenceCustom27.setText(PropertyHandler.getInstance().getValue("Custom.Reference.27.Description"));
        jButtonReferenceCustom28.setText(PropertyHandler.getInstance().getValue("Custom.Reference.28.Description"));
        jButtonReferenceCustom29.setText(PropertyHandler.getInstance().getValue("Custom.Reference.29.Description"));
        jButtonReferenceCustom30.setText(PropertyHandler.getInstance().getValue("Custom.Reference.30.Description"));
        jButtonReferenceCustom31.setText(PropertyHandler.getInstance().getValue("Custom.Reference.31.Description"));
        jButtonReferenceCustom32.setText(PropertyHandler.getInstance().getValue("Custom.Reference.32.Description"));
        jButtonReferenceCustom33.setText(PropertyHandler.getInstance().getValue("Custom.Reference.33.Description"));

        //--- Load Script button text
        jButtonScriptCustom01.setText(PropertyHandler.getInstance().getValue("Custom.Script.01.Description"));
        jButtonScriptCustom02.setText(PropertyHandler.getInstance().getValue("Custom.Script.02.Description"));
        jButtonScriptCustom03.setText(PropertyHandler.getInstance().getValue("Custom.Script.03.Description"));
        jButtonScriptCustom04.setText(PropertyHandler.getInstance().getValue("Custom.Script.04.Description"));
        jButtonScriptCustom05.setText(PropertyHandler.getInstance().getValue("Custom.Script.05.Description"));
        jButtonScriptCustom06.setText(PropertyHandler.getInstance().getValue("Custom.Script.06.Description"));
        jButtonScriptCustom07.setText(PropertyHandler.getInstance().getValue("Custom.Script.07.Description"));
        jButtonScriptCustom08.setText(PropertyHandler.getInstance().getValue("Custom.Script.08.Description"));
        jButtonScriptCustom09.setText(PropertyHandler.getInstance().getValue("Custom.Script.09.Description"));
        jButtonScriptCustom10.setText(PropertyHandler.getInstance().getValue("Custom.Script.10.Description"));
        jButtonScriptCustom11.setText(PropertyHandler.getInstance().getValue("Custom.Script.11.Description"));
        jButtonScriptCustom12.setText(PropertyHandler.getInstance().getValue("Custom.Script.12.Description"));
        jButtonScriptCustom13.setText(PropertyHandler.getInstance().getValue("Custom.Script.13.Description"));
        jButtonScriptCustom14.setText(PropertyHandler.getInstance().getValue("Custom.Script.14.Description"));
        jButtonScriptCustom15.setText(PropertyHandler.getInstance().getValue("Custom.Script.15.Description"));
        jButtonScriptCustom16.setText(PropertyHandler.getInstance().getValue("Custom.Script.16.Description"));
        jButtonScriptCustom17.setText(PropertyHandler.getInstance().getValue("Custom.Script.17.Description"));
        jButtonScriptCustom18.setText(PropertyHandler.getInstance().getValue("Custom.Script.18.Description"));
        jButtonScriptCustom19.setText(PropertyHandler.getInstance().getValue("Custom.Script.19.Description"));
        jButtonScriptCustom20.setText(PropertyHandler.getInstance().getValue("Custom.Script.20.Description"));
        jButtonScriptCustom21.setText(PropertyHandler.getInstance().getValue("Custom.Script.21.Description"));
        jButtonScriptCustom22.setText(PropertyHandler.getInstance().getValue("Custom.Script.22.Description"));
        jButtonScriptCustom23.setText(PropertyHandler.getInstance().getValue("Custom.Script.23.Description"));
        jButtonScriptCustom24.setText(PropertyHandler.getInstance().getValue("Custom.Script.24.Description"));
        jButtonScriptCustom25.setText(PropertyHandler.getInstance().getValue("Custom.Script.25.Description"));
        jButtonScriptCustom26.setText(PropertyHandler.getInstance().getValue("Custom.Script.26.Description"));
        jButtonScriptCustom27.setText(PropertyHandler.getInstance().getValue("Custom.Script.27.Description"));
        jButtonScriptCustom28.setText(PropertyHandler.getInstance().getValue("Custom.Script.28.Description"));
        jButtonScriptCustom29.setText(PropertyHandler.getInstance().getValue("Custom.Script.29.Description"));
        jButtonScriptCustom30.setText(PropertyHandler.getInstance().getValue("Custom.Script.30.Description"));
        jButtonScriptCustom31.setText(PropertyHandler.getInstance().getValue("Custom.Script.31.Description"));
        jButtonScriptCustom32.setText(PropertyHandler.getInstance().getValue("Custom.Script.32.Description"));
        jButtonScriptCustom33.setText(PropertyHandler.getInstance().getValue("Custom.Script.33.Description"));
        jButtonScriptCustom34.setText(PropertyHandler.getInstance().getValue("Custom.Script.34.Description"));
        jButtonScriptCustom35.setText(PropertyHandler.getInstance().getValue("Custom.Script.35.Description"));
        jButtonScriptCustom36.setText(PropertyHandler.getInstance().getValue("Custom.Script.36.Description")); 
    }
    
    

    
    //- Display all the buttons in a list
    private final class ButtonList {
        
        private final Map<String, ImageIcon> imageMap;

        public ButtonList() {
            String[] arrButtonList = null;
            try {
                arrButtonList = this.getResourceListing(launchpad.LaunchPad.class , "launchpad/images/buttons/");
            } catch (URISyntaxException | IOException ex) {
                Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
            }
            //System.out.println("Button List: " + Arrays.toString(arrButtonList));
            Arrays.sort(arrButtonList, Collator.getInstance());
            imageMap = createImageMap(arrButtonList);
            
            //- Remove .png
//            for (int index =0; index < arrButtonList.length; index++){
//                arrButtonList[index] = arrButtonList[index].replace(".png", "");
//            }    
            JList list = new JList(arrButtonList);
            list.setCellRenderer(new ButtonListRenderer());
            JScrollPane scroll = new JScrollPane(list);
            scroll.setPreferredSize(new Dimension(250, 500));
            JFrame frame = new JFrame();
            frame.add(scroll);
            frame.setTitle("Button Listing");
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }

        public class ButtonListRenderer extends DefaultListCellRenderer {


            @Override
            public Component getListCellRendererComponent(
                    JList list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {

                JLabel label = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);
                label.setIcon(imageMap.get((String) value));
                label.setHorizontalTextPosition(JLabel.RIGHT);
                //label.setFont(font);
                (imageMap.get((String) value)).setImageObserver(label);
                return label;
            }
        }

        private Map<String, ImageIcon> createImageMap(String[] list) {
            Map<String, ImageIcon> map = new HashMap<>();
            for (String s : list) {
                ImageIcon imageIconPreResize = new ImageIcon( getClass().getResource("/launchpad/images/buttons/" + s));
                Image image = imageIconPreResize.getImage(); // transform it 
                Image newimg = image.getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
                ImageIcon imageIconPostResize= new ImageIcon(newimg);                
                
                
                map.put(s, imageIconPostResize );
            }
            return map;
        }

        
      String[] getResourceListing(Class clazz, String path) throws URISyntaxException, IOException {
      URL dirURL = clazz.getClassLoader().getResource(path);
      if (dirURL != null && dirURL.getProtocol().equals("file")) {
        /* A file path: easy enough */
        return new File(dirURL.toURI()).list();
      } 

      if (dirURL == null) {
        /* 
         * In case of a jar file, we can't actually find a directory.
         * Have to assume the same jar as clazz.
         */
        String me = clazz.getName().replace(".", "/")+".class";
        dirURL = clazz.getClassLoader().getResource(me);
      }
      
      if (dirURL.getProtocol().equals("jar")) {
        /* A JAR path */
        String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); //strip out only the JAR file
        JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
        Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
        Set<String> result = new HashSet<>(); //avoid duplicates in case it is a subdirectory
        while(entries.hasMoreElements()) {
          String name = entries.nextElement().getName();
          if (name.startsWith(path)) { //filter according to the path
            String entry = name.substring(path.length());
            int checkSubdir = entry.indexOf("/");
            if (checkSubdir >= 0) {
              // if it is a subdirectory, we just return the directory name
              entry = entry.substring(0, checkSubdir);
            }
            result.add(entry);
          }
        }
        return result.toArray(new String[result.size()]);
      } 
        
      throw new UnsupportedOperationException("Cannot list files for URL "+dirURL);
  }
    }
    

      
    //--- Hash generating stuff
    public enum HashGenerate {
        MD5("MD5"),
        SHA1("SHA1"),
        SHA256("SHA-256"),
        SHA512("SHA-512");
        private final String name;
        HashGenerate(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }
        public byte[] checksum(File input) {
            try (InputStream in = new FileInputStream(input)) {
                MessageDigest digest = MessageDigest.getInstance(getName());
                byte[] block = new byte[4096];
                int length;
                while ((length = in.read(block)) > 0) {
                    digest.update(block, 0, length);
                }
                return digest.digest();
            } catch (Exception e) {
            }
            return null;
        }
        public static String toHex(byte[] bytes) {
            return DatatypeConverter.printHexBinary(bytes);
        }
    }



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator1 = new javax.swing.JSeparator();
        buttonGroupSSHClient = new javax.swing.ButtonGroup();
        buttonGroupConsoleClient = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        buttonGroupLanguage = new javax.swing.ButtonGroup();
        buttonGroupPWauthEnableDisable = new javax.swing.ButtonGroup();
        jTabbedMain = new javax.swing.JTabbedPane();
        jPanelMain = new javax.swing.JPanel();
        jScrollPaneSessionList = new javax.swing.JScrollPane();
        jListSessions = new javax.swing.JList<>();
        jPanelMainRightSide = new javax.swing.JPanel();
        jTextFieldConnectHostname = new javax.swing.JTextField();
        jButtonExecuteFunctionHTTPS = new javax.swing.JButton();
        jButtonExecuteFunctionSSH = new javax.swing.JButton();
        jTextFieldConnectUsername = new javax.swing.JTextField();
        jPasswordFieldConnectPassword = new javax.swing.JPasswordField();
        jSeparator3 = new javax.swing.JSeparator();
        jTextFieldPingHostname = new javax.swing.JTextField();
        jCheckBoxDNS = new javax.swing.JCheckBox();
        jButtonTracert = new javax.swing.JButton();
        jButtonPing = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        jComboBoxConsoleCOM = new javax.swing.JComboBox<>();
        jButtonShowCOMList = new javax.swing.JButton();
        jComboBoxConsoleBaud = new javax.swing.JComboBox<>();
        jButtonConsole = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        jButtonMainButton1 = new javax.swing.JButton();
        jButtonMainButton2 = new javax.swing.JButton();
        jButtonMainButton3 = new javax.swing.JButton();
        jButtonMainButton4 = new javax.swing.JButton();
        jButtonMainButton8 = new javax.swing.JButton();
        jButtonMainButton12 = new javax.swing.JButton();
        jButtonMainButton7 = new javax.swing.JButton();
        jButtonMainButton11 = new javax.swing.JButton();
        jButtonMainButton6 = new javax.swing.JButton();
        jButtonMainButton10 = new javax.swing.JButton();
        jButtonMainButton5 = new javax.swing.JButton();
        jButtonMainButton9 = new javax.swing.JButton();
        jButtonMainButton13 = new javax.swing.JButton();
        jButtonMainButton14 = new javax.swing.JButton();
        jButtonMainButton15 = new javax.swing.JButton();
        jButtonMainButton16 = new javax.swing.JButton();
        jButtonMainButton20 = new javax.swing.JButton();
        jButtonMainButton19 = new javax.swing.JButton();
        jButtonMainButton18 = new javax.swing.JButton();
        jButtonMainButton17 = new javax.swing.JButton();
        jCheckBoxAlternateLogin = new javax.swing.JCheckBox();
        jButtonExecuteFunctionRDP = new javax.swing.JButton();
        jTextFieldTCPTestPort = new javax.swing.JTextField();
        jButtonTCP = new javax.swing.JButton();
        jLabelLocalMAC = new javax.swing.JLabel();
        jLabelLocalHostname = new javax.swing.JLabel();
        jLabelLocalIP = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        jButtonRefreshHostnameIPMAC = new javax.swing.JButton();
        jButtonExecuteFunctionPKI = new javax.swing.JButton();
        jButtonMainButton24 = new javax.swing.JButton();
        jButtonMainButton23 = new javax.swing.JButton();
        jButtonMainButton22 = new javax.swing.JButton();
        jButtonMainButton21 = new javax.swing.JButton();
        jButtonNetworkConnections = new javax.swing.JButton();
        jTextFieldFilter = new javax.swing.JTextField();
        jCheckBoxFavorites = new javax.swing.JCheckBox();
        jButtonClearFilter = new javax.swing.JButton();
        jPanelAppsCustom = new javax.swing.JPanel();
        jButtonLinkCustom01 = new javax.swing.JButton();
        jButtonLinkCustom02 = new javax.swing.JButton();
        jButtonLinkCustom03 = new javax.swing.JButton();
        jButtonLinkCustom04 = new javax.swing.JButton();
        jButtonLinkCustom05 = new javax.swing.JButton();
        jButtonLinkCustom06 = new javax.swing.JButton();
        jButtonLinkCustom07 = new javax.swing.JButton();
        jButtonLinkCustom08 = new javax.swing.JButton();
        jButtonLinkCustom09 = new javax.swing.JButton();
        jButtonLinkCustom11 = new javax.swing.JButton();
        jButtonLinkCustom10 = new javax.swing.JButton();
        jButtonLinkCustom12 = new javax.swing.JButton();
        jButtonLinkCustom13 = new javax.swing.JButton();
        jButtonLinkCustom14 = new javax.swing.JButton();
        jButtonLinkCustom15 = new javax.swing.JButton();
        jButtonLinkCustom16 = new javax.swing.JButton();
        jButtonLinkCustom17 = new javax.swing.JButton();
        jButtonLinkCustom18 = new javax.swing.JButton();
        jButtonLinkCustom19 = new javax.swing.JButton();
        jButtonLinkCustom20 = new javax.swing.JButton();
        jButtonLinkCustom21 = new javax.swing.JButton();
        jButtonLinkCustom22 = new javax.swing.JButton();
        jButtonLinkCustom23 = new javax.swing.JButton();
        jButtonLinkCustom24 = new javax.swing.JButton();
        jButtonLinkCustom25 = new javax.swing.JButton();
        jButtonLinkCustom26 = new javax.swing.JButton();
        jButtonLinkCustom27 = new javax.swing.JButton();
        jButtonLinkCustom28 = new javax.swing.JButton();
        jButtonLinkCustom29 = new javax.swing.JButton();
        jButtonLinkCustom30 = new javax.swing.JButton();
        jButtonLinkCustom31 = new javax.swing.JButton();
        jButtonLinkCustom32 = new javax.swing.JButton();
        jButtonLinkCustom33 = new javax.swing.JButton();
        jButtonLinkCustom34 = new javax.swing.JButton();
        jButtonLinkCustom35 = new javax.swing.JButton();
        jButtonLinkCustom36 = new javax.swing.JButton();
        jTextFieldLinksFilter = new javax.swing.JTextField();
        jLabelLinksFilter = new javax.swing.JLabel();
        jPanelReference = new javax.swing.JPanel();
        jToggleOnlineOfflineMode = new javax.swing.JToggleButton();
        jButtonReferenceCustom01 = new javax.swing.JButton();
        jButtonReferenceCustom02 = new javax.swing.JButton();
        jButtonReferenceCustom03 = new javax.swing.JButton();
        jButtonReferenceCustom04 = new javax.swing.JButton();
        jButtonReferenceCustom05 = new javax.swing.JButton();
        jButtonReferenceCustom06 = new javax.swing.JButton();
        jButtonReferenceCustom07 = new javax.swing.JButton();
        jButtonReferenceCustom08 = new javax.swing.JButton();
        jButtonReferenceCustom09 = new javax.swing.JButton();
        jButtonReferenceCustom10 = new javax.swing.JButton();
        jButtonReferenceCustom11 = new javax.swing.JButton();
        jButtonReferenceCustom12 = new javax.swing.JButton();
        jButtonReferenceCustom13 = new javax.swing.JButton();
        jButtonReferenceCustom14 = new javax.swing.JButton();
        jButtonReferenceCustom15 = new javax.swing.JButton();
        jButtonReferenceCustom16 = new javax.swing.JButton();
        jButtonReferenceCustom17 = new javax.swing.JButton();
        jButtonReferenceCustom18 = new javax.swing.JButton();
        jButtonReferenceCustom19 = new javax.swing.JButton();
        jButtonReferenceCustom20 = new javax.swing.JButton();
        jButtonReferenceCustom21 = new javax.swing.JButton();
        jButtonReferenceCustom22 = new javax.swing.JButton();
        jButtonReferenceCustom23 = new javax.swing.JButton();
        jButtonReferenceCustom24 = new javax.swing.JButton();
        jButtonReferenceCustom25 = new javax.swing.JButton();
        jButtonReferenceCustom26 = new javax.swing.JButton();
        jButtonReferenceCustom27 = new javax.swing.JButton();
        jButtonReferenceCustom28 = new javax.swing.JButton();
        jButtonReferenceCustom29 = new javax.swing.JButton();
        jButtonReferenceCustom30 = new javax.swing.JButton();
        jButtonReferenceCustom31 = new javax.swing.JButton();
        jButtonReferenceCustom32 = new javax.swing.JButton();
        jButtonReferenceCustom33 = new javax.swing.JButton();
        jTextFieldReferenceFilter = new javax.swing.JTextField();
        jLabelReferenceFilter = new javax.swing.JLabel();
        jPanelScripts = new javax.swing.JPanel();
        jButtonScriptCustom03 = new javax.swing.JButton();
        jButtonScriptCustom01 = new javax.swing.JButton();
        jButtonScriptCustom02 = new javax.swing.JButton();
        jButtonScriptCustom06 = new javax.swing.JButton();
        jButtonScriptCustom04 = new javax.swing.JButton();
        jButtonScriptCustom05 = new javax.swing.JButton();
        jButtonScriptCustom08 = new javax.swing.JButton();
        jButtonScriptCustom09 = new javax.swing.JButton();
        jButtonScriptCustom07 = new javax.swing.JButton();
        jButtonScriptCustom12 = new javax.swing.JButton();
        jButtonScriptCustom15 = new javax.swing.JButton();
        jButtonScriptCustom11 = new javax.swing.JButton();
        jButtonScriptCustom10 = new javax.swing.JButton();
        jButtonScriptCustom13 = new javax.swing.JButton();
        jButtonScriptCustom14 = new javax.swing.JButton();
        jButtonScriptCustom18 = new javax.swing.JButton();
        jButtonScriptCustom16 = new javax.swing.JButton();
        jButtonScriptCustom17 = new javax.swing.JButton();
        jButtonScriptCustom20 = new javax.swing.JButton();
        jButtonScriptCustom21 = new javax.swing.JButton();
        jButtonScriptCustom19 = new javax.swing.JButton();
        jButtonScriptCustom24 = new javax.swing.JButton();
        jButtonScriptCustom22 = new javax.swing.JButton();
        jButtonScriptCustom23 = new javax.swing.JButton();
        jButtonScriptCustom26 = new javax.swing.JButton();
        jButtonScriptCustom27 = new javax.swing.JButton();
        jButtonScriptCustom25 = new javax.swing.JButton();
        jButtonScriptCustom29 = new javax.swing.JButton();
        jButtonScriptCustom30 = new javax.swing.JButton();
        jButtonScriptCustom28 = new javax.swing.JButton();
        jButtonScriptCustom32 = new javax.swing.JButton();
        jButtonScriptCustom33 = new javax.swing.JButton();
        jButtonScriptCustom31 = new javax.swing.JButton();
        jButtonScriptCustom34 = new javax.swing.JButton();
        jButtonScriptCustom35 = new javax.swing.JButton();
        jButtonScriptCustom36 = new javax.swing.JButton();
        jTextFieldScriptsFilter = new javax.swing.JTextField();
        jLabelScriptsFilter = new javax.swing.JLabel();
        jTabbedPaneToolBox = new javax.swing.JTabbedPane();
        jPanelWebJavaDocs = new javax.swing.JPanel();
        jPanelDocuments = new javax.swing.JPanel();
        jButton24 = new javax.swing.JButton();
        jButton36 = new javax.swing.JButton();
        jButton37 = new javax.swing.JButton();
        jButton42 = new javax.swing.JButton();
        jPanelJavaApps = new javax.swing.JPanel();
        jButton41 = new javax.swing.JButton();
        jPanelWebApps = new javax.swing.JPanel();
        jButtonJSDiff2 = new javax.swing.JButton();
        jButtonConfigBuilder1 = new javax.swing.JButton();
        jButtonSubnetCalculator = new javax.swing.JButton();
        jButtonSubnetCalculator1 = new javax.swing.JButton();
        jButtonIPv4SubnetCalculator = new javax.swing.JButton();
        jButtonRomajiToHiraKata = new javax.swing.JButton();
        jButtonIconGenerator = new javax.swing.JButton();
        jPanelZipEncrypt = new javax.swing.JPanel();
        jButtonFolderToZip = new javax.swing.JButton();
        jTextFieldZipSourceFolder = new javax.swing.JTextField();
        jTextFieldZipFilename = new javax.swing.JTextField();
        jButton25 = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jPasswordFieldZip = new javax.swing.JPasswordField();
        jLabel18 = new javax.swing.JLabel();
        jProgressBarZip = new javax.swing.JProgressBar();
        jLabelFolderToZip4 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        jComboBoxZipEncMethod = new javax.swing.JComboBox<>();
        jLabelFolderToZip7 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jTextFieldZipSourceFile = new javax.swing.JTextField();
        jButtonZipBrowseSourceZip = new javax.swing.JButton();
        jLabel34 = new javax.swing.JLabel();
        jTextFieldZipDestinationFolder = new javax.swing.JTextField();
        jButtonZipBrowseDestinationFolder = new javax.swing.JButton();
        jButton39 = new javax.swing.JButton();
        jProgressBarZipExtract = new javax.swing.JProgressBar();
        jLabel17 = new javax.swing.JLabel();
        jPasswordFieldZipConfirm = new javax.swing.JPasswordField();
        jLabel35 = new javax.swing.JLabel();
        jPasswordFieldZipExtract = new javax.swing.JPasswordField();
        jPanelType7 = new javax.swing.JPanel();
        jTextFieldType7Input = new javax.swing.JTextField();
        jButton21 = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();
        jTextFieldType7Output = new javax.swing.JTextField();
        jLabelType7reverse = new javax.swing.JLabel();
        jButton23 = new javax.swing.JButton();
        jPanelHashGen = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jButtonGenerateHash = new javax.swing.JButton();
        jTextFieldFileHashGenerate = new javax.swing.JTextField();
        jButton27 = new javax.swing.JButton();
        jTextFieldHashSHA512 = new javax.swing.JTextField();
        jTextFieldHashMD5 = new javax.swing.JTextField();
        jTextFieldHashSHA1 = new javax.swing.JTextField();
        jTextFieldHashSHA256 = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabelHashGenerator = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaHash = new javax.swing.JTextArea();
        jButtonHashCopyMD5 = new javax.swing.JButton();
        jButtonHashCopySHA512 = new javax.swing.JButton();
        jButtonHashCopySHA1 = new javax.swing.JButton();
        jButtonHashCopySHA256 = new javax.swing.JButton();
        jPanelNTPTime = new javax.swing.JPanel();
        jTextFieldNtpServer = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabelGetNTP1 = new javax.swing.JLabel();
        jScrollPaneNTPMessage = new javax.swing.JScrollPane();
        jTextAreaNTPMessage = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jPanelToolboxScripts = new javax.swing.JPanel();
        jPanelScanning = new javax.swing.JPanel();
        jButtonScriptPowershellPingSweepRange = new javax.swing.JButton();
        jButtonScriptTestUDPTCP = new javax.swing.JButton();
        jButtonScriptPingLoggerToFile = new javax.swing.JButton();
        jButtonScriptMTUSweep = new javax.swing.JButton();
        jButtonScriptHashChecker1 = new javax.swing.JButton();
        jPanelSyncing = new javax.swing.JPanel();
        jButtonSyncStandaloneDevices = new javax.swing.JButton();
        jButtonSyncProductionDevices = new javax.swing.JButton();
        jButtonMapSharedFolder = new javax.swing.JButton();
        jButtonEditProductionDevicesList1 = new javax.swing.JButton();
        jButtonEditProductionDevicesList = new javax.swing.JButton();
        jPanelMiscellaneous = new javax.swing.JPanel();
        jButtonScriptCreateDummyFile = new javax.swing.JButton();
        jButtonScriptiPerfServer = new javax.swing.JButton();
        jButtonScriptSendMessage = new javax.swing.JButton();
        jButtonScriptiPerfClient = new javax.swing.JButton();
        jButtonScriptGetNTPTimePS = new javax.swing.JButton();
        jButtonScriptHashChecker = new javax.swing.JButton();
        jButton31 = new javax.swing.JButton();
        jButton45 = new javax.swing.JButton();
        jButton46 = new javax.swing.JButton();
        jButtonScriptKeepAwake = new javax.swing.JButton();
        jButtonCiscoType7Decrypt = new javax.swing.JButton();
        jTabbedPaneSettings = new javax.swing.JTabbedPane();
        jPanelSettingsMain = new javax.swing.JPanel();
        jButtonReportIssue = new javax.swing.JButton();
        jTextField2 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jButton38 = new javax.swing.JButton();
        jButton32 = new javax.swing.JButton();
        jButton30 = new javax.swing.JButton();
        jButton43 = new javax.swing.JButton();
        jLabelListTextSizePreview = new javax.swing.JLabel();
        jSliderListTextSize = new javax.swing.JSlider();
        jLabelListTextSize1 = new javax.swing.JLabel();
        jRadioButtonJapanese = new javax.swing.JRadioButton();
        jRadioButtonEnglish = new javax.swing.JRadioButton();
        jLabelLanguageSelect = new javax.swing.JLabel();
        jButtonCopyShortcutToDesktop = new javax.swing.JButton();
        jCheckBoxChangePingOnSessionSelect = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        jLabelEnablePWauth = new javax.swing.JLabel();
        jRadioButtonPWauthEnabled = new javax.swing.JRadioButton();
        jRadioButtonPWauthDisabled = new javax.swing.JRadioButton();
        jComboBoxClassification = new javax.swing.JComboBox<>();
        jLabelEnablePWauth1 = new javax.swing.JLabel();
        jButton28 = new javax.swing.JButton();
        jButton34 = new javax.swing.JButton();
        jButton35 = new javax.swing.JButton();
        jLabelEnablePWauth2 = new javax.swing.JLabel();
        jTextFieldSecureCRTPath = new javax.swing.JTextField();
        jLabelEnablePWauth3 = new javax.swing.JLabel();
        jTextFieldPreloadedIP = new javax.swing.JTextField();
        jLabelEnablePWauth4 = new javax.swing.JLabel();
        jTextFieldPreloadedPingIP = new javax.swing.JTextField();
        jLabelListTextSize3 = new javax.swing.JLabel();
        jComboBoxSessionDoubleClick = new javax.swing.JComboBox<>();
        jLabelListTextSize4 = new javax.swing.JLabel();
        jComboBoxSessionEnterPress = new javax.swing.JComboBox<>();
        jLabelEnablePWauth5 = new javax.swing.JLabel();
        jTextFieldSettingPreferredIpTertiary = new javax.swing.JTextField();
        jTextFieldSettingPreferredIpPrimary = new javax.swing.JTextField();
        jTextFieldSettingPreferredIpSecondary = new javax.swing.JTextField();
        jButtonScriptUpdateLaunchPad = new javax.swing.JButton();
        jButtonViewExampleUpdateScript = new javax.swing.JButton();
        jScrollPaneSettingsButtons = new javax.swing.JScrollPane();
        jPanelSettingsButtons = new javax.swing.JPanel();
        jButton33 = new javax.swing.JButton();
        jTextFieldButtonExecute1 = new javax.swing.JTextField();
        jLabelButtonToolTip1 = new javax.swing.JLabel();
        jTextFieldButtonToolTip1 = new javax.swing.JTextField();
        jLabelButtonIcon1 = new javax.swing.JLabel();
        jLabelButtonExecute1 = new javax.swing.JLabel();
        jComboBoxButtonIcon1 = new javax.swing.JComboBox<>();
        jTextFieldButtonExecute2 = new javax.swing.JTextField();
        jLabelButtonToolTip2 = new javax.swing.JLabel();
        jTextFieldButtonToolTip2 = new javax.swing.JTextField();
        jLabelButtonIcon2 = new javax.swing.JLabel();
        jLabelButtonExecute2 = new javax.swing.JLabel();
        jComboBoxButtonIcon2 = new javax.swing.JComboBox<>();
        jTextFieldButtonExecute3 = new javax.swing.JTextField();
        jLabelButtonToolTip3 = new javax.swing.JLabel();
        jTextFieldButtonToolTip3 = new javax.swing.JTextField();
        jLabelButtonIcon3 = new javax.swing.JLabel();
        jLabelButtonExecute3 = new javax.swing.JLabel();
        jComboBoxButtonIcon3 = new javax.swing.JComboBox<>();
        jTextFieldButtonExecute4 = new javax.swing.JTextField();
        jLabelButtonToolTip4 = new javax.swing.JLabel();
        jTextFieldButtonToolTip4 = new javax.swing.JTextField();
        jLabelButtonIcon4 = new javax.swing.JLabel();
        jLabelButtonExecute4 = new javax.swing.JLabel();
        jComboBoxButtonIcon4 = new javax.swing.JComboBox<>();
        jTextFieldButtonExecute5 = new javax.swing.JTextField();
        jLabelButtonToolTip5 = new javax.swing.JLabel();
        jTextFieldButtonToolTip5 = new javax.swing.JTextField();
        jLabelButtonIcon5 = new javax.swing.JLabel();
        jLabelButtonExecute5 = new javax.swing.JLabel();
        jComboBoxButtonIcon5 = new javax.swing.JComboBox<>();
        jTextFieldButtonExecute6 = new javax.swing.JTextField();
        jLabelButtonToolTip6 = new javax.swing.JLabel();
        jTextFieldButtonToolTip6 = new javax.swing.JTextField();
        jLabelButtonIcon6 = new javax.swing.JLabel();
        jLabelButtonExecute6 = new javax.swing.JLabel();
        jComboBoxButtonIcon6 = new javax.swing.JComboBox<>();
        jTextFieldButtonExecute7 = new javax.swing.JTextField();
        jLabelButtonToolTip7 = new javax.swing.JLabel();
        jTextFieldButtonToolTip7 = new javax.swing.JTextField();
        jLabelButtonIcon7 = new javax.swing.JLabel();
        jLabelButtonExecute7 = new javax.swing.JLabel();
        jComboBoxButtonIcon7 = new javax.swing.JComboBox<>();
        jTextFieldButtonExecute8 = new javax.swing.JTextField();
        jLabelButtonToolTip8 = new javax.swing.JLabel();
        jTextFieldButtonToolTip8 = new javax.swing.JTextField();
        jLabelButtonIcon8 = new javax.swing.JLabel();
        jLabelButtonExecute8 = new javax.swing.JLabel();
        jComboBoxButtonIcon8 = new javax.swing.JComboBox<>();
        jLabelButtonExecute9 = new javax.swing.JLabel();
        jComboBoxButtonIcon9 = new javax.swing.JComboBox<>();
        jTextFieldButtonExecute9 = new javax.swing.JTextField();
        jLabelButtonToolTip9 = new javax.swing.JLabel();
        jTextFieldButtonToolTip9 = new javax.swing.JTextField();
        jLabelButtonIcon9 = new javax.swing.JLabel();
        jLabelButtonExecute10 = new javax.swing.JLabel();
        jComboBoxButtonIcon10 = new javax.swing.JComboBox<>();
        jTextFieldButtonExecute10 = new javax.swing.JTextField();
        jLabelButtonToolTip10 = new javax.swing.JLabel();
        jTextFieldButtonToolTip10 = new javax.swing.JTextField();
        jLabelButtonIcon10 = new javax.swing.JLabel();
        jLabelButtonExecute11 = new javax.swing.JLabel();
        jComboBoxButtonIcon11 = new javax.swing.JComboBox<>();
        jTextFieldButtonExecute11 = new javax.swing.JTextField();
        jLabelButtonToolTip11 = new javax.swing.JLabel();
        jTextFieldButtonToolTip11 = new javax.swing.JTextField();
        jLabelButtonIcon11 = new javax.swing.JLabel();
        jLabelButtonExecute12 = new javax.swing.JLabel();
        jComboBoxButtonIcon12 = new javax.swing.JComboBox<>();
        jTextFieldButtonExecute12 = new javax.swing.JTextField();
        jLabelButtonToolTip12 = new javax.swing.JLabel();
        jTextFieldButtonToolTip12 = new javax.swing.JTextField();
        jLabelButtonIcon12 = new javax.swing.JLabel();
        jLabelButtonExecute13 = new javax.swing.JLabel();
        jComboBoxButtonIcon13 = new javax.swing.JComboBox<>();
        jTextFieldButtonExecute13 = new javax.swing.JTextField();
        jLabelButtonToolTip13 = new javax.swing.JLabel();
        jTextFieldButtonToolTip13 = new javax.swing.JTextField();
        jLabelButtonIcon13 = new javax.swing.JLabel();
        jLabelButtonExecute14 = new javax.swing.JLabel();
        jComboBoxButtonIcon14 = new javax.swing.JComboBox<>();
        jTextFieldButtonExecute14 = new javax.swing.JTextField();
        jLabelButtonToolTip14 = new javax.swing.JLabel();
        jTextFieldButtonToolTip14 = new javax.swing.JTextField();
        jLabelButtonIcon14 = new javax.swing.JLabel();
        jLabelButtonExecute15 = new javax.swing.JLabel();
        jComboBoxButtonIcon15 = new javax.swing.JComboBox<>();
        jTextFieldButtonExecute15 = new javax.swing.JTextField();
        jLabelButtonToolTip15 = new javax.swing.JLabel();
        jTextFieldButtonToolTip15 = new javax.swing.JTextField();
        jLabelButtonIcon15 = new javax.swing.JLabel();
        jLabelButtonExecute16 = new javax.swing.JLabel();
        jComboBoxButtonIcon16 = new javax.swing.JComboBox<>();
        jTextFieldButtonExecute16 = new javax.swing.JTextField();
        jLabelButtonToolTip16 = new javax.swing.JLabel();
        jTextFieldButtonToolTip16 = new javax.swing.JTextField();
        jLabelButtonIcon16 = new javax.swing.JLabel();
        jLabelButtonExecute17 = new javax.swing.JLabel();
        jComboBoxButtonIcon17 = new javax.swing.JComboBox<>();
        jTextFieldButtonExecute17 = new javax.swing.JTextField();
        jLabelButtonToolTip17 = new javax.swing.JLabel();
        jTextFieldButtonToolTip17 = new javax.swing.JTextField();
        jLabelButtonIcon17 = new javax.swing.JLabel();
        jLabelButtonExecute18 = new javax.swing.JLabel();
        jComboBoxButtonIcon18 = new javax.swing.JComboBox<>();
        jTextFieldButtonExecute18 = new javax.swing.JTextField();
        jLabelButtonToolTip18 = new javax.swing.JLabel();
        jTextFieldButtonToolTip18 = new javax.swing.JTextField();
        jLabelButtonIcon18 = new javax.swing.JLabel();
        jLabelButtonExecute19 = new javax.swing.JLabel();
        jComboBoxButtonIcon19 = new javax.swing.JComboBox<>();
        jTextFieldButtonExecute19 = new javax.swing.JTextField();
        jLabelButtonToolTip19 = new javax.swing.JLabel();
        jTextFieldButtonToolTip19 = new javax.swing.JTextField();
        jLabelButtonIcon19 = new javax.swing.JLabel();
        jLabelButtonExecute20 = new javax.swing.JLabel();
        jComboBoxButtonIcon20 = new javax.swing.JComboBox<>();
        jTextFieldButtonExecute20 = new javax.swing.JTextField();
        jLabelButtonToolTip20 = new javax.swing.JLabel();
        jTextFieldButtonToolTip20 = new javax.swing.JTextField();
        jLabelButtonIcon20 = new javax.swing.JLabel();
        jButton40 = new javax.swing.JButton();
        jLabelButtonExecute21 = new javax.swing.JLabel();
        jComboBoxButtonIcon21 = new javax.swing.JComboBox<>();
        jTextFieldButtonExecute21 = new javax.swing.JTextField();
        jLabelButtonToolTip21 = new javax.swing.JLabel();
        jTextFieldButtonToolTip21 = new javax.swing.JTextField();
        jLabelButtonIcon21 = new javax.swing.JLabel();
        jLabelButtonExecute22 = new javax.swing.JLabel();
        jComboBoxButtonIcon22 = new javax.swing.JComboBox<>();
        jTextFieldButtonExecute22 = new javax.swing.JTextField();
        jLabelButtonToolTip22 = new javax.swing.JLabel();
        jTextFieldButtonToolTip22 = new javax.swing.JTextField();
        jLabelButtonIcon22 = new javax.swing.JLabel();
        jLabelButtonExecute23 = new javax.swing.JLabel();
        jComboBoxButtonIcon23 = new javax.swing.JComboBox<>();
        jTextFieldButtonExecute23 = new javax.swing.JTextField();
        jLabelButtonToolTip23 = new javax.swing.JLabel();
        jTextFieldButtonToolTip23 = new javax.swing.JTextField();
        jLabelButtonIcon23 = new javax.swing.JLabel();
        jLabelButtonExecute24 = new javax.swing.JLabel();
        jComboBoxButtonIcon24 = new javax.swing.JComboBox<>();
        jTextFieldButtonExecute24 = new javax.swing.JTextField();
        jLabelButtonToolTip24 = new javax.swing.JLabel();
        jTextFieldButtonToolTip24 = new javax.swing.JTextField();
        jLabelButtonIcon24 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPaneSettingsLinks = new javax.swing.JScrollPane();
        jPanelSettingsLinks = new javax.swing.JPanel();
        jLabelLinkText1 = new javax.swing.JLabel();
        jTextFieldLinkText1 = new javax.swing.JTextField();
        jLabelLinkExecute1 = new javax.swing.JLabel();
        jTextFieldLinkExecute1 = new javax.swing.JTextField();
        jButton44 = new javax.swing.JButton();
        jTextFieldLinkText2 = new javax.swing.JTextField();
        jLabelLinkText2 = new javax.swing.JLabel();
        jTextFieldLinkExecute2 = new javax.swing.JTextField();
        jLabelLinkExecute2 = new javax.swing.JLabel();
        jLabelLinkExecute3 = new javax.swing.JLabel();
        jTextFieldLinkExecute3 = new javax.swing.JTextField();
        jLabelLinkText3 = new javax.swing.JLabel();
        jTextFieldLinkText3 = new javax.swing.JTextField();
        jLabelLinkExecute4 = new javax.swing.JLabel();
        jTextFieldLinkExecute4 = new javax.swing.JTextField();
        jLabelLinkText4 = new javax.swing.JLabel();
        jTextFieldLinkText4 = new javax.swing.JTextField();
        jLabelLinkExecute5 = new javax.swing.JLabel();
        jTextFieldLinkExecute5 = new javax.swing.JTextField();
        jLabelLinkText5 = new javax.swing.JLabel();
        jTextFieldLinkText5 = new javax.swing.JTextField();
        jLabelLinkExecute6 = new javax.swing.JLabel();
        jTextFieldLinkExecute6 = new javax.swing.JTextField();
        jLabelLinkText6 = new javax.swing.JLabel();
        jTextFieldLinkText6 = new javax.swing.JTextField();
        jLabelLinkExecute7 = new javax.swing.JLabel();
        jTextFieldLinkExecute7 = new javax.swing.JTextField();
        jLabelLinkText7 = new javax.swing.JLabel();
        jTextFieldLinkText7 = new javax.swing.JTextField();
        jLabelLinkExecute8 = new javax.swing.JLabel();
        jTextFieldLinkExecute8 = new javax.swing.JTextField();
        jLabelLinkText8 = new javax.swing.JLabel();
        jTextFieldLinkText8 = new javax.swing.JTextField();
        jLabelLinkExecute9 = new javax.swing.JLabel();
        jTextFieldLinkExecute9 = new javax.swing.JTextField();
        jLabelLinkText9 = new javax.swing.JLabel();
        jTextFieldLinkText9 = new javax.swing.JTextField();
        jLabelLinkExecute10 = new javax.swing.JLabel();
        jTextFieldLinkExecute10 = new javax.swing.JTextField();
        jLabelLinkText10 = new javax.swing.JLabel();
        jTextFieldLinkText10 = new javax.swing.JTextField();
        jLabelLinkExecute11 = new javax.swing.JLabel();
        jTextFieldLinkExecute11 = new javax.swing.JTextField();
        jLabelLinkText11 = new javax.swing.JLabel();
        jTextFieldLinkText11 = new javax.swing.JTextField();
        jLabelLinkExecute12 = new javax.swing.JLabel();
        jTextFieldLinkExecute12 = new javax.swing.JTextField();
        jLabelLinkText12 = new javax.swing.JLabel();
        jTextFieldLinkText12 = new javax.swing.JTextField();
        jLabelLinkExecute13 = new javax.swing.JLabel();
        jTextFieldLinkExecute13 = new javax.swing.JTextField();
        jLabelLinkText13 = new javax.swing.JLabel();
        jTextFieldLinkText13 = new javax.swing.JTextField();
        jLabelLinkExecute14 = new javax.swing.JLabel();
        jTextFieldLinkExecute14 = new javax.swing.JTextField();
        jLabelLinkText14 = new javax.swing.JLabel();
        jTextFieldLinkText14 = new javax.swing.JTextField();
        jLabelLinkExecute15 = new javax.swing.JLabel();
        jTextFieldLinkExecute15 = new javax.swing.JTextField();
        jLabelLinkText15 = new javax.swing.JLabel();
        jTextFieldLinkText15 = new javax.swing.JTextField();
        jLabelLinkExecute16 = new javax.swing.JLabel();
        jTextFieldLinkExecute16 = new javax.swing.JTextField();
        jLabelLinkText16 = new javax.swing.JLabel();
        jTextFieldLinkText16 = new javax.swing.JTextField();
        jLabelLinkExecute17 = new javax.swing.JLabel();
        jTextFieldLinkExecute17 = new javax.swing.JTextField();
        jLabelLinkText17 = new javax.swing.JLabel();
        jTextFieldLinkText17 = new javax.swing.JTextField();
        jLabelLinkExecute18 = new javax.swing.JLabel();
        jTextFieldLinkExecute18 = new javax.swing.JTextField();
        jLabelLinkText18 = new javax.swing.JLabel();
        jTextFieldLinkText18 = new javax.swing.JTextField();
        jLabelLinkExecute19 = new javax.swing.JLabel();
        jTextFieldLinkExecute19 = new javax.swing.JTextField();
        jLabelLinkText19 = new javax.swing.JLabel();
        jTextFieldLinkText19 = new javax.swing.JTextField();
        jLabelLinkExecute20 = new javax.swing.JLabel();
        jTextFieldLinkExecute20 = new javax.swing.JTextField();
        jLabelLinkText20 = new javax.swing.JLabel();
        jTextFieldLinkText20 = new javax.swing.JTextField();
        jLabelLinkExecute21 = new javax.swing.JLabel();
        jTextFieldLinkExecute21 = new javax.swing.JTextField();
        jLabelLinkText21 = new javax.swing.JLabel();
        jTextFieldLinkText21 = new javax.swing.JTextField();
        jLabelLinkExecute22 = new javax.swing.JLabel();
        jTextFieldLinkExecute22 = new javax.swing.JTextField();
        jLabelLinkText22 = new javax.swing.JLabel();
        jTextFieldLinkText22 = new javax.swing.JTextField();
        jLabelLinkExecute23 = new javax.swing.JLabel();
        jTextFieldLinkExecute23 = new javax.swing.JTextField();
        jLabelLinkText23 = new javax.swing.JLabel();
        jTextFieldLinkText23 = new javax.swing.JTextField();
        jLabelLinkExecute24 = new javax.swing.JLabel();
        jTextFieldLinkExecute24 = new javax.swing.JTextField();
        jLabelLinkText24 = new javax.swing.JLabel();
        jTextFieldLinkText24 = new javax.swing.JTextField();
        jLabelLinkExecute25 = new javax.swing.JLabel();
        jTextFieldLinkExecute25 = new javax.swing.JTextField();
        jLabelLinkText25 = new javax.swing.JLabel();
        jTextFieldLinkText25 = new javax.swing.JTextField();
        jLabelLinkExecute26 = new javax.swing.JLabel();
        jTextFieldLinkExecute26 = new javax.swing.JTextField();
        jLabelLinkText26 = new javax.swing.JLabel();
        jTextFieldLinkText26 = new javax.swing.JTextField();
        jLabelLinkExecute27 = new javax.swing.JLabel();
        jTextFieldLinkExecute27 = new javax.swing.JTextField();
        jLabelLinkText27 = new javax.swing.JLabel();
        jTextFieldLinkText27 = new javax.swing.JTextField();
        jLabelLinkExecute28 = new javax.swing.JLabel();
        jTextFieldLinkExecute28 = new javax.swing.JTextField();
        jLabelLinkText28 = new javax.swing.JLabel();
        jTextFieldLinkText28 = new javax.swing.JTextField();
        jLabelLinkExecute29 = new javax.swing.JLabel();
        jTextFieldLinkExecute29 = new javax.swing.JTextField();
        jLabelLinkText29 = new javax.swing.JLabel();
        jTextFieldLinkText29 = new javax.swing.JTextField();
        jLabelLinkExecute30 = new javax.swing.JLabel();
        jTextFieldLinkExecute30 = new javax.swing.JTextField();
        jLabelLinkText30 = new javax.swing.JLabel();
        jTextFieldLinkText30 = new javax.swing.JTextField();
        jLabelLinkExecute31 = new javax.swing.JLabel();
        jTextFieldLinkExecute31 = new javax.swing.JTextField();
        jLabelLinkText31 = new javax.swing.JLabel();
        jTextFieldLinkText31 = new javax.swing.JTextField();
        jLabelLinkExecute32 = new javax.swing.JLabel();
        jTextFieldLinkExecute32 = new javax.swing.JTextField();
        jLabelLinkText32 = new javax.swing.JLabel();
        jTextFieldLinkText32 = new javax.swing.JTextField();
        jLabelLinkExecute33 = new javax.swing.JLabel();
        jTextFieldLinkExecute33 = new javax.swing.JTextField();
        jLabelLinkText33 = new javax.swing.JLabel();
        jTextFieldLinkText33 = new javax.swing.JTextField();
        jLabelLinkExecute34 = new javax.swing.JLabel();
        jTextFieldLinkExecute34 = new javax.swing.JTextField();
        jLabelLinkText34 = new javax.swing.JLabel();
        jTextFieldLinkText34 = new javax.swing.JTextField();
        jLabelLinkExecute35 = new javax.swing.JLabel();
        jTextFieldLinkExecute35 = new javax.swing.JTextField();
        jLabelLinkText35 = new javax.swing.JLabel();
        jTextFieldLinkText35 = new javax.swing.JTextField();
        jLabelLinkExecute36 = new javax.swing.JLabel();
        jTextFieldLinkExecute36 = new javax.swing.JTextField();
        jLabelLinkText36 = new javax.swing.JLabel();
        jTextFieldLinkText36 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jScrollPaneSettingsScripts = new javax.swing.JScrollPane();
        jScrollPaneSettingsReferences = new javax.swing.JScrollPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("LaunchPad");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setSize(new java.awt.Dimension(550, 630));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jTabbedMain.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N
        jTabbedMain.setPreferredSize(new java.awt.Dimension(586, 630));

        jPanelMain.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jPanelMain.setPreferredSize(new java.awt.Dimension(500, 503));

        jListSessions.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N
        jListSessions.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jListSessions.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListSessionsValueChanged(evt);
            }
        });
        jScrollPaneSessionList.setViewportView(jListSessions);
        jListSessions.addMouseListener(new MyMouseListener());

        jPanelMainRightSide.setLayout(null);

        jTextFieldConnectHostname.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N
        jTextFieldConnectHostname.setToolTipText("IP or DNS Hostname");
        jTextFieldConnectHostname.setNextFocusableComponent(jTextFieldConnectUsername);
        jTextFieldConnectHostname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldConnectHostnameActionPerformed(evt);
            }
        });
        jTextFieldConnectHostname.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldConnectHostnameKeyTyped(evt);
            }
        });
        jPanelMainRightSide.add(jTextFieldConnectHostname);
        jTextFieldConnectHostname.setBounds(10, 80, 120, 20);

        jButtonExecuteFunctionHTTPS.setBackground(new java.awt.Color(255, 208, 153));
        jButtonExecuteFunctionHTTPS.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        jButtonExecuteFunctionHTTPS.setText("HTTPS");
        jButtonExecuteFunctionHTTPS.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonExecuteFunctionHTTPS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExecuteFunctionHTTPSActionPerformed(evt);
            }
        });
        jPanelMainRightSide.add(jButtonExecuteFunctionHTTPS);
        jButtonExecuteFunctionHTTPS.setBounds(140, 140, 60, 20);

        jButtonExecuteFunctionSSH.setBackground(new java.awt.Color(200, 255, 153));
        jButtonExecuteFunctionSSH.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        jButtonExecuteFunctionSSH.setText("SSH");
        jButtonExecuteFunctionSSH.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonExecuteFunctionSSH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExecuteFunctionSSHActionPerformed(evt);
            }
        });
        jPanelMainRightSide.add(jButtonExecuteFunctionSSH);
        jButtonExecuteFunctionSSH.setBounds(140, 100, 60, 20);

        jTextFieldConnectUsername.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N
        jTextFieldConnectUsername.setToolTipText("Username");
        jTextFieldConnectUsername.setNextFocusableComponent(jPasswordFieldConnectPassword);
        jTextFieldConnectUsername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldConnectUsernameKeyTyped(evt);
            }
        });
        jPanelMainRightSide.add(jTextFieldConnectUsername);
        jTextFieldConnectUsername.setBounds(10, 120, 120, 20);

        jPasswordFieldConnectPassword.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N
        jPasswordFieldConnectPassword.setToolTipText("Password");
        jPasswordFieldConnectPassword.setNextFocusableComponent(jButtonExecuteFunctionSSH);
        jPasswordFieldConnectPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jPasswordFieldConnectPasswordKeyTyped(evt);
            }
        });
        jPanelMainRightSide.add(jPasswordFieldConnectPassword);
        jPasswordFieldConnectPassword.setBounds(10, 140, 120, 20);
        jPanelMainRightSide.add(jSeparator3);
        jSeparator3.setBounds(10, 170, 190, 10);

        jTextFieldPingHostname.setBackground(new java.awt.Color(244, 230, 255));
        jTextFieldPingHostname.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N
        jTextFieldPingHostname.setToolTipText("IP or DNS Hostname");
        jTextFieldPingHostname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldPingHostnameActionPerformed(evt);
            }
        });
        jTextFieldPingHostname.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldPingHostnameKeyTyped(evt);
            }
        });
        jPanelMainRightSide.add(jTextFieldPingHostname);
        jTextFieldPingHostname.setBounds(10, 180, 100, 20);

        jCheckBoxDNS.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N
        jCheckBoxDNS.setText("DNS");
        jCheckBoxDNS.setToolTipText("Resolve DNS");
        jCheckBoxDNS.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanelMainRightSide.add(jCheckBoxDNS);
        jCheckBoxDNS.setBounds(150, 180, 50, 20);

        jButtonTracert.setBackground(new java.awt.Color(208, 153, 255));
        jButtonTracert.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        jButtonTracert.setText("TRACERT");
        jButtonTracert.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButtonTracert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTracertActionPerformed(evt);
            }
        });
        jPanelMainRightSide.add(jButtonTracert);
        jButtonTracert.setBounds(70, 200, 80, 20);

        jButtonPing.setBackground(new java.awt.Color(208, 153, 255));
        jButtonPing.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        jButtonPing.setText("PING");
        jButtonPing.setToolTipText("Runs a constant ping and logs to the output folder");
        jButtonPing.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButtonPing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPingActionPerformed(evt);
            }
        });
        jPanelMainRightSide.add(jButtonPing);
        jButtonPing.setBounds(10, 200, 60, 20);
        jPanelMainRightSide.add(jSeparator5);
        jSeparator5.setBounds(10, 70, 190, 10);

        jComboBoxConsoleCOM.setBackground(new java.awt.Color(230, 241, 255));
        jComboBoxConsoleCOM.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N
        jComboBoxConsoleCOM.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "COM10", "COM11", "COM12", "COM13", "COM14", "COM15", "COM16", "COM17", "COM18", "COM19", "COM20" }));
        jComboBoxConsoleCOM.setToolTipText("Serial COM port");
        jComboBoxConsoleCOM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxConsoleCOMActionPerformed(evt);
            }
        });
        jPanelMainRightSide.add(jComboBoxConsoleCOM);
        jComboBoxConsoleCOM.setBounds(10, 240, 70, 20);

        jButtonShowCOMList.setBackground(new java.awt.Color(230, 241, 255));
        jButtonShowCOMList.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N
        jButtonShowCOMList.setText("Query");
        jButtonShowCOMList.setToolTipText("List your Serial COM ports");
        jButtonShowCOMList.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonShowCOMList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonShowCOMListActionPerformed(evt);
            }
        });
        jPanelMainRightSide.add(jButtonShowCOMList);
        jButtonShowCOMList.setBounds(150, 240, 50, 20);

        jComboBoxConsoleBaud.setBackground(new java.awt.Color(230, 241, 255));
        jComboBoxConsoleBaud.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N
        jComboBoxConsoleBaud.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "9600", "115200" }));
        jComboBoxConsoleBaud.setToolTipText("Baud Rate");
        jPanelMainRightSide.add(jComboBoxConsoleBaud);
        jComboBoxConsoleBaud.setBounds(80, 240, 70, 20);

        jButtonConsole.setBackground(new java.awt.Color(153, 200, 255));
        jButtonConsole.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        jButtonConsole.setText("Connect to Serial Port");
        jButtonConsole.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonConsole.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConsoleActionPerformed(evt);
            }
        });
        jPanelMainRightSide.add(jButtonConsole);
        jButtonConsole.setBounds(10, 260, 190, 20);
        jPanelMainRightSide.add(jSeparator4);
        jSeparator4.setBounds(10, 290, 190, 10);

        jButtonMainButton1.setContentAreaFilled(false);
        jButtonMainButton1.setFocusPainted(false);
        jButtonMainButton1.setFocusable(false);
        jButtonMainButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMainButton1ActionPerformed(evt);
            }
        });
        jPanelMainRightSide.add(jButtonMainButton1);
        jButtonMainButton1.setBounds(10, 300, 40, 40);

        jButtonMainButton2.setContentAreaFilled(false);
        jButtonMainButton2.setFocusPainted(false);
        jButtonMainButton2.setFocusable(false);
        jButtonMainButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMainButton2ActionPerformed(evt);
            }
        });
        jPanelMainRightSide.add(jButtonMainButton2);
        jButtonMainButton2.setBounds(60, 300, 40, 40);

        jButtonMainButton3.setContentAreaFilled(false);
        jButtonMainButton3.setFocusPainted(false);
        jButtonMainButton3.setFocusable(false);
        jButtonMainButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMainButton3ActionPerformed(evt);
            }
        });
        jPanelMainRightSide.add(jButtonMainButton3);
        jButtonMainButton3.setBounds(110, 300, 40, 40);

        jButtonMainButton4.setContentAreaFilled(false);
        jButtonMainButton4.setFocusPainted(false);
        jButtonMainButton4.setFocusable(false);
        jButtonMainButton4.setPreferredSize(new java.awt.Dimension(45, 45));
        jButtonMainButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMainButton4ActionPerformed(evt);
            }
        });
        jPanelMainRightSide.add(jButtonMainButton4);
        jButtonMainButton4.setBounds(160, 300, 40, 40);

        jButtonMainButton8.setContentAreaFilled(false);
        jButtonMainButton8.setFocusPainted(false);
        jButtonMainButton8.setFocusable(false);
        jButtonMainButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMainButton8ActionPerformed(evt);
            }
        });
        jPanelMainRightSide.add(jButtonMainButton8);
        jButtonMainButton8.setBounds(160, 350, 40, 40);

        jButtonMainButton12.setContentAreaFilled(false);
        jButtonMainButton12.setFocusPainted(false);
        jButtonMainButton12.setFocusable(false);
        jButtonMainButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMainButton12ActionPerformed(evt);
            }
        });
        jPanelMainRightSide.add(jButtonMainButton12);
        jButtonMainButton12.setBounds(160, 400, 40, 40);

        jButtonMainButton7.setContentAreaFilled(false);
        jButtonMainButton7.setFocusPainted(false);
        jButtonMainButton7.setFocusable(false);
        jButtonMainButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMainButton7ActionPerformed(evt);
            }
        });
        jPanelMainRightSide.add(jButtonMainButton7);
        jButtonMainButton7.setBounds(110, 350, 40, 40);

        jButtonMainButton11.setContentAreaFilled(false);
        jButtonMainButton11.setFocusPainted(false);
        jButtonMainButton11.setFocusable(false);
        jButtonMainButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMainButton11ActionPerformed(evt);
            }
        });
        jPanelMainRightSide.add(jButtonMainButton11);
        jButtonMainButton11.setBounds(110, 400, 40, 40);

        jButtonMainButton6.setContentAreaFilled(false);
        jButtonMainButton6.setFocusPainted(false);
        jButtonMainButton6.setFocusable(false);
        jButtonMainButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMainButton6ActionPerformed(evt);
            }
        });
        jPanelMainRightSide.add(jButtonMainButton6);
        jButtonMainButton6.setBounds(60, 350, 40, 40);

        jButtonMainButton10.setContentAreaFilled(false);
        jButtonMainButton10.setFocusPainted(false);
        jButtonMainButton10.setFocusable(false);
        jButtonMainButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMainButton10ActionPerformed(evt);
            }
        });
        jPanelMainRightSide.add(jButtonMainButton10);
        jButtonMainButton10.setBounds(60, 400, 40, 40);

        jButtonMainButton5.setContentAreaFilled(false);
        jButtonMainButton5.setFocusPainted(false);
        jButtonMainButton5.setFocusable(false);
        jButtonMainButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMainButton5ActionPerformed(evt);
            }
        });
        jPanelMainRightSide.add(jButtonMainButton5);
        jButtonMainButton5.setBounds(10, 350, 40, 40);

        jButtonMainButton9.setContentAreaFilled(false);
        jButtonMainButton9.setFocusPainted(false);
        jButtonMainButton9.setFocusable(false);
        jButtonMainButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMainButton9ActionPerformed(evt);
            }
        });
        jPanelMainRightSide.add(jButtonMainButton9);
        jButtonMainButton9.setBounds(10, 400, 40, 40);

        jButtonMainButton13.setContentAreaFilled(false);
        jButtonMainButton13.setFocusPainted(false);
        jButtonMainButton13.setFocusable(false);
        jButtonMainButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMainButton13ActionPerformed(evt);
            }
        });
        jPanelMainRightSide.add(jButtonMainButton13);
        jButtonMainButton13.setBounds(10, 450, 40, 40);

        jButtonMainButton14.setContentAreaFilled(false);
        jButtonMainButton14.setFocusPainted(false);
        jButtonMainButton14.setFocusable(false);
        jButtonMainButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMainButton14ActionPerformed(evt);
            }
        });
        jPanelMainRightSide.add(jButtonMainButton14);
        jButtonMainButton14.setBounds(60, 450, 40, 40);

        jButtonMainButton15.setContentAreaFilled(false);
        jButtonMainButton15.setFocusPainted(false);
        jButtonMainButton15.setFocusable(false);
        jButtonMainButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMainButton15ActionPerformed(evt);
            }
        });
        jPanelMainRightSide.add(jButtonMainButton15);
        jButtonMainButton15.setBounds(110, 450, 40, 40);

        jButtonMainButton16.setContentAreaFilled(false);
        jButtonMainButton16.setFocusPainted(false);
        jButtonMainButton16.setFocusable(false);
        jButtonMainButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMainButton16ActionPerformed(evt);
            }
        });
        jPanelMainRightSide.add(jButtonMainButton16);
        jButtonMainButton16.setBounds(160, 450, 40, 40);

        jButtonMainButton20.setContentAreaFilled(false);
        jButtonMainButton20.setFocusPainted(false);
        jButtonMainButton20.setFocusable(false);
        jButtonMainButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMainButton20ActionPerformed(evt);
            }
        });
        jPanelMainRightSide.add(jButtonMainButton20);
        jButtonMainButton20.setBounds(160, 500, 40, 40);

        jButtonMainButton19.setContentAreaFilled(false);
        jButtonMainButton19.setFocusPainted(false);
        jButtonMainButton19.setFocusable(false);
        jButtonMainButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMainButton19ActionPerformed(evt);
            }
        });
        jPanelMainRightSide.add(jButtonMainButton19);
        jButtonMainButton19.setBounds(110, 500, 40, 40);

        jButtonMainButton18.setContentAreaFilled(false);
        jButtonMainButton18.setFocusPainted(false);
        jButtonMainButton18.setFocusable(false);
        jButtonMainButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMainButton18ActionPerformed(evt);
            }
        });
        jPanelMainRightSide.add(jButtonMainButton18);
        jButtonMainButton18.setBounds(60, 500, 40, 40);

        jButtonMainButton17.setContentAreaFilled(false);
        jButtonMainButton17.setFocusPainted(false);
        jButtonMainButton17.setFocusable(false);
        jButtonMainButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMainButton17ActionPerformed(evt);
            }
        });
        jPanelMainRightSide.add(jButtonMainButton17);
        jButtonMainButton17.setBounds(10, 500, 40, 40);

        jCheckBoxAlternateLogin.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jCheckBoxAlternateLogin.setText("Alternate Credentials");
        jCheckBoxAlternateLogin.setToolTipText("");
        jCheckBoxAlternateLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxAlternateLoginActionPerformed(evt);
            }
        });
        jPanelMainRightSide.add(jCheckBoxAlternateLogin);
        jCheckBoxAlternateLogin.setBounds(10, 100, 130, 20);

        jButtonExecuteFunctionRDP.setBackground(new java.awt.Color(251, 255, 153));
        jButtonExecuteFunctionRDP.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        jButtonExecuteFunctionRDP.setText("RDP");
        jButtonExecuteFunctionRDP.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonExecuteFunctionRDP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExecuteFunctionRDPActionPerformed(evt);
            }
        });
        jPanelMainRightSide.add(jButtonExecuteFunctionRDP);
        jButtonExecuteFunctionRDP.setBounds(140, 120, 60, 20);

        jTextFieldTCPTestPort.setBackground(new java.awt.Color(244, 230, 255));
        jTextFieldTCPTestPort.setToolTipText("TCP port to test");
        jPanelMainRightSide.add(jTextFieldTCPTestPort);
        jTextFieldTCPTestPort.setBounds(110, 180, 40, 20);

        jButtonTCP.setBackground(new java.awt.Color(208, 153, 255));
        jButtonTCP.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        jButtonTCP.setText("TCP");
        jButtonTCP.setToolTipText("Test the TCP port specified");
        jButtonTCP.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButtonTCP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTCPActionPerformed(evt);
            }
        });
        jPanelMainRightSide.add(jButtonTCP);
        jButtonTCP.setBounds(150, 200, 50, 20);

        jLabelLocalMAC.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        jLabelLocalMAC.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelLocalMAC.setText("Local MAC");
        jPanelMainRightSide.add(jLabelLocalMAC);
        jLabelLocalMAC.setBounds(30, 40, 150, 20);

        jLabelLocalHostname.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        jLabelLocalHostname.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelLocalHostname.setText("Local Hostname");
        jPanelMainRightSide.add(jLabelLocalHostname);
        jLabelLocalHostname.setBounds(10, 0, 190, 20);

        jLabelLocalIP.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        jLabelLocalIP.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelLocalIP.setText("Local IP");
        jLabelLocalIP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelLocalIPMouseClicked(evt);
            }
        });
        jPanelMainRightSide.add(jLabelLocalIP);
        jLabelLocalIP.setBounds(10, 20, 190, 20);
        jPanelMainRightSide.add(jSeparator6);
        jSeparator6.setBounds(10, 230, 190, 10);

        jButtonRefreshHostnameIPMAC.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        jButtonRefreshHostnameIPMAC.setToolTipText("Refresh Hostname IP and MAC");
        jButtonRefreshHostnameIPMAC.setContentAreaFilled(false);
        jButtonRefreshHostnameIPMAC.setFocusPainted(false);
        jButtonRefreshHostnameIPMAC.setFocusable(false);
        jButtonRefreshHostnameIPMAC.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonRefreshHostnameIPMAC.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButtonRefreshHostnameIPMACMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButtonRefreshHostnameIPMACMouseReleased(evt);
            }
        });
        jButtonRefreshHostnameIPMAC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRefreshHostnameIPMACActionPerformed(evt);
            }
        });
        jPanelMainRightSide.add(jButtonRefreshHostnameIPMAC);
        jButtonRefreshHostnameIPMAC.setBounds(180, 40, 20, 20);

        jButtonExecuteFunctionPKI.setBackground(new java.awt.Color(200, 255, 153));
        jButtonExecuteFunctionPKI.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        jButtonExecuteFunctionPKI.setText("PKI");
        jButtonExecuteFunctionPKI.setToolTipText("Public Key or 2FA CAC Auth using x.509");
        jButtonExecuteFunctionPKI.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonExecuteFunctionPKI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExecuteFunctionPKIActionPerformed(evt);
            }
        });
        jPanelMainRightSide.add(jButtonExecuteFunctionPKI);
        jButtonExecuteFunctionPKI.setBounds(140, 80, 60, 20);

        jButtonMainButton24.setContentAreaFilled(false);
        jButtonMainButton24.setFocusPainted(false);
        jButtonMainButton24.setFocusable(false);
        jButtonMainButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMainButton24ActionPerformed(evt);
            }
        });
        jPanelMainRightSide.add(jButtonMainButton24);
        jButtonMainButton24.setBounds(160, 550, 40, 40);

        jButtonMainButton23.setContentAreaFilled(false);
        jButtonMainButton23.setFocusPainted(false);
        jButtonMainButton23.setFocusable(false);
        jButtonMainButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMainButton23ActionPerformed(evt);
            }
        });
        jPanelMainRightSide.add(jButtonMainButton23);
        jButtonMainButton23.setBounds(110, 550, 40, 40);

        jButtonMainButton22.setContentAreaFilled(false);
        jButtonMainButton22.setFocusPainted(false);
        jButtonMainButton22.setFocusable(false);
        jButtonMainButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMainButton22ActionPerformed(evt);
            }
        });
        jPanelMainRightSide.add(jButtonMainButton22);
        jButtonMainButton22.setBounds(60, 550, 40, 40);

        jButtonMainButton21.setContentAreaFilled(false);
        jButtonMainButton21.setFocusPainted(false);
        jButtonMainButton21.setFocusable(false);
        jButtonMainButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMainButton21ActionPerformed(evt);
            }
        });
        jPanelMainRightSide.add(jButtonMainButton21);
        jButtonMainButton21.setBounds(10, 550, 40, 40);

        jButtonNetworkConnections.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        jButtonNetworkConnections.setToolTipText("Network Control Panel Applet (ncpa.cpl)");
        jButtonNetworkConnections.setContentAreaFilled(false);
        jButtonNetworkConnections.setFocusPainted(false);
        jButtonNetworkConnections.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonNetworkConnections.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButtonNetworkConnectionsMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButtonNetworkConnectionsMouseReleased(evt);
            }
        });
        jButtonNetworkConnections.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNetworkConnectionsActionPerformed(evt);
            }
        });
        jPanelMainRightSide.add(jButtonNetworkConnections);
        jButtonNetworkConnections.setBounds(10, 40, 20, 20);

        jTextFieldFilter.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N
        jTextFieldFilter.setToolTipText("Type to filter the list. ");
        jTextFieldFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldFilterActionPerformed(evt);
            }
        });
        jTextFieldFilter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldFilterKeyReleased(evt);
            }
        });

        jCheckBoxFavorites.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N
        jCheckBoxFavorites.setText("Favorites");
        jCheckBoxFavorites.setToolTipText("Load an alternate list.  To edit the list, open it under the settings tab");
        jCheckBoxFavorites.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxFavoritesItemStateChanged(evt);
            }
        });
        jCheckBoxFavorites.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBoxFavoritesStateChanged(evt);
            }
        });

        jButtonClearFilter.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N
        jButtonClearFilter.setText("X");
        jButtonClearFilter.setToolTipText("Clear filter");
        jButtonClearFilter.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonClearFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearFilterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelMainLayout = new javax.swing.GroupLayout(jPanelMain);
        jPanelMain.setLayout(jPanelMainLayout);
        jPanelMainLayout.setHorizontalGroup(
            jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMainLayout.createSequentialGroup()
                .addGroup(jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPaneSessionList)
                    .addGroup(jPanelMainLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTextFieldFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonClearFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBoxFavorites, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 2, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelMainRightSide, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanelMainLayout.setVerticalGroup(
            jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMainLayout.createSequentialGroup()
                .addGroup(jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelMainLayout.createSequentialGroup()
                        .addGroup(jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jCheckBoxFavorites, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonClearFilter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldFilter))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPaneSessionList, javax.swing.GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE))
                    .addComponent(jPanelMainRightSide, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );

        jTabbedMain.addTab("Main", jPanelMain);

        jPanelAppsCustom.setLayout(null);

        jButtonLinkCustom01.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonLinkCustom01.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonLinkCustom01.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom01ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom01);
        jButtonLinkCustom01.setBounds(30, 60, 170, 30);

        jButtonLinkCustom02.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonLinkCustom02.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonLinkCustom02.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom02ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom02);
        jButtonLinkCustom02.setBounds(210, 60, 170, 30);

        jButtonLinkCustom03.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonLinkCustom03.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonLinkCustom03.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom03ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom03);
        jButtonLinkCustom03.setBounds(390, 60, 170, 30);

        jButtonLinkCustom04.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonLinkCustom04.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonLinkCustom04.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom04ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom04);
        jButtonLinkCustom04.setBounds(30, 100, 170, 30);

        jButtonLinkCustom05.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonLinkCustom05.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonLinkCustom05.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom05ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom05);
        jButtonLinkCustom05.setBounds(210, 100, 170, 30);

        jButtonLinkCustom06.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonLinkCustom06.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonLinkCustom06.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom06ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom06);
        jButtonLinkCustom06.setBounds(390, 100, 170, 30);

        jButtonLinkCustom07.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonLinkCustom07.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonLinkCustom07.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom07ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom07);
        jButtonLinkCustom07.setBounds(30, 140, 170, 30);

        jButtonLinkCustom08.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonLinkCustom08.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonLinkCustom08.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom08ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom08);
        jButtonLinkCustom08.setBounds(210, 140, 170, 30);

        jButtonLinkCustom09.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonLinkCustom09.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonLinkCustom09.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom09ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom09);
        jButtonLinkCustom09.setBounds(390, 140, 170, 30);

        jButtonLinkCustom11.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonLinkCustom11.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonLinkCustom11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom11ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom11);
        jButtonLinkCustom11.setBounds(210, 180, 170, 30);

        jButtonLinkCustom10.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonLinkCustom10.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonLinkCustom10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom10ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom10);
        jButtonLinkCustom10.setBounds(30, 180, 170, 30);

        jButtonLinkCustom12.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonLinkCustom12.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonLinkCustom12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom12ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom12);
        jButtonLinkCustom12.setBounds(390, 180, 170, 30);

        jButtonLinkCustom13.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonLinkCustom13.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonLinkCustom13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom13ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom13);
        jButtonLinkCustom13.setBounds(30, 220, 170, 30);

        jButtonLinkCustom14.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonLinkCustom14.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonLinkCustom14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom14ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom14);
        jButtonLinkCustom14.setBounds(210, 220, 170, 30);

        jButtonLinkCustom15.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonLinkCustom15.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonLinkCustom15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom15ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom15);
        jButtonLinkCustom15.setBounds(390, 220, 170, 30);

        jButtonLinkCustom16.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonLinkCustom16.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonLinkCustom16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom16ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom16);
        jButtonLinkCustom16.setBounds(30, 260, 170, 30);

        jButtonLinkCustom17.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonLinkCustom17.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonLinkCustom17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom17ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom17);
        jButtonLinkCustom17.setBounds(210, 260, 170, 30);

        jButtonLinkCustom18.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonLinkCustom18.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonLinkCustom18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom18ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom18);
        jButtonLinkCustom18.setBounds(390, 260, 170, 30);

        jButtonLinkCustom19.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonLinkCustom19.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonLinkCustom19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom19ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom19);
        jButtonLinkCustom19.setBounds(30, 300, 170, 30);

        jButtonLinkCustom20.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonLinkCustom20.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonLinkCustom20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom20ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom20);
        jButtonLinkCustom20.setBounds(210, 300, 170, 30);

        jButtonLinkCustom21.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonLinkCustom21.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonLinkCustom21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom21ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom21);
        jButtonLinkCustom21.setBounds(390, 300, 170, 30);

        jButtonLinkCustom22.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonLinkCustom22.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonLinkCustom22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom22ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom22);
        jButtonLinkCustom22.setBounds(30, 340, 170, 30);

        jButtonLinkCustom23.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonLinkCustom23.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonLinkCustom23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom23ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom23);
        jButtonLinkCustom23.setBounds(210, 340, 170, 30);

        jButtonLinkCustom24.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonLinkCustom24.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonLinkCustom24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom24ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom24);
        jButtonLinkCustom24.setBounds(390, 340, 170, 30);

        jButtonLinkCustom25.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonLinkCustom25.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonLinkCustom25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom25ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom25);
        jButtonLinkCustom25.setBounds(30, 380, 170, 30);

        jButtonLinkCustom26.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonLinkCustom26.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonLinkCustom26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom26ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom26);
        jButtonLinkCustom26.setBounds(210, 380, 170, 30);

        jButtonLinkCustom27.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonLinkCustom27.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonLinkCustom27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom27ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom27);
        jButtonLinkCustom27.setBounds(390, 380, 170, 30);

        jButtonLinkCustom28.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonLinkCustom28.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonLinkCustom28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom28ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom28);
        jButtonLinkCustom28.setBounds(30, 420, 170, 30);

        jButtonLinkCustom29.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonLinkCustom29.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonLinkCustom29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom29ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom29);
        jButtonLinkCustom29.setBounds(210, 420, 170, 30);

        jButtonLinkCustom30.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonLinkCustom30.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonLinkCustom30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom30ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom30);
        jButtonLinkCustom30.setBounds(390, 420, 170, 30);

        jButtonLinkCustom31.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonLinkCustom31.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonLinkCustom31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom31ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom31);
        jButtonLinkCustom31.setBounds(30, 460, 170, 30);

        jButtonLinkCustom32.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonLinkCustom32.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonLinkCustom32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom32ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom32);
        jButtonLinkCustom32.setBounds(210, 460, 170, 30);

        jButtonLinkCustom33.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonLinkCustom33.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonLinkCustom33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom33ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom33);
        jButtonLinkCustom33.setBounds(390, 460, 170, 30);

        jButtonLinkCustom34.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonLinkCustom34.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonLinkCustom34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom34ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom34);
        jButtonLinkCustom34.setBounds(30, 500, 170, 30);

        jButtonLinkCustom35.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonLinkCustom35.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonLinkCustom35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom35ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom35);
        jButtonLinkCustom35.setBounds(210, 500, 170, 30);

        jButtonLinkCustom36.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonLinkCustom36.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonLinkCustom36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom36ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom36);
        jButtonLinkCustom36.setBounds(390, 500, 170, 30);

        jTextFieldLinksFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldLinksFilterActionPerformed(evt);
            }
        });
        jTextFieldLinksFilter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinksFilterKeyReleased(evt);
            }
        });
        jPanelAppsCustom.add(jTextFieldLinksFilter);
        jTextFieldLinksFilter.setBounds(210, 20, 170, 20);

        jLabelLinksFilter.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinksFilter.setText("Filter:");
        jPanelAppsCustom.add(jLabelLinksFilter);
        jLabelLinksFilter.setBounds(90, 20, 110, 20);

        jTabbedMain.addTab("Links", jPanelAppsCustom);

        jPanelReference.setLayout(null);

        jToggleOnlineOfflineMode.setBackground(new java.awt.Color(180, 236, 180));
        jToggleOnlineOfflineMode.setFont(new java.awt.Font("Arial Unicode MS", 0, 18)); // NOI18N
        jToggleOnlineOfflineMode.setText("Online");
        jToggleOnlineOfflineMode.setToolTipText("Alternate between using network and local files.");
        jToggleOnlineOfflineMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleOnlineOfflineModeActionPerformed(evt);
            }
        });
        jPanelReference.add(jToggleOnlineOfflineMode);
        jToggleOnlineOfflineMode.setBounds(210, 60, 170, 30);

        jButtonReferenceCustom01.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonReferenceCustom01.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonReferenceCustom01.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom01ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom01);
        jButtonReferenceCustom01.setBounds(30, 100, 170, 30);

        jButtonReferenceCustom02.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonReferenceCustom02.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonReferenceCustom02.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom02ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom02);
        jButtonReferenceCustom02.setBounds(210, 100, 170, 30);

        jButtonReferenceCustom03.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonReferenceCustom03.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonReferenceCustom03.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom03ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom03);
        jButtonReferenceCustom03.setBounds(390, 100, 170, 30);

        jButtonReferenceCustom04.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonReferenceCustom04.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonReferenceCustom04.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom04ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom04);
        jButtonReferenceCustom04.setBounds(30, 140, 170, 30);

        jButtonReferenceCustom05.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonReferenceCustom05.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonReferenceCustom05.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom05ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom05);
        jButtonReferenceCustom05.setBounds(210, 140, 170, 30);

        jButtonReferenceCustom06.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonReferenceCustom06.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonReferenceCustom06.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom06ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom06);
        jButtonReferenceCustom06.setBounds(390, 140, 170, 30);

        jButtonReferenceCustom07.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonReferenceCustom07.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonReferenceCustom07.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom07ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom07);
        jButtonReferenceCustom07.setBounds(30, 180, 170, 30);

        jButtonReferenceCustom08.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonReferenceCustom08.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonReferenceCustom08.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom08ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom08);
        jButtonReferenceCustom08.setBounds(210, 180, 170, 30);

        jButtonReferenceCustom09.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonReferenceCustom09.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonReferenceCustom09.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom09ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom09);
        jButtonReferenceCustom09.setBounds(390, 180, 170, 30);

        jButtonReferenceCustom10.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonReferenceCustom10.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonReferenceCustom10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom10ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom10);
        jButtonReferenceCustom10.setBounds(30, 220, 170, 30);

        jButtonReferenceCustom11.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonReferenceCustom11.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonReferenceCustom11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom11ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom11);
        jButtonReferenceCustom11.setBounds(210, 220, 170, 30);

        jButtonReferenceCustom12.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonReferenceCustom12.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonReferenceCustom12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom12ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom12);
        jButtonReferenceCustom12.setBounds(390, 220, 170, 30);

        jButtonReferenceCustom13.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonReferenceCustom13.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonReferenceCustom13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom13ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom13);
        jButtonReferenceCustom13.setBounds(30, 260, 170, 30);

        jButtonReferenceCustom14.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonReferenceCustom14.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonReferenceCustom14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom14ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom14);
        jButtonReferenceCustom14.setBounds(210, 260, 170, 30);

        jButtonReferenceCustom15.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonReferenceCustom15.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonReferenceCustom15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom15ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom15);
        jButtonReferenceCustom15.setBounds(390, 260, 170, 30);

        jButtonReferenceCustom16.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonReferenceCustom16.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonReferenceCustom16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom16ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom16);
        jButtonReferenceCustom16.setBounds(30, 300, 170, 30);

        jButtonReferenceCustom17.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonReferenceCustom17.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonReferenceCustom17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom17ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom17);
        jButtonReferenceCustom17.setBounds(210, 300, 170, 30);

        jButtonReferenceCustom18.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonReferenceCustom18.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonReferenceCustom18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom18ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom18);
        jButtonReferenceCustom18.setBounds(390, 300, 170, 30);

        jButtonReferenceCustom19.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonReferenceCustom19.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonReferenceCustom19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom19ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom19);
        jButtonReferenceCustom19.setBounds(30, 340, 170, 30);

        jButtonReferenceCustom20.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonReferenceCustom20.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonReferenceCustom20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom20ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom20);
        jButtonReferenceCustom20.setBounds(210, 340, 170, 30);

        jButtonReferenceCustom21.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonReferenceCustom21.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonReferenceCustom21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom21ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom21);
        jButtonReferenceCustom21.setBounds(390, 340, 170, 30);

        jButtonReferenceCustom22.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonReferenceCustom22.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonReferenceCustom22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom22ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom22);
        jButtonReferenceCustom22.setBounds(30, 380, 170, 30);

        jButtonReferenceCustom23.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonReferenceCustom23.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonReferenceCustom23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom23ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom23);
        jButtonReferenceCustom23.setBounds(210, 380, 170, 30);

        jButtonReferenceCustom24.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonReferenceCustom24.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonReferenceCustom24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom24ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom24);
        jButtonReferenceCustom24.setBounds(390, 380, 170, 30);

        jButtonReferenceCustom25.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonReferenceCustom25.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonReferenceCustom25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom25ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom25);
        jButtonReferenceCustom25.setBounds(30, 420, 170, 30);

        jButtonReferenceCustom26.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonReferenceCustom26.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonReferenceCustom26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom26ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom26);
        jButtonReferenceCustom26.setBounds(210, 420, 170, 30);

        jButtonReferenceCustom27.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonReferenceCustom27.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonReferenceCustom27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom27ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom27);
        jButtonReferenceCustom27.setBounds(390, 420, 170, 30);

        jButtonReferenceCustom28.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonReferenceCustom28.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonReferenceCustom28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom28ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom28);
        jButtonReferenceCustom28.setBounds(30, 460, 170, 30);

        jButtonReferenceCustom29.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonReferenceCustom29.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonReferenceCustom29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom29ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom29);
        jButtonReferenceCustom29.setBounds(210, 460, 170, 30);

        jButtonReferenceCustom30.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonReferenceCustom30.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonReferenceCustom30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom30ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom30);
        jButtonReferenceCustom30.setBounds(390, 460, 170, 30);

        jButtonReferenceCustom31.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonReferenceCustom31.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonReferenceCustom31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom31ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom31);
        jButtonReferenceCustom31.setBounds(30, 500, 170, 30);

        jButtonReferenceCustom32.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonReferenceCustom32.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonReferenceCustom32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom32ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom32);
        jButtonReferenceCustom32.setBounds(210, 500, 170, 30);

        jButtonReferenceCustom33.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonReferenceCustom33.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonReferenceCustom33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom33ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom33);
        jButtonReferenceCustom33.setBounds(390, 500, 170, 30);

        jTextFieldReferenceFilter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldReferenceFilterKeyReleased(evt);
            }
        });
        jPanelReference.add(jTextFieldReferenceFilter);
        jTextFieldReferenceFilter.setBounds(210, 20, 170, 20);

        jLabelReferenceFilter.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelReferenceFilter.setText("Filter:");
        jPanelReference.add(jLabelReferenceFilter);
        jLabelReferenceFilter.setBounds(110, 20, 90, 20);

        jTabbedMain.addTab("Reference", jPanelReference);

        jPanelScripts.setLayout(null);

        jButtonScriptCustom03.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptCustom03.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom03.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom03ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom03);
        jButtonScriptCustom03.setBounds(390, 60, 170, 30);

        jButtonScriptCustom01.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptCustom01.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom01.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom01ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom01);
        jButtonScriptCustom01.setBounds(30, 60, 170, 30);

        jButtonScriptCustom02.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptCustom02.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom02.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom02ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom02);
        jButtonScriptCustom02.setBounds(210, 60, 170, 30);

        jButtonScriptCustom06.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptCustom06.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom06.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom06ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom06);
        jButtonScriptCustom06.setBounds(390, 100, 170, 30);

        jButtonScriptCustom04.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptCustom04.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom04.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom04ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom04);
        jButtonScriptCustom04.setBounds(30, 100, 170, 30);

        jButtonScriptCustom05.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptCustom05.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom05.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom05ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom05);
        jButtonScriptCustom05.setBounds(210, 100, 170, 30);

        jButtonScriptCustom08.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptCustom08.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom08.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom08ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom08);
        jButtonScriptCustom08.setBounds(210, 140, 170, 30);

        jButtonScriptCustom09.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptCustom09.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom09.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom09ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom09);
        jButtonScriptCustom09.setBounds(390, 140, 170, 30);

        jButtonScriptCustom07.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptCustom07.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom07.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom07ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom07);
        jButtonScriptCustom07.setBounds(30, 140, 170, 30);

        jButtonScriptCustom12.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptCustom12.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom12ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom12);
        jButtonScriptCustom12.setBounds(390, 180, 170, 30);

        jButtonScriptCustom15.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptCustom15.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom15ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom15);
        jButtonScriptCustom15.setBounds(390, 220, 170, 30);

        jButtonScriptCustom11.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptCustom11.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom11ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom11);
        jButtonScriptCustom11.setBounds(210, 180, 170, 30);

        jButtonScriptCustom10.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptCustom10.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom10ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom10);
        jButtonScriptCustom10.setBounds(30, 180, 170, 30);

        jButtonScriptCustom13.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptCustom13.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom13ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom13);
        jButtonScriptCustom13.setBounds(30, 220, 170, 30);

        jButtonScriptCustom14.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptCustom14.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom14ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom14);
        jButtonScriptCustom14.setBounds(210, 220, 170, 30);

        jButtonScriptCustom18.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptCustom18.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom18ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom18);
        jButtonScriptCustom18.setBounds(390, 260, 170, 30);

        jButtonScriptCustom16.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptCustom16.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom16ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom16);
        jButtonScriptCustom16.setBounds(30, 260, 170, 30);

        jButtonScriptCustom17.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptCustom17.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom17ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom17);
        jButtonScriptCustom17.setBounds(210, 260, 170, 30);

        jButtonScriptCustom20.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptCustom20.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom20ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom20);
        jButtonScriptCustom20.setBounds(210, 300, 170, 30);

        jButtonScriptCustom21.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptCustom21.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom21ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom21);
        jButtonScriptCustom21.setBounds(390, 300, 170, 30);

        jButtonScriptCustom19.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptCustom19.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom19ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom19);
        jButtonScriptCustom19.setBounds(30, 300, 170, 30);

        jButtonScriptCustom24.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptCustom24.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom24ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom24);
        jButtonScriptCustom24.setBounds(390, 340, 170, 30);

        jButtonScriptCustom22.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptCustom22.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom22ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom22);
        jButtonScriptCustom22.setBounds(30, 340, 170, 30);

        jButtonScriptCustom23.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptCustom23.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom23ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom23);
        jButtonScriptCustom23.setBounds(210, 340, 170, 30);

        jButtonScriptCustom26.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptCustom26.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom26ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom26);
        jButtonScriptCustom26.setBounds(210, 380, 170, 30);

        jButtonScriptCustom27.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptCustom27.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom27ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom27);
        jButtonScriptCustom27.setBounds(390, 380, 170, 30);

        jButtonScriptCustom25.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptCustom25.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom25ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom25);
        jButtonScriptCustom25.setBounds(30, 380, 170, 30);

        jButtonScriptCustom29.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptCustom29.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom29ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom29);
        jButtonScriptCustom29.setBounds(210, 420, 170, 30);

        jButtonScriptCustom30.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptCustom30.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom30ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom30);
        jButtonScriptCustom30.setBounds(390, 420, 170, 30);

        jButtonScriptCustom28.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptCustom28.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom28ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom28);
        jButtonScriptCustom28.setBounds(30, 420, 170, 30);

        jButtonScriptCustom32.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptCustom32.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom32ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom32);
        jButtonScriptCustom32.setBounds(210, 460, 170, 30);

        jButtonScriptCustom33.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptCustom33.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom33ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom33);
        jButtonScriptCustom33.setBounds(390, 460, 170, 30);

        jButtonScriptCustom31.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptCustom31.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom31ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom31);
        jButtonScriptCustom31.setBounds(30, 460, 170, 30);

        jButtonScriptCustom34.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptCustom34.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom34ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom34);
        jButtonScriptCustom34.setBounds(30, 500, 170, 30);

        jButtonScriptCustom35.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptCustom35.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom35ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom35);
        jButtonScriptCustom35.setBounds(210, 500, 170, 30);

        jButtonScriptCustom36.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptCustom36.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom36ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom36);
        jButtonScriptCustom36.setBounds(390, 500, 170, 30);

        jTextFieldScriptsFilter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldScriptsFilterKeyReleased(evt);
            }
        });
        jPanelScripts.add(jTextFieldScriptsFilter);
        jTextFieldScriptsFilter.setBounds(210, 20, 170, 20);

        jLabelScriptsFilter.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelScriptsFilter.setText("Filter:");
        jPanelScripts.add(jLabelScriptsFilter);
        jLabelScriptsFilter.setBounds(120, 20, 80, 20);

        jTabbedMain.addTab("Scripts", jPanelScripts);

        jTabbedPaneToolBox.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N

        jPanelWebJavaDocs.setLayout(null);

        jPanelDocuments.setBorder(javax.swing.BorderFactory.createTitledBorder("Documents"));
        jPanelDocuments.setLayout(null);

        jButton24.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButton24.setText("IPv4 Subnet Chart");
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });
        jPanelDocuments.add(jButton24);
        jButton24.setBounds(10, 20, 170, 30);

        jButton36.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButton36.setText("IPv4 Subnet Cheat Sheet");
        jButton36.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton36ActionPerformed(evt);
            }
        });
        jPanelDocuments.add(jButton36);
        jButton36.setBounds(190, 20, 170, 30);

        jButton37.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButton37.setText("Stretch's Cheat Sheets");
        jButton37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton37ActionPerformed(evt);
            }
        });
        jPanelDocuments.add(jButton37);
        jButton37.setBounds(370, 20, 170, 30);

        jButton42.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButton42.setText("Excel ToolBox");
        jButton42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton42ActionPerformed(evt);
            }
        });
        jPanelDocuments.add(jButton42);
        jButton42.setBounds(10, 60, 170, 30);

        jPanelWebJavaDocs.add(jPanelDocuments);
        jPanelDocuments.setBounds(10, 230, 550, 100);

        jPanelJavaApps.setBorder(javax.swing.BorderFactory.createTitledBorder("Java Apps"));
        jPanelJavaApps.setLayout(null);

        jButton41.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButton41.setText("Puppeteer");
        jButton41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton41ActionPerformed(evt);
            }
        });
        jPanelJavaApps.add(jButton41);
        jButton41.setBounds(10, 20, 170, 30);

        jPanelWebJavaDocs.add(jPanelJavaApps);
        jPanelJavaApps.setBounds(10, 160, 550, 60);

        jPanelWebApps.setBorder(javax.swing.BorderFactory.createTitledBorder("Web Apps"));
        jPanelWebApps.setLayout(null);

        jButtonJSDiff2.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonJSDiff2.setText("jsDiff (Compare Files)");
        jButtonJSDiff2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonJSDiff2ActionPerformed(evt);
            }
        });
        jPanelWebApps.add(jButtonJSDiff2);
        jButtonJSDiff2.setBounds(10, 60, 170, 30);

        jButtonConfigBuilder1.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonConfigBuilder1.setText("Config Builder (For Lab Use)");
        jButtonConfigBuilder1.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButtonConfigBuilder1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConfigBuilder1ActionPerformed(evt);
            }
        });
        jPanelWebApps.add(jButtonConfigBuilder1);
        jButtonConfigBuilder1.setBounds(190, 60, 170, 30);

        jButtonSubnetCalculator.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonSubnetCalculator.setText("IPv4/v6 Subnet Calculator");
        jButtonSubnetCalculator.setToolTipText("Just another style with both v4 and v6");
        jButtonSubnetCalculator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSubnetCalculatorActionPerformed(evt);
            }
        });
        jPanelWebApps.add(jButtonSubnetCalculator);
        jButtonSubnetCalculator.setBounds(370, 20, 170, 30);

        jButtonSubnetCalculator1.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonSubnetCalculator1.setText("IPv6 Subnet Calculator");
        jButtonSubnetCalculator1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSubnetCalculator1ActionPerformed(evt);
            }
        });
        jPanelWebApps.add(jButtonSubnetCalculator1);
        jButtonSubnetCalculator1.setBounds(190, 20, 170, 30);

        jButtonIPv4SubnetCalculator.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonIPv4SubnetCalculator.setText("IPv4 Subnet Calculator");
        jButtonIPv4SubnetCalculator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonIPv4SubnetCalculatorActionPerformed(evt);
            }
        });
        jPanelWebApps.add(jButtonIPv4SubnetCalculator);
        jButtonIPv4SubnetCalculator.setBounds(10, 20, 170, 30);

        jButtonRomajiToHiraKata.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonRomajiToHiraKata.setText("Romaji to Hiragana/Katakana");
        jButtonRomajiToHiraKata.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButtonRomajiToHiraKata.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRomajiToHiraKataActionPerformed(evt);
            }
        });
        jPanelWebApps.add(jButtonRomajiToHiraKata);
        jButtonRomajiToHiraKata.setBounds(370, 60, 170, 30);

        jButtonIconGenerator.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonIconGenerator.setText("Icon Generator");
        jButtonIconGenerator.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButtonIconGenerator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonIconGeneratorActionPerformed(evt);
            }
        });
        jPanelWebApps.add(jButtonIconGenerator);
        jButtonIconGenerator.setBounds(10, 100, 170, 30);

        jPanelWebJavaDocs.add(jPanelWebApps);
        jPanelWebApps.setBounds(10, 10, 550, 140);

        jTabbedPaneToolBox.addTab("Web/Java/Docs", jPanelWebJavaDocs);

        jPanelZipEncrypt.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jPanelZipEncrypt.setLayout(null);

        jButtonFolderToZip.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonFolderToZip.setText("Create!");
        jButtonFolderToZip.setToolTipText("");
        jButtonFolderToZip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFolderToZipActionPerformed(evt);
            }
        });
        jPanelZipEncrypt.add(jButtonFolderToZip);
        jButtonFolderToZip.setBounds(310, 140, 170, 20);

        jTextFieldZipSourceFolder.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldZipSourceFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldZipSourceFolderActionPerformed(evt);
            }
        });
        jPanelZipEncrypt.add(jTextFieldZipSourceFolder);
        jTextFieldZipSourceFolder.setBounds(130, 50, 340, 20);

        jTextFieldZipFilename.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jPanelZipEncrypt.add(jTextFieldZipFilename);
        jTextFieldZipFilename.setBounds(130, 80, 350, 20);

        jButton25.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButton25.setText("Browse");
        jButton25.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });
        jPanelZipEncrypt.add(jButton25);
        jButton25.setBounds(480, 50, 70, 20);

        jLabel16.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("Confirm Password: ");
        jPanelZipEncrypt.add(jLabel16);
        jLabel16.setBounds(0, 140, 130, 20);

        jPasswordFieldZip.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jPasswordFieldZip.setToolTipText("");
        jPasswordFieldZip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordFieldZipActionPerformed(evt);
            }
        });
        jPanelZipEncrypt.add(jPasswordFieldZip);
        jPasswordFieldZip.setBounds(130, 110, 170, 21);

        jLabel18.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("Destination Zip: ");
        jPanelZipEncrypt.add(jLabel18);
        jLabel18.setBounds(10, 80, 120, 20);

        jProgressBarZip.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jPanelZipEncrypt.add(jProgressBarZip);
        jProgressBarZip.setBounds(20, 170, 520, 20);

        jLabelFolderToZip4.setFont(new java.awt.Font("Arial Unicode MS", 0, 20)); // NOI18N
        jLabelFolderToZip4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelFolderToZip4.setText("Extract Encrypted Archive");
        jPanelZipEncrypt.add(jLabelFolderToZip4);
        jLabelFolderToZip4.setBounds(20, 220, 530, 40);

        jLabel20.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel20.setText("Source Folder: ");
        jPanelZipEncrypt.add(jLabel20);
        jLabel20.setBounds(40, 50, 90, 20);
        jPanelZipEncrypt.add(jSeparator9);
        jSeparator9.setBounds(10, 210, 540, 10);

        jComboBoxZipEncMethod.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jComboBoxZipEncMethod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "AES-256 Encryption", "Standard Encryption" }));
        jComboBoxZipEncMethod.setToolTipText("Encryption Method");
        jPanelZipEncrypt.add(jComboBoxZipEncMethod);
        jComboBoxZipEncMethod.setBounds(310, 110, 170, 20);

        jLabelFolderToZip7.setFont(new java.awt.Font("Arial Unicode MS", 0, 20)); // NOI18N
        jLabelFolderToZip7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelFolderToZip7.setText("Create Encrypted Archive");
        jPanelZipEncrypt.add(jLabelFolderToZip7);
        jLabelFolderToZip7.setBounds(20, 10, 530, 40);

        jLabel33.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel33.setText("Source Zip: ");
        jPanelZipEncrypt.add(jLabel33);
        jLabel33.setBounds(10, 260, 120, 20);

        jTextFieldZipSourceFile.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldZipSourceFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldZipSourceFileActionPerformed(evt);
            }
        });
        jPanelZipEncrypt.add(jTextFieldZipSourceFile);
        jTextFieldZipSourceFile.setBounds(130, 260, 340, 20);

        jButtonZipBrowseSourceZip.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonZipBrowseSourceZip.setText("Browse");
        jButtonZipBrowseSourceZip.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonZipBrowseSourceZip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonZipBrowseSourceZipActionPerformed(evt);
            }
        });
        jPanelZipEncrypt.add(jButtonZipBrowseSourceZip);
        jButtonZipBrowseSourceZip.setBounds(480, 260, 70, 20);

        jLabel34.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel34.setText("Destination Folder: ");
        jPanelZipEncrypt.add(jLabel34);
        jLabel34.setBounds(0, 290, 130, 20);

        jTextFieldZipDestinationFolder.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jPanelZipEncrypt.add(jTextFieldZipDestinationFolder);
        jTextFieldZipDestinationFolder.setBounds(130, 290, 340, 20);

        jButtonZipBrowseDestinationFolder.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonZipBrowseDestinationFolder.setText("Browse");
        jButtonZipBrowseDestinationFolder.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonZipBrowseDestinationFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonZipBrowseDestinationFolderActionPerformed(evt);
            }
        });
        jPanelZipEncrypt.add(jButtonZipBrowseDestinationFolder);
        jButtonZipBrowseDestinationFolder.setBounds(480, 290, 70, 20);

        jButton39.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButton39.setText("Extract!");
        jButton39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton39ActionPerformed(evt);
            }
        });
        jPanelZipEncrypt.add(jButton39);
        jButton39.setBounds(330, 320, 200, 20);

        jProgressBarZipExtract.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jPanelZipEncrypt.add(jProgressBarZipExtract);
        jProgressBarZipExtract.setBounds(20, 350, 530, 20);

        jLabel17.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Password: ");
        jPanelZipEncrypt.add(jLabel17);
        jLabel17.setBounds(40, 320, 90, 20);

        jPasswordFieldZipConfirm.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jPasswordFieldZipConfirm.setToolTipText("");
        jPasswordFieldZipConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordFieldZipConfirmActionPerformed(evt);
            }
        });
        jPanelZipEncrypt.add(jPasswordFieldZipConfirm);
        jPasswordFieldZipConfirm.setBounds(130, 140, 170, 21);

        jLabel35.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel35.setText("Password: ");
        jPanelZipEncrypt.add(jLabel35);
        jLabel35.setBounds(40, 110, 90, 20);

        jPasswordFieldZipExtract.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jPasswordFieldZipExtract.setToolTipText("");
        jPasswordFieldZipExtract.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordFieldZipExtractActionPerformed(evt);
            }
        });
        jPanelZipEncrypt.add(jPasswordFieldZipExtract);
        jPasswordFieldZipExtract.setBounds(130, 320, 170, 21);

        jTabbedPaneToolBox.addTab("Zip & Encrypt", jPanelZipEncrypt);

        jPanelType7.setLayout(null);

        jTextFieldType7Input.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldType7Input.setText("06240320455D0C3D00211B0C090A2F3921");
        jPanelType7.add(jTextFieldType7Input);
        jTextFieldType7Input.setBounds(20, 50, 530, 20);

        jButton21.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButton21.setText("Plain");
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });
        jPanelType7.add(jButton21);
        jButton21.setBounds(190, 80, 79, 20);

        jButton22.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButton22.setText("Cipher");
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });
        jPanelType7.add(jButton22);
        jButton22.setBounds(300, 80, 80, 20);

        jTextFieldType7Output.setEditable(false);
        jTextFieldType7Output.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jPanelType7.add(jTextFieldType7Output);
        jTextFieldType7Output.setBounds(20, 110, 450, 21);

        jLabelType7reverse.setFont(new java.awt.Font("Arial Unicode MS", 0, 20)); // NOI18N
        jLabelType7reverse.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelType7reverse.setText("Type 7 Conversion");
        jPanelType7.add(jLabelType7reverse);
        jLabelType7reverse.setBounds(110, 10, 350, 40);

        jButton23.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButton23.setText("Copy");
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });
        jPanelType7.add(jButton23);
        jButton23.setBounds(480, 110, 70, 20);

        jTabbedPaneToolBox.addTab("Type 7", jPanelType7);

        jPanelHashGen.setLayout(null);

        jLabel19.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabel19.setText("SHA512:");
        jPanelHashGen.add(jLabel19);
        jLabel19.setBounds(10, 170, 90, 20);

        jButtonGenerateHash.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonGenerateHash.setText("Generate!");
        jButtonGenerateHash.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButtonGenerateHashMousePressed(evt);
            }
        });
        jButtonGenerateHash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGenerateHashActionPerformed(evt);
            }
        });
        jPanelHashGen.add(jButtonGenerateHash);
        jButtonGenerateHash.setBounds(240, 80, 90, 23);

        jTextFieldFileHashGenerate.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jPanelHashGen.add(jTextFieldFileHashGenerate);
        jTextFieldFileHashGenerate.setBounds(50, 50, 400, 20);

        jButton27.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButton27.setText("Browse");
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });
        jPanelHashGen.add(jButton27);
        jButton27.setBounds(460, 50, 90, 20);

        jTextFieldHashSHA512.setEditable(false);
        jTextFieldHashSHA512.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jPanelHashGen.add(jTextFieldHashSHA512);
        jTextFieldHashSHA512.setBounds(70, 170, 430, 20);

        jTextFieldHashMD5.setEditable(false);
        jTextFieldHashMD5.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jPanelHashGen.add(jTextFieldHashMD5);
        jTextFieldHashMD5.setBounds(70, 110, 430, 20);

        jTextFieldHashSHA1.setEditable(false);
        jTextFieldHashSHA1.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jPanelHashGen.add(jTextFieldHashSHA1);
        jTextFieldHashSHA1.setBounds(70, 130, 430, 20);

        jTextFieldHashSHA256.setEditable(false);
        jTextFieldHashSHA256.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jPanelHashGen.add(jTextFieldHashSHA256);
        jTextFieldHashSHA256.setBounds(70, 150, 430, 20);

        jLabel22.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabel22.setText("MD5:");
        jPanelHashGen.add(jLabel22);
        jLabel22.setBounds(10, 110, 90, 20);

        jLabel23.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabel23.setText("SHA1:");
        jPanelHashGen.add(jLabel23);
        jLabel23.setBounds(10, 130, 90, 20);

        jLabel24.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabel24.setText("SHA256:");
        jPanelHashGen.add(jLabel24);
        jLabel24.setBounds(10, 150, 90, 20);

        jLabelHashGenerator.setFont(new java.awt.Font("Arial Unicode MS", 0, 20)); // NOI18N
        jLabelHashGenerator.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelHashGenerator.setText("Hash Generation");
        jPanelHashGen.add(jLabelHashGenerator);
        jLabelHashGenerator.setBounds(110, 10, 350, 40);

        jLabel25.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabel25.setText("File:");
        jPanelHashGen.add(jLabel25);
        jLabel25.setBounds(10, 50, 90, 20);

        jTextAreaHash.setBackground(new java.awt.Color(240, 240, 240));
        jTextAreaHash.setColumns(20);
        jTextAreaHash.setFont(new java.awt.Font("Monospaced", 0, 14)); // NOI18N
        jTextAreaHash.setLineWrap(true);
        jTextAreaHash.setRows(5);
        jScrollPane2.setViewportView(jTextAreaHash);

        jPanelHashGen.add(jScrollPane2);
        jScrollPane2.setBounds(10, 200, 540, 250);

        jButtonHashCopyMD5.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonHashCopyMD5.setText("Copy");
        jButtonHashCopyMD5.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonHashCopyMD5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonHashCopyMD5ActionPerformed(evt);
            }
        });
        jPanelHashGen.add(jButtonHashCopyMD5);
        jButtonHashCopyMD5.setBounds(500, 110, 50, 20);

        jButtonHashCopySHA512.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonHashCopySHA512.setText("Copy");
        jButtonHashCopySHA512.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonHashCopySHA512.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonHashCopySHA512ActionPerformed(evt);
            }
        });
        jPanelHashGen.add(jButtonHashCopySHA512);
        jButtonHashCopySHA512.setBounds(500, 170, 50, 20);

        jButtonHashCopySHA1.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonHashCopySHA1.setText("Copy");
        jButtonHashCopySHA1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonHashCopySHA1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonHashCopySHA1ActionPerformed(evt);
            }
        });
        jPanelHashGen.add(jButtonHashCopySHA1);
        jButtonHashCopySHA1.setBounds(500, 130, 50, 20);

        jButtonHashCopySHA256.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonHashCopySHA256.setText("Copy");
        jButtonHashCopySHA256.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonHashCopySHA256.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonHashCopySHA256ActionPerformed(evt);
            }
        });
        jPanelHashGen.add(jButtonHashCopySHA256);
        jButtonHashCopySHA256.setBounds(500, 150, 50, 20);

        jTabbedPaneToolBox.addTab("Hash Generate", jPanelHashGen);

        jPanelNTPTime.setLayout(null);

        jTextFieldNtpServer.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldNtpServer.setText("tick.usno.navy.mil");
        jPanelNTPTime.add(jTextFieldNtpServer);
        jTextFieldNtpServer.setBounds(140, 50, 290, 20);

        jLabel26.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabel26.setText("Server:");
        jPanelNTPTime.add(jLabel26);
        jLabel26.setBounds(80, 50, 90, 20);

        jLabelGetNTP1.setFont(new java.awt.Font("Arial Unicode MS", 0, 20)); // NOI18N
        jLabelGetNTP1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelGetNTP1.setText("Get NTP Time");
        jPanelNTPTime.add(jLabelGetNTP1);
        jLabelGetNTP1.setBounds(10, 10, 550, 40);

        jTextAreaNTPMessage.setEditable(false);
        jTextAreaNTPMessage.setBackground(new java.awt.Color(240, 240, 240));
        jTextAreaNTPMessage.setColumns(20);
        jTextAreaNTPMessage.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        jTextAreaNTPMessage.setRows(5);
        jScrollPaneNTPMessage.setViewportView(jTextAreaNTPMessage);

        jPanelNTPTime.add(jScrollPaneNTPMessage);
        jScrollPaneNTPMessage.setBounds(20, 90, 530, 260);

        jButton1.setText("Get Time!");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanelNTPTime.add(jButton1);
        jButton1.setBounds(440, 50, 110, 20);

        jTabbedPaneToolBox.addTab("NTP", jPanelNTPTime);

        jPanelToolboxScripts.setLayout(null);

        jPanelScanning.setBorder(javax.swing.BorderFactory.createTitledBorder("Scanning"));
        jPanelScanning.setLayout(null);

        jButtonScriptPowershellPingSweepRange.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptPowershellPingSweepRange.setText("Ping Sweep Range");
        jButtonScriptPowershellPingSweepRange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptPowershellPingSweepRangeActionPerformed(evt);
            }
        });
        jPanelScanning.add(jButtonScriptPowershellPingSweepRange);
        jButtonScriptPowershellPingSweepRange.setBounds(10, 20, 170, 30);

        jButtonScriptTestUDPTCP.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptTestUDPTCP.setText("<html><center>Test TCP/UDP Port<br />(Experimental)</center.</html>");
        jButtonScriptTestUDPTCP.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButtonScriptTestUDPTCP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptTestUDPTCPActionPerformed(evt);
            }
        });
        jPanelScanning.add(jButtonScriptTestUDPTCP);
        jButtonScriptTestUDPTCP.setBounds(190, 20, 170, 30);

        jButtonScriptPingLoggerToFile.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptPingLoggerToFile.setText("Ping a List - Log to CSV");
        jButtonScriptPingLoggerToFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptPingLoggerToFileActionPerformed(evt);
            }
        });
        jPanelScanning.add(jButtonScriptPingLoggerToFile);
        jButtonScriptPingLoggerToFile.setBounds(10, 60, 170, 30);

        jButtonScriptMTUSweep.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptMTUSweep.setText("MTU Sweep");
        jButtonScriptMTUSweep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptMTUSweepActionPerformed(evt);
            }
        });
        jPanelScanning.add(jButtonScriptMTUSweep);
        jButtonScriptMTUSweep.setBounds(370, 20, 170, 30);

        jButtonScriptHashChecker1.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptHashChecker1.setText("PS Nmap-Style Scan");
        jButtonScriptHashChecker1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptHashChecker1ActionPerformed(evt);
            }
        });
        jPanelScanning.add(jButtonScriptHashChecker1);
        jButtonScriptHashChecker1.setBounds(190, 60, 170, 30);

        jPanelToolboxScripts.add(jPanelScanning);
        jPanelScanning.setBounds(10, 10, 550, 100);

        jPanelSyncing.setBorder(javax.swing.BorderFactory.createTitledBorder("Syncing"));
        jPanelSyncing.setLayout(null);

        jButtonSyncStandaloneDevices.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonSyncStandaloneDevices.setText("<html><center>Sync <b>Standalone</b> Devices</center></html>");
        jButtonSyncStandaloneDevices.setEnabled(false);
        jButtonSyncStandaloneDevices.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButtonSyncStandaloneDevices.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSyncStandaloneDevicesActionPerformed(evt);
            }
        });
        jPanelSyncing.add(jButtonSyncStandaloneDevices);
        jButtonSyncStandaloneDevices.setBounds(190, 20, 170, 30);

        jButtonSyncProductionDevices.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonSyncProductionDevices.setText("<html><center>Sync <b>Production</b> Devices</center></html>");
        jButtonSyncProductionDevices.setEnabled(false);
        jButtonSyncProductionDevices.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButtonSyncProductionDevices.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSyncProductionDevicesActionPerformed(evt);
            }
        });
        jPanelSyncing.add(jButtonSyncProductionDevices);
        jButtonSyncProductionDevices.setBounds(10, 20, 170, 30);

        jButtonMapSharedFolder.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonMapSharedFolder.setText("Map Shared Folder");
        jButtonMapSharedFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMapSharedFolderActionPerformed(evt);
            }
        });
        jPanelSyncing.add(jButtonMapSharedFolder);
        jButtonMapSharedFolder.setBounds(370, 20, 170, 30);

        jButtonEditProductionDevicesList1.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonEditProductionDevicesList1.setText("<html><center>Edit <b>Production</b> Devices List</center></html>");
        jButtonEditProductionDevicesList1.setEnabled(false);
        jButtonEditProductionDevicesList1.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButtonEditProductionDevicesList1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditProductionDevicesList1ActionPerformed(evt);
            }
        });
        jPanelSyncing.add(jButtonEditProductionDevicesList1);
        jButtonEditProductionDevicesList1.setBounds(10, 60, 170, 30);

        jButtonEditProductionDevicesList.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonEditProductionDevicesList.setText("<html><center>Edit <b>Standalone</b> Devices List</center></html>");
        jButtonEditProductionDevicesList.setEnabled(false);
        jButtonEditProductionDevicesList.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButtonEditProductionDevicesList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditProductionDevicesListActionPerformed(evt);
            }
        });
        jPanelSyncing.add(jButtonEditProductionDevicesList);
        jButtonEditProductionDevicesList.setBounds(190, 60, 170, 30);

        jPanelToolboxScripts.add(jPanelSyncing);
        jPanelSyncing.setBounds(10, 120, 550, 100);

        jPanelMiscellaneous.setBorder(javax.swing.BorderFactory.createTitledBorder("Miscellaneous"));
        jPanelMiscellaneous.setLayout(null);

        jButtonScriptCreateDummyFile.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptCreateDummyFile.setText("Create Dummy File");
        jButtonScriptCreateDummyFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCreateDummyFileActionPerformed(evt);
            }
        });
        jPanelMiscellaneous.add(jButtonScriptCreateDummyFile);
        jButtonScriptCreateDummyFile.setBounds(370, 60, 170, 30);

        jButtonScriptiPerfServer.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptiPerfServer.setText("iPerf Server");
        jButtonScriptiPerfServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptiPerfServerActionPerformed(evt);
            }
        });
        jPanelMiscellaneous.add(jButtonScriptiPerfServer);
        jButtonScriptiPerfServer.setBounds(190, 20, 170, 30);

        jButtonScriptSendMessage.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptSendMessage.setText("Send Message");
        jButtonScriptSendMessage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptSendMessageActionPerformed(evt);
            }
        });
        jPanelMiscellaneous.add(jButtonScriptSendMessage);
        jButtonScriptSendMessage.setBounds(10, 60, 170, 30);

        jButtonScriptiPerfClient.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptiPerfClient.setText("iPerf Client");
        jButtonScriptiPerfClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptiPerfClientActionPerformed(evt);
            }
        });
        jPanelMiscellaneous.add(jButtonScriptiPerfClient);
        jButtonScriptiPerfClient.setBounds(370, 20, 170, 30);

        jButtonScriptGetNTPTimePS.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptGetNTPTimePS.setText("<html><center>Get NTP Time<br />(Experimental)</center></html>");
        jButtonScriptGetNTPTimePS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptGetNTPTimePSActionPerformed(evt);
            }
        });
        jPanelMiscellaneous.add(jButtonScriptGetNTPTimePS);
        jButtonScriptGetNTPTimePS.setBounds(190, 60, 170, 30);

        jButtonScriptHashChecker.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptHashChecker.setText("Hash Checker");
        jButtonScriptHashChecker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptHashCheckerActionPerformed(evt);
            }
        });
        jPanelMiscellaneous.add(jButtonScriptHashChecker);
        jButtonScriptHashChecker.setBounds(10, 20, 170, 30);

        jButton31.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButton31.setText("Reset SecureCRT Settings");
        jButton31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton31ActionPerformed(evt);
            }
        });
        jPanelMiscellaneous.add(jButton31);
        jButton31.setBounds(10, 100, 170, 30);

        jButton45.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButton45.setText("Image Folder to Base64");
        jButton45.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton45ActionPerformed(evt);
            }
        });
        jPanelMiscellaneous.add(jButton45);
        jButton45.setBounds(370, 100, 170, 30);

        jButton46.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButton46.setText("Image to Base64");
        jButton46.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton46ActionPerformed(evt);
            }
        });
        jPanelMiscellaneous.add(jButton46);
        jButton46.setBounds(190, 100, 170, 30);

        jButtonScriptKeepAwake.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptKeepAwake.setText("Keep Awake");
        jButtonScriptKeepAwake.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptKeepAwakeActionPerformed(evt);
            }
        });
        jPanelMiscellaneous.add(jButtonScriptKeepAwake);
        jButtonScriptKeepAwake.setBounds(190, 140, 170, 30);

        jButtonCiscoType7Decrypt.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonCiscoType7Decrypt.setText("Type 7 Reverse");
        jButtonCiscoType7Decrypt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCiscoType7DecryptActionPerformed(evt);
            }
        });
        jPanelMiscellaneous.add(jButtonCiscoType7Decrypt);
        jButtonCiscoType7Decrypt.setBounds(10, 140, 170, 30);

        jPanelToolboxScripts.add(jPanelMiscellaneous);
        jPanelMiscellaneous.setBounds(10, 230, 550, 180);

        jTabbedPaneToolBox.addTab("Built-in Scripts", jPanelToolboxScripts);

        jTabbedMain.addTab("ToolBox", jTabbedPaneToolBox);

        jTabbedPaneSettings.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N

        jPanelSettingsMain.setLayout(null);

        jButtonReportIssue.setBackground(new java.awt.Color(255, 178, 174));
        jButtonReportIssue.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonReportIssue.setText("Submit a change request");
        jButtonReportIssue.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonReportIssue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReportIssueActionPerformed(evt);
            }
        });
        jPanelSettingsMain.add(jButtonReportIssue);
        jButtonReportIssue.setBounds(10, 540, 170, 20);

        jTextField2.setBackground(new java.awt.Color(51, 51, 51));
        jTextField2.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextField2.setForeground(new java.awt.Color(255, 204, 153));
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField2KeyTyped(evt);
            }
        });
        jPanelSettingsMain.add(jTextField2);
        jTextField2.setBounds(370, 540, 190, 20);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Personal Items (Never overwritten)"));
        jPanel3.setLayout(null);

        jButton38.setBackground(new java.awt.Color(204, 204, 255));
        jButton38.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButton38.setText("Edit personal properties file");
        jButton38.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton38ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton38);
        jButton38.setBounds(190, 110, 170, 20);

        jButton32.setBackground(new java.awt.Color(204, 204, 255));
        jButton32.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButton32.setText("Edit personal favorites list");
        jButton32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton32ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton32);
        jButton32.setBounds(370, 110, 170, 20);

        jButton30.setBackground(new java.awt.Color(255, 233, 162));
        jButton30.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButton30.setText("Open logging-output folder");
        jButton30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton30ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton30);
        jButton30.setBounds(10, 140, 170, 20);

        jButton43.setBackground(new java.awt.Color(255, 233, 162));
        jButton43.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButton43.setText("Open personal folder");
        jButton43.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton43ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton43);
        jButton43.setBounds(10, 110, 170, 20);

        jLabelListTextSizePreview.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N
        jLabelListTextSizePreview.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabelListTextSizePreview.setText("c9300-a01-abcde-1234");
        jPanel3.add(jLabelListTextSizePreview);
        jLabelListTextSizePreview.setBounds(250, 40, 280, 40);

        jSliderListTextSize.setMaximum(6);
        jSliderListTextSize.setPaintLabels(true);
        jSliderListTextSize.setSnapToTicks(true);
        jSliderListTextSize.setValue(1);
        //- Set Personal Text Setting
        try {
            String myValue = PropertyHandlerPersonal.getInstance().getValue("Setting.TextSize");
            if("".equals(myValue)) {
                PropertyHandlerPersonal.getInstance().setValue("Setting.TextSize", "1");
            }
            jSliderListTextSize.setValue(Integer.parseInt(PropertyHandlerPersonal.getInstance().getValue("Setting.TextSize")));

        } catch (NullPointerException e) {System.out.println("TextSize Goofed");
        }
        jSliderListTextSize.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSliderListTextSizeStateChanged(evt);
            }
        });
        jPanel3.add(jSliderListTextSize);
        jSliderListTextSize.setBounds(100, 50, 140, 20);

        jLabelListTextSize1.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelListTextSize1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelListTextSize1.setText("List text size:");
        jPanel3.add(jLabelListTextSize1);
        jLabelListTextSize1.setBounds(10, 50, 80, 20);

        buttonGroupLanguage.add(jRadioButtonJapanese);
        jRadioButtonJapanese.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N
        jRadioButtonJapanese.setText("日本語");
        jRadioButtonJapanese.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jRadioButtonJapanese.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonJapaneseActionPerformed(evt);
            }
        });
        jPanel3.add(jRadioButtonJapanese);
        jRadioButtonJapanese.setBounds(180, 20, 80, 20);

        buttonGroupLanguage.add(jRadioButtonEnglish);
        jRadioButtonEnglish.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jRadioButtonEnglish.setText("English");
        jRadioButtonEnglish.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jRadioButtonEnglish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonEnglishActionPerformed(evt);
            }
        });
        jPanel3.add(jRadioButtonEnglish);
        jRadioButtonEnglish.setBounds(100, 20, 70, 20);

        jLabelLanguageSelect.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelLanguageSelect.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLanguageSelect.setText("Language:");
        jPanel3.add(jLabelLanguageSelect);
        jLabelLanguageSelect.setBounds(10, 20, 80, 20);

        jButtonCopyShortcutToDesktop.setBackground(new java.awt.Color(206, 221, 226));
        jButtonCopyShortcutToDesktop.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonCopyShortcutToDesktop.setText("Copy shortcut to desktop");
        jButtonCopyShortcutToDesktop.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonCopyShortcutToDesktop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCopyShortcutToDesktopActionPerformed(evt);
            }
        });
        jPanel3.add(jButtonCopyShortcutToDesktop);
        jButtonCopyShortcutToDesktop.setBounds(190, 140, 170, 20);

        jCheckBoxChangePingOnSessionSelect.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jCheckBoxChangePingOnSessionSelect.setText("Change ping host field on session select");
        jCheckBoxChangePingOnSessionSelect.setToolTipText("");
        jPanel3.add(jCheckBoxChangePingOnSessionSelect);
        jCheckBoxChangePingOnSessionSelect.setBounds(10, 80, 270, 20);

        jPanelSettingsMain.add(jPanel3);
        jPanel3.setBounds(10, 10, 550, 170);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Shared Items (Will be overwritten by local policy)"));
        jPanel4.setLayout(null);

        jLabelEnablePWauth.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelEnablePWauth.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelEnablePWauth.setText("Preloaded session IP:");
        jLabelEnablePWauth.setToolTipText("Preloaded IP for SSH/RDP/HTTPS etc.");
        jPanel4.add(jLabelEnablePWauth);
        jLabelEnablePWauth.setBounds(0, 50, 120, 20);

        buttonGroupPWauthEnableDisable.add(jRadioButtonPWauthEnabled);
        jRadioButtonPWauthEnabled.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jRadioButtonPWauthEnabled.setText("Enabled");
        jRadioButtonPWauthEnabled.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jRadioButtonPWauthEnabled.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonPWauthEnabledActionPerformed(evt);
            }
        });
        jPanel4.add(jRadioButtonPWauthEnabled);
        jRadioButtonPWauthEnabled.setBounds(130, 110, 70, 20);

        buttonGroupPWauthEnableDisable.add(jRadioButtonPWauthDisabled);
        jRadioButtonPWauthDisabled.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jRadioButtonPWauthDisabled.setText("Disabled");
        jRadioButtonPWauthDisabled.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jRadioButtonPWauthDisabled.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonPWauthDisabledActionPerformed(evt);
            }
        });
        jPanel4.add(jRadioButtonPWauthDisabled);
        jRadioButtonPWauthDisabled.setBounds(210, 110, 63, 20);

        jComboBoxClassification.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jComboBoxClassification.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "None", "Unclassified", "Confidential", "Secret", "Top Secret", "SCI", "Coalition" }));
        jComboBoxClassification.setSelectedItem(PropertyHandler.getInstance().getValue("Setting.Classification"));
        jComboBoxClassification.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxClassificationItemStateChanged(evt);
            }
        });
        jPanel4.add(jComboBoxClassification);
        jComboBoxClassification.setBounds(130, 140, 150, 20);

        jLabelEnablePWauth1.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelEnablePWauth1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelEnablePWauth1.setText("SSH password auth:");
        jPanel4.add(jLabelEnablePWauth1);
        jLabelEnablePWauth1.setBounds(0, 110, 120, 20);

        jButton28.setBackground(new java.awt.Color(255, 233, 162));
        jButton28.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButton28.setText("Open LaunchPad folder");
        jButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton28ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton28);
        jButton28.setBounds(10, 200, 170, 20);

        jButton34.setBackground(new java.awt.Color(210, 210, 210));
        jButton34.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButton34.setText("View shared properties file");
        jButton34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton34ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton34);
        jButton34.setBounds(190, 200, 170, 20);

        jButton35.setBackground(new java.awt.Color(204, 204, 255));
        jButton35.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButton35.setText("Edit shared properties file");
        jButton35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton35ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton35);
        jButton35.setBounds(370, 200, 170, 20);

        jLabelEnablePWauth2.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelEnablePWauth2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelEnablePWauth2.setText("Preferred IP prefixes:");
        jLabelEnablePWauth2.setToolTipText("IP prefixes that will be preferred when multiple network interfaces exist.");
        jPanel4.add(jLabelEnablePWauth2);
        jLabelEnablePWauth2.setBounds(0, 170, 120, 20);

        jTextFieldSecureCRTPath.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldSecureCRTPathKeyReleased(evt);
            }
        });
        jPanel4.add(jTextFieldSecureCRTPath);
        jTextFieldSecureCRTPath.setBounds(130, 20, 410, 20);

        jLabelEnablePWauth3.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelEnablePWauth3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelEnablePWauth3.setText("SecureCRT path:");
        jLabelEnablePWauth3.setToolTipText("Path to SecureCRT.exe");
        jPanel4.add(jLabelEnablePWauth3);
        jLabelEnablePWauth3.setBounds(20, 20, 100, 20);

        jTextFieldPreloadedIP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldPreloadedIPKeyReleased(evt);
            }
        });
        jPanel4.add(jTextFieldPreloadedIP);
        jTextFieldPreloadedIP.setBounds(130, 50, 100, 20);

        jLabelEnablePWauth4.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelEnablePWauth4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelEnablePWauth4.setText("Preloaded ping IP:");
        jLabelEnablePWauth4.setToolTipText("Preloaded IP for the Ping section.");
        jPanel4.add(jLabelEnablePWauth4);
        jLabelEnablePWauth4.setBounds(240, 50, 90, 20);

        jTextFieldPreloadedPingIP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldPreloadedPingIPKeyReleased(evt);
            }
        });
        jPanel4.add(jTextFieldPreloadedPingIP);
        jTextFieldPreloadedPingIP.setBounds(340, 50, 100, 20);

        jLabelListTextSize3.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelListTextSize3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelListTextSize3.setText("Session double click:");
        jPanel4.add(jLabelListTextSize3);
        jLabelListTextSize3.setBounds(0, 80, 120, 20);

        jComboBoxSessionDoubleClick.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jComboBoxSessionDoubleClick.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "PKI", "SSH", "RDP", "HTTPS" }));
        jComboBoxSessionDoubleClick.setSelectedItem(PropertyHandler.getInstance().getValue("ButtonExecuteFunction.DoubleClick"));
        jComboBoxSessionDoubleClick.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxSessionDoubleClickItemStateChanged(evt);
            }
        });
        jPanel4.add(jComboBoxSessionDoubleClick);
        jComboBoxSessionDoubleClick.setBounds(130, 80, 62, 20);

        jLabelListTextSize4.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelListTextSize4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelListTextSize4.setText("Session enter press:");
        jPanel4.add(jLabelListTextSize4);
        jLabelListTextSize4.setBounds(190, 80, 120, 20);

        jComboBoxSessionEnterPress.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jComboBoxSessionEnterPress.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "PKI", "SSH", "RDP", "HTTPS" }));
        jComboBoxSessionEnterPress.setSelectedItem(PropertyHandler.getInstance().getValue("ButtonExecuteFunction.OnEnterPress"));
        jPanel4.add(jComboBoxSessionEnterPress);
        jComboBoxSessionEnterPress.setBounds(320, 80, 62, 20);

        jLabelEnablePWauth5.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelEnablePWauth5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelEnablePWauth5.setText("Classification:");
        jPanel4.add(jLabelEnablePWauth5);
        jLabelEnablePWauth5.setBounds(20, 140, 100, 20);

        jTextFieldSettingPreferredIpTertiary.setToolTipText("Tertiary");
        jTextFieldSettingPreferredIpTertiary.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldSettingPreferredIpTertiaryKeyReleased(evt);
            }
        });
        jPanel4.add(jTextFieldSettingPreferredIpTertiary);
        jTextFieldSettingPreferredIpTertiary.setBounds(350, 170, 100, 20);

        jTextFieldSettingPreferredIpPrimary.setToolTipText("Primary");
        jTextFieldSettingPreferredIpPrimary.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldSettingPreferredIpPrimaryKeyReleased(evt);
            }
        });
        jPanel4.add(jTextFieldSettingPreferredIpPrimary);
        jTextFieldSettingPreferredIpPrimary.setBounds(130, 170, 100, 20);

        jTextFieldSettingPreferredIpSecondary.setToolTipText("Secondary");
        jTextFieldSettingPreferredIpSecondary.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldSettingPreferredIpSecondaryKeyReleased(evt);
            }
        });
        jPanel4.add(jTextFieldSettingPreferredIpSecondary);
        jTextFieldSettingPreferredIpSecondary.setBounds(240, 170, 100, 20);

        jPanelSettingsMain.add(jPanel4);
        jPanel4.setBounds(10, 230, 550, 230);

        jButtonScriptUpdateLaunchPad.setBackground(new java.awt.Color(255, 211, 148));
        jButtonScriptUpdateLaunchPad.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonScriptUpdateLaunchPad.setText("Force update");
        jButtonScriptUpdateLaunchPad.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptUpdateLaunchPad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptUpdateLaunchPadActionPerformed(evt);
            }
        });
        jPanelSettingsMain.add(jButtonScriptUpdateLaunchPad);
        jButtonScriptUpdateLaunchPad.setBounds(220, 540, 120, 20);

        jButtonViewExampleUpdateScript.setBackground(new java.awt.Color(210, 210, 210));
        jButtonViewExampleUpdateScript.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButtonViewExampleUpdateScript.setText("View example PS update script");
        jButtonViewExampleUpdateScript.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonViewExampleUpdateScript.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonViewExampleUpdateScriptActionPerformed(evt);
            }
        });
        jPanelSettingsMain.add(jButtonViewExampleUpdateScript);
        jButtonViewExampleUpdateScript.setBounds(10, 510, 170, 20);

        jTabbedPaneSettings.addTab("Main", jPanelSettingsMain);

        jPanelSettingsButtons.setPreferredSize(new java.awt.Dimension(555, 2300));

        jButton33.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButton33.setText("Icon List");
        jButton33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton33ActionPerformed(evt);
            }
        });

        jTextFieldButtonExecute1.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldButtonExecute1.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonExecute1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonExecute1KeyReleased(evt);
            }
        });

        jLabelButtonToolTip1.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelButtonToolTip1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonToolTip1.setText("Button 1:");

        jTextFieldButtonToolTip1.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonToolTip1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonToolTip1KeyReleased(evt);
            }
        });

        jLabelButtonIcon1.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonIcon1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonIcon1.setText("Icon:");

        jLabelButtonExecute1.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonExecute1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonExecute1.setText("Execute:");

        jComboBoxButtonIcon1.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jComboBoxButtonIcon1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxButtonIcon1.setModel(new DefaultComboBoxModel(arrIconList));
        jComboBoxButtonIcon1.setSelectedItem(PropertyHandler.getInstance().getValue("Button.01.Icon"));
        jComboBoxButtonIcon1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxButtonIcon1ItemStateChanged(evt);
            }
        });

        jTextFieldButtonExecute2.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldButtonExecute2.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonExecute2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonExecute2KeyReleased(evt);
            }
        });

        jLabelButtonToolTip2.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelButtonToolTip2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonToolTip2.setText("Button 2:");

        jTextFieldButtonToolTip2.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonToolTip2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonToolTip2KeyReleased(evt);
            }
        });

        jLabelButtonIcon2.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonIcon2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonIcon2.setText("Icon:");

        jLabelButtonExecute2.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonExecute2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonExecute2.setText("Execute:");

        jComboBoxButtonIcon2.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jComboBoxButtonIcon2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxButtonIcon2.setModel(new DefaultComboBoxModel(arrIconList));
        jComboBoxButtonIcon2.setSelectedItem(PropertyHandler.getInstance().getValue("Button.02.Icon"));
        jComboBoxButtonIcon2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxButtonIcon2ItemStateChanged(evt);
            }
        });

        jTextFieldButtonExecute3.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldButtonExecute3.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonExecute3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonExecute3KeyReleased(evt);
            }
        });

        jLabelButtonToolTip3.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelButtonToolTip3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonToolTip3.setText("Button 3:");

        jTextFieldButtonToolTip3.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonToolTip3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonToolTip3KeyReleased(evt);
            }
        });

        jLabelButtonIcon3.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonIcon3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonIcon3.setText("Icon:");

        jLabelButtonExecute3.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonExecute3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonExecute3.setText("Execute:");

        jComboBoxButtonIcon3.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jComboBoxButtonIcon3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxButtonIcon3.setModel(new DefaultComboBoxModel(arrIconList));
        jComboBoxButtonIcon3.setSelectedItem(PropertyHandler.getInstance().getValue("Button.03.Icon"));
        jComboBoxButtonIcon3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxButtonIcon3ItemStateChanged(evt);
            }
        });

        jTextFieldButtonExecute4.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldButtonExecute4.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonExecute4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonExecute4KeyReleased(evt);
            }
        });

        jLabelButtonToolTip4.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelButtonToolTip4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonToolTip4.setText("Button 4:");

        jTextFieldButtonToolTip4.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonToolTip4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonToolTip4KeyReleased(evt);
            }
        });

        jLabelButtonIcon4.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonIcon4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonIcon4.setText("Icon:");

        jLabelButtonExecute4.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonExecute4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonExecute4.setText("Execute:");

        jComboBoxButtonIcon4.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jComboBoxButtonIcon4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxButtonIcon4.setModel(new DefaultComboBoxModel(arrIconList));
        jComboBoxButtonIcon4.setSelectedItem(PropertyHandler.getInstance().getValue("Button.04.Icon"));
        jComboBoxButtonIcon4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxButtonIcon4ItemStateChanged(evt);
            }
        });

        jTextFieldButtonExecute5.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldButtonExecute5.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonExecute5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonExecute5KeyReleased(evt);
            }
        });

        jLabelButtonToolTip5.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelButtonToolTip5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonToolTip5.setText("Button 5:");

        jTextFieldButtonToolTip5.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonToolTip5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonToolTip5KeyReleased(evt);
            }
        });

        jLabelButtonIcon5.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonIcon5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonIcon5.setText("Icon:");

        jLabelButtonExecute5.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonExecute5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonExecute5.setText("Execute:");

        jComboBoxButtonIcon5.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jComboBoxButtonIcon5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxButtonIcon5.setModel(new DefaultComboBoxModel(arrIconList));
        jComboBoxButtonIcon5.setSelectedItem(PropertyHandler.getInstance().getValue("Button.05.Icon"));
        jComboBoxButtonIcon5.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxButtonIcon5ItemStateChanged(evt);
            }
        });

        jTextFieldButtonExecute6.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldButtonExecute6.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonExecute6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonExecute6KeyReleased(evt);
            }
        });

        jLabelButtonToolTip6.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelButtonToolTip6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonToolTip6.setText("Button 6:");

        jTextFieldButtonToolTip6.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonToolTip6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonToolTip6KeyReleased(evt);
            }
        });

        jLabelButtonIcon6.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonIcon6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonIcon6.setText("Icon:");

        jLabelButtonExecute6.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonExecute6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonExecute6.setText("Execute:");

        jComboBoxButtonIcon6.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jComboBoxButtonIcon6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxButtonIcon6.setModel(new DefaultComboBoxModel(arrIconList));
        jComboBoxButtonIcon6.setSelectedItem(PropertyHandler.getInstance().getValue("Button.06.Icon"));
        jComboBoxButtonIcon6.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxButtonIcon6ItemStateChanged(evt);
            }
        });

        jTextFieldButtonExecute7.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldButtonExecute7.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonExecute7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonExecute7KeyReleased(evt);
            }
        });

        jLabelButtonToolTip7.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelButtonToolTip7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonToolTip7.setText("Button 7:");

        jTextFieldButtonToolTip7.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonToolTip7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonToolTip7KeyReleased(evt);
            }
        });

        jLabelButtonIcon7.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonIcon7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonIcon7.setText("Icon:");

        jLabelButtonExecute7.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonExecute7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonExecute7.setText("Execute:");

        jComboBoxButtonIcon7.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jComboBoxButtonIcon7.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxButtonIcon7.setModel(new DefaultComboBoxModel(arrIconList));
        jComboBoxButtonIcon7.setSelectedItem(PropertyHandler.getInstance().getValue("Button.07.Icon"));
        jComboBoxButtonIcon7.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxButtonIcon7ItemStateChanged(evt);
            }
        });

        jTextFieldButtonExecute8.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldButtonExecute8.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonExecute8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonExecute8KeyReleased(evt);
            }
        });

        jLabelButtonToolTip8.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelButtonToolTip8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonToolTip8.setText("Button 8:");

        jTextFieldButtonToolTip8.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonToolTip8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonToolTip8KeyReleased(evt);
            }
        });

        jLabelButtonIcon8.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonIcon8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonIcon8.setText("Icon:");

        jLabelButtonExecute8.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonExecute8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonExecute8.setText("Execute:");

        jComboBoxButtonIcon8.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jComboBoxButtonIcon8.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxButtonIcon8.setModel(new DefaultComboBoxModel(arrIconList));
        jComboBoxButtonIcon8.setSelectedItem(PropertyHandler.getInstance().getValue("Button.08.Icon"));
        jComboBoxButtonIcon8.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxButtonIcon8ItemStateChanged(evt);
            }
        });

        jLabelButtonExecute9.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonExecute9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonExecute9.setText("Execute:");

        jComboBoxButtonIcon9.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jComboBoxButtonIcon9.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxButtonIcon9.setModel(new DefaultComboBoxModel(arrIconList));
        jComboBoxButtonIcon9.setSelectedItem(PropertyHandler.getInstance().getValue("Button.09.Icon"));
        jComboBoxButtonIcon9.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxButtonIcon9ItemStateChanged(evt);
            }
        });

        jTextFieldButtonExecute9.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldButtonExecute9.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonExecute9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonExecute9KeyReleased(evt);
            }
        });

        jLabelButtonToolTip9.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelButtonToolTip9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonToolTip9.setText("Button 9:");

        jTextFieldButtonToolTip9.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonToolTip9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonToolTip9KeyReleased(evt);
            }
        });

        jLabelButtonIcon9.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonIcon9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonIcon9.setText("Icon:");

        jLabelButtonExecute10.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonExecute10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonExecute10.setText("Execute:");

        jComboBoxButtonIcon10.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jComboBoxButtonIcon10.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxButtonIcon10.setModel(new DefaultComboBoxModel(arrIconList));
        jComboBoxButtonIcon10.setSelectedItem(PropertyHandler.getInstance().getValue("Button.10.Icon"));
        jComboBoxButtonIcon10.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxButtonIcon10ItemStateChanged(evt);
            }
        });

        jTextFieldButtonExecute10.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldButtonExecute10.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonExecute10.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonExecute10KeyReleased(evt);
            }
        });

        jLabelButtonToolTip10.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelButtonToolTip10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonToolTip10.setText("Button 10:");

        jTextFieldButtonToolTip10.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonToolTip10.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonToolTip10KeyReleased(evt);
            }
        });

        jLabelButtonIcon10.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonIcon10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonIcon10.setText("Icon:");

        jLabelButtonExecute11.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonExecute11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonExecute11.setText("Execute:");

        jComboBoxButtonIcon11.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jComboBoxButtonIcon11.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxButtonIcon11.setModel(new DefaultComboBoxModel(arrIconList));
        jComboBoxButtonIcon11.setSelectedItem(PropertyHandler.getInstance().getValue("Button.11.Icon"));
        jComboBoxButtonIcon11.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxButtonIcon11ItemStateChanged(evt);
            }
        });

        jTextFieldButtonExecute11.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldButtonExecute11.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonExecute11.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonExecute11KeyReleased(evt);
            }
        });

        jLabelButtonToolTip11.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelButtonToolTip11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonToolTip11.setText("Button 11:");

        jTextFieldButtonToolTip11.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonToolTip11.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonToolTip11KeyReleased(evt);
            }
        });

        jLabelButtonIcon11.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonIcon11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonIcon11.setText("Icon:");

        jLabelButtonExecute12.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonExecute12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonExecute12.setText("Execute:");

        jComboBoxButtonIcon12.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jComboBoxButtonIcon12.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxButtonIcon12.setModel(new DefaultComboBoxModel(arrIconList));
        jComboBoxButtonIcon12.setSelectedItem(PropertyHandler.getInstance().getValue("Button.12.Icon"));
        jComboBoxButtonIcon12.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxButtonIcon12ItemStateChanged(evt);
            }
        });

        jTextFieldButtonExecute12.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldButtonExecute12.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonExecute12.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonExecute12KeyReleased(evt);
            }
        });

        jLabelButtonToolTip12.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelButtonToolTip12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonToolTip12.setText("Button 12:");

        jTextFieldButtonToolTip12.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonToolTip12.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonToolTip12KeyReleased(evt);
            }
        });

        jLabelButtonIcon12.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonIcon12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonIcon12.setText("Icon:");

        jLabelButtonExecute13.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonExecute13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonExecute13.setText("Execute:");

        jComboBoxButtonIcon13.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jComboBoxButtonIcon13.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxButtonIcon13.setModel(new DefaultComboBoxModel(arrIconList));
        jComboBoxButtonIcon13.setSelectedItem(PropertyHandler.getInstance().getValue("Button.13.Icon"));
        jComboBoxButtonIcon13.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxButtonIcon13ItemStateChanged(evt);
            }
        });

        jTextFieldButtonExecute13.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldButtonExecute13.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonExecute13.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonExecute13KeyReleased(evt);
            }
        });

        jLabelButtonToolTip13.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelButtonToolTip13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonToolTip13.setText("Button 13:");

        jTextFieldButtonToolTip13.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonToolTip13.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonToolTip13KeyReleased(evt);
            }
        });

        jLabelButtonIcon13.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonIcon13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonIcon13.setText("Icon:");

        jLabelButtonExecute14.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonExecute14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonExecute14.setText("Execute:");

        jComboBoxButtonIcon14.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jComboBoxButtonIcon14.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxButtonIcon14.setModel(new DefaultComboBoxModel(arrIconList));
        jComboBoxButtonIcon14.setSelectedItem(PropertyHandler.getInstance().getValue("Button.14.Icon"));
        jComboBoxButtonIcon14.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxButtonIcon14ItemStateChanged(evt);
            }
        });

        jTextFieldButtonExecute14.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldButtonExecute14.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonExecute14.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonExecute14KeyReleased(evt);
            }
        });

        jLabelButtonToolTip14.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelButtonToolTip14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonToolTip14.setText("Button 14:");

        jTextFieldButtonToolTip14.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonToolTip14.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonToolTip14KeyReleased(evt);
            }
        });

        jLabelButtonIcon14.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonIcon14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonIcon14.setText("Icon:");

        jLabelButtonExecute15.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonExecute15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonExecute15.setText("Execute:");

        jComboBoxButtonIcon15.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jComboBoxButtonIcon15.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxButtonIcon15.setModel(new DefaultComboBoxModel(arrIconList));
        jComboBoxButtonIcon15.setSelectedItem(PropertyHandler.getInstance().getValue("Button.15.Icon"));
        jComboBoxButtonIcon15.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxButtonIcon15ItemStateChanged(evt);
            }
        });

        jTextFieldButtonExecute15.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldButtonExecute15.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonExecute15.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonExecute15KeyReleased(evt);
            }
        });

        jLabelButtonToolTip15.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelButtonToolTip15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonToolTip15.setText("Button 15:");

        jTextFieldButtonToolTip15.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonToolTip15.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonToolTip15KeyReleased(evt);
            }
        });

        jLabelButtonIcon15.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonIcon15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonIcon15.setText("Icon:");

        jLabelButtonExecute16.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonExecute16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonExecute16.setText("Execute:");

        jComboBoxButtonIcon16.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jComboBoxButtonIcon16.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxButtonIcon16.setModel(new DefaultComboBoxModel(arrIconList));
        jComboBoxButtonIcon16.setSelectedItem(PropertyHandler.getInstance().getValue("Button.16.Icon"));
        jComboBoxButtonIcon16.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxButtonIcon16ItemStateChanged(evt);
            }
        });

        jTextFieldButtonExecute16.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldButtonExecute16.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonExecute16.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonExecute16KeyReleased(evt);
            }
        });

        jLabelButtonToolTip16.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelButtonToolTip16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonToolTip16.setText("Button 16:");

        jTextFieldButtonToolTip16.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonToolTip16.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonToolTip16KeyReleased(evt);
            }
        });

        jLabelButtonIcon16.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonIcon16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonIcon16.setText("Icon:");

        jLabelButtonExecute17.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonExecute17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonExecute17.setText("Execute:");

        jComboBoxButtonIcon17.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jComboBoxButtonIcon17.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxButtonIcon17.setModel(new DefaultComboBoxModel(arrIconList));
        jComboBoxButtonIcon17.setSelectedItem(PropertyHandler.getInstance().getValue("Button.17.Icon"));
        jComboBoxButtonIcon17.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxButtonIcon17ItemStateChanged(evt);
            }
        });

        jTextFieldButtonExecute17.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldButtonExecute17.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonExecute17.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonExecute17KeyReleased(evt);
            }
        });

        jLabelButtonToolTip17.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelButtonToolTip17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonToolTip17.setText("Button 17:");

        jTextFieldButtonToolTip17.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonToolTip17.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonToolTip17KeyReleased(evt);
            }
        });

        jLabelButtonIcon17.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonIcon17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonIcon17.setText("Icon:");

        jLabelButtonExecute18.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonExecute18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonExecute18.setText("Execute:");

        jComboBoxButtonIcon18.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jComboBoxButtonIcon18.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxButtonIcon18.setModel(new DefaultComboBoxModel(arrIconList));
        jComboBoxButtonIcon18.setSelectedItem(PropertyHandler.getInstance().getValue("Button.18.Icon"));
        jComboBoxButtonIcon18.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxButtonIcon18ItemStateChanged(evt);
            }
        });

        jTextFieldButtonExecute18.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldButtonExecute18.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonExecute18.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonExecute18KeyReleased(evt);
            }
        });

        jLabelButtonToolTip18.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelButtonToolTip18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonToolTip18.setText("Button 18:");

        jTextFieldButtonToolTip18.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonToolTip18.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonToolTip18KeyReleased(evt);
            }
        });

        jLabelButtonIcon18.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonIcon18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonIcon18.setText("Icon:");

        jLabelButtonExecute19.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonExecute19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonExecute19.setText("Execute:");

        jComboBoxButtonIcon19.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jComboBoxButtonIcon19.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxButtonIcon19.setModel(new DefaultComboBoxModel(arrIconList));
        jComboBoxButtonIcon19.setSelectedItem(PropertyHandler.getInstance().getValue("Button.19.Icon"));
        jComboBoxButtonIcon19.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxButtonIcon19ItemStateChanged(evt);
            }
        });

        jTextFieldButtonExecute19.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldButtonExecute19.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonExecute19.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonExecute19KeyReleased(evt);
            }
        });

        jLabelButtonToolTip19.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelButtonToolTip19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonToolTip19.setText("Button 19:");

        jTextFieldButtonToolTip19.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonToolTip19.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonToolTip19KeyReleased(evt);
            }
        });

        jLabelButtonIcon19.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonIcon19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonIcon19.setText("Icon:");

        jLabelButtonExecute20.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonExecute20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonExecute20.setText("Execute:");

        jComboBoxButtonIcon20.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jComboBoxButtonIcon20.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxButtonIcon20.setModel(new DefaultComboBoxModel(arrIconList));
        jComboBoxButtonIcon20.setSelectedItem(PropertyHandler.getInstance().getValue("Button.20.Icon"));    
        jComboBoxButtonIcon20.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxButtonIcon20ItemStateChanged(evt);
            }
        });

        jTextFieldButtonExecute20.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldButtonExecute20.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonExecute20.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonExecute20KeyReleased(evt);
            }
        });

        jLabelButtonToolTip20.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelButtonToolTip20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonToolTip20.setText("Button 20:");

        jTextFieldButtonToolTip20.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonToolTip20.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonToolTip20KeyReleased(evt);
            }
        });

        jLabelButtonIcon20.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonIcon20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonIcon20.setText("Icon:");

        jButton40.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButton40.setText("Restart");
        jButton40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton40ActionPerformed(evt);
            }
        });

        jLabelButtonExecute21.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonExecute21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonExecute21.setText("Execute:");

        jComboBoxButtonIcon21.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jComboBoxButtonIcon21.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxButtonIcon21.setModel(new DefaultComboBoxModel(arrIconList));
        jComboBoxButtonIcon21.setSelectedItem(PropertyHandler.getInstance().getValue("Button.21.Icon"));    
        jComboBoxButtonIcon21.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxButtonIcon21ItemStateChanged(evt);
            }
        });

        jTextFieldButtonExecute21.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldButtonExecute21.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonExecute21.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonExecute21KeyReleased(evt);
            }
        });

        jLabelButtonToolTip21.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelButtonToolTip21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonToolTip21.setText("Button 21:");

        jTextFieldButtonToolTip21.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonToolTip21.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonToolTip21KeyReleased(evt);
            }
        });

        jLabelButtonIcon21.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonIcon21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonIcon21.setText("Icon:");

        jLabelButtonExecute22.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonExecute22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonExecute22.setText("Execute:");

        jComboBoxButtonIcon22.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jComboBoxButtonIcon22.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxButtonIcon22.setModel(new DefaultComboBoxModel(arrIconList));
        jComboBoxButtonIcon22.setSelectedItem(PropertyHandler.getInstance().getValue("Button.22.Icon"));    
        jComboBoxButtonIcon22.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxButtonIcon22ItemStateChanged(evt);
            }
        });

        jTextFieldButtonExecute22.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldButtonExecute22.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonExecute22.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonExecute22KeyReleased(evt);
            }
        });

        jLabelButtonToolTip22.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelButtonToolTip22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonToolTip22.setText("Button 22:");

        jTextFieldButtonToolTip22.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonToolTip22.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonToolTip22KeyReleased(evt);
            }
        });

        jLabelButtonIcon22.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonIcon22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonIcon22.setText("Icon:");

        jLabelButtonExecute23.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonExecute23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonExecute23.setText("Execute:");

        jComboBoxButtonIcon23.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jComboBoxButtonIcon23.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxButtonIcon23.setModel(new DefaultComboBoxModel(arrIconList));
        jComboBoxButtonIcon23.setSelectedItem(PropertyHandler.getInstance().getValue("Button.23.Icon"));    
        jComboBoxButtonIcon23.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxButtonIcon23ItemStateChanged(evt);
            }
        });

        jTextFieldButtonExecute23.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldButtonExecute23.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonExecute23.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonExecute23KeyReleased(evt);
            }
        });

        jLabelButtonToolTip23.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelButtonToolTip23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonToolTip23.setText("Button 23:");

        jTextFieldButtonToolTip23.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonToolTip23.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonToolTip23KeyReleased(evt);
            }
        });

        jLabelButtonIcon23.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonIcon23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonIcon23.setText("Icon:");

        jLabelButtonExecute24.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonExecute24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonExecute24.setText("Execute:");

        jComboBoxButtonIcon24.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jComboBoxButtonIcon24.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxButtonIcon24.setModel(new DefaultComboBoxModel(arrIconList));
        jComboBoxButtonIcon24.setSelectedItem(PropertyHandler.getInstance().getValue("Button.24.Icon"));    
        jComboBoxButtonIcon24.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxButtonIcon24ItemStateChanged(evt);
            }
        });

        jTextFieldButtonExecute24.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldButtonExecute24.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonExecute24.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonExecute24KeyReleased(evt);
            }
        });

        jLabelButtonToolTip24.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelButtonToolTip24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonToolTip24.setText("Button 24:");

        jTextFieldButtonToolTip24.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldButtonToolTip24.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldButtonToolTip24KeyReleased(evt);
            }
        });

        jLabelButtonIcon24.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelButtonIcon24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelButtonIcon24.setText("Icon:");

        jLabel1.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N
        jLabel1.setText("Note: These settings will be overwritten by local policy");

        javax.swing.GroupLayout jPanelSettingsButtonsLayout = new javax.swing.GroupLayout(jPanelSettingsButtons);
        jPanelSettingsButtons.setLayout(jPanelSettingsButtonsLayout);
        jPanelSettingsButtonsLayout.setHorizontalGroup(
            jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton40))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addComponent(jLabelButtonToolTip2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldButtonToolTip2))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelButtonExecute2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelButtonIcon2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxButtonIcon2, 0, 473, Short.MAX_VALUE)
                            .addComponent(jTextFieldButtonExecute2)))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addComponent(jLabelButtonToolTip3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldButtonToolTip3))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelButtonExecute3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelButtonIcon3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxButtonIcon3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldButtonExecute3)))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addComponent(jLabelButtonToolTip4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldButtonToolTip4))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelButtonExecute4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelButtonIcon4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxButtonIcon4, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldButtonExecute4)))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addComponent(jLabelButtonToolTip5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldButtonToolTip5))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelButtonExecute5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelButtonIcon5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxButtonIcon5, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldButtonExecute5)))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addComponent(jLabelButtonToolTip6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldButtonToolTip6))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelButtonExecute6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelButtonIcon6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxButtonIcon6, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldButtonExecute6)))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addComponent(jLabelButtonToolTip7, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldButtonToolTip7))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelButtonExecute7, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelButtonIcon7, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxButtonIcon7, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldButtonExecute7)))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addComponent(jLabelButtonToolTip8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldButtonToolTip8))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelButtonExecute8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelButtonIcon8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxButtonIcon8, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldButtonExecute8)))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addComponent(jLabelButtonToolTip9, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldButtonToolTip9))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelButtonExecute9, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelButtonIcon9, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxButtonIcon9, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldButtonExecute9)))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addComponent(jLabelButtonToolTip10, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldButtonToolTip10))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelButtonExecute10, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelButtonIcon10, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxButtonIcon10, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldButtonExecute10)))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addComponent(jLabelButtonToolTip11, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldButtonToolTip11))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelButtonExecute11, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelButtonIcon11, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxButtonIcon11, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldButtonExecute11)))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addComponent(jLabelButtonToolTip12, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldButtonToolTip12))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelButtonExecute12, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelButtonIcon12, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxButtonIcon12, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldButtonExecute12)))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addComponent(jLabelButtonToolTip13, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldButtonToolTip13))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelButtonExecute13, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelButtonIcon13, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxButtonIcon13, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldButtonExecute13)))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addComponent(jLabelButtonToolTip14, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldButtonToolTip14))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelButtonExecute14, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelButtonIcon14, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxButtonIcon14, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldButtonExecute14)))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addComponent(jLabelButtonToolTip15, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldButtonToolTip15))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelButtonExecute15, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelButtonIcon15, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxButtonIcon15, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldButtonExecute15)))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addComponent(jLabelButtonToolTip16, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldButtonToolTip16))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelButtonExecute16, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelButtonIcon16, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxButtonIcon16, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldButtonExecute16)))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addComponent(jLabelButtonToolTip17, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldButtonToolTip17))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelButtonExecute17, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelButtonIcon17, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxButtonIcon17, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldButtonExecute17)))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addComponent(jLabelButtonToolTip18, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldButtonToolTip18))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelButtonExecute18, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelButtonIcon18, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxButtonIcon18, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldButtonExecute18)))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addComponent(jLabelButtonToolTip19, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldButtonToolTip19))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelButtonExecute19, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelButtonIcon19, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxButtonIcon19, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldButtonExecute19)))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addComponent(jLabelButtonToolTip20, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldButtonToolTip20))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelButtonExecute20, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelButtonIcon20, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxButtonIcon20, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldButtonExecute20)))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabelButtonToolTip1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelButtonExecute1, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                            .addComponent(jLabelButtonIcon1, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxButtonIcon1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldButtonExecute1)
                            .addComponent(jTextFieldButtonToolTip1)))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addComponent(jLabelButtonToolTip21, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldButtonToolTip21))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelButtonExecute21, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelButtonIcon21, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxButtonIcon21, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldButtonExecute21)))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addComponent(jLabelButtonToolTip22, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldButtonToolTip22))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelButtonExecute22, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelButtonIcon22, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxButtonIcon22, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldButtonExecute22)))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addComponent(jLabelButtonToolTip23, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldButtonToolTip23))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelButtonExecute23, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelButtonIcon23, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxButtonIcon23, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldButtonExecute23)))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addComponent(jLabelButtonToolTip24, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldButtonToolTip24))
                    .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelButtonExecute24, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelButtonIcon24, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxButtonIcon24, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldButtonExecute24))))
                .addContainerGap())
        );
        jPanelSettingsButtonsLayout.setVerticalGroup(
            jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSettingsButtonsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton40, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton33)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldButtonToolTip1)
                    .addComponent(jLabelButtonToolTip1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBoxButtonIcon1)
                    .addComponent(jLabelButtonIcon1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelButtonExecute1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldButtonExecute1))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldButtonToolTip2)
                    .addComponent(jLabelButtonToolTip2, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBoxButtonIcon2)
                    .addComponent(jLabelButtonIcon2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelButtonExecute2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldButtonExecute2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldButtonToolTip3)
                    .addComponent(jLabelButtonToolTip3, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBoxButtonIcon3)
                    .addComponent(jLabelButtonIcon3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelButtonExecute3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldButtonExecute3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldButtonToolTip4)
                    .addComponent(jLabelButtonToolTip4, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBoxButtonIcon4)
                    .addComponent(jLabelButtonIcon4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelButtonExecute4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldButtonExecute4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldButtonToolTip5)
                    .addComponent(jLabelButtonToolTip5, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBoxButtonIcon5)
                    .addComponent(jLabelButtonIcon5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelButtonExecute5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldButtonExecute5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldButtonToolTip6)
                    .addComponent(jLabelButtonToolTip6, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBoxButtonIcon6)
                    .addComponent(jLabelButtonIcon6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelButtonExecute6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldButtonExecute6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldButtonToolTip7)
                    .addComponent(jLabelButtonToolTip7, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBoxButtonIcon7)
                    .addComponent(jLabelButtonIcon7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelButtonExecute7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldButtonExecute7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldButtonToolTip8)
                    .addComponent(jLabelButtonToolTip8, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBoxButtonIcon8)
                    .addComponent(jLabelButtonIcon8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelButtonExecute8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldButtonExecute8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldButtonToolTip9)
                    .addComponent(jLabelButtonToolTip9, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBoxButtonIcon9)
                    .addComponent(jLabelButtonIcon9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelButtonExecute9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldButtonExecute9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldButtonToolTip10)
                    .addComponent(jLabelButtonToolTip10, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBoxButtonIcon10)
                    .addComponent(jLabelButtonIcon10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelButtonExecute10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldButtonExecute10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldButtonToolTip11)
                    .addComponent(jLabelButtonToolTip11, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBoxButtonIcon11)
                    .addComponent(jLabelButtonIcon11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelButtonExecute11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldButtonExecute11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldButtonToolTip12)
                    .addComponent(jLabelButtonToolTip12, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBoxButtonIcon12)
                    .addComponent(jLabelButtonIcon12, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelButtonExecute12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldButtonExecute12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldButtonToolTip13)
                    .addComponent(jLabelButtonToolTip13, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBoxButtonIcon13)
                    .addComponent(jLabelButtonIcon13, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelButtonExecute13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldButtonExecute13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldButtonToolTip14)
                    .addComponent(jLabelButtonToolTip14, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBoxButtonIcon14)
                    .addComponent(jLabelButtonIcon14, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelButtonExecute14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldButtonExecute14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldButtonToolTip15)
                    .addComponent(jLabelButtonToolTip15, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBoxButtonIcon15)
                    .addComponent(jLabelButtonIcon15, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelButtonExecute15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldButtonExecute15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldButtonToolTip16)
                    .addComponent(jLabelButtonToolTip16, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBoxButtonIcon16)
                    .addComponent(jLabelButtonIcon16, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelButtonExecute16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldButtonExecute16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldButtonToolTip17)
                    .addComponent(jLabelButtonToolTip17, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBoxButtonIcon17)
                    .addComponent(jLabelButtonIcon17, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelButtonExecute17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldButtonExecute17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldButtonToolTip18)
                    .addComponent(jLabelButtonToolTip18, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBoxButtonIcon18)
                    .addComponent(jLabelButtonIcon18, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelButtonExecute18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldButtonExecute18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldButtonToolTip19)
                    .addComponent(jLabelButtonToolTip19, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBoxButtonIcon19)
                    .addComponent(jLabelButtonIcon19, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelButtonExecute19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldButtonExecute19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldButtonToolTip20)
                    .addComponent(jLabelButtonToolTip20, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBoxButtonIcon20)
                    .addComponent(jLabelButtonIcon20, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelButtonExecute20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldButtonExecute20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldButtonToolTip21)
                    .addComponent(jLabelButtonToolTip21, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBoxButtonIcon21)
                    .addComponent(jLabelButtonIcon21, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelButtonExecute21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldButtonExecute21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldButtonToolTip22)
                    .addComponent(jLabelButtonToolTip22, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBoxButtonIcon22)
                    .addComponent(jLabelButtonIcon22, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelButtonExecute22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldButtonExecute22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldButtonToolTip23)
                    .addComponent(jLabelButtonToolTip23, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBoxButtonIcon23)
                    .addComponent(jLabelButtonIcon23, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelButtonExecute23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldButtonExecute23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldButtonToolTip24)
                    .addComponent(jLabelButtonToolTip24, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBoxButtonIcon24)
                    .addComponent(jLabelButtonIcon24, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelButtonExecute24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldButtonExecute24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(52, Short.MAX_VALUE))
        );

        jScrollPaneSettingsButtons.setViewportView(jPanelSettingsButtons);

        jTabbedPaneSettings.addTab("Buttons", jScrollPaneSettingsButtons);

        jScrollPaneSettingsLinks.setPreferredSize(new java.awt.Dimension(557, 2000));

        jPanelSettingsLinks.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanelSettingsLinks.setPreferredSize(new java.awt.Dimension(550, 2430));

        jLabelLinkText1.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelLinkText1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkText1.setText("Link 1:");

        jTextFieldLinkText1.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkText1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldLinkText1ActionPerformed(evt);
            }
        });
        jTextFieldLinkText1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkText1KeyReleased(evt);
            }
        });

        jLabelLinkExecute1.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelLinkExecute1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkExecute1.setText("Execute:");

        jTextFieldLinkExecute1.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldLinkExecute1.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkExecute1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkExecute1KeyReleased(evt);
            }
        });

        jButton44.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButton44.setText("Restart");
        jButton44.setMargin(null);
        jButton44.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton44ActionPerformed(evt);
            }
        });

        jTextFieldLinkText2.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkText2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkText2KeyReleased(evt);
            }
        });

        jLabelLinkText2.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelLinkText2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkText2.setText("Link 2:");

        jTextFieldLinkExecute2.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldLinkExecute2.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkExecute2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkExecute2KeyReleased(evt);
            }
        });

        jLabelLinkExecute2.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelLinkExecute2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkExecute2.setText("Execute:");

        jLabelLinkExecute3.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelLinkExecute3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkExecute3.setText("Execute:");

        jTextFieldLinkExecute3.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldLinkExecute3.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkExecute3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkExecute3KeyReleased(evt);
            }
        });

        jLabelLinkText3.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelLinkText3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkText3.setText("Link 3:");

        jTextFieldLinkText3.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkText3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkText3KeyReleased(evt);
            }
        });

        jLabelLinkExecute4.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelLinkExecute4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkExecute4.setText("Execute:");

        jTextFieldLinkExecute4.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldLinkExecute4.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkExecute4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkExecute4KeyReleased(evt);
            }
        });

        jLabelLinkText4.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelLinkText4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkText4.setText("Link 4:");

        jTextFieldLinkText4.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkText4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkText4KeyReleased(evt);
            }
        });

        jLabelLinkExecute5.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelLinkExecute5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkExecute5.setText("Execute:");

        jTextFieldLinkExecute5.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldLinkExecute5.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkExecute5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkExecute5KeyReleased(evt);
            }
        });

        jLabelLinkText5.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelLinkText5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkText5.setText("Link 5:");

        jTextFieldLinkText5.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkText5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkText5KeyReleased(evt);
            }
        });

        jLabelLinkExecute6.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelLinkExecute6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkExecute6.setText("Execute:");

        jTextFieldLinkExecute6.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldLinkExecute6.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkExecute6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkExecute6KeyReleased(evt);
            }
        });

        jLabelLinkText6.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelLinkText6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkText6.setText("Link 6:");

        jTextFieldLinkText6.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkText6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkText6KeyReleased(evt);
            }
        });

        jLabelLinkExecute7.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelLinkExecute7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkExecute7.setText("Execute:");

        jTextFieldLinkExecute7.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldLinkExecute7.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkExecute7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkExecute7KeyReleased(evt);
            }
        });

        jLabelLinkText7.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelLinkText7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkText7.setText("Link 7:");

        jTextFieldLinkText7.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkText7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkText7KeyReleased(evt);
            }
        });

        jLabelLinkExecute8.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelLinkExecute8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkExecute8.setText("Execute:");

        jTextFieldLinkExecute8.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldLinkExecute8.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkExecute8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkExecute8KeyReleased(evt);
            }
        });

        jLabelLinkText8.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelLinkText8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkText8.setText("Link 8:");

        jTextFieldLinkText8.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkText8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkText8KeyReleased(evt);
            }
        });

        jLabelLinkExecute9.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelLinkExecute9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkExecute9.setText("Execute:");

        jTextFieldLinkExecute9.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldLinkExecute9.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkExecute9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkExecute9KeyReleased(evt);
            }
        });

        jLabelLinkText9.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelLinkText9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkText9.setText("Link 9:");

        jTextFieldLinkText9.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkText9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkText9KeyReleased(evt);
            }
        });

        jLabelLinkExecute10.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelLinkExecute10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkExecute10.setText("Execute:");

        jTextFieldLinkExecute10.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldLinkExecute10.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkExecute10.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkExecute10KeyReleased(evt);
            }
        });

        jLabelLinkText10.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelLinkText10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkText10.setText("Link 10:");

        jTextFieldLinkText10.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkText10.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkText10KeyReleased(evt);
            }
        });

        jLabelLinkExecute11.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelLinkExecute11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkExecute11.setText("Execute:");

        jTextFieldLinkExecute11.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldLinkExecute11.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkExecute11.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkExecute11KeyReleased(evt);
            }
        });

        jLabelLinkText11.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelLinkText11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkText11.setText("Link 11:");

        jTextFieldLinkText11.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkText11.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkText11KeyReleased(evt);
            }
        });

        jLabelLinkExecute12.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelLinkExecute12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkExecute12.setText("Execute:");

        jTextFieldLinkExecute12.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldLinkExecute12.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkExecute12.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkExecute12KeyReleased(evt);
            }
        });

        jLabelLinkText12.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelLinkText12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkText12.setText("Link 12:");

        jTextFieldLinkText12.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkText12.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkText12KeyReleased(evt);
            }
        });

        jLabelLinkExecute13.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelLinkExecute13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkExecute13.setText("Execute:");

        jTextFieldLinkExecute13.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldLinkExecute13.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkExecute13.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkExecute13KeyReleased(evt);
            }
        });

        jLabelLinkText13.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelLinkText13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkText13.setText("Link 13:");

        jTextFieldLinkText13.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkText13.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkText13KeyReleased(evt);
            }
        });

        jLabelLinkExecute14.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelLinkExecute14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkExecute14.setText("Execute:");

        jTextFieldLinkExecute14.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldLinkExecute14.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkExecute14.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkExecute14KeyReleased(evt);
            }
        });

        jLabelLinkText14.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelLinkText14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkText14.setText("Link 14:");

        jTextFieldLinkText14.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkText14.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkText14KeyReleased(evt);
            }
        });

        jLabelLinkExecute15.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelLinkExecute15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkExecute15.setText("Execute:");

        jTextFieldLinkExecute15.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldLinkExecute15.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkExecute15.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkExecute15KeyReleased(evt);
            }
        });

        jLabelLinkText15.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelLinkText15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkText15.setText("Link 15:");

        jTextFieldLinkText15.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkText15.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkText15KeyReleased(evt);
            }
        });

        jLabelLinkExecute16.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelLinkExecute16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkExecute16.setText("Execute:");

        jTextFieldLinkExecute16.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldLinkExecute16.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkExecute16.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkExecute16KeyReleased(evt);
            }
        });

        jLabelLinkText16.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelLinkText16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkText16.setText("Link 16:");

        jTextFieldLinkText16.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkText16.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkText16KeyReleased(evt);
            }
        });

        jLabelLinkExecute17.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelLinkExecute17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkExecute17.setText("Execute:");

        jTextFieldLinkExecute17.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldLinkExecute17.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkExecute17.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkExecute17KeyReleased(evt);
            }
        });

        jLabelLinkText17.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelLinkText17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkText17.setText("Link 17:");

        jTextFieldLinkText17.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkText17.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkText17KeyReleased(evt);
            }
        });

        jLabelLinkExecute18.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelLinkExecute18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkExecute18.setText("Execute:");

        jTextFieldLinkExecute18.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldLinkExecute18.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkExecute18.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkExecute18KeyReleased(evt);
            }
        });

        jLabelLinkText18.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelLinkText18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkText18.setText("Link 18:");

        jTextFieldLinkText18.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkText18.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkText18KeyReleased(evt);
            }
        });

        jLabelLinkExecute19.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelLinkExecute19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkExecute19.setText("Execute:");

        jTextFieldLinkExecute19.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldLinkExecute19.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkExecute19.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkExecute19KeyReleased(evt);
            }
        });

        jLabelLinkText19.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelLinkText19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkText19.setText("Link 19:");

        jTextFieldLinkText19.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkText19.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkText19KeyReleased(evt);
            }
        });

        jLabelLinkExecute20.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelLinkExecute20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkExecute20.setText("Execute:");

        jTextFieldLinkExecute20.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldLinkExecute20.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkExecute20.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkExecute20KeyReleased(evt);
            }
        });

        jLabelLinkText20.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelLinkText20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkText20.setText("Link 20:");

        jTextFieldLinkText20.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkText20.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkText20KeyReleased(evt);
            }
        });

        jLabelLinkExecute21.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelLinkExecute21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkExecute21.setText("Execute:");

        jTextFieldLinkExecute21.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldLinkExecute21.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkExecute21.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkExecute21KeyReleased(evt);
            }
        });

        jLabelLinkText21.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelLinkText21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkText21.setText("Link 21:");

        jTextFieldLinkText21.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkText21.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkText21KeyReleased(evt);
            }
        });

        jLabelLinkExecute22.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelLinkExecute22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkExecute22.setText("Execute:");

        jTextFieldLinkExecute22.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldLinkExecute22.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkExecute22.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkExecute22KeyReleased(evt);
            }
        });

        jLabelLinkText22.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelLinkText22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkText22.setText("Link 22:");

        jTextFieldLinkText22.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkText22.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkText22KeyReleased(evt);
            }
        });

        jLabelLinkExecute23.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelLinkExecute23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkExecute23.setText("Execute:");

        jTextFieldLinkExecute23.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldLinkExecute23.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkExecute23.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkExecute23KeyReleased(evt);
            }
        });

        jLabelLinkText23.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelLinkText23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkText23.setText("Link 23:");

        jTextFieldLinkText23.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkText23.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkText23KeyReleased(evt);
            }
        });

        jLabelLinkExecute24.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelLinkExecute24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkExecute24.setText("Execute:");

        jTextFieldLinkExecute24.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldLinkExecute24.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkExecute24.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkExecute24KeyReleased(evt);
            }
        });

        jLabelLinkText24.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelLinkText24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkText24.setText("Link 24:");

        jTextFieldLinkText24.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkText24.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkText24KeyReleased(evt);
            }
        });

        jLabelLinkExecute25.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelLinkExecute25.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkExecute25.setText("Execute:");

        jTextFieldLinkExecute25.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldLinkExecute25.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkExecute25.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkExecute25KeyReleased(evt);
            }
        });

        jLabelLinkText25.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelLinkText25.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkText25.setText("Link 25:");

        jTextFieldLinkText25.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkText25.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkText25KeyReleased(evt);
            }
        });

        jLabelLinkExecute26.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelLinkExecute26.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkExecute26.setText("Execute:");

        jTextFieldLinkExecute26.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldLinkExecute26.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkExecute26.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkExecute26KeyReleased(evt);
            }
        });

        jLabelLinkText26.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelLinkText26.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkText26.setText("Link 26:");

        jTextFieldLinkText26.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkText26.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkText26KeyReleased(evt);
            }
        });

        jLabelLinkExecute27.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelLinkExecute27.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkExecute27.setText("Execute:");

        jTextFieldLinkExecute27.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldLinkExecute27.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkExecute27.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkExecute27KeyReleased(evt);
            }
        });

        jLabelLinkText27.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelLinkText27.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkText27.setText("Link 27:");

        jTextFieldLinkText27.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkText27.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkText27KeyReleased(evt);
            }
        });

        jLabelLinkExecute28.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelLinkExecute28.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkExecute28.setText("Execute:");

        jTextFieldLinkExecute28.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldLinkExecute28.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkExecute28.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkExecute28KeyReleased(evt);
            }
        });

        jLabelLinkText28.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelLinkText28.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkText28.setText("Link 28:");

        jTextFieldLinkText28.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkText28.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkText28KeyReleased(evt);
            }
        });

        jLabelLinkExecute29.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelLinkExecute29.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkExecute29.setText("Execute:");

        jTextFieldLinkExecute29.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldLinkExecute29.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkExecute29.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkExecute29KeyReleased(evt);
            }
        });

        jLabelLinkText29.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelLinkText29.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkText29.setText("Link 29:");

        jTextFieldLinkText29.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkText29.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkText29KeyReleased(evt);
            }
        });

        jLabelLinkExecute30.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelLinkExecute30.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkExecute30.setText("Execute:");

        jTextFieldLinkExecute30.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldLinkExecute30.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkExecute30.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkExecute30KeyReleased(evt);
            }
        });

        jLabelLinkText30.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelLinkText30.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkText30.setText("Link 30:");

        jTextFieldLinkText30.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkText30.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkText30KeyReleased(evt);
            }
        });

        jLabelLinkExecute31.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelLinkExecute31.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkExecute31.setText("Execute:");

        jTextFieldLinkExecute31.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldLinkExecute31.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkExecute31.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkExecute31KeyReleased(evt);
            }
        });

        jLabelLinkText31.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelLinkText31.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkText31.setText("Link 31:");

        jTextFieldLinkText31.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkText31.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkText31KeyReleased(evt);
            }
        });

        jLabelLinkExecute32.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelLinkExecute32.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkExecute32.setText("Execute:");

        jTextFieldLinkExecute32.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldLinkExecute32.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkExecute32.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkExecute32KeyReleased(evt);
            }
        });

        jLabelLinkText32.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelLinkText32.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkText32.setText("Link 32:");

        jTextFieldLinkText32.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkText32.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkText32KeyReleased(evt);
            }
        });

        jLabelLinkExecute33.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelLinkExecute33.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkExecute33.setText("Execute:");

        jTextFieldLinkExecute33.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldLinkExecute33.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkExecute33.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkExecute33KeyReleased(evt);
            }
        });

        jLabelLinkText33.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelLinkText33.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkText33.setText("Link 33:");

        jTextFieldLinkText33.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkText33.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkText33KeyReleased(evt);
            }
        });

        jLabelLinkExecute34.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelLinkExecute34.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkExecute34.setText("Execute:");

        jTextFieldLinkExecute34.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldLinkExecute34.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkExecute34.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkExecute34KeyReleased(evt);
            }
        });

        jLabelLinkText34.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelLinkText34.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkText34.setText("Link 34:");

        jTextFieldLinkText34.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkText34.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkText34KeyReleased(evt);
            }
        });

        jLabelLinkExecute35.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelLinkExecute35.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkExecute35.setText("Execute:");

        jTextFieldLinkExecute35.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldLinkExecute35.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkExecute35.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkExecute35KeyReleased(evt);
            }
        });

        jLabelLinkText35.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelLinkText35.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkText35.setText("Link 35:");

        jTextFieldLinkText35.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkText35.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkText35KeyReleased(evt);
            }
        });

        jLabelLinkExecute36.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabelLinkExecute36.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkExecute36.setText("Execute:");

        jTextFieldLinkExecute36.setBackground(new java.awt.Color(219, 219, 255));
        jTextFieldLinkExecute36.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkExecute36.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkExecute36KeyReleased(evt);
            }
        });

        jLabelLinkText36.setFont(new java.awt.Font("Arial Unicode MS", 1, 11)); // NOI18N
        jLabelLinkText36.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelLinkText36.setText("Link 36:");

        jTextFieldLinkText36.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTextFieldLinkText36.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldLinkText36KeyReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N
        jLabel2.setText("Note: These settings will be overwritten by local policy");

        javax.swing.GroupLayout jPanelSettingsLinksLayout = new javax.swing.GroupLayout(jPanelSettingsLinks);
        jPanelSettingsLinks.setLayout(jPanelSettingsLinksLayout);
        jPanelSettingsLinksLayout.setHorizontalGroup(
            jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkText1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkText1, javax.swing.GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkText2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkText2))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkText3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkText3))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkText4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkText4))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkExecute4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkExecute4))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkText5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkText5))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkExecute5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkExecute5))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkText6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkText6))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkExecute6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkExecute6))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkText7, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkText7))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkExecute7, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkExecute7))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkText8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkText8))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkExecute8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkExecute8))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkText9, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkText9))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkExecute9, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkExecute9))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkText10, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkText10))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkExecute10, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkExecute10))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkText11, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkText11))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkExecute11, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkExecute11))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkText12, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkText12))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkExecute12, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkExecute12))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkText13, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkText13))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkExecute13, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkExecute13))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkText14, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkText14))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkExecute14, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkExecute14))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkText15, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkText15))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkExecute15, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkExecute15))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkText16, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkText16))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkExecute16, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkExecute16))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkText17, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkText17))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkExecute17, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkExecute17))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkText18, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkText18))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkExecute18, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkExecute18))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkText19, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkText19))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkExecute19, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkExecute19))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkText20, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkText20))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkExecute20, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkExecute20))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkText21, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkText21))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkExecute21, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkExecute21))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkText22, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkText22))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkExecute22, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkExecute22))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkText23, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkText23))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkExecute23, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkExecute23))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkText24, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkText24))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkExecute24, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkExecute24))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkText25, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkText25))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkExecute25, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkExecute25))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkText26, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkText26))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkExecute26, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkExecute26))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkText27, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkText27))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkExecute27, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkExecute27))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkText28, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkText28))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkExecute28, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkExecute28))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkText29, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkText29))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkExecute29, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkExecute29))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkText30, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkText30))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkExecute30, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkExecute30))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkText31, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkText31))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkExecute31, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkExecute31))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkText32, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkText32))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkExecute32, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkExecute32))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkText33, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkText33))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkExecute33, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkExecute33))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkText34, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkText34))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkExecute34, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkExecute34))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkText35, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkText35))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkExecute35, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkExecute35))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkText36, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkText36))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkExecute36, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkExecute36))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkExecute1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkExecute1))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkExecute3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkExecute3))
                    .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabelLinkExecute2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLinkExecute2))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelSettingsLinksLayout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton44)))
                .addContainerGap())
        );
        jPanelSettingsLinksLayout.setVerticalGroup(
            jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSettingsLinksLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton44, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLinkText1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkText1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldLinkExecute1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkExecute1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLinkText2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkText2, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldLinkExecute2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkExecute2, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLinkText3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkText3, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldLinkExecute3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkExecute3, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLinkText4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkText4, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldLinkExecute4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkExecute4, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLinkText5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkText5, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldLinkExecute5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkExecute5, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLinkText6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkText6, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldLinkExecute6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkExecute6, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLinkText7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkText7, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldLinkExecute7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkExecute7, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLinkText8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkText8, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldLinkExecute8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkExecute8, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLinkText9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkText9, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldLinkExecute9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkExecute9, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLinkText10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkText10, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldLinkExecute10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkExecute10, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLinkText11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkText11, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldLinkExecute11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkExecute11, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLinkText12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkText12, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldLinkExecute12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkExecute12, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLinkText13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkText13, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldLinkExecute13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkExecute13, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLinkText14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkText14, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldLinkExecute14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkExecute14, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLinkText15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkText15, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldLinkExecute15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkExecute15, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLinkText16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkText16, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldLinkExecute16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkExecute16, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLinkText17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkText17, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldLinkExecute17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkExecute17, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLinkText18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkText18, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldLinkExecute18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkExecute18, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLinkText19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkText19, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldLinkExecute19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkExecute19, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLinkText20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkText20, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldLinkExecute20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkExecute20, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLinkText21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkText21, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldLinkExecute21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkExecute21, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLinkText22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkText22, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldLinkExecute22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkExecute22, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLinkText23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkText23, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldLinkExecute23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkExecute23, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLinkText24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkText24, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldLinkExecute24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkExecute24, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLinkText25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkText25, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldLinkExecute25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkExecute25, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLinkText26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkText26, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldLinkExecute26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkExecute26, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLinkText27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkText27, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldLinkExecute27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkExecute27, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLinkText28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkText28, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldLinkExecute28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkExecute28, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLinkText29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkText29, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldLinkExecute29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkExecute29, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLinkText30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkText30, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldLinkExecute30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkExecute30, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLinkText31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkText31, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldLinkExecute31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkExecute31, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLinkText32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkText32, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldLinkExecute32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkExecute32, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLinkText33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkText33, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldLinkExecute33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkExecute33, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLinkText34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkText34, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldLinkExecute34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkExecute34, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLinkText35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkText35, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldLinkExecute35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkExecute35, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLinkText36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkText36, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsLinksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldLinkExecute36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelLinkExecute36, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        jScrollPaneSettingsLinks.setViewportView(jPanelSettingsLinks);

        jTabbedPaneSettings.addTab("Links", jScrollPaneSettingsLinks);
        jTabbedPaneSettings.addTab("Scripts", jScrollPaneSettingsScripts);
        jTabbedPaneSettings.addTab("References", jScrollPaneSettingsReferences);

        jTabbedMain.addTab("Settings", jTabbedPaneSettings);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedMain.getAccessibleContext().setAccessibleName("");

        getAccessibleContext().setAccessibleName("LaunchPad Pre-Alpha");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        //- Icon
        URL iconURL = getClass().getResource("/icon/icon.png");
        ImageIcon img = new ImageIcon(iconURL);
        this.setIconImage(img.getImage());     
    }//GEN-LAST:event_formWindowOpened

    private void jButtonReportIssueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReportIssueActionPerformed
        // TODO add your handling code here:
        Desktop desktop;
        if (Desktop.isDesktopSupported()
            && (desktop = Desktop.getDesktop()).isSupported(Desktop.Action.MAIL)) {
            URI mailto = null;
            try {
                mailto = new URI("mailto:" + PropertyHandler.getInstance().getValue("Setting.EmailContactIssues") + "?subject=LaunchPad%20Issue");
            } catch (URISyntaxException ex) {
                Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                desktop.mail(mailto);
            } catch (IOException ex) {
                Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            // TODO fallback to some Runtime.exec(..) voodoo?
            throw new RuntimeException("desktop doesn't support mailto");
        }
    }//GEN-LAST:event_jButtonReportIssueActionPerformed

    private void jButtonScriptUpdateLaunchPadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptUpdateLaunchPadActionPerformed
        String StrFileUpdateScript = PropertyHandler.getInstance().getValue("File.Update.Script");
        // switched from using just the file name to specifying the entire command in the properties file
        // if(new File(StrFileUpdateScript).isFile()) { 
        if(!StrFileUpdateScript.isEmpty()) { 
            // String myValue = "cmd.exe /c start powershell.exe -ExecutionPolicy Bypass -File \"" + PropertyHandler.getInstance().getValue("File.Update.Script") + "\"";
            String myValue = PropertyHandler.getInstance().getValue("File.Update.Script");
            System.out.println(myValue);
            try {
                Runtime.getRuntime().exec(myValue);
            }
            catch (IOException e) {
                System.out.println("Something is wrong!");
                JOptionPane.showMessageDialog(null, "Something is wrong!");
            }
                System.exit(0);
            }
        else {
            String StrMessage = "Update script is blank in properties file.";
            String StrTitle = "Oh no!";
            JOptionPane.showMessageDialog(null, StrMessage,StrTitle,JOptionPane.WARNING_MESSAGE);
            System.out.println(StrMessage);
        }
    }//GEN-LAST:event_jButtonScriptUpdateLaunchPadActionPerformed

    private void jTextField2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyTyped
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            if ("ipranger32".equals(jTextField2.getText()) ||
                "ipranger".equals(jTextField2.getText())) {
                String inputPdf = "apps/ipranger32.jar";
                InputStream manualAsStream = getClass().getClassLoader().getResourceAsStream(inputPdf);

                Path tempOutput = null;
                try {
                    tempOutput = Files.createTempFile("TempFile", ".jar");
                } catch (IOException ex) {
                    Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
                }
                tempOutput.toFile().deleteOnExit();

                try {
                    Files.copy(manualAsStream, tempOutput, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
                }

                File userManual = new File (tempOutput.toFile().getPath());
                if (userManual.exists())
                {
                    try {
                        Desktop.getDesktop().open(userManual);
                    } catch (IOException ex) {
                        Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                jTextField2.setText("");
            }
            if ("ipranger64".equals(jTextField2.getText())) {
                String inputPdf = "apps/ipranger64.jar";
                InputStream manualAsStream = getClass().getClassLoader().getResourceAsStream(inputPdf);

                Path tempOutput = null;
                try {
                    tempOutput = Files.createTempFile("TempFile", ".jar");
                } catch (IOException ex) {
                    Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
                }
                tempOutput.toFile().deleteOnExit();

                try {
                    Files.copy(manualAsStream, tempOutput, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
                }

                File userManual = new File (tempOutput.toFile().getPath());
                if (userManual.exists())
                {
                    try {
                        Desktop.getDesktop().open(userManual);
                    } catch (IOException ex) {
                        Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                jTextField2.setText("");
            }

            if ("jdiskreport".equals(jTextField2.getText()) ||
                "diskreport".equals(jTextField2.getText()) ||
                "foldersize".equals(jTextField2.getText())) {

                String inputPdf = "apps/jdiskreport-1.4.1.jar";
                InputStream manualAsStream = getClass().getClassLoader().getResourceAsStream(inputPdf);

                Path tempOutput = null;
                try {
                    tempOutput = Files.createTempFile("TempFile", ".jar");
                } catch (IOException ex) {
                    Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
                }
                tempOutput.toFile().deleteOnExit();

                try {
                    Files.copy(manualAsStream, tempOutput, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
                }

                File userManual = new File (tempOutput.toFile().getPath());
                if (userManual.exists())
                {
                    try {
                        Desktop.getDesktop().open(userManual);
                    } catch (IOException ex) {
                        Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                jTextField2.setText("");
            }

            if ("pixelitor".equals(jTextField2.getText()) ||
                "paint".equals(jTextField2.getText())) {

                String inputPdf = "apps/pixelitor_4.2.0.jar";
                InputStream manualAsStream = getClass().getClassLoader().getResourceAsStream(inputPdf);

                Path tempOutput = null;
                try {
                    tempOutput = Files.createTempFile("TempFile", ".jar");
                } catch (IOException ex) {
                    Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
                }
                tempOutput.toFile().deleteOnExit();

                try {
                    Files.copy(manualAsStream, tempOutput, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
                }

                File userManual = new File (tempOutput.toFile().getPath());
                if (userManual.exists())
                {
                    try {
                        Desktop.getDesktop().open(userManual);
                    } catch (IOException ex) {
                        Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                jTextField2.setText("");
            }

            if ("tftp".equals(jTextField2.getText())) {

                String inputPdf = "apps/tftp.jar";
                InputStream manualAsStream = getClass().getClassLoader().getResourceAsStream(inputPdf);

                Path tempOutput = null;
                try {
                    tempOutput = Files.createTempFile("TempFile", ".jar");
                } catch (IOException ex) {
                    Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
                }
                tempOutput.toFile().deleteOnExit();

                try {
                    Files.copy(manualAsStream, tempOutput, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
                }

                File userManual = new File (tempOutput.toFile().getPath());
                if (userManual.exists())
                {
                    try {
                        Desktop.getDesktop().open(userManual);
                    } catch (IOException ex) {
                        Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                jTextField2.setText("");
            }

            if ("ftp".equals(jTextField2.getText())) {

                String inputPdf = "apps/ftp.jar";
                InputStream manualAsStream = getClass().getClassLoader().getResourceAsStream(inputPdf);

                Path tempOutput = null;
                try {
                    tempOutput = Files.createTempFile("TempFile", ".jar");
                } catch (IOException ex) {
                    Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
                }
                tempOutput.toFile().deleteOnExit();

                try {
                    Files.copy(manualAsStream, tempOutput, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
                }

                File userManual = new File (tempOutput.toFile().getPath());
                if (userManual.exists())
                {
                    try {
                        Desktop.getDesktop().open(userManual);
                    } catch (IOException ex) {
                        Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                jTextField2.setText("");
            }

            
            if ("http".equals(jTextField2.getText())) {

                String inputPdf = "apps/http.jar";
                InputStream manualAsStream = getClass().getClassLoader().getResourceAsStream(inputPdf);

                Path tempOutput = null;
                try {
                    tempOutput = Files.createTempFile("TempFile", ".jar");
                } catch (IOException ex) {
                    Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
                }
                tempOutput.toFile().deleteOnExit();

                try {
                    Files.copy(manualAsStream, tempOutput, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
                }

                File userManual = new File (tempOutput.toFile().getPath());
                if (userManual.exists())
                {
                    try {
                        Desktop.getDesktop().open(userManual);
                    } catch (IOException ex) {
                        Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                jTextField2.setText("");
            }
            

        }
    }//GEN-LAST:event_jTextField2KeyTyped

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jButton34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton34ActionPerformed
        DefaultListModel listModel = new DefaultListModel();
        ArrayList arrSessionList = new ArrayList();

        File archivo = new File(strPathPropertiesFile);

        try (FileReader fr = new FileReader(archivo)) {
            BufferedReader buffIn;
            buffIn = new BufferedReader(fr);

            String line;
            while ((line = buffIn.readLine()) != null) {
                arrSessionList.add(line);
                listModel.addElement(line);
                //System.out.println(line);
            }
            JList list = new JList(listModel);

            JScrollPane scroll = new JScrollPane(list);

            scroll.setPreferredSize(new Dimension(800, 500));

            JFrame frame = new JFrame();
            frame.add(scroll);
            frame.setTitle("Properties File");
            //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
        catch (IOException e) {
            System.out.println("SessionList.csv no good");
            JOptionPane.showMessageDialog(null, "SessionList.csv Error!");
        }
    }//GEN-LAST:event_jButton34ActionPerformed

    private void jButton33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton33ActionPerformed
        // TODO add your handling code here:
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ButtonList buttonList = new ButtonList();
            }
        });
    }//GEN-LAST:event_jButton33ActionPerformed

    private void jButton32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton32ActionPerformed
        // TODO add your handling code here:
        openFileUsingDesktop(strSessionListFavorites);
        
//        try {
//            Process p = Runtime.getRuntime().exec
//            (new String [] { "cmd.exe", "/c", "assoc", ".xls"});
//            BufferedReader input =
//            new BufferedReader
//            (new InputStreamReader(p.getInputStream()));
//            String extensionType = input.readLine();
//            input.close();
//            // extract type
//            if (extensionType == null) {
//                System.out.println("no office installed ?");
//                String myValue = "cmd.exe /c start wordpad.exe \"" + strSessionListFavorites + "\"";
//                System.out.println(myValue);
//                try {
//                    Runtime.getRuntime().exec(myValue);
//                }
//                catch (IOException e) {
//                    System.out.println("Something is wrong!");
//                    JOptionPane.showMessageDialog(null, "Something is wrong!");
//                }
//
//                return;
//            }
//            String fileType[] = extensionType.split("=");
//
//            p = Runtime.getRuntime().exec
//            (new String [] { "cmd.exe", "/c", "ftype", fileType[1]});
//            input =
//            new BufferedReader
//            (new InputStreamReader(p.getInputStream()));
//            String fileAssociation = input.readLine();
//            // extract path
//            String officePath = fileAssociation.split("=")[1];
//            Pattern patternInQuotes = Pattern.compile("\"([^\"]*)\"");
//            Matcher matchInQuotes = patternInQuotes.matcher(officePath);
//            while (matchInQuotes.find()) {
//                officePath = matchInQuotes.group(1);
//                System.out.println(matchInQuotes.group(1));
//            }
//            String myValue = "\"" + officePath + " \" \"" + strSessionListFavorites + "\"";
//            System.out.println(myValue);
//            try {
//                Runtime.getRuntime().exec(myValue);
//            }
//            catch (IOException e) {
//                System.out.println("Something is wrong!");
//                JOptionPane.showMessageDialog(null, "Something is wrong!");
//            }
//        }
//        catch (IOException err) {
//        }
    }//GEN-LAST:event_jButton32ActionPerformed

    private void jButton31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton31ActionPerformed
        // TODO add your handling code here:
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog (null, "Are you sure you want to reset your SecureCRT settings?","Warning", dialogButton);
        if(dialogResult == JOptionPane.YES_OPTION){
            String myValue = "cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"Remove-Item $env:USERPROFILE\\AppData\\Roaming\\VanDyke\\Config\\ -Force -Recurse; write-host 'SecureCRT Reset!' -foregroundcolor green; pause; exit\"";
            System.out.println(myValue);
            try {
                Runtime.getRuntime().exec(myValue);
            }
            catch (IOException e) {
                System.out.println("Something is wrong!");
                JOptionPane.showMessageDialog(null, "Something is wrong!");
            }
        }
    }//GEN-LAST:event_jButton31ActionPerformed

    private void jSliderListTextSizeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSliderListTextSizeStateChanged
        setSessionTextSize();
        PropertyHandlerPersonal.getInstance().setValue("Setting.TextSize", String.valueOf(jSliderListTextSize.getValue()));
    }//GEN-LAST:event_jSliderListTextSizeStateChanged

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
        // TODO add your handling code here:
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Choose Folder...");
        //chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            jTextFieldFileHashGenerate.setText(chooser.getSelectedFile().getAbsolutePath());
            System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
            System.out.println("getSelectedFile(): " + chooser.getSelectedFile());
        } else {
            System.out.println("No Selection ");
        }
    }//GEN-LAST:event_jButton27ActionPerformed

    private void jButtonGenerateHashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGenerateHashActionPerformed

        
        File file = new File(jTextFieldFileHashGenerate.getText());
        
        //System.out.println("MD5    : " + toHex(HashGenerate.MD5.checksum(file)));
        jTextFieldHashMD5.setText(toHex(HashGenerate.MD5.checksum(file)));
        jTextAreaHash.setText("MD5: " + toHex(HashGenerate.MD5.checksum(file)));
        
        //System.out.println("SHA1   : " + toHex(HashGenerate.SHA1.checksum(file)));
        jTextFieldHashSHA1.setText(toHex(HashGenerate.SHA1.checksum(file)));
        jTextAreaHash.append("\n\nSHA1: " + toHex(HashGenerate.SHA1.checksum(file)));
           
        //System.out.println("SHA256 : " + toHex(HashGenerate.SHA256.checksum(file)));
        jTextFieldHashSHA256.setText(toHex(HashGenerate.SHA256.checksum(file)));
        jTextAreaHash.append("\n\nSHA256: " + toHex(HashGenerate.SHA256.checksum(file)));
        
        //System.out.println("SHA512 : " + toHex(HashGenerate.SHA512.checksum(file)));
        jTextFieldHashSHA512.setText(toHex(HashGenerate.SHA512.checksum(file)));
        jTextAreaHash.append("\n\nSHA512: " + toHex(HashGenerate.SHA512.checksum(file)));
        
        
    }//GEN-LAST:event_jButtonGenerateHashActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        // TODO add your handling code here:
        String myString = jTextFieldType7Output.getText();
        StringSelection stringSelection = new StringSelection(myString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        // TODO add your handling code here:
        String type7stuff = CiscoVigenere.encrypt(jTextFieldType7Input.getText());
        System.out.println(type7stuff);
        jTextFieldType7Output.setText(type7stuff);
    }//GEN-LAST:event_jButton22ActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        // TODO add your handling code here:
        String type7stuff = CiscoVigenere.decrypt(jTextFieldType7Input.getText());
        System.out.println(type7stuff);
        jTextFieldType7Output.setText(type7stuff);
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jPasswordFieldZipExtractActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordFieldZipExtractActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jPasswordFieldZipExtractActionPerformed

    private void jPasswordFieldZipConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordFieldZipConfirmActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jPasswordFieldZipConfirmActionPerformed

    private void jButton39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton39ActionPerformed
        // TODO add your handling code here:
        new Thread(() -> {
            //Do whatever

            try {
                // Initiate ZipFile object with the path/name of the zip file.
                ZipFile zipFile = new ZipFile(jTextFieldZipSourceFile.getText());

                // Set runInThread variable of ZipFile to true.
                // When this variable is set, Zip4j will run any task in a new thread
                // If this variable is not set, Zip4j will run all tasks in the current
                // thread.
                zipFile.setRunInThread(true);

                // Folder to add
                //                String folderToAdd = jTextFieldZipSourceFolder.getText();

                // Initiate Zip Parameters which define various properties such
                // as compression method, etc.
                ZipParameters parameters = new ZipParameters();

                // set compression method to store compression
                //                parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);

                // Set the compression level
                //                parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

                // Set the encryption flag to true
                // If this is set to false, then the rest of encryption properties are ignored
                //                parameters.setEncryptFiles(true);

                // Set the encryption method to Standard Zip Encryption
                //parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
                //                if (jComboBoxZipEncMethod.getSelectedItem().equals("AES256")) {
                    //                parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
                    //                parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
                    //                }
                //                if (jComboBoxZipEncMethod.getSelectedItem().equals("Standard")) {
                    //                parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
                    //                }

                // Set password
                if (jPasswordFieldZipExtract.getPassword().length != 0) {
                    String passText = new String(jPasswordFieldZipExtract.getPassword());
                    zipFile.setPassword(passText);
                }

                // Add folder to the zip file
                //                zipFile.addFolder(folderToAdd, parameters);

                // Extracts all files to the path specified
                try {
                    if (zipFile.isEncrypted()) {
                        zipFile.setPassword(jPasswordFieldZipExtract.getPassword());
                    }
                    zipFile.extractAll(jTextFieldZipDestinationFolder.getText());
                } catch (ZipException e) {
                }

                // Get progress monitor from ZipFile
                ProgressMonitor progressMonitor = zipFile.getProgressMonitor();
                jProgressBarZipExtract.setStringPainted(true);
                // PLEASE NOTE: Below code does a lot of Sysout's.

                // ProgressMonitor has two states, READY and BUSY. READY indicates that
                // Zip4j can now accept any new tasks. BUSY indicates that Zip4j is
                // currently performing some task and cannot accept any new task at the moment
                // Any attempt to perform any other task will throw an Exception
                while (progressMonitor.getState() == ProgressMonitor.STATE_BUSY) {
                    // ProgressMonitor has a lot of useful information like, the current
                    // operation being performed by Zip4j, current file being processed,
                    // percentage done, etc. Once an operation is completed, ProgressMonitor
                    // also contains the result of the operation. If any exception is thrown
                    // during an operation, this is also stored in this object and can be retrieved
                    // as shown below

                    // To get the percentage done
                    System.out.println("Percent Done: " + progressMonitor.getPercentDone());
                    jProgressBarZipExtract.setValue(progressMonitor.getPercentDone());

                    // To get the current file being processed
                    System.out.println("File: " + progressMonitor.getFileName());

                    // To get current operation
                    // Possible values are:
                    // ProgressMonitor.OPERATION_NONE - no operation being performed
                    // ProgressMonitor.OPERATION_ADD - files are being added to the zip file
                    // ProgressMonitor.OPERATION_EXTRACT - files are being extracted from the zip file
                    // ProgressMonitor.OPERATION_REMOVE - files are being removed from zip file
                    // ProgressMonitor.OPERATION_CALC_CRC - CRC of the file is being calculated
                    // ProgressMonitor.OPERATION_MERGE - Split zip files are being merged
                    switch (progressMonitor.getCurrentOperation()) {
                        case ProgressMonitor.OPERATION_NONE:
                        System.out.println("no operation being performed");
                        jProgressBarZipExtract.setString("No operation being performed");
                        break;
                        case ProgressMonitor.OPERATION_ADD:
                        System.out.println("Add operation");
                        jProgressBarZipExtract.setString("Add operation");
                        break;
                        case ProgressMonitor.OPERATION_EXTRACT:
                        System.out.println("Extract operation");
                        jProgressBarZipExtract.setString("Extract operation");
                        break;
                        case ProgressMonitor.OPERATION_REMOVE:
                        System.out.println("Remove operation");
                        jProgressBarZipExtract.setString("Remove operation");
                        break;
                        case ProgressMonitor.OPERATION_CALC_CRC:
                        System.out.println("Calcualting CRC");
                        jProgressBarZipExtract.setString("Calcualting CRC");
                        break;
                        case ProgressMonitor.OPERATION_MERGE:
                        System.out.println("Merge operation");
                        jProgressBarZipExtract.setString("Merge operation");
                        break;
                        default:
                        System.out.println("invalid operation");
                        jProgressBarZipExtract.setString("invalid operation");
                        break;
                    }
                }

                // Once Zip4j is done with its task, it changes the ProgressMonitor
                // state from BUSY to READY, so the above loop breaks.
                // To get the result of the operation:
                // Possible values:
                // ProgressMonitor.RESULT_SUCCESS - Operation was successful
                // ProgressMonitor.RESULT_WORKING - Zip4j is still working and is not
                //									yet done with the current operation
                // ProgressMonitor.RESULT_ERROR - An error occurred during processing
                System.out.println("Result: " + progressMonitor.getResult());
                if (progressMonitor.getResult() == ProgressMonitor.RESULT_ERROR) {
                    jProgressBarZip.setString("Result: Error!  Password wrong maybe?");
                    System.err.println("An error occurred");

                    // Any exception can be retrieved as below:
                    if (progressMonitor.getException() != null) {
                    } else {
                        System.err.println("An error occurred without any exception");
                    }
                }

            } catch (ZipException e) {
            }
        }).start();
    }//GEN-LAST:event_jButton39ActionPerformed

    private void jButtonZipBrowseDestinationFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonZipBrowseDestinationFolderActionPerformed
        // TODO add your handling code here:
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Choose Folder...");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            jTextFieldZipDestinationFolder.setText(chooser.getSelectedFile().getAbsolutePath());
            System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
            System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
        } else {
            System.out.println("No Selection ");
        }
    }//GEN-LAST:event_jButtonZipBrowseDestinationFolderActionPerformed

    private void jButtonZipBrowseSourceZipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonZipBrowseSourceZipActionPerformed
        // TODO add your handling code here:
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Choose Folder...");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        //chooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter zipFilter = new FileNameExtensionFilter("ZIP Files", "zip");
        chooser.setFileFilter(zipFilter);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            jTextFieldZipSourceFile.setText(chooser.getSelectedFile().getAbsolutePath());
            System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
            System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
        } else {
            System.out.println("No Selection ");
        }
    }//GEN-LAST:event_jButtonZipBrowseSourceZipActionPerformed

    private void jTextFieldZipSourceFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldZipSourceFileActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldZipSourceFileActionPerformed

    private void jPasswordFieldZipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordFieldZipActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jPasswordFieldZipActionPerformed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        // TODO add your handling code here:
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Choose Folder...");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            jTextFieldZipSourceFolder.setText(chooser.getSelectedFile().getAbsolutePath());
            System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
            System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
        } else {
            System.out.println("No Selection ");
        }
    }//GEN-LAST:event_jButton25ActionPerformed

    private void jButtonFolderToZipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFolderToZipActionPerformed
        // TODO add your handling code here:
        new Thread(() -> {
            //Do whatever

            try {
                // Initiate ZipFile object with the path/name of the zip file.
                ZipFile zipFile = new ZipFile(jTextFieldZipFilename.getText());

                // Set runInThread variable of ZipFile to true.
                // When this variable is set, Zip4j will run any task in a new thread
                // If this variable is not set, Zip4j will run all tasks in the current
                // thread.
                zipFile.setRunInThread(true);

                // Folder to add
                String folderToAdd = jTextFieldZipSourceFolder.getText();

                // Initiate Zip Parameters which define various properties such
                // as compression method, etc.
                ZipParameters parameters = new ZipParameters();

                // set compression method to store compression
                parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);

                // Set the compression level
                parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

                // Set the encryption flag to true
                // If this is set to false, then the rest of encryption properties are ignored
                parameters.setEncryptFiles(true);

                // Set the encryption method to Standard Zip Encryption
                //parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
                if (jComboBoxZipEncMethod.getSelectedItem().equals("AES-256 Encryption")) {
                    parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
                    parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
                }
                if (jComboBoxZipEncMethod.getSelectedItem().equals("Standard Encryption")) {
                    parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
                }

                // Set password
                if (Arrays.equals(jPasswordFieldZip.getPassword(),jPasswordFieldZipConfirm.getPassword())) {
                    String passText = new String(jPasswordFieldZip.getPassword());
                    parameters.setPassword(passText);
                } else {
                    JOptionPane.showMessageDialog(null, "Passwords don't match!");
                    return;
                }

                // Add folder to the zip file
                zipFile.addFolder(folderToAdd, parameters);

                // Get progress monitor from ZipFile
                ProgressMonitor progressMonitor = zipFile.getProgressMonitor();
                jProgressBarZip.setStringPainted(true);
                // PLEASE NOTE: Below code does a lot of Sysout's.

                // ProgressMonitor has two states, READY and BUSY. READY indicates that
                // Zip4j can now accept any new tasks. BUSY indicates that Zip4j is
                // currently performing some task and cannot accept any new task at the moment
                // Any attempt to perform any other task will throw an Exception
                while (progressMonitor.getState() == ProgressMonitor.STATE_BUSY) {
                    // ProgressMonitor has a lot of useful information like, the current
                    // operation being performed by Zip4j, current file being processed,
                    // percentage done, etc. Once an operation is completed, ProgressMonitor
                    // also contains the result of the operation. If any exception is thrown
                    // during an operation, this is also stored in this object and can be retrieved
                    // as shown below

                    // To get the percentage done
                    System.out.println("Percent Done: " + progressMonitor.getPercentDone());
                    jProgressBarZip.setValue(progressMonitor.getPercentDone());

                    // To get the current file being processed
                    System.out.println("File: " + progressMonitor.getFileName());

                    // To get current operation
                    // Possible values are:
                    // ProgressMonitor.OPERATION_NONE - no operation being performed
                    // ProgressMonitor.OPERATION_ADD - files are being added to the zip file
                    // ProgressMonitor.OPERATION_EXTRACT - files are being extracted from the zip file
                    // ProgressMonitor.OPERATION_REMOVE - files are being removed from zip file
                    // ProgressMonitor.OPERATION_CALC_CRC - CRC of the file is being calculated
                    // ProgressMonitor.OPERATION_MERGE - Split zip files are being merged
                    switch (progressMonitor.getCurrentOperation()) {
                        case ProgressMonitor.OPERATION_NONE:
                        System.out.println("no operation being performed");
                        jProgressBarZip.setString("No operation being performed");
                        break;
                        case ProgressMonitor.OPERATION_ADD:
                        System.out.println("Add operation");
                        jProgressBarZip.setString("Add operation");
                        break;
                        case ProgressMonitor.OPERATION_EXTRACT:
                        System.out.println("Extract operation");
                        jProgressBarZip.setString("Extract operation");
                        break;
                        case ProgressMonitor.OPERATION_REMOVE:
                        System.out.println("Remove operation");
                        jProgressBarZip.setString("Remove operation");
                        break;
                        case ProgressMonitor.OPERATION_CALC_CRC:
                        System.out.println("Calcualting CRC");
                        jProgressBarZip.setString("Calcualting CRC");
                        break;
                        case ProgressMonitor.OPERATION_MERGE:
                        System.out.println("Merge operation");
                        jProgressBarZip.setString("Merge operation");
                        break;

                        default:
                        System.out.println("invalid operation");
                        jProgressBarZip.setString("invalid operation");
                        break;
                    }
                }

                // Once Zip4j is done with its task, it changes the ProgressMonitor
                // state from BUSY to READY, so the above loop breaks.
                // To get the result of the operation:
                // Possible values:
                // ProgressMonitor.RESULT_SUCCESS - Operation was successful
                // ProgressMonitor.RESULT_WORKING - Zip4j is still working and is not
                //									yet done with the current operation
                // ProgressMonitor.RESULT_ERROR - An error occurred during processing
                System.out.println("Result: " + progressMonitor.getResult());
                if (progressMonitor.getResult() == ProgressMonitor.RESULT_ERROR) {
                    //jProgressBarZip.setString("Result: Error!  Password wrong maybe?");
                    System.err.println("An error occurred");

                    // Any exception can be retrieved as below:
                    if (progressMonitor.getException() != null) {
                    } else {
                        System.err.println("An error occurred without any exception");
                    }
                }

            } catch (ZipException e) {
            }
        }).start();
    }//GEN-LAST:event_jButtonFolderToZipActionPerformed

    private void jButton37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton37ActionPerformed
        // TODO add your handling code here:
        String inputPdf = "files/Cheat Sheets - PacketLife.net.zip";
        InputStream manualAsStream = getClass().getClassLoader().getResourceAsStream(inputPdf);

        Path tempOutput = null;
        try {
            tempOutput = Files.createTempFile("TempFile", ".zip");
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        tempOutput.toFile().deleteOnExit();

        try {
            Files.copy(manualAsStream, tempOutput, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }

        File userManual = new File (tempOutput.toFile().getPath());
        if (userManual.exists())
        {
            try {
                Desktop.getDesktop().open(userManual);
            } catch (IOException ex) {
                Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButton37ActionPerformed

    private void jButton36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton36ActionPerformed
        // TODO add your handling code here:
        String inputPdf = "files/IPv4_Subnetting.pdf";
        InputStream manualAsStream = getClass().getClassLoader().getResourceAsStream(inputPdf);

        Path tempOutput = null;
        try {
            tempOutput = Files.createTempFile("TempFile", ".pdf");
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        tempOutput.toFile().deleteOnExit();

        try {
            Files.copy(manualAsStream, tempOutput, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }

        File userManual = new File (tempOutput.toFile().getPath());
        if (userManual.exists())
        {
            try {
                Desktop.getDesktop().open(userManual);
            } catch (IOException ex) {
                Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButton36ActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        openTempFileUsingDesktop("files/Subnets.pdf", ".pdf");
    }//GEN-LAST:event_jButton24ActionPerformed

    private void jButtonReferenceCustom03ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom03ActionPerformed
        try {
            openReference("Custom.Reference.03");
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonReferenceCustom03ActionPerformed

    private void jButtonReferenceCustom02ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom02ActionPerformed
        try {
            openReference("Custom.Reference.02");
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonReferenceCustom02ActionPerformed

    private void jButtonReferenceCustom01ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom01ActionPerformed
        try {
            openReference("Custom.Reference.01");
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonReferenceCustom01ActionPerformed

    private void jToggleOnlineOfflineModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleOnlineOfflineModeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jToggleOnlineOfflineModeActionPerformed

    private void jButtonScriptiPerfServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptiPerfServerActionPerformed
        openEmbeddedPowershellScript("scripts/Powershell-iPerfServer.ps1", ".ps1");
    }//GEN-LAST:event_jButtonScriptiPerfServerActionPerformed

    private void jButtonScriptPowershellPingSweepRangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptPowershellPingSweepRangeActionPerformed
        openEmbeddedPowershellScript("scripts/Powershell-PingSweep.ps1", ".ps1");
    }//GEN-LAST:event_jButtonScriptPowershellPingSweepRangeActionPerformed

    private void jButtonScriptCreateDummyFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCreateDummyFileActionPerformed
        openEmbeddedPowershellScript("scripts/Powershell-CreateDummyFile.ps1", ".ps1");
    }//GEN-LAST:event_jButtonScriptCreateDummyFileActionPerformed

    private void jButtonScriptCustom28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom28ActionPerformed
        openScriptCustom("Custom.Script.28.Exec");
    }//GEN-LAST:event_jButtonScriptCustom28ActionPerformed

    private void jButtonScriptCustom29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom29ActionPerformed
        openScriptCustom("Custom.Script.29.Exec");
    }//GEN-LAST:event_jButtonScriptCustom29ActionPerformed

    private void jButtonScriptCustom25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom25ActionPerformed
        openScriptCustom("Custom.Script.25.Exec");
    }//GEN-LAST:event_jButtonScriptCustom25ActionPerformed

    private void jButtonScriptCustom26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom26ActionPerformed
        openScriptCustom("Custom.Script.26.Exec");
    }//GEN-LAST:event_jButtonScriptCustom26ActionPerformed

    private void jButtonScriptCustom23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom23ActionPerformed
        openScriptCustom("Custom.Script.23.Exec");
    }//GEN-LAST:event_jButtonScriptCustom23ActionPerformed

    private void jButtonScriptCustom22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom22ActionPerformed
        openScriptCustom("Custom.Script.22.Exec");
    }//GEN-LAST:event_jButtonScriptCustom22ActionPerformed

    private void jButtonScriptCustom19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom19ActionPerformed
        openScriptCustom("Custom.Script.19.Exec");
    }//GEN-LAST:event_jButtonScriptCustom19ActionPerformed

    private void jButtonScriptCustom20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom20ActionPerformed
        openScriptCustom("Custom.Script.20.Exec");
    }//GEN-LAST:event_jButtonScriptCustom20ActionPerformed

    private void jButtonScriptCustom17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom17ActionPerformed
        openScriptCustom("Custom.Script.17.Exec");
    }//GEN-LAST:event_jButtonScriptCustom17ActionPerformed

    private void jButtonScriptCustom16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom16ActionPerformed
        openScriptCustom("Custom.Script.16.Exec");
    }//GEN-LAST:event_jButtonScriptCustom16ActionPerformed

    private void jButtonScriptCustom14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom14ActionPerformed
        openScriptCustom("Custom.Script.14.Exec");
    }//GEN-LAST:event_jButtonScriptCustom14ActionPerformed

    private void jButtonScriptCustom13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom13ActionPerformed
        openScriptCustom("Custom.Script.13.Exec");
    }//GEN-LAST:event_jButtonScriptCustom13ActionPerformed

    private void jButtonScriptCustom10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom10ActionPerformed
        openScriptCustom("Custom.Script.10.Exec");
    }//GEN-LAST:event_jButtonScriptCustom10ActionPerformed

    private void jButtonScriptCustom11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom11ActionPerformed
        openScriptCustom("Custom.Script.11.Exec");
    }//GEN-LAST:event_jButtonScriptCustom11ActionPerformed

    private void jButtonScriptCustom12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom12ActionPerformed
        openScriptCustom("Custom.Script.12.Exec");
    }//GEN-LAST:event_jButtonScriptCustom12ActionPerformed

    private void jButtonScriptCustom07ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom07ActionPerformed
        openScriptCustom("Custom.Script.07.Exec");
    }//GEN-LAST:event_jButtonScriptCustom07ActionPerformed

    private void jButtonScriptCustom08ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom08ActionPerformed
        openScriptCustom("Custom.Script.08.Exec");
    }//GEN-LAST:event_jButtonScriptCustom08ActionPerformed

    private void jButtonScriptCustom05ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom05ActionPerformed
        openScriptCustom("Custom.Script.05.Exec");
    }//GEN-LAST:event_jButtonScriptCustom05ActionPerformed

    private void jButtonScriptCustom04ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom04ActionPerformed
        openScriptCustom("Custom.Script.04.Exec");
    }//GEN-LAST:event_jButtonScriptCustom04ActionPerformed

    private void jButtonScriptCustom02ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom02ActionPerformed
        openScriptCustom("Custom.Script.02.Exec");
    }//GEN-LAST:event_jButtonScriptCustom02ActionPerformed

    private void jButtonScriptCustom01ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom01ActionPerformed
        openScriptCustom("Custom.Script.01.Exec");
    }//GEN-LAST:event_jButtonScriptCustom01ActionPerformed

    private void jButtonLinkCustom30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom30ActionPerformed
        openLinkCustom("Custom.Link.30.Exec");
    }//GEN-LAST:event_jButtonLinkCustom30ActionPerformed

    private void jButtonLinkCustom29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom29ActionPerformed
        openLinkCustom("Custom.Link.29.Exec");
    }//GEN-LAST:event_jButtonLinkCustom29ActionPerformed

    private void jButtonLinkCustom28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom28ActionPerformed
        openLinkCustom("Custom.Link.28.Exec");
    }//GEN-LAST:event_jButtonLinkCustom28ActionPerformed

    private void jButtonLinkCustom27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom27ActionPerformed
        openLinkCustom("Custom.Link.27.Exec");
    }//GEN-LAST:event_jButtonLinkCustom27ActionPerformed

    private void jButtonLinkCustom26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom26ActionPerformed
        openLinkCustom("Custom.Link.26.Exec");
    }//GEN-LAST:event_jButtonLinkCustom26ActionPerformed

    private void jButtonLinkCustom25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom25ActionPerformed
        openLinkCustom("Custom.Link.25.Exec");
    }//GEN-LAST:event_jButtonLinkCustom25ActionPerformed

    private void jButtonLinkCustom24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom24ActionPerformed
        openLinkCustom("Custom.Link.24.Exec");
    }//GEN-LAST:event_jButtonLinkCustom24ActionPerformed

    private void jButtonLinkCustom23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom23ActionPerformed
        openLinkCustom("Custom.Link.23.Exec");
    }//GEN-LAST:event_jButtonLinkCustom23ActionPerformed

    private void jButtonLinkCustom22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom22ActionPerformed
        openLinkCustom("Custom.Link.22.Exec");
    }//GEN-LAST:event_jButtonLinkCustom22ActionPerformed

    private void jButtonLinkCustom21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom21ActionPerformed
        openLinkCustom("Custom.Link.21.Exec");
    }//GEN-LAST:event_jButtonLinkCustom21ActionPerformed

    private void jButtonLinkCustom20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom20ActionPerformed
        openLinkCustom("Custom.Link.20.Exec");
    }//GEN-LAST:event_jButtonLinkCustom20ActionPerformed

    private void jButtonLinkCustom19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom19ActionPerformed
        openLinkCustom("Custom.Link.19.Exec");
    }//GEN-LAST:event_jButtonLinkCustom19ActionPerformed

    private void jButtonLinkCustom18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom18ActionPerformed
        openLinkCustom("Custom.Link.18.Exec");
    }//GEN-LAST:event_jButtonLinkCustom18ActionPerformed

    private void jButtonLinkCustom17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom17ActionPerformed
        openLinkCustom("Custom.Link.17.Exec");
    }//GEN-LAST:event_jButtonLinkCustom17ActionPerformed

    private void jButtonLinkCustom16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom16ActionPerformed
        openLinkCustom("Custom.Link.16.Exec");
    }//GEN-LAST:event_jButtonLinkCustom16ActionPerformed

    private void jButtonLinkCustom15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom15ActionPerformed
        openLinkCustom("Custom.Link.15.Exec");
    }//GEN-LAST:event_jButtonLinkCustom15ActionPerformed

    private void jButtonLinkCustom14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom14ActionPerformed
        openLinkCustom("Custom.Link.14.Exec");
    }//GEN-LAST:event_jButtonLinkCustom14ActionPerformed

    private void jButtonLinkCustom13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom13ActionPerformed
        openLinkCustom("Custom.Link.13.Exec");
    }//GEN-LAST:event_jButtonLinkCustom13ActionPerformed

    private void jButtonLinkCustom12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom12ActionPerformed
        openLinkCustom("Custom.Link.12.Exec");
    }//GEN-LAST:event_jButtonLinkCustom12ActionPerformed

    private void jButtonLinkCustom10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom10ActionPerformed
        openLinkCustom("Custom.Link.10.Exec");
    }//GEN-LAST:event_jButtonLinkCustom10ActionPerformed

    private void jButtonLinkCustom11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom11ActionPerformed
        openLinkCustom("Custom.Link.11.Exec");
    }//GEN-LAST:event_jButtonLinkCustom11ActionPerformed

    private void jButtonLinkCustom09ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom09ActionPerformed
        openLinkCustom("Custom.Link.09.Exec");  
    }//GEN-LAST:event_jButtonLinkCustom09ActionPerformed

    private void jButtonLinkCustom08ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom08ActionPerformed
        openLinkCustom("Custom.Link.08.Exec");
    }//GEN-LAST:event_jButtonLinkCustom08ActionPerformed

    private void jButtonLinkCustom07ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom07ActionPerformed
        openLinkCustom("Custom.Link.07.Exec");
    }//GEN-LAST:event_jButtonLinkCustom07ActionPerformed

    private void jButtonLinkCustom06ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom06ActionPerformed
        openLinkCustom("Custom.Link.06.Exec");
    }//GEN-LAST:event_jButtonLinkCustom06ActionPerformed

    private void jButtonLinkCustom05ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom05ActionPerformed
        openLinkCustom("Custom.Link.05.Exec");
    }//GEN-LAST:event_jButtonLinkCustom05ActionPerformed

    private void jButtonLinkCustom04ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom04ActionPerformed
        openLinkCustom("Custom.Link.04.Exec");
    }//GEN-LAST:event_jButtonLinkCustom04ActionPerformed

    private void jButtonLinkCustom03ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom03ActionPerformed
        openLinkCustom("Custom.Link.03.Exec");
    }//GEN-LAST:event_jButtonLinkCustom03ActionPerformed

    private void jButtonLinkCustom02ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom02ActionPerformed
        openLinkCustom("Custom.Link.02.Exec");
    }//GEN-LAST:event_jButtonLinkCustom02ActionPerformed

    private void jButtonLinkCustom01ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom01ActionPerformed
        openLinkCustom("Custom.Link.01.Exec");
    }//GEN-LAST:event_jButtonLinkCustom01ActionPerformed

    private void jCheckBoxFavoritesStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBoxFavoritesStateChanged

    }//GEN-LAST:event_jCheckBoxFavoritesStateChanged

    private void jCheckBoxFavoritesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxFavoritesItemStateChanged
        // TODO add your handling code here:
        try {
            getSessionList();
        } catch (IOException | URISyntaxException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jCheckBoxFavoritesItemStateChanged

    private void jTextFieldFilterKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldFilterKeyReleased
        // TODO add your handling code here:
        try {
            searchFilter(jTextFieldFilter.getText());
        } catch (IOException | URISyntaxException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jTextFieldFilterKeyReleased

    private void jTextFieldFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldFilterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldFilterActionPerformed

    private void jButtonExecuteFunctionRDPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExecuteFunctionRDPActionPerformed
        // TODO add your handling code here:
        System.out.println("RDP enganged.");
        String strEXEC = "cmd /c start mstsc.exe /v:" + jTextFieldConnectHostname.getText();
        try {
            Runtime.getRuntime().exec(strEXEC);
        }
        catch (IOException e) {
            System.out.println("Something is wrong!");
            JOptionPane.showMessageDialog(null, "Something is wrong!");
        }
    }//GEN-LAST:event_jButtonExecuteFunctionRDPActionPerformed

    private void jCheckBoxAlternateLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxAlternateLoginActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBoxAlternateLoginActionPerformed

    private void jButtonMainButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMainButton17ActionPerformed
        openCommand("Button.17.StrExec");
    }//GEN-LAST:event_jButtonMainButton17ActionPerformed

    private void jButtonMainButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMainButton18ActionPerformed
        openCommand("Button.18.StrExec");
    }//GEN-LAST:event_jButtonMainButton18ActionPerformed

    private void jButtonMainButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMainButton19ActionPerformed
        openCommand("Button.19.StrExec");
    }//GEN-LAST:event_jButtonMainButton19ActionPerformed

    private void jButtonMainButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMainButton20ActionPerformed
        openCommand("Button.20.StrExec");
    }//GEN-LAST:event_jButtonMainButton20ActionPerformed

    private void jButtonMainButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMainButton16ActionPerformed
        openCommand("Button.16.StrExec");
    }//GEN-LAST:event_jButtonMainButton16ActionPerformed

    private void jButtonMainButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMainButton15ActionPerformed
        openCommand("Button.15.StrExec");
    }//GEN-LAST:event_jButtonMainButton15ActionPerformed

    private void jButtonMainButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMainButton14ActionPerformed
        openCommand("Button.14.StrExec");
    }//GEN-LAST:event_jButtonMainButton14ActionPerformed

    private void jButtonMainButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMainButton13ActionPerformed
        openCommand("Button.13.StrExec");
    }//GEN-LAST:event_jButtonMainButton13ActionPerformed

    private void jButtonMainButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMainButton9ActionPerformed
        openCommand("Button.09.StrExec");
    }//GEN-LAST:event_jButtonMainButton9ActionPerformed

    private void jButtonMainButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMainButton5ActionPerformed
        openCommand("Button.05.StrExec");
    }//GEN-LAST:event_jButtonMainButton5ActionPerformed

    private void jButtonMainButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMainButton10ActionPerformed
        openCommand("Button.10.StrExec");
    }//GEN-LAST:event_jButtonMainButton10ActionPerformed

    private void jButtonMainButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMainButton6ActionPerformed
        openCommand("Button.06.StrExec");
    }//GEN-LAST:event_jButtonMainButton6ActionPerformed

    private void jButtonMainButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMainButton11ActionPerformed
        openCommand("Button.11.StrExec");
    }//GEN-LAST:event_jButtonMainButton11ActionPerformed

    private void jButtonMainButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMainButton7ActionPerformed
        openCommand("Button.07.StrExec");
    }//GEN-LAST:event_jButtonMainButton7ActionPerformed

    private void jButtonMainButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMainButton12ActionPerformed
        openCommand("Button.12.StrExec");
    }//GEN-LAST:event_jButtonMainButton12ActionPerformed

    private void jButtonMainButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMainButton8ActionPerformed
        openCommand("Button.08.StrExec");
    }//GEN-LAST:event_jButtonMainButton8ActionPerformed

    private void jButtonMainButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMainButton4ActionPerformed
        openCommand("Button.04.StrExec");
    }//GEN-LAST:event_jButtonMainButton4ActionPerformed

    private void jButtonMainButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMainButton3ActionPerformed
        openCommand("Button.03.StrExec");
    }//GEN-LAST:event_jButtonMainButton3ActionPerformed

    private void jButtonMainButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMainButton2ActionPerformed
        openCommand("Button.02.StrExec");
    }//GEN-LAST:event_jButtonMainButton2ActionPerformed

    private void jButtonMainButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMainButton1ActionPerformed
        openCommand("Button.01.StrExec");
    }//GEN-LAST:event_jButtonMainButton1ActionPerformed

    private void jButtonConsoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConsoleActionPerformed
        // TODO add your handling code here:
        String strSecureCRTexe = PropertyHandler.getInstance().getValue("SecureCRT.Exec").replace("%USERPROFILE%", pathUserProfile);

        System.out.println("SecureCRT file: " + strSecureCRTexe);

        SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("yyyy-MM-dd_HHmm-ssSSS");
        String dateTime = simpleDateFormat.format(new Date());
        String fileLog = pathLogging + "\\Serial-" + jComboBoxConsoleCOM.getSelectedItem() + "-" + dateTime + ".txt";
        System.out.println("Log file: " + fileLog);


            String strEXEC = strSecureCRTexe + " /LOG \"" + fileLog + "\" /T /SERIAL " + jComboBoxConsoleCOM.getSelectedItem() + " /BAUD " + jComboBoxConsoleBaud.getSelectedItem();
            System.out.println(strEXEC);
            try {
                Runtime.getRuntime().exec(strEXEC);
            }
            catch (IOException e) {
                System.out.println("Something is wrong!");
                JOptionPane.showMessageDialog(null, "Something is wrong!");
            }
    }//GEN-LAST:event_jButtonConsoleActionPerformed

    private void jButtonShowCOMListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonShowCOMListActionPerformed
        // TODO add your handling code here:
        System.out.println("Pressed");
        try {
            Runtime.getRuntime().exec("cmd /c start cmd.exe /c \"change port /query & pause\"");
        }
        catch (IOException e) {
            System.out.println("Something is wrong!");
        }
    }//GEN-LAST:event_jButtonShowCOMListActionPerformed

    private void jComboBoxConsoleCOMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxConsoleCOMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxConsoleCOMActionPerformed

    private void jButtonPingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPingActionPerformed
        // TODO add your handling code here:
        System.out.println("Pressed");
        String StringPingDNS = "";
        String StringPingLOG = "";
        simpleDateFormat  = new SimpleDateFormat("yyyy-MM-dd_HHmm-ssSSS");
        dateTime = simpleDateFormat.format(new Date());
        
        if(jCheckBoxDNS.isSelected()) {
            StringPingDNS = "-a";
        }
        
        //- Decided to hard-set logging to the output folder
        //if(jCheckBoxLOG.isSelected()) {
            StringPingLOG = "| Tee-Object \"" + strPathLoggingFolder + "\\Ping-" + jTextFieldPingHostname.getText() + "-" + dateTime + ".txt" + "\"";
        //}        
        
        try {
            //- Old CMD Style
            //Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"ping -t " + StringPingDNS + " " + jTextFieldPingHostname.getText() + "\"");
            
            //- PowerShell
            String strExec = "cmd /c start powershell.exe -ExecutionPolicy Bypass -noexit -Command \"$host.ui.RawUI.WindowTitle = 'Pinging " + jTextFieldPingHostname.getText() + "' ; ping -t " + StringPingDNS + " " + jTextFieldPingHostname.getText() + " 2>&1 |Foreach{'{0} - {1}' -f (Get-Date -f yyyy-MM-dd_HHmm-ssfff),$_} | select -skip 1 " + StringPingLOG;
            //JOptionPane.showMessageDialog(this, strExec);
            System.out.println(strExec);           
            Runtime.getRuntime().exec(strExec);
        }
        catch (IOException e) {
            System.out.println("Something is wrong!");
            JOptionPane.showMessageDialog(null, "Something is wrong!");
        }
    }//GEN-LAST:event_jButtonPingActionPerformed

    private void jButtonTracertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTracertActionPerformed
        // TODO add your handling code here:
        System.out.println("Pressed");
        try {
            Runtime.getRuntime().exec("cmd /c start cmd.exe /c \"tracert " + jTextFieldPingHostname.getText() + " & pause\"");
        }
        catch (IOException e) {
            System.out.println("Something is wrong!");
        }
    }//GEN-LAST:event_jButtonTracertActionPerformed

    private void jTextFieldPingHostnameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldPingHostnameKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            Robot r = null;
            try {
                r = new Robot();
            } catch (AWTException ex) {
                Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
            }
            jButtonPing.doClick();
        }
    }//GEN-LAST:event_jTextFieldPingHostnameKeyTyped

    private void jTextFieldPingHostnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldPingHostnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldPingHostnameActionPerformed

    private void jPasswordFieldConnectPasswordKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPasswordFieldConnectPasswordKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            Robot r = null;
            try {
                r = new Robot();
            } catch (AWTException ex) {
                Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
            }
            jButtonExecuteFunctionSSH.doClick();
        }
    }//GEN-LAST:event_jPasswordFieldConnectPasswordKeyTyped

    private void jTextFieldConnectUsernameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldConnectUsernameKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            Robot r = null;
            try {
                r = new Robot();
            } catch (AWTException ex) {
                Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
            }
            jButtonExecuteFunctionSSH.doClick();
        }
    }//GEN-LAST:event_jTextFieldConnectUsernameKeyTyped

    private void jButtonExecuteFunctionSSHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExecuteFunctionSSHActionPerformed
        String strSecureCRTexe = PropertyHandler.getInstance().getValue("SecureCRT.Exec").replace("%USERPROFILE%", pathUserProfile);
        System.out.println("SecureCRT file: " + strSecureCRTexe);
        simpleDateFormat  = new SimpleDateFormat("yyyy-MM-dd_HHmm-ssSSS");
        dateTime = simpleDateFormat.format(new Date());
        String fileLog = pathDesktop + "\\Logging-Output\\SSH-" + jTextFieldConnectHostname.getText() + " " + dateTime.toString() + ".txt";
        System.out.println("Log file: " + fileLog);


            if (!jCheckBoxAlternateLogin.isSelected()) {
                        String strEXEC = strSecureCRTexe + " /LOG \"" + fileLog + "\" /T /SSH2 /ACCEPTHOSTKEYS /AUTH keyboard-interactive " + jTextFieldConnectHostname.getText();
                        System.out.println(strEXEC);
                        try {
                            Runtime.getRuntime().exec(strEXEC);
                        }
                        catch (IOException e) {
                            System.out.println("Something is wrong!");
                            JOptionPane.showMessageDialog(null, "Something is wrong!");
                        }
            }
            else {
                String passText = new String(jPasswordFieldConnectPassword.getPassword());
                String strEXEC = strSecureCRTexe + " /LOG \"" + fileLog + "\" /T /SSH2 /ACCEPTHOSTKEYS " + jTextFieldConnectHostname.getText() + " /L " + jTextFieldConnectUsername.getText() + " /PASSWORD \"" + passText + "\"  ";
                try {
                    Runtime.getRuntime().exec(strEXEC);
                }
                catch (IOException e) {
                    System.out.println("Something is wrong!");
                    JOptionPane.showMessageDialog(null, "Something is wrong!");
                }
            }
        try {
            if("1".equals(PropertyHandler.getInstance().getValue("Setting.PasswordBasedSSHauthDisableAutoReset"))) {
                jRadioButtonPWauthDisabled.setSelected(Boolean.TRUE);
                jButtonExecuteFunctionSSH.setBackground(new javax.swing.JButton().getBackground());
                jButtonExecuteFunctionSSH.setEnabled(false);
            }
        } catch (NullPointerException e) {System.out.println("Setting.PasswordBasedSSHauthDisable Goofed");
        }
    }//GEN-LAST:event_jButtonExecuteFunctionSSHActionPerformed

    private void jButtonExecuteFunctionHTTPSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExecuteFunctionHTTPSActionPerformed
        // TODO add your handling code here:
        Integer buttonHeightWidth = 40;
        Image newimg;
        System.out.println("Pressed");
        //- Explorer 
        ImageIcon iconExplorer = new ImageIcon(getClass().getResource("/launchpad/images/buttons/iexplore.png"));
        newimg = iconExplorer.getImage(); newimg = newimg.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButtonMainButton1.setIcon(new ImageIcon(newimg));
        iconExplorer = new ImageIcon(newimg);
        //- Edge
        ImageIcon iconEdge = new ImageIcon(getClass().getResource("/launchpad/images/buttons/edge.png"));
        newimg = iconEdge.getImage(); newimg = newimg.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButtonMainButton1.setIcon(new ImageIcon(newimg));
        iconEdge = new ImageIcon(newimg);        
        //- FireFox
        ImageIcon iconFireFox = new ImageIcon(getClass().getResource("/launchpad/images/buttons/firefox.png"));
        newimg = iconFireFox.getImage(); newimg = newimg.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButtonMainButton1.setIcon(new ImageIcon(newimg));
        iconFireFox = new ImageIcon(newimg);  
        //- Chrome
        ImageIcon iconChrome = new ImageIcon(getClass().getResource("/launchpad/images/buttons/chrome.png"));
        newimg = iconChrome.getImage(); newimg = newimg.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButtonMainButton1.setIcon(new ImageIcon(newimg));
        iconChrome = new ImageIcon(newimg);  
        //- Build Option Pane
        Object[] iconArray = {iconExplorer,
            iconEdge,
            iconFireFox,
            iconChrome};
        int result = JOptionPane.showOptionDialog(null,
            "IE, Edge, FireFox, Chrome?",
            "Browser Chooser",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            iconArray,
            iconArray[3]);
        System.out.println("Option selected: " + result);

        if(result == 0) {
            System.out.println("IE engaged.");
            String strEXEC = "cmd /c start iexplore.exe https://" + jTextFieldConnectHostname.getText();
            try {
                Runtime.getRuntime().exec(strEXEC);
            }
            catch (IOException e) {
                System.out.println("Something is wrong!");
                JOptionPane.showMessageDialog(null, "Something is wrong!");
            }
        }
        if(result == 2) {
            System.out.println("FireKitsune engaged.");
            String strEXEC = "cmd /c start firefox.exe https://" + jTextFieldConnectHostname.getText();
            try {
                Runtime.getRuntime().exec(strEXEC);
            }
            catch (IOException e) {
                System.out.println("Something is wrong!");
                JOptionPane.showMessageDialog(null, "Something is wrong!");
            }
        }
        if(result == 1) {
            System.out.println("Edge engaged.");
            String strEXEC = "cmd /c start microsoft-edge:https://" + jTextFieldConnectHostname.getText();
            try {
                Runtime.getRuntime().exec(strEXEC);
            }
            catch (IOException e) {
                System.out.println("Something is wrong!");
                JOptionPane.showMessageDialog(null, "Something is wrong!");
            }
        }
        if(result == 3) {
            System.out.println("Chrome engaged.");
            String strEXEC = "cmd /c start chrome.exe https://" + jTextFieldConnectHostname.getText();
            try {
                Runtime.getRuntime().exec(strEXEC);
            }
            catch (IOException e) {
                System.out.println("Something is wrong!");
                JOptionPane.showMessageDialog(null, "Something is wrong!");
            }
        }
    }//GEN-LAST:event_jButtonExecuteFunctionHTTPSActionPerformed

    private void jTextFieldConnectHostnameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldConnectHostnameKeyTyped
        // TODO add your handling code here:
        
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
//            Robot r = null;
//            try {
//                r = new Robot();
//            } catch (AWTException ex) {
//                Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
//            }
            QuickLaunchSessionOnEnter();


        }
    }//GEN-LAST:event_jTextFieldConnectHostnameKeyTyped

    private void jTextFieldConnectHostnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldConnectHostnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldConnectHostnameActionPerformed

    private void jListSessionsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListSessionsValueChanged
        // TODO add your handling code here:
        String strSelectedValue = jListSessions.getSelectedValue();
        try {
                if(strSelectedValue.contains(",")) {
                String[] arrSelectedValue = strSelectedValue.split(",");
                jTextFieldConnectHostname.setText(arrSelectedValue[1]);
                if (jCheckBoxChangePingOnSessionSelect.isSelected()){
                    jTextFieldPingHostname.setText(arrSelectedValue[1]);
                }
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jListSessionsValueChanged

    private void jButtonJSDiff2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonJSDiff2ActionPerformed
        openTempFileUsingDesktop("html/jsdiff/jsDiff.html", ".html");
    }//GEN-LAST:event_jButtonJSDiff2ActionPerformed

    private void jButtonConfigBuilder1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConfigBuilder1ActionPerformed
        openTempFileUsingDesktop("html/configbuilder/ConfigBuilder.html", ".html");
    }//GEN-LAST:event_jButtonConfigBuilder1ActionPerformed

    private void jButtonSubnetCalculatorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSubnetCalculatorActionPerformed
        openTempFileUsingDesktop("html/subnetcalculator/SubnetCalculator.html", ".html");
    }//GEN-LAST:event_jButtonSubnetCalculatorActionPerformed

    private void jButton41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton41ActionPerformed
        openTempFileUsingDesktop("apps/Puppeteer.jar", ".jar");
    }//GEN-LAST:event_jButton41ActionPerformed

    private void jButtonLinkCustom31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom31ActionPerformed
        openLinkCustom("Custom.Link.31.Exec");
    }//GEN-LAST:event_jButtonLinkCustom31ActionPerformed

    private void jButtonLinkCustom32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom32ActionPerformed
        openLinkCustom("Custom.Link.32.Exec");
    }//GEN-LAST:event_jButtonLinkCustom32ActionPerformed

    private void jButtonLinkCustom33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom33ActionPerformed
        openLinkCustom("Custom.Link.33.Exec");
    }//GEN-LAST:event_jButtonLinkCustom33ActionPerformed

    private void jButtonLinkCustom34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom34ActionPerformed
        openLinkCustom("Custom.Link.34.Exec");
    }//GEN-LAST:event_jButtonLinkCustom34ActionPerformed

    private void jButtonLinkCustom35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom35ActionPerformed
        openLinkCustom("Custom.Link.35.Exec");
    }//GEN-LAST:event_jButtonLinkCustom35ActionPerformed

    private void jButtonLinkCustom36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom36ActionPerformed
        openLinkCustom("Custom.Link.36.Exec");
    }//GEN-LAST:event_jButtonLinkCustom36ActionPerformed

    private void jButtonScriptCustom32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom32ActionPerformed
        openScriptCustom("Custom.Script.32.Exec");
    }//GEN-LAST:event_jButtonScriptCustom32ActionPerformed

    private void jButtonScriptCustom31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom31ActionPerformed
        openScriptCustom("Custom.Script.31.Exec");
    }//GEN-LAST:event_jButtonScriptCustom31ActionPerformed

    private void jButtonScriptCustom34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom34ActionPerformed
        openScriptCustom("Custom.Script.34.Exec");
    }//GEN-LAST:event_jButtonScriptCustom34ActionPerformed

    private void jButtonScriptCustom35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom35ActionPerformed
        openScriptCustom("Custom.Script.35.Exec");
    }//GEN-LAST:event_jButtonScriptCustom35ActionPerformed

    private void jButtonReferenceCustom04ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom04ActionPerformed
        try {
            openReference("Custom.Reference.04");
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonReferenceCustom04ActionPerformed

    private void jButtonReferenceCustom05ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom05ActionPerformed
        try {
            openReference("Custom.Reference.05");
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonReferenceCustom05ActionPerformed

    private void jButtonReferenceCustom06ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom06ActionPerformed
        try {
            openReference("Custom.Reference.06");
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonReferenceCustom06ActionPerformed

    private void jButtonReferenceCustom07ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom07ActionPerformed
        try {
            openReference("Custom.Reference.07");
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonReferenceCustom07ActionPerformed

    private void jButtonReferenceCustom08ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom08ActionPerformed
        try {
            openReference("Custom.Reference.08");
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonReferenceCustom08ActionPerformed

    private void jButtonReferenceCustom09ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom09ActionPerformed
        try {
            openReference("Custom.Reference.09");
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonReferenceCustom09ActionPerformed

    private void jButtonReferenceCustom10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom10ActionPerformed
        try {
            openReference("Custom.Reference.10");
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonReferenceCustom10ActionPerformed

    private void jButtonReferenceCustom11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom11ActionPerformed
        try {
            openReference("Custom.Reference.11");
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonReferenceCustom11ActionPerformed

    private void jButtonReferenceCustom12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom12ActionPerformed
        try {
            openReference("Custom.Reference.12");
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonReferenceCustom12ActionPerformed

    private void jButtonReferenceCustom13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom13ActionPerformed
        try {
            openReference("Custom.Reference.13");
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonReferenceCustom13ActionPerformed

    private void jButtonReferenceCustom14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom14ActionPerformed
        try {
            openReference("Custom.Reference.14");
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonReferenceCustom14ActionPerformed

    private void jButtonReferenceCustom15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom15ActionPerformed
        try {
            openReference("Custom.Reference.15");
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonReferenceCustom15ActionPerformed

    private void jButtonReferenceCustom16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom16ActionPerformed
        try {
            openReference("Custom.Reference.16");
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonReferenceCustom16ActionPerformed

    private void jButtonReferenceCustom17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom17ActionPerformed
        try {
            openReference("Custom.Reference.17");
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonReferenceCustom17ActionPerformed

    private void jButtonReferenceCustom18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom18ActionPerformed
        try {
            openReference("Custom.Reference.18");
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonReferenceCustom18ActionPerformed

    private void jButtonReferenceCustom19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom19ActionPerformed
        try {
            openReference("Custom.Reference.19");
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonReferenceCustom19ActionPerformed

    private void jButtonReferenceCustom20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom20ActionPerformed
        try {
            openReference("Custom.Reference.20");
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonReferenceCustom20ActionPerformed

    private void jButtonReferenceCustom21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom21ActionPerformed
        try {
            openReference("Custom.Reference.21");
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonReferenceCustom21ActionPerformed

    private void jButtonReferenceCustom22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom22ActionPerformed
        try {
            openReference("Custom.Reference.22");
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonReferenceCustom22ActionPerformed

    private void jButtonReferenceCustom23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom23ActionPerformed
        try {
            openReference("Custom.Reference.23");
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonReferenceCustom23ActionPerformed

    private void jButtonReferenceCustom24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom24ActionPerformed
        try {
            openReference("Custom.Reference.24");
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonReferenceCustom24ActionPerformed

    private void jButtonReferenceCustom25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom25ActionPerformed
        try {
            openReference("Custom.Reference.25");
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonReferenceCustom25ActionPerformed

    private void jButtonReferenceCustom26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom26ActionPerformed
        try {
            openReference("Custom.Reference.26");
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonReferenceCustom26ActionPerformed

    private void jButtonReferenceCustom27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom27ActionPerformed
        try {
            openReference("Custom.Reference.27");
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonReferenceCustom27ActionPerformed

    private void jButtonReferenceCustom28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom28ActionPerformed
        try {
            openReference("Custom.Reference.28");
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonReferenceCustom28ActionPerformed

    private void jButtonReferenceCustom29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom29ActionPerformed
        try {
            openReference("Custom.Reference.29");
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonReferenceCustom29ActionPerformed

    private void jButtonReferenceCustom30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom30ActionPerformed
        try {
            openReference("Custom.Reference.30");
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonReferenceCustom30ActionPerformed

    private void jButtonReferenceCustom31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom31ActionPerformed
        try {
            openReference("Custom.Reference.31");
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonReferenceCustom31ActionPerformed

    private void jButtonReferenceCustom32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom32ActionPerformed
        try {
            openReference("Custom.Reference.32");
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonReferenceCustom32ActionPerformed

    private void jButtonReferenceCustom33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom33ActionPerformed
        try {
            openReference("Custom.Reference.33");
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonReferenceCustom33ActionPerformed

    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed
        //- Set Local Policy Warnings
        try {
            if("1".equals(PropertyHandler.getInstance().getValue("Setting.LocalPolicyWarning"))) {
                JOptionPane.showMessageDialog(null, "Data in this folder is overwritten by local policy!","Warning!",JOptionPane.WARNING_MESSAGE);
            } 
        } catch (NullPointerException e) {System.out.println("Setting.LocalPolicyWarning Goofed");
        }       
        openFileUsingDesktop(strPathLaunchPadFolder);
    }//GEN-LAST:event_jButton28ActionPerformed

    private void jButtonScriptCustom03ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom03ActionPerformed
        openScriptCustom("Custom.Script.03.Exec");
    }//GEN-LAST:event_jButtonScriptCustom03ActionPerformed

    private void jButtonScriptCustom06ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom06ActionPerformed
        openScriptCustom("Custom.Script.06.Exec");
    }//GEN-LAST:event_jButtonScriptCustom06ActionPerformed

    private void jButtonScriptCustom30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom30ActionPerformed
        openScriptCustom("Custom.Script.30.Exec");
    }//GEN-LAST:event_jButtonScriptCustom30ActionPerformed

    private void jButtonScriptCustom36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom36ActionPerformed
        openScriptCustom("Custom.Script.36.Exec");
    }//GEN-LAST:event_jButtonScriptCustom36ActionPerformed

    private void jButtonScriptCustom09ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom09ActionPerformed
        openScriptCustom("Custom.Script.09.Exec");
    }//GEN-LAST:event_jButtonScriptCustom09ActionPerformed

    private void jButtonScriptCustom15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom15ActionPerformed
        openScriptCustom("Custom.Script.15.Exec");
    }//GEN-LAST:event_jButtonScriptCustom15ActionPerformed

    private void jButtonScriptCustom18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom18ActionPerformed
        openScriptCustom("Custom.Script.18.Exec");
    }//GEN-LAST:event_jButtonScriptCustom18ActionPerformed

    private void jButtonScriptCustom21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom21ActionPerformed
        openScriptCustom("Custom.Script.21.Exec");
    }//GEN-LAST:event_jButtonScriptCustom21ActionPerformed

    private void jButtonScriptCustom24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom24ActionPerformed
        openScriptCustom("Custom.Script.24.Exec");
    }//GEN-LAST:event_jButtonScriptCustom24ActionPerformed

    private void jButtonScriptCustom27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom27ActionPerformed
        openScriptCustom("Custom.Script.27.Exec");
    }//GEN-LAST:event_jButtonScriptCustom27ActionPerformed

    private void jButtonScriptCustom33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom33ActionPerformed
        openScriptCustom("Custom.Script.33.Exec");
    }//GEN-LAST:event_jButtonScriptCustom33ActionPerformed

    private void jButtonScriptSendMessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptSendMessageActionPerformed
        openEmbeddedPowershellScript("scripts/Powershell-SendMessage.ps1", ".ps1");
    }//GEN-LAST:event_jButtonScriptSendMessageActionPerformed

    private void jButtonScriptTestUDPTCPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptTestUDPTCPActionPerformed
        openEmbeddedPowershellScript("scripts/Powershell-TestTCPorUDP.ps1", ".ps1");
    }//GEN-LAST:event_jButtonScriptTestUDPTCPActionPerformed

    private void jButtonScriptiPerfClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptiPerfClientActionPerformed
        openEmbeddedPowershellScript("scripts/Powershell-iPerfClient.ps1", ".ps1");
    }//GEN-LAST:event_jButtonScriptiPerfClientActionPerformed

    private void jButtonScriptPingLoggerToFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptPingLoggerToFileActionPerformed
        openEmbeddedPowershellScript("scripts/Powershell-PingLoggerToCSV.ps1", ".ps1");
    }//GEN-LAST:event_jButtonScriptPingLoggerToFileActionPerformed

    private void jButtonScriptGetNTPTimePSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptGetNTPTimePSActionPerformed
        openEmbeddedPowershellScript("scripts/Powershell-GetNTPTime.ps1", ".ps1");
    }//GEN-LAST:event_jButtonScriptGetNTPTimePSActionPerformed

    private void jButtonScriptMTUSweepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptMTUSweepActionPerformed
        openEmbeddedPowershellScript("scripts/Powershell-MTUSweep.ps1", ".ps1");
    }//GEN-LAST:event_jButtonScriptMTUSweepActionPerformed

    private void jButtonScriptHashCheckerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptHashCheckerActionPerformed
        openEmbeddedPowershellScript("scripts/Powershell-HashChecker.ps1", ".ps1");
    }//GEN-LAST:event_jButtonScriptHashCheckerActionPerformed

    private void jButtonScriptHashChecker1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptHashChecker1ActionPerformed
        openEmbeddedPowershellScript("scripts/Powershell-PSnmap.ps1", ".ps1");
    }//GEN-LAST:event_jButtonScriptHashChecker1ActionPerformed

    private void jButtonTCPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTCPActionPerformed
        String inputInternalFile = "scripts/Powershell-TestTCP.ps1";
        
        InputStream manualAsStream = getClass().getClassLoader().getResourceAsStream(inputInternalFile);

        Path tempOutput = null;
        try {
            tempOutput = Files.createTempFile("TempFile", ".ps1");
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        tempOutput.toFile().deleteOnExit();

        try {
            Files.copy(manualAsStream, tempOutput, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }

        File fileInTempLocation = new File (tempOutput.toFile().getPath());
        if (fileInTempLocation.exists())
        {
            try {
                //Desktop.getDesktop().open(userManual);
                //String strExec = "cmd /c start powershell.exe -ExecutionPolicy Bypass -noexit -Command \"$host.ui.RawUI.WindowTitle = 'Pinging " + jTextFieldPingHostname.getText() + "' ; ping -t " + StringPingDNS + " " + jTextFieldPingHostname.getText() + " 2>&1 |Foreach{'{0} - {1}' -f (Get-Date -f yyyyMMdd_HHmmss),$_} | select -skip 1 " + StringPingLOG;
                //Runtime.getRuntime().exec(strExec);
                
                Runtime.getRuntime().exec("cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -File \"" + fileInTempLocation + "\" -computername \"" + jTextFieldPingHostname.getText() + "\" -Port " + jTextFieldTCPTestPort.getText());
                
            } catch (IOException ex) {
                Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
      
    }//GEN-LAST:event_jButtonTCPActionPerformed

    private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed
        openFileUsingDesktop(strPathLoggingFolder);
    }//GEN-LAST:event_jButton30ActionPerformed

    private void jButtonSyncStandaloneDevicesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSyncStandaloneDevicesActionPerformed
        openEmbeddedPowershellScript("scripts/Powershell-SyncStandaloneDevices.ps1", ".ps1");
    }//GEN-LAST:event_jButtonSyncStandaloneDevicesActionPerformed

    private void jButtonSyncProductionDevicesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSyncProductionDevicesActionPerformed
        openEmbeddedPowershellScript("scripts/Powershell-SyncProductionDevices.ps1", ".ps1");
    }//GEN-LAST:event_jButtonSyncProductionDevicesActionPerformed

    private void jButtonMapSharedFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMapSharedFolderActionPerformed
        openEmbeddedPowershellScript("scripts/Powershell-MapSharedFolder.ps1", ".ps1");
    }//GEN-LAST:event_jButtonMapSharedFolderActionPerformed

    private void jButtonEditProductionDevicesListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditProductionDevicesListActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonEditProductionDevicesListActionPerformed

    private void jButtonEditProductionDevicesList1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditProductionDevicesList1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonEditProductionDevicesList1ActionPerformed

    private void jTextFieldZipSourceFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldZipSourceFolderActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldZipSourceFolderActionPerformed

    private void jButtonSubnetCalculator1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSubnetCalculator1ActionPerformed
        openTempFileUsingDesktop("html/subnetcalculator/IPv6SubnetCalculator", ".html");
    }//GEN-LAST:event_jButtonSubnetCalculator1ActionPerformed

    private void jButtonIPv4SubnetCalculatorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonIPv4SubnetCalculatorActionPerformed
        openTempFileUsingDesktop("html/subnetcalculator/IPv4SubnetCalculator", ".html");
    }//GEN-LAST:event_jButtonIPv4SubnetCalculatorActionPerformed

    private void jButton35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton35ActionPerformed
        //- Set Local Policy Warnings
        try {
            if("1".equals(PropertyHandler.getInstance().getValue("Setting.LocalPolicyWarning"))) {
                JOptionPane.showMessageDialog(null, "This file is overwritten by local policy!","Warning!",JOptionPane.WARNING_MESSAGE);
            } 
        } catch (NullPointerException e) {System.out.println("Setting.LocalPolicyWarning Goofed");
        } 
        openFileUsingDesktop(strPathPropertiesFile);
    }//GEN-LAST:event_jButton35ActionPerformed

    private void jButtonHashCopyMD5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHashCopyMD5ActionPerformed
        String myString = jTextFieldHashMD5.getText();
        StringSelection stringSelection = new StringSelection(myString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }//GEN-LAST:event_jButtonHashCopyMD5ActionPerformed

    private void jButtonHashCopySHA512ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHashCopySHA512ActionPerformed
        String myString = jTextFieldHashSHA512.getText();
        StringSelection stringSelection = new StringSelection(myString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }//GEN-LAST:event_jButtonHashCopySHA512ActionPerformed

    private void jButtonHashCopySHA1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHashCopySHA1ActionPerformed
        String myString = jTextFieldHashSHA1.getText();
        StringSelection stringSelection = new StringSelection(myString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }//GEN-LAST:event_jButtonHashCopySHA1ActionPerformed

    private void jButtonHashCopySHA256ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHashCopySHA256ActionPerformed
        String myString = jTextFieldHashSHA256.getText();
        StringSelection stringSelection = new StringSelection(myString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }//GEN-LAST:event_jButtonHashCopySHA256ActionPerformed

    private void jButtonGenerateHashMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonGenerateHashMousePressed
        jTextAreaHash.setText("Generating...");
    }//GEN-LAST:event_jButtonGenerateHashMousePressed

    private void jButtonRomajiToHiraKataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRomajiToHiraKataActionPerformed
        openTempFileUsingDesktop("html/romajitohiraganakatakana/RomajiToHiraganaKatakana.html", ".html");
    }//GEN-LAST:event_jButtonRomajiToHiraKataActionPerformed

    private void jButtonRefreshHostnameIPMACActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRefreshHostnameIPMACActionPerformed

    }//GEN-LAST:event_jButtonRefreshHostnameIPMACActionPerformed

    private void jButtonRefreshHostnameIPMACMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonRefreshHostnameIPMACMousePressed
        jLabelLocalHostname.setText("Refreshing");
        jLabelLocalHostname.setToolTipText("Refreshing");
        jLabelLocalIP.setText("Refreshing");
        jLabelLocalMAC.setText("Refreshing");
    }//GEN-LAST:event_jButtonRefreshHostnameIPMACMousePressed

    private void jButton38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton38ActionPerformed
        openFileUsingDesktop(strPathLaunchPadPersistantPropertiesFile);
    }//GEN-LAST:event_jButton38ActionPerformed

    private void jRadioButtonJapaneseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonJapaneseActionPerformed
    PropertyHandlerPersonal.getInstance().setValue("Setting.Language", "Japanese");
    SetLanguageJapanese();
    }//GEN-LAST:event_jRadioButtonJapaneseActionPerformed

    private void jRadioButtonEnglishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonEnglishActionPerformed
    PropertyHandlerPersonal.getInstance().setValue("Setting.Language", "English");
    String ObjButtons[] = {"Yes はい","No いいえ"};
    int PromptResult = JOptionPane.showOptionDialog(null,"Reload in English?\r\n英語でリロードしますか？","",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,null,ObjButtons,ObjButtons[1]);
    if(PromptResult==JOptionPane.YES_OPTION)
    {
        try {
            restartApplication();
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    }//GEN-LAST:event_jRadioButtonEnglishActionPerformed

    private void jTextFieldLinksFilterKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinksFilterKeyReleased
        try {
            searchFilterLinks(jTextFieldLinksFilter.getText());
        } catch (IOException | URISyntaxException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jTextFieldLinksFilterKeyReleased

    private void jTextFieldLinksFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldLinksFilterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldLinksFilterActionPerformed

    private void jTextFieldReferenceFilterKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldReferenceFilterKeyReleased
        try {
            searchFilterReference(jTextFieldReferenceFilter.getText());
        } catch (IOException | URISyntaxException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jTextFieldReferenceFilterKeyReleased

    private void jTextFieldScriptsFilterKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldScriptsFilterKeyReleased
        try {
            searchFilterScripts(jTextFieldScriptsFilter.getText());
        } catch (IOException | URISyntaxException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jTextFieldScriptsFilterKeyReleased

    private void jButtonClearFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClearFilterActionPerformed
        jTextFieldFilter.setText("");
        jTextFieldFilter.requestFocus();
        try {
            Robot robot = new Robot();
            robot.keyRelease(KeyEvent.VK_BACK_SPACE);
        } catch (AWTException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_jButtonClearFilterActionPerformed

    private void jTextFieldButtonToolTip1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonToolTip1KeyReleased
        PropertyHandler.getInstance().setValue("Button.01.ToolTip",jTextFieldButtonToolTip1.getText());
    }//GEN-LAST:event_jTextFieldButtonToolTip1KeyReleased

    private void jTextFieldButtonExecute1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonExecute1KeyReleased
        PropertyHandler.getInstance().setValue("Button.01.StrExec",jTextFieldButtonExecute1.getText() );
    }//GEN-LAST:event_jTextFieldButtonExecute1KeyReleased

    private void jComboBoxButtonIcon1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxButtonIcon1ItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            PropertyHandler.getInstance().setValue("Button.01.Icon",((String)jComboBoxButtonIcon1.getSelectedItem()));
        }
    }//GEN-LAST:event_jComboBoxButtonIcon1ItemStateChanged

    private void jTextFieldButtonExecute2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonExecute2KeyReleased
        PropertyHandler.getInstance().setValue("Button.02.StrExec",jTextFieldButtonExecute2.getText() );
    }//GEN-LAST:event_jTextFieldButtonExecute2KeyReleased

    private void jTextFieldButtonToolTip2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonToolTip2KeyReleased
        PropertyHandler.getInstance().setValue("Button.02.ToolTip",jTextFieldButtonToolTip2.getText());
    }//GEN-LAST:event_jTextFieldButtonToolTip2KeyReleased

    private void jComboBoxButtonIcon2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxButtonIcon2ItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            PropertyHandler.getInstance().setValue("Button.02.Icon",((String)jComboBoxButtonIcon2.getSelectedItem()));
        }
    }//GEN-LAST:event_jComboBoxButtonIcon2ItemStateChanged

    private void jTextFieldButtonExecute3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonExecute3KeyReleased
        PropertyHandler.getInstance().setValue("Button.03.StrExec",jTextFieldButtonExecute3.getText() );
    }//GEN-LAST:event_jTextFieldButtonExecute3KeyReleased

    private void jTextFieldButtonToolTip3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonToolTip3KeyReleased
        PropertyHandler.getInstance().setValue("Button.03.ToolTip",jTextFieldButtonToolTip3.getText());
    }//GEN-LAST:event_jTextFieldButtonToolTip3KeyReleased

    private void jComboBoxButtonIcon3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxButtonIcon3ItemStateChanged
       if (evt.getStateChange() == ItemEvent.SELECTED) {
            PropertyHandler.getInstance().setValue("Button.03.Icon",((String)jComboBoxButtonIcon3.getSelectedItem()));
       }
    }//GEN-LAST:event_jComboBoxButtonIcon3ItemStateChanged

    private void jTextFieldButtonExecute4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonExecute4KeyReleased
        PropertyHandler.getInstance().setValue("Button.04.StrExec",jTextFieldButtonExecute4.getText() );
    }//GEN-LAST:event_jTextFieldButtonExecute4KeyReleased

    private void jTextFieldButtonToolTip4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonToolTip4KeyReleased
        PropertyHandler.getInstance().setValue("Button.04.ToolTip",jTextFieldButtonToolTip4.getText());
    }//GEN-LAST:event_jTextFieldButtonToolTip4KeyReleased

    private void jComboBoxButtonIcon4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxButtonIcon4ItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            PropertyHandler.getInstance().setValue("Button.04.Icon",((String)jComboBoxButtonIcon4.getSelectedItem()));
        }
    }//GEN-LAST:event_jComboBoxButtonIcon4ItemStateChanged

    private void jTextFieldButtonExecute5KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonExecute5KeyReleased
        PropertyHandler.getInstance().setValue("Button.05.StrExec",jTextFieldButtonExecute5.getText() );
    }//GEN-LAST:event_jTextFieldButtonExecute5KeyReleased

    private void jTextFieldButtonToolTip5KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonToolTip5KeyReleased
        PropertyHandler.getInstance().setValue("Button.05.ToolTip",jTextFieldButtonToolTip5.getText());
    }//GEN-LAST:event_jTextFieldButtonToolTip5KeyReleased

    private void jComboBoxButtonIcon5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxButtonIcon5ItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            PropertyHandler.getInstance().setValue("Button.05.Icon",((String)jComboBoxButtonIcon5.getSelectedItem()));
        }
    }//GEN-LAST:event_jComboBoxButtonIcon5ItemStateChanged

    private void jTextFieldButtonExecute6KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonExecute6KeyReleased
        PropertyHandler.getInstance().setValue("Button.06.StrExec",jTextFieldButtonExecute6.getText() );
    }//GEN-LAST:event_jTextFieldButtonExecute6KeyReleased

    private void jTextFieldButtonToolTip6KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonToolTip6KeyReleased
        PropertyHandler.getInstance().setValue("Button.06.ToolTip",jTextFieldButtonToolTip6.getText());
    }//GEN-LAST:event_jTextFieldButtonToolTip6KeyReleased

    private void jComboBoxButtonIcon6ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxButtonIcon6ItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            PropertyHandler.getInstance().setValue("Button.06.Icon",((String)jComboBoxButtonIcon6.getSelectedItem()));
        }
    }//GEN-LAST:event_jComboBoxButtonIcon6ItemStateChanged

    private void jTextFieldButtonExecute7KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonExecute7KeyReleased
        PropertyHandler.getInstance().setValue("Button.07.StrExec",jTextFieldButtonExecute7.getText() );
    }//GEN-LAST:event_jTextFieldButtonExecute7KeyReleased

    private void jTextFieldButtonToolTip7KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonToolTip7KeyReleased
        PropertyHandler.getInstance().setValue("Button.07.ToolTip",jTextFieldButtonToolTip7.getText());
    }//GEN-LAST:event_jTextFieldButtonToolTip7KeyReleased

    private void jComboBoxButtonIcon7ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxButtonIcon7ItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            PropertyHandler.getInstance().setValue("Button.07.Icon",((String)jComboBoxButtonIcon7.getSelectedItem()));
        }
    }//GEN-LAST:event_jComboBoxButtonIcon7ItemStateChanged

    private void jTextFieldButtonExecute8KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonExecute8KeyReleased
        PropertyHandler.getInstance().setValue("Button.08.StrExec",jTextFieldButtonExecute8.getText() );
    }//GEN-LAST:event_jTextFieldButtonExecute8KeyReleased

    private void jTextFieldButtonToolTip8KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonToolTip8KeyReleased
        PropertyHandler.getInstance().setValue("Button.08.ToolTip",jTextFieldButtonToolTip8.getText());
    }//GEN-LAST:event_jTextFieldButtonToolTip8KeyReleased

    private void jComboBoxButtonIcon8ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxButtonIcon8ItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            PropertyHandler.getInstance().setValue("Button.08.Icon",((String)jComboBoxButtonIcon8.getSelectedItem()));
        }
    }//GEN-LAST:event_jComboBoxButtonIcon8ItemStateChanged

    private void jComboBoxButtonIcon9ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxButtonIcon9ItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            PropertyHandler.getInstance().setValue("Button.09.Icon",((String)jComboBoxButtonIcon9.getSelectedItem()));
        }
    }//GEN-LAST:event_jComboBoxButtonIcon9ItemStateChanged

    private void jTextFieldButtonExecute9KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonExecute9KeyReleased
        PropertyHandler.getInstance().setValue("Button.09.StrExec",jTextFieldButtonExecute9.getText() );
    }//GEN-LAST:event_jTextFieldButtonExecute9KeyReleased

    private void jTextFieldButtonToolTip9KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonToolTip9KeyReleased
        PropertyHandler.getInstance().setValue("Button.09.ToolTip",jTextFieldButtonToolTip9.getText());
    }//GEN-LAST:event_jTextFieldButtonToolTip9KeyReleased

    private void jComboBoxButtonIcon10ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxButtonIcon10ItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            PropertyHandler.getInstance().setValue("Button.10.Icon",((String)jComboBoxButtonIcon10.getSelectedItem()));
        }
    }//GEN-LAST:event_jComboBoxButtonIcon10ItemStateChanged

    private void jTextFieldButtonExecute10KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonExecute10KeyReleased
        PropertyHandler.getInstance().setValue("Button.10.StrExec",jTextFieldButtonExecute10.getText() );
    }//GEN-LAST:event_jTextFieldButtonExecute10KeyReleased

    private void jTextFieldButtonToolTip10KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonToolTip10KeyReleased
        PropertyHandler.getInstance().setValue("Button.10.ToolTip",jTextFieldButtonToolTip10.getText());
    }//GEN-LAST:event_jTextFieldButtonToolTip10KeyReleased

    private void jComboBoxButtonIcon11ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxButtonIcon11ItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            PropertyHandler.getInstance().setValue("Button.11.Icon",((String)jComboBoxButtonIcon11.getSelectedItem()));
        }
    }//GEN-LAST:event_jComboBoxButtonIcon11ItemStateChanged

    private void jTextFieldButtonExecute11KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonExecute11KeyReleased
        PropertyHandler.getInstance().setValue("Button.11.StrExec",jTextFieldButtonExecute11.getText() );
    }//GEN-LAST:event_jTextFieldButtonExecute11KeyReleased

    private void jTextFieldButtonToolTip11KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonToolTip11KeyReleased
        PropertyHandler.getInstance().setValue("Button.11.ToolTip",jTextFieldButtonToolTip11.getText());
    }//GEN-LAST:event_jTextFieldButtonToolTip11KeyReleased

    private void jComboBoxButtonIcon12ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxButtonIcon12ItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            PropertyHandler.getInstance().setValue("Button.12.Icon",((String)jComboBoxButtonIcon12.getSelectedItem()));
        }
    }//GEN-LAST:event_jComboBoxButtonIcon12ItemStateChanged

    private void jTextFieldButtonExecute12KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonExecute12KeyReleased
        PropertyHandler.getInstance().setValue("Button.12.StrExec",jTextFieldButtonExecute12.getText() );
    }//GEN-LAST:event_jTextFieldButtonExecute12KeyReleased

    private void jTextFieldButtonToolTip12KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonToolTip12KeyReleased
        PropertyHandler.getInstance().setValue("Button.12.ToolTip",jTextFieldButtonToolTip12.getText());
    }//GEN-LAST:event_jTextFieldButtonToolTip12KeyReleased

    private void jComboBoxButtonIcon13ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxButtonIcon13ItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            PropertyHandler.getInstance().setValue("Button.13.Icon",((String)jComboBoxButtonIcon13.getSelectedItem()));
        }
    }//GEN-LAST:event_jComboBoxButtonIcon13ItemStateChanged

    private void jTextFieldButtonExecute13KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonExecute13KeyReleased
        PropertyHandler.getInstance().setValue("Button.13.StrExec",jTextFieldButtonExecute13.getText() );
    }//GEN-LAST:event_jTextFieldButtonExecute13KeyReleased

    private void jTextFieldButtonToolTip13KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonToolTip13KeyReleased
        PropertyHandler.getInstance().setValue("Button.13.ToolTip",jTextFieldButtonToolTip13.getText());
    }//GEN-LAST:event_jTextFieldButtonToolTip13KeyReleased

    private void jComboBoxButtonIcon14ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxButtonIcon14ItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            PropertyHandler.getInstance().setValue("Button.14.Icon",((String)jComboBoxButtonIcon14.getSelectedItem()));
        }
    }//GEN-LAST:event_jComboBoxButtonIcon14ItemStateChanged

    private void jTextFieldButtonExecute14KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonExecute14KeyReleased
        PropertyHandler.getInstance().setValue("Button.14.StrExec",jTextFieldButtonExecute14.getText() );
    }//GEN-LAST:event_jTextFieldButtonExecute14KeyReleased

    private void jTextFieldButtonToolTip14KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonToolTip14KeyReleased
        PropertyHandler.getInstance().setValue("Button.14.ToolTip",jTextFieldButtonToolTip14.getText());
    }//GEN-LAST:event_jTextFieldButtonToolTip14KeyReleased

    private void jComboBoxButtonIcon15ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxButtonIcon15ItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            PropertyHandler.getInstance().setValue("Button.15.Icon",((String)jComboBoxButtonIcon15.getSelectedItem()));
        }
    }//GEN-LAST:event_jComboBoxButtonIcon15ItemStateChanged

    private void jTextFieldButtonExecute15KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonExecute15KeyReleased
        PropertyHandler.getInstance().setValue("Button.15.StrExec",jTextFieldButtonExecute15.getText() );
    }//GEN-LAST:event_jTextFieldButtonExecute15KeyReleased

    private void jTextFieldButtonToolTip15KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonToolTip15KeyReleased
        PropertyHandler.getInstance().setValue("Button.15.ToolTip",jTextFieldButtonToolTip15.getText());
    }//GEN-LAST:event_jTextFieldButtonToolTip15KeyReleased

    private void jComboBoxButtonIcon16ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxButtonIcon16ItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            PropertyHandler.getInstance().setValue("Button.16.Icon",((String)jComboBoxButtonIcon16.getSelectedItem()));
        }
    }//GEN-LAST:event_jComboBoxButtonIcon16ItemStateChanged

    private void jTextFieldButtonExecute16KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonExecute16KeyReleased
        PropertyHandler.getInstance().setValue("Button.16.StrExec",jTextFieldButtonExecute16.getText() );
    }//GEN-LAST:event_jTextFieldButtonExecute16KeyReleased

    private void jTextFieldButtonToolTip16KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonToolTip16KeyReleased
        PropertyHandler.getInstance().setValue("Button.16.ToolTip",jTextFieldButtonToolTip16.getText());
    }//GEN-LAST:event_jTextFieldButtonToolTip16KeyReleased

    private void jComboBoxButtonIcon17ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxButtonIcon17ItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            PropertyHandler.getInstance().setValue("Button.17.Icon",((String)jComboBoxButtonIcon17.getSelectedItem()));
        }
    }//GEN-LAST:event_jComboBoxButtonIcon17ItemStateChanged

    private void jTextFieldButtonExecute17KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonExecute17KeyReleased
        PropertyHandler.getInstance().setValue("Button.17.StrExec",jTextFieldButtonExecute17.getText() );
    }//GEN-LAST:event_jTextFieldButtonExecute17KeyReleased

    private void jTextFieldButtonToolTip17KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonToolTip17KeyReleased
        PropertyHandler.getInstance().setValue("Button.17.ToolTip",jTextFieldButtonToolTip17.getText());
    }//GEN-LAST:event_jTextFieldButtonToolTip17KeyReleased

    private void jComboBoxButtonIcon18ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxButtonIcon18ItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            PropertyHandler.getInstance().setValue("Button.18.Icon",((String)jComboBoxButtonIcon18.getSelectedItem()));
        }
    }//GEN-LAST:event_jComboBoxButtonIcon18ItemStateChanged

    private void jTextFieldButtonExecute18KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonExecute18KeyReleased
        PropertyHandler.getInstance().setValue("Button.18.StrExec",jTextFieldButtonExecute18.getText() );
    }//GEN-LAST:event_jTextFieldButtonExecute18KeyReleased

    private void jTextFieldButtonToolTip18KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonToolTip18KeyReleased
        PropertyHandler.getInstance().setValue("Button.18.ToolTip",jTextFieldButtonToolTip18.getText());
    }//GEN-LAST:event_jTextFieldButtonToolTip18KeyReleased

    private void jComboBoxButtonIcon19ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxButtonIcon19ItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            PropertyHandler.getInstance().setValue("Button.19.Icon",((String)jComboBoxButtonIcon19.getSelectedItem()));
        }
    }//GEN-LAST:event_jComboBoxButtonIcon19ItemStateChanged

    private void jTextFieldButtonExecute19KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonExecute19KeyReleased
        PropertyHandler.getInstance().setValue("Button.19.StrExec",jTextFieldButtonExecute19.getText() );
    }//GEN-LAST:event_jTextFieldButtonExecute19KeyReleased

    private void jTextFieldButtonToolTip19KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonToolTip19KeyReleased
        PropertyHandler.getInstance().setValue("Button.19.ToolTip",jTextFieldButtonToolTip19.getText());
    }//GEN-LAST:event_jTextFieldButtonToolTip19KeyReleased

    private void jComboBoxButtonIcon20ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxButtonIcon20ItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            PropertyHandler.getInstance().setValue("Button.20.Icon",((String)jComboBoxButtonIcon20.getSelectedItem())); 
        }
    }//GEN-LAST:event_jComboBoxButtonIcon20ItemStateChanged

    private void jTextFieldButtonExecute20KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonExecute20KeyReleased
        PropertyHandler.getInstance().setValue("Button.20.StrExec",jTextFieldButtonExecute20.getText() );
    }//GEN-LAST:event_jTextFieldButtonExecute20KeyReleased

    private void jTextFieldButtonToolTip20KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonToolTip20KeyReleased
        PropertyHandler.getInstance().setValue("Button.20.ToolTip",jTextFieldButtonToolTip20.getText());
    }//GEN-LAST:event_jTextFieldButtonToolTip20KeyReleased

    private void jButton40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton40ActionPerformed
        try {
            restartApplication();
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton40ActionPerformed

    private void jButton42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton42ActionPerformed
        openTempFileUsingDesktop("files/ToolBox.xlsm", ".xlsm");
    }//GEN-LAST:event_jButton42ActionPerformed

    private void jButtonExecuteFunctionPKIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExecuteFunctionPKIActionPerformed
        String strSecureCRTexe = PropertyHandler.getInstance().getValue("SecureCRT.Exec").replace("%USERPROFILE%", pathUserProfile);
        System.out.println("SecureCRT file: " + strSecureCRTexe);
        simpleDateFormat  = new SimpleDateFormat("yyyy-MM-dd_HHmm-ssSSS");
        dateTime = simpleDateFormat.format(new Date());
        String fileLog = pathDesktop + "\\Logging-Output\\SSH-" + jTextFieldConnectHostname.getText() + " " + dateTime + ".txt";
        System.out.println("Log file: " + fileLog);


        String strEXEC = strSecureCRTexe + " /LOG \"" + fileLog + "\" /T /SSH2 /ACCEPTHOSTKEYS /AUTH publickey " + jTextFieldConnectHostname.getText();
        System.out.println(strEXEC);
        try {
            Runtime.getRuntime().exec(strEXEC);
        }
        catch (IOException e) {
            System.out.println("Something is wrong!");
            JOptionPane.showMessageDialog(null, "Something is wrong!");
        }


    }//GEN-LAST:event_jButtonExecuteFunctionPKIActionPerformed

    private void jRadioButtonPWauthDisabledActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonPWauthDisabledActionPerformed
        PropertyHandler.getInstance().setValue("Setting.PasswordBasedSSHauthDisable", "1");
        jRadioButtonPWauthDisabled.setSelected(Boolean.TRUE);
        jButtonExecuteFunctionSSH.setBackground(new javax.swing.JButton().getBackground());
        jButtonExecuteFunctionSSH.setEnabled(false);
    }//GEN-LAST:event_jRadioButtonPWauthDisabledActionPerformed

    private void jRadioButtonPWauthEnabledActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonPWauthEnabledActionPerformed
        PropertyHandler.getInstance().setValue("Setting.PasswordBasedSSHauthDisable", "0");
        jRadioButtonPWauthEnabled.setSelected(Boolean.TRUE);
        jButtonExecuteFunctionSSH.setBackground(new Color(200,255,153));
        jButtonExecuteFunctionSSH.setEnabled(Boolean.TRUE);
    }//GEN-LAST:event_jRadioButtonPWauthEnabledActionPerformed

    private void jButton43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton43ActionPerformed
        openFileUsingDesktop(strPathLaunchPadPersistantUserFolder);
    }//GEN-LAST:event_jButton43ActionPerformed

    private void jTextFieldLinkText1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkText1KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.01.Description",jTextFieldLinkText1.getText());
    }//GEN-LAST:event_jTextFieldLinkText1KeyReleased

    private void jTextFieldLinkExecute1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkExecute1KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.01.Exec",jTextFieldLinkExecute1.getText());
    }//GEN-LAST:event_jTextFieldLinkExecute1KeyReleased

    private void jButton44ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton44ActionPerformed
        try {
            restartApplication();
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton44ActionPerformed

    private void jTextFieldLinkText2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkText2KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.02.Description",jTextFieldLinkText2.getText());
    }//GEN-LAST:event_jTextFieldLinkText2KeyReleased

    private void jTextFieldLinkExecute2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkExecute2KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.02.Exec",jTextFieldLinkExecute2.getText());
    }//GEN-LAST:event_jTextFieldLinkExecute2KeyReleased

    private void jTextFieldLinkExecute3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkExecute3KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.03.Exec",jTextFieldLinkExecute3.getText());
    }//GEN-LAST:event_jTextFieldLinkExecute3KeyReleased

    private void jTextFieldLinkText3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkText3KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.03.Description",jTextFieldLinkText3.getText());
    }//GEN-LAST:event_jTextFieldLinkText3KeyReleased

    private void jTextFieldLinkExecute4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkExecute4KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.04.Exec",jTextFieldLinkExecute4.getText());
    }//GEN-LAST:event_jTextFieldLinkExecute4KeyReleased

    private void jTextFieldLinkText4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkText4KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.04.Description",jTextFieldLinkText4.getText());
    }//GEN-LAST:event_jTextFieldLinkText4KeyReleased

    private void jTextFieldLinkExecute5KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkExecute5KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.05.Exec",jTextFieldLinkExecute5.getText());
    }//GEN-LAST:event_jTextFieldLinkExecute5KeyReleased

    private void jTextFieldLinkText5KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkText5KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.05.Description",jTextFieldLinkText5.getText());
    }//GEN-LAST:event_jTextFieldLinkText5KeyReleased

    private void jTextFieldLinkExecute6KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkExecute6KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.06.Exec",jTextFieldLinkExecute6.getText());
    }//GEN-LAST:event_jTextFieldLinkExecute6KeyReleased

    private void jTextFieldLinkText6KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkText6KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.06.Description",jTextFieldLinkText6.getText());
    }//GEN-LAST:event_jTextFieldLinkText6KeyReleased

    private void jTextFieldLinkExecute7KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkExecute7KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.07.Exec",jTextFieldLinkExecute7.getText());
    }//GEN-LAST:event_jTextFieldLinkExecute7KeyReleased

    private void jTextFieldLinkText7KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkText7KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.07.Description",jTextFieldLinkText7.getText());
    }//GEN-LAST:event_jTextFieldLinkText7KeyReleased

    private void jTextFieldLinkExecute8KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkExecute8KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.08.Exec",jTextFieldLinkExecute8.getText());
    }//GEN-LAST:event_jTextFieldLinkExecute8KeyReleased

    private void jTextFieldLinkText8KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkText8KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.08.Description",jTextFieldLinkText8.getText());
    }//GEN-LAST:event_jTextFieldLinkText8KeyReleased

    private void jTextFieldLinkExecute9KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkExecute9KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.09.Exec",jTextFieldLinkExecute9.getText());
    }//GEN-LAST:event_jTextFieldLinkExecute9KeyReleased

    private void jTextFieldLinkText9KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkText9KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.09.Description",jTextFieldLinkText9.getText());
    }//GEN-LAST:event_jTextFieldLinkText9KeyReleased

    private void jTextFieldLinkExecute10KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkExecute10KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.10.Exec",jTextFieldLinkExecute10.getText());
    }//GEN-LAST:event_jTextFieldLinkExecute10KeyReleased

    private void jTextFieldLinkText10KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkText10KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.10.Description",jTextFieldLinkText10.getText());
    }//GEN-LAST:event_jTextFieldLinkText10KeyReleased

    private void jTextFieldLinkExecute11KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkExecute11KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.11.Exec",jTextFieldLinkExecute11.getText());
    }//GEN-LAST:event_jTextFieldLinkExecute11KeyReleased

    private void jTextFieldLinkText11KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkText11KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.11.Description",jTextFieldLinkText11.getText());
    }//GEN-LAST:event_jTextFieldLinkText11KeyReleased

    private void jTextFieldLinkExecute12KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkExecute12KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.12.Exec", jTextFieldLinkExecute12.getText());
    }//GEN-LAST:event_jTextFieldLinkExecute12KeyReleased

    private void jTextFieldLinkText12KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkText12KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.12.Description",jTextFieldLinkText12.getText());
    }//GEN-LAST:event_jTextFieldLinkText12KeyReleased

    private void jTextFieldLinkExecute13KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkExecute13KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.13.Exec",jTextFieldLinkExecute13.getText());
    }//GEN-LAST:event_jTextFieldLinkExecute13KeyReleased

    private void jTextFieldLinkText13KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkText13KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.13.Description",jTextFieldLinkText13.getText());
    }//GEN-LAST:event_jTextFieldLinkText13KeyReleased

    private void jTextFieldLinkExecute14KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkExecute14KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.14.Exec",jTextFieldLinkExecute14.getText());
    }//GEN-LAST:event_jTextFieldLinkExecute14KeyReleased

    private void jTextFieldLinkText14KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkText14KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.14.Description",jTextFieldLinkText14.getText());
    }//GEN-LAST:event_jTextFieldLinkText14KeyReleased

    private void jTextFieldLinkExecute15KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkExecute15KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.15.Exec",jTextFieldLinkExecute15.getText());
    }//GEN-LAST:event_jTextFieldLinkExecute15KeyReleased

    private void jTextFieldLinkText15KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkText15KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.15.Description",jTextFieldLinkText15.getText());
    }//GEN-LAST:event_jTextFieldLinkText15KeyReleased

    private void jTextFieldLinkExecute16KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkExecute16KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.16.Exec",jTextFieldLinkExecute16.getText());
    }//GEN-LAST:event_jTextFieldLinkExecute16KeyReleased

    private void jTextFieldLinkText16KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkText16KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.16.Description",jTextFieldLinkText16.getText());
    }//GEN-LAST:event_jTextFieldLinkText16KeyReleased

    private void jTextFieldLinkExecute17KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkExecute17KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.17.Exec",jTextFieldLinkExecute17.getText());
    }//GEN-LAST:event_jTextFieldLinkExecute17KeyReleased

    private void jTextFieldLinkText17KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkText17KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.17.Description",jTextFieldLinkText17.getText());
    }//GEN-LAST:event_jTextFieldLinkText17KeyReleased

    private void jTextFieldLinkExecute18KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkExecute18KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.18.Exec",jTextFieldLinkExecute18.getText());
    }//GEN-LAST:event_jTextFieldLinkExecute18KeyReleased

    private void jTextFieldLinkText18KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkText18KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.18.Description",jTextFieldLinkText18.getText());
    }//GEN-LAST:event_jTextFieldLinkText18KeyReleased

    private void jTextFieldLinkExecute19KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkExecute19KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.19.Exec",jTextFieldLinkExecute19.getText());
    }//GEN-LAST:event_jTextFieldLinkExecute19KeyReleased

    private void jTextFieldLinkText19KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkText19KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.19.Description",jTextFieldLinkText19.getText());
    }//GEN-LAST:event_jTextFieldLinkText19KeyReleased

    private void jTextFieldLinkExecute20KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkExecute20KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.20.Exec",jTextFieldLinkExecute20.getText());
    }//GEN-LAST:event_jTextFieldLinkExecute20KeyReleased

    private void jTextFieldLinkText20KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkText20KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.20.Description",jTextFieldLinkText20.getText());
    }//GEN-LAST:event_jTextFieldLinkText20KeyReleased

    private void jTextFieldLinkExecute21KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkExecute21KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.21.Exec",jTextFieldLinkExecute21.getText());
    }//GEN-LAST:event_jTextFieldLinkExecute21KeyReleased

    private void jTextFieldLinkText21KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkText21KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.21.Description",jTextFieldLinkText21.getText());
    }//GEN-LAST:event_jTextFieldLinkText21KeyReleased

    private void jTextFieldLinkExecute22KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkExecute22KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.22.Exec",jTextFieldLinkExecute22.getText());
    }//GEN-LAST:event_jTextFieldLinkExecute22KeyReleased

    private void jTextFieldLinkText22KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkText22KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.22.Description",jTextFieldLinkText22.getText());
    }//GEN-LAST:event_jTextFieldLinkText22KeyReleased

    private void jTextFieldLinkExecute23KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkExecute23KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.23.Exec",jTextFieldLinkExecute23.getText());
    }//GEN-LAST:event_jTextFieldLinkExecute23KeyReleased

    private void jTextFieldLinkText23KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkText23KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.23.Description",jTextFieldLinkText23.getText());
    }//GEN-LAST:event_jTextFieldLinkText23KeyReleased

    private void jTextFieldLinkExecute24KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkExecute24KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.24.Exec",jTextFieldLinkExecute24.getText());
    }//GEN-LAST:event_jTextFieldLinkExecute24KeyReleased

    private void jTextFieldLinkText24KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkText24KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.24.Description",jTextFieldLinkText24.getText());
    }//GEN-LAST:event_jTextFieldLinkText24KeyReleased

    private void jTextFieldLinkExecute25KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkExecute25KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.25.Exec",jTextFieldLinkExecute25.getText());
    }//GEN-LAST:event_jTextFieldLinkExecute25KeyReleased

    private void jTextFieldLinkText25KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkText25KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.25.Description",jTextFieldLinkText25.getText());
    }//GEN-LAST:event_jTextFieldLinkText25KeyReleased

    private void jTextFieldLinkExecute26KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkExecute26KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.26.Exec",jTextFieldLinkExecute26.getText());
    }//GEN-LAST:event_jTextFieldLinkExecute26KeyReleased

    private void jTextFieldLinkText26KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkText26KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.26.Description",jTextFieldLinkText26.getText());
    }//GEN-LAST:event_jTextFieldLinkText26KeyReleased

    private void jTextFieldLinkExecute27KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkExecute27KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.27.Exec",jTextFieldLinkExecute27.getText());
    }//GEN-LAST:event_jTextFieldLinkExecute27KeyReleased

    private void jTextFieldLinkText27KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkText27KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.27.Description",jTextFieldLinkText27.getText());
    }//GEN-LAST:event_jTextFieldLinkText27KeyReleased

    private void jTextFieldLinkExecute28KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkExecute28KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.28.Exec",jTextFieldLinkExecute28.getText());
    }//GEN-LAST:event_jTextFieldLinkExecute28KeyReleased

    private void jTextFieldLinkText28KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkText28KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.28.Description",jTextFieldLinkText28.getText());
    }//GEN-LAST:event_jTextFieldLinkText28KeyReleased

    private void jTextFieldLinkExecute29KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkExecute29KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.29.Exec",jTextFieldLinkExecute29.getText());
    }//GEN-LAST:event_jTextFieldLinkExecute29KeyReleased

    private void jTextFieldLinkText29KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkText29KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.29.Description",jTextFieldLinkText29.getText());
    }//GEN-LAST:event_jTextFieldLinkText29KeyReleased

    private void jTextFieldLinkExecute30KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkExecute30KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.30.Exec",jTextFieldLinkExecute30.getText());
    }//GEN-LAST:event_jTextFieldLinkExecute30KeyReleased

    private void jTextFieldLinkText30KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkText30KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.30.Description",jTextFieldLinkText30.getText());
    }//GEN-LAST:event_jTextFieldLinkText30KeyReleased

    private void jTextFieldLinkExecute31KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkExecute31KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.31.Exec",jTextFieldLinkExecute31.getText());
    }//GEN-LAST:event_jTextFieldLinkExecute31KeyReleased

    private void jTextFieldLinkText31KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkText31KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.31.Description",jTextFieldLinkText31.getText());
    }//GEN-LAST:event_jTextFieldLinkText31KeyReleased

    private void jTextFieldLinkExecute32KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkExecute32KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.32.Exec",jTextFieldLinkExecute32.getText());
    }//GEN-LAST:event_jTextFieldLinkExecute32KeyReleased

    private void jTextFieldLinkText32KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkText32KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.32.Description",jTextFieldLinkText32.getText());
    }//GEN-LAST:event_jTextFieldLinkText32KeyReleased

    private void jTextFieldLinkExecute33KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkExecute33KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.33.Exec",jTextFieldLinkExecute33.getText());
    }//GEN-LAST:event_jTextFieldLinkExecute33KeyReleased

    private void jTextFieldLinkText33KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkText33KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.33.Description",jTextFieldLinkText33.getText());
    }//GEN-LAST:event_jTextFieldLinkText33KeyReleased

    private void jTextFieldLinkExecute34KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkExecute34KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.34.Exec",jTextFieldLinkExecute34.getText());
    }//GEN-LAST:event_jTextFieldLinkExecute34KeyReleased

    private void jTextFieldLinkText34KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkText34KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.34.Description",jTextFieldLinkText34.getText());
    }//GEN-LAST:event_jTextFieldLinkText34KeyReleased

    private void jTextFieldLinkExecute35KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkExecute35KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.35.Exec",jTextFieldLinkExecute35.getText());
    }//GEN-LAST:event_jTextFieldLinkExecute35KeyReleased

    private void jTextFieldLinkText35KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkText35KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.35.Description",jTextFieldLinkText35.getText());
    }//GEN-LAST:event_jTextFieldLinkText35KeyReleased

    private void jTextFieldLinkExecute36KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkExecute36KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.36.Exec",jTextFieldLinkExecute36.getText());
    }//GEN-LAST:event_jTextFieldLinkExecute36KeyReleased

    private void jTextFieldLinkText36KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkText36KeyReleased
        PropertyHandler.getInstance().setValue("Custom.Link.36.Description",jTextFieldLinkText36.getText());
    }//GEN-LAST:event_jTextFieldLinkText36KeyReleased

    private void jTextFieldLinkText1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldLinkText1ActionPerformed
    }//GEN-LAST:event_jTextFieldLinkText1ActionPerformed

    private void jButtonRefreshHostnameIPMACMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonRefreshHostnameIPMACMouseReleased
        //- Get Hostname IP and MAC
        try {
            String systemname = getSystemName();
            jLabelLocalHostname.setText(systemname);
            jLabelLocalHostname.setToolTipText(systemname);
            jLabelLocalIP.setText(getIPAddress());
            jLabelLocalMAC.setText(getMAC());
        } catch (SocketException | UnknownHostException e) {
        }

    }//GEN-LAST:event_jButtonRefreshHostnameIPMACMouseReleased

    private void jComboBoxClassificationItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxClassificationItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            PropertyHandler.getInstance().setValue("Setting.Classification",((String)jComboBoxClassification.getSelectedItem()));
            loadClassification();
        }
    }//GEN-LAST:event_jComboBoxClassificationItemStateChanged

    private void jTextFieldSecureCRTPathKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldSecureCRTPathKeyReleased
        PropertyHandler.getInstance().setValue("SecureCRT.Exec",jTextFieldSecureCRTPath.getText() );
    }//GEN-LAST:event_jTextFieldSecureCRTPathKeyReleased

    private void jTextFieldPreloadedIPKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldPreloadedIPKeyReleased
        PropertyHandler.getInstance().setValue("Preload.SSH",jTextFieldPreloadedIP.getText() );
    }//GEN-LAST:event_jTextFieldPreloadedIPKeyReleased

    private void jTextFieldPreloadedPingIPKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldPreloadedPingIPKeyReleased
        PropertyHandler.getInstance().setValue("Preload.Ping",jTextFieldPreloadedPingIP.getText() );
    }//GEN-LAST:event_jTextFieldPreloadedPingIPKeyReleased

    private void jComboBoxSessionDoubleClickItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxSessionDoubleClickItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            PropertyHandler.getInstance().setValue("Button.ExecuteFunctionDoubleClick",((String)jComboBoxSessionDoubleClick.getSelectedItem()));
            loadClassification();
        }
    }//GEN-LAST:event_jComboBoxSessionDoubleClickItemStateChanged

    private void jButtonMainButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMainButton24ActionPerformed
        openCommand("Button.24.StrExec");
    }//GEN-LAST:event_jButtonMainButton24ActionPerformed

    private void jButtonMainButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMainButton23ActionPerformed
        openCommand("Button.23.StrExec");
    }//GEN-LAST:event_jButtonMainButton23ActionPerformed

    private void jButtonMainButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMainButton22ActionPerformed
        openCommand("Button.22.StrExec");
    }//GEN-LAST:event_jButtonMainButton22ActionPerformed

    private void jButtonMainButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMainButton21ActionPerformed
        openCommand("Button.21.StrExec");
    }//GEN-LAST:event_jButtonMainButton21ActionPerformed

    private void jComboBoxButtonIcon21ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxButtonIcon21ItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            PropertyHandler.getInstance().setValue("Button.21.Icon",((String)jComboBoxButtonIcon21.getSelectedItem())); 
        }
    }//GEN-LAST:event_jComboBoxButtonIcon21ItemStateChanged

    private void jTextFieldButtonExecute21KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonExecute21KeyReleased
        PropertyHandler.getInstance().setValue("Button.21.StrExec",jTextFieldButtonExecute21.getText() );
    }//GEN-LAST:event_jTextFieldButtonExecute21KeyReleased

    private void jTextFieldButtonToolTip21KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonToolTip21KeyReleased
        PropertyHandler.getInstance().setValue("Button.21.ToolTip",jTextFieldButtonToolTip21.getText());
    }//GEN-LAST:event_jTextFieldButtonToolTip21KeyReleased

    private void jComboBoxButtonIcon22ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxButtonIcon22ItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            PropertyHandler.getInstance().setValue("Button.22.Icon",((String)jComboBoxButtonIcon22.getSelectedItem())); 
        }
    }//GEN-LAST:event_jComboBoxButtonIcon22ItemStateChanged

    private void jTextFieldButtonExecute22KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonExecute22KeyReleased
        PropertyHandler.getInstance().setValue("Button.22.StrExec",jTextFieldButtonExecute22.getText() );
    }//GEN-LAST:event_jTextFieldButtonExecute22KeyReleased

    private void jTextFieldButtonToolTip22KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonToolTip22KeyReleased
        PropertyHandler.getInstance().setValue("Button.22.ToolTip",jTextFieldButtonToolTip22.getText());
    }//GEN-LAST:event_jTextFieldButtonToolTip22KeyReleased

    private void jComboBoxButtonIcon23ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxButtonIcon23ItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            PropertyHandler.getInstance().setValue("Button.23.Icon",((String)jComboBoxButtonIcon23.getSelectedItem())); 
        }
    }//GEN-LAST:event_jComboBoxButtonIcon23ItemStateChanged

    private void jTextFieldButtonExecute23KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonExecute23KeyReleased
        PropertyHandler.getInstance().setValue("Button.23.StrExec",jTextFieldButtonExecute23.getText() );
    }//GEN-LAST:event_jTextFieldButtonExecute23KeyReleased

    private void jTextFieldButtonToolTip23KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonToolTip23KeyReleased
        PropertyHandler.getInstance().setValue("Button.23.ToolTip",jTextFieldButtonToolTip23.getText());
    }//GEN-LAST:event_jTextFieldButtonToolTip23KeyReleased

    private void jComboBoxButtonIcon24ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxButtonIcon24ItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            PropertyHandler.getInstance().setValue("Button.24.Icon",((String)jComboBoxButtonIcon24.getSelectedItem())); 
        }
    }//GEN-LAST:event_jComboBoxButtonIcon24ItemStateChanged

    private void jTextFieldButtonExecute24KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonExecute24KeyReleased
        PropertyHandler.getInstance().setValue("Button.24.StrExec",jTextFieldButtonExecute24.getText() );
    }//GEN-LAST:event_jTextFieldButtonExecute24KeyReleased

    private void jTextFieldButtonToolTip24KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldButtonToolTip24KeyReleased
        PropertyHandler.getInstance().setValue("Button.24.ToolTip",jTextFieldButtonToolTip24.getText());
    }//GEN-LAST:event_jTextFieldButtonToolTip24KeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String args = jTextFieldNtpServer.getText();
        if (args.length() == 0) {
            System.err.println("No server entered.");
            jTextAreaNTPMessage.setText("No server entered.");
            return;
        }
        
        InetAddress hostAddr = null;
        try {
            hostAddr = InetAddress.getByName(args);
            jTextAreaNTPMessage.setText(" Resolved, retrieving NTP data from " + args);            
        } catch (UnknownHostException ex) {
            jTextAreaNTPMessage.setText(" Could not resolve " + args + ".  :(");
            return;
        }


        NTPUDPClient client = new NTPUDPClient();
        // We want to timeout if a response takes longer than 10 seconds
        client.setDefaultTimeout(10000);
        try {
            client.open();
                System.out.println();
                try {
                    System.out.println("> " + hostAddr.getHostName() + " (" + hostAddr.getHostAddress() + ")");
                    jTextAreaNTPMessage.setText("> " + hostAddr.getHostName() + "/" + hostAddr.getHostAddress());
                    TimeInfo info = client.getTime(hostAddr);
                    processResponseNTP(info);
                } catch (IOException e) {
                    jTextAreaNTPMessage.setText("> Failed to retrive time from " + hostAddr.getHostAddress());
                }
//            }
        } catch (SocketException e) {
            jTextAreaNTPMessage.setText("> There was some kind of socket problem with " + hostAddr.getHostName() + " (" + hostAddr.getHostAddress() + ")");
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButtonViewExampleUpdateScriptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonViewExampleUpdateScriptActionPerformed
    openTempTextFileInWindow("launchpad/files/LaunchPad-Pull.ps1",".ps1");
    }//GEN-LAST:event_jButtonViewExampleUpdateScriptActionPerformed

    private void jButtonCopyShortcutToDesktopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCopyShortcutToDesktopActionPerformed
        copyShortcutToDesktop();
    }//GEN-LAST:event_jButtonCopyShortcutToDesktopActionPerformed

    private void jTextFieldSettingPreferredIpPrimaryKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldSettingPreferredIpPrimaryKeyReleased
        PropertyHandler.getInstance().setValue("Setting.ShowIpPreferredPrimary",jTextFieldSettingPreferredIpPrimary.getText() );
    }//GEN-LAST:event_jTextFieldSettingPreferredIpPrimaryKeyReleased

    private void jTextFieldSettingPreferredIpSecondaryKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldSettingPreferredIpSecondaryKeyReleased
        PropertyHandler.getInstance().setValue("Setting.ShowIpPreferredSecondary",jTextFieldSettingPreferredIpSecondary.getText() );
    }//GEN-LAST:event_jTextFieldSettingPreferredIpSecondaryKeyReleased

    private void jTextFieldSettingPreferredIpTertiaryKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldSettingPreferredIpTertiaryKeyReleased
        PropertyHandler.getInstance().setValue("Setting.ShowIpPreferredTertiary",jTextFieldSettingPreferredIpTertiary.getText() );
    }//GEN-LAST:event_jTextFieldSettingPreferredIpTertiaryKeyReleased

    private void jButtonIconGeneratorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonIconGeneratorActionPerformed
        openTempFileUsingDesktop("html/icongenerator/IconGenerator.html", ".html");
    }//GEN-LAST:event_jButtonIconGeneratorActionPerformed

    private void jButtonNetworkConnectionsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonNetworkConnectionsMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonNetworkConnectionsMousePressed

    private void jButtonNetworkConnectionsMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonNetworkConnectionsMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonNetworkConnectionsMouseReleased

    private void jButtonNetworkConnectionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNetworkConnectionsActionPerformed
        System.out.println("Pressed");
        try {
            Runtime.getRuntime().exec("cmd /c start ncpa.cpl");
        }
        catch (IOException e) {
            System.out.println("Something is wrong!");
        }
    }//GEN-LAST:event_jButtonNetworkConnectionsActionPerformed

    private void jLabelLocalIPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelLocalIPMouseClicked
        String strCMD = "cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -Command \"& {$RunIPconfig = {Clear-Host; ipconfig /all; Write-Host ''; Write-Host 'Press Enter to REFRESH...' -NoNewLine  -ForegroundColor Green; Read-Host -Prompt ' '; .$RunIPconfig}; &$RunIPconfig}\"";
        System.out.println("Pressed");
        try {
            Runtime.getRuntime().exec(strCMD);
        }
        catch (IOException e) {
            System.out.println("Something is wrong!");
        }
    }//GEN-LAST:event_jLabelLocalIPMouseClicked

    private void jButton45ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton45ActionPerformed
        openEmbeddedPowershellScript("scripts/Powershell-ImageFolderToBase64.ps1", ".ps1");
    }//GEN-LAST:event_jButton45ActionPerformed

    private void jButton46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton46ActionPerformed
        openEmbeddedPowershellScript("scripts/Powershell-ImageToBase64.ps1", ".ps1");
    }//GEN-LAST:event_jButton46ActionPerformed

    private void jButtonScriptKeepAwakeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptKeepAwakeActionPerformed
        openEmbeddedPowershellScript("scripts/Powershell-KeepAlive.ps1", ".ps1");
    }//GEN-LAST:event_jButtonScriptKeepAwakeActionPerformed

    private void jButtonCiscoType7DecryptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCiscoType7DecryptActionPerformed
        openEmbeddedPowershellScript("scripts/Powershell-UnprotectType7.ps1", ".ps1");
    }//GEN-LAST:event_jButtonCiscoType7DecryptActionPerformed

    
    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String args[]) throws FileNotFoundException, IOException {
        
        


    }
    
    
    private void initAddSettingsReferences() {
        JPanelSettingsReferences jPanelSettingsReferencesClass = new JPanelSettingsReferences();
        jScrollPaneSettingsReferences.setViewportView(jPanelSettingsReferencesClass);
    }

    private void initAddSettingsScripts() {
        JPanelSettingsScripts jPanelSettingsScriptsClass = new JPanelSettingsScripts();
        jScrollPaneSettingsScripts.setViewportView(jPanelSettingsScriptsClass);
    }
    
     private ArrayList getSessionList() throws FileNotFoundException, IOException, URISyntaxException
    {
        String strSessionList;
        DefaultListModel listModel = new DefaultListModel();
        ArrayList arrSessionList = new ArrayList();
        //File pathWorkingDirectory = new File(System.getProperty("user.dir"));
        if(jCheckBoxFavorites.isSelected()){
            strSessionList = strSessionListFavorites;
        }
        else {
            strSessionList = strSessionListDefault;
        }
        System.out.println("SessionList.csv directory: " + strSessionList);
        File archivo = new File(strSessionList);
        if(!archivo.exists())
        {  
            archivo.createNewFile();
            List<String> lines = Arrays.asList(
" Click \"Edit personal favorites list\"",
" under the Settings tab.");
            
            Path file = Paths.get(strSessionList);
            Files.write(file, lines, Charset.forName("UTF-8"));
        }
        try (FileReader fr = new FileReader(archivo)) {
            BufferedReader buffIn;
            buffIn = new BufferedReader(fr);
               
            String line;
            while ((line = buffIn.readLine()) != null) {
                arrSessionList.add(line);
                listModel.addElement(line);
                //System.out.println(line);
            }
        jListSessions.setModel(listModel);
        }
        catch (IOException e) {
            System.out.println("SessionList.csv no good"); 
            JOptionPane.showMessageDialog(null, "SessionList.csv Error!"); 
        }
        return arrSessionList;
    }
     
    private void searchFilter(String searchTerm) throws IOException, FileNotFoundException, URISyntaxException
    {
        DefaultListModel filteredItems=new DefaultListModel();
        ArrayList sessions=getSessionList();

        sessions.stream().forEach((session) -> {
            String sessionName=session.toString().toLowerCase();
            if (sessionName.contains(searchTerm.toLowerCase())) {
                filteredItems.addElement(session);
            }
        });
        defaultListModelFilteredItems=filteredItems;
        jListSessions.setModel(defaultListModelFilteredItems);

    }
    
    public void loadMainButtonStyles() {
        //--- Load tooltips
        jButtonMainButton1.setToolTipText(PropertyHandler.getInstance().getValue("Button.01.ToolTip"));
        jButtonMainButton2.setToolTipText(PropertyHandler.getInstance().getValue("Button.02.ToolTip"));
        jButtonMainButton3.setToolTipText(PropertyHandler.getInstance().getValue("Button.03.ToolTip"));
        jButtonMainButton4.setToolTipText(PropertyHandler.getInstance().getValue("Button.04.ToolTip"));
        jButtonMainButton5.setToolTipText(PropertyHandler.getInstance().getValue("Button.05.ToolTip"));
        jButtonMainButton6.setToolTipText(PropertyHandler.getInstance().getValue("Button.06.ToolTip"));
        jButtonMainButton7.setToolTipText(PropertyHandler.getInstance().getValue("Button.07.ToolTip"));
        jButtonMainButton8.setToolTipText(PropertyHandler.getInstance().getValue("Button.08.ToolTip"));
        jButtonMainButton9.setToolTipText(PropertyHandler.getInstance().getValue("Button.09.ToolTip"));
        jButtonMainButton10.setToolTipText(PropertyHandler.getInstance().getValue("Button.10.ToolTip"));
        jButtonMainButton11.setToolTipText(PropertyHandler.getInstance().getValue("Button.11.ToolTip"));
        jButtonMainButton12.setToolTipText(PropertyHandler.getInstance().getValue("Button.12.ToolTip"));
        jButtonMainButton13.setToolTipText(PropertyHandler.getInstance().getValue("Button.13.ToolTip"));
        jButtonMainButton14.setToolTipText(PropertyHandler.getInstance().getValue("Button.14.ToolTip"));
        jButtonMainButton15.setToolTipText(PropertyHandler.getInstance().getValue("Button.15.ToolTip"));
        jButtonMainButton16.setToolTipText(PropertyHandler.getInstance().getValue("Button.16.ToolTip"));
        jButtonMainButton17.setToolTipText(PropertyHandler.getInstance().getValue("Button.17.ToolTip"));
        jButtonMainButton18.setToolTipText(PropertyHandler.getInstance().getValue("Button.18.ToolTip"));
        jButtonMainButton19.setToolTipText(PropertyHandler.getInstance().getValue("Button.19.ToolTip"));
        jButtonMainButton20.setToolTipText(PropertyHandler.getInstance().getValue("Button.20.ToolTip"));
        jButtonMainButton21.setToolTipText(PropertyHandler.getInstance().getValue("Button.21.ToolTip"));
        jButtonMainButton22.setToolTipText(PropertyHandler.getInstance().getValue("Button.22.ToolTip"));
        jButtonMainButton23.setToolTipText(PropertyHandler.getInstance().getValue("Button.23.ToolTip"));
        jButtonMainButton24.setToolTipText(PropertyHandler.getInstance().getValue("Button.24.ToolTip"));
    }
    
    private void searchFilterLinks(String searchTerm) throws IOException, FileNotFoundException, URISyntaxException
    {
        if (jButtonLinkCustom01.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonLinkCustom01.setVisible(Boolean.TRUE);}else{jButtonLinkCustom01.setVisible(Boolean.FALSE);}
        if (jButtonLinkCustom02.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonLinkCustom02.setVisible(Boolean.TRUE);}else{jButtonLinkCustom02.setVisible(Boolean.FALSE);}
        if (jButtonLinkCustom03.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonLinkCustom03.setVisible(Boolean.TRUE);}else{jButtonLinkCustom03.setVisible(Boolean.FALSE);}
        if (jButtonLinkCustom04.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonLinkCustom04.setVisible(Boolean.TRUE);}else{jButtonLinkCustom04.setVisible(Boolean.FALSE);}
        if (jButtonLinkCustom05.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonLinkCustom05.setVisible(Boolean.TRUE);}else{jButtonLinkCustom05.setVisible(Boolean.FALSE);}
        if (jButtonLinkCustom06.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonLinkCustom06.setVisible(Boolean.TRUE);}else{jButtonLinkCustom06.setVisible(Boolean.FALSE);}
        if (jButtonLinkCustom07.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonLinkCustom07.setVisible(Boolean.TRUE);}else{jButtonLinkCustom07.setVisible(Boolean.FALSE);}
        if (jButtonLinkCustom08.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonLinkCustom08.setVisible(Boolean.TRUE);}else{jButtonLinkCustom08.setVisible(Boolean.FALSE);}
        if (jButtonLinkCustom09.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonLinkCustom09.setVisible(Boolean.TRUE);}else{jButtonLinkCustom09.setVisible(Boolean.FALSE);}
        if (jButtonLinkCustom10.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonLinkCustom10.setVisible(Boolean.TRUE);}else{jButtonLinkCustom10.setVisible(Boolean.FALSE);}
        if (jButtonLinkCustom11.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonLinkCustom11.setVisible(Boolean.TRUE);}else{jButtonLinkCustom11.setVisible(Boolean.FALSE);}
        if (jButtonLinkCustom12.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonLinkCustom12.setVisible(Boolean.TRUE);}else{jButtonLinkCustom12.setVisible(Boolean.FALSE);}
        if (jButtonLinkCustom13.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonLinkCustom13.setVisible(Boolean.TRUE);}else{jButtonLinkCustom13.setVisible(Boolean.FALSE);}
        if (jButtonLinkCustom14.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonLinkCustom14.setVisible(Boolean.TRUE);}else{jButtonLinkCustom14.setVisible(Boolean.FALSE);}
        if (jButtonLinkCustom15.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonLinkCustom15.setVisible(Boolean.TRUE);}else{jButtonLinkCustom15.setVisible(Boolean.FALSE);}
        if (jButtonLinkCustom16.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonLinkCustom16.setVisible(Boolean.TRUE);}else{jButtonLinkCustom16.setVisible(Boolean.FALSE);}
        if (jButtonLinkCustom17.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonLinkCustom17.setVisible(Boolean.TRUE);}else{jButtonLinkCustom17.setVisible(Boolean.FALSE);}
        if (jButtonLinkCustom18.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonLinkCustom18.setVisible(Boolean.TRUE);}else{jButtonLinkCustom18.setVisible(Boolean.FALSE);}
        if (jButtonLinkCustom19.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonLinkCustom19.setVisible(Boolean.TRUE);}else{jButtonLinkCustom19.setVisible(Boolean.FALSE);}
        if (jButtonLinkCustom20.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonLinkCustom20.setVisible(Boolean.TRUE);}else{jButtonLinkCustom20.setVisible(Boolean.FALSE);}
        if (jButtonLinkCustom21.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonLinkCustom21.setVisible(Boolean.TRUE);}else{jButtonLinkCustom21.setVisible(Boolean.FALSE);}
        if (jButtonLinkCustom22.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonLinkCustom22.setVisible(Boolean.TRUE);}else{jButtonLinkCustom22.setVisible(Boolean.FALSE);}
        if (jButtonLinkCustom23.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonLinkCustom23.setVisible(Boolean.TRUE);}else{jButtonLinkCustom23.setVisible(Boolean.FALSE);}
        if (jButtonLinkCustom24.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonLinkCustom24.setVisible(Boolean.TRUE);}else{jButtonLinkCustom24.setVisible(Boolean.FALSE);}
        if (jButtonLinkCustom25.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonLinkCustom25.setVisible(Boolean.TRUE);}else{jButtonLinkCustom25.setVisible(Boolean.FALSE);}
        if (jButtonLinkCustom26.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonLinkCustom26.setVisible(Boolean.TRUE);}else{jButtonLinkCustom26.setVisible(Boolean.FALSE);}
        if (jButtonLinkCustom27.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonLinkCustom27.setVisible(Boolean.TRUE);}else{jButtonLinkCustom27.setVisible(Boolean.FALSE);}
        if (jButtonLinkCustom28.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonLinkCustom28.setVisible(Boolean.TRUE);}else{jButtonLinkCustom28.setVisible(Boolean.FALSE);}
        if (jButtonLinkCustom29.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonLinkCustom29.setVisible(Boolean.TRUE);}else{jButtonLinkCustom29.setVisible(Boolean.FALSE);}
        if (jButtonLinkCustom30.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonLinkCustom30.setVisible(Boolean.TRUE);}else{jButtonLinkCustom30.setVisible(Boolean.FALSE);}
        if (jButtonLinkCustom31.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonLinkCustom31.setVisible(Boolean.TRUE);}else{jButtonLinkCustom31.setVisible(Boolean.FALSE);}
        if (jButtonLinkCustom32.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonLinkCustom32.setVisible(Boolean.TRUE);}else{jButtonLinkCustom32.setVisible(Boolean.FALSE);}
        if (jButtonLinkCustom33.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonLinkCustom33.setVisible(Boolean.TRUE);}else{jButtonLinkCustom33.setVisible(Boolean.FALSE);}
        if (jButtonLinkCustom34.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonLinkCustom34.setVisible(Boolean.TRUE);}else{jButtonLinkCustom34.setVisible(Boolean.FALSE);}
        if (jButtonLinkCustom35.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonLinkCustom35.setVisible(Boolean.TRUE);}else{jButtonLinkCustom35.setVisible(Boolean.FALSE);}
        if (jButtonLinkCustom36.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonLinkCustom36.setVisible(Boolean.TRUE);}else{jButtonLinkCustom36.setVisible(Boolean.FALSE);}
    }
    
    private void searchFilterReference(String searchTerm) throws IOException, FileNotFoundException, URISyntaxException
    {
        if (jButtonReferenceCustom01.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonReferenceCustom01.setVisible(Boolean.TRUE);}else{jButtonReferenceCustom01.setVisible(Boolean.FALSE);}
        if (jButtonReferenceCustom02.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonReferenceCustom02.setVisible(Boolean.TRUE);}else{jButtonReferenceCustom02.setVisible(Boolean.FALSE);}
        if (jButtonReferenceCustom03.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonReferenceCustom03.setVisible(Boolean.TRUE);}else{jButtonReferenceCustom03.setVisible(Boolean.FALSE);}
        if (jButtonReferenceCustom04.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonReferenceCustom04.setVisible(Boolean.TRUE);}else{jButtonReferenceCustom04.setVisible(Boolean.FALSE);}
        if (jButtonReferenceCustom05.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonReferenceCustom05.setVisible(Boolean.TRUE);}else{jButtonReferenceCustom05.setVisible(Boolean.FALSE);}
        if (jButtonReferenceCustom06.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonReferenceCustom06.setVisible(Boolean.TRUE);}else{jButtonReferenceCustom06.setVisible(Boolean.FALSE);}
        if (jButtonReferenceCustom07.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonReferenceCustom07.setVisible(Boolean.TRUE);}else{jButtonReferenceCustom07.setVisible(Boolean.FALSE);}
        if (jButtonReferenceCustom08.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonReferenceCustom08.setVisible(Boolean.TRUE);}else{jButtonReferenceCustom08.setVisible(Boolean.FALSE);}
        if (jButtonReferenceCustom09.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonReferenceCustom09.setVisible(Boolean.TRUE);}else{jButtonReferenceCustom09.setVisible(Boolean.FALSE);}
        if (jButtonReferenceCustom10.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonReferenceCustom10.setVisible(Boolean.TRUE);}else{jButtonReferenceCustom10.setVisible(Boolean.FALSE);}
        if (jButtonReferenceCustom11.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonReferenceCustom11.setVisible(Boolean.TRUE);}else{jButtonReferenceCustom11.setVisible(Boolean.FALSE);}
        if (jButtonReferenceCustom12.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonReferenceCustom12.setVisible(Boolean.TRUE);}else{jButtonReferenceCustom12.setVisible(Boolean.FALSE);}
        if (jButtonReferenceCustom13.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonReferenceCustom13.setVisible(Boolean.TRUE);}else{jButtonReferenceCustom13.setVisible(Boolean.FALSE);}
        if (jButtonReferenceCustom14.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonReferenceCustom14.setVisible(Boolean.TRUE);}else{jButtonReferenceCustom14.setVisible(Boolean.FALSE);}
        if (jButtonReferenceCustom15.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonReferenceCustom15.setVisible(Boolean.TRUE);}else{jButtonReferenceCustom15.setVisible(Boolean.FALSE);}
        if (jButtonReferenceCustom16.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonReferenceCustom16.setVisible(Boolean.TRUE);}else{jButtonReferenceCustom16.setVisible(Boolean.FALSE);}
        if (jButtonReferenceCustom17.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonReferenceCustom17.setVisible(Boolean.TRUE);}else{jButtonReferenceCustom17.setVisible(Boolean.FALSE);}
        if (jButtonReferenceCustom18.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonReferenceCustom18.setVisible(Boolean.TRUE);}else{jButtonReferenceCustom18.setVisible(Boolean.FALSE);}
        if (jButtonReferenceCustom19.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonReferenceCustom19.setVisible(Boolean.TRUE);}else{jButtonReferenceCustom19.setVisible(Boolean.FALSE);}
        if (jButtonReferenceCustom20.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonReferenceCustom20.setVisible(Boolean.TRUE);}else{jButtonReferenceCustom20.setVisible(Boolean.FALSE);}
        if (jButtonReferenceCustom21.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonReferenceCustom21.setVisible(Boolean.TRUE);}else{jButtonReferenceCustom21.setVisible(Boolean.FALSE);}
        if (jButtonReferenceCustom22.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonReferenceCustom22.setVisible(Boolean.TRUE);}else{jButtonReferenceCustom22.setVisible(Boolean.FALSE);}
        if (jButtonReferenceCustom23.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonReferenceCustom23.setVisible(Boolean.TRUE);}else{jButtonReferenceCustom23.setVisible(Boolean.FALSE);}
        if (jButtonReferenceCustom24.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonReferenceCustom24.setVisible(Boolean.TRUE);}else{jButtonReferenceCustom24.setVisible(Boolean.FALSE);}
        if (jButtonReferenceCustom25.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonReferenceCustom25.setVisible(Boolean.TRUE);}else{jButtonReferenceCustom25.setVisible(Boolean.FALSE);}
        if (jButtonReferenceCustom26.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonReferenceCustom26.setVisible(Boolean.TRUE);}else{jButtonReferenceCustom26.setVisible(Boolean.FALSE);}
        if (jButtonReferenceCustom27.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonReferenceCustom27.setVisible(Boolean.TRUE);}else{jButtonReferenceCustom27.setVisible(Boolean.FALSE);}
        if (jButtonReferenceCustom28.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonReferenceCustom28.setVisible(Boolean.TRUE);}else{jButtonReferenceCustom28.setVisible(Boolean.FALSE);}
        if (jButtonReferenceCustom29.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonReferenceCustom29.setVisible(Boolean.TRUE);}else{jButtonReferenceCustom29.setVisible(Boolean.FALSE);}
        if (jButtonReferenceCustom30.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonReferenceCustom30.setVisible(Boolean.TRUE);}else{jButtonReferenceCustom30.setVisible(Boolean.FALSE);}
        if (jButtonReferenceCustom31.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonReferenceCustom31.setVisible(Boolean.TRUE);}else{jButtonReferenceCustom31.setVisible(Boolean.FALSE);}
        if (jButtonReferenceCustom32.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonReferenceCustom32.setVisible(Boolean.TRUE);}else{jButtonReferenceCustom32.setVisible(Boolean.FALSE);}
        if (jButtonReferenceCustom33.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonReferenceCustom33.setVisible(Boolean.TRUE);}else{jButtonReferenceCustom33.setVisible(Boolean.FALSE);}
    }  
    
    private void searchFilterScripts(String searchTerm) throws IOException, FileNotFoundException, URISyntaxException
    {
        if (jButtonScriptCustom01.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonScriptCustom01.setVisible(Boolean.TRUE);}else{jButtonScriptCustom01.setVisible(Boolean.FALSE);}
        if (jButtonScriptCustom02.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonScriptCustom02.setVisible(Boolean.TRUE);}else{jButtonScriptCustom02.setVisible(Boolean.FALSE);}
        if (jButtonScriptCustom03.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonScriptCustom03.setVisible(Boolean.TRUE);}else{jButtonScriptCustom03.setVisible(Boolean.FALSE);}
        if (jButtonScriptCustom04.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonScriptCustom04.setVisible(Boolean.TRUE);}else{jButtonScriptCustom04.setVisible(Boolean.FALSE);}
        if (jButtonScriptCustom05.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonScriptCustom05.setVisible(Boolean.TRUE);}else{jButtonScriptCustom05.setVisible(Boolean.FALSE);}
        if (jButtonScriptCustom06.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonScriptCustom06.setVisible(Boolean.TRUE);}else{jButtonScriptCustom06.setVisible(Boolean.FALSE);}
        if (jButtonScriptCustom07.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonScriptCustom07.setVisible(Boolean.TRUE);}else{jButtonScriptCustom07.setVisible(Boolean.FALSE);}
        if (jButtonScriptCustom08.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonScriptCustom08.setVisible(Boolean.TRUE);}else{jButtonScriptCustom08.setVisible(Boolean.FALSE);}
        if (jButtonScriptCustom09.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonScriptCustom09.setVisible(Boolean.TRUE);}else{jButtonScriptCustom09.setVisible(Boolean.FALSE);}
        if (jButtonScriptCustom10.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonScriptCustom10.setVisible(Boolean.TRUE);}else{jButtonScriptCustom10.setVisible(Boolean.FALSE);}
        if (jButtonScriptCustom11.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonScriptCustom11.setVisible(Boolean.TRUE);}else{jButtonScriptCustom11.setVisible(Boolean.FALSE);}
        if (jButtonScriptCustom12.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonScriptCustom12.setVisible(Boolean.TRUE);}else{jButtonScriptCustom12.setVisible(Boolean.FALSE);}
        if (jButtonScriptCustom13.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonScriptCustom13.setVisible(Boolean.TRUE);}else{jButtonScriptCustom13.setVisible(Boolean.FALSE);}
        if (jButtonScriptCustom14.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonScriptCustom14.setVisible(Boolean.TRUE);}else{jButtonScriptCustom14.setVisible(Boolean.FALSE);}
        if (jButtonScriptCustom15.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonScriptCustom15.setVisible(Boolean.TRUE);}else{jButtonScriptCustom15.setVisible(Boolean.FALSE);}
        if (jButtonScriptCustom16.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonScriptCustom16.setVisible(Boolean.TRUE);}else{jButtonScriptCustom16.setVisible(Boolean.FALSE);}
        if (jButtonScriptCustom17.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonScriptCustom17.setVisible(Boolean.TRUE);}else{jButtonScriptCustom17.setVisible(Boolean.FALSE);}
        if (jButtonScriptCustom18.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonScriptCustom18.setVisible(Boolean.TRUE);}else{jButtonScriptCustom18.setVisible(Boolean.FALSE);}
        if (jButtonScriptCustom19.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonScriptCustom19.setVisible(Boolean.TRUE);}else{jButtonScriptCustom19.setVisible(Boolean.FALSE);}
        if (jButtonScriptCustom20.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonScriptCustom20.setVisible(Boolean.TRUE);}else{jButtonScriptCustom20.setVisible(Boolean.FALSE);}
        if (jButtonScriptCustom21.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonScriptCustom21.setVisible(Boolean.TRUE);}else{jButtonScriptCustom21.setVisible(Boolean.FALSE);}
        if (jButtonScriptCustom22.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonScriptCustom22.setVisible(Boolean.TRUE);}else{jButtonScriptCustom22.setVisible(Boolean.FALSE);}
        if (jButtonScriptCustom23.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonScriptCustom23.setVisible(Boolean.TRUE);}else{jButtonScriptCustom23.setVisible(Boolean.FALSE);}
        if (jButtonScriptCustom24.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonScriptCustom24.setVisible(Boolean.TRUE);}else{jButtonScriptCustom24.setVisible(Boolean.FALSE);}
        if (jButtonScriptCustom25.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonScriptCustom25.setVisible(Boolean.TRUE);}else{jButtonScriptCustom25.setVisible(Boolean.FALSE);}
        if (jButtonScriptCustom26.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonScriptCustom26.setVisible(Boolean.TRUE);}else{jButtonScriptCustom26.setVisible(Boolean.FALSE);}
        if (jButtonScriptCustom27.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonScriptCustom27.setVisible(Boolean.TRUE);}else{jButtonScriptCustom27.setVisible(Boolean.FALSE);}
        if (jButtonScriptCustom28.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonScriptCustom28.setVisible(Boolean.TRUE);}else{jButtonScriptCustom28.setVisible(Boolean.FALSE);}
        if (jButtonScriptCustom29.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonScriptCustom29.setVisible(Boolean.TRUE);}else{jButtonScriptCustom29.setVisible(Boolean.FALSE);}
        if (jButtonScriptCustom30.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonScriptCustom30.setVisible(Boolean.TRUE);}else{jButtonScriptCustom30.setVisible(Boolean.FALSE);}
        if (jButtonScriptCustom31.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonScriptCustom31.setVisible(Boolean.TRUE);}else{jButtonScriptCustom31.setVisible(Boolean.FALSE);}
        if (jButtonScriptCustom32.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonScriptCustom32.setVisible(Boolean.TRUE);}else{jButtonScriptCustom32.setVisible(Boolean.FALSE);}
        if (jButtonScriptCustom33.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonScriptCustom33.setVisible(Boolean.TRUE);}else{jButtonScriptCustom33.setVisible(Boolean.FALSE);}
        if (jButtonScriptCustom34.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonScriptCustom34.setVisible(Boolean.TRUE);}else{jButtonScriptCustom34.setVisible(Boolean.FALSE);}
        if (jButtonScriptCustom35.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonScriptCustom35.setVisible(Boolean.TRUE);}else{jButtonScriptCustom35.setVisible(Boolean.FALSE);}
        if (jButtonScriptCustom36.getText().toLowerCase().contains(searchTerm.toLowerCase())) {jButtonScriptCustom36.setVisible(Boolean.TRUE);}else{jButtonScriptCustom36.setVisible(Boolean.FALSE);}
    }
    
  /**
   * List directory contents for a resource folder. Not recursive.
   * Works for regular files and also JARs.
   * 
   * @param clazz Any java class that lives in the same place as the resources you want.
   * @param path Should end with "/", but not start with one.
   * @return Just the name of each member item, not the full paths.
   * @throws URISyntaxException 
   * @throws IOException 
   */
  String[] getResourceListing(Class clazz, String path) throws URISyntaxException, IOException {
      URL dirURL = clazz.getClassLoader().getResource(path);
      if (dirURL != null && dirURL.getProtocol().equals("file")) {
        /* A file path: easy enough */
        return new File(dirURL.toURI()).list();
      } 

      if (dirURL == null) {
        /* 
         * In case of a jar file, we can't actually find a directory.
         * Have to assume the same jar as clazz.
         */
        String me = clazz.getName().replace(".", "/")+".class";
        dirURL = clazz.getClassLoader().getResource(me);
      }
      
      if (dirURL.getProtocol().equals("jar")) {
        /* A JAR path */
        String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); //strip out only the JAR file
        JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
        Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
        Set<String> result = new HashSet<>(); //avoid duplicates in case it is a subdirectory
        while(entries.hasMoreElements()) {
          String name = entries.nextElement().getName();
          if (name.startsWith(path)) { //filter according to the path
            String entry = name.substring(path.length());
            int checkSubdir = entry.indexOf("/");
            if (checkSubdir >= 0) {
              // if it is a subdirectory, we just return the directory name
              entry = entry.substring(0, checkSubdir);
            }
            result.add(entry);
          }
        }
        return result.toArray(new String[result.size()]);
      } 
        
      throw new UnsupportedOperationException("Cannot list files for URL "+dirURL);
  }
    
  

    public void openLinkCustom(String strLinkText) {
        String strCommand = PropertyHandler.getInstance().getValue(strLinkText);
        //System.out.println(strCommand);
        try {
            //- Using ProcessBuilder
            //String[] args = new String[] {"cmd.exe", "/c", "start", strCommand};
            //Process proc = new ProcessBuilder(args).start();

            //- Using Runtime
            System.out.println("Opening Link: " + strCommand); 
            if(strCommand != null && !strCommand.isEmpty()) { /* do your stuffs here */             
                //Runtime.getRuntime().exec("cmd.exe /c start " + strCommand);
                Runtime.getRuntime().exec(strCommand);
            }
        }
        catch (IOException e) {
            System.out.println("Something is Wrong!");
            JOptionPane.showMessageDialog(null, "Something is wrong!");
        }
    }
    
    public void openScriptCustom(String strLinkText) {
        String strCommand = PropertyHandler.getInstance().getValue(strLinkText);
        //System.out.println(strCommand);
        try {
            //- Using ProcessBuilder
            //String[] args = new String[] {"cmd.exe", "/c", "start", strCommand};
            //Process proc = new ProcessBuilder(args).start();

            //- Using Runtime
            System.out.println("Opening Script: " + strCommand);  
            if(strCommand != null && !strCommand.isEmpty()) { /* do your stuffs here */ 
                //Runtime.getRuntime().exec("cmd.exe /c start " + strCommand);
                Runtime.getRuntime().exec(strCommand);
            }
        }
        catch (IOException e) {
            System.out.println("Something is Wrong!");
            JOptionPane.showMessageDialog(null, "Something is wrong!");
        }
    }
    
    public void openCommand(String strProperty) {
        String strCommand = PropertyHandler.getInstance().getValue(strProperty);
        //System.out.println(strCommand);
        try {
            //- Using ProcessBuilder
            //String[] args = new String[] {"cmd.exe", "/c", "start", strCommand};
            //Process proc = new ProcessBuilder(args).start();

            //- Using Runtime
            System.out.println("Opening Command: " + strCommand);     
            if(strCommand != null && !strCommand.isEmpty()) { /* do your stuffs here */ 
                //Runtime.getRuntime().exec("cmd.exe /c start " + strCommand);
                Runtime.getRuntime().exec("" + strCommand);
            }
        }
        catch (IOException e) {
            System.out.println("Something is Wrong!");
            JOptionPane.showMessageDialog(null, "Something is wrong!");
        }
    }
    
    public void openFileUsingDesktop(String strFullFilePath) {
        System.out.println("openFileUsingDesktop: " + strFullFilePath);

        File userManual = new File (strFullFilePath);
        if (userManual.exists())
        {
            try {               
                Desktop.getDesktop().open(userManual);
            } catch (IOException ex) {
                System.out.println("Something is Wrong! openFileUsingDesktop: " + strFullFilePath);
                Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
   

    public void openReference(String strReferenceToOpen) throws IOException {
    
        String strReferenceFromPropertiesFile;
        //strReference = PropertyHandler.getInstance().getValue(strReferenceToOpen);

        if(jToggleOnlineOfflineMode.isSelected()){
            strReferenceFromPropertiesFile =  PropertyHandler.getInstance().getValue(strReferenceToOpen + ".Offline");
            System.out.println("Using Offline: " + strReferenceFromPropertiesFile);
        }
        else{
            strReferenceFromPropertiesFile =  PropertyHandler.getInstance().getValue(strReferenceToOpen + ".Online");
            System.out.println("Using Online: " + strReferenceFromPropertiesFile);        
        }

        try {
            //- Using ProcessBuilder
            //String[] args = new String[] {"cmd.exe", "/c", "start", strReferenceFromPropertiesFile};
            //Process proc = new ProcessBuilder(args).start();
            
            //- Using Runtime
            System.out.println("Opening: " + strReferenceFromPropertiesFile); 
            
            if(strReferenceFromPropertiesFile != null && !strReferenceFromPropertiesFile.isEmpty()) { 
                Runtime.getRuntime().exec(strReferenceFromPropertiesFile);
            }

            
        }
        catch (IOException e) {
            System.out.println("Something is Wrong!");
            JOptionPane.showMessageDialog(null, "Something is wrong!");
        }
    }

    
    public void openTempFileUsingDesktop(String strFullFilePath,String strExtension) {
    
            String inputPdf = strFullFilePath;
        InputStream manualAsStream = getClass().getClassLoader().getResourceAsStream(inputPdf);

        Path tempOutput = null;
        try {
            tempOutput = Files.createTempFile("TempFile", strExtension);
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        tempOutput.toFile().deleteOnExit();

        try {
            Files.copy(manualAsStream, tempOutput, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }

        File userManual = new File (tempOutput.toFile().getPath());
        if (userManual.exists())
        {
            try {
                Desktop.getDesktop().open(userManual);
            } catch (IOException ex) {
                Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }  

    public void openTempTextFileInWindow(String strFullFilePath,String strExtension) {
    
        InputStream manualAsStream = getClass().getClassLoader().getResourceAsStream(strFullFilePath);

        Path tempOutput = null;
        try {
            tempOutput = Files.createTempFile("TempFile", strExtension);
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        tempOutput.toFile().deleteOnExit();

        try {
            Files.copy(manualAsStream, tempOutput, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }

        File userManual = new File (tempOutput.toFile().getPath());
        if (userManual.exists())
        {
//            try {
//                Desktop.getDesktop().open(userManual);
//            } catch (IOException ex) {
//                Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
//            }
            
        //DefaultListModel listModel = new DefaultListModel();
        //ArrayList arrSessionList = new ArrayList();

        //File archivo = new File(strPathPropertiesFile);

        try (FileReader fr = new FileReader(userManual)) {
            //BufferedReader buffIn;
            //buffIn = new BufferedReader(fr);

            //String line;
            JTextArea textarea = new JTextArea();

            try (BufferedReader myReader = new BufferedReader(fr)) {
                    textarea.read(myReader, "Stream description");

            } catch (IOException e) {
            }
            
            
            //JList list = new JList(listModel);

            JScrollPane scroll = new JScrollPane(textarea);
scroll.setPreferredSize(new Dimension(800, 500));

            JFrame frame = new JFrame();
            frame.add(scroll);
            frame.setTitle("Properties File");
            //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            
        }
        catch (IOException e) {
            System.out.println("SessionList.csv no good");
            JOptionPane.showMessageDialog(null, "SessionList.csv Error!");
        }
            
        }
        
    }
    
    
    public void copyInternalFileToSpecifiedDestination(String strFullFilePath, String strDestinationPath) throws Exception, Exception {
    
        InputStream stream = null;
        OutputStream resStreamOut = null;
        try {
            stream = LaunchPadForm.class.getResourceAsStream(strFullFilePath);//note that each / is a directory down in the "jar tree" been the jar the root of the tree
            if(stream == null) {
                throw new Exception("Cannot get resource \"" + strFullFilePath + "\" from Jar file.");
            }

            int readBytes;
            byte[] buffer = new byte[4096];
            resStreamOut = new FileOutputStream(strDestinationPath);
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            stream.close();
            resStreamOut.close();
        }
        
    }      
    
    public void openEmbeddedPowershellScript(String strFullFilePath,String strExtension) {
    
            String inputInternalFile = strFullFilePath;
        InputStream manualAsStream = getClass().getClassLoader().getResourceAsStream(inputInternalFile);

        Path tempOutput = null;
        try {
            tempOutput = Files.createTempFile("TempFile", strExtension);
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        tempOutput.toFile().deleteOnExit();

        try {
            Files.copy(manualAsStream, tempOutput, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }

        File fileInTempLocation = new File (tempOutput.toFile().getPath());
        if (fileInTempLocation.exists())
        {
            try {
                //Desktop.getDesktop().open(userManual);
                Runtime.getRuntime().exec("cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -noexit -File \"" + fileInTempLocation + "\"");
               
                
            } catch (IOException ex) {
                Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }  


//    static public boolean isValidURL(String urlStr) {
//        try {
//          URI uri = new URI(urlStr);
//          return uri.getScheme().equals("http") || uri.getScheme().equals("https");
//        }
//        catch (Exception e) {
//            return false;
//        }
//    }   
    
    //- Double and Triple click functions
    class MyMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent evt) {
//            if (evt.getClickCount() == 3) {
//                System.out.println("triple-click");
//            } else 
            if (evt.getClickCount() == 2) {
                System.out.println("Action: double-click detected");
                String strSelectedValue = jListSessions.getSelectedValue();
                if(strSelectedValue.contains(",")) {
                    String[] arrSelectedValue = strSelectedValue.split(",");
                    jTextFieldConnectHostname.setText(arrSelectedValue[1]);
                    jTextFieldPingHostname.setText(arrSelectedValue[1]);
                }
                String myValue = PropertyHandler.getInstance().getValue("Button.ExecuteFunctionDoubleClick");
                //- If blank, then set to SSH
                if("".equals(myValue)) {
                    PropertyHandler.getInstance().setValue("Button.ExecuteFunctionDoubleClick", "SSH");
                }              
                //- Get value again to run
                myValue = PropertyHandler.getInstance().getValue("Button.ExecuteFunctionDoubleClick");
                if("HTTPS".equals(myValue)) {
                    jButtonExecuteFunctionHTTPS.doClick();
                    System.out.println("Double-Click: HTTPS");
                } else if("RDP".equals(myValue)) {
                    jButtonExecuteFunctionRDP.doClick();               
                    System.out.println("Double-Click: RDP");                   
                } else if("SSH".equals(myValue)){
                    jButtonExecuteFunctionSSH.doClick();
                    System.out.println("Double-Click: SSH");                    
                } else if("PKI".equals(myValue)){
                    jButtonExecuteFunctionPKI.doClick();
                    System.out.println("Double-Click: PKI");                    
                }
            }
        }
    }

    //- Get local Hostname
    private static String getSystemName(){  
        try{
            InetAddress inetaddress=InetAddress.getLocalHost(); //Get LocalHost reference
            String name = inetaddress.getHostName();   //Get Host Name
            return name;   //return Host Name
        }
        catch(UnknownHostException e){ 
            return null;
        }
    }
     
    //- Get local IP
    public static String getIPAddress() throws SocketException, UnknownHostException {
        //- Original method
//        try{
//            InetAddress inetaddress=InetAddress.getLocalHost();  //Get LocalHost refrence
//            String ip = inetaddress.getHostAddress();  // Get Host IP Address
//            return ip;   // return IP Address
//        }
//        catch(Exception E){
//            E.printStackTrace();  //print Exception StackTrace
//            return null;
//        }
        String primaryProperty = PropertyHandler.getInstance().getValue("Setting.ShowIpPreferredPrimary");
        String secondaryProperty = PropertyHandler.getInstance().getValue("Setting.ShowIpPreferredSecondary");
        String tertiaryProperty = PropertyHandler.getInstance().getValue("Setting.ShowIpPreferredTertiary");        
        String nicname = null;
        String ip = null;
        String nicnameip = null;
        String primary = null;
        String secondary = null;
        String tertiary = null;
        try {
            Enumeration<NetworkInterface> nics = NetworkInterface.getNetworkInterfaces();
            while (nics.hasMoreElements()) {
                NetworkInterface nic = nics.nextElement();
                Enumeration<InetAddress> addrs = nic.getInetAddresses();
                while (addrs.hasMoreElements()) {
                    InetAddress addr = addrs.nextElement();
                    nicname = nic.getName();
                    ip = addr.getHostAddress();
                    nicnameip = nicname + " - " + ip;
                    //System.out.println(nicname + " " + ip);
                    //- Look for Primary, Secondary or Tertiary IP, else just grab one.
                    if (ip.startsWith(primaryProperty) && primaryProperty != null && !primaryProperty.isEmpty()) {
                        //System.out.println("Matched Primary Display IP:" + ip);
                        primary = nicnameip;
                    } else if (ip.startsWith(secondaryProperty) && secondaryProperty != null && !secondaryProperty.isEmpty()) {
                        //System.out.println("Matched Secondary Display IP:" + ip);
                        secondary = nicnameip;
                    } else if (ip.startsWith(tertiaryProperty) && tertiaryProperty != null && !tertiaryProperty.isEmpty()) {
                        //System.out.println("Matched Tertiary Display IP:" + ip);
                        tertiary = nicnameip;
                    }
                }
            }
        } catch (SocketException e) {
        }
        //- Choose to show Primary, Secondary or Tertiary IP, else just grab one.
        if (primary != null && !primary.isEmpty()) {
            //System.out.println("Returning Primary Display IP:" + ip);
            return primary;
        } else if (secondary != null && !secondary.isEmpty()) {
            //System.out.println("Returning Secondary Display IP:" + ip);
            return secondary;
        } else if (tertiary != null && !tertiary.isEmpty()) {
            //System.out.println("Returning Tertiary Display IP:" + ip);
            return tertiary;
        } else {
            InetAddress inetaddress=InetAddress.getLocalHost();  //Get LocalHost refrence
            ip = inetaddress.getHostAddress();  // Get Host IP Address
            return ip;   // return IP Address
        }        

//        try {
//            
//            
//            InetAddress localhost = InetAddress.getLocalHost();
//            System.out.println(" IP Addr: " + localhost.getHostAddress());
//            // Just in case this host has multiple IP addresses....
//            InetAddress[] allMyIps = InetAddress.getAllByName(localhost.getCanonicalHostName());
//            if (allMyIps != null && allMyIps.length > 1) {
//                System.out.println(" Full list of IP addresses:");
//            for (int i = 0; i < allMyIps.length; i++) {
//                    System.out.println("    " + allMyIps[i]);
//                    //- Look for Primary, Secondary or Tertiary IP, else just grab one.
//                    if (allMyIps[i].toString().contains("10.2.1")) {
//                        System.out.println("Matched Primary Display IP:" + allMyIps[i]);
//                        ip = allMyIps[i].toString();
//                        return ip;
//                    } 
//                    else if (allMyIps[i].toString().contains("192.168.204")) {
//                        System.out.println("Matched Secondary Display IP:" + allMyIps[i]);
//                        ip = allMyIps[i].toString();
//                        return ip;
//                    } else if (allMyIps[i].toString().contains("192.168.204")) {
//                        System.out.println("Matched Tertiary Display IP:" + allMyIps[i]);
//                        ip = allMyIps[i].toString();
//                        return ip;
//                    } 
//                }
//                //- Just grab an IP
//                try{
//                    InetAddress inetaddress=InetAddress.getLocalHost();
//                    ip = inetaddress.getHostAddress();
//                    System.out.println("Any IP:" + ip);                            
//                    return ip;
//                }
//                catch(UnknownHostException e){ 
//                    return null;
//                }  
//            }
//        } catch (UnknownHostException e) {
//            return null;
//        }
               
        //- Get outbound IP - uses DatagramSocket which may not be good in the eyes of host-based security
//        String ip;
//        try(final DatagramSocket socket = new DatagramSocket()){
//            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
//            ip = socket.getLocalAddress().getHostAddress();
//            return ip;
//        }
//        catch(UnknownHostException e){ 
//            return null;
//        }        
 
    }
    

 
    //- Get local MAC
    public static String getMAC(){
        String nicname = null;
        String ip = null;
        String nicnameip = null;
        String macAddress = "Error";
         try{
//            InetAddress inetaddress=InetAddress.getLocalHost(); //Get LocalHost refrence  

            Enumeration<NetworkInterface> nics = NetworkInterface.getNetworkInterfaces();
            while (nics.hasMoreElements()) {
                NetworkInterface nic = nics.nextElement();
                Enumeration<InetAddress> addrs = nic.getInetAddresses();
                while (addrs.hasMoreElements()) {
                    InetAddress addr = addrs.nextElement();
                    nicname = nic.getName();
                    ip = addr.getHostAddress();
                    nicnameip = nicname + " - " + ip;
                    //- Look for Primary, Secondary or Tertiary IP, else just grab one.
                    if (nicnameip.contains(getIPAddress())) {
                        
                                    //get Network interface Refrence by InetAddress Refrence
            NetworkInterface network = NetworkInterface.getByInetAddress(addr); 
            byte[] macArray = network.getHardwareAddress();  //get Hardware address Array
            StringBuilder str = new StringBuilder();             
            // Convert Array to String 
            //System.out.println(Arrays.toString(macArray));  
            if (macArray == null) {
                macArray = new byte[]{0, 0, 0, 0, 0, 0};
            }
            for (int i = 0; i < macArray.length; i++) {
                    str.append(String.format("%02X%s", macArray[i], (i < macArray.length - 1) ? "-" : ""));
            }
            macAddress=str.toString();         
            return macAddress; //return MAC Address
                    }                    
                }                
            }
        }
        catch(SocketException e){ 
            return null;
        } catch (UnknownHostException ex) { 
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        return macAddress;
    }
    
    private void loadClassification() {  
        String strClassification = PropertyHandler.getInstance().getValue("Setting.Classification");
        System.out.println("Classification: " + strClassification);
        if("Unclassified".equalsIgnoreCase(strClassification)) {
            getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, new Color(0,122,61)));           
        }
        if("Confidential".equalsIgnoreCase(strClassification)) {
            getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, new Color(0,56,168)));           
        }
        if("Secret".equalsIgnoreCase(strClassification)) {
            getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, new Color(206,17,38)));           
        }
        if("Top Secret".equalsIgnoreCase(strClassification)) {
            getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, new Color(249,99,2)));           
        }
        if("SCI".equalsIgnoreCase(strClassification)) {
            getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, new Color(240,240,0)));           
        }        
        if("Coalition".equalsIgnoreCase(strClassification)) {
            getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, new Color(127,127,255)));           
        }
        if("None".equalsIgnoreCase(strClassification)) {
            getRootPane().setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, new Color(0,0,0)));           
        }         
    }
    
    private void loadHostIPMAC() {
        //- Get Hostname IP and MAC
        try {
            String ipaddress = getIPAddress();
            String systemname = getSystemName();
            String macaddress = getMAC();
            jLabelLocalHostname.setText(systemname);
            jLabelLocalHostname.setToolTipText(systemname);
            jLabelLocalIP.setText(ipaddress);
            jLabelLocalMAC.setText(macaddress);
            System.out.println("Host Name : "+systemname);
            System.out.println("Host IP   : "+ipaddress);
            System.out.println("Host MAC  : "+macaddress); 
        } catch (UnknownHostException | SocketException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    
    public void loadSettingsPersonal() {


        //- Set Personal Language Setting 
        try {
            if("Japanese".equals(PropertyHandlerPersonal.getInstance().getValue("Setting.Language"))) {
                jRadioButtonJapanese.setSelected(Boolean.TRUE);
                SetLanguageJapanese();
            }
        } catch (NullPointerException e) {System.out.println("Setting.Language Goofed");
        }
        try {
            if("English".equals(PropertyHandlerPersonal.getInstance().getValue("Setting.Language"))) {
                jRadioButtonEnglish.setSelected(Boolean.TRUE);
            }
        } catch (NullPointerException e) {System.out.println("Setting.Language Goofed");
        }   
        
        //- Set Ping on Session Select
        try {
            if("1".equals(PropertyHandlerPersonal.getInstance().getValue("Setting.ChangePingOnSessionSelect"))) {
                jCheckBoxChangePingOnSessionSelect.setSelected(Boolean.TRUE);
            } else if ("0".equals(PropertyHandlerPersonal.getInstance().getValue("Setting.ChangePingOnSessionSelect"))) {
                jCheckBoxChangePingOnSessionSelect.setSelected(Boolean.FALSE);
            } else {
                PropertyHandlerPersonal.getInstance().setValue("Setting.ChangePingOnSessionSelect", "0");
            }
        } catch (NullPointerException e) {System.out.println("Setting.ChangePingOnSessionSelect Goofed");
        }        
    }
    
    public void loadSettingsShared() {
        //- SecureCRT Path
        jTextFieldSecureCRTPath.setText(PropertyHandler.getInstance().getValue("SecureCRT.Exec"));
        
        //- Preloaded IPs
        jTextFieldPreloadedIP.setText(PropertyHandler.getInstance().getValue("Preload.SSH"));
        jTextFieldPreloadedPingIP.setText(PropertyHandler.getInstance().getValue("Preload.Ping"));

        //- Preferred IP Prefixes
        jTextFieldSettingPreferredIpPrimary.setText(PropertyHandler.getInstance().getValue("Setting.ShowIpPreferredPrimary"));
        jTextFieldSettingPreferredIpSecondary.setText(PropertyHandler.getInstance().getValue("Setting.ShowIpPreferredSecondary"));
        jTextFieldSettingPreferredIpTertiary.setText(PropertyHandler.getInstance().getValue("Setting.ShowIpPreferredTertiary"));
       
        //- Set Local Policy Warnings
        try {
            if("1".equals(PropertyHandler.getInstance().getValue("Setting.LocalPolicyWarning"))) {
                jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Shared Items (Will be overwritten by local policy)"));
                jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Personal Items (Never overwritten)"));                
            } 
            if("0".equals(PropertyHandler.getInstance().getValue("Setting.LocalPolicyWarning"))) {
                jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Shared Items"));
                jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Personal Items"));                
                jLabel1.setText("");
                jLabel2.setText("");
            }
        } catch (NullPointerException e) {System.out.println("Setting.LocalPolicyWarning Goofed");
        }
        
        //- NTP
        jTextFieldNtpServer.setText(PropertyHandler.getInstance().getValue("NTP.Test.IP"));
        
        //- Set PW SSH Auth Default ENABLE or DISABLE
        try {
            if("1".equals(PropertyHandler.getInstance().getValue("Setting.PasswordBasedSSHauthDisable"))) {
                jRadioButtonPWauthDisabled.setSelected(Boolean.TRUE);
                jButtonExecuteFunctionSSH.setBackground(new javax.swing.JButton().getBackground());
                jButtonExecuteFunctionSSH.setEnabled(false);
            }
        } catch (NullPointerException e) {System.out.println("Setting.PasswordBasedSSHauthDisable Goofed");
        }
        try {
            if("0".equals(PropertyHandler.getInstance().getValue("Setting.PasswordBasedSSHauthDisable"))) {
                jRadioButtonPWauthEnabled.setSelected(Boolean.TRUE);
                jButtonExecuteFunctionSSH.setBackground(new Color(200,255,153));
                jButtonExecuteFunctionSSH.setEnabled(Boolean.TRUE);
            }
        } catch (NullPointerException e) {System.out.println("Setting.PasswordBasedSSHauthDisable Goofed");
        }
    }

    
    private void SetLanguageJapanese() {
        jTabbedMain.setTitleAt(0,"メイン"); //Main
        jTabbedMain.setTitleAt(1,"リンク"); //Link
        jTabbedMain.setTitleAt(2,"レファレンス"); //Reference
        jTabbedMain.setTitleAt(3,"スクリプト"); //Script
        jTabbedMain.setTitleAt(4,"ツールボックス"); //Toolbox
        jTabbedMain.setTitleAt(5,"セッティング"); //Setting
        jCheckBoxFavorites.setText("お気に入り");
        jCheckBoxAlternateLogin.setText("代替ログイン");
        jButtonPing.setText("ピング");
        jButtonTracert.setText("トレースルート");
        jButtonTracert.setFont(jButtonTracert.getFont().deriveFont(10.0f));           
        jButtonConsole.setText("シリアルポートに接続します");
        jToggleOnlineOfflineMode.setText("オンライン");
        jTabbedMain.setTitleAt(0,"メイン");
        jTabbedPaneToolBox.setTitleAt(0, "ファイルアーカイブ"); //File Archive
        jTabbedPaneToolBox.setTitleAt(1, "タイプ"); //Type
        jTabbedPaneToolBox.setTitleAt(2, "ハッシュ"); //Hash
        jTabbedPaneToolBox.setTitleAt(3, "NTP"); //NTP
        jTabbedPaneToolBox.setTitleAt(4, "アプリ"); //App
        jTabbedPaneToolBox.setTitleAt(5, "スクリプト"); //Script
        jLabelLinksFilter.setText("フィルタ:");
        jLabelReferenceFilter.setText("フィルタ:");
        jLabelScriptsFilter.setText("フィルタ:");
        jLabelFolderToZip7.setText("暗号化されたアーカイブにフォルダを追加します");
        jLabelFolderToZip4.setText("暗号化されたアーカイブを抽出する");
        jLabelType7reverse.setText("タイプ7リバース");
        jLabelHashGenerator.setText("ハッシュジェネレーター");
        jLabelGetNTP1.setText("NTP時間の取得");
        jPanelWebApps.setBorder(javax.swing.BorderFactory.createTitledBorder("ウェブアプリ"));
        jPanelJavaApps.setBorder(javax.swing.BorderFactory.createTitledBorder("Javaアプリ"));
        jPanelDocuments.setBorder(javax.swing.BorderFactory.createTitledBorder("ドキュメント"));
        jPanelScanning.setBorder(javax.swing.BorderFactory.createTitledBorder("スキャン"));
        jPanelSyncing.setBorder(javax.swing.BorderFactory.createTitledBorder("同期"));
        jPanelMiscellaneous.setBorder(javax.swing.BorderFactory.createTitledBorder("その他"));
        jTabbedPaneSettings.setTitleAt(0, "メイン"); //Main
        jTabbedPaneSettings.setTitleAt(1, "ボタン"); //Button
        jTabbedPaneSettings.setTitleAt(2, "リンク"); //Link
        jTabbedPaneSettings.setTitleAt(3, "スクリプト"); //Script
        jTabbedPaneSettings.setTitleAt(4, "レファレンス"); //Reference
    }
    
    private void loadSettingsMainButtonsData() {
        //- ToolTips
        jTextFieldButtonToolTip1.setText(PropertyHandler.getInstance().getValue("Button.01.ToolTip"));
        jTextFieldButtonToolTip2.setText(PropertyHandler.getInstance().getValue("Button.02.ToolTip"));
        jTextFieldButtonToolTip3.setText(PropertyHandler.getInstance().getValue("Button.03.ToolTip"));
        jTextFieldButtonToolTip4.setText(PropertyHandler.getInstance().getValue("Button.04.ToolTip"));
        jTextFieldButtonToolTip5.setText(PropertyHandler.getInstance().getValue("Button.05.ToolTip"));
        jTextFieldButtonToolTip6.setText(PropertyHandler.getInstance().getValue("Button.06.ToolTip"));
        jTextFieldButtonToolTip7.setText(PropertyHandler.getInstance().getValue("Button.07.ToolTip"));
        jTextFieldButtonToolTip8.setText(PropertyHandler.getInstance().getValue("Button.08.ToolTip"));
        jTextFieldButtonToolTip9.setText(PropertyHandler.getInstance().getValue("Button.09.ToolTip"));
        jTextFieldButtonToolTip10.setText(PropertyHandler.getInstance().getValue("Button.10.ToolTip"));
        jTextFieldButtonToolTip11.setText(PropertyHandler.getInstance().getValue("Button.11.ToolTip"));
        jTextFieldButtonToolTip12.setText(PropertyHandler.getInstance().getValue("Button.12.ToolTip"));
        jTextFieldButtonToolTip13.setText(PropertyHandler.getInstance().getValue("Button.13.ToolTip"));
        jTextFieldButtonToolTip14.setText(PropertyHandler.getInstance().getValue("Button.14.ToolTip"));
        jTextFieldButtonToolTip15.setText(PropertyHandler.getInstance().getValue("Button.15.ToolTip"));
        jTextFieldButtonToolTip16.setText(PropertyHandler.getInstance().getValue("Button.16.ToolTip"));
        jTextFieldButtonToolTip17.setText(PropertyHandler.getInstance().getValue("Button.17.ToolTip"));
        jTextFieldButtonToolTip18.setText(PropertyHandler.getInstance().getValue("Button.18.ToolTip"));
        jTextFieldButtonToolTip19.setText(PropertyHandler.getInstance().getValue("Button.19.ToolTip"));
        jTextFieldButtonToolTip20.setText(PropertyHandler.getInstance().getValue("Button.20.ToolTip"));        
        jTextFieldButtonToolTip21.setText(PropertyHandler.getInstance().getValue("Button.21.ToolTip"));        
        jTextFieldButtonToolTip22.setText(PropertyHandler.getInstance().getValue("Button.22.ToolTip"));        
        jTextFieldButtonToolTip23.setText(PropertyHandler.getInstance().getValue("Button.23.ToolTip"));        
        jTextFieldButtonToolTip24.setText(PropertyHandler.getInstance().getValue("Button.24.ToolTip"));        
        //- Icons - MOVED TO PRE-ACTION LISTENER
//        jComboBoxButtonIcon1.setSelectedItem(PropertyHandler.getInstance().getValue("Button.01.Icon"));
//        jComboBoxButtonIcon2.setSelectedItem(PropertyHandler.getInstance().getValue("Button.02.Icon"));
//        jComboBoxButtonIcon3.setSelectedItem(PropertyHandler.getInstance().getValue("Button.03.Icon"));
//        jComboBoxButtonIcon4.setSelectedItem(PropertyHandler.getInstance().getValue("Button.04.Icon"));
//        jComboBoxButtonIcon5.setSelectedItem(PropertyHandler.getInstance().getValue("Button.05.Icon"));
//        jComboBoxButtonIcon6.setSelectedItem(PropertyHandler.getInstance().getValue("Button.06.Icon"));
//        jComboBoxButtonIcon7.setSelectedItem(PropertyHandler.getInstance().getValue("Button.07.Icon"));
//        jComboBoxButtonIcon8.setSelectedItem(PropertyHandler.getInstance().getValue("Button.08.Icon"));
//        jComboBoxButtonIcon9.setSelectedItem(PropertyHandler.getInstance().getValue("Button.09.Icon"));
//        jComboBoxButtonIcon10.setSelectedItem(PropertyHandler.getInstance().getValue("Button.10.Icon"));
//        jComboBoxButtonIcon11.setSelectedItem(PropertyHandler.getInstance().getValue("Button.11.Icon"));
//        jComboBoxButtonIcon12.setSelectedItem(PropertyHandler.getInstance().getValue("Button.12.Icon"));
//        jComboBoxButtonIcon13.setSelectedItem(PropertyHandler.getInstance().getValue("Button.13.Icon"));
//        jComboBoxButtonIcon14.setSelectedItem(PropertyHandler.getInstance().getValue("Button.14.Icon"));
//        jComboBoxButtonIcon15.setSelectedItem(PropertyHandler.getInstance().getValue("Button.15.Icon"));
//        jComboBoxButtonIcon16.setSelectedItem(PropertyHandler.getInstance().getValue("Button.16.Icon"));
//        jComboBoxButtonIcon17.setSelectedItem(PropertyHandler.getInstance().getValue("Button.17.Icon"));
//        jComboBoxButtonIcon18.setSelectedItem(PropertyHandler.getInstance().getValue("Button.18.Icon"));
//        jComboBoxButtonIcon19.setSelectedItem(PropertyHandler.getInstance().getValue("Button.19.Icon"));
//        jComboBoxButtonIcon20.setSelectedItem(PropertyHandler.getInstance().getValue("Button.20.Icon"));       
        //- Executions
        jTextFieldButtonExecute1.setText(PropertyHandler.getInstance().getValue("Button.01.StrExec"));
        jTextFieldButtonExecute2.setText(PropertyHandler.getInstance().getValue("Button.02.StrExec"));
        jTextFieldButtonExecute3.setText(PropertyHandler.getInstance().getValue("Button.03.StrExec"));
        jTextFieldButtonExecute4.setText(PropertyHandler.getInstance().getValue("Button.04.StrExec"));
        jTextFieldButtonExecute5.setText(PropertyHandler.getInstance().getValue("Button.05.StrExec"));
        jTextFieldButtonExecute6.setText(PropertyHandler.getInstance().getValue("Button.06.StrExec"));
        jTextFieldButtonExecute7.setText(PropertyHandler.getInstance().getValue("Button.07.StrExec"));
        jTextFieldButtonExecute8.setText(PropertyHandler.getInstance().getValue("Button.08.StrExec"));
        jTextFieldButtonExecute9.setText(PropertyHandler.getInstance().getValue("Button.09.StrExec"));
        jTextFieldButtonExecute10.setText(PropertyHandler.getInstance().getValue("Button.10.StrExec"));
        jTextFieldButtonExecute11.setText(PropertyHandler.getInstance().getValue("Button.11.StrExec"));
        jTextFieldButtonExecute12.setText(PropertyHandler.getInstance().getValue("Button.12.StrExec"));
        jTextFieldButtonExecute13.setText(PropertyHandler.getInstance().getValue("Button.13.StrExec"));
        jTextFieldButtonExecute14.setText(PropertyHandler.getInstance().getValue("Button.14.StrExec"));
        jTextFieldButtonExecute15.setText(PropertyHandler.getInstance().getValue("Button.15.StrExec"));
        jTextFieldButtonExecute16.setText(PropertyHandler.getInstance().getValue("Button.16.StrExec"));
        jTextFieldButtonExecute17.setText(PropertyHandler.getInstance().getValue("Button.17.StrExec"));
        jTextFieldButtonExecute18.setText(PropertyHandler.getInstance().getValue("Button.18.StrExec"));
        jTextFieldButtonExecute19.setText(PropertyHandler.getInstance().getValue("Button.19.StrExec"));
        jTextFieldButtonExecute20.setText(PropertyHandler.getInstance().getValue("Button.20.StrExec"));
        jTextFieldButtonExecute21.setText(PropertyHandler.getInstance().getValue("Button.21.StrExec"));
        jTextFieldButtonExecute22.setText(PropertyHandler.getInstance().getValue("Button.22.StrExec"));
        jTextFieldButtonExecute23.setText(PropertyHandler.getInstance().getValue("Button.23.StrExec"));
        jTextFieldButtonExecute24.setText(PropertyHandler.getInstance().getValue("Button.24.StrExec"));
    }
    
    private void loadSettingsLinkData() {
        //- Text
        jTextFieldLinkText1.setText(PropertyHandler.getInstance().getValue("Custom.Link.01.Description"));
        jTextFieldLinkText2.setText(PropertyHandler.getInstance().getValue("Custom.Link.02.Description"));
        jTextFieldLinkText3.setText(PropertyHandler.getInstance().getValue("Custom.Link.03.Description"));
        jTextFieldLinkText4.setText(PropertyHandler.getInstance().getValue("Custom.Link.04.Description"));
        jTextFieldLinkText5.setText(PropertyHandler.getInstance().getValue("Custom.Link.05.Description"));
        jTextFieldLinkText6.setText(PropertyHandler.getInstance().getValue("Custom.Link.06.Description"));
        jTextFieldLinkText7.setText(PropertyHandler.getInstance().getValue("Custom.Link.07.Description"));
        jTextFieldLinkText8.setText(PropertyHandler.getInstance().getValue("Custom.Link.08.Description"));
        jTextFieldLinkText9.setText(PropertyHandler.getInstance().getValue("Custom.Link.09.Description"));
        jTextFieldLinkText10.setText(PropertyHandler.getInstance().getValue("Custom.Link.10.Description"));
        jTextFieldLinkText11.setText(PropertyHandler.getInstance().getValue("Custom.Link.11.Description"));
        jTextFieldLinkText12.setText(PropertyHandler.getInstance().getValue("Custom.Link.12.Description"));
        jTextFieldLinkText13.setText(PropertyHandler.getInstance().getValue("Custom.Link.13.Description"));
        jTextFieldLinkText14.setText(PropertyHandler.getInstance().getValue("Custom.Link.14.Description"));
        jTextFieldLinkText15.setText(PropertyHandler.getInstance().getValue("Custom.Link.15.Description"));
        jTextFieldLinkText16.setText(PropertyHandler.getInstance().getValue("Custom.Link.16.Description"));
        jTextFieldLinkText17.setText(PropertyHandler.getInstance().getValue("Custom.Link.17.Description"));
        jTextFieldLinkText18.setText(PropertyHandler.getInstance().getValue("Custom.Link.18.Description"));
        jTextFieldLinkText19.setText(PropertyHandler.getInstance().getValue("Custom.Link.19.Description"));
        jTextFieldLinkText20.setText(PropertyHandler.getInstance().getValue("Custom.Link.20.Description"));
        jTextFieldLinkText21.setText(PropertyHandler.getInstance().getValue("Custom.Link.21.Description"));
        jTextFieldLinkText22.setText(PropertyHandler.getInstance().getValue("Custom.Link.22.Description"));
        jTextFieldLinkText23.setText(PropertyHandler.getInstance().getValue("Custom.Link.23.Description"));
        jTextFieldLinkText24.setText(PropertyHandler.getInstance().getValue("Custom.Link.24.Description"));
        jTextFieldLinkText25.setText(PropertyHandler.getInstance().getValue("Custom.Link.25.Description"));
        jTextFieldLinkText26.setText(PropertyHandler.getInstance().getValue("Custom.Link.26.Description"));
        jTextFieldLinkText27.setText(PropertyHandler.getInstance().getValue("Custom.Link.27.Description"));
        jTextFieldLinkText28.setText(PropertyHandler.getInstance().getValue("Custom.Link.28.Description"));
        jTextFieldLinkText29.setText(PropertyHandler.getInstance().getValue("Custom.Link.29.Description"));
        jTextFieldLinkText30.setText(PropertyHandler.getInstance().getValue("Custom.Link.30.Description"));
        jTextFieldLinkText31.setText(PropertyHandler.getInstance().getValue("Custom.Link.31.Description"));
        jTextFieldLinkText32.setText(PropertyHandler.getInstance().getValue("Custom.Link.32.Description"));
        jTextFieldLinkText33.setText(PropertyHandler.getInstance().getValue("Custom.Link.33.Description"));
        jTextFieldLinkText34.setText(PropertyHandler.getInstance().getValue("Custom.Link.34.Description"));
        jTextFieldLinkText35.setText(PropertyHandler.getInstance().getValue("Custom.Link.35.Description"));
        jTextFieldLinkText36.setText(PropertyHandler.getInstance().getValue("Custom.Link.36.Description"));
        //- Executions
        jTextFieldLinkExecute1.setText(PropertyHandler.getInstance().getValue("Custom.Link.01.Exec"));
        jTextFieldLinkExecute2.setText(PropertyHandler.getInstance().getValue("Custom.Link.02.Exec"));
        jTextFieldLinkExecute3.setText(PropertyHandler.getInstance().getValue("Custom.Link.03.Exec"));
        jTextFieldLinkExecute4.setText(PropertyHandler.getInstance().getValue("Custom.Link.04.Exec"));
        jTextFieldLinkExecute5.setText(PropertyHandler.getInstance().getValue("Custom.Link.05.Exec"));
        jTextFieldLinkExecute6.setText(PropertyHandler.getInstance().getValue("Custom.Link.06.Exec"));
        jTextFieldLinkExecute7.setText(PropertyHandler.getInstance().getValue("Custom.Link.07.Exec"));
        jTextFieldLinkExecute8.setText(PropertyHandler.getInstance().getValue("Custom.Link.08.Exec"));
        jTextFieldLinkExecute9.setText(PropertyHandler.getInstance().getValue("Custom.Link.09.Exec"));
        jTextFieldLinkExecute10.setText(PropertyHandler.getInstance().getValue("Custom.Link.10.Exec"));
        jTextFieldLinkExecute11.setText(PropertyHandler.getInstance().getValue("Custom.Link.11.Exec"));
        jTextFieldLinkExecute12.setText(PropertyHandler.getInstance().getValue("Custom.Link.12.Exec"));
        jTextFieldLinkExecute13.setText(PropertyHandler.getInstance().getValue("Custom.Link.13.Exec"));
        jTextFieldLinkExecute14.setText(PropertyHandler.getInstance().getValue("Custom.Link.14.Exec"));
        jTextFieldLinkExecute15.setText(PropertyHandler.getInstance().getValue("Custom.Link.15.Exec"));
        jTextFieldLinkExecute16.setText(PropertyHandler.getInstance().getValue("Custom.Link.16.Exec"));
        jTextFieldLinkExecute17.setText(PropertyHandler.getInstance().getValue("Custom.Link.17.Exec"));
        jTextFieldLinkExecute18.setText(PropertyHandler.getInstance().getValue("Custom.Link.18.Exec"));
        jTextFieldLinkExecute19.setText(PropertyHandler.getInstance().getValue("Custom.Link.19.Exec"));
        jTextFieldLinkExecute20.setText(PropertyHandler.getInstance().getValue("Custom.Link.20.Exec"));
        jTextFieldLinkExecute21.setText(PropertyHandler.getInstance().getValue("Custom.Link.21.Exec"));
        jTextFieldLinkExecute22.setText(PropertyHandler.getInstance().getValue("Custom.Link.22.Exec"));
        jTextFieldLinkExecute23.setText(PropertyHandler.getInstance().getValue("Custom.Link.23.Exec"));
        jTextFieldLinkExecute24.setText(PropertyHandler.getInstance().getValue("Custom.Link.24.Exec"));
        jTextFieldLinkExecute25.setText(PropertyHandler.getInstance().getValue("Custom.Link.25.Exec"));
        jTextFieldLinkExecute26.setText(PropertyHandler.getInstance().getValue("Custom.Link.26.Exec"));
        jTextFieldLinkExecute27.setText(PropertyHandler.getInstance().getValue("Custom.Link.27.Exec"));
        jTextFieldLinkExecute28.setText(PropertyHandler.getInstance().getValue("Custom.Link.28.Exec"));
        jTextFieldLinkExecute29.setText(PropertyHandler.getInstance().getValue("Custom.Link.29.Exec"));
        jTextFieldLinkExecute30.setText(PropertyHandler.getInstance().getValue("Custom.Link.30.Exec"));
        jTextFieldLinkExecute31.setText(PropertyHandler.getInstance().getValue("Custom.Link.31.Exec"));
        jTextFieldLinkExecute32.setText(PropertyHandler.getInstance().getValue("Custom.Link.32.Exec"));
        jTextFieldLinkExecute33.setText(PropertyHandler.getInstance().getValue("Custom.Link.33.Exec"));
        jTextFieldLinkExecute34.setText(PropertyHandler.getInstance().getValue("Custom.Link.34.Exec"));
        jTextFieldLinkExecute35.setText(PropertyHandler.getInstance().getValue("Custom.Link.35.Exec"));
        jTextFieldLinkExecute36.setText(PropertyHandler.getInstance().getValue("Custom.Link.36.Exec"));
    }
    

        
    private Map<String, ImageIcon> imageMap;

    public void loadSettingsMainButtonList() {
        //String[] arrIconList = null;
        try {
            arrIconList = this.getResourceListing(launchpad.LaunchPad.class , "launchpad/images/buttons/");
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
//        System.out.println("Button List: " + Arrays.toString(arrIconList));
        Arrays.sort(arrIconList, Collator.getInstance());

        //imageMap = createImageMap(arrIconList);
        
        //return arrIconList;
//        jComboBoxButtonIcon2.setModel(new DefaultComboBoxModel(arrIconList));
//        jComboBoxButtonIcon3.setModel(new DefaultComboBoxModel(arrIconList));
//        jComboBoxButtonIcon4.setModel(new DefaultComboBoxModel(arrIconList));
//        jComboBoxButtonIcon5.setModel(new DefaultComboBoxModel(arrIconList));
//        jComboBoxButtonIcon6.setModel(new DefaultComboBoxModel(arrIconList));
//        jComboBoxButtonIcon7.setModel(new DefaultComboBoxModel(arrIconList));
//        jComboBoxButtonIcon8.setModel(new DefaultComboBoxModel(arrIconList));
//        jComboBoxButtonIcon9.setModel(new DefaultComboBoxModel(arrIconList));
//        jComboBoxButtonIcon10.setModel(new DefaultComboBoxModel(arrIconList));
//        jComboBoxButtonIcon11.setModel(new DefaultComboBoxModel(arrIconList));
//        jComboBoxButtonIcon12.setModel(new DefaultComboBoxModel(arrIconList));
//        jComboBoxButtonIcon13.setModel(new DefaultComboBoxModel(arrIconList));
//        jComboBoxButtonIcon14.setModel(new DefaultComboBoxModel(arrIconList));
//        jComboBoxButtonIcon15.setModel(new DefaultComboBoxModel(arrIconList));
//        jComboBoxButtonIcon16.setModel(new DefaultComboBoxModel(arrIconList));
//        jComboBoxButtonIcon17.setModel(new DefaultComboBoxModel(arrIconList));
//        jComboBoxButtonIcon18.setModel(new DefaultComboBoxModel(arrIconList));
//        jComboBoxButtonIcon19.setModel(new DefaultComboBoxModel(arrIconList));
//        jComboBoxButtonIcon20.setModel(new DefaultComboBoxModel(arrIconList));
    }
    

        
    

    public class ButtonListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setIcon(imageMap.get((String) value));
            label.setHorizontalTextPosition(JLabel.RIGHT);
            //label.setFont(font);
            (imageMap.get((String) value)).setImageObserver(label);
            return label;
        }
    }

    private Map<String, ImageIcon> createImageMap(String[] list) {
        Map<String, ImageIcon> map = new HashMap<>();
        for (String s : list) {
            ImageIcon imageIconPreResize = new ImageIcon( getClass().getResource("/launchpad/images/buttons/" + s));
            Image image = imageIconPreResize.getImage(); // transform it 
            Image newimg = image.getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
            ImageIcon imageIconPostResize= new ImageIcon(newimg);                
            map.put(s, imageIconPostResize );
        }
        return map;
    }
    
    public void restartApplication() throws URISyntaxException, IOException {
        final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        final File currentJar = new File(LaunchPad.class.getProtectionDomain().getCodeSource().getLocation().toURI());

        /* is it a jar file? */
        if(!currentJar.getName().endsWith(".jar"))
          return;

        /* Build command: java -jar application.jar */
        final ArrayList<String> command = new ArrayList<String>();
        command.add(javaBin);
        command.add("-jar");
        command.add(currentJar.getPath());

        final ProcessBuilder builder = new ProcessBuilder(command);
        builder.start();
        System.exit(0);
    }
    
    private void QuickLaunchSessionOnEnter() {
        String myValue = PropertyHandler.getInstance().getValue("ButtonExecuteFunction.OnEnterPress");
        if("".equals(myValue)) {
            PropertyHandler.getInstance().setValue("ButtonExecuteFunction.OnEnterPress", "SSH");
        }
        myValue = PropertyHandler.getInstance().getValue("ButtonExecuteFunction.OnEnterPress");
        if("HTTPS".equals(myValue)) {
            jButtonExecuteFunctionHTTPS.doClick();
        } else if("RDP".equals(myValue)) {
            jButtonExecuteFunctionRDP.doClick();
        } else if("SSH".equals(myValue)){
            jButtonExecuteFunctionSSH.doClick();
        } else if("PKI".equals(myValue)){
            jButtonExecuteFunctionPKI.doClick();
        }
    }
    
    private static final NumberFormat NUMBERFORMATNTP = new java.text.DecimalFormat("0.00");

    
    public void processResponseNTP(TimeInfo info) {
        NtpV3Packet message = info.getMessage();
        int stratum = message.getStratum();
        String refType;
        if (stratum <= 0) {
            refType = "(Unspecified or Unavailable)";
        } else if (stratum == 1) {
            refType = "(Primary Reference; e.g., GPS)"; // GPS, radio clock, etc.
        } else {
            refType = "(Secondary Reference; e.g. via NTP or SNTP)";
        }
        // stratum should be 0..15...
        addToNTPLogWindow(" Stratum: " + stratum + " " + refType);
        int version = message.getVersion();
        int li = message.getLeapIndicator();
        addToNTPLogWindow(" leap=" + li + ", version=" + version + ", precision=" + message.getPrecision());

        addToNTPLogWindow(" mode: " + message.getModeName() + " (" + message.getMode() + ")");
        int poll = message.getPoll();
        // poll value typically btwn MINPOLL (4) and MAXPOLL (14)
        addToNTPLogWindow(" poll: " + (poll <= 0 ? 1 : (int) Math.pow(2, poll)) + " seconds" + " (2 ** " + poll + ")");        
        double disp = message.getRootDispersionInMillisDouble();
        addToNTPLogWindow(" rootdelay=" + NUMBERFORMATNTP.format(message.getRootDelayInMillisDouble()) + ", rootdispersion(ms): " + NUMBERFORMATNTP.format(disp));
        int refId = message.getReferenceId();
        String refAddr = NtpUtils.getHostAddress(refId);
        String refName = null;
        if (refId != 0) {
            if (refAddr.equals("127.127.1.0")) {
                refName = "LOCAL"; // This is the ref address for the Local Clock
            } else if (stratum >= 2) {
                // If reference id has 127.127 prefix then it uses its own reference clock
                // defined in the form 127.127.clock-type.unit-num (e.g. 127.127.8.0 mode 5
                // for GENERIC DCF77 AM; see refclock.htm from the NTP software distribution.
                if (!refAddr.startsWith("127.127")) {
                    try {
                        InetAddress addr = InetAddress.getByName(refAddr);
                        String name = addr.getHostName();
                        if (name != null && !name.equals(refAddr)) {
                            refName = name;
                        }
                    } catch (UnknownHostException e) {
                        // some stratum-2 servers sync to ref clock device but fudge stratum level higher... (e.g. 2)
                        // ref not valid host maybe it's a reference clock name?
                        // otherwise just show the ref IP address.
                        refName = NtpUtils.getReferenceClock(message);
                    }
                }
            } else if (version >= 3 && (stratum == 0 || stratum == 1)) {
                refName = NtpUtils.getReferenceClock(message);
                // refname usually have at least 3 characters (e.g. GPS, WWV, LCL, etc.)
            }
            // otherwise give up on naming the beast...
        }
        if (refName != null && refName.length() > 1) {
            refAddr += " (" + refName + ")";
        }
        addToNTPLogWindow(" Reference Identifier:\t" + refAddr);        

        TimeStamp refNtpTime = message.getReferenceTimeStamp();
        addToNTPLogWindow(" Reference Timestamp:\t" + refNtpTime + "  " + refNtpTime.toDateString());        

        // Originate Time is time request sent by client (t1)
        TimeStamp origNtpTime = message.getOriginateTimeStamp();
        addToNTPLogWindow(" Originate Timestamp:\t" + origNtpTime + "  " + origNtpTime.toDateString());        

        long destTime = info.getReturnTime();
        // Receive Time is time request received by server (t2)
        TimeStamp rcvNtpTime = message.getReceiveTimeStamp();
        addToNTPLogWindow(" Receive Timestamp:\t" + rcvNtpTime + "  " + rcvNtpTime.toDateString());        

        // Transmit time is time reply sent by server (t3)
        TimeStamp xmitNtpTime = message.getTransmitTimeStamp();
        addToNTPLogWindow(" Transmit Timestamp:\t" + xmitNtpTime + "  " + xmitNtpTime.toDateString());        

        // Destination time is time reply received by client (t4)
        TimeStamp destNtpTime = TimeStamp.getNtpTime(destTime);
        addToNTPLogWindow(" Destination Timestamp:\t" + destNtpTime + "  " + destNtpTime.toDateString());        

        info.computeDetails(); // compute offset/delay if not already done
        Long offsetValue = info.getOffset();
        Long delayValue = info.getDelay();
        String delay = (delayValue == null) ? "N/A" : delayValue.toString();
        String offset = (offsetValue == null) ? "N/A" : offsetValue.toString();

        addToNTPLogWindow(" Roundtrip delay(ms)=" + delay + ", clock offset(ms)=" + offset);        

    }
    
    private void addToNTPLogWindow(String strLogLine) {
            try {
                jTextAreaNTPMessage.getDocument().insertString(jTextAreaNTPMessage.getDocument().getEndPosition().getOffset(),strLogLine + "\n", null);
            } catch (BadLocationException ex) {
                Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(strLogLine);
            //- Scroll to bottom
            scrollToBottom(jScrollPaneNTPMessage);
            //jScrollPaneLog.update(jScrollPaneLog.getGraphics());            
            jTextAreaNTPMessage.update(jTextAreaNTPMessage.getGraphics());
            scrollToBottom(jScrollPaneNTPMessage);    
    }
    
    private void scrollToBottom(JScrollPane scrollPane) {
        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        AdjustmentListener downScroller = new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                Adjustable adjustable = e.getAdjustable();
                adjustable.setValue(adjustable.getMaximum());
                verticalBar.removeAdjustmentListener(this);
            }
        };
        verticalBar.addAdjustmentListener(downScroller);
    }
    
    private void copyShortcutToDesktop() {
        try {
            new File(strPathLaunchPadFolder + "/Links/LaunchPad/icons").mkdirs();
            copyInternalFileToSpecifiedDestination("/launchpad/files/launchpad.orig.ico", strPathLaunchPadFolder + "/Links/LaunchPad/icons/launchpad.orig.ico");
            copyInternalFileToSpecifiedDestination("/launchpad/files/LaunchPad.lnk", strPathUserDesktop + "/LaunchPad.lnk");
        } catch (Exception ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            System.out.println("Desktop link: created");
        }
    }
        private void setSessionTextSize() {
        String strSliderValue = String.valueOf(jSliderListTextSize.getValue());
        //System.out.println(strSliderValue);

        if (strSliderValue.equals("0")){
            jListSessions.setFont(jListSessions.getFont().deriveFont(10.0f));
            jLabelListTextSizePreview.setFont(jLabelListTextSizePreview.getFont().deriveFont(10.0f)); 
            //PropertyHandlerPersonal.getInstance().setValue("Setting.TextSize", "0");
        }
        if (strSliderValue.equals("1")){
            jListSessions.setFont(jListSessions.getFont().deriveFont(12.0f));
            jLabelListTextSizePreview.setFont(jLabelListTextSizePreview.getFont().deriveFont(12.0f));
            //PropertyHandlerPersonal.getInstance().setValue("Setting.TextSize", "1");            
        }
        if (strSliderValue.equals("2")){
            jListSessions.setFont(jListSessions.getFont().deriveFont(14.0f));
            jLabelListTextSizePreview.setFont(jLabelListTextSizePreview.getFont().deriveFont(14.0f));
            //PropertyHandlerPersonal.getInstance().setValue("Setting.TextSize", "2");            
        }
        if (strSliderValue.equals("3")){
            jListSessions.setFont(jListSessions.getFont().deriveFont(16.0f));
            jLabelListTextSizePreview.setFont(jLabelListTextSizePreview.getFont().deriveFont(16.0f));
            //PropertyHandlerPersonal.getInstance().setValue("Setting.TextSize", "3");            
        }
        if (strSliderValue.equals("4")){
            jListSessions.setFont(jListSessions.getFont().deriveFont(18.0f));
            jLabelListTextSizePreview.setFont(jLabelListTextSizePreview.getFont().deriveFont(18.0f));
            //PropertyHandlerPersonal.getInstance().setValue("Setting.TextSize", "4");            
        }
        if (strSliderValue.equals("5")){
            jListSessions.setFont(jListSessions.getFont().deriveFont(20.0f));
            jLabelListTextSizePreview.setFont(jLabelListTextSizePreview.getFont().deriveFont(20.0f));
            //PropertyHandlerPersonal.getInstance().setValue("Setting.TextSize", "5");            
        }
        if (strSliderValue.equals("6")){
            jListSessions.setFont(jListSessions.getFont().deriveFont(22.0f));
            jLabelListTextSizePreview.setFont(jLabelListTextSizePreview.getFont().deriveFont(22.0f));
            //PropertyHandlerPersonal.getInstance().setValue("Setting.TextSize", "6");            
        }
        }


  
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupConsoleClient;
    private javax.swing.ButtonGroup buttonGroupLanguage;
    private javax.swing.ButtonGroup buttonGroupPWauthEnableDisable;
    private javax.swing.ButtonGroup buttonGroupSSHClient;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton35;
    private javax.swing.JButton jButton36;
    private javax.swing.JButton jButton37;
    private javax.swing.JButton jButton38;
    private javax.swing.JButton jButton39;
    private javax.swing.JButton jButton40;
    private javax.swing.JButton jButton41;
    private javax.swing.JButton jButton42;
    private javax.swing.JButton jButton43;
    private javax.swing.JButton jButton44;
    private javax.swing.JButton jButton45;
    private javax.swing.JButton jButton46;
    private javax.swing.JButton jButtonCiscoType7Decrypt;
    private javax.swing.JButton jButtonClearFilter;
    private javax.swing.JButton jButtonConfigBuilder1;
    private javax.swing.JButton jButtonConsole;
    private javax.swing.JButton jButtonCopyShortcutToDesktop;
    private javax.swing.JButton jButtonEditProductionDevicesList;
    private javax.swing.JButton jButtonEditProductionDevicesList1;
    private javax.swing.JButton jButtonExecuteFunctionHTTPS;
    private javax.swing.JButton jButtonExecuteFunctionPKI;
    private javax.swing.JButton jButtonExecuteFunctionRDP;
    private javax.swing.JButton jButtonExecuteFunctionSSH;
    private javax.swing.JButton jButtonFolderToZip;
    private javax.swing.JButton jButtonGenerateHash;
    private javax.swing.JButton jButtonHashCopyMD5;
    private javax.swing.JButton jButtonHashCopySHA1;
    private javax.swing.JButton jButtonHashCopySHA256;
    private javax.swing.JButton jButtonHashCopySHA512;
    private javax.swing.JButton jButtonIPv4SubnetCalculator;
    private javax.swing.JButton jButtonIconGenerator;
    private javax.swing.JButton jButtonJSDiff2;
    private javax.swing.JButton jButtonLinkCustom01;
    private javax.swing.JButton jButtonLinkCustom02;
    private javax.swing.JButton jButtonLinkCustom03;
    private javax.swing.JButton jButtonLinkCustom04;
    private javax.swing.JButton jButtonLinkCustom05;
    private javax.swing.JButton jButtonLinkCustom06;
    private javax.swing.JButton jButtonLinkCustom07;
    private javax.swing.JButton jButtonLinkCustom08;
    private javax.swing.JButton jButtonLinkCustom09;
    private javax.swing.JButton jButtonLinkCustom10;
    private javax.swing.JButton jButtonLinkCustom11;
    private javax.swing.JButton jButtonLinkCustom12;
    private javax.swing.JButton jButtonLinkCustom13;
    private javax.swing.JButton jButtonLinkCustom14;
    private javax.swing.JButton jButtonLinkCustom15;
    private javax.swing.JButton jButtonLinkCustom16;
    private javax.swing.JButton jButtonLinkCustom17;
    private javax.swing.JButton jButtonLinkCustom18;
    private javax.swing.JButton jButtonLinkCustom19;
    private javax.swing.JButton jButtonLinkCustom20;
    private javax.swing.JButton jButtonLinkCustom21;
    private javax.swing.JButton jButtonLinkCustom22;
    private javax.swing.JButton jButtonLinkCustom23;
    private javax.swing.JButton jButtonLinkCustom24;
    private javax.swing.JButton jButtonLinkCustom25;
    private javax.swing.JButton jButtonLinkCustom26;
    private javax.swing.JButton jButtonLinkCustom27;
    private javax.swing.JButton jButtonLinkCustom28;
    private javax.swing.JButton jButtonLinkCustom29;
    private javax.swing.JButton jButtonLinkCustom30;
    private javax.swing.JButton jButtonLinkCustom31;
    private javax.swing.JButton jButtonLinkCustom32;
    private javax.swing.JButton jButtonLinkCustom33;
    private javax.swing.JButton jButtonLinkCustom34;
    private javax.swing.JButton jButtonLinkCustom35;
    private javax.swing.JButton jButtonLinkCustom36;
    private javax.swing.JButton jButtonMainButton1;
    private javax.swing.JButton jButtonMainButton10;
    private javax.swing.JButton jButtonMainButton11;
    private javax.swing.JButton jButtonMainButton12;
    private javax.swing.JButton jButtonMainButton13;
    private javax.swing.JButton jButtonMainButton14;
    private javax.swing.JButton jButtonMainButton15;
    private javax.swing.JButton jButtonMainButton16;
    private javax.swing.JButton jButtonMainButton17;
    private javax.swing.JButton jButtonMainButton18;
    private javax.swing.JButton jButtonMainButton19;
    private javax.swing.JButton jButtonMainButton2;
    private javax.swing.JButton jButtonMainButton20;
    private javax.swing.JButton jButtonMainButton21;
    private javax.swing.JButton jButtonMainButton22;
    private javax.swing.JButton jButtonMainButton23;
    private javax.swing.JButton jButtonMainButton24;
    private javax.swing.JButton jButtonMainButton3;
    private javax.swing.JButton jButtonMainButton4;
    private javax.swing.JButton jButtonMainButton5;
    private javax.swing.JButton jButtonMainButton6;
    private javax.swing.JButton jButtonMainButton7;
    private javax.swing.JButton jButtonMainButton8;
    private javax.swing.JButton jButtonMainButton9;
    private javax.swing.JButton jButtonMapSharedFolder;
    private javax.swing.JButton jButtonNetworkConnections;
    private javax.swing.JButton jButtonPing;
    public javax.swing.JButton jButtonReferenceCustom01;
    public javax.swing.JButton jButtonReferenceCustom02;
    public javax.swing.JButton jButtonReferenceCustom03;
    public javax.swing.JButton jButtonReferenceCustom04;
    public javax.swing.JButton jButtonReferenceCustom05;
    public javax.swing.JButton jButtonReferenceCustom06;
    public javax.swing.JButton jButtonReferenceCustom07;
    public javax.swing.JButton jButtonReferenceCustom08;
    public javax.swing.JButton jButtonReferenceCustom09;
    public javax.swing.JButton jButtonReferenceCustom10;
    public javax.swing.JButton jButtonReferenceCustom11;
    public javax.swing.JButton jButtonReferenceCustom12;
    public javax.swing.JButton jButtonReferenceCustom13;
    public javax.swing.JButton jButtonReferenceCustom14;
    public javax.swing.JButton jButtonReferenceCustom15;
    public javax.swing.JButton jButtonReferenceCustom16;
    public javax.swing.JButton jButtonReferenceCustom17;
    public javax.swing.JButton jButtonReferenceCustom18;
    public javax.swing.JButton jButtonReferenceCustom19;
    public javax.swing.JButton jButtonReferenceCustom20;
    public javax.swing.JButton jButtonReferenceCustom21;
    public javax.swing.JButton jButtonReferenceCustom22;
    public javax.swing.JButton jButtonReferenceCustom23;
    public javax.swing.JButton jButtonReferenceCustom24;
    public javax.swing.JButton jButtonReferenceCustom25;
    public javax.swing.JButton jButtonReferenceCustom26;
    public javax.swing.JButton jButtonReferenceCustom27;
    public javax.swing.JButton jButtonReferenceCustom28;
    public javax.swing.JButton jButtonReferenceCustom29;
    public javax.swing.JButton jButtonReferenceCustom30;
    public javax.swing.JButton jButtonReferenceCustom31;
    public javax.swing.JButton jButtonReferenceCustom32;
    public javax.swing.JButton jButtonReferenceCustom33;
    private javax.swing.JButton jButtonRefreshHostnameIPMAC;
    private javax.swing.JButton jButtonReportIssue;
    private javax.swing.JButton jButtonRomajiToHiraKata;
    private javax.swing.JButton jButtonScriptCreateDummyFile;
    private javax.swing.JButton jButtonScriptCustom01;
    private javax.swing.JButton jButtonScriptCustom02;
    private javax.swing.JButton jButtonScriptCustom03;
    private javax.swing.JButton jButtonScriptCustom04;
    private javax.swing.JButton jButtonScriptCustom05;
    private javax.swing.JButton jButtonScriptCustom06;
    private javax.swing.JButton jButtonScriptCustom07;
    private javax.swing.JButton jButtonScriptCustom08;
    private javax.swing.JButton jButtonScriptCustom09;
    private javax.swing.JButton jButtonScriptCustom10;
    private javax.swing.JButton jButtonScriptCustom11;
    private javax.swing.JButton jButtonScriptCustom12;
    private javax.swing.JButton jButtonScriptCustom13;
    private javax.swing.JButton jButtonScriptCustom14;
    private javax.swing.JButton jButtonScriptCustom15;
    private javax.swing.JButton jButtonScriptCustom16;
    private javax.swing.JButton jButtonScriptCustom17;
    private javax.swing.JButton jButtonScriptCustom18;
    private javax.swing.JButton jButtonScriptCustom19;
    private javax.swing.JButton jButtonScriptCustom20;
    private javax.swing.JButton jButtonScriptCustom21;
    private javax.swing.JButton jButtonScriptCustom22;
    private javax.swing.JButton jButtonScriptCustom23;
    private javax.swing.JButton jButtonScriptCustom24;
    private javax.swing.JButton jButtonScriptCustom25;
    private javax.swing.JButton jButtonScriptCustom26;
    private javax.swing.JButton jButtonScriptCustom27;
    private javax.swing.JButton jButtonScriptCustom28;
    private javax.swing.JButton jButtonScriptCustom29;
    private javax.swing.JButton jButtonScriptCustom30;
    private javax.swing.JButton jButtonScriptCustom31;
    private javax.swing.JButton jButtonScriptCustom32;
    private javax.swing.JButton jButtonScriptCustom33;
    private javax.swing.JButton jButtonScriptCustom34;
    private javax.swing.JButton jButtonScriptCustom35;
    private javax.swing.JButton jButtonScriptCustom36;
    private javax.swing.JButton jButtonScriptGetNTPTimePS;
    private javax.swing.JButton jButtonScriptHashChecker;
    private javax.swing.JButton jButtonScriptHashChecker1;
    private javax.swing.JButton jButtonScriptKeepAwake;
    private javax.swing.JButton jButtonScriptMTUSweep;
    private javax.swing.JButton jButtonScriptPingLoggerToFile;
    private javax.swing.JButton jButtonScriptPowershellPingSweepRange;
    private javax.swing.JButton jButtonScriptSendMessage;
    private javax.swing.JButton jButtonScriptTestUDPTCP;
    private javax.swing.JButton jButtonScriptUpdateLaunchPad;
    private javax.swing.JButton jButtonScriptiPerfClient;
    private javax.swing.JButton jButtonScriptiPerfServer;
    private javax.swing.JButton jButtonShowCOMList;
    private javax.swing.JButton jButtonSubnetCalculator;
    private javax.swing.JButton jButtonSubnetCalculator1;
    private javax.swing.JButton jButtonSyncProductionDevices;
    private javax.swing.JButton jButtonSyncStandaloneDevices;
    private javax.swing.JButton jButtonTCP;
    private javax.swing.JButton jButtonTracert;
    private javax.swing.JButton jButtonViewExampleUpdateScript;
    private javax.swing.JButton jButtonZipBrowseDestinationFolder;
    private javax.swing.JButton jButtonZipBrowseSourceZip;
    private javax.swing.JCheckBox jCheckBoxAlternateLogin;
    private javax.swing.JCheckBox jCheckBoxChangePingOnSessionSelect;
    private javax.swing.JCheckBox jCheckBoxDNS;
    private javax.swing.JCheckBox jCheckBoxFavorites;
    private javax.swing.JComboBox<String> jComboBoxButtonIcon1;
    private javax.swing.JComboBox<String> jComboBoxButtonIcon10;
    private javax.swing.JComboBox<String> jComboBoxButtonIcon11;
    private javax.swing.JComboBox<String> jComboBoxButtonIcon12;
    private javax.swing.JComboBox<String> jComboBoxButtonIcon13;
    private javax.swing.JComboBox<String> jComboBoxButtonIcon14;
    private javax.swing.JComboBox<String> jComboBoxButtonIcon15;
    private javax.swing.JComboBox<String> jComboBoxButtonIcon16;
    private javax.swing.JComboBox<String> jComboBoxButtonIcon17;
    private javax.swing.JComboBox<String> jComboBoxButtonIcon18;
    private javax.swing.JComboBox<String> jComboBoxButtonIcon19;
    private javax.swing.JComboBox<String> jComboBoxButtonIcon2;
    private javax.swing.JComboBox<String> jComboBoxButtonIcon20;
    private javax.swing.JComboBox<String> jComboBoxButtonIcon21;
    private javax.swing.JComboBox<String> jComboBoxButtonIcon22;
    private javax.swing.JComboBox<String> jComboBoxButtonIcon23;
    private javax.swing.JComboBox<String> jComboBoxButtonIcon24;
    private javax.swing.JComboBox<String> jComboBoxButtonIcon3;
    private javax.swing.JComboBox<String> jComboBoxButtonIcon4;
    private javax.swing.JComboBox<String> jComboBoxButtonIcon5;
    private javax.swing.JComboBox<String> jComboBoxButtonIcon6;
    private javax.swing.JComboBox<String> jComboBoxButtonIcon7;
    private javax.swing.JComboBox<String> jComboBoxButtonIcon8;
    private javax.swing.JComboBox<String> jComboBoxButtonIcon9;
    private javax.swing.JComboBox<String> jComboBoxClassification;
    private javax.swing.JComboBox<String> jComboBoxConsoleBaud;
    private javax.swing.JComboBox<String> jComboBoxConsoleCOM;
    private javax.swing.JComboBox<String> jComboBoxSessionDoubleClick;
    private javax.swing.JComboBox<String> jComboBoxSessionEnterPress;
    private javax.swing.JComboBox<String> jComboBoxZipEncMethod;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabelButtonExecute1;
    private javax.swing.JLabel jLabelButtonExecute10;
    private javax.swing.JLabel jLabelButtonExecute11;
    private javax.swing.JLabel jLabelButtonExecute12;
    private javax.swing.JLabel jLabelButtonExecute13;
    private javax.swing.JLabel jLabelButtonExecute14;
    private javax.swing.JLabel jLabelButtonExecute15;
    private javax.swing.JLabel jLabelButtonExecute16;
    private javax.swing.JLabel jLabelButtonExecute17;
    private javax.swing.JLabel jLabelButtonExecute18;
    private javax.swing.JLabel jLabelButtonExecute19;
    private javax.swing.JLabel jLabelButtonExecute2;
    private javax.swing.JLabel jLabelButtonExecute20;
    private javax.swing.JLabel jLabelButtonExecute21;
    private javax.swing.JLabel jLabelButtonExecute22;
    private javax.swing.JLabel jLabelButtonExecute23;
    private javax.swing.JLabel jLabelButtonExecute24;
    private javax.swing.JLabel jLabelButtonExecute3;
    private javax.swing.JLabel jLabelButtonExecute4;
    private javax.swing.JLabel jLabelButtonExecute5;
    private javax.swing.JLabel jLabelButtonExecute6;
    private javax.swing.JLabel jLabelButtonExecute7;
    private javax.swing.JLabel jLabelButtonExecute8;
    private javax.swing.JLabel jLabelButtonExecute9;
    private javax.swing.JLabel jLabelButtonIcon1;
    private javax.swing.JLabel jLabelButtonIcon10;
    private javax.swing.JLabel jLabelButtonIcon11;
    private javax.swing.JLabel jLabelButtonIcon12;
    private javax.swing.JLabel jLabelButtonIcon13;
    private javax.swing.JLabel jLabelButtonIcon14;
    private javax.swing.JLabel jLabelButtonIcon15;
    private javax.swing.JLabel jLabelButtonIcon16;
    private javax.swing.JLabel jLabelButtonIcon17;
    private javax.swing.JLabel jLabelButtonIcon18;
    private javax.swing.JLabel jLabelButtonIcon19;
    private javax.swing.JLabel jLabelButtonIcon2;
    private javax.swing.JLabel jLabelButtonIcon20;
    private javax.swing.JLabel jLabelButtonIcon21;
    private javax.swing.JLabel jLabelButtonIcon22;
    private javax.swing.JLabel jLabelButtonIcon23;
    private javax.swing.JLabel jLabelButtonIcon24;
    private javax.swing.JLabel jLabelButtonIcon3;
    private javax.swing.JLabel jLabelButtonIcon4;
    private javax.swing.JLabel jLabelButtonIcon5;
    private javax.swing.JLabel jLabelButtonIcon6;
    private javax.swing.JLabel jLabelButtonIcon7;
    private javax.swing.JLabel jLabelButtonIcon8;
    private javax.swing.JLabel jLabelButtonIcon9;
    private javax.swing.JLabel jLabelButtonToolTip1;
    private javax.swing.JLabel jLabelButtonToolTip10;
    private javax.swing.JLabel jLabelButtonToolTip11;
    private javax.swing.JLabel jLabelButtonToolTip12;
    private javax.swing.JLabel jLabelButtonToolTip13;
    private javax.swing.JLabel jLabelButtonToolTip14;
    private javax.swing.JLabel jLabelButtonToolTip15;
    private javax.swing.JLabel jLabelButtonToolTip16;
    private javax.swing.JLabel jLabelButtonToolTip17;
    private javax.swing.JLabel jLabelButtonToolTip18;
    private javax.swing.JLabel jLabelButtonToolTip19;
    private javax.swing.JLabel jLabelButtonToolTip2;
    private javax.swing.JLabel jLabelButtonToolTip20;
    private javax.swing.JLabel jLabelButtonToolTip21;
    private javax.swing.JLabel jLabelButtonToolTip22;
    private javax.swing.JLabel jLabelButtonToolTip23;
    private javax.swing.JLabel jLabelButtonToolTip24;
    private javax.swing.JLabel jLabelButtonToolTip3;
    private javax.swing.JLabel jLabelButtonToolTip4;
    private javax.swing.JLabel jLabelButtonToolTip5;
    private javax.swing.JLabel jLabelButtonToolTip6;
    private javax.swing.JLabel jLabelButtonToolTip7;
    private javax.swing.JLabel jLabelButtonToolTip8;
    private javax.swing.JLabel jLabelButtonToolTip9;
    private javax.swing.JLabel jLabelEnablePWauth;
    private javax.swing.JLabel jLabelEnablePWauth1;
    private javax.swing.JLabel jLabelEnablePWauth2;
    private javax.swing.JLabel jLabelEnablePWauth3;
    private javax.swing.JLabel jLabelEnablePWauth4;
    private javax.swing.JLabel jLabelEnablePWauth5;
    private javax.swing.JLabel jLabelFolderToZip4;
    private javax.swing.JLabel jLabelFolderToZip7;
    private javax.swing.JLabel jLabelGetNTP1;
    private javax.swing.JLabel jLabelHashGenerator;
    private javax.swing.JLabel jLabelLanguageSelect;
    private javax.swing.JLabel jLabelLinkExecute1;
    private javax.swing.JLabel jLabelLinkExecute10;
    private javax.swing.JLabel jLabelLinkExecute11;
    private javax.swing.JLabel jLabelLinkExecute12;
    private javax.swing.JLabel jLabelLinkExecute13;
    private javax.swing.JLabel jLabelLinkExecute14;
    private javax.swing.JLabel jLabelLinkExecute15;
    private javax.swing.JLabel jLabelLinkExecute16;
    private javax.swing.JLabel jLabelLinkExecute17;
    private javax.swing.JLabel jLabelLinkExecute18;
    private javax.swing.JLabel jLabelLinkExecute19;
    private javax.swing.JLabel jLabelLinkExecute2;
    private javax.swing.JLabel jLabelLinkExecute20;
    private javax.swing.JLabel jLabelLinkExecute21;
    private javax.swing.JLabel jLabelLinkExecute22;
    private javax.swing.JLabel jLabelLinkExecute23;
    private javax.swing.JLabel jLabelLinkExecute24;
    private javax.swing.JLabel jLabelLinkExecute25;
    private javax.swing.JLabel jLabelLinkExecute26;
    private javax.swing.JLabel jLabelLinkExecute27;
    private javax.swing.JLabel jLabelLinkExecute28;
    private javax.swing.JLabel jLabelLinkExecute29;
    private javax.swing.JLabel jLabelLinkExecute3;
    private javax.swing.JLabel jLabelLinkExecute30;
    private javax.swing.JLabel jLabelLinkExecute31;
    private javax.swing.JLabel jLabelLinkExecute32;
    private javax.swing.JLabel jLabelLinkExecute33;
    private javax.swing.JLabel jLabelLinkExecute34;
    private javax.swing.JLabel jLabelLinkExecute35;
    private javax.swing.JLabel jLabelLinkExecute36;
    private javax.swing.JLabel jLabelLinkExecute4;
    private javax.swing.JLabel jLabelLinkExecute5;
    private javax.swing.JLabel jLabelLinkExecute6;
    private javax.swing.JLabel jLabelLinkExecute7;
    private javax.swing.JLabel jLabelLinkExecute8;
    private javax.swing.JLabel jLabelLinkExecute9;
    private javax.swing.JLabel jLabelLinkText1;
    private javax.swing.JLabel jLabelLinkText10;
    private javax.swing.JLabel jLabelLinkText11;
    private javax.swing.JLabel jLabelLinkText12;
    private javax.swing.JLabel jLabelLinkText13;
    private javax.swing.JLabel jLabelLinkText14;
    private javax.swing.JLabel jLabelLinkText15;
    private javax.swing.JLabel jLabelLinkText16;
    private javax.swing.JLabel jLabelLinkText17;
    private javax.swing.JLabel jLabelLinkText18;
    private javax.swing.JLabel jLabelLinkText19;
    private javax.swing.JLabel jLabelLinkText2;
    private javax.swing.JLabel jLabelLinkText20;
    private javax.swing.JLabel jLabelLinkText21;
    private javax.swing.JLabel jLabelLinkText22;
    private javax.swing.JLabel jLabelLinkText23;
    private javax.swing.JLabel jLabelLinkText24;
    private javax.swing.JLabel jLabelLinkText25;
    private javax.swing.JLabel jLabelLinkText26;
    private javax.swing.JLabel jLabelLinkText27;
    private javax.swing.JLabel jLabelLinkText28;
    private javax.swing.JLabel jLabelLinkText29;
    private javax.swing.JLabel jLabelLinkText3;
    private javax.swing.JLabel jLabelLinkText30;
    private javax.swing.JLabel jLabelLinkText31;
    private javax.swing.JLabel jLabelLinkText32;
    private javax.swing.JLabel jLabelLinkText33;
    private javax.swing.JLabel jLabelLinkText34;
    private javax.swing.JLabel jLabelLinkText35;
    private javax.swing.JLabel jLabelLinkText36;
    private javax.swing.JLabel jLabelLinkText4;
    private javax.swing.JLabel jLabelLinkText5;
    private javax.swing.JLabel jLabelLinkText6;
    private javax.swing.JLabel jLabelLinkText7;
    private javax.swing.JLabel jLabelLinkText8;
    private javax.swing.JLabel jLabelLinkText9;
    private javax.swing.JLabel jLabelLinksFilter;
    private javax.swing.JLabel jLabelListTextSize1;
    private javax.swing.JLabel jLabelListTextSize3;
    private javax.swing.JLabel jLabelListTextSize4;
    private javax.swing.JLabel jLabelListTextSizePreview;
    private javax.swing.JLabel jLabelLocalHostname;
    private javax.swing.JLabel jLabelLocalIP;
    private javax.swing.JLabel jLabelLocalMAC;
    private javax.swing.JLabel jLabelReferenceFilter;
    private javax.swing.JLabel jLabelScriptsFilter;
    private javax.swing.JLabel jLabelType7reverse;
    private javax.swing.JList<String> jListSessions;

    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelAppsCustom;
    private javax.swing.JPanel jPanelDocuments;
    private javax.swing.JPanel jPanelHashGen;
    private javax.swing.JPanel jPanelJavaApps;
    private javax.swing.JPanel jPanelMain;
    private javax.swing.JPanel jPanelMainRightSide;
    private javax.swing.JPanel jPanelMiscellaneous;
    private javax.swing.JPanel jPanelNTPTime;
    private javax.swing.JPanel jPanelReference;
    private javax.swing.JPanel jPanelScanning;
    private javax.swing.JPanel jPanelScripts;
    private javax.swing.JPanel jPanelSettingsButtons;
    private javax.swing.JPanel jPanelSettingsLinks;
    private javax.swing.JPanel jPanelSettingsMain;
    private javax.swing.JPanel jPanelSyncing;
    private javax.swing.JPanel jPanelToolboxScripts;
    private javax.swing.JPanel jPanelType7;
    private javax.swing.JPanel jPanelWebApps;
    private javax.swing.JPanel jPanelWebJavaDocs;
    private javax.swing.JPanel jPanelZipEncrypt;
    private javax.swing.JPasswordField jPasswordFieldConnectPassword;
    private javax.swing.JPasswordField jPasswordFieldZip;
    private javax.swing.JPasswordField jPasswordFieldZipConfirm;
    private javax.swing.JPasswordField jPasswordFieldZipExtract;
    private javax.swing.JProgressBar jProgressBarZip;
    private javax.swing.JProgressBar jProgressBarZipExtract;
    private javax.swing.JRadioButton jRadioButtonEnglish;
    private javax.swing.JRadioButton jRadioButtonJapanese;
    private javax.swing.JRadioButton jRadioButtonPWauthDisabled;
    private javax.swing.JRadioButton jRadioButtonPWauthEnabled;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPaneNTPMessage;
    private javax.swing.JScrollPane jScrollPaneSessionList;
    private javax.swing.JScrollPane jScrollPaneSettingsButtons;
    private javax.swing.JScrollPane jScrollPaneSettingsLinks;
    private javax.swing.JScrollPane jScrollPaneSettingsReferences;
    private javax.swing.JScrollPane jScrollPaneSettingsScripts;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JSlider jSliderListTextSize;
    private javax.swing.JTabbedPane jTabbedMain;
    private javax.swing.JTabbedPane jTabbedPaneSettings;
    private javax.swing.JTabbedPane jTabbedPaneToolBox;
    private javax.swing.JTextArea jTextAreaHash;
    private javax.swing.JTextArea jTextAreaNTPMessage;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextFieldButtonExecute1;
    private javax.swing.JTextField jTextFieldButtonExecute10;
    private javax.swing.JTextField jTextFieldButtonExecute11;
    private javax.swing.JTextField jTextFieldButtonExecute12;
    private javax.swing.JTextField jTextFieldButtonExecute13;
    private javax.swing.JTextField jTextFieldButtonExecute14;
    private javax.swing.JTextField jTextFieldButtonExecute15;
    private javax.swing.JTextField jTextFieldButtonExecute16;
    private javax.swing.JTextField jTextFieldButtonExecute17;
    private javax.swing.JTextField jTextFieldButtonExecute18;
    private javax.swing.JTextField jTextFieldButtonExecute19;
    private javax.swing.JTextField jTextFieldButtonExecute2;
    private javax.swing.JTextField jTextFieldButtonExecute20;
    private javax.swing.JTextField jTextFieldButtonExecute21;
    private javax.swing.JTextField jTextFieldButtonExecute22;
    private javax.swing.JTextField jTextFieldButtonExecute23;
    private javax.swing.JTextField jTextFieldButtonExecute24;
    private javax.swing.JTextField jTextFieldButtonExecute3;
    private javax.swing.JTextField jTextFieldButtonExecute4;
    private javax.swing.JTextField jTextFieldButtonExecute5;
    private javax.swing.JTextField jTextFieldButtonExecute6;
    private javax.swing.JTextField jTextFieldButtonExecute7;
    private javax.swing.JTextField jTextFieldButtonExecute8;
    private javax.swing.JTextField jTextFieldButtonExecute9;
    private javax.swing.JTextField jTextFieldButtonToolTip1;
    private javax.swing.JTextField jTextFieldButtonToolTip10;
    private javax.swing.JTextField jTextFieldButtonToolTip11;
    private javax.swing.JTextField jTextFieldButtonToolTip12;
    private javax.swing.JTextField jTextFieldButtonToolTip13;
    private javax.swing.JTextField jTextFieldButtonToolTip14;
    private javax.swing.JTextField jTextFieldButtonToolTip15;
    private javax.swing.JTextField jTextFieldButtonToolTip16;
    private javax.swing.JTextField jTextFieldButtonToolTip17;
    private javax.swing.JTextField jTextFieldButtonToolTip18;
    private javax.swing.JTextField jTextFieldButtonToolTip19;
    private javax.swing.JTextField jTextFieldButtonToolTip2;
    private javax.swing.JTextField jTextFieldButtonToolTip20;
    private javax.swing.JTextField jTextFieldButtonToolTip21;
    private javax.swing.JTextField jTextFieldButtonToolTip22;
    private javax.swing.JTextField jTextFieldButtonToolTip23;
    private javax.swing.JTextField jTextFieldButtonToolTip24;
    private javax.swing.JTextField jTextFieldButtonToolTip3;
    private javax.swing.JTextField jTextFieldButtonToolTip4;
    private javax.swing.JTextField jTextFieldButtonToolTip5;
    private javax.swing.JTextField jTextFieldButtonToolTip6;
    private javax.swing.JTextField jTextFieldButtonToolTip7;
    private javax.swing.JTextField jTextFieldButtonToolTip8;
    private javax.swing.JTextField jTextFieldButtonToolTip9;
    private javax.swing.JTextField jTextFieldConnectHostname;
    private javax.swing.JTextField jTextFieldConnectUsername;
    private javax.swing.JTextField jTextFieldFileHashGenerate;
    private javax.swing.JTextField jTextFieldFilter;
    private javax.swing.JTextField jTextFieldHashMD5;
    private javax.swing.JTextField jTextFieldHashSHA1;
    private javax.swing.JTextField jTextFieldHashSHA256;
    private javax.swing.JTextField jTextFieldHashSHA512;
    private javax.swing.JTextField jTextFieldLinkExecute1;
    private javax.swing.JTextField jTextFieldLinkExecute10;
    private javax.swing.JTextField jTextFieldLinkExecute11;
    private javax.swing.JTextField jTextFieldLinkExecute12;
    private javax.swing.JTextField jTextFieldLinkExecute13;
    private javax.swing.JTextField jTextFieldLinkExecute14;
    private javax.swing.JTextField jTextFieldLinkExecute15;
    private javax.swing.JTextField jTextFieldLinkExecute16;
    private javax.swing.JTextField jTextFieldLinkExecute17;
    private javax.swing.JTextField jTextFieldLinkExecute18;
    private javax.swing.JTextField jTextFieldLinkExecute19;
    private javax.swing.JTextField jTextFieldLinkExecute2;
    private javax.swing.JTextField jTextFieldLinkExecute20;
    private javax.swing.JTextField jTextFieldLinkExecute21;
    private javax.swing.JTextField jTextFieldLinkExecute22;
    private javax.swing.JTextField jTextFieldLinkExecute23;
    private javax.swing.JTextField jTextFieldLinkExecute24;
    private javax.swing.JTextField jTextFieldLinkExecute25;
    private javax.swing.JTextField jTextFieldLinkExecute26;
    private javax.swing.JTextField jTextFieldLinkExecute27;
    private javax.swing.JTextField jTextFieldLinkExecute28;
    private javax.swing.JTextField jTextFieldLinkExecute29;
    private javax.swing.JTextField jTextFieldLinkExecute3;
    private javax.swing.JTextField jTextFieldLinkExecute30;
    private javax.swing.JTextField jTextFieldLinkExecute31;
    private javax.swing.JTextField jTextFieldLinkExecute32;
    private javax.swing.JTextField jTextFieldLinkExecute33;
    private javax.swing.JTextField jTextFieldLinkExecute34;
    private javax.swing.JTextField jTextFieldLinkExecute35;
    private javax.swing.JTextField jTextFieldLinkExecute36;
    private javax.swing.JTextField jTextFieldLinkExecute4;
    private javax.swing.JTextField jTextFieldLinkExecute5;
    private javax.swing.JTextField jTextFieldLinkExecute6;
    private javax.swing.JTextField jTextFieldLinkExecute7;
    private javax.swing.JTextField jTextFieldLinkExecute8;
    private javax.swing.JTextField jTextFieldLinkExecute9;
    private javax.swing.JTextField jTextFieldLinkText1;
    private javax.swing.JTextField jTextFieldLinkText10;
    private javax.swing.JTextField jTextFieldLinkText11;
    private javax.swing.JTextField jTextFieldLinkText12;
    private javax.swing.JTextField jTextFieldLinkText13;
    private javax.swing.JTextField jTextFieldLinkText14;
    private javax.swing.JTextField jTextFieldLinkText15;
    private javax.swing.JTextField jTextFieldLinkText16;
    private javax.swing.JTextField jTextFieldLinkText17;
    private javax.swing.JTextField jTextFieldLinkText18;
    private javax.swing.JTextField jTextFieldLinkText19;
    private javax.swing.JTextField jTextFieldLinkText2;
    private javax.swing.JTextField jTextFieldLinkText20;
    private javax.swing.JTextField jTextFieldLinkText21;
    private javax.swing.JTextField jTextFieldLinkText22;
    private javax.swing.JTextField jTextFieldLinkText23;
    private javax.swing.JTextField jTextFieldLinkText24;
    private javax.swing.JTextField jTextFieldLinkText25;
    private javax.swing.JTextField jTextFieldLinkText26;
    private javax.swing.JTextField jTextFieldLinkText27;
    private javax.swing.JTextField jTextFieldLinkText28;
    private javax.swing.JTextField jTextFieldLinkText29;
    private javax.swing.JTextField jTextFieldLinkText3;
    private javax.swing.JTextField jTextFieldLinkText30;
    private javax.swing.JTextField jTextFieldLinkText31;
    private javax.swing.JTextField jTextFieldLinkText32;
    private javax.swing.JTextField jTextFieldLinkText33;
    private javax.swing.JTextField jTextFieldLinkText34;
    private javax.swing.JTextField jTextFieldLinkText35;
    private javax.swing.JTextField jTextFieldLinkText36;
    private javax.swing.JTextField jTextFieldLinkText4;
    private javax.swing.JTextField jTextFieldLinkText5;
    private javax.swing.JTextField jTextFieldLinkText6;
    private javax.swing.JTextField jTextFieldLinkText7;
    private javax.swing.JTextField jTextFieldLinkText8;
    private javax.swing.JTextField jTextFieldLinkText9;
    private javax.swing.JTextField jTextFieldLinksFilter;
    private javax.swing.JTextField jTextFieldNtpServer;
    private javax.swing.JTextField jTextFieldPingHostname;
    private javax.swing.JTextField jTextFieldPreloadedIP;
    private javax.swing.JTextField jTextFieldPreloadedPingIP;
    private javax.swing.JTextField jTextFieldReferenceFilter;
    private javax.swing.JTextField jTextFieldScriptsFilter;
    private javax.swing.JTextField jTextFieldSecureCRTPath;
    private javax.swing.JTextField jTextFieldSettingPreferredIpPrimary;
    private javax.swing.JTextField jTextFieldSettingPreferredIpSecondary;
    private javax.swing.JTextField jTextFieldSettingPreferredIpTertiary;
    private javax.swing.JTextField jTextFieldTCPTestPort;
    private javax.swing.JTextField jTextFieldType7Input;
    private javax.swing.JTextField jTextFieldType7Output;
    private javax.swing.JTextField jTextFieldZipDestinationFolder;
    private javax.swing.JTextField jTextFieldZipFilename;
    private javax.swing.JTextField jTextFieldZipSourceFile;
    private javax.swing.JTextField jTextFieldZipSourceFolder;
    private javax.swing.JToggleButton jToggleOnlineOfflineMode;
    // End of variables declaration//GEN-END:variables




}