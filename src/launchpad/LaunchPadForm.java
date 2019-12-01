/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package launchpad;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.embed.swing.JFXPanel;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.xml.bind.DatatypeConverter;
import static launchpad.LaunchPadForm.HashGenerate.toHex;
import launchpad.Type7Reverse.CiscoVigenere;
import launchpad.ntp.NtpMessage;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.progress.ProgressMonitor;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.apache.commons.net.ntp.TimeStamp;

/**
 *
 * @author Mathew
 */
public class LaunchPadForm extends javax.swing.JFrame {
    
    //Shared and public items
    DefaultListModel defaultListModelFilteredItems = new DefaultListModel();
    File pathWorkingDirectory = new File(System.getProperty("user.dir"));
    File pathDesktop = new File(System.getProperty("user.home"), "Desktop");
    String pathUserProfile = System.getenv("USERPROFILE");
    File pathLogging = new File(pathDesktop + "\\Logging-Output");
    String strSessionListFavorites = pathUserProfile + "\\.launchpad\\SessionList.csv";
    String strSessionListDefault = pathDesktop + "\\LaunchPad\\SessionList.csv";

    //--- Get Date and time for things
    SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("yyyyMMdd_HHmm-ssSSS");
    String dateTime = simpleDateFormat.format(new Date());

               

    
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
     */
    public LaunchPadForm() throws IOException, FileNotFoundException, URISyntaxException, AWTException, InterruptedException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        initComponents();
final JFXPanel fxPanel = new JFXPanel();
        setTitle(PropertyHandler.getInstance().getValue("WindowTitle"));
        //importSessionList();
        getSessionList();

        //updateSessionList();       
        try {
        UIManager.setLookAndFeel(
            UIManager.getCrossPlatformLookAndFeelClassName());
        // turn off bold fonts
        UIManager.put("swing.boldMetal", Boolean.FALSE);

        // re-install the Metal Look and Feel
        UIManager.setLookAndFeel(new MetalLookAndFeel());

        // Update the ComponentUIs for all Components. This
        // needs to be invoked for all windows.
        SwingUtilities.updateComponentTreeUI(this);
                
                
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }





        //--- Apply button icons and set size
        Integer buttonHeightWidth = 40;
        ImageIcon icon;
        Image img;
        Image newimg;
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        String strClassification = PropertyHandler.getInstance().getValue("SettingClassification");
        System.out.println("Classification: " + strClassification);
        Color strClassificationColor = new Color(4,159,168);
        if("unclassified".equalsIgnoreCase(strClassification)) {
                    strClassificationColor = new Color(0,122,61);
        }
        if("confidential".equalsIgnoreCase(strClassification)) {
                    strClassificationColor = new Color(0,56,168);
        }
        if("secret".equalsIgnoreCase(strClassification)) {
                    strClassificationColor = new Color(206,17,38);
        }
        if("top secret".equalsIgnoreCase(strClassification)) {
                    strClassificationColor = new Color(249,99,2);
        }
        if("sci".equalsIgnoreCase(strClassification)) {
                    strClassificationColor = new Color(240,240,0);
        }        
        if("coalition".equalsIgnoreCase(strClassification)) {
                    strClassificationColor = new Color(127,127,255);
        } 
        if("none".equalsIgnoreCase(strClassification)) {
                    strClassificationColor = new Color(0,0,0);
        }         
        getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, strClassificationColor));


        addWindowListener(new WindowAdapter() {
            @Override
            //--- Listen for window close
            public void windowClosing(WindowEvent we)
            { 
                String ObjButtons[] = {"Yes","No"};
                int PromptResult = JOptionPane.showOptionDialog(null,"Are you sure you want to exit LaunchPad?","You sure??",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,null,ObjButtons,ObjButtons[1]);
                if(PromptResult==JOptionPane.YES_OPTION)
                {
                    System.exit(0);
                }
            }
            //--- Gray out the username and password when loaded
            public void windowOpened(WindowEvent e) {
                jTextFieldConnectUsername.setEnabled(false);
                jPasswordFieldConnectPassword.setEnabled(false);
            }
        });
        

        
        //--- Listen for credential checkbox

        jCheckBoxAlternateLogin.addItemListener(new ItemListener(){
                public void itemStateChanged(ItemEvent e) {
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
                }
            });
        
        //--- Listen for online/offline reference button
        jToggleOfflineMode.addItemListener((ItemEvent ev) -> {
            if(ev.getStateChange()==ItemEvent.SELECTED){
                System.out.println("Offline is selected");
                jToggleOfflineMode.setText("Offline");
                jToggleOfflineMode.setBackground(Color.GRAY);
            } else if(ev.getStateChange()==ItemEvent.DESELECTED){
                System.out.println("Offline is not selected");
                jToggleOfflineMode.setText("Online");
                jToggleOfflineMode.setBackground(Color.GREEN);
            }
        });
        
        //--- Set function buttons
        try {
            //button 1
            String myValue = PropertyHandler.getInstance().getValue("ButtonExecuteFunction1");
            if("".equals(myValue)) {
                PropertyHandler.getInstance().setValue("ButtonExecuteFunction1", "HTTPS");
            }
            jButtonExecuteFunction1.setText(PropertyHandler.getInstance().getValue("ButtonExecuteFunction1"));

        } catch (NullPointerException e) {System.out.println("Button1 Goofed");
        }
        try {
            //button 2
            String myValue = PropertyHandler.getInstance().getValue("ButtonExecuteFunction2");
            if("".equals(myValue)) {
                PropertyHandler.getInstance().setValue("ButtonExecuteFunction2", "RDP");
            }         
            jButtonExecuteFunction2.setText(PropertyHandler.getInstance().getValue("ButtonExecuteFunction2"));

        } catch (NullPointerException e) {System.out.println("Button2 Goofed");
        }
        try {
            //button 3
            String myValue = PropertyHandler.getInstance().getValue("ButtonExecuteFunction3");
            if("".equals(myValue)) {
                PropertyHandler.getInstance().setValue("ButtonExecuteFunction3", "SSH");
            }        
            jButtonExecuteFunction3.setText(PropertyHandler.getInstance().getValue("ButtonExecuteFunction3"));            
        } catch (NullPointerException e) {System.out.println("Button3 Goofed");
        }        

        //--- Apply images to buttons
        try {
            //Button01
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button01icon") + ".png"));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButton1.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed"); StringBuilder sb = new StringBuilder(e.toString());            for (StackTraceElement ste : e.getStackTrace()) {                sb.append("\n\tat ");                sb.append(ste);            }            String trace = sb.toString();            JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);
        }
        try {
            //Button2
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button02icon") + ".png"));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButton2.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed"); StringBuilder sb = new StringBuilder(e.toString());            for (StackTraceElement ste : e.getStackTrace()) {                sb.append("\n\tat ");                sb.append(ste);            }            String trace = sb.toString();            JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);
        }
        try {
            //Button3
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button03icon") + ".png"));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButton3.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed"); StringBuilder sb = new StringBuilder(e.toString());            for (StackTraceElement ste : e.getStackTrace()) {                sb.append("\n\tat ");                sb.append(ste);            }            String trace = sb.toString();            JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);
        }
        try {
            //Button4
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button04icon") + ".png"));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButton4.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed"); StringBuilder sb = new StringBuilder(e.toString());            for (StackTraceElement ste : e.getStackTrace()) {                sb.append("\n\tat ");                sb.append(ste);            }            String trace = sb.toString();            JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);
        }
        try {
            //Button5
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button05icon") + ".png"));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButton5.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed"); StringBuilder sb = new StringBuilder(e.toString());            for (StackTraceElement ste : e.getStackTrace()) {                sb.append("\n\tat ");                sb.append(ste);            }            String trace = sb.toString();            JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);
        }
        try {
            //Button6
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button06icon") + ".png"));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButton6.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed"); StringBuilder sb = new StringBuilder(e.toString());            for (StackTraceElement ste : e.getStackTrace()) {                sb.append("\n\tat ");                sb.append(ste);            }            String trace = sb.toString();            JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);
        }
        try {
            //Button7
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button07icon") + ".png"));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButton7.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed"); StringBuilder sb = new StringBuilder(e.toString());            for (StackTraceElement ste : e.getStackTrace()) {                sb.append("\n\tat ");                sb.append(ste);            }            String trace = sb.toString();            JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);
        }
        try {
            //Button8
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button08icon") + ".png"));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButton8.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed"); StringBuilder sb = new StringBuilder(e.toString());            for (StackTraceElement ste : e.getStackTrace()) {                sb.append("\n\tat ");                sb.append(ste);            }            String trace = sb.toString();            JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);
        }
        try {
            //Button9
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button09icon") + ".png"));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButton9.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed"); StringBuilder sb = new StringBuilder(e.toString());            for (StackTraceElement ste : e.getStackTrace()) {                sb.append("\n\tat ");                sb.append(ste);            }            String trace = sb.toString();            JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);
        }
        try {
            //Button10
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button10icon") + ".png"));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButton10.setIcon(new ImageIcon(newimg));
         } catch (NullPointerException e) {System.out.println("Icon Goofed"); StringBuilder sb = new StringBuilder(e.toString());            for (StackTraceElement ste : e.getStackTrace()) {                sb.append("\n\tat ");                sb.append(ste);            }            String trace = sb.toString();            JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);
        }
        try {
           //Button11
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button11icon") + ".png"));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButton11.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed"); StringBuilder sb = new StringBuilder(e.toString());            for (StackTraceElement ste : e.getStackTrace()) {                sb.append("\n\tat ");                sb.append(ste);            }            String trace = sb.toString();            JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);
        }
        try {
            //Button12
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button12icon") + ".png"));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButton12.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed"); StringBuilder sb = new StringBuilder(e.toString());            for (StackTraceElement ste : e.getStackTrace()) {                sb.append("\n\tat ");                sb.append(ste);            }            String trace = sb.toString();            JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);
        }
        try {
            //Button13
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button13icon") + ".png"));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButton13.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed"); StringBuilder sb = new StringBuilder(e.toString());            for (StackTraceElement ste : e.getStackTrace()) {                sb.append("\n\tat ");                sb.append(ste);            }            String trace = sb.toString();            JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);
        }
        try {
            //Button14
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button14icon") + ".png"));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButton14.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed"); StringBuilder sb = new StringBuilder(e.toString());            for (StackTraceElement ste : e.getStackTrace()) {                sb.append("\n\tat ");                sb.append(ste);            }            String trace = sb.toString();            JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);
        }
        try {
            //Button15
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button15icon") + ".png"));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButton15.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed"); StringBuilder sb = new StringBuilder(e.toString());            for (StackTraceElement ste : e.getStackTrace()) {                sb.append("\n\tat ");                sb.append(ste);            }            String trace = sb.toString();            JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);
        }
        try {
            //Button16
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button16icon") + ".png"));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButton16.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed"); StringBuilder sb = new StringBuilder(e.toString());            for (StackTraceElement ste : e.getStackTrace()) {                sb.append("\n\tat ");                sb.append(ste);            }            String trace = sb.toString();            JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);
        }
        try {
            //Button17
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button17icon") + ".png"));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButton17.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed"); StringBuilder sb = new StringBuilder(e.toString());            for (StackTraceElement ste : e.getStackTrace()) {                sb.append("\n\tat ");                sb.append(ste);            }            String trace = sb.toString();            JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);
        }
        try {
            //Button18
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button18icon") + ".png"));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButton18.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed"); StringBuilder sb = new StringBuilder(e.toString());            for (StackTraceElement ste : e.getStackTrace()) {                sb.append("\n\tat ");                sb.append(ste);            }            String trace = sb.toString();            JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);
        }
        try {
            //Button19
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button19icon") + ".png"));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButton19.setIcon(new ImageIcon(newimg));
        } catch (NullPointerException e) {System.out.println("Icon Goofed"); StringBuilder sb = new StringBuilder(e.toString());            for (StackTraceElement ste : e.getStackTrace()) {                sb.append("\n\tat ");                sb.append(ste);            }            String trace = sb.toString();            JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);
        }
        try {
            //Button20
            icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button20icon") + ".png"));
            img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButton20.setIcon(new ImageIcon(newimg));


        } catch (NullPointerException e) {
            System.out.println("Icon Goofed");
                StringBuilder sb = new StringBuilder(e.toString());
            for (StackTraceElement ste : e.getStackTrace()) {
                sb.append("\n\tat ");
                sb.append(ste);

            }
            String trace = sb.toString();
            JOptionPane.showMessageDialog(null, trace, "An Icon Goofed", 1);

        }
        

//        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button01icon") + ".png")));
//        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button02icon") + ".png")));
//        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button03icon") + ".png")));
//        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button04icon") + ".png")));
//        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button05icon") + ".png")));
//        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button06icon") + ".png")));
//        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button07icon") + ".png")));
//        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button08icon") + ".png")));
//        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button09icon") + ".png")));
//        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button10icon") + ".png")));
//        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button11icon") + ".png")));
//        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button12icon") + ".png")));
//        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button13icon") + ".png")));
//        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button14icon") + ".png")));
//        jButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button15icon") + ".png")));
//        jButton16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button16icon") + ".png")));
//        jButton17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button17icon") + ".png")));
//        jButton18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button18icon") + ".png")));
//        jButton19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button19icon") + ".png")));
//        jButton20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button20icon") + ".png")));

    

        //--- Load preloaded IPs
        jTextFieldConnectHostname.setText(PropertyHandler.getInstance().getValue("PreloadSSH"));
        jTextFieldPingHostname.setText(PropertyHandler.getInstance().getValue("PreloadPing"));

        //--- Load preloaded Zip Items
        jTextFieldZipSourceFolder.setText(PropertyHandler.getInstance().getValue("ZipDefaultSourceFolder").replace("%USERPROFILE%", pathUserProfile));
        jTextFieldZipDestinationFolder.setText(PropertyHandler.getInstance().getValue("ZipDefaultDestinationFolder").replace("%USERPROFILE%", pathUserProfile));
 
        
        //--- Load tooltips
        jButton1.setToolTipText(PropertyHandler.getInstance().getValue("Button01ToolTip"));
        jButton2.setToolTipText(PropertyHandler.getInstance().getValue("Button02ToolTip"));
        jButton3.setToolTipText(PropertyHandler.getInstance().getValue("Button03ToolTip"));
        jButton4.setToolTipText(PropertyHandler.getInstance().getValue("Button04ToolTip"));
        jButton5.setToolTipText(PropertyHandler.getInstance().getValue("Button05ToolTip"));
        jButton6.setToolTipText(PropertyHandler.getInstance().getValue("Button06ToolTip"));
        jButton7.setToolTipText(PropertyHandler.getInstance().getValue("Button07ToolTip"));
        jButton8.setToolTipText(PropertyHandler.getInstance().getValue("Button08ToolTip"));
        jButton9.setToolTipText(PropertyHandler.getInstance().getValue("Button09ToolTip"));
        jButton10.setToolTipText(PropertyHandler.getInstance().getValue("Button10ToolTip"));
        jButton11.setToolTipText(PropertyHandler.getInstance().getValue("Button11ToolTip"));
        jButton12.setToolTipText(PropertyHandler.getInstance().getValue("Button12ToolTip"));
        jButton13.setToolTipText(PropertyHandler.getInstance().getValue("Button13ToolTip"));
        jButton14.setToolTipText(PropertyHandler.getInstance().getValue("Button14ToolTip"));
        jButton15.setToolTipText(PropertyHandler.getInstance().getValue("Button15ToolTip"));
        jButton16.setToolTipText(PropertyHandler.getInstance().getValue("Button16ToolTip"));
        jButton17.setToolTipText(PropertyHandler.getInstance().getValue("Button17ToolTip"));
        jButton18.setToolTipText(PropertyHandler.getInstance().getValue("Button18ToolTip"));
        jButton19.setToolTipText(PropertyHandler.getInstance().getValue("Button19ToolTip"));
        jButton20.setToolTipText(PropertyHandler.getInstance().getValue("Button20ToolTip"));
        
       
        //--- Create directories if not exist
        //new File(pathLogging.toString()).mkdirs();
        
        //--- Set Zip Text
        String strPathDesktop = pathDesktop.toString();
        jTextFieldZipFilename.setText(strPathDesktop + "\\Backup_" + dateTime + ".zip");
        
        
        //--- Load Reference button text
        jButtonReferenceCustom01.setText(PropertyHandler.getInstance().getValue("ReferenceFile01Text"));
        jButtonReferenceCustom02.setText(PropertyHandler.getInstance().getValue("ReferenceFile02Text"));
        jButtonReferenceCustom03.setText(PropertyHandler.getInstance().getValue("ReferenceFile03Text"));
        jButtonReferenceCustom04.setText(PropertyHandler.getInstance().getValue("ReferenceFile04Text"));
        jButtonReferenceCustom05.setText(PropertyHandler.getInstance().getValue("ReferenceFile05Text"));
        jButtonReferenceCustom06.setText(PropertyHandler.getInstance().getValue("ReferenceFile06Text"));


        //--- Load Script button text
        jButtonCustomScript01.setText(PropertyHandler.getInstance().getValue("ScriptCustom01Text"));
        jButtonCustomScript02.setText(PropertyHandler.getInstance().getValue("ScriptCustom02Text"));
        jButtonCustomScript03.setText(PropertyHandler.getInstance().getValue("ScriptCustom03Text"));
        jButtonCustomScript01.setText(PropertyHandler.getInstance().getValue("ScriptCustom04Text"));
        jButtonCustomScript02.setText(PropertyHandler.getInstance().getValue("ScriptCustom05Text"));
        jButtonCustomScript03.setText(PropertyHandler.getInstance().getValue("ScriptCustom06Text"));
       
        //--- Chat stuff
        //REMOVED

        //--- Populate Button Listing
        //Get image list
//        String[] arrButtonList = null;
//        try {
//            arrButtonList = this.getResourceListing(launchpad.LaunchPad.class , "launchpad/images/buttons/");
//        } catch (URISyntaxException | IOException ex) {
//            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        System.out.println(Arrays.toString(arrButtonList));
        //Add to list
//        DefaultListModel listModel = new DefaultListModel();
//        Map<String, ImageIcon> imageMap;
//        //imageMap = createimagemap(arrButtonList);
//        for (String strButton : arrButtonList) {
//            System.out.println(strButton);
//            listModel.addElement(strButton);
//        }
//        jListButtonListing.setModel(listModel);
        
        
        
        //--- Populate Button Listing
        //Get image list
//        String[] arrButtonList = null;
//        try {
//            arrButtonList = this.getResourceListing(launchpad.LaunchPad.class , "launchpad/images/buttons/");
//        } catch (URISyntaxException | IOException ex) {
//            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        System.out.println(Arrays.toString(arrButtonList));
        
        //Add to list
//        DefaultListModel listModel = new DefaultListModel();
//        Map<String, ImageIcon> imageMap;
//        //imageMap = createimagemap(arrButtonList);                        
//        for (String strButton : arrButtonList) {
//            System.out.println(strButton);
//            listModel.addElement(strButton);
//        }
//        jListButtonListing.setModel(listModel);

        
        
        
      
                
                
                
        
    }
    
    private final class ButtonList {
        
        private Map<String, ImageIcon> imageMap;

        public ButtonList() {
            String[] arrButtonList = null;
            try {
                arrButtonList = this.getResourceListing(launchpad.LaunchPad.class , "launchpad/images/buttons/");
            } catch (URISyntaxException | IOException ex) {
                Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Button List: " + Arrays.toString(arrButtonList));
            imageMap = createImageMap(arrButtonList);
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
        private String name;
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
        buttonGroup1 = new javax.swing.ButtonGroup();
        jTabbedMain = new javax.swing.JTabbedPane();
        jPanelMain = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListSessions = new javax.swing.JList<>();
        jPanel1 = new javax.swing.JPanel();
        jTextFieldConnectHostname = new javax.swing.JTextField();
        jButtonExecuteFunction1 = new javax.swing.JButton();
        jButtonExecuteFunction3 = new javax.swing.JButton();
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
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jCheckBoxAlternateLogin = new javax.swing.JCheckBox();
        jButtonExecuteFunction2 = new javax.swing.JButton();
        jTextFieldFilter = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jTabbedPanelLinks = new javax.swing.JTabbedPane();
        jPanelAppsCustom = new javax.swing.JPanel();
        jButtonLinkCustom01 = new javax.swing.JButton();
        jButtonAppCustom05 = new javax.swing.JButton();
        jButtonAppCustom06 = new javax.swing.JButton();
        jButtonAppCustom8 = new javax.swing.JButton();
        jButtonAppCustom9 = new javax.swing.JButton();
        jButtonAppCustom10 = new javax.swing.JButton();
        jButtonAppCustom11 = new javax.swing.JButton();
        jButtonAppCustom12 = new javax.swing.JButton();
        jButtonAppCustom13 = new javax.swing.JButton();
        jButtonAppCustom3 = new javax.swing.JButton();
        jButtonAppCustom2 = new javax.swing.JButton();
        jButtonAppCustom4 = new javax.swing.JButton();
        jButtonAppCustom5 = new javax.swing.JButton();
        jButtonAppCustom6 = new javax.swing.JButton();
        jButtonAppCustom7 = new javax.swing.JButton();
        jButtonAppCustom14 = new javax.swing.JButton();
        jButtonAppCustom15 = new javax.swing.JButton();
        jButtonAppCustom16 = new javax.swing.JButton();
        jButtonAppCustom17 = new javax.swing.JButton();
        jButtonAppCustom18 = new javax.swing.JButton();
        jButtonAppCustom19 = new javax.swing.JButton();
        jButtonAppCustom20 = new javax.swing.JButton();
        jButtonAppCustom21 = new javax.swing.JButton();
        jButtonAppCustom22 = new javax.swing.JButton();
        jButtonAppCustom23 = new javax.swing.JButton();
        jButtonAppCustom24 = new javax.swing.JButton();
        jButtonAppCustom25 = new javax.swing.JButton();
        jButtonAppCustom27 = new javax.swing.JButton();
        jButtonAppCustom28 = new javax.swing.JButton();
        jButtonAppCustom29 = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        jPanelAppsEmbedded = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jButtonJSDiff = new javax.swing.JButton();
        jButtonConfigBuilder = new javax.swing.JButton();
        jButtonJSDiff1 = new javax.swing.JButton();
        jButton28 = new javax.swing.JButton();
        jButton30 = new javax.swing.JButton();
        jSeparator10 = new javax.swing.JSeparator();
        jButton35 = new javax.swing.JButton();
        jLabel36 = new javax.swing.JLabel();
        jTabbedPaneScripts = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jButtonCustomScript03 = new javax.swing.JButton();
        jButtonCustomScript01 = new javax.swing.JButton();
        jButtonCustomScript02 = new javax.swing.JButton();
        jButtonCustomScript06 = new javax.swing.JButton();
        jButtonCustomScript04 = new javax.swing.JButton();
        jButtonCustomScript05 = new javax.swing.JButton();
        jButtonCustomScript8 = new javax.swing.JButton();
        jButtonCustomScript9 = new javax.swing.JButton();
        jButtonCustomScript07 = new javax.swing.JButton();
        jButtonCustomScript4 = new javax.swing.JButton();
        jButtonCustomScript11 = new javax.swing.JButton();
        jButtonCustomScript3 = new javax.swing.JButton();
        jButtonCustomScript2 = new javax.swing.JButton();
        jButtonCustomScript12 = new javax.swing.JButton();
        jButtonCustomScript13 = new javax.swing.JButton();
        jButtonCustomScript14 = new javax.swing.JButton();
        jButtonCustomScript15 = new javax.swing.JButton();
        jButtonCustomScript16 = new javax.swing.JButton();
        jButtonCustomScript17 = new javax.swing.JButton();
        jButtonCustomScript18 = new javax.swing.JButton();
        jButtonCustomScript19 = new javax.swing.JButton();
        jButtonCustomScript20 = new javax.swing.JButton();
        jButtonCustomScript21 = new javax.swing.JButton();
        jButtonCustomScript22 = new javax.swing.JButton();
        jButtonCustomScript23 = new javax.swing.JButton();
        jButtonCustomScript24 = new javax.swing.JButton();
        jButtonCustomScript25 = new javax.swing.JButton();
        jButtonCustomScript26 = new javax.swing.JButton();
        jButtonCustomScript27 = new javax.swing.JButton();
        jButtonCustomScript28 = new javax.swing.JButton();
        jLabel30 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jButtonScriptSyncStandalones = new javax.swing.JButton();
        jButtonScriptBackupShares = new javax.swing.JButton();
        jButtonScriptSyncStandalones1 = new javax.swing.JButton();
        jTabbedPaneReference = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jToggleOfflineMode = new javax.swing.JToggleButton();
        jButtonReferenceCustom06 = new javax.swing.JButton();
        jButtonReferenceCustom01 = new javax.swing.JButton();
        jButtonReferenceCustom02 = new javax.swing.JButton();
        jButtonReferenceCustom03 = new javax.swing.JButton();
        jButtonReferenceCustom04 = new javax.swing.JButton();
        jButtonReferenceCustom05 = new javax.swing.JButton();
        jButtonReferenceCustom7 = new javax.swing.JButton();
        jButtonReferenceCustom2 = new javax.swing.JButton();
        jButtonReferenceCustom3 = new javax.swing.JButton();
        jButtonReferenceCustom4 = new javax.swing.JButton();
        jButtonReferenceCustom5 = new javax.swing.JButton();
        jButtonReferenceCustom6 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jButton24 = new javax.swing.JButton();
        jButton36 = new javax.swing.JButton();
        jButton37 = new javax.swing.JButton();
        jTabbedPaneToolBox = new javax.swing.JTabbedPane();
        jPanel7 = new javax.swing.JPanel();
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
        jPanel8 = new javax.swing.JPanel();
        jSeparator8 = new javax.swing.JSeparator();
        jTextFieldType7Input = new javax.swing.JTextField();
        jButton21 = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();
        jTextFieldType7Output = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jButton23 = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
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
        jLabelFolderToZip5 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabelFolderToZip3 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jButton26 = new javax.swing.JButton();
        jTextFieldNtpServer = new javax.swing.JTextField();
        jTextFieldNtpAtomicTime = new javax.swing.JTextField();
        jTextFieldNtpSystemTime = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jButton29 = new javax.swing.JButton();
        jLabelFolderToZip6 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextAreaNTPMessage = new javax.swing.JTextArea();
        jPanelSettings = new javax.swing.JPanel();
        jLabelSSHClient = new javax.swing.JLabel();
        jRadioButtonSSHClientSecureCRT = new javax.swing.JRadioButton();
        jRadioButtonSSHClientPuTTY = new javax.swing.JRadioButton();
        jLabelListTextSizePreview = new javax.swing.JLabel();
        jRadioButtonConsolePutty = new javax.swing.JRadioButton();
        jRadioButtonConsoleSecureCRT = new javax.swing.JRadioButton();
        jSliderListTextSize = new javax.swing.JSlider();
        jLabelConsoleClient = new javax.swing.JLabel();
        jLabelListTextSize1 = new javax.swing.JLabel();
        jButton31 = new javax.swing.JButton();
        jButton32 = new javax.swing.JButton();
        jButton33 = new javax.swing.JButton();
        jButton34 = new javax.swing.JButton();
        jTextField2 = new javax.swing.JTextField();
        jButtonScriptUpdateLaunchPad = new javax.swing.JButton();
        jButtonReportIssue = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("LaunchPad - Pre-Alpha");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setSize(new java.awt.Dimension(550, 600));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jTabbedMain.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jTabbedMain.setPreferredSize(new java.awt.Dimension(577, 531));

        jPanelMain.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jPanelMain.setPreferredSize(new java.awt.Dimension(500, 503));

        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.LINE_AXIS));

        jListSessions.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
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
        jScrollPane1.setViewportView(jListSessions);
        jListSessions.addMouseListener(new MyMouseListener());

        jPanel1.setLayout(null);

        jTextFieldConnectHostname.setFont(new java.awt.Font("Arial Unicode MS", 0, 10)); // NOI18N
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
        jPanel1.add(jTextFieldConnectHostname);
        jTextFieldConnectHostname.setBounds(10, 10, 120, 20);

        jButtonExecuteFunction1.setBackground(new java.awt.Color(255, 204, 153));
        jButtonExecuteFunction1.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        jButtonExecuteFunction1.setText("HTTPS");
        jButtonExecuteFunction1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonExecuteFunction1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExecuteFunction1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonExecuteFunction1);
        jButtonExecuteFunction1.setBounds(140, 10, 60, 23);

        jButtonExecuteFunction3.setBackground(new java.awt.Color(0, 204, 102));
        jButtonExecuteFunction3.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        jButtonExecuteFunction3.setText("SSH");
        jButtonExecuteFunction3.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonExecuteFunction3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExecuteFunction3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonExecuteFunction3);
        jButtonExecuteFunction3.setBounds(140, 70, 60, 20);

        jTextFieldConnectUsername.setFont(new java.awt.Font("Arial Unicode MS", 0, 10)); // NOI18N
        jTextFieldConnectUsername.setToolTipText("Username");
        jTextFieldConnectUsername.setNextFocusableComponent(jPasswordFieldConnectPassword);
        jTextFieldConnectUsername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldConnectUsernameKeyTyped(evt);
            }
        });
        jPanel1.add(jTextFieldConnectUsername);
        jTextFieldConnectUsername.setBounds(10, 50, 120, 20);

        jPasswordFieldConnectPassword.setFont(new java.awt.Font("Arial Unicode MS", 0, 10)); // NOI18N
        jPasswordFieldConnectPassword.setToolTipText("Password");
        jPasswordFieldConnectPassword.setNextFocusableComponent(jButtonExecuteFunction3);
        jPasswordFieldConnectPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jPasswordFieldConnectPasswordKeyTyped(evt);
            }
        });
        jPanel1.add(jPasswordFieldConnectPassword);
        jPasswordFieldConnectPassword.setBounds(10, 70, 120, 20);
        jPanel1.add(jSeparator3);
        jSeparator3.setBounds(10, 100, 190, 10);

        jTextFieldPingHostname.setFont(new java.awt.Font("Arial Unicode MS", 0, 10)); // NOI18N
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
        jPanel1.add(jTextFieldPingHostname);
        jTextFieldPingHostname.setBounds(10, 110, 120, 20);

        jCheckBoxDNS.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jCheckBoxDNS.setText("DNS");
        jCheckBoxDNS.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanel1.add(jCheckBoxDNS);
        jCheckBoxDNS.setBounds(140, 110, 60, 20);

        jButtonTracert.setBackground(new java.awt.Color(163, 163, 255));
        jButtonTracert.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        jButtonTracert.setText("TRACERT");
        jButtonTracert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTracertActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonTracert);
        jButtonTracert.setBounds(100, 130, 100, 20);

        jButtonPing.setBackground(new java.awt.Color(163, 163, 255));
        jButtonPing.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        jButtonPing.setText("PING");
        jButtonPing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPingActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonPing);
        jButtonPing.setBounds(10, 130, 80, 20);
        jPanel1.add(jSeparator5);
        jSeparator5.setBounds(10, 160, 190, 10);

        jComboBoxConsoleCOM.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N
        jComboBoxConsoleCOM.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "COM10", "COM11", "COM12", "COM13", "COM14", "COM15", "COM16", "COM17", "COM18", "COM19", "COM20" }));
        jComboBoxConsoleCOM.setToolTipText("Serial COM port");
        jComboBoxConsoleCOM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxConsoleCOMActionPerformed(evt);
            }
        });
        jPanel1.add(jComboBoxConsoleCOM);
        jComboBoxConsoleCOM.setBounds(10, 170, 70, 20);

        jButtonShowCOMList.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N
        jButtonShowCOMList.setText("?");
        jButtonShowCOMList.setToolTipText("List your Serial COM ports");
        jButtonShowCOMList.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonShowCOMList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonShowCOMListActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonShowCOMList);
        jButtonShowCOMList.setBounds(90, 170, 30, 20);

        jComboBoxConsoleBaud.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N
        jComboBoxConsoleBaud.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "9600", "115200" }));
        jComboBoxConsoleBaud.setToolTipText("Baud Rate");
        jPanel1.add(jComboBoxConsoleBaud);
        jComboBoxConsoleBaud.setBounds(130, 170, 70, 20);

        jButtonConsole.setBackground(new java.awt.Color(92, 146, 255));
        jButtonConsole.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        jButtonConsole.setText("Connect to Serial Port");
        jButtonConsole.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonConsole.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConsoleActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonConsole);
        jButtonConsole.setBounds(10, 190, 190, 20);
        jPanel1.add(jSeparator4);
        jSeparator4.setBounds(10, 220, 190, 10);

        jButton1.setContentAreaFilled(false);
        jButton1.setFocusPainted(false);
        jButton1.setFocusable(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(10, 230, 40, 40);

        jButton2.setContentAreaFilled(false);
        jButton2.setFocusPainted(false);
        jButton2.setFocusable(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2);
        jButton2.setBounds(60, 230, 40, 40);

        jButton3.setContentAreaFilled(false);
        jButton3.setFocusPainted(false);
        jButton3.setFocusable(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3);
        jButton3.setBounds(110, 230, 40, 40);

        jButton4.setContentAreaFilled(false);
        jButton4.setFocusPainted(false);
        jButton4.setFocusable(false);
        jButton4.setPreferredSize(new java.awt.Dimension(45, 45));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton4);
        jButton4.setBounds(160, 230, 40, 40);

        jButton8.setContentAreaFilled(false);
        jButton8.setFocusPainted(false);
        jButton8.setFocusable(false);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton8);
        jButton8.setBounds(160, 280, 40, 40);

        jButton12.setContentAreaFilled(false);
        jButton12.setFocusPainted(false);
        jButton12.setFocusable(false);
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton12);
        jButton12.setBounds(160, 330, 40, 40);

        jButton7.setContentAreaFilled(false);
        jButton7.setFocusPainted(false);
        jButton7.setFocusable(false);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton7);
        jButton7.setBounds(110, 280, 40, 40);

        jButton11.setContentAreaFilled(false);
        jButton11.setFocusPainted(false);
        jButton11.setFocusable(false);
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton11);
        jButton11.setBounds(110, 330, 40, 40);

        jButton6.setContentAreaFilled(false);
        jButton6.setFocusPainted(false);
        jButton6.setFocusable(false);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton6);
        jButton6.setBounds(60, 280, 40, 40);

        jButton10.setContentAreaFilled(false);
        jButton10.setFocusPainted(false);
        jButton10.setFocusable(false);
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton10);
        jButton10.setBounds(60, 330, 40, 40);

        jButton5.setContentAreaFilled(false);
        jButton5.setFocusPainted(false);
        jButton5.setFocusable(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton5);
        jButton5.setBounds(10, 280, 40, 40);

        jButton9.setContentAreaFilled(false);
        jButton9.setFocusPainted(false);
        jButton9.setFocusable(false);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton9);
        jButton9.setBounds(10, 330, 40, 40);

        jButton13.setContentAreaFilled(false);
        jButton13.setFocusPainted(false);
        jButton13.setFocusable(false);
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton13);
        jButton13.setBounds(10, 380, 40, 40);

        jButton14.setContentAreaFilled(false);
        jButton14.setFocusPainted(false);
        jButton14.setFocusable(false);
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton14);
        jButton14.setBounds(60, 380, 40, 40);

        jButton15.setContentAreaFilled(false);
        jButton15.setFocusPainted(false);
        jButton15.setFocusable(false);
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton15);
        jButton15.setBounds(110, 380, 40, 40);

        jButton16.setContentAreaFilled(false);
        jButton16.setFocusPainted(false);
        jButton16.setFocusable(false);
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton16);
        jButton16.setBounds(160, 380, 40, 40);

        jButton20.setContentAreaFilled(false);
        jButton20.setFocusPainted(false);
        jButton20.setFocusable(false);
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton20);
        jButton20.setBounds(160, 430, 40, 40);

        jButton19.setContentAreaFilled(false);
        jButton19.setFocusPainted(false);
        jButton19.setFocusable(false);
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton19);
        jButton19.setBounds(110, 430, 40, 40);

        jButton18.setContentAreaFilled(false);
        jButton18.setFocusPainted(false);
        jButton18.setFocusable(false);
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton18);
        jButton18.setBounds(60, 430, 40, 40);

        jButton17.setContentAreaFilled(false);
        jButton17.setFocusPainted(false);
        jButton17.setFocusable(false);
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton17);
        jButton17.setBounds(10, 430, 40, 40);

        jCheckBoxAlternateLogin.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jCheckBoxAlternateLogin.setText("Alternate Login");
        jCheckBoxAlternateLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxAlternateLoginActionPerformed(evt);
            }
        });
        jPanel1.add(jCheckBoxAlternateLogin);
        jCheckBoxAlternateLogin.setBounds(10, 30, 120, 20);

        jButtonExecuteFunction2.setBackground(new java.awt.Color(251, 255, 153));
        jButtonExecuteFunction2.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        jButtonExecuteFunction2.setText("RDP");
        jButtonExecuteFunction2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonExecuteFunction2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExecuteFunction2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonExecuteFunction2);
        jButtonExecuteFunction2.setBounds(140, 40, 60, 23);

        jTextFieldFilter.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
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

        jCheckBox1.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jCheckBox1.setText("Favorites");
        jCheckBox1.setToolTipText("Load an alternate list.");
        jCheckBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox1ItemStateChanged(evt);
            }
        });
        jCheckBox1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBox1StateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanelMainLayout = new javax.swing.GroupLayout(jPanelMain);
        jPanelMain.setLayout(jPanelMainLayout);
        jPanelMainLayout.setHorizontalGroup(
            jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMainLayout.createSequentialGroup()
                .addGroup(jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 361, Short.MAX_VALUE)
                    .addGroup(jPanelMainLayout.createSequentialGroup()
                        .addComponent(jTextFieldFilter)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelMainLayout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(211, 211, 211))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelMainLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanelMainLayout.setVerticalGroup(
            jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMainLayout.createSequentialGroup()
                .addGroup(jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelMainLayout.createSequentialGroup()
                        .addGroup(jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );

        jTabbedMain.addTab("Main", jPanelMain);

        jPanelAppsCustom.setLayout(null);

        jButtonLinkCustom01.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom01ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom01);
        jButtonLinkCustom01.setBounds(20, 50, 170, 30);

        jButtonAppCustom05.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAppCustom05ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonAppCustom05);
        jButtonAppCustom05.setBounds(200, 50, 170, 30);

        jButtonAppCustom06.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAppCustom06ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonAppCustom06);
        jButtonAppCustom06.setBounds(380, 50, 170, 30);

        jButtonAppCustom8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAppCustom8ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonAppCustom8);
        jButtonAppCustom8.setBounds(20, 90, 170, 30);

        jButtonAppCustom9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAppCustom9ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonAppCustom9);
        jButtonAppCustom9.setBounds(200, 90, 170, 30);

        jButtonAppCustom10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAppCustom10ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonAppCustom10);
        jButtonAppCustom10.setBounds(380, 90, 170, 30);

        jButtonAppCustom11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAppCustom11ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonAppCustom11);
        jButtonAppCustom11.setBounds(20, 130, 170, 30);

        jButtonAppCustom12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAppCustom12ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonAppCustom12);
        jButtonAppCustom12.setBounds(200, 130, 170, 30);

        jButtonAppCustom13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAppCustom13ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonAppCustom13);
        jButtonAppCustom13.setBounds(380, 130, 170, 30);

        jButtonAppCustom3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAppCustom3ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonAppCustom3);
        jButtonAppCustom3.setBounds(200, 170, 170, 30);

        jButtonAppCustom2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAppCustom2ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonAppCustom2);
        jButtonAppCustom2.setBounds(20, 170, 170, 30);

        jButtonAppCustom4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAppCustom4ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonAppCustom4);
        jButtonAppCustom4.setBounds(380, 170, 170, 30);

        jButtonAppCustom5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAppCustom5ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonAppCustom5);
        jButtonAppCustom5.setBounds(20, 210, 170, 30);

        jButtonAppCustom6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAppCustom6ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonAppCustom6);
        jButtonAppCustom6.setBounds(200, 210, 170, 30);

        jButtonAppCustom7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAppCustom7ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonAppCustom7);
        jButtonAppCustom7.setBounds(380, 210, 170, 30);

        jButtonAppCustom14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAppCustom14ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonAppCustom14);
        jButtonAppCustom14.setBounds(20, 250, 170, 30);

        jButtonAppCustom15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAppCustom15ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonAppCustom15);
        jButtonAppCustom15.setBounds(200, 250, 170, 30);

        jButtonAppCustom16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAppCustom16ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonAppCustom16);
        jButtonAppCustom16.setBounds(380, 250, 170, 30);

        jButtonAppCustom17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAppCustom17ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonAppCustom17);
        jButtonAppCustom17.setBounds(20, 290, 170, 30);

        jButtonAppCustom18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAppCustom18ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonAppCustom18);
        jButtonAppCustom18.setBounds(200, 290, 170, 30);

        jButtonAppCustom19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAppCustom19ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonAppCustom19);
        jButtonAppCustom19.setBounds(380, 290, 170, 30);

        jButtonAppCustom20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAppCustom20ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonAppCustom20);
        jButtonAppCustom20.setBounds(20, 330, 170, 30);

        jButtonAppCustom21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAppCustom21ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonAppCustom21);
        jButtonAppCustom21.setBounds(200, 330, 170, 30);

        jButtonAppCustom22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAppCustom22ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonAppCustom22);
        jButtonAppCustom22.setBounds(380, 330, 170, 30);

        jButtonAppCustom23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAppCustom23ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonAppCustom23);
        jButtonAppCustom23.setBounds(20, 370, 170, 30);

        jButtonAppCustom24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAppCustom24ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonAppCustom24);
        jButtonAppCustom24.setBounds(200, 370, 170, 30);

        jButtonAppCustom25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAppCustom25ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonAppCustom25);
        jButtonAppCustom25.setBounds(380, 370, 170, 30);

        jButtonAppCustom27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAppCustom27ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonAppCustom27);
        jButtonAppCustom27.setBounds(20, 410, 170, 30);

        jButtonAppCustom28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAppCustom28ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonAppCustom28);
        jButtonAppCustom28.setBounds(200, 410, 170, 30);

        jButtonAppCustom29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAppCustom29ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonAppCustom29);
        jButtonAppCustom29.setBounds(380, 410, 170, 30);

        jLabel29.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel29.setText("Links");
        jPanelAppsCustom.add(jLabel29);
        jLabel29.setBounds(110, 10, 350, 30);

        jTabbedPanelLinks.addTab("User-Defined", jPanelAppsCustom);

        jPanelAppsEmbedded.setLayout(null);

        jLabel28.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("Embedded Web Apps");
        jPanelAppsEmbedded.add(jLabel28);
        jLabel28.setBounds(110, 10, 350, 20);

        jButtonJSDiff.setText("jsDiff");
        jButtonJSDiff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonJSDiffActionPerformed(evt);
            }
        });
        jPanelAppsEmbedded.add(jButtonJSDiff);
        jButtonJSDiff.setBounds(380, 40, 170, 30);

        jButtonConfigBuilder.setText("Config Builder (Lab Use)");
        jButtonConfigBuilder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConfigBuilderActionPerformed(evt);
            }
        });
        jPanelAppsEmbedded.add(jButtonConfigBuilder);
        jButtonConfigBuilder.setBounds(200, 40, 170, 30);

        jButtonJSDiff1.setText("Subnet Calculator");
        jButtonJSDiff1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonJSDiff1ActionPerformed(evt);
            }
        });
        jPanelAppsEmbedded.add(jButtonJSDiff1);
        jButtonJSDiff1.setBounds(20, 40, 170, 30);

        jButton28.setText("IPv4 Subnet Generator");
        jButton28.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton28ActionPerformed(evt);
            }
        });
        jPanelAppsEmbedded.add(jButton28);
        jButton28.setBounds(20, 130, 170, 30);

        jButton30.setText("IPv6 Subnet Generator");
        jButton30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton30ActionPerformed(evt);
            }
        });
        jPanelAppsEmbedded.add(jButton30);
        jButton30.setBounds(380, 130, 170, 30);
        jPanelAppsEmbedded.add(jSeparator10);
        jSeparator10.setBounds(10, 90, 550, 10);

        jButton35.setText("Puppeteer");
        jButton35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton35ActionPerformed(evt);
            }
        });
        jPanelAppsEmbedded.add(jButton35);
        jButton35.setBounds(200, 130, 170, 30);

        jLabel36.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel36.setText("Embedded Java Apps");
        jPanelAppsEmbedded.add(jLabel36);
        jLabel36.setBounds(110, 100, 350, 20);

        jTabbedPanelLinks.addTab("Embedded", jPanelAppsEmbedded);

        jTabbedMain.addTab("Links", jTabbedPanelLinks);

        jPanel3.setLayout(null);

        jButtonCustomScript03.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanel3.add(jButtonCustomScript03);
        jButtonCustomScript03.setBounds(380, 50, 170, 30);

        jButtonCustomScript01.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonCustomScript01.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCustomScript01ActionPerformed(evt);
            }
        });
        jPanel3.add(jButtonCustomScript01);
        jButtonCustomScript01.setBounds(20, 50, 170, 30);

        jButtonCustomScript02.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonCustomScript02.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCustomScript02ActionPerformed(evt);
            }
        });
        jPanel3.add(jButtonCustomScript02);
        jButtonCustomScript02.setBounds(200, 50, 170, 30);

        jButtonCustomScript06.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanel3.add(jButtonCustomScript06);
        jButtonCustomScript06.setBounds(380, 90, 170, 30);

        jButtonCustomScript04.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonCustomScript04.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCustomScript04ActionPerformed(evt);
            }
        });
        jPanel3.add(jButtonCustomScript04);
        jButtonCustomScript04.setBounds(20, 90, 170, 30);

        jButtonCustomScript05.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonCustomScript05.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCustomScript05ActionPerformed(evt);
            }
        });
        jPanel3.add(jButtonCustomScript05);
        jButtonCustomScript05.setBounds(200, 90, 170, 30);

        jButtonCustomScript8.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonCustomScript8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCustomScript8ActionPerformed(evt);
            }
        });
        jPanel3.add(jButtonCustomScript8);
        jButtonCustomScript8.setBounds(200, 130, 170, 30);

        jButtonCustomScript9.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanel3.add(jButtonCustomScript9);
        jButtonCustomScript9.setBounds(380, 130, 170, 30);

        jButtonCustomScript07.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonCustomScript07.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCustomScript07ActionPerformed(evt);
            }
        });
        jPanel3.add(jButtonCustomScript07);
        jButtonCustomScript07.setBounds(20, 130, 170, 30);

        jButtonCustomScript4.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonCustomScript4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCustomScript4ActionPerformed(evt);
            }
        });
        jPanel3.add(jButtonCustomScript4);
        jButtonCustomScript4.setBounds(380, 170, 170, 30);

        jButtonCustomScript11.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanel3.add(jButtonCustomScript11);
        jButtonCustomScript11.setBounds(380, 210, 170, 30);

        jButtonCustomScript3.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonCustomScript3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCustomScript3ActionPerformed(evt);
            }
        });
        jPanel3.add(jButtonCustomScript3);
        jButtonCustomScript3.setBounds(200, 170, 170, 30);

        jButtonCustomScript2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonCustomScript2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCustomScript2ActionPerformed(evt);
            }
        });
        jPanel3.add(jButtonCustomScript2);
        jButtonCustomScript2.setBounds(20, 170, 170, 30);

        jButtonCustomScript12.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonCustomScript12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCustomScript12ActionPerformed(evt);
            }
        });
        jPanel3.add(jButtonCustomScript12);
        jButtonCustomScript12.setBounds(20, 210, 170, 30);

        jButtonCustomScript13.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonCustomScript13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCustomScript13ActionPerformed(evt);
            }
        });
        jPanel3.add(jButtonCustomScript13);
        jButtonCustomScript13.setBounds(200, 210, 170, 30);

        jButtonCustomScript14.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanel3.add(jButtonCustomScript14);
        jButtonCustomScript14.setBounds(380, 250, 170, 30);

        jButtonCustomScript15.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonCustomScript15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCustomScript15ActionPerformed(evt);
            }
        });
        jPanel3.add(jButtonCustomScript15);
        jButtonCustomScript15.setBounds(20, 250, 170, 30);

        jButtonCustomScript16.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonCustomScript16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCustomScript16ActionPerformed(evt);
            }
        });
        jPanel3.add(jButtonCustomScript16);
        jButtonCustomScript16.setBounds(200, 250, 170, 30);

        jButtonCustomScript17.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonCustomScript17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCustomScript17ActionPerformed(evt);
            }
        });
        jPanel3.add(jButtonCustomScript17);
        jButtonCustomScript17.setBounds(200, 290, 170, 30);

        jButtonCustomScript18.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanel3.add(jButtonCustomScript18);
        jButtonCustomScript18.setBounds(380, 290, 170, 30);

        jButtonCustomScript19.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonCustomScript19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCustomScript19ActionPerformed(evt);
            }
        });
        jPanel3.add(jButtonCustomScript19);
        jButtonCustomScript19.setBounds(20, 290, 170, 30);

        jButtonCustomScript20.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanel3.add(jButtonCustomScript20);
        jButtonCustomScript20.setBounds(380, 330, 170, 30);

        jButtonCustomScript21.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonCustomScript21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCustomScript21ActionPerformed(evt);
            }
        });
        jPanel3.add(jButtonCustomScript21);
        jButtonCustomScript21.setBounds(20, 330, 170, 30);

        jButtonCustomScript22.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonCustomScript22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCustomScript22ActionPerformed(evt);
            }
        });
        jPanel3.add(jButtonCustomScript22);
        jButtonCustomScript22.setBounds(200, 330, 170, 30);

        jButtonCustomScript23.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonCustomScript23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCustomScript23ActionPerformed(evt);
            }
        });
        jPanel3.add(jButtonCustomScript23);
        jButtonCustomScript23.setBounds(200, 370, 170, 30);

        jButtonCustomScript24.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanel3.add(jButtonCustomScript24);
        jButtonCustomScript24.setBounds(380, 370, 170, 30);

        jButtonCustomScript25.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonCustomScript25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCustomScript25ActionPerformed(evt);
            }
        });
        jPanel3.add(jButtonCustomScript25);
        jButtonCustomScript25.setBounds(20, 370, 170, 30);

        jButtonCustomScript26.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonCustomScript26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCustomScript26ActionPerformed(evt);
            }
        });
        jPanel3.add(jButtonCustomScript26);
        jButtonCustomScript26.setBounds(200, 410, 170, 30);

        jButtonCustomScript27.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanel3.add(jButtonCustomScript27);
        jButtonCustomScript27.setBounds(380, 410, 170, 30);

        jButtonCustomScript28.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonCustomScript28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCustomScript28ActionPerformed(evt);
            }
        });
        jPanel3.add(jButtonCustomScript28);
        jButtonCustomScript28.setBounds(20, 410, 170, 30);

        jLabel30.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setText("Scripts");
        jPanel3.add(jLabel30);
        jLabel30.setBounds(110, 10, 350, 30);

        jTabbedPaneScripts.addTab("User-Defined", jPanel3);

        jPanel4.setLayout(null);

        jButtonScriptSyncStandalones.setText("Sync Standalones");
        jButtonScriptSyncStandalones.setEnabled(false);
        jButtonScriptSyncStandalones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptSyncStandalonesActionPerformed(evt);
            }
        });
        jPanel4.add(jButtonScriptSyncStandalones);
        jButtonScriptSyncStandalones.setBounds(200, 10, 170, 30);

        jButtonScriptBackupShares.setText("Backup Shares");
        jButtonScriptBackupShares.setEnabled(false);
        jButtonScriptBackupShares.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptBackupSharesActionPerformed(evt);
            }
        });
        jPanel4.add(jButtonScriptBackupShares);
        jButtonScriptBackupShares.setBounds(20, 10, 170, 30);

        jButtonScriptSyncStandalones1.setText("Map Standalone Share");
        jButtonScriptSyncStandalones1.setEnabled(false);
        jButtonScriptSyncStandalones1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptSyncStandalones1ActionPerformed(evt);
            }
        });
        jPanel4.add(jButtonScriptSyncStandalones1);
        jButtonScriptSyncStandalones1.setBounds(380, 10, 170, 30);

        jTabbedPaneScripts.addTab("Embedded", jPanel4);

        jTabbedMain.addTab("Scripts", jTabbedPaneScripts);

        jPanel6.setLayout(null);

        jToggleOfflineMode.setBackground(new java.awt.Color(0, 204, 51));
        jToggleOfflineMode.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jToggleOfflineMode.setText("Online");
        jToggleOfflineMode.setToolTipText("Alternate between using network and local files.");
        jToggleOfflineMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleOfflineModeActionPerformed(evt);
            }
        });
        jPanel6.add(jToggleOfflineMode);
        jToggleOfflineMode.setBounds(200, 10, 170, 30);

        jButtonReferenceCustom06.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom06ActionPerformed(evt);
            }
        });
        jPanel6.add(jButtonReferenceCustom06);
        jButtonReferenceCustom06.setBounds(380, 90, 170, 30);

        jButtonReferenceCustom01.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom01ActionPerformed(evt);
            }
        });
        jPanel6.add(jButtonReferenceCustom01);
        jButtonReferenceCustom01.setBounds(20, 50, 170, 30);

        jButtonReferenceCustom02.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom02ActionPerformed(evt);
            }
        });
        jPanel6.add(jButtonReferenceCustom02);
        jButtonReferenceCustom02.setBounds(200, 50, 170, 30);

        jButtonReferenceCustom03.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom03ActionPerformed(evt);
            }
        });
        jPanel6.add(jButtonReferenceCustom03);
        jButtonReferenceCustom03.setBounds(380, 50, 170, 30);

        jButtonReferenceCustom04.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom04ActionPerformed(evt);
            }
        });
        jPanel6.add(jButtonReferenceCustom04);
        jButtonReferenceCustom04.setBounds(20, 90, 170, 30);

        jButtonReferenceCustom05.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom05ActionPerformed(evt);
            }
        });
        jPanel6.add(jButtonReferenceCustom05);
        jButtonReferenceCustom05.setBounds(200, 90, 170, 30);

        jButtonReferenceCustom7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom7ActionPerformed(evt);
            }
        });
        jPanel6.add(jButtonReferenceCustom7);
        jButtonReferenceCustom7.setBounds(380, 170, 170, 30);

        jButtonReferenceCustom2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom2ActionPerformed(evt);
            }
        });
        jPanel6.add(jButtonReferenceCustom2);
        jButtonReferenceCustom2.setBounds(20, 130, 170, 30);

        jButtonReferenceCustom3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom3ActionPerformed(evt);
            }
        });
        jPanel6.add(jButtonReferenceCustom3);
        jButtonReferenceCustom3.setBounds(200, 130, 170, 30);

        jButtonReferenceCustom4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom4ActionPerformed(evt);
            }
        });
        jPanel6.add(jButtonReferenceCustom4);
        jButtonReferenceCustom4.setBounds(380, 130, 170, 30);

        jButtonReferenceCustom5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom5ActionPerformed(evt);
            }
        });
        jPanel6.add(jButtonReferenceCustom5);
        jButtonReferenceCustom5.setBounds(20, 170, 170, 30);

        jButtonReferenceCustom6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom6ActionPerformed(evt);
            }
        });
        jPanel6.add(jButtonReferenceCustom6);
        jButtonReferenceCustom6.setBounds(200, 170, 170, 30);

        jTabbedPaneReference.addTab("User-Defined", jPanel6);

        jPanel10.setLayout(null);

        jButton24.setText("IPv4 Subnet Chart");
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });
        jPanel10.add(jButton24);
        jButton24.setBounds(20, 20, 170, 30);

        jButton36.setText("IPv4 Subnet Cheat Sheet");
        jButton36.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton36ActionPerformed(evt);
            }
        });
        jPanel10.add(jButton36);
        jButton36.setBounds(200, 20, 170, 30);

        jButton37.setText("Stretch's Cheat Sheets");
        jButton37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton37ActionPerformed(evt);
            }
        });
        jPanel10.add(jButton37);
        jButton37.setBounds(380, 20, 170, 30);

        jTabbedPaneReference.addTab("Embedded", jPanel10);

        jTabbedMain.addTab("Reference", jTabbedPaneReference);

        jPanel7.setLayout(null);

        jButtonFolderToZip.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButtonFolderToZip.setText("Add Folder to Zip!");
        jButtonFolderToZip.setToolTipText("");
        jButtonFolderToZip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFolderToZipActionPerformed(evt);
            }
        });
        jPanel7.add(jButtonFolderToZip);
        jButtonFolderToZip.setBounds(310, 140, 170, 20);
        jPanel7.add(jTextFieldZipSourceFolder);
        jTextFieldZipSourceFolder.setBounds(130, 50, 340, 20);
        jPanel7.add(jTextFieldZipFilename);
        jTextFieldZipFilename.setBounds(130, 80, 350, 20);

        jButton25.setText("Browse");
        jButton25.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton25);
        jButton25.setBounds(480, 50, 70, 20);

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("Confirm Password: ");
        jPanel7.add(jLabel16);
        jLabel16.setBounds(0, 140, 130, 20);

        jPasswordFieldZip.setToolTipText("");
        jPasswordFieldZip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordFieldZipActionPerformed(evt);
            }
        });
        jPanel7.add(jPasswordFieldZip);
        jPasswordFieldZip.setBounds(130, 110, 170, 20);

        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("Destination Zip: ");
        jPanel7.add(jLabel18);
        jLabel18.setBounds(10, 80, 120, 20);
        jPanel7.add(jProgressBarZip);
        jProgressBarZip.setBounds(20, 170, 520, 20);

        jLabelFolderToZip4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabelFolderToZip4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelFolderToZip4.setText("Extract Archive to Folder");
        jPanel7.add(jLabelFolderToZip4);
        jLabelFolderToZip4.setBounds(20, 230, 530, 20);

        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel20.setText("Source Folder: ");
        jPanel7.add(jLabel20);
        jLabel20.setBounds(40, 50, 90, 20);
        jPanel7.add(jSeparator9);
        jSeparator9.setBounds(10, 210, 540, 10);

        jComboBoxZipEncMethod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Standard Encryption", "AES-256 Encryption" }));
        jComboBoxZipEncMethod.setToolTipText("Encryption Method");
        jPanel7.add(jComboBoxZipEncMethod);
        jComboBoxZipEncMethod.setBounds(310, 110, 170, 20);

        jLabelFolderToZip7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabelFolderToZip7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelFolderToZip7.setText("Add Folder to Encrypted Archive");
        jPanel7.add(jLabelFolderToZip7);
        jLabelFolderToZip7.setBounds(20, 20, 530, 20);

        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel33.setText("Source Zip: ");
        jPanel7.add(jLabel33);
        jLabel33.setBounds(10, 260, 120, 20);

        jTextFieldZipSourceFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldZipSourceFileActionPerformed(evt);
            }
        });
        jPanel7.add(jTextFieldZipSourceFile);
        jTextFieldZipSourceFile.setBounds(130, 260, 340, 20);

        jButtonZipBrowseSourceZip.setText("Browse");
        jButtonZipBrowseSourceZip.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonZipBrowseSourceZip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonZipBrowseSourceZipActionPerformed(evt);
            }
        });
        jPanel7.add(jButtonZipBrowseSourceZip);
        jButtonZipBrowseSourceZip.setBounds(480, 260, 70, 20);

        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel34.setText("Destination Folder: ");
        jPanel7.add(jLabel34);
        jLabel34.setBounds(0, 290, 130, 20);
        jPanel7.add(jTextFieldZipDestinationFolder);
        jTextFieldZipDestinationFolder.setBounds(130, 290, 340, 20);

        jButtonZipBrowseDestinationFolder.setText("Browse");
        jButtonZipBrowseDestinationFolder.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonZipBrowseDestinationFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonZipBrowseDestinationFolderActionPerformed(evt);
            }
        });
        jPanel7.add(jButtonZipBrowseDestinationFolder);
        jButtonZipBrowseDestinationFolder.setBounds(480, 290, 70, 20);

        jButton39.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton39.setText("Extract to Folder!");
        jButton39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton39ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton39);
        jButton39.setBounds(330, 320, 200, 20);
        jPanel7.add(jProgressBarZipExtract);
        jProgressBarZipExtract.setBounds(20, 350, 530, 20);

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Password: ");
        jPanel7.add(jLabel17);
        jLabel17.setBounds(40, 320, 90, 20);

        jPasswordFieldZipConfirm.setToolTipText("");
        jPasswordFieldZipConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordFieldZipConfirmActionPerformed(evt);
            }
        });
        jPanel7.add(jPasswordFieldZipConfirm);
        jPasswordFieldZipConfirm.setBounds(130, 140, 170, 20);

        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel35.setText("Password: ");
        jPanel7.add(jLabel35);
        jLabel35.setBounds(40, 110, 90, 20);

        jPasswordFieldZipExtract.setToolTipText("");
        jPasswordFieldZipExtract.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordFieldZipExtractActionPerformed(evt);
            }
        });
        jPanel7.add(jPasswordFieldZipExtract);
        jPasswordFieldZipExtract.setBounds(130, 320, 170, 20);

        jTabbedPaneToolBox.addTab("Zip & Encrypt", jPanel7);

        jPanel8.setLayout(null);
        jPanel8.add(jSeparator8);
        jSeparator8.setBounds(10, 150, 550, 10);

        jTextFieldType7Input.setText("05240E0715444F1D0A321F131F211D1A2A373B243A3017301710");
        jPanel8.add(jTextFieldType7Input);
        jTextFieldType7Input.setBounds(20, 50, 530, 20);

        jButton21.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton21.setText("Decrypt");
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });
        jPanel8.add(jButton21);
        jButton21.setBounds(190, 80, 79, 20);

        jButton22.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton22.setText("Encrypt");
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });
        jPanel8.add(jButton22);
        jButton22.setBounds(320, 80, 80, 20);

        jTextFieldType7Output.setEditable(false);
        jPanel8.add(jTextFieldType7Output);
        jTextFieldType7Output.setBounds(20, 110, 450, 20);

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Type 7 Decrypt/Encrypt");
        jPanel8.add(jLabel14);
        jLabel14.setBounds(120, 20, 350, 20);

        jButton23.setText("Copy");
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });
        jPanel8.add(jButton23);
        jButton23.setBounds(480, 110, 70, 20);

        jTabbedPaneToolBox.addTab("Type 7", jPanel8);

        jPanel12.setLayout(null);

        jLabel19.setText("SHA512:");
        jPanel12.add(jLabel19);
        jLabel19.setBounds(10, 170, 90, 20);

        jButtonGenerateHash.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButtonGenerateHash.setText("Generate!");
        jButtonGenerateHash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGenerateHashActionPerformed(evt);
            }
        });
        jPanel12.add(jButtonGenerateHash);
        jButtonGenerateHash.setBounds(240, 80, 90, 23);
        jPanel12.add(jTextFieldFileHashGenerate);
        jTextFieldFileHashGenerate.setBounds(50, 50, 400, 20);

        jButton27.setText("Browse");
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });
        jPanel12.add(jButton27);
        jButton27.setBounds(460, 50, 90, 20);

        jTextFieldHashSHA512.setEditable(false);
        jPanel12.add(jTextFieldHashSHA512);
        jTextFieldHashSHA512.setBounds(70, 170, 480, 20);

        jTextFieldHashMD5.setEditable(false);
        jPanel12.add(jTextFieldHashMD5);
        jTextFieldHashMD5.setBounds(70, 110, 480, 20);

        jTextFieldHashSHA1.setEditable(false);
        jPanel12.add(jTextFieldHashSHA1);
        jTextFieldHashSHA1.setBounds(70, 130, 480, 20);

        jTextFieldHashSHA256.setEditable(false);
        jPanel12.add(jTextFieldHashSHA256);
        jTextFieldHashSHA256.setBounds(70, 150, 480, 20);

        jLabel22.setText("MD5:");
        jPanel12.add(jLabel22);
        jLabel22.setBounds(10, 110, 90, 20);

        jLabel23.setText("SHA1:");
        jPanel12.add(jLabel23);
        jLabel23.setBounds(10, 130, 90, 20);

        jLabel24.setText("SHA256:");
        jPanel12.add(jLabel24);
        jLabel24.setBounds(10, 150, 90, 20);

        jLabelFolderToZip5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabelFolderToZip5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelFolderToZip5.setText("Hash Generator");
        jPanel12.add(jLabelFolderToZip5);
        jLabelFolderToZip5.setBounds(110, 20, 350, 20);

        jLabel25.setText("File:");
        jPanel12.add(jLabel25);
        jLabel25.setBounds(10, 50, 90, 20);

        jTabbedPaneToolBox.addTab("Hash Generate", jPanel12);

        jPanel9.setLayout(null);

        jLabelFolderToZip3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabelFolderToZip3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelFolderToZip3.setText("Get NTP Time (NtpMessage.java Method)");
        jPanel9.add(jLabelFolderToZip3);
        jLabelFolderToZip3.setBounds(110, 130, 350, 20);

        jLabel21.setText("Atomic Time:");
        jPanel9.add(jLabel21);
        jLabel21.setBounds(20, 90, 90, 20);

        jButton26.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton26.setText("Get Time!");
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton26ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton26);
        jButton26.setBounds(439, 37, 100, 23);

        jTextFieldNtpServer.setText("pool.ntp.org");
        jPanel9.add(jTextFieldNtpServer);
        jTextFieldNtpServer.setBounds(140, 40, 290, 20);

        jTextFieldNtpAtomicTime.setEditable(false);
        jPanel9.add(jTextFieldNtpAtomicTime);
        jTextFieldNtpAtomicTime.setBounds(110, 90, 430, 20);

        jTextFieldNtpSystemTime.setEditable(false);
        jPanel9.add(jTextFieldNtpSystemTime);
        jTextFieldNtpSystemTime.setBounds(110, 70, 430, 20);

        jLabel26.setText("Server:");
        jPanel9.add(jLabel26);
        jLabel26.setBounds(80, 40, 90, 20);

        jLabel27.setText("System Time:");
        jPanel9.add(jLabel27);
        jLabel27.setBounds(20, 70, 90, 20);

        jButton29.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton29.setText("Get Time!");
        jButton29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton29ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton29);
        jButton29.setBounds(230, 160, 100, 20);

        jLabelFolderToZip6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabelFolderToZip6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelFolderToZip6.setText("Get NTP Time (NTPUDPClient Method)");
        jPanel9.add(jLabelFolderToZip6);
        jLabelFolderToZip6.setBounds(110, 10, 350, 20);
        jPanel9.add(jSeparator7);
        jSeparator7.setBounds(10, 120, 550, 10);

        jTextAreaNTPMessage.setEditable(false);
        jTextAreaNTPMessage.setBackground(new java.awt.Color(240, 240, 240));
        jTextAreaNTPMessage.setColumns(20);
        jTextAreaNTPMessage.setRows(5);
        jScrollPane4.setViewportView(jTextAreaNTPMessage);

        jPanel9.add(jScrollPane4);
        jScrollPane4.setBounds(20, 190, 530, 220);

        jTabbedPaneToolBox.addTab("NTP", jPanel9);

        jTabbedMain.addTab("ToolBox", jTabbedPaneToolBox);

        jPanelSettings.setLayout(null);

        jLabelSSHClient.setText("SSH Client:");
        jPanelSettings.add(jLabelSSHClient);
        jLabelSSHClient.setBounds(10, 10, 90, 30);

        buttonGroupSSHClient.add(jRadioButtonSSHClientSecureCRT);
        jRadioButtonSSHClientSecureCRT.setSelected(true);
        jRadioButtonSSHClientSecureCRT.setText("SecureCRT");
        jPanelSettings.add(jRadioButtonSSHClientSecureCRT);
        jRadioButtonSSHClientSecureCRT.setBounds(170, 10, 90, 30);

        buttonGroupSSHClient.add(jRadioButtonSSHClientPuTTY);
        jRadioButtonSSHClientPuTTY.setText("PuTTY");
        jRadioButtonSSHClientPuTTY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonSSHClientPuTTYActionPerformed(evt);
            }
        });
        jPanelSettings.add(jRadioButtonSSHClientPuTTY);
        jRadioButtonSSHClientPuTTY.setBounds(100, 10, 70, 30);

        jLabelListTextSizePreview.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        jLabelListTextSizePreview.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelListTextSizePreview.setText("c9300-a01-abcde-1234");
        jPanelSettings.add(jLabelListTextSizePreview);
        jLabelListTextSizePreview.setBounds(240, 70, 200, 30);

        buttonGroupConsoleClient.add(jRadioButtonConsolePutty);
        jRadioButtonConsolePutty.setText("PuTTY");
        jRadioButtonConsolePutty.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jRadioButtonConsolePuttyMouseClicked(evt);
            }
        });
        jRadioButtonConsolePutty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonConsolePuttyActionPerformed(evt);
            }
        });
        jPanelSettings.add(jRadioButtonConsolePutty);
        jRadioButtonConsolePutty.setBounds(100, 40, 70, 30);

        buttonGroupConsoleClient.add(jRadioButtonConsoleSecureCRT);
        jRadioButtonConsoleSecureCRT.setSelected(true);
        jRadioButtonConsoleSecureCRT.setText("SecureCRT");
        jPanelSettings.add(jRadioButtonConsoleSecureCRT);
        jRadioButtonConsoleSecureCRT.setBounds(170, 40, 100, 30);

        jSliderListTextSize.setMaximum(6);
        jSliderListTextSize.setPaintLabels(true);
        jSliderListTextSize.setSnapToTicks(true);
        jSliderListTextSize.setValue(3);
        jSliderListTextSize.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSliderListTextSizeStateChanged(evt);
            }
        });
        jPanelSettings.add(jSliderListTextSize);
        jSliderListTextSize.setBounds(100, 70, 140, 30);

        jLabelConsoleClient.setText("Console Client:");
        jPanelSettings.add(jLabelConsoleClient);
        jLabelConsoleClient.setBounds(10, 40, 90, 30);

        jLabelListTextSize1.setText("List Text Size:");
        jPanelSettings.add(jLabelListTextSize1);
        jLabelListTextSize1.setBounds(10, 70, 90, 30);

        jButton31.setText("Reset SecureCRT Settings");
        jButton31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton31ActionPerformed(evt);
            }
        });
        jPanelSettings.add(jButton31);
        jButton31.setBounds(10, 110, 190, 20);

        jButton32.setText("Edit Favorites List");
        jButton32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton32ActionPerformed(evt);
            }
        });
        jPanelSettings.add(jButton32);
        jButton32.setBounds(10, 140, 190, 20);

        jButton33.setText("Show Button List");
        jButton33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton33ActionPerformed(evt);
            }
        });
        jPanelSettings.add(jButton33);
        jButton33.setBounds(10, 170, 190, 20);

        jButton34.setText("View Settings File");
        jButton34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton34ActionPerformed(evt);
            }
        });
        jPanelSettings.add(jButton34);
        jButton34.setBounds(10, 200, 190, 20);

        jTextField2.setBackground(new java.awt.Color(51, 51, 51));
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
        jPanelSettings.add(jTextField2);
        jTextField2.setBounds(370, 460, 190, 20);

        jButtonScriptUpdateLaunchPad.setText("Update LaunchPad");
        jButtonScriptUpdateLaunchPad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptUpdateLaunchPadActionPerformed(evt);
            }
        });
        jPanelSettings.add(jButtonScriptUpdateLaunchPad);
        jButtonScriptUpdateLaunchPad.setBounds(390, 20, 160, 30);

        jButtonReportIssue.setText("Report an Issue");
        jButtonReportIssue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReportIssueActionPerformed(evt);
            }
        });
        jPanelSettings.add(jButtonReportIssue);
        jButtonReportIssue.setBounds(10, 460, 160, 23);

        jTabbedMain.addTab("Settings", jPanelSettings);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedMain, javax.swing.GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE)
        );

        jTabbedMain.getAccessibleContext().setAccessibleName("");

        getAccessibleContext().setAccessibleName("NSB LaunchPad Pre-Alpha");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
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
                mailto = new URI("mailto:mathew.bray@gmail.com?subject=LaunchPad%20Issue");
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
            throw new RuntimeException("desktop doesn't support mailto; mail is dead anyway ;)");
        }
    }//GEN-LAST:event_jButtonReportIssueActionPerformed

    private void jButtonScriptUpdateLaunchPadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptUpdateLaunchPadActionPerformed
        // TODO add your handling code here:
        String myValue = "cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -File \"" + PropertyHandler.getInstance().getValue("FileUpdateScript") + "\"";
        System.out.println(myValue);
        try {
            Runtime.getRuntime().exec(myValue);
        }
        catch (IOException e) {
            System.out.println("HEY Buddy ! U r Doing Something Wrong ");
            JOptionPane.showMessageDialog(null, "Something is wrong!");
        }
        System.exit(0);
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

            if ("settingsfile".equals(jTextField2.getText())) {

                String strPathPropertiesFile = pathDesktop + "\\LaunchPad\\launchpad.properties";

                //text file, should be opening in default text editor
                File file = new File(strPathPropertiesFile);

                //first check if Desktop is supported by Platform or not
                if(!Desktop.isDesktopSupported()){
                    System.out.println("Desktop is not supported");
                    return;
                }

                Desktop desktop = Desktop.getDesktop();
                if(file.exists()) try {
                    desktop.open(file);
                } catch (IOException ex) {
                    Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("launchpad.LaunchPadForm.jButtonReferenceCustom01ActionPerformed()");
                }

                // Open
                if(file.exists()) try {
                    desktop.open(file);
                } catch (IOException ex) {
                    Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("launchpad.LaunchPadForm.jButtonReferenceCustom01ActionPerformed()");

                }

                jTextField2.setText("");
            }

        }
    }//GEN-LAST:event_jTextField2KeyTyped

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jButton34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton34ActionPerformed
        // TODO add your handling code here:
        String strPathPropertiesFile = pathDesktop + "\\LaunchPad\\launchpad.properties";
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
            frame.setTitle("Settings File");
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
                new ButtonList();
            }
        });
    }//GEN-LAST:event_jButton33ActionPerformed

    private void jButton32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton32ActionPerformed
        // TODO add your handling code here:
        try {
            Process p = Runtime.getRuntime().exec
            (new String [] { "cmd.exe", "/c", "assoc", ".xls"});
            BufferedReader input =
            new BufferedReader
            (new InputStreamReader(p.getInputStream()));
            String extensionType = input.readLine();
            input.close();
            // extract type
            if (extensionType == null) {
                System.out.println("no office installed ?");
                String myValue = "cmd.exe /c start wordpad.exe \"" + strSessionListFavorites + "\"";
                System.out.println(myValue);
                try {
                    Runtime.getRuntime().exec(myValue);
                }
                catch (IOException e) {
                    System.out.println("HEY Buddy ! U r Doing Something Wrong ");
                    JOptionPane.showMessageDialog(null, "Something is wrong!");
                }

                return;
            }
            String fileType[] = extensionType.split("=");

            p = Runtime.getRuntime().exec
            (new String [] { "cmd.exe", "/c", "ftype", fileType[1]});
            input =
            new BufferedReader
            (new InputStreamReader(p.getInputStream()));
            String fileAssociation = input.readLine();
            // extract path
            String officePath = fileAssociation.split("=")[1];
            Pattern patternInQuotes = Pattern.compile("\"([^\"]*)\"");
            Matcher matchInQuotes = patternInQuotes.matcher(officePath);
            while (matchInQuotes.find()) {
                officePath = matchInQuotes.group(1);
                System.out.println(matchInQuotes.group(1));
            }
            String myValue = "\"" + officePath + " \" \"" + strSessionListFavorites + "\"";
            System.out.println(myValue);
            try {
                Runtime.getRuntime().exec(myValue);
            }
            catch (IOException e) {
                System.out.println("HEY Buddy ! U r Doing Something Wrong ");
                JOptionPane.showMessageDialog(null, "Something is wrong!");
            }
        }
        catch (IOException err) {
        }
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
                System.out.println("HEY Buddy ! U r Doing Something Wrong ");
                JOptionPane.showMessageDialog(null, "Something is wrong!");
            }
        }
    }//GEN-LAST:event_jButton31ActionPerformed

    private void jSliderListTextSizeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSliderListTextSizeStateChanged
        // TODO add your handling code here:
        String strSliderValue = String.valueOf(jSliderListTextSize.getValue());
        System.out.println(strSliderValue);

        if (strSliderValue.equals("0")){
            jListSessions.setFont(jListSessions.getFont().deriveFont(10.0f));
            jLabelListTextSizePreview.setFont(jLabelListTextSizePreview.getFont().deriveFont(10.0f));
        }
        if (strSliderValue.equals("1")){
            jListSessions.setFont(jListSessions.getFont().deriveFont(11.0f));
            jLabelListTextSizePreview.setFont(jLabelListTextSizePreview.getFont().deriveFont(11.0f));
        }
        if (strSliderValue.equals("2")){
            jListSessions.setFont(jListSessions.getFont().deriveFont(12.0f));
            jLabelListTextSizePreview.setFont(jLabelListTextSizePreview.getFont().deriveFont(12.0f));
        }
        if (strSliderValue.equals("3")){
            jListSessions.setFont(jListSessions.getFont().deriveFont(13.0f));
            jLabelListTextSizePreview.setFont(jLabelListTextSizePreview.getFont().deriveFont(13.0f));
        }
        if (strSliderValue.equals("4")){
            jListSessions.setFont(jListSessions.getFont().deriveFont(14.0f));
            jLabelListTextSizePreview.setFont(jLabelListTextSizePreview.getFont().deriveFont(14.0f));
        }
        if (strSliderValue.equals("5")){
            jListSessions.setFont(jListSessions.getFont().deriveFont(15.0f));
            jLabelListTextSizePreview.setFont(jLabelListTextSizePreview.getFont().deriveFont(15.0f));
        }
        if (strSliderValue.equals("6")){
            jListSessions.setFont(jListSessions.getFont().deriveFont(16.0f));
            jLabelListTextSizePreview.setFont(jLabelListTextSizePreview.getFont().deriveFont(16.0f));
        }
    }//GEN-LAST:event_jSliderListTextSizeStateChanged

    private void jRadioButtonConsolePuttyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonConsolePuttyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButtonConsolePuttyActionPerformed

    private void jRadioButtonConsolePuttyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jRadioButtonConsolePuttyMouseClicked
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(null, "Console with not work with old garbage versions of PuTTY from 2007(.60)!");
    }//GEN-LAST:event_jRadioButtonConsolePuttyMouseClicked

    private void jRadioButtonSSHClientPuTTYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonSSHClientPuTTYActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButtonSSHClientPuTTYActionPerformed

    private void jButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton29ActionPerformed
        // TODO add your handling code here:
        // Send message
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
        } catch (SocketException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
            jTextAreaNTPMessage.setText("Socket not opened.");

        }
        InetAddress address = null;
        try {
            address = InetAddress.getByName(jTextFieldNtpServer.getText());
        } catch (UnknownHostException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
            jTextAreaNTPMessage.setText("No IP received from DNS.");
        }
        byte[] buf = new NtpMessage().toByteArray();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 123);
        try {
            socket.send(packet);
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
            jTextAreaNTPMessage.setText("Nothing sent.");
        }

        try {
            // Get response
            socket.receive(packet);
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
            jTextAreaNTPMessage.setText("Nothing received.");

        }
        NtpMessage msg = new NtpMessage(packet.getData());
        //double offset = ((msg.receiveTimestamp - msg.originateTimestamp) + (msg.transmitTimestamp - destinationTimestamp)) / 2;

        System.out.println(msg.toString());
        jTextAreaNTPMessage.setText(msg.toString());
    }//GEN-LAST:event_jButton29ActionPerformed

    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed
        // TODO add your handling code here:
        String SERVER_NAME = jTextFieldNtpServer.getText();
        //String SERVER_NAME = "pool.ntp.org";
        NTPUDPClient timeClient = new NTPUDPClient();
        // We want to timeout if a response takes longer than 10 seconds
        timeClient.setDefaultTimeout(10000);

        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getByName(SERVER_NAME);
        } catch (UnknownHostException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Failed - Couldn't find server.");
            jTextFieldNtpSystemTime.setText("Failed - Couldn't find server: " + SERVER_NAME);
            jTextFieldNtpAtomicTime.setText("");
        }
        TimeInfo timeInfo = null;
        try {
            timeInfo = timeClient.getTime(inetAddress);
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Failed - Found server, but no time received. ");
            jTextFieldNtpSystemTime.setText("Failed - Found server: " + SERVER_NAME + ", but no time received. ");
            jTextFieldNtpAtomicTime.setText("");

        }
        long returnTime = timeInfo.getReturnTime();
        TimeStamp destNtpTime = TimeStamp.getNtpTime(returnTime);
        System.out.println("System time:\t" + destNtpTime + "  " + destNtpTime.toDateString());
        jTextFieldNtpSystemTime.setText("" + destNtpTime + "  " + destNtpTime.toDateString());

        TimeStamp currentNtpTime = TimeStamp.getCurrentTime();
        System.out.println("Atomic time:\t" + currentNtpTime + "  " + currentNtpTime.toDateString());
        jTextFieldNtpAtomicTime.setText("" + currentNtpTime + "  " + currentNtpTime.toDateString());
    }//GEN-LAST:event_jButton26ActionPerformed

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
            System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
        } else {
            System.out.println("No Selection ");
        }
    }//GEN-LAST:event_jButton27ActionPerformed

    private void jButtonGenerateHashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGenerateHashActionPerformed
        // TODO add your handling code here:

        File file = new File(jTextFieldFileHashGenerate.getText());

        //System.out.println("MD5    : " + toHex(HashGenerate.MD5.checksum(file)));
        jTextFieldHashMD5.setText(toHex(HashGenerate.MD5.checksum(file)));
        //System.out.println("SHA1   : " + toHex(HashGenerate.SHA1.checksum(file)));
        jTextFieldHashSHA1.setText(toHex(HashGenerate.SHA1.checksum(file)));
        //System.out.println("SHA256 : " + toHex(HashGenerate.SHA256.checksum(file)));
        jTextFieldHashSHA256.setText(toHex(HashGenerate.SHA256.checksum(file)));
        //System.out.println("SHA512 : " + toHex(HashGenerate.SHA512.checksum(file)));
        jTextFieldHashSHA512.setText(toHex(HashGenerate.SHA512.checksum(file)));
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
                    e.printStackTrace();
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
        // TODO add your handling code here:
        String inputPdf = "files/Subnets.pdf";
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
    }//GEN-LAST:event_jButton24ActionPerformed

    private void jButtonReferenceCustom6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom6ActionPerformed

    private void jButtonReferenceCustom5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom5ActionPerformed

    private void jButtonReferenceCustom4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom4ActionPerformed

    private void jButtonReferenceCustom3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom3ActionPerformed

    private void jButtonReferenceCustom2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom2ActionPerformed

    private void jButtonReferenceCustom7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom7ActionPerformed

    private void jButtonReferenceCustom05ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom05ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom05ActionPerformed

    private void jButtonReferenceCustom04ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom04ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom04ActionPerformed

    private void jButtonReferenceCustom03ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom03ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom03ActionPerformed

    private void jButtonReferenceCustom02ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom02ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom02ActionPerformed

    private void jButtonReferenceCustom01ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom01ActionPerformed
        // TODO add your handling code here:
        String strReference01;
        strReference01 = PropertyHandler.getInstance().getValue("ReferenceFile01");

        if(jToggleOfflineMode.isSelected()){
            strReference01 =  PropertyHandler.getInstance().getValue("ReferenceFolderOffline") + strReference01;
            System.out.println("Using Offline: " + strReference01);
        }
        else{
            strReference01 =  PropertyHandler.getInstance().getValue("ReferenceFolderOnline") + strReference01;
        }

        //text file, should be opening in default text editor
        File file = new File(strReference01);

        //first check if Desktop is supported by Platform or not
        if(!Desktop.isDesktopSupported()){
            System.out.println("Desktop is not supported");
            return;
        }

        Desktop desktop = Desktop.getDesktop();
        if(file.exists()) try {
            desktop.open(file);
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("launchpad.LaunchPadForm.jButtonReferenceCustom01ActionPerformed()");
        }

        // Open
        if(file.exists()) try {
            desktop.open(file);
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("launchpad.LaunchPadForm.jButtonReferenceCustom01ActionPerformed()");

        }
    }//GEN-LAST:event_jButtonReferenceCustom01ActionPerformed

    private void jButtonReferenceCustom06ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom06ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom06ActionPerformed

    private void jToggleOfflineModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleOfflineModeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jToggleOfflineModeActionPerformed

    private void jButtonScriptSyncStandalones1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptSyncStandalones1ActionPerformed
        // TODO add your handling code here:
        String myValue = "cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -File \"" + PropertyHandler.getInstance().getValue("ScriptStandaloneMapShare") + "\"";
        System.out.println(myValue);
        try {
            Runtime.getRuntime().exec(myValue);
        }
        catch (IOException e) {
            System.out.println("HEY Buddy ! U r Doing Something Wrong ");
            JOptionPane.showMessageDialog(null, "Something is wrong!");
        }
    }//GEN-LAST:event_jButtonScriptSyncStandalones1ActionPerformed

    private void jButtonScriptBackupSharesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptBackupSharesActionPerformed
        // TODO add your handling code here:
        String myValue = "cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -File \"" + PropertyHandler.getInstance().getValue("ScriptBackupShare") + "\"";
        System.out.println(myValue);
        try {
            Runtime.getRuntime().exec(myValue);
        }
        catch (IOException e) {
            System.out.println("HEY Buddy ! U r Doing Something Wrong ");
            JOptionPane.showMessageDialog(null, "Something is wrong!");
        }
    }//GEN-LAST:event_jButtonScriptBackupSharesActionPerformed

    private void jButtonScriptSyncStandalonesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptSyncStandalonesActionPerformed
        // TODO add your handling code here:
        String myValue = "cmd.exe /c start cmd.exe /c powershell.exe -ExecutionPolicy Bypass -File \"" + PropertyHandler.getInstance().getValue("ScriptStandaloneSync") + "\"";
        System.out.println(myValue);
        try {
            Runtime.getRuntime().exec(myValue);
        }
        catch (IOException e) {
            System.out.println("HEY Buddy ! U r Doing Something Wrong ");
            JOptionPane.showMessageDialog(null, "Something is wrong!");
        }
    }//GEN-LAST:event_jButtonScriptSyncStandalonesActionPerformed

    private void jButtonCustomScript28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCustomScript28ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonCustomScript28ActionPerformed

    private void jButtonCustomScript26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCustomScript26ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonCustomScript26ActionPerformed

    private void jButtonCustomScript25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCustomScript25ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonCustomScript25ActionPerformed

    private void jButtonCustomScript23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCustomScript23ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonCustomScript23ActionPerformed

    private void jButtonCustomScript22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCustomScript22ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonCustomScript22ActionPerformed

    private void jButtonCustomScript21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCustomScript21ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonCustomScript21ActionPerformed

    private void jButtonCustomScript19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCustomScript19ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonCustomScript19ActionPerformed

    private void jButtonCustomScript17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCustomScript17ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonCustomScript17ActionPerformed

    private void jButtonCustomScript16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCustomScript16ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonCustomScript16ActionPerformed

    private void jButtonCustomScript15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCustomScript15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonCustomScript15ActionPerformed

    private void jButtonCustomScript13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCustomScript13ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonCustomScript13ActionPerformed

    private void jButtonCustomScript12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCustomScript12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonCustomScript12ActionPerformed

    private void jButtonCustomScript2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCustomScript2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonCustomScript2ActionPerformed

    private void jButtonCustomScript3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCustomScript3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonCustomScript3ActionPerformed

    private void jButtonCustomScript4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCustomScript4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonCustomScript4ActionPerformed

    private void jButtonCustomScript07ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCustomScript07ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonCustomScript07ActionPerformed

    private void jButtonCustomScript8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCustomScript8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonCustomScript8ActionPerformed

    private void jButtonCustomScript05ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCustomScript05ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonCustomScript05ActionPerformed

    private void jButtonCustomScript04ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCustomScript04ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonCustomScript04ActionPerformed

    private void jButtonCustomScript02ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCustomScript02ActionPerformed
        // TODO add your handling code here:
        String myValue = PropertyHandler.getInstance().getValue("ScriptCustom05").replace("%USERPROFILE%", pathUserProfile);
        System.out.println(myValue);
        try {
            Runtime.getRuntime().exec(myValue);
        }
        catch (IOException e) {
            System.out.println("HEY Buddy ! U r Doing Something Wrong ");
            JOptionPane.showMessageDialog(null, "Something is wrong!");
        }
    }//GEN-LAST:event_jButtonCustomScript02ActionPerformed

    private void jButtonCustomScript01ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCustomScript01ActionPerformed
        // TODO add your handling code here:
        String myValue = PropertyHandler.getInstance().getValue("ScriptCustom04").replace("%USERPROFILE%", pathUserProfile);
        System.out.println(myValue);
        try {
            Runtime.getRuntime().exec(myValue);
        }
        catch (IOException e) {
            System.out.println("HEY Buddy ! U r Doing Something Wrong ");
            JOptionPane.showMessageDialog(null, "Something is wrong!");
        }
    }//GEN-LAST:event_jButtonCustomScript01ActionPerformed

    private void jButton35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton35ActionPerformed
        // TODO add your handling code here:
        String inputPdf = "apps/Puppeteer.jar";
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
    }//GEN-LAST:event_jButton35ActionPerformed

    private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed
        // TODO add your handling code here:
        String inputPdf = "apps/IPv6SubnetCalculator.jar";
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
    }//GEN-LAST:event_jButton30ActionPerformed

    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed
        // TODO add your handling code here:
        String inputPdf = "apps/IPSubnetCalculator.jar";
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
    }//GEN-LAST:event_jButton28ActionPerformed

    private void jButtonJSDiff1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonJSDiff1ActionPerformed
        // TODO add your handling code here:
        System.out.println("Pressed");
        String inputFile = "html/subnetcalculator/SubnetCalculator.html";
        InputStream manualAsStream = getClass().getClassLoader().getResourceAsStream(inputFile);

        Path tempOutput = null;
        try {
            tempOutput = Files.createTempFile("TempFile", ".html");
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        tempOutput.toFile().deleteOnExit();

        try {
            Files.copy(manualAsStream, tempOutput, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }

        File tempFile = new File (tempOutput.toFile().getPath());
        if (tempFile.exists())
        {
            //Desktop.getDesktop().open(tempFile);

        }
        String strWebpage = tempOutput.toFile().getPath();
        Icon iconExplorer = new ImageIcon(getClass().getResource("/launchpad/images/buttons/iexplore.png"));
        Icon iconEdge = new ImageIcon(getClass().getResource("/launchpad/images/buttons/edge.png"));
        Icon iconFireFox = new ImageIcon(getClass().getResource("/launchpad/images/buttons/firefox.png"));
        Icon iconChrome = new ImageIcon(getClass().getResource("/launchpad/images/buttons/chrome.png"));
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
            String strEXEC = "cmd /c start iexplore.exe " + strWebpage;
            try {
                Runtime.getRuntime().exec(strEXEC);
                System.out.println(strEXEC);
            }
            catch (IOException e) {
                System.out.println("HEY Buddy ! U r Doing Something Wrong ");
                JOptionPane.showMessageDialog(null, "Something is wrong!");
            }
        }
        if(result == 2) {
            String strEXEC = "cmd /c start firefox.exe " + strWebpage;
            try {
                Runtime.getRuntime().exec(strEXEC);
                System.out.println(strEXEC);
            }
            catch (IOException e) {
                System.out.println("HEY Buddy ! U r Doing Something Wrong ");
                JOptionPane.showMessageDialog(null, "Something is wrong!");
            }
        }
        if(result == 1) {
            String strEXEC = "cmd /c start microsoft-edge:" + strWebpage;
            try {
                Runtime.getRuntime().exec(strEXEC);
                System.out.println(strEXEC);
            }
            catch (IOException e) {
                System.out.println("HEY Buddy ! U r Doing Something Wrong ");
                JOptionPane.showMessageDialog(null, "Something is wrong!");
            }
        }
        if(result == 3) {
            String strEXEC = "cmd /c start chrome.exe " + strWebpage;
            try {
                Runtime.getRuntime().exec(strEXEC);
                System.out.println(strEXEC);
            }
            catch (IOException e) {
                System.out.println("HEY Buddy ! U r Doing Something Wrong ");
                JOptionPane.showMessageDialog(null, "Something is wrong!");
            }
        }
    }//GEN-LAST:event_jButtonJSDiff1ActionPerformed

    private void jButtonConfigBuilderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConfigBuilderActionPerformed
        // TODO add your handling code here:
        System.out.println("Pressed");
        String inputFile = "html/configbuilder/ConfigBuilder.html";
        InputStream manualAsStream = getClass().getClassLoader().getResourceAsStream(inputFile);

        Path tempOutput = null;
        try {
            tempOutput = Files.createTempFile("TempFile", ".html");
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        tempOutput.toFile().deleteOnExit();

        try {
            Files.copy(manualAsStream, tempOutput, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }

        File tempFile = new File (tempOutput.toFile().getPath());
        if (tempFile.exists())
        {
            //Desktop.getDesktop().open(tempFile);

        }
        String strWebpage = tempOutput.toFile().getPath();
        Icon iconExplorer = new ImageIcon(getClass().getResource("/launchpad/images/buttons/iexplore.png"));
        Icon iconEdge = new ImageIcon(getClass().getResource("/launchpad/images/buttons/edge.png"));
        Icon iconFireFox = new ImageIcon(getClass().getResource("/launchpad/images/buttons/firefox.png"));
        Icon iconChrome = new ImageIcon(getClass().getResource("/launchpad/images/buttons/chrome.png"));
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
            String strEXEC = "cmd /c start iexplore.exe " + strWebpage;
            try {
                Runtime.getRuntime().exec(strEXEC);
                System.out.println(strEXEC);
            }
            catch (IOException e) {
                System.out.println("HEY Buddy ! U r Doing Something Wrong ");
                JOptionPane.showMessageDialog(null, "Something is wrong!");
            }
        }
        if(result == 2) {
            String strEXEC = "cmd /c start firefox.exe " + strWebpage;
            try {
                Runtime.getRuntime().exec(strEXEC);
                System.out.println(strEXEC);
            }
            catch (IOException e) {
                System.out.println("HEY Buddy ! U r Doing Something Wrong ");
                JOptionPane.showMessageDialog(null, "Something is wrong!");
            }
        }
        if(result == 1) {
            String strEXEC = "cmd /c start microsoft-edge:" + strWebpage;
            try {
                Runtime.getRuntime().exec(strEXEC);
                System.out.println(strEXEC);
            }
            catch (IOException e) {
                System.out.println("HEY Buddy ! U r Doing Something Wrong ");
                JOptionPane.showMessageDialog(null, "Something is wrong!");
            }
        }
        if(result == 3) {
            String strEXEC = "cmd /c start chrome.exe " + strWebpage;
            try {
                Runtime.getRuntime().exec(strEXEC);
                System.out.println(strEXEC);
            }
            catch (IOException e) {
                System.out.println("HEY Buddy ! U r Doing Something Wrong ");
                JOptionPane.showMessageDialog(null, "Something is wrong!");
            }
        }
    }//GEN-LAST:event_jButtonConfigBuilderActionPerformed

    private void jButtonJSDiffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonJSDiffActionPerformed
        // TODO add your handling code here:
        System.out.println("Pressed");
        String inputFile = "html/jsdiff/jsDiff.html";
        InputStream manualAsStream = getClass().getClassLoader().getResourceAsStream(inputFile);

        Path tempOutput = null;
        try {
            tempOutput = Files.createTempFile("TempFile", ".html");
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        tempOutput.toFile().deleteOnExit();

        try {
            Files.copy(manualAsStream, tempOutput, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }

        File tempFile = new File (tempOutput.toFile().getPath());
        if (tempFile.exists())
        {
            //Desktop.getDesktop().open(tempFile);

        }
        String strWebpage = tempOutput.toFile().getPath();
        Icon iconExplorer = new ImageIcon(getClass().getResource("/launchpad/images/buttons/iexplore.png"));
        Icon iconEdge = new ImageIcon(getClass().getResource("/launchpad/images/buttons/edge.png"));
        Icon iconFireFox = new ImageIcon(getClass().getResource("/launchpad/images/buttons/firefox.png"));
        Icon iconChrome = new ImageIcon(getClass().getResource("/launchpad/images/buttons/chrome.png"));
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
            String strEXEC = "cmd /c start iexplore.exe " + strWebpage;
            try {
                Runtime.getRuntime().exec(strEXEC);
                System.out.println(strEXEC);
            }
            catch (IOException e) {
                System.out.println("HEY Buddy ! U r Doing Something Wrong ");
                JOptionPane.showMessageDialog(null, "Something is wrong!");
            }
        }
        if(result == 2) {
            String strEXEC = "cmd /c start firefox.exe " + strWebpage;
            try {
                Runtime.getRuntime().exec(strEXEC);
                System.out.println(strEXEC);
            }
            catch (IOException e) {
                System.out.println("HEY Buddy ! U r Doing Something Wrong ");
                JOptionPane.showMessageDialog(null, "Something is wrong!");
            }
        }
        if(result == 1) {
            String strEXEC = "cmd /c start microsoft-edge:" + strWebpage;
            try {
                Runtime.getRuntime().exec(strEXEC);
                System.out.println(strEXEC);
            }
            catch (IOException e) {
                System.out.println("HEY Buddy ! U r Doing Something Wrong ");
                JOptionPane.showMessageDialog(null, "Something is wrong!");
            }
        }
        if(result == 3) {
            String strEXEC = "cmd /c start chrome.exe " + strWebpage;
            try {
                Runtime.getRuntime().exec(strEXEC);
                System.out.println(strEXEC);
            }
            catch (IOException e) {
                System.out.println("HEY Buddy ! U r Doing Something Wrong ");
                JOptionPane.showMessageDialog(null, "Something is wrong!");
            }
        }
    }//GEN-LAST:event_jButtonJSDiffActionPerformed

    private void jButtonAppCustom29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAppCustom29ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonAppCustom29ActionPerformed

    private void jButtonAppCustom28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAppCustom28ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonAppCustom28ActionPerformed

    private void jButtonAppCustom27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAppCustom27ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonAppCustom27ActionPerformed

    private void jButtonAppCustom25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAppCustom25ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonAppCustom25ActionPerformed

    private void jButtonAppCustom24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAppCustom24ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonAppCustom24ActionPerformed

    private void jButtonAppCustom23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAppCustom23ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonAppCustom23ActionPerformed

    private void jButtonAppCustom22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAppCustom22ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonAppCustom22ActionPerformed

    private void jButtonAppCustom21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAppCustom21ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonAppCustom21ActionPerformed

    private void jButtonAppCustom20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAppCustom20ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonAppCustom20ActionPerformed

    private void jButtonAppCustom19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAppCustom19ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonAppCustom19ActionPerformed

    private void jButtonAppCustom18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAppCustom18ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonAppCustom18ActionPerformed

    private void jButtonAppCustom17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAppCustom17ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonAppCustom17ActionPerformed

    private void jButtonAppCustom16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAppCustom16ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonAppCustom16ActionPerformed

    private void jButtonAppCustom15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAppCustom15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonAppCustom15ActionPerformed

    private void jButtonAppCustom14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAppCustom14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonAppCustom14ActionPerformed

    private void jButtonAppCustom7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAppCustom7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonAppCustom7ActionPerformed

    private void jButtonAppCustom6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAppCustom6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonAppCustom6ActionPerformed

    private void jButtonAppCustom5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAppCustom5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonAppCustom5ActionPerformed

    private void jButtonAppCustom4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAppCustom4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonAppCustom4ActionPerformed

    private void jButtonAppCustom2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAppCustom2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonAppCustom2ActionPerformed

    private void jButtonAppCustom3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAppCustom3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonAppCustom3ActionPerformed

    private void jButtonAppCustom13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAppCustom13ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonAppCustom13ActionPerformed

    private void jButtonAppCustom12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAppCustom12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonAppCustom12ActionPerformed

    private void jButtonAppCustom11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAppCustom11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonAppCustom11ActionPerformed

    private void jButtonAppCustom10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAppCustom10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonAppCustom10ActionPerformed

    private void jButtonAppCustom9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAppCustom9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonAppCustom9ActionPerformed

    private void jButtonAppCustom8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAppCustom8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonAppCustom8ActionPerformed

    private void jButtonAppCustom06ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAppCustom06ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonAppCustom06ActionPerformed

    private void jButtonAppCustom05ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAppCustom05ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonAppCustom05ActionPerformed

    private void jButtonLinkCustom01ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom01ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonLinkCustom01ActionPerformed

    private void jCheckBox1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBox1StateChanged

    }//GEN-LAST:event_jCheckBox1StateChanged

    private void jCheckBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox1ItemStateChanged
        // TODO add your handling code here:
        try {
            getSessionList();
        } catch (IOException | URISyntaxException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jCheckBox1ItemStateChanged

    private void jTextFieldFilterKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldFilterKeyReleased
        // TODO add your handling code here:
        try {
            searchFilter(jTextFieldFilter.getText());
        } catch (IOException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jTextFieldFilterKeyReleased

    private void jTextFieldFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldFilterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldFilterActionPerformed

    private void jButtonExecuteFunction2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExecuteFunction2ActionPerformed
        // TODO add your handling code here:
        System.out.println("RDP enganged.");
        String strEXEC = "cmd /c start mstsc.exe /v:" + jTextFieldConnectHostname.getText();
        try {
            Runtime.getRuntime().exec(strEXEC);
        }
        catch (IOException e) {
            System.out.println("HEY Buddy ! U r Doing Something Wrong ");
            JOptionPane.showMessageDialog(null, "Something is wrong!");
        }
    }//GEN-LAST:event_jButtonExecuteFunction2ActionPerformed

    private void jCheckBoxAlternateLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxAlternateLoginActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBoxAlternateLoginActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        // TODO add your handling code here:
        String myValue = PropertyHandler.getInstance().getValue("Button17exe").replace("%USERPROFILE%", pathUserProfile);
        System.out.println(myValue);
        try {
            Runtime.getRuntime().exec(myValue);
        }
        catch (IOException e) {
            System.out.println("HEY Buddy ! U r Doing Something Wrong ");
            JOptionPane.showMessageDialog(null, "Something is wrong!");
        }
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        // TODO add your handling code here:
        String myValue = PropertyHandler.getInstance().getValue("Button18exe").replace("%USERPROFILE%", pathUserProfile);
        System.out.println(myValue);
        try {
            Runtime.getRuntime().exec(myValue);
        }
        catch (IOException e) {
            System.out.println("HEY Buddy ! U r Doing Something Wrong ");
            JOptionPane.showMessageDialog(null, "Something is wrong!");
        }
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        // TODO add your handling code here:
        String myValue = PropertyHandler.getInstance().getValue("Button19exe").replace("%USERPROFILE%", pathUserProfile);
        System.out.println(myValue);
        try {
            Runtime.getRuntime().exec(myValue);
        }
        catch (IOException e) {
            System.out.println("HEY Buddy ! U r Doing Something Wrong ");
            JOptionPane.showMessageDialog(null, "Something is wrong!");
        }
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        // TODO add your handling code here:
        String myValue = PropertyHandler.getInstance().getValue("Button20exe").replace("%USERPROFILE%", pathUserProfile);
        System.out.println(myValue);
        try {
            Runtime.getRuntime().exec(myValue);
        }
        catch (IOException e) {
            System.out.println("HEY Buddy ! U r Doing Something Wrong ");
            JOptionPane.showMessageDialog(null, "Something is wrong!");
        }
    }//GEN-LAST:event_jButton20ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        // TODO add your handling code here:
        String myValue = PropertyHandler.getInstance().getValue("Button16exe").replace("%USERPROFILE%", pathUserProfile);
        System.out.println(myValue);
        try {
            Runtime.getRuntime().exec(myValue);
        }
        catch (IOException e) {
            System.out.println("HEY Buddy ! U r Doing Something Wrong ");
            JOptionPane.showMessageDialog(null, "Something is wrong!");
        }
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // TODO add your handling code here:
        String myValue = PropertyHandler.getInstance().getValue("Button15exe").replace("%USERPROFILE%", pathUserProfile);
        System.out.println(myValue);
        try {
            Runtime.getRuntime().exec(myValue);
        }
        catch (IOException e) {
            System.out.println("HEY Buddy ! U r Doing Something Wrong ");
            JOptionPane.showMessageDialog(null, "Something is wrong!");
        }
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // TODO add your handling code here:
        String myValue = PropertyHandler.getInstance().getValue("Button14exe").replace("%USERPROFILE%", pathUserProfile);
        System.out.println(myValue);
        try {
            Runtime.getRuntime().exec(myValue);
        }
        catch (IOException e) {
            System.out.println("HEY Buddy ! U r Doing Something Wrong ");
            JOptionPane.showMessageDialog(null, "Something is wrong!");
        }
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
        String myValue = PropertyHandler.getInstance().getValue("Button13exe").replace("%USERPROFILE%", pathUserProfile);
        System.out.println(myValue);
        try {
            Runtime.getRuntime().exec(myValue);
        }
        catch (IOException e) {
            System.out.println("HEY Buddy ! U r Doing Something Wrong ");
            JOptionPane.showMessageDialog(null, "Something is wrong!");
        }
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        String myValue = PropertyHandler.getInstance().getValue("Button09exe").replace("%USERPROFILE%", pathUserProfile);
        System.out.println(myValue);
        try {
            Runtime.getRuntime().exec(myValue);
        }
        catch (IOException e) {
            System.out.println("HEY Buddy ! U r Doing Something Wrong ");
            JOptionPane.showMessageDialog(null, "Something is wrong!");
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        String myValue = PropertyHandler.getInstance().getValue("Button05exe").replace("%USERPROFILE%", pathUserProfile);
        System.out.println(myValue);
        try {
            Runtime.getRuntime().exec(myValue);
        }
        catch (IOException e) {
            System.out.println("HEY Buddy ! U r Doing Something Wrong ");
            JOptionPane.showMessageDialog(null, "Something is wrong!");
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        String myValue = PropertyHandler.getInstance().getValue("Button10exe").replace("%USERPROFILE%", pathUserProfile);
        System.out.println(myValue);
        try {
            Runtime.getRuntime().exec(myValue);
        }
        catch (IOException e) {
            System.out.println("HEY Buddy ! U r Doing Something Wrong ");
            JOptionPane.showMessageDialog(null, "Something is wrong!");
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        String myValue = PropertyHandler.getInstance().getValue("Button06exe").replace("%USERPROFILE%", pathUserProfile);
        System.out.println(myValue);
        try {
            Runtime.getRuntime().exec(myValue);
        }
        catch (IOException e) {
            System.out.println("HEY Buddy ! U r Doing Something Wrong ");
            JOptionPane.showMessageDialog(null, "Something is wrong!");
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
        String myValue = PropertyHandler.getInstance().getValue("Button11exe").replace("%USERPROFILE%", pathUserProfile);
        System.out.println(myValue);
        try {
            Runtime.getRuntime().exec(myValue);
        }
        catch (IOException e) {
            System.out.println("HEY Buddy ! U r Doing Something Wrong ");
            JOptionPane.showMessageDialog(null, "Something is wrong!");
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        String myValue = PropertyHandler.getInstance().getValue("Button07exe").replace("%USERPROFILE%", pathUserProfile);
        System.out.println(myValue);
        try {
            Runtime.getRuntime().exec(myValue);
        }
        catch (IOException e) {
            System.out.println("HEY Buddy ! U r Doing Something Wrong ");
            JOptionPane.showMessageDialog(null, "Something is wrong!");
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
        String myValue = PropertyHandler.getInstance().getValue("Button12exe").replace("%USERPROFILE%", pathUserProfile);
        System.out.println(myValue);
        try {
            Runtime.getRuntime().exec(myValue);
        }
        catch (IOException e) {
            System.out.println("HEY Buddy ! U r Doing Something Wrong ");
            JOptionPane.showMessageDialog(null, "Something is wrong!");
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        String myValue = PropertyHandler.getInstance().getValue("Button08exe").replace("%USERPROFILE%", pathUserProfile);
        System.out.println(myValue);
        try {
            Runtime.getRuntime().exec(myValue);
        }
        catch (IOException e) {
            System.out.println("HEY Buddy ! U r Doing Something Wrong ");
            JOptionPane.showMessageDialog(null, "Something is wrong!");
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        String myValue = PropertyHandler.getInstance().getValue("Button04exe").replace("%USERPROFILE%", pathUserProfile);
        System.out.println(myValue);
        try {
            Runtime.getRuntime().exec(myValue);
        }
        catch (IOException e) {
            System.out.println("HEY Buddy ! U r Doing Something Wrong ");
            JOptionPane.showMessageDialog(null, "Something is wrong!");
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        String myValue = PropertyHandler.getInstance().getValue("Button03exe").replace("%USERPROFILE%", pathUserProfile);
        System.out.println(myValue);
        try {
            Runtime.getRuntime().exec(myValue);
        }
        catch (IOException e) {
            System.out.println("HEY Buddy ! U r Doing Something Wrong ");
            JOptionPane.showMessageDialog(null, "Something is wrong!");
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        String myValue = PropertyHandler.getInstance().getValue("Button02exe").replace("%USERPROFILE%", pathUserProfile);
        System.out.println(myValue);
        try {
            Runtime.getRuntime().exec(myValue);
        }
        catch (IOException e) {
            System.out.println("HEY Buddy ! U r Doing Something Wrong ");
            JOptionPane.showMessageDialog(null, "Something is wrong!");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        String myValue = PropertyHandler.getInstance().getValue("Button01exe").replace("%USERPROFILE%", pathUserProfile);
        System.out.println(myValue);
        try {
            Runtime.getRuntime().exec(myValue);
        }
        catch (IOException e) {
            System.out.println("HEY Buddy ! U r Doing Something Wrong ");
            JOptionPane.showMessageDialog(null, "Something is wrong!");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButtonConsoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConsoleActionPerformed
        // TODO add your handling code here:
        String strSecureCRTexe = PropertyHandler.getInstance().getValue("SecureCRTexe").replace("%USERPROFILE%", pathUserProfile);
        String strPuTTYexe = PropertyHandler.getInstance().getValue("PuTTYexe").replace("%USERPROFILE%", pathUserProfile);

        System.out.println("SecureCRT file: " + strSecureCRTexe);
        System.out.println("PuTTY file: " + strPuTTYexe);

        SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("yyyyMMdd_HHmm-ssSSS");
        String dateTime = simpleDateFormat.format(new Date());
        String fileLog = pathLogging + "\\Serial-" + dateTime + ".txt";
        System.out.println("Log file: " + fileLog);

        if (jRadioButtonConsolePutty.isSelected() == true) {
            String strEXEC = strPuTTYexe + " -serial " + jComboBoxConsoleCOM.getSelectedItem() + " -sercfg " + jComboBoxConsoleBaud.getSelectedItem() + " ,8,n,1,N  ";
            System.out.println(strEXEC);
            try {
                Runtime.getRuntime().exec(strEXEC);
            }
            catch (IOException e) {
                System.out.println("HEY Buddy ! U r Doing Something Wrong ");
                JOptionPane.showMessageDialog(null, "Something is wrong!");
            }
        }
        if (jRadioButtonConsoleSecureCRT.isSelected() == true) {
            String strEXEC = strSecureCRTexe + " /LOG \"" + fileLog + "\" /T /SERIAL " + jComboBoxConsoleCOM.getSelectedItem() + " /BAUD " + jComboBoxConsoleBaud.getSelectedItem();
            System.out.println(strEXEC);
            try {
                Runtime.getRuntime().exec(strEXEC);
            }
            catch (IOException e) {
                System.out.println("HEY Buddy ! U r Doing Something Wrong ");
                JOptionPane.showMessageDialog(null, "Something is wrong!");
            }
        }
    }//GEN-LAST:event_jButtonConsoleActionPerformed

    private void jButtonShowCOMListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonShowCOMListActionPerformed
        // TODO add your handling code here:
        System.out.println("Pressed");
        try {
            Runtime.getRuntime().exec("cmd /c start cmd.exe /c \"change port /query & pause");
        }
        catch (IOException e) {
            System.out.println("HEY Buddy ! U r Doing Something Wrong ");
        }
    }//GEN-LAST:event_jButtonShowCOMListActionPerformed

    private void jComboBoxConsoleCOMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxConsoleCOMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxConsoleCOMActionPerformed

    private void jButtonPingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPingActionPerformed
        // TODO add your handling code here:
        System.out.println("Pressed");
        String StringPingDNS = "";
        if(jCheckBoxDNS.isSelected()) {
            StringPingDNS = " -a ";
        }
        try {
            Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"ping -t " + StringPingDNS + " " + jTextFieldPingHostname.getText() + "\"");
        }
        catch (IOException e) {
            System.out.println("HEY Buddy ! U r Doing Something Wrong ");
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
            System.out.println("HEY Buddy ! U r Doing Something Wrong ");
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
            jButtonExecuteFunction3.doClick();
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
            jButtonExecuteFunction3.doClick();
        }
    }//GEN-LAST:event_jTextFieldConnectUsernameKeyTyped

    private void jButtonExecuteFunction3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExecuteFunction3ActionPerformed
        // TODO add your handling code here:
        String strSecureCRTexe = PropertyHandler.getInstance().getValue("SecureCRTexe").replace("%USERPROFILE%", pathUserProfile);
        String strPuTTYexe = PropertyHandler.getInstance().getValue("PuTTYexe").replace("%USERPROFILE%", pathUserProfile);
        System.out.println("SecureCRT file: " + strSecureCRTexe);
        System.out.println("PuTTY file: " + strPuTTYexe);
        simpleDateFormat  = new SimpleDateFormat("yyyyMMdd_HHmm-ssSSS");
        dateTime = simpleDateFormat.format(new Date());
        String fileLog = pathDesktop + "\\Logging-Output\\SSH-" + jTextFieldConnectHostname.getText() + " " + dateTime.toString() + ".txt";
        System.out.println("Log file: " + fileLog);

        if (jRadioButtonSSHClientPuTTY.isSelected() == true) {

            if (!jCheckBoxAlternateLogin.isSelected()) {
                //if (jTextFieldConnectUsername.getText().equals("")){
                    //if (jPasswordFieldConnectPassword.getPassword().length == 0){
                        String strEXEC = strPuTTYexe + " -ssh " + jTextFieldConnectHostname.getText() + "  ";
                        try {
                            Runtime.getRuntime().exec(strEXEC);
                        }
                        catch (IOException e) {
                            System.out.println("HEY Buddy ! U r Doing Something Wrong ");
                            JOptionPane.showMessageDialog(null, "Something is wrong!");
                        }
                        //}
                    //}
            }
            else {
                String passText = new String(jPasswordFieldConnectPassword.getPassword());
                String strEXEC = strPuTTYexe + " -ssh " + jTextFieldConnectHostname.getText() + " -l " + jTextFieldConnectUsername.getText() + " -pw \"" + passText + "\"  ";
                try {
                    Runtime.getRuntime().exec(strEXEC);
                }
                catch (IOException e) {
                    System.out.println("HEY Buddy ! U r Doing Something Wrong ");
                    JOptionPane.showMessageDialog(null, "Something is wrong!");
                }
            }
        }
        if (jRadioButtonSSHClientSecureCRT.isSelected() == true) {

            if (!jCheckBoxAlternateLogin.isSelected()) {
                //if (jTextFieldConnectUsername.getText().equals("")){
                    //if (jPasswordFieldConnectPassword.getPassword().length == 0){
                        String strEXEC = strSecureCRTexe + " /LOG \"" + fileLog + "\" /T /SSH2 /ACCEPTHOSTKEYS " + jTextFieldConnectHostname.getText();
                        System.out.println(strEXEC);
                        try {
                            Runtime.getRuntime().exec(strEXEC);
                        }
                        catch (IOException e) {
                            System.out.println("HEY Buddy ! U r Doing Something Wrong ");
                            JOptionPane.showMessageDialog(null, "Something is wrong!");
                        }
                        //}
                    //}
            }
            else {
                String passText = new String(jPasswordFieldConnectPassword.getPassword());
                String strEXEC = strSecureCRTexe + " /LOG \"" + fileLog + "\" /T /SSH2 /ACCEPTHOSTKEYS " + jTextFieldConnectHostname.getText() + " /L " + jTextFieldConnectUsername.getText() + " /PASSWORD \"" + passText + "\"  ";
                try {
                    Runtime.getRuntime().exec(strEXEC);
                }
                catch (IOException e) {
                    System.out.println("HEY Buddy ! U r Doing Something Wrong ");
                    JOptionPane.showMessageDialog(null, "Something is wrong!");
                }
            }
        }
    }//GEN-LAST:event_jButtonExecuteFunction3ActionPerformed

    private void jButtonExecuteFunction1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExecuteFunction1ActionPerformed
        // TODO add your handling code here:
        System.out.println("Pressed");
        Icon iconExplorer = new ImageIcon(getClass().getResource("/launchpad/images/buttons/iexplore.png"));
        Icon iconEdge = new ImageIcon(getClass().getResource("/launchpad/images/buttons/edge.png"));
        Icon iconFireFox = new ImageIcon(getClass().getResource("/launchpad/images/buttons/firefox.png"));
        Icon iconChrome = new ImageIcon(getClass().getResource("/launchpad/images/buttons/chrome.png"));
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
            String strEXEC = "cmd /c start iexplore.exe " + jTextFieldConnectHostname.getText();
            try {
                Runtime.getRuntime().exec(strEXEC);
            }
            catch (IOException e) {
                System.out.println("HEY Buddy ! U r Doing Something Wrong ");
                JOptionPane.showMessageDialog(null, "Something is wrong!");
            }
        }
        if(result == 2) {
            System.out.println("FireKitsune engaged.");
            String strEXEC = "cmd /c start firefox.exe " + jTextFieldConnectHostname.getText();
            try {
                Runtime.getRuntime().exec(strEXEC);
            }
            catch (IOException e) {
                System.out.println("HEY Buddy ! U r Doing Something Wrong ");
                JOptionPane.showMessageDialog(null, "Something is wrong!");
            }
        }
        if(result == 1) {
            System.out.println("Edge engaged.");
            String strEXEC = "cmd /c start microsoft-edge:" + jTextFieldConnectHostname.getText();
            try {
                Runtime.getRuntime().exec(strEXEC);
            }
            catch (IOException e) {
                System.out.println("HEY Buddy ! U r Doing Something Wrong ");
                JOptionPane.showMessageDialog(null, "Something is wrong!");
            }
        }
        if(result == 3) {
            System.out.println("Chrome engaged.");
            String strEXEC = "cmd /c start chrome.exe " + jTextFieldConnectHostname.getText();
            try {
                Runtime.getRuntime().exec(strEXEC);
            }
            catch (IOException e) {
                System.out.println("HEY Buddy ! U r Doing Something Wrong ");
                JOptionPane.showMessageDialog(null, "Something is wrong!");
            }
        }
    }//GEN-LAST:event_jButtonExecuteFunction1ActionPerformed

    private void jTextFieldConnectHostnameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldConnectHostnameKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            Robot r = null;
            try {
                r = new Robot();
            } catch (AWTException ex) {
                Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
            }
            String myValue = PropertyHandler.getInstance().getValue("ButtonExecuteFunctionOnEnterPress");
            if("".equals(myValue)) {
                PropertyHandler.getInstance().setValue("ButtonExecuteFunctionOnEnterPress", "3");
            }
            myValue = PropertyHandler.getInstance().getValue("ButtonExecuteFunctionOnEnterPress");
            if("1".equals(myValue)) {
                jButtonExecuteFunction1.doClick();
            } else if("2".equals(myValue)) {
                jButtonExecuteFunction2.doClick();
            } else if("3".equals(myValue)){
                jButtonExecuteFunction3.doClick();
            }

        }
    }//GEN-LAST:event_jTextFieldConnectHostnameKeyTyped

    private void jTextFieldConnectHostnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldConnectHostnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldConnectHostnameActionPerformed

    private void jListSessionsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListSessionsValueChanged
        // TODO add your handling code here:
        String strSelectedValue = jListSessions.getSelectedValue();
        if(strSelectedValue.contains(",")) {
            String[] arrSelectedValue = strSelectedValue.split(",");
            jTextFieldConnectHostname.setText(arrSelectedValue[1]);
            jTextFieldPingHostname.setText(arrSelectedValue[1]);
        }
    }//GEN-LAST:event_jListSessionsValueChanged

    
    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String args[]) throws FileNotFoundException, IOException {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                    if ("Windows".equals(info.getName())) {       
//                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    }
//                }
        //</editor-fold>
        //</editor-fold>
        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(() -> {
//            try {
//
//                new LaunchPadForm().setVisible(true);
//            } catch (IOException | URISyntaxException | AWTException | InterruptedException ex) {
//                Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        });
        
        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(() -> {
//            try {
//                
//                new LaunchPadForm().setVisible(true);
//            } catch (IOException | URISyntaxException | AWTException | InterruptedException ex) {
//                Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        });
        


    }
    
    

   
     private ArrayList getSessionList() throws FileNotFoundException, IOException, URISyntaxException
    {
        String strSessionList;
        DefaultListModel listModel = new DefaultListModel();
        ArrayList arrSessionList = new ArrayList();
        //File pathWorkingDirectory = new File(System.getProperty("user.dir"));
        if(jCheckBox1.isSelected()){
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
" ~~~~~~~~ ROUTERS ~~~~~~~~~",
"C1000-a01-asdf-090-Rm100,10.0.0.14",
"C1000-a01-asdf-A51-0710-Rm100,10.0.0.15",
"",
" ~~~~~~~ CORE NODES ~~~~~~~~",
"C6500-a01-asdf-0700-Rm100,10.0.0.12",
"C6500-a01-asdf-0700-Rm100,10.0.0.13",
"",
" ~~~~ DISTRIBUTION NODES ~~~~",
"C4500-a01-asdf-0020-1FL-Rm100,10.0.0.8",
"C4500-a01-asdf-0020-2FL-Rm200_C4500,10.0.0.9",
"",
" ~~~~~~ ACCESS NODES ~~~~~~ ",
"C9300-a01-asdf-0001-1FL-Rm100,10.0.0.5",
"C3850-a01-asdf-0001-2FL-Rm200,10.0.0.6",
"C3750-a01-asdf-0051-BSMT-Rm2,10.0.0.7",
"",
" ~~~~~~~~ SERVERS ~~~~~~~~~",
"Cisco ISE,10.0.1.2",
"Cisco ACS,10.0.1.3",
"InfoBlox,10.0.1.4");
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
    
    
    
  /**
   * List directory contents for a resource folder. Not recursive.
   * This is basically a brute-force implementation.
   * Works for regular files and also JARs.
   * 
   * @author Greg Briggs
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
  
    class MyMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent evt) {
          if (evt.getClickCount() == 3) {
            System.out.println("triple-click");
          } else if (evt.getClickCount() == 2) {
            System.out.println("double-click");
            String strSelectedValue = jListSessions.getSelectedValue();
            if(strSelectedValue.contains(",")) {
                String[] arrSelectedValue = strSelectedValue.split(",");
                jTextFieldConnectHostname.setText(arrSelectedValue[1]);
                jTextFieldPingHostname.setText(arrSelectedValue[1]);
            }
            String myValue = PropertyHandler.getInstance().getValue("ButtonExecuteFunctionDoubleClick");
            if("".equals(myValue)) {
                PropertyHandler.getInstance().setValue("ButtonExecuteFunctionDoubleClick", "3");
            }              
            myValue = PropertyHandler.getInstance().getValue("ButtonExecuteFunctionDoubleClick");
            if("1".equals(myValue)) {
                jButtonExecuteFunction1.doClick();
            } else if("2".equals(myValue)) {
                jButtonExecuteFunction2.doClick();               
            } else if("3".equals(myValue)){
                jButtonExecuteFunction3.doClick();
            }
            
          }
        }
      }
  
  

  
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroupConsoleClient;
    private javax.swing.ButtonGroup buttonGroupSSHClient;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton35;
    private javax.swing.JButton jButton36;
    private javax.swing.JButton jButton37;
    private javax.swing.JButton jButton39;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JButton jButtonAppCustom05;
    private javax.swing.JButton jButtonAppCustom06;
    private javax.swing.JButton jButtonAppCustom10;
    private javax.swing.JButton jButtonAppCustom11;
    private javax.swing.JButton jButtonAppCustom12;
    private javax.swing.JButton jButtonAppCustom13;
    private javax.swing.JButton jButtonAppCustom14;
    private javax.swing.JButton jButtonAppCustom15;
    private javax.swing.JButton jButtonAppCustom16;
    private javax.swing.JButton jButtonAppCustom17;
    private javax.swing.JButton jButtonAppCustom18;
    private javax.swing.JButton jButtonAppCustom19;
    private javax.swing.JButton jButtonAppCustom2;
    private javax.swing.JButton jButtonAppCustom20;
    private javax.swing.JButton jButtonAppCustom21;
    private javax.swing.JButton jButtonAppCustom22;
    private javax.swing.JButton jButtonAppCustom23;
    private javax.swing.JButton jButtonAppCustom24;
    private javax.swing.JButton jButtonAppCustom25;
    private javax.swing.JButton jButtonAppCustom27;
    private javax.swing.JButton jButtonAppCustom28;
    private javax.swing.JButton jButtonAppCustom29;
    private javax.swing.JButton jButtonAppCustom3;
    private javax.swing.JButton jButtonAppCustom4;
    private javax.swing.JButton jButtonAppCustom5;
    private javax.swing.JButton jButtonAppCustom6;
    private javax.swing.JButton jButtonAppCustom7;
    private javax.swing.JButton jButtonAppCustom8;
    private javax.swing.JButton jButtonAppCustom9;
    private javax.swing.JButton jButtonConfigBuilder;
    private javax.swing.JButton jButtonConsole;
    private javax.swing.JButton jButtonCustomScript01;
    private javax.swing.JButton jButtonCustomScript02;
    private javax.swing.JButton jButtonCustomScript03;
    private javax.swing.JButton jButtonCustomScript04;
    private javax.swing.JButton jButtonCustomScript05;
    private javax.swing.JButton jButtonCustomScript06;
    private javax.swing.JButton jButtonCustomScript07;
    private javax.swing.JButton jButtonCustomScript11;
    private javax.swing.JButton jButtonCustomScript12;
    private javax.swing.JButton jButtonCustomScript13;
    private javax.swing.JButton jButtonCustomScript14;
    private javax.swing.JButton jButtonCustomScript15;
    private javax.swing.JButton jButtonCustomScript16;
    private javax.swing.JButton jButtonCustomScript17;
    private javax.swing.JButton jButtonCustomScript18;
    private javax.swing.JButton jButtonCustomScript19;
    private javax.swing.JButton jButtonCustomScript2;
    private javax.swing.JButton jButtonCustomScript20;
    private javax.swing.JButton jButtonCustomScript21;
    private javax.swing.JButton jButtonCustomScript22;
    private javax.swing.JButton jButtonCustomScript23;
    private javax.swing.JButton jButtonCustomScript24;
    private javax.swing.JButton jButtonCustomScript25;
    private javax.swing.JButton jButtonCustomScript26;
    private javax.swing.JButton jButtonCustomScript27;
    private javax.swing.JButton jButtonCustomScript28;
    private javax.swing.JButton jButtonCustomScript3;
    private javax.swing.JButton jButtonCustomScript4;
    private javax.swing.JButton jButtonCustomScript8;
    private javax.swing.JButton jButtonCustomScript9;
    private javax.swing.JButton jButtonExecuteFunction1;
    private javax.swing.JButton jButtonExecuteFunction2;
    private javax.swing.JButton jButtonExecuteFunction3;
    private javax.swing.JButton jButtonFolderToZip;
    private javax.swing.JButton jButtonGenerateHash;
    private javax.swing.JButton jButtonJSDiff;
    private javax.swing.JButton jButtonJSDiff1;
    private javax.swing.JButton jButtonLinkCustom01;
    private javax.swing.JButton jButtonPing;
    private javax.swing.JButton jButtonReferenceCustom01;
    private javax.swing.JButton jButtonReferenceCustom02;
    private javax.swing.JButton jButtonReferenceCustom03;
    private javax.swing.JButton jButtonReferenceCustom04;
    private javax.swing.JButton jButtonReferenceCustom05;
    private javax.swing.JButton jButtonReferenceCustom06;
    private javax.swing.JButton jButtonReferenceCustom2;
    private javax.swing.JButton jButtonReferenceCustom3;
    private javax.swing.JButton jButtonReferenceCustom4;
    private javax.swing.JButton jButtonReferenceCustom5;
    private javax.swing.JButton jButtonReferenceCustom6;
    private javax.swing.JButton jButtonReferenceCustom7;
    private javax.swing.JButton jButtonReportIssue;
    private javax.swing.JButton jButtonScriptBackupShares;
    private javax.swing.JButton jButtonScriptSyncStandalones;
    private javax.swing.JButton jButtonScriptSyncStandalones1;
    private javax.swing.JButton jButtonScriptUpdateLaunchPad;
    private javax.swing.JButton jButtonShowCOMList;
    private javax.swing.JButton jButtonTracert;
    private javax.swing.JButton jButtonZipBrowseDestinationFolder;
    private javax.swing.JButton jButtonZipBrowseSourceZip;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBoxAlternateLogin;
    private javax.swing.JCheckBox jCheckBoxDNS;
    private javax.swing.JComboBox<String> jComboBoxConsoleBaud;
    private javax.swing.JComboBox<String> jComboBoxConsoleCOM;
    private javax.swing.JComboBox<String> jComboBoxZipEncMethod;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabelConsoleClient;
    private javax.swing.JLabel jLabelFolderToZip3;
    private javax.swing.JLabel jLabelFolderToZip4;
    private javax.swing.JLabel jLabelFolderToZip5;
    private javax.swing.JLabel jLabelFolderToZip6;
    private javax.swing.JLabel jLabelFolderToZip7;
    private javax.swing.JLabel jLabelListTextSize1;
    private javax.swing.JLabel jLabelListTextSizePreview;
    private javax.swing.JLabel jLabelSSHClient;
    private javax.swing.JList<String> jListSessions;

    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelAppsCustom;
    private javax.swing.JPanel jPanelAppsEmbedded;
    private javax.swing.JPanel jPanelMain;
    private javax.swing.JPanel jPanelSettings;
    private javax.swing.JPasswordField jPasswordFieldConnectPassword;
    private javax.swing.JPasswordField jPasswordFieldZip;
    private javax.swing.JPasswordField jPasswordFieldZipConfirm;
    private javax.swing.JPasswordField jPasswordFieldZipExtract;
    private javax.swing.JProgressBar jProgressBarZip;
    private javax.swing.JProgressBar jProgressBarZipExtract;
    private javax.swing.JRadioButton jRadioButtonConsolePutty;
    private javax.swing.JRadioButton jRadioButtonConsoleSecureCRT;
    private javax.swing.JRadioButton jRadioButtonSSHClientPuTTY;
    private javax.swing.JRadioButton jRadioButtonSSHClientSecureCRT;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JSlider jSliderListTextSize;
    private javax.swing.JTabbedPane jTabbedMain;
    private javax.swing.JTabbedPane jTabbedPaneReference;
    private javax.swing.JTabbedPane jTabbedPaneScripts;
    private javax.swing.JTabbedPane jTabbedPaneToolBox;
    private javax.swing.JTabbedPane jTabbedPanelLinks;
    private javax.swing.JTextArea jTextAreaNTPMessage;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextFieldConnectHostname;
    private javax.swing.JTextField jTextFieldConnectUsername;
    private javax.swing.JTextField jTextFieldFileHashGenerate;
    private javax.swing.JTextField jTextFieldFilter;
    private javax.swing.JTextField jTextFieldHashMD5;
    private javax.swing.JTextField jTextFieldHashSHA1;
    private javax.swing.JTextField jTextFieldHashSHA256;
    private javax.swing.JTextField jTextFieldHashSHA512;
    private javax.swing.JTextField jTextFieldNtpAtomicTime;
    private javax.swing.JTextField jTextFieldNtpServer;
    private javax.swing.JTextField jTextFieldNtpSystemTime;
    private javax.swing.JTextField jTextFieldPingHostname;
    private javax.swing.JTextField jTextFieldType7Input;
    private javax.swing.JTextField jTextFieldType7Output;
    private javax.swing.JTextField jTextFieldZipDestinationFolder;
    private javax.swing.JTextField jTextFieldZipFilename;
    private javax.swing.JTextField jTextFieldZipSourceFile;
    private javax.swing.JTextField jTextFieldZipSourceFolder;
    private javax.swing.JToggleButton jToggleOfflineMode;
    // End of variables declaration//GEN-END:variables




}