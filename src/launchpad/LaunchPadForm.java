/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package launchpad;

import java.awt.AWTException;
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
import javax.swing.border.Border;
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
    String strPathLaunchPadFolder = pathUserProfile + "\\.launchpad";
    String strSessionListFavoritesFolder = strPathLaunchPadFolder + "\\Favorites";   
    String strSessionListFavorites = strSessionListFavoritesFolder + "\\SessionList.csv";
    String strSessionListDefault = strPathLaunchPadFolder + "\\SessionList.csv";
    String strPathPropertiesFile = strPathLaunchPadFolder + "\\launchpad.properties";

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
    public LaunchPadForm() throws IOException, FileNotFoundException, URISyntaxException, AWTException, InterruptedException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
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
        
//        //- Swap LAF for toggle so the background will actually color
//        jToggleOnlineOfflineMode.setUI(new MetalToggleButtonUI() {
//            @Override
//            protected Color getSelectColor() {
//                return Color.GREEN;
//            }
//        });
//        //- Click it twitce to enable color
//        jToggleOnlineOfflineMode.doClick();
//        jToggleOnlineOfflineMode.doClick();
        
        //--- Listen for online/offline reference button
        jToggleOnlineOfflineMode.addItemListener((ItemEvent ev) -> {
            if(ev.getStateChange()==ItemEvent.SELECTED){
                System.out.println("Offline is selected");
                jToggleOnlineOfflineMode.setText("Offline");
                jToggleOnlineOfflineMode.setBackground(Color.GRAY);
//                jToggleOnlineOfflineMode.setUI(new MetalToggleButtonUI() {
//                    @Override
//                    protected Color getSelectColor() {
//                        return Color.GRAY;
//                    }
//                });                
            } else if(ev.getStateChange()==ItemEvent.DESELECTED){
                System.out.println("Offline is not selected");
                jToggleOnlineOfflineMode.setText("Online");
                //- Had to 
                jToggleOnlineOfflineMode.setBackground(Color.GREEN);
//                jToggleOnlineOfflineMode.setUI(new MetalToggleButtonUI() {
//                    @Override
//                    protected Color getSelectColor() {
//                        return Color.GREEN;
//                    }
//                });
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
        
        //-- Set to use the pointy finger cursor
        jButton1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButton2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButton3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButton4.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButton5.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButton6.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButton7.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButton8.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButton9.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButton10.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButton11.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButton12.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButton13.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButton14.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButton15.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButton16.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButton17.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButton18.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButton19.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButton20.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        //- Add the slight buuton movement when hovered over function
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButton1.getX(); int y = jButton1.getY(); jButton1.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButton1.getX(); int y = jButton1.getY(); jButton1.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButton1.getX(); int y = jButton1.getY(); jButton1.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButton1.getX(); int y = jButton1.getY(); jButton1.setLocation(x - 1, y - 1); } });
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButton2.getX(); int y = jButton2.getY(); jButton2.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButton2.getX(); int y = jButton2.getY(); jButton2.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButton2.getX(); int y = jButton2.getY(); jButton2.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButton2.getX(); int y = jButton2.getY(); jButton2.setLocation(x - 1, y - 1); } });
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButton3.getX(); int y = jButton3.getY(); jButton3.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButton3.getX(); int y = jButton3.getY(); jButton3.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButton3.getX(); int y = jButton3.getY(); jButton3.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButton3.getX(); int y = jButton3.getY(); jButton3.setLocation(x - 1, y - 1); } });
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButton4.getX(); int y = jButton4.getY(); jButton4.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButton5.getX(); int y = jButton5.getY(); jButton5.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButton5.getX(); int y = jButton5.getY(); jButton5.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButton5.getX(); int y = jButton5.getY(); jButton5.setLocation(x - 1, y - 1); } });
        jButton6.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButton6.getX(); int y = jButton6.getY(); jButton6.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButton6.getX(); int y = jButton6.getY(); jButton6.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButton6.getX(); int y = jButton6.getY(); jButton6.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButton6.getX(); int y = jButton6.getY(); jButton6.setLocation(x - 1, y - 1); } });
        jButton7.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButton7.getX(); int y = jButton7.getY(); jButton7.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButton7.getX(); int y = jButton7.getY(); jButton7.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButton7.getX(); int y = jButton7.getY(); jButton7.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButton7.getX(); int y = jButton7.getY(); jButton7.setLocation(x - 1, y - 1); } });
        jButton8.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButton8.getX(); int y = jButton8.getY(); jButton8.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButton8.getX(); int y = jButton8.getY(); jButton8.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButton8.getX(); int y = jButton8.getY(); jButton8.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButton8.getX(); int y = jButton8.getY(); jButton8.setLocation(x - 1, y - 1); } });
        jButton9.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButton9.getX(); int y = jButton9.getY(); jButton9.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButton9.getX(); int y = jButton9.getY(); jButton9.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButton9.getX(); int y = jButton9.getY(); jButton9.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButton9.getX(); int y = jButton9.getY(); jButton9.setLocation(x - 1, y - 1); } });
        jButton10.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButton10.getX(); int y = jButton10.getY(); jButton10.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButton10.getX(); int y = jButton10.getY(); jButton10.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButton10.getX(); int y = jButton10.getY(); jButton10.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButton10.getX(); int y = jButton10.getY(); jButton10.setLocation(x - 1, y - 1); } });
        jButton11.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButton11.getX(); int y = jButton11.getY(); jButton11.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButton11.getX(); int y = jButton11.getY(); jButton11.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButton11.getX(); int y = jButton11.getY(); jButton11.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButton11.getX(); int y = jButton11.getY(); jButton11.setLocation(x - 1, y - 1); } });
        jButton12.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButton12.getX(); int y = jButton12.getY(); jButton12.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButton12.getX(); int y = jButton12.getY(); jButton12.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButton12.getX(); int y = jButton12.getY(); jButton12.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButton12.getX(); int y = jButton12.getY(); jButton12.setLocation(x - 1, y - 1); } });
        jButton13.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButton13.getX(); int y = jButton13.getY(); jButton13.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButton13.getX(); int y = jButton13.getY(); jButton13.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButton13.getX(); int y = jButton13.getY(); jButton13.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButton13.getX(); int y = jButton13.getY(); jButton13.setLocation(x - 1, y - 1); } });
        jButton14.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButton14.getX(); int y = jButton14.getY(); jButton14.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButton14.getX(); int y = jButton14.getY(); jButton14.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButton14.getX(); int y = jButton14.getY(); jButton14.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButton14.getX(); int y = jButton14.getY(); jButton14.setLocation(x - 1, y - 1); } });
        jButton15.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButton15.getX(); int y = jButton15.getY(); jButton15.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButton15.getX(); int y = jButton15.getY(); jButton15.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButton15.getX(); int y = jButton15.getY(); jButton15.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButton15.getX(); int y = jButton15.getY(); jButton15.setLocation(x - 1, y - 1); } });
        jButton16.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButton16.getX(); int y = jButton16.getY(); jButton16.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButton16.getX(); int y = jButton16.getY(); jButton16.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButton16.getX(); int y = jButton16.getY(); jButton16.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButton16.getX(); int y = jButton16.getY(); jButton16.setLocation(x - 1, y - 1); } });
        jButton17.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButton17.getX(); int y = jButton17.getY(); jButton17.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButton17.getX(); int y = jButton17.getY(); jButton17.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButton17.getX(); int y = jButton17.getY(); jButton17.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButton17.getX(); int y = jButton17.getY(); jButton17.setLocation(x - 1, y - 1); } });
        jButton18.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButton18.getX(); int y = jButton18.getY(); jButton18.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButton18.getX(); int y = jButton18.getY(); jButton18.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButton19.getX(); int y = jButton19.getY(); jButton19.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButton19.getX(); int y = jButton19.getY(); jButton19.setLocation(x - 1, y - 1); } });
        jButton20.addMouseListener(new java.awt.event.MouseAdapter() {@Override public void mouseEntered(java.awt.event.MouseEvent evt) { int x = jButton20.getX(); int y = jButton20.getY(); jButton20.setLocation(x + -1, y + -1);}@Override public void mouseExited(java.awt.event.MouseEvent evt) { int x = jButton20.getX(); int y = jButton20.getY(); jButton20.setLocation(x + 1, y + 1); }@Override public void mousePressed(java.awt.event.MouseEvent evt) { int x = jButton20.getX(); int y = jButton20.getY(); jButton20.setLocation(x + 1, y + 1); }@Override public void mouseReleased(java.awt.event.MouseEvent evt) { int x = jButton20.getX(); int y = jButton20.getY(); jButton20.setLocation(x - 1, y - 1); } });
        
        //- Remove the button borders
        Border emptyBorder = BorderFactory.createEmptyBorder();
        jButton1.setBorder(emptyBorder);
        jButton2.setBorder(emptyBorder);
        jButton3.setBorder(emptyBorder);
        jButton4.setBorder(emptyBorder);
        jButton5.setBorder(emptyBorder);
        jButton6.setBorder(emptyBorder);
        jButton7.setBorder(emptyBorder);
        jButton8.setBorder(emptyBorder);
        jButton9.setBorder(emptyBorder);
        jButton10.setBorder(emptyBorder);
        jButton12.setBorder(emptyBorder);
        jButton13.setBorder(emptyBorder);
        jButton14.setBorder(emptyBorder);
        jButton15.setBorder(emptyBorder);
        jButton16.setBorder(emptyBorder);
        jButton17.setBorder(emptyBorder);
        jButton18.setBorder(emptyBorder);
        jButton19.setBorder(emptyBorder);
        jButton20.setBorder(emptyBorder);
       
        //--- Create directories if not exist
        //new File(pathLogging.toString()).mkdirs();
        
        //--- Set Zip Text
        String strPathDesktop = pathDesktop.toString();
        jTextFieldZipFilename.setText(strPathDesktop + "\\Backup_" + dateTime + ".zip");
        
        //--- Load Links button text
        jButtonLinkCustom01.setText(PropertyHandler.getInstance().getValue("LinkCustom01Text"));
        jButtonLinkCustom02.setText(PropertyHandler.getInstance().getValue("LinkCustom02Text"));
        jButtonLinkCustom03.setText(PropertyHandler.getInstance().getValue("LinkCustom03Text"));
        jButtonLinkCustom04.setText(PropertyHandler.getInstance().getValue("LinkCustom04Text"));
        jButtonLinkCustom05.setText(PropertyHandler.getInstance().getValue("LinkCustom05Text"));
        jButtonLinkCustom06.setText(PropertyHandler.getInstance().getValue("LinkCustom06Text"));        
        
        
        //--- Load Reference button text
        jButtonReferenceCustom01.setText(PropertyHandler.getInstance().getValue("Reference01Text"));
        jButtonReferenceCustom02.setText(PropertyHandler.getInstance().getValue("Reference02Text"));
        jButtonReferenceCustom03.setText(PropertyHandler.getInstance().getValue("Reference03Text"));
        jButtonReferenceCustom04.setText(PropertyHandler.getInstance().getValue("Reference04Text"));
        jButtonReferenceCustom05.setText(PropertyHandler.getInstance().getValue("Reference05Text"));
        jButtonReferenceCustom06.setText(PropertyHandler.getInstance().getValue("Reference06Text"));

        //--- Load Script button text
        jButtonScriptCustom01.setText(PropertyHandler.getInstance().getValue("ScriptCustom01Text"));
        jButtonScriptCustom02.setText(PropertyHandler.getInstance().getValue("ScriptCustom02Text"));
        jButtonScriptCustom03.setText(PropertyHandler.getInstance().getValue("ScriptCustom03Text"));
        jButtonScriptCustom01.setText(PropertyHandler.getInstance().getValue("ScriptCustom04Text"));
        jButtonScriptCustom02.setText(PropertyHandler.getInstance().getValue("ScriptCustom05Text"));
        jButtonScriptCustom03.setText(PropertyHandler.getInstance().getValue("ScriptCustom06Text"));

         //--- Make buttons and stuff look less crappy - this also allows background coloring
        String laf = UIManager.getSystemLookAndFeelClassName();
        UIManager.setLookAndFeel(laf);

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
        jPanel11 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jButtonJSDiff2 = new javax.swing.JButton();
        jButtonConfigBuilder1 = new javax.swing.JButton();
        jButtonSubnetCalculator = new javax.swing.JButton();
        jButton41 = new javax.swing.JButton();
        jLabel38 = new javax.swing.JLabel();
        jSeparator12 = new javax.swing.JSeparator();
        jSeparator13 = new javax.swing.JSeparator();
        jLabel39 = new javax.swing.JLabel();
        jButton24 = new javax.swing.JButton();
        jButton36 = new javax.swing.JButton();
        jButton37 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jButtonScriptSyncStandalones = new javax.swing.JButton();
        jButtonScriptBackupShares = new javax.swing.JButton();
        jButtonScriptSyncStandalones1 = new javax.swing.JButton();
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
        jButton28 = new javax.swing.JButton();

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
        jPanel1.add(jTextFieldConnectHostname);
        jTextFieldConnectHostname.setBounds(10, 10, 120, 23);

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

        jTextFieldConnectUsername.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N
        jTextFieldConnectUsername.setToolTipText("Username");
        jTextFieldConnectUsername.setNextFocusableComponent(jPasswordFieldConnectPassword);
        jTextFieldConnectUsername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldConnectUsernameKeyTyped(evt);
            }
        });
        jPanel1.add(jTextFieldConnectUsername);
        jTextFieldConnectUsername.setBounds(10, 50, 120, 23);

        jPasswordFieldConnectPassword.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N
        jPasswordFieldConnectPassword.setToolTipText("Password");
        jPasswordFieldConnectPassword.setNextFocusableComponent(jButtonExecuteFunction3);
        jPasswordFieldConnectPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jPasswordFieldConnectPasswordKeyTyped(evt);
            }
        });
        jPanel1.add(jPasswordFieldConnectPassword);
        jPasswordFieldConnectPassword.setBounds(10, 70, 120, 23);
        jPanel1.add(jSeparator3);
        jSeparator3.setBounds(10, 100, 190, 10);

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

        jTextFieldFilter.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N
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
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 461, Short.MAX_VALUE))
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
        jButtonLinkCustom01.setBounds(20, 10, 170, 30);

        jButtonLinkCustom02.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom02ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom02);
        jButtonLinkCustom02.setBounds(200, 10, 170, 30);

        jButtonLinkCustom03.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom03ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom03);
        jButtonLinkCustom03.setBounds(380, 10, 170, 30);

        jButtonLinkCustom04.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom04ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom04);
        jButtonLinkCustom04.setBounds(20, 50, 170, 30);

        jButtonLinkCustom05.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom05ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom05);
        jButtonLinkCustom05.setBounds(200, 50, 170, 30);

        jButtonLinkCustom06.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom06ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom06);
        jButtonLinkCustom06.setBounds(380, 50, 170, 30);

        jButtonLinkCustom07.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom07ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom07);
        jButtonLinkCustom07.setBounds(20, 90, 170, 30);

        jButtonLinkCustom08.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom08ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom08);
        jButtonLinkCustom08.setBounds(200, 90, 170, 30);

        jButtonLinkCustom09.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom09ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom09);
        jButtonLinkCustom09.setBounds(380, 90, 170, 30);

        jButtonLinkCustom11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom11ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom11);
        jButtonLinkCustom11.setBounds(200, 130, 170, 30);

        jButtonLinkCustom10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom10ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom10);
        jButtonLinkCustom10.setBounds(20, 130, 170, 30);

        jButtonLinkCustom12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom12ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom12);
        jButtonLinkCustom12.setBounds(380, 130, 170, 30);

        jButtonLinkCustom13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom13ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom13);
        jButtonLinkCustom13.setBounds(20, 170, 170, 30);

        jButtonLinkCustom14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom14ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom14);
        jButtonLinkCustom14.setBounds(200, 170, 170, 30);

        jButtonLinkCustom15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom15ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom15);
        jButtonLinkCustom15.setBounds(380, 170, 170, 30);

        jButtonLinkCustom16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom16ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom16);
        jButtonLinkCustom16.setBounds(20, 210, 170, 30);

        jButtonLinkCustom17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom17ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom17);
        jButtonLinkCustom17.setBounds(200, 210, 170, 30);

        jButtonLinkCustom18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom18ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom18);
        jButtonLinkCustom18.setBounds(380, 210, 170, 30);

        jButtonLinkCustom19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom19ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom19);
        jButtonLinkCustom19.setBounds(20, 250, 170, 30);

        jButtonLinkCustom20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom20ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom20);
        jButtonLinkCustom20.setBounds(200, 250, 170, 30);

        jButtonLinkCustom21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom21ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom21);
        jButtonLinkCustom21.setBounds(380, 250, 170, 30);

        jButtonLinkCustom22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom22ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom22);
        jButtonLinkCustom22.setBounds(20, 290, 170, 30);

        jButtonLinkCustom23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom23ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom23);
        jButtonLinkCustom23.setBounds(200, 290, 170, 30);

        jButtonLinkCustom24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom24ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom24);
        jButtonLinkCustom24.setBounds(380, 290, 170, 30);

        jButtonLinkCustom25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom25ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom25);
        jButtonLinkCustom25.setBounds(20, 330, 170, 30);

        jButtonLinkCustom26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom26ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom26);
        jButtonLinkCustom26.setBounds(200, 330, 170, 30);

        jButtonLinkCustom27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom27ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom27);
        jButtonLinkCustom27.setBounds(380, 330, 170, 30);

        jButtonLinkCustom28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom28ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom28);
        jButtonLinkCustom28.setBounds(20, 370, 170, 30);

        jButtonLinkCustom29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom29ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom29);
        jButtonLinkCustom29.setBounds(200, 370, 170, 30);

        jButtonLinkCustom30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom30ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom30);
        jButtonLinkCustom30.setBounds(380, 370, 170, 30);

        jButtonLinkCustom31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom31ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom31);
        jButtonLinkCustom31.setBounds(20, 410, 170, 30);

        jButtonLinkCustom32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom32ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom32);
        jButtonLinkCustom32.setBounds(200, 410, 170, 30);

        jButtonLinkCustom33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom33ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom33);
        jButtonLinkCustom33.setBounds(380, 410, 170, 30);

        jButtonLinkCustom34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom34ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom34);
        jButtonLinkCustom34.setBounds(20, 450, 170, 30);

        jButtonLinkCustom35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom35ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom35);
        jButtonLinkCustom35.setBounds(200, 450, 170, 30);

        jButtonLinkCustom36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLinkCustom36ActionPerformed(evt);
            }
        });
        jPanelAppsCustom.add(jButtonLinkCustom36);
        jButtonLinkCustom36.setBounds(380, 450, 170, 30);

        jTabbedMain.addTab("Links", jPanelAppsCustom);

        jPanelReference.setLayout(null);

        jToggleOnlineOfflineMode.setBackground(new java.awt.Color(0, 204, 51));
        jToggleOnlineOfflineMode.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jToggleOnlineOfflineMode.setText("Online");
        jToggleOnlineOfflineMode.setToolTipText("Alternate between using network and local files.");
        jToggleOnlineOfflineMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleOnlineOfflineModeActionPerformed(evt);
            }
        });
        jPanelReference.add(jToggleOnlineOfflineMode);
        jToggleOnlineOfflineMode.setBounds(200, 10, 170, 30);

        jButtonReferenceCustom01.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom01ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom01);
        jButtonReferenceCustom01.setBounds(20, 50, 170, 30);

        jButtonReferenceCustom02.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom02ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom02);
        jButtonReferenceCustom02.setBounds(200, 50, 170, 30);

        jButtonReferenceCustom03.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom03ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom03);
        jButtonReferenceCustom03.setBounds(380, 50, 170, 30);

        jButtonReferenceCustom04.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom04ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom04);
        jButtonReferenceCustom04.setBounds(20, 90, 170, 30);

        jButtonReferenceCustom05.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom05ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom05);
        jButtonReferenceCustom05.setBounds(200, 90, 170, 30);

        jButtonReferenceCustom06.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom06ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom06);
        jButtonReferenceCustom06.setBounds(380, 90, 170, 30);

        jButtonReferenceCustom07.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom07ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom07);
        jButtonReferenceCustom07.setBounds(20, 130, 170, 30);

        jButtonReferenceCustom08.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom08ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom08);
        jButtonReferenceCustom08.setBounds(200, 130, 170, 30);

        jButtonReferenceCustom09.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom09ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom09);
        jButtonReferenceCustom09.setBounds(380, 130, 170, 30);

        jButtonReferenceCustom10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom10ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom10);
        jButtonReferenceCustom10.setBounds(20, 170, 170, 30);

        jButtonReferenceCustom11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom11ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom11);
        jButtonReferenceCustom11.setBounds(200, 170, 170, 30);

        jButtonReferenceCustom12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom12ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom12);
        jButtonReferenceCustom12.setBounds(380, 170, 170, 30);

        jButtonReferenceCustom13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom13ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom13);
        jButtonReferenceCustom13.setBounds(20, 210, 170, 30);

        jButtonReferenceCustom14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom14ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom14);
        jButtonReferenceCustom14.setBounds(200, 210, 170, 30);

        jButtonReferenceCustom15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom15ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom15);
        jButtonReferenceCustom15.setBounds(380, 210, 170, 30);

        jButtonReferenceCustom16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom16ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom16);
        jButtonReferenceCustom16.setBounds(20, 250, 170, 30);

        jButtonReferenceCustom17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom17ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom17);
        jButtonReferenceCustom17.setBounds(200, 250, 170, 30);

        jButtonReferenceCustom18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom18ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom18);
        jButtonReferenceCustom18.setBounds(380, 250, 170, 30);

        jButtonReferenceCustom19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom19ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom19);
        jButtonReferenceCustom19.setBounds(20, 290, 170, 30);

        jButtonReferenceCustom20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom20ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom20);
        jButtonReferenceCustom20.setBounds(200, 290, 170, 30);

        jButtonReferenceCustom21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom21ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom21);
        jButtonReferenceCustom21.setBounds(380, 290, 170, 30);

        jButtonReferenceCustom22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom22ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom22);
        jButtonReferenceCustom22.setBounds(20, 330, 170, 30);

        jButtonReferenceCustom23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom23ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom23);
        jButtonReferenceCustom23.setBounds(200, 330, 170, 30);

        jButtonReferenceCustom24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom24ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom24);
        jButtonReferenceCustom24.setBounds(380, 330, 170, 30);

        jButtonReferenceCustom25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom25ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom25);
        jButtonReferenceCustom25.setBounds(20, 370, 170, 30);

        jButtonReferenceCustom26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom26ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom26);
        jButtonReferenceCustom26.setBounds(200, 370, 170, 30);

        jButtonReferenceCustom27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom27ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom27);
        jButtonReferenceCustom27.setBounds(380, 370, 170, 30);

        jButtonReferenceCustom28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom28ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom28);
        jButtonReferenceCustom28.setBounds(20, 410, 170, 30);

        jButtonReferenceCustom29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom29ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom29);
        jButtonReferenceCustom29.setBounds(200, 410, 170, 30);

        jButtonReferenceCustom30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom30ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom30);
        jButtonReferenceCustom30.setBounds(380, 410, 170, 30);

        jButtonReferenceCustom31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom31ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom31);
        jButtonReferenceCustom31.setBounds(20, 450, 170, 30);

        jButtonReferenceCustom32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom32ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom32);
        jButtonReferenceCustom32.setBounds(200, 450, 170, 30);

        jButtonReferenceCustom33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReferenceCustom33ActionPerformed(evt);
            }
        });
        jPanelReference.add(jButtonReferenceCustom33);
        jButtonReferenceCustom33.setBounds(380, 450, 170, 30);

        jTabbedMain.addTab("Reference", jPanelReference);

        jPanelScripts.setLayout(null);

        jButtonScriptCustom03.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanelScripts.add(jButtonScriptCustom03);
        jButtonScriptCustom03.setBounds(380, 10, 170, 30);

        jButtonScriptCustom01.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom01.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom01ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom01);
        jButtonScriptCustom01.setBounds(20, 10, 170, 30);

        jButtonScriptCustom02.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom02.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom02ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom02);
        jButtonScriptCustom02.setBounds(200, 10, 170, 30);

        jButtonScriptCustom06.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanelScripts.add(jButtonScriptCustom06);
        jButtonScriptCustom06.setBounds(380, 50, 170, 30);

        jButtonScriptCustom04.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom04.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom04ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom04);
        jButtonScriptCustom04.setBounds(20, 50, 170, 30);

        jButtonScriptCustom05.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom05.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom05ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom05);
        jButtonScriptCustom05.setBounds(200, 50, 170, 30);

        jButtonScriptCustom08.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom08.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom08ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom08);
        jButtonScriptCustom08.setBounds(200, 90, 170, 30);

        jButtonScriptCustom09.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanelScripts.add(jButtonScriptCustom09);
        jButtonScriptCustom09.setBounds(380, 90, 170, 30);

        jButtonScriptCustom07.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom07.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom07ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom07);
        jButtonScriptCustom07.setBounds(20, 90, 170, 30);

        jButtonScriptCustom12.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom12ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom12);
        jButtonScriptCustom12.setBounds(380, 130, 170, 30);

        jButtonScriptCustom15.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanelScripts.add(jButtonScriptCustom15);
        jButtonScriptCustom15.setBounds(380, 170, 170, 30);

        jButtonScriptCustom11.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom11ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom11);
        jButtonScriptCustom11.setBounds(200, 130, 170, 30);

        jButtonScriptCustom10.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom10ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom10);
        jButtonScriptCustom10.setBounds(20, 130, 170, 30);

        jButtonScriptCustom13.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom13ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom13);
        jButtonScriptCustom13.setBounds(20, 170, 170, 30);

        jButtonScriptCustom14.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom14ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom14);
        jButtonScriptCustom14.setBounds(200, 170, 170, 30);

        jButtonScriptCustom18.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanelScripts.add(jButtonScriptCustom18);
        jButtonScriptCustom18.setBounds(380, 210, 170, 30);

        jButtonScriptCustom16.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom16ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom16);
        jButtonScriptCustom16.setBounds(20, 210, 170, 30);

        jButtonScriptCustom17.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom17ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom17);
        jButtonScriptCustom17.setBounds(200, 210, 170, 30);

        jButtonScriptCustom20.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom20ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom20);
        jButtonScriptCustom20.setBounds(200, 250, 170, 30);

        jButtonScriptCustom21.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanelScripts.add(jButtonScriptCustom21);
        jButtonScriptCustom21.setBounds(380, 250, 170, 30);

        jButtonScriptCustom19.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom19ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom19);
        jButtonScriptCustom19.setBounds(20, 250, 170, 30);

        jButtonScriptCustom24.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanelScripts.add(jButtonScriptCustom24);
        jButtonScriptCustom24.setBounds(380, 290, 170, 30);

        jButtonScriptCustom22.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom22ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom22);
        jButtonScriptCustom22.setBounds(20, 290, 170, 30);

        jButtonScriptCustom23.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom23ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom23);
        jButtonScriptCustom23.setBounds(200, 290, 170, 30);

        jButtonScriptCustom26.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom26ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom26);
        jButtonScriptCustom26.setBounds(200, 330, 170, 30);

        jButtonScriptCustom27.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanelScripts.add(jButtonScriptCustom27);
        jButtonScriptCustom27.setBounds(380, 330, 170, 30);

        jButtonScriptCustom25.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom25ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom25);
        jButtonScriptCustom25.setBounds(20, 330, 170, 30);

        jButtonScriptCustom29.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom29ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom29);
        jButtonScriptCustom29.setBounds(200, 370, 170, 30);

        jButtonScriptCustom30.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanelScripts.add(jButtonScriptCustom30);
        jButtonScriptCustom30.setBounds(380, 370, 170, 30);

        jButtonScriptCustom28.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom28ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom28);
        jButtonScriptCustom28.setBounds(20, 370, 170, 30);

        jButtonScriptCustom32.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom32ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom32);
        jButtonScriptCustom32.setBounds(200, 410, 170, 30);

        jButtonScriptCustom33.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanelScripts.add(jButtonScriptCustom33);
        jButtonScriptCustom33.setBounds(380, 410, 170, 30);

        jButtonScriptCustom31.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom31ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom31);
        jButtonScriptCustom31.setBounds(20, 410, 170, 30);

        jButtonScriptCustom34.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom34ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom34);
        jButtonScriptCustom34.setBounds(20, 450, 170, 30);

        jButtonScriptCustom35.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonScriptCustom35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptCustom35ActionPerformed(evt);
            }
        });
        jPanelScripts.add(jButtonScriptCustom35);
        jButtonScriptCustom35.setBounds(200, 450, 170, 30);

        jButtonScriptCustom36.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanelScripts.add(jButtonScriptCustom36);
        jButtonScriptCustom36.setBounds(380, 450, 170, 30);

        jTabbedMain.addTab("Scripts", jPanelScripts);

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

        jPanel11.setLayout(null);

        jLabel31.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel31.setText("Web Apps");
        jPanel11.add(jLabel31);
        jLabel31.setBounds(110, 10, 350, 20);

        jButtonJSDiff2.setText("jsDiff (Compare Files)");
        jButtonJSDiff2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonJSDiff2ActionPerformed(evt);
            }
        });
        jPanel11.add(jButtonJSDiff2);
        jButtonJSDiff2.setBounds(200, 40, 170, 30);

        jButtonConfigBuilder1.setText("Config Builder (For Lab Use)");
        jButtonConfigBuilder1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConfigBuilder1ActionPerformed(evt);
            }
        });
        jPanel11.add(jButtonConfigBuilder1);
        jButtonConfigBuilder1.setBounds(380, 40, 170, 30);

        jButtonSubnetCalculator.setText("IPv4/v6 Subnet Calculator");
        jButtonSubnetCalculator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSubnetCalculatorActionPerformed(evt);
            }
        });
        jPanel11.add(jButtonSubnetCalculator);
        jButtonSubnetCalculator.setBounds(20, 40, 170, 30);

        jButton41.setText("Puppeteer");
        jButton41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton41ActionPerformed(evt);
            }
        });
        jPanel11.add(jButton41);
        jButton41.setBounds(200, 130, 170, 30);

        jLabel38.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel38.setText("Java Apps");
        jPanel11.add(jLabel38);
        jLabel38.setBounds(110, 100, 350, 20);
        jPanel11.add(jSeparator12);
        jSeparator12.setBounds(10, 90, 550, 10);
        jPanel11.add(jSeparator13);
        jSeparator13.setBounds(10, 180, 550, 10);

        jLabel39.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel39.setText("Documents");
        jPanel11.add(jLabel39);
        jLabel39.setBounds(110, 190, 350, 20);

        jButton24.setText("IPv4 Subnet Chart");
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });
        jPanel11.add(jButton24);
        jButton24.setBounds(20, 220, 170, 30);

        jButton36.setText("IPv4 Subnet Cheat Sheet");
        jButton36.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton36ActionPerformed(evt);
            }
        });
        jPanel11.add(jButton36);
        jButton36.setBounds(200, 220, 170, 30);

        jButton37.setText("Stretch's Cheat Sheets");
        jButton37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton37ActionPerformed(evt);
            }
        });
        jPanel11.add(jButton37);
        jButton37.setBounds(380, 220, 170, 30);

        jTabbedPaneToolBox.addTab("Web/Java/Docs", jPanel11);

        jPanel4.setLayout(null);

        jButtonScriptSyncStandalones.setText("Sync Standalones");
        jButtonScriptSyncStandalones.setEnabled(false);
        jButtonScriptSyncStandalones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptSyncStandalonesActionPerformed(evt);
            }
        });
        jPanel4.add(jButtonScriptSyncStandalones);
        jButtonScriptSyncStandalones.setBounds(200, 20, 170, 30);

        jButtonScriptBackupShares.setText("Backup Shares");
        jButtonScriptBackupShares.setEnabled(false);
        jButtonScriptBackupShares.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptBackupSharesActionPerformed(evt);
            }
        });
        jPanel4.add(jButtonScriptBackupShares);
        jButtonScriptBackupShares.setBounds(20, 20, 170, 30);

        jButtonScriptSyncStandalones1.setText("Map Standalone Share");
        jButtonScriptSyncStandalones1.setEnabled(false);
        jButtonScriptSyncStandalones1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptSyncStandalones1ActionPerformed(evt);
            }
        });
        jPanel4.add(jButtonScriptSyncStandalones1);
        jButtonScriptSyncStandalones1.setBounds(380, 20, 170, 30);

        jTabbedPaneToolBox.addTab("Scripts", jPanel4);

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

        jButton28.setText("Open \".launchpad\" Folder");
        jButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton28ActionPerformed(evt);
            }
        });
        jPanelSettings.add(jButton28);
        jButton28.setBounds(10, 230, 190, 20);

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
//                    System.out.println("HEY Buddy ! U r Doing Something Wrong ");
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
//                System.out.println("HEY Buddy ! U r Doing Something Wrong ");
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
        openTempFileUsingDesktop("files/Subnets.pdf", ".pdf");
    }//GEN-LAST:event_jButton24ActionPerformed

    private void jButtonReferenceCustom03ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom03ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom03ActionPerformed

    private void jButtonReferenceCustom02ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom02ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom02ActionPerformed

    private void jButtonReferenceCustom01ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom01ActionPerformed
        // TODO add your handling code here:
        
        openReferenceUsingDesktop("ReferencePath01");
//        String strReference01;
//        strReference01 = PropertyHandler.getInstance().getValue("ReferencePath01");
//
//        if(jToggleOfflineMode.isSelected()){
//            strReference01 =  PropertyHandler.getInstance().getValue("ReferenceFolderOffline") + strReference01;
//            System.out.println("Using Offline: " + strReference01);
//        }
//        else{
//            strReference01 =  PropertyHandler.getInstance().getValue("ReferenceFolderOnline") + strReference01;
//        }
//
//        //text file, should be opening in default text editor
//        File file = new File(strReference01);
//
//        //first check if Desktop is supported by Platform or not
//        if(!Desktop.isDesktopSupported()){
//            System.out.println("Desktop is not supported");
//            return;
//        }
//
//        Desktop desktop = Desktop.getDesktop();
//        if(file.exists()) try {
//            desktop.open(file);
//        } catch (IOException ex) {
//            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
//            System.out.println("launchpad.LaunchPadForm.jButtonReferenceCustom01ActionPerformed()");
//        }
//
//        // Open
//        if(file.exists()) try {
//            desktop.open(file);
//        } catch (IOException ex) {
//            Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
//            System.out.println("launchpad.LaunchPadForm.jButtonReferenceCustom01ActionPerformed()");
//
//        }
    }//GEN-LAST:event_jButtonReferenceCustom01ActionPerformed

    private void jToggleOnlineOfflineModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleOnlineOfflineModeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jToggleOnlineOfflineModeActionPerformed

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

    private void jButtonScriptCustom28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom28ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonScriptCustom28ActionPerformed

    private void jButtonScriptCustom29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom29ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonScriptCustom29ActionPerformed

    private void jButtonScriptCustom25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom25ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonScriptCustom25ActionPerformed

    private void jButtonScriptCustom26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom26ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonScriptCustom26ActionPerformed

    private void jButtonScriptCustom23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom23ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonScriptCustom23ActionPerformed

    private void jButtonScriptCustom22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom22ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonScriptCustom22ActionPerformed

    private void jButtonScriptCustom19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom19ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonScriptCustom19ActionPerformed

    private void jButtonScriptCustom20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom20ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonScriptCustom20ActionPerformed

    private void jButtonScriptCustom17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom17ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonScriptCustom17ActionPerformed

    private void jButtonScriptCustom16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom16ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonScriptCustom16ActionPerformed

    private void jButtonScriptCustom14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonScriptCustom14ActionPerformed

    private void jButtonScriptCustom13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom13ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonScriptCustom13ActionPerformed

    private void jButtonScriptCustom10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonScriptCustom10ActionPerformed

    private void jButtonScriptCustom11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonScriptCustom11ActionPerformed

    private void jButtonScriptCustom12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonScriptCustom12ActionPerformed

    private void jButtonScriptCustom07ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom07ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonScriptCustom07ActionPerformed

    private void jButtonScriptCustom08ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom08ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonScriptCustom08ActionPerformed

    private void jButtonScriptCustom05ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom05ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonScriptCustom05ActionPerformed

    private void jButtonScriptCustom04ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom04ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonScriptCustom04ActionPerformed

    private void jButtonScriptCustom02ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom02ActionPerformed
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
    }//GEN-LAST:event_jButtonScriptCustom02ActionPerformed

    private void jButtonScriptCustom01ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom01ActionPerformed
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
    }//GEN-LAST:event_jButtonScriptCustom01ActionPerformed

    private void jButtonLinkCustom30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom30ActionPerformed
        // TODO add your handling code here:
        linkCustom("LinkCustom30");
    }//GEN-LAST:event_jButtonLinkCustom30ActionPerformed

    private void jButtonLinkCustom29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom29ActionPerformed
        // TODO add your handling code here:
        linkCustom("LinkCustom29");
    }//GEN-LAST:event_jButtonLinkCustom29ActionPerformed

    private void jButtonLinkCustom28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom28ActionPerformed
        // TODO add your handling code here:
        linkCustom("LinkCustom28");
    }//GEN-LAST:event_jButtonLinkCustom28ActionPerformed

    private void jButtonLinkCustom27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom27ActionPerformed
        // TODO add your handling code here:
        linkCustom("LinkCustom27");
    }//GEN-LAST:event_jButtonLinkCustom27ActionPerformed

    private void jButtonLinkCustom26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom26ActionPerformed
        // TODO add your handling code here:
        linkCustom("LinkCustom26");
    }//GEN-LAST:event_jButtonLinkCustom26ActionPerformed

    private void jButtonLinkCustom25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom25ActionPerformed
        // TODO add your handling code here:
        linkCustom("LinkCustom25");
    }//GEN-LAST:event_jButtonLinkCustom25ActionPerformed

    private void jButtonLinkCustom24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom24ActionPerformed
        // TODO add your handling code here:
        linkCustom("LinkCustom24");
    }//GEN-LAST:event_jButtonLinkCustom24ActionPerformed

    private void jButtonLinkCustom23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom23ActionPerformed
        // TODO add your handling code here:
        linkCustom("LinkCustom23");
    }//GEN-LAST:event_jButtonLinkCustom23ActionPerformed

    private void jButtonLinkCustom22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom22ActionPerformed
        // TODO add your handling code here:
        linkCustom("LinkCustom22");
    }//GEN-LAST:event_jButtonLinkCustom22ActionPerformed

    private void jButtonLinkCustom21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom21ActionPerformed
        // TODO add your handling code here:
        linkCustom("LinkCustom21");
    }//GEN-LAST:event_jButtonLinkCustom21ActionPerformed

    private void jButtonLinkCustom20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom20ActionPerformed
        // TODO add your handling code here:
        linkCustom("LinkCustom20");
    }//GEN-LAST:event_jButtonLinkCustom20ActionPerformed

    private void jButtonLinkCustom19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom19ActionPerformed
        // TODO add your handling code here:
        linkCustom("LinkCustom19");
    }//GEN-LAST:event_jButtonLinkCustom19ActionPerformed

    private void jButtonLinkCustom18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom18ActionPerformed
        // TODO add your handling code here:
        linkCustom("LinkCustom18");
    }//GEN-LAST:event_jButtonLinkCustom18ActionPerformed

    private void jButtonLinkCustom17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom17ActionPerformed
        // TODO add your handling code here:
        linkCustom("LinkCustom17");
    }//GEN-LAST:event_jButtonLinkCustom17ActionPerformed

    private void jButtonLinkCustom16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom16ActionPerformed
        // TODO add your handling code here:
        linkCustom("LinkCustom16");
    }//GEN-LAST:event_jButtonLinkCustom16ActionPerformed

    private void jButtonLinkCustom15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom15ActionPerformed
        // TODO add your handling code here:
        linkCustom("LinkCustom15");
    }//GEN-LAST:event_jButtonLinkCustom15ActionPerformed

    private void jButtonLinkCustom14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom14ActionPerformed
        // TODO add your handling code here:
        linkCustom("LinkCustom14");
    }//GEN-LAST:event_jButtonLinkCustom14ActionPerformed

    private void jButtonLinkCustom13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom13ActionPerformed
        // TODO add your handling code here:
        linkCustom("LinkCustom13");
    }//GEN-LAST:event_jButtonLinkCustom13ActionPerformed

    private void jButtonLinkCustom12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom12ActionPerformed
        // TODO add your handling code here:
        linkCustom("LinkCustom12");
    }//GEN-LAST:event_jButtonLinkCustom12ActionPerformed

    private void jButtonLinkCustom10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom10ActionPerformed
        // TODO add your handling code here:
        linkCustom("LinkCustom10");
    }//GEN-LAST:event_jButtonLinkCustom10ActionPerformed

    private void jButtonLinkCustom11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom11ActionPerformed
        // TODO add your handling code here:
        linkCustom("LinkCustom11");
    }//GEN-LAST:event_jButtonLinkCustom11ActionPerformed

    private void jButtonLinkCustom09ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom09ActionPerformed
        // TODO add your handling code here:
        linkCustom("LinkCustom09");  
    }//GEN-LAST:event_jButtonLinkCustom09ActionPerformed

    private void jButtonLinkCustom08ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom08ActionPerformed
        // TODO add your handling code here:
        linkCustom("LinkCustom08");
    }//GEN-LAST:event_jButtonLinkCustom08ActionPerformed

    private void jButtonLinkCustom07ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom07ActionPerformed
        // TODO add your handling code here:
        linkCustom("LinkCustom07");
    }//GEN-LAST:event_jButtonLinkCustom07ActionPerformed

    private void jButtonLinkCustom06ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom06ActionPerformed
        // TODO add your handling code here:
        linkCustom("LinkCustom06");
    }//GEN-LAST:event_jButtonLinkCustom06ActionPerformed

    private void jButtonLinkCustom05ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom05ActionPerformed
        // TODO add your handling code here:
        linkCustom("LinkCustom05");
    }//GEN-LAST:event_jButtonLinkCustom05ActionPerformed

    private void jButtonLinkCustom04ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom04ActionPerformed
        // TODO add your handling code here:
        linkCustom("LinkCustom04");
    }//GEN-LAST:event_jButtonLinkCustom04ActionPerformed

    private void jButtonLinkCustom03ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom03ActionPerformed
        // TODO add your handling code here:
        linkCustom("LinkCustom03");
    }//GEN-LAST:event_jButtonLinkCustom03ActionPerformed

    private void jButtonLinkCustom02ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom02ActionPerformed
        // TODO add your handling code here:
        linkCustom("LinkCustom02");
    }//GEN-LAST:event_jButtonLinkCustom02ActionPerformed

    private void jButtonLinkCustom01ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom01ActionPerformed
        // TODO add your handling code here:
        linkCustom("LinkCustom01");
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

    private void jButtonJSDiff2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonJSDiff2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonJSDiff2ActionPerformed

    private void jButtonConfigBuilder1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConfigBuilder1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonConfigBuilder1ActionPerformed

    private void jButtonSubnetCalculatorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSubnetCalculatorActionPerformed
        // TODO add your handling code here:
                openTempFileUsingDesktop("html/subnetcalculator/subnetcalculator.html", ".html");
    }//GEN-LAST:event_jButtonSubnetCalculatorActionPerformed

    private void jButton41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton41ActionPerformed
        // TODO add your handling code here:
        openTempFileUsingDesktop("apps/Puppeteer.jar", ".jar");
    }//GEN-LAST:event_jButton41ActionPerformed

    private void jButtonLinkCustom31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom31ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonLinkCustom31ActionPerformed

    private void jButtonLinkCustom32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom32ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonLinkCustom32ActionPerformed

    private void jButtonLinkCustom33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom33ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonLinkCustom33ActionPerformed

    private void jButtonLinkCustom34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom34ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonLinkCustom34ActionPerformed

    private void jButtonLinkCustom35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom35ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonLinkCustom35ActionPerformed

    private void jButtonLinkCustom36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLinkCustom36ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonLinkCustom36ActionPerformed

    private void jButtonScriptCustom32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom32ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonScriptCustom32ActionPerformed

    private void jButtonScriptCustom31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom31ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonScriptCustom31ActionPerformed

    private void jButtonScriptCustom34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom34ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonScriptCustom34ActionPerformed

    private void jButtonScriptCustom35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptCustom35ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonScriptCustom35ActionPerformed

    private void jButtonReferenceCustom04ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom04ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom04ActionPerformed

    private void jButtonReferenceCustom05ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom05ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom05ActionPerformed

    private void jButtonReferenceCustom06ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom06ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom06ActionPerformed

    private void jButtonReferenceCustom07ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom07ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom07ActionPerformed

    private void jButtonReferenceCustom08ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom08ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom08ActionPerformed

    private void jButtonReferenceCustom09ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom09ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom09ActionPerformed

    private void jButtonReferenceCustom10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom10ActionPerformed

    private void jButtonReferenceCustom11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom11ActionPerformed

    private void jButtonReferenceCustom12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom12ActionPerformed

    private void jButtonReferenceCustom13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom13ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom13ActionPerformed

    private void jButtonReferenceCustom14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom14ActionPerformed

    private void jButtonReferenceCustom15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom15ActionPerformed

    private void jButtonReferenceCustom16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom16ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom16ActionPerformed

    private void jButtonReferenceCustom17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom17ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom17ActionPerformed

    private void jButtonReferenceCustom18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom18ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom18ActionPerformed

    private void jButtonReferenceCustom19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom19ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom19ActionPerformed

    private void jButtonReferenceCustom20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom20ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom20ActionPerformed

    private void jButtonReferenceCustom21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom21ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom21ActionPerformed

    private void jButtonReferenceCustom22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom22ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom22ActionPerformed

    private void jButtonReferenceCustom23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom23ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom23ActionPerformed

    private void jButtonReferenceCustom24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom24ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom24ActionPerformed

    private void jButtonReferenceCustom25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom25ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom25ActionPerformed

    private void jButtonReferenceCustom26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom26ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom26ActionPerformed

    private void jButtonReferenceCustom27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom27ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom27ActionPerformed

    private void jButtonReferenceCustom28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom28ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom28ActionPerformed

    private void jButtonReferenceCustom29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom29ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom29ActionPerformed

    private void jButtonReferenceCustom30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom30ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom30ActionPerformed

    private void jButtonReferenceCustom31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom31ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom31ActionPerformed

    private void jButtonReferenceCustom32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom32ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom32ActionPerformed

    private void jButtonReferenceCustom33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReferenceCustom33ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonReferenceCustom33ActionPerformed

    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed
        // TODO add your handling code here:
        openFileUsingDesktop(strPathLaunchPadFolder);
    }//GEN-LAST:event_jButton28ActionPerformed

    
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
    
  

    public void linkCustom(String strLinkText) {
        String myValue = PropertyHandler.getInstance().getValue(strLinkText).replace("%USERPROFILE%", pathUserProfile);
        System.out.println(myValue);
        try {
            String[] args = new String[] {"cmd.exe", "/c", myValue};
            Process proc = new ProcessBuilder(args).start();
        }
        catch (IOException e) {
            System.out.println("Something is Wrong!");
            JOptionPane.showMessageDialog(null, "Something is wrong!");
        }
    }
    
    public void openFileUsingDesktop(String strFullFilePath) {
    
        File userManual = new File (strFullFilePath);
        if (userManual.exists())
        {
            try {
                Desktop.getDesktop().open(userManual);
            } catch (IOException ex) {
                Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
            }
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
    

    public void openReferenceUsingDesktop(String strReferenceToOpen) {
    
        String strReference;
        //strReference = PropertyHandler.getInstance().getValue(strReferenceToOpen);

        if(jToggleOnlineOfflineMode.isSelected()){
            strReference =  PropertyHandler.getInstance().getValue(strReferenceToOpen + "Offline");
            System.out.println("Using Offline: " + strReference);
        }
        else{
            strReference =  PropertyHandler.getInstance().getValue(strReferenceToOpen + "Online");
            System.out.println("Using Online: " + strReference);        
        }

        //text file, should be opening in default text editor
        File file = new File(strReference);

        //first check if Desktop is supported by Platform or not
        if(!Desktop.isDesktopSupported()){
            System.out.println("Desktop is not supported");
            return;
        }

        Desktop desktop = Desktop.getDesktop();


        if(isValidURL(strReferenceToOpen))

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
    
    }

    static public boolean isValidURL(String urlStr) {
        try {
          URI uri = new URI(urlStr);
          return uri.getScheme().equals("http") || uri.getScheme().equals("https");
        }
        catch (Exception e) {
            return false;
        }
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
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton36;
    private javax.swing.JButton jButton37;
    private javax.swing.JButton jButton39;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton41;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JButton jButtonConfigBuilder1;
    private javax.swing.JButton jButtonConsole;
    private javax.swing.JButton jButtonExecuteFunction1;
    private javax.swing.JButton jButtonExecuteFunction2;
    private javax.swing.JButton jButtonExecuteFunction3;
    private javax.swing.JButton jButtonFolderToZip;
    private javax.swing.JButton jButtonGenerateHash;
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
    private javax.swing.JButton jButtonPing;
    private javax.swing.JButton jButtonReferenceCustom01;
    private javax.swing.JButton jButtonReferenceCustom02;
    private javax.swing.JButton jButtonReferenceCustom03;
    private javax.swing.JButton jButtonReferenceCustom04;
    private javax.swing.JButton jButtonReferenceCustom05;
    private javax.swing.JButton jButtonReferenceCustom06;
    private javax.swing.JButton jButtonReferenceCustom07;
    private javax.swing.JButton jButtonReferenceCustom08;
    private javax.swing.JButton jButtonReferenceCustom09;
    private javax.swing.JButton jButtonReferenceCustom10;
    private javax.swing.JButton jButtonReferenceCustom11;
    private javax.swing.JButton jButtonReferenceCustom12;
    private javax.swing.JButton jButtonReferenceCustom13;
    private javax.swing.JButton jButtonReferenceCustom14;
    private javax.swing.JButton jButtonReferenceCustom15;
    private javax.swing.JButton jButtonReferenceCustom16;
    private javax.swing.JButton jButtonReferenceCustom17;
    private javax.swing.JButton jButtonReferenceCustom18;
    private javax.swing.JButton jButtonReferenceCustom19;
    private javax.swing.JButton jButtonReferenceCustom20;
    private javax.swing.JButton jButtonReferenceCustom21;
    private javax.swing.JButton jButtonReferenceCustom22;
    private javax.swing.JButton jButtonReferenceCustom23;
    private javax.swing.JButton jButtonReferenceCustom24;
    private javax.swing.JButton jButtonReferenceCustom25;
    private javax.swing.JButton jButtonReferenceCustom26;
    private javax.swing.JButton jButtonReferenceCustom27;
    private javax.swing.JButton jButtonReferenceCustom28;
    private javax.swing.JButton jButtonReferenceCustom29;
    private javax.swing.JButton jButtonReferenceCustom30;
    private javax.swing.JButton jButtonReferenceCustom31;
    private javax.swing.JButton jButtonReferenceCustom32;
    private javax.swing.JButton jButtonReferenceCustom33;
    private javax.swing.JButton jButtonReportIssue;
    private javax.swing.JButton jButtonScriptBackupShares;
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
    private javax.swing.JButton jButtonScriptSyncStandalones;
    private javax.swing.JButton jButtonScriptSyncStandalones1;
    private javax.swing.JButton jButtonScriptUpdateLaunchPad;
    private javax.swing.JButton jButtonShowCOMList;
    private javax.swing.JButton jButtonSubnetCalculator;
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
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
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
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelAppsCustom;
    private javax.swing.JPanel jPanelMain;
    private javax.swing.JPanel jPanelReference;
    private javax.swing.JPanel jPanelScripts;
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
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JSlider jSliderListTextSize;
    private javax.swing.JTabbedPane jTabbedMain;
    private javax.swing.JTabbedPane jTabbedPaneToolBox;
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
    private javax.swing.JToggleButton jToggleOnlineOfflineMode;
    // End of variables declaration//GEN-END:variables




}