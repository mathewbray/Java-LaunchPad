/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package launchpad;

import com.mnnit.server.model.SingletonUIResource;
import com.mnnit.server.ui.MainFrame;
import static com.mnnit.server.ui.MainFrame.getLookAndFeel;
import com.mnnit.server.ui.PopUpMenu;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import launchpad.Type7Reverse.CiscoVigenere;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.progress.ProgressMonitor;
import net.lingala.zip4j.util.Zip4jConstants;
import org.snmp4j.PDU;
import org.snmp4j.ScopedPDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.UserTarget;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.AuthMD5;
import org.snmp4j.security.PrivDES;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUser;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

/**
 *
 * @author Mathew
 */
public class LaunchPadForm extends javax.swing.JFrame {

    DefaultListModel defaultListModel=new DefaultListModel();
    File pathWorkingDirectory = new File(System.getProperty("user.dir"));
    File pathDesktop = new File(System.getProperty("user.home"), "Desktop");
    File pathLogging = new File(pathDesktop + "\\Logging-Output");
    
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
     */
    public LaunchPadForm() throws IOException, FileNotFoundException, URISyntaxException, AWTException, InterruptedException {
        initComponents();
        //importSessionList();
        getSessionList();
        //updateSessionList();
        //--- Apply button icons and set size
        Integer buttonHeightWidth = 40;
        ImageIcon icon;
        Image img;
        Image newimg;
        //Button1
        icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button1icon") + ".png"));
        img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButton1.setIcon(new ImageIcon(newimg));
        //Button2
        icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button2icon") + ".png"));
        img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButton2.setIcon(new ImageIcon(newimg));
        //Button3
        icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button3icon") + ".png"));
        img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButton3.setIcon(new ImageIcon(newimg));
        //Button4
        icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button4icon") + ".png"));
        img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButton4.setIcon(new ImageIcon(newimg));
        //Button5
        icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button5icon") + ".png"));
        img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButton5.setIcon(new ImageIcon(newimg));
        //Button6
        icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button6icon") + ".png"));
        img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButton6.setIcon(new ImageIcon(newimg));
        //Button7
        icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button7icon") + ".png"));
        img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButton7.setIcon(new ImageIcon(newimg));
        //Button8
        icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button8icon") + ".png"));
        img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButton8.setIcon(new ImageIcon(newimg));
        //Button9
        icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button9icon") + ".png"));
        img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButton9.setIcon(new ImageIcon(newimg));
        //Button10
        icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button10icon") + ".png"));
        img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButton10.setIcon(new ImageIcon(newimg));
        //Button11
        icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button11icon") + ".png"));
        img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButton11.setIcon(new ImageIcon(newimg));
        //Button12
        icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button12icon") + ".png"));
        img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButton12.setIcon(new ImageIcon(newimg));
        //Button13
        icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button13icon") + ".png"));
        img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButton13.setIcon(new ImageIcon(newimg));
        //Button14
        icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button14icon") + ".png"));
        img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButton14.setIcon(new ImageIcon(newimg));
        //Button15
        icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button15icon") + ".png"));
        img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButton15.setIcon(new ImageIcon(newimg));
        //Button16
        icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button16icon") + ".png"));
        img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButton16.setIcon(new ImageIcon(newimg));
        //Button17
        icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button17icon") + ".png"));
        img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButton17.setIcon(new ImageIcon(newimg));
        //Button18
        icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button18icon") + ".png"));
        img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButton18.setIcon(new ImageIcon(newimg));
        //Button19
        icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button19icon") + ".png"));
        img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButton19.setIcon(new ImageIcon(newimg));
        //Button20
        icon = new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button20icon") + ".png"));
        img = icon.getImage(); newimg = img.getScaledInstance( buttonHeightWidth, buttonHeightWidth,  java.awt.Image.SCALE_SMOOTH ); jButton20.setIcon(new ImageIcon(newimg));
        
        
//        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button1icon") + ".png")));
//        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button2icon") + ".png")));
//        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button3icon") + ".png")));
//        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button4icon") + ".png")));
//        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button5icon") + ".png")));
//        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button6icon") + ".png")));
//        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button7icon") + ".png")));
//        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button8icon") + ".png")));
//        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button9icon") + ".png")));
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

        //--- Load tooltips
        jButton1.setToolTipText(PropertyHandler.getInstance().getValue("Button1ToolTip"));
        jButton2.setToolTipText(PropertyHandler.getInstance().getValue("Button2ToolTip"));
        jButton3.setToolTipText(PropertyHandler.getInstance().getValue("Button3ToolTip"));
        jButton4.setToolTipText(PropertyHandler.getInstance().getValue("Button4ToolTip"));
        jButton5.setToolTipText(PropertyHandler.getInstance().getValue("Button5ToolTip"));
        jButton6.setToolTipText(PropertyHandler.getInstance().getValue("Button6ToolTip"));
        jButton7.setToolTipText(PropertyHandler.getInstance().getValue("Button7ToolTip"));
        jButton8.setToolTipText(PropertyHandler.getInstance().getValue("Button8ToolTip"));
        jButton9.setToolTipText(PropertyHandler.getInstance().getValue("Button9ToolTip"));
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
 
        
        
        
        try {
            UIManager.LookAndFeelInfo lookAndFeel = getLookAndFeel( "Windows" );

            if ( lookAndFeel != null )
                UIManager.setLookAndFeel( lookAndFeel.getClassName() );
            
            Runtime.getRuntime().addShutdownHook( new Thread( "ControllerShutdownHook" )
                    {
                            @Override
                            public void run()
                            {
                                    logOff(  );
                                                            }
                    } );
            resource = new SingletonUIResource(mainChatArea, jTextField1, userList);
            
            resource.getNetworkController().logOn();
                   ListSelectionListener listSelectionListener = new ListSelectionListener() {

                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        /** Get the particular element selected and act on it accordingly 
                         *  This method is still under Beta Phase and does not have any
                         *  reliability associated with it .This is sparta !!
                         */
                        boolean adjust = e.getValueIsAdjusting();
                        if(!adjust)
                        {
                            JList list = (JList) e.getSource() ;
                            int selections[] = list.getSelectedIndices() ;
                            Object selectionvalues[] = list.getSelectedValues() ;
                            for(int i = 0 ; i< selections.length ; i++)
                            {
                                userSelected = selectionvalues[i].toString();
                                System.out.println(userSelected);
                            }
                        }
                    }
                };
                userList.addListSelectionListener(listSelectionListener);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
 

        
    }
    
        public void populateUserList(DefaultListModel listModel)
    {
        if(listModel==null)
               throw new NullPointerException("the jlist is null");
        else 
            userList.setModel(listModel);
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
        jTabbedTools2 = new javax.swing.JTabbedPane();
        jPanelMain = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListSessions = new javax.swing.JList<>();
        jPanel1 = new javax.swing.JPanel();
        jTextFieldConnectHostname = new javax.swing.JTextField();
        jButtonHTTPS = new javax.swing.JButton();
        jButtonSSH = new javax.swing.JButton();
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
        jTextFieldFilter = new javax.swing.JTextField();
        jPanelTools1 = new javax.swing.JPanel();
        oct3 = new javax.swing.JTextField();
        subnetMask = new javax.swing.JTextField();
        oct4 = new javax.swing.JTextField();
        networkAddress = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        broadcastAddress = new javax.swing.JTextField();
        cidrValue = new javax.swing.JTextField();
        noSubnets = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        hostsPSubnet = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        calBtn = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        resetBtn = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        oct1 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        oct2 = new javax.swing.JTextField();
        networkClass = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel13 = new javax.swing.JLabel();
        jTextFieldType7Input = new javax.swing.JTextField();
        jButton21 = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();
        jTextFieldType7Output = new javax.swing.JTextField();
        jButton23 = new javax.swing.JButton();
        jButtonJSDiff = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JSeparator();
        jLabel14 = new javax.swing.JLabel();
        jButtonConfigBuilder = new javax.swing.JButton();
        jButton24 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jButtonFolderToZip = new javax.swing.JButton();
        jTextFieldZipSourceFolder = new javax.swing.JTextField();
        jTextFieldZipFilename = new javax.swing.JTextField();
        jButton25 = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jPasswordFieldZip = new javax.swing.JPasswordField();
        jLabel18 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jProgressBarZip = new javax.swing.JProgressBar();
        jLabelFolderToZip2 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        userList = new javax.swing.JList();
        jScrollPane3 = new javax.swing.JScrollPane();
        mainChatArea = new javax.swing.JTextArea();
        jTextField1 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
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
        jButtonUpdateLaunchPad = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("NSB LaunchPad - Pre-Alpha");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setSize(new java.awt.Dimension(550, 600));

        jPanelMain.setPreferredSize(new java.awt.Dimension(500, 503));

        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.LINE_AXIS));

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

        jPanel1.setLayout(null);

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

        jButtonHTTPS.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButtonHTTPS.setText("HTTPS");
        jButtonHTTPS.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonHTTPS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonHTTPSActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonHTTPS);
        jButtonHTTPS.setBounds(140, 10, 60, 30);

        jButtonSSH.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jButtonSSH.setText("SSH");
        jButtonSSH.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonSSH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSSHActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonSSH);
        jButtonSSH.setBounds(140, 40, 60, 40);

        jTextFieldConnectUsername.setToolTipText("Username");
        jTextFieldConnectUsername.setNextFocusableComponent(jPasswordFieldConnectPassword);
        jTextFieldConnectUsername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldConnectUsernameKeyTyped(evt);
            }
        });
        jPanel1.add(jTextFieldConnectUsername);
        jTextFieldConnectUsername.setBounds(10, 40, 120, 20);

        jPasswordFieldConnectPassword.setToolTipText("Password");
        jPasswordFieldConnectPassword.setNextFocusableComponent(jButtonSSH);
        jPasswordFieldConnectPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jPasswordFieldConnectPasswordKeyTyped(evt);
            }
        });
        jPanel1.add(jPasswordFieldConnectPassword);
        jPasswordFieldConnectPassword.setBounds(10, 60, 120, 20);
        jPanel1.add(jSeparator3);
        jSeparator3.setBounds(10, 90, 190, 10);

        jTextFieldPingHostname.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
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
        jTextFieldPingHostname.setBounds(10, 100, 120, 20);

        jCheckBoxDNS.setSelected(true);
        jCheckBoxDNS.setText("DNS");
        jPanel1.add(jCheckBoxDNS);
        jCheckBoxDNS.setBounds(140, 100, 50, 20);

        jButtonTracert.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButtonTracert.setText("TRACERT");
        jButtonTracert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTracertActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonTracert);
        jButtonTracert.setBounds(100, 120, 100, 20);

        jButtonPing.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButtonPing.setText("PING");
        jButtonPing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPingActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonPing);
        jButtonPing.setBounds(10, 120, 80, 20);
        jPanel1.add(jSeparator5);
        jSeparator5.setBounds(10, 150, 190, 10);

        jComboBoxConsoleCOM.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "COM10", "COM11", "COM12", "COM13", "COM14", "COM15", "COM16", "COM17", "COM18", "COM19", "COM20" }));
        jComboBoxConsoleCOM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxConsoleCOMActionPerformed(evt);
            }
        });
        jPanel1.add(jComboBoxConsoleCOM);
        jComboBoxConsoleCOM.setBounds(10, 160, 70, 20);

        jButtonShowCOMList.setText("?");
        jButtonShowCOMList.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonShowCOMList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonShowCOMListActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonShowCOMList);
        jButtonShowCOMList.setBounds(90, 160, 30, 20);

        jComboBoxConsoleBaud.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "BAUD", "9600", "115200" }));
        jPanel1.add(jComboBoxConsoleBaud);
        jComboBoxConsoleBaud.setBounds(130, 160, 70, 20);

        jButtonConsole.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButtonConsole.setText("CONSOLE");
        jButtonConsole.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonConsole.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConsoleActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonConsole);
        jButtonConsole.setBounds(60, 180, 90, 30);
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

        jTextFieldFilter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldFilterKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanelMainLayout = new javax.swing.GroupLayout(jPanelMain);
        jPanelMain.setLayout(jPanelMainLayout);
        jPanelMainLayout.setHorizontalGroup(
            jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMainLayout.createSequentialGroup()
                .addGroup(jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldFilter)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE))
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
                            .addComponent(jTextFieldFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );

        jTabbedTools2.addTab("Main", jPanelMain);

        jPanelTools1.setLayout(null);

        oct3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                oct3KeyTyped(evt);
            }
        });
        jPanelTools1.add(oct3);
        oct3.setBounds(270, 40, 30, 20);

        subnetMask.setEditable(false);
        jPanelTools1.add(subnetMask);
        subnetMask.setBounds(210, 70, 120, 20);

        oct4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                oct4KeyTyped(evt);
            }
        });
        jPanelTools1.add(oct4);
        oct4.setBounds(310, 40, 30, 20);

        networkAddress.setEditable(false);
        jPanelTools1.add(networkAddress);
        networkAddress.setBounds(210, 100, 120, 20);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText(".");
        jPanelTools1.add(jLabel2);
        jLabel2.setBounds(300, 40, 10, 20);

        broadcastAddress.setEditable(false);
        jPanelTools1.add(broadcastAddress);
        broadcastAddress.setBounds(210, 130, 120, 20);

        cidrValue.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cidrValueKeyTyped(evt);
            }
        });
        jPanelTools1.add(cidrValue);
        cidrValue.setBounds(370, 40, 30, 20);

        noSubnets.setEditable(false);
        jPanelTools1.add(noSubnets);
        noSubnets.setBounds(210, 160, 120, 20);

        jLabel3.setText("Subnet Mask");
        jPanelTools1.add(jLabel3);
        jLabel3.setBounds(100, 70, 110, 20);

        hostsPSubnet.setEditable(false);
        jPanelTools1.add(hostsPSubnet);
        hostsPSubnet.setBounds(210, 190, 120, 20);

        jLabel4.setText("Network Address");
        jPanelTools1.add(jLabel4);
        jLabel4.setBounds(100, 100, 110, 20);

        calBtn.setText("Calculate");
        calBtn.setMargin(new java.awt.Insets(0, 0, 0, 0));
        calBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calBtnActionPerformed(evt);
            }
        });
        jPanelTools1.add(calBtn);
        calBtn.setBounds(370, 70, 80, 30);

        jLabel5.setText("Broadcast Address");
        jPanelTools1.add(jLabel5);
        jLabel5.setBounds(100, 130, 110, 20);

        resetBtn.setText("Reset");
        resetBtn.setMargin(new java.awt.Insets(0, 0, 0, 0));
        resetBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetBtnActionPerformed(evt);
            }
        });
        jPanelTools1.add(resetBtn);
        resetBtn.setBounds(370, 110, 80, 30);

        jLabel6.setText("Number of Subnets");
        jPanelTools1.add(jLabel6);
        jLabel6.setBounds(100, 160, 110, 20);

        jLabel1.setText("IP Address");
        jPanelTools1.add(jLabel1);
        jLabel1.setBounds(100, 40, 80, 20);

        jLabel7.setText("Hosts Per Subnet");
        jPanelTools1.add(jLabel7);
        jLabel7.setBounds(100, 190, 110, 20);

        oct1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oct1ActionPerformed(evt);
            }
        });
        oct1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                oct1KeyTyped(evt);
            }
        });
        jPanelTools1.add(oct1);
        oct1.setBounds(190, 40, 30, 20);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Network Class");
        jPanelTools1.add(jLabel8);
        jLabel8.setBounds(360, 160, 100, 20);

        oct2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                oct2KeyTyped(evt);
            }
        });
        jPanelTools1.add(oct2);
        oct2.setBounds(230, 40, 30, 20);

        networkClass.setEditable(false);
        jPanelTools1.add(networkClass);
        networkClass.setBounds(400, 180, 20, 20);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("/");
        jPanelTools1.add(jLabel9);
        jLabel9.setBounds(350, 40, 10, 20);

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText(".");
        jPanelTools1.add(jLabel10);
        jLabel10.setBounds(220, 40, 10, 20);

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText(".");
        jPanelTools1.add(jLabel11);
        jLabel11.setBounds(260, 40, 10, 20);

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Other");
        jPanelTools1.add(jLabel12);
        jLabel12.setBounds(100, 400, 350, 20);
        jPanelTools1.add(jSeparator2);
        jSeparator2.setBounds(10, 390, 550, 10);

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Subnet Calculator");
        jPanelTools1.add(jLabel13);
        jLabel13.setBounds(100, 10, 350, 20);

        jTextFieldType7Input.setText("05240E0715444F1D0A321F131F211D1A2A373B243A3017301710");
        jPanelTools1.add(jTextFieldType7Input);
        jTextFieldType7Input.setBounds(100, 260, 350, 20);

        jButton21.setText("Decrypt");
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });
        jPanelTools1.add(jButton21);
        jButton21.setBounds(180, 290, 79, 30);

        jButton22.setText("Encrypt");
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });
        jPanelTools1.add(jButton22);
        jButton22.setBounds(290, 290, 80, 30);

        jTextFieldType7Output.setEditable(false);
        jPanelTools1.add(jTextFieldType7Output);
        jTextFieldType7Output.setBounds(100, 330, 350, 20);

        jButton23.setText("Copy to Clipboard");
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });
        jPanelTools1.add(jButton23);
        jButton23.setBounds(190, 360, 170, 23);

        jButtonJSDiff.setText("jsDiff");
        jButtonJSDiff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonJSDiffActionPerformed(evt);
            }
        });
        jPanelTools1.add(jButtonJSDiff);
        jButtonJSDiff.setBounds(310, 440, 90, 23);
        jPanelTools1.add(jSeparator6);
        jSeparator6.setBounds(20, 220, 550, 10);

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Type 7 Decrypt/Encrypt");
        jPanelTools1.add(jLabel14);
        jLabel14.setBounds(100, 230, 350, 20);

        jButtonConfigBuilder.setText("Config Builder");
        jButtonConfigBuilder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConfigBuilderActionPerformed(evt);
            }
        });
        jPanelTools1.add(jButtonConfigBuilder);
        jButtonConfigBuilder.setBounds(140, 440, 120, 23);

        jButton24.setText("Reference");
        jButton24.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });
        jPanelTools1.add(jButton24);
        jButton24.setBounds(480, 30, 80, 40);

        jTabbedTools2.addTab("Tools-1", jPanelTools1);

        jPanel3.setLayout(null);

        jButtonFolderToZip.setText("Folder to Zip!");
        jButtonFolderToZip.setToolTipText("");
        jButtonFolderToZip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFolderToZipActionPerformed(evt);
            }
        });
        jPanel3.add(jButtonFolderToZip);
        jButtonFolderToZip.setBounds(280, 100, 170, 20);
        jPanel3.add(jTextFieldZipSourceFolder);
        jTextFieldZipSourceFolder.setBounds(100, 40, 350, 20);
        jPanel3.add(jTextFieldZipFilename);
        jTextFieldZipFilename.setBounds(100, 70, 350, 20);

        jButton25.setText("Browse");
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton25);
        jButton25.setBounds(460, 40, 90, 20);

        jLabel16.setText("Password:");
        jPanel3.add(jLabel16);
        jLabel16.setBounds(10, 100, 90, 20);

        jPasswordFieldZip.setToolTipText("");
        jPasswordFieldZip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordFieldZipActionPerformed(evt);
            }
        });
        jPanel3.add(jPasswordFieldZip);
        jPasswordFieldZip.setBounds(100, 100, 160, 20);

        jLabel18.setText("Filename:");
        jPanel3.add(jLabel18);
        jLabel18.setBounds(10, 70, 90, 20);
        jPanel3.add(jSeparator7);
        jSeparator7.setBounds(10, 160, 550, 10);
        jPanel3.add(jProgressBarZip);
        jProgressBarZip.setBounds(20, 130, 530, 20);

        jLabelFolderToZip2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabelFolderToZip2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelFolderToZip2.setText("Folder to Encrypted Zip");
        jPanel3.add(jLabelFolderToZip2);
        jLabelFolderToZip2.setBounds(100, 10, 350, 20);

        jLabel19.setText("Source Folder:");
        jPanel3.add(jLabel19);
        jLabel19.setBounds(10, 40, 90, 20);

        jTabbedTools2.addTab("Tools-2", jPanel3);

        jPanel4.setLayout(null);

        jPanel6.setLayout(null);

        userList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                userListMouseReleased(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                userListMousePressed(evt);
            }
        });
        jScrollPane2.setViewportView(userList);

        jPanel6.add(jScrollPane2);
        jScrollPane2.setBounds(400, 30, 140, 380);

        mainChatArea.setEditable(false);
        mainChatArea.setColumns(20);
        mainChatArea.setRows(5);
        jScrollPane3.setViewportView(mainChatArea);

        jPanel6.add(jScrollPane3);
        jScrollPane3.setBounds(10, 30, 380, 380);

        jTextField1.setMaximumSize(new java.awt.Dimension(6, 2147483647));
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jPanel6.add(jTextField1);
        jTextField1.setBounds(10, 420, 530, 30);

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Chat doesn't really work yet.");
        jPanel6.add(jLabel15);
        jLabel15.setBounds(10, 4, 530, 20);

        jPanel4.add(jPanel6);
        jPanel6.setBounds(10, 10, 550, 460);

        jTabbedTools2.addTab("Chat", jPanel4);

        jPanelSettings.setLayout(null);

        jLabelSSHClient.setText("SSH Client:");
        jPanelSettings.add(jLabelSSHClient);
        jLabelSSHClient.setBounds(10, 10, 90, 30);

        buttonGroupSSHClient.add(jRadioButtonSSHClientSecureCRT);
        jRadioButtonSSHClientSecureCRT.setSelected(true);
        jRadioButtonSSHClientSecureCRT.setText("SecureCRT");
        jPanelSettings.add(jRadioButtonSSHClientSecureCRT);
        jRadioButtonSSHClientSecureCRT.setBounds(170, 10, 93, 30);

        buttonGroupSSHClient.add(jRadioButtonSSHClientPuTTY);
        jRadioButtonSSHClientPuTTY.setText("PuTTY");
        jRadioButtonSSHClientPuTTY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonSSHClientPuTTYActionPerformed(evt);
            }
        });
        jPanelSettings.add(jRadioButtonSSHClientPuTTY);
        jRadioButtonSSHClientPuTTY.setBounds(100, 10, 70, 30);

        jLabelListTextSizePreview.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelListTextSizePreview.setText("c3850-a01-zamjp-101");
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
        jSliderListTextSize.setValue(1);
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

        jButtonUpdateLaunchPad.setText("Update LaunchPad");
        jButtonUpdateLaunchPad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUpdateLaunchPadActionPerformed(evt);
            }
        });
        jPanelSettings.add(jButtonUpdateLaunchPad);
        jButtonUpdateLaunchPad.setBounds(399, 450, 160, 23);

        jTabbedTools2.addTab("Settings", jPanelSettings);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedTools2, javax.swing.GroupLayout.DEFAULT_SIZE, 577, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedTools2, javax.swing.GroupLayout.DEFAULT_SIZE, 509, Short.MAX_VALUE)
        );

        jTabbedTools2.getAccessibleContext().setAccessibleName("");

        getAccessibleContext().setAccessibleName("NSB LaunchPad Pre-Alpha");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonUpdateLaunchPadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUpdateLaunchPadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonUpdateLaunchPadActionPerformed

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
                parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);

                // Set password
                String passText = new String(jPasswordFieldZip.getPassword());
                parameters.setPassword(passText);

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

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        // TODO add your handling code here:
        String myValue = "cmd.exe /c start explorer.exe \"" + pathDesktop + "\\LaunchPad\\Documents\\IP Addressing Breakdown.pdf\"";
        System.out.println(myValue);
        try {
            Runtime.getRuntime().exec(myValue);
        }
        catch (IOException e) {
            System.out.println("HEY Buddy ! U r Doing Something Wrong ");
            JOptionPane.showMessageDialog(null, "Something is wrong!");
        }
    }//GEN-LAST:event_jButton24ActionPerformed

    private void jButtonConfigBuilderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConfigBuilderActionPerformed
        // TODO add your handling code here:
        System.out.println("Pressed");
        String strWebpage = new String(pathDesktop + "\\LaunchPad\\HTML\\configbuilder\\index.html");
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
        String strWebpage = new String(pathDesktop + "\\LaunchPad\\HTML\\jsdiff\\index.html");
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

    private void oct2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_oct2KeyTyped
        // TODO add your handling code here:
        System.out.println("Key pressed code=" + evt.getKeyCode() + ", char=" + evt.getKeyChar());
        if (evt.getKeyChar() == KeyEvent.VK_PERIOD) {
            Robot r = null;
            try {
                r = new Robot();
            } catch (AWTException ex) {
                Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
            }
            r.keyPress(KeyEvent.VK_BACK_SPACE);
            r.keyRelease(KeyEvent.VK_BACK_SPACE);
            r.keyPress(KeyEvent.VK_TAB);
            r.keyRelease(KeyEvent.VK_TAB);
        }
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            Robot r = null;
            try {
                r = new Robot();
            } catch (AWTException ex) {
                Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
            }
            calBtn.doClick();
        }
    }//GEN-LAST:event_oct2KeyTyped

    private void oct1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_oct1KeyTyped
        // TODO add your handling code here:
        System.out.println("Key pressed code=" + evt.getKeyCode() + ", char=" + evt.getKeyChar());
        if (evt.getKeyChar() == KeyEvent.VK_PERIOD) {
            Robot r = null;
            try {
                r = new Robot();
            } catch (AWTException ex) {
                Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
            }
            r.keyPress(KeyEvent.VK_BACK_SPACE);
            r.keyRelease(KeyEvent.VK_BACK_SPACE);
            r.keyPress(KeyEvent.VK_TAB);
            r.keyRelease(KeyEvent.VK_TAB);
        }
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            Robot r = null;
            try {
                r = new Robot();
            } catch (AWTException ex) {
                Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
            }
            calBtn.doClick();
        }
    }//GEN-LAST:event_oct1KeyTyped

    private void oct1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oct1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_oct1ActionPerformed

    private void resetBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetBtnActionPerformed
        oct1.setText("");
        oct2.setText("");
        oct3.setText("");
        oct4.setText("");
        cidrValue.setText("");
        subnetMask.setText("");
        networkAddress.setText("");
        broadcastAddress.setText("");
        noSubnets.setText("");
        hostsPSubnet.setText("");
        networkClass.setText("");
    }//GEN-LAST:event_resetBtnActionPerformed

    private void calBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calBtnActionPerformed
        if(!oct1.getText().equals("")&&!oct2.getText().equals("")&&!oct3.getText().equals("")&&!oct4.getText().equals("")&&!cidrValue.getText().equals("")){

            int oct1v = Integer.parseInt(oct1.getText().toString());
            int oct2v = Integer.parseInt(oct2.getText().toString());
            int oct3v = Integer.parseInt(oct3.getText().toString());
            int oct4v = Integer.parseInt(oct4.getText().toString());

            int cidrv = Integer.parseInt(cidrValue.getText().toString());

            switch(cidrv){
                case 1: subnetMask.setText("128.0.0.0"); break;
                case 2: subnetMask.setText("192.0.0.0"); break;
                case 3: subnetMask.setText("224.0.0.0"); break;
                case 4: subnetMask.setText("240.0.0.0"); break;
                case 5: subnetMask.setText("248.0.0.0"); break;
                case 6: subnetMask.setText("252.0.0.0"); break;
                case 7: subnetMask.setText("254.0.0.0"); break;
                case 8: subnetMask.setText("255.0.0.0"); break;
                case 9: subnetMask.setText("255.128.0.0"); break;
                case 10: subnetMask.setText("255.192.0.0"); break;
                case 11: subnetMask.setText("255.224.0.0"); break;
                case 12: subnetMask.setText("255.240.0.0"); break;
                case 13: subnetMask.setText("255.248.0.0"); break;
                case 14: subnetMask.setText("255.252.0.0"); break;
                case 15: subnetMask.setText("255.254.0.0"); break;
                case 16: subnetMask.setText("255.255.0.0"); break;
                case 17: subnetMask.setText("255.255.128.0"); break;
                case 18: subnetMask.setText("255.255.192.0"); break;
                case 19: subnetMask.setText("255.255.224.0"); break;
                case 20: subnetMask.setText("255.255.240.0"); break;
                case 21: subnetMask.setText("255.255.248.0"); break;
                case 22: subnetMask.setText("255.255.252.0"); break;
                case 23: subnetMask.setText("255.255.254.0"); break;
                case 24: subnetMask.setText("255.255.255.0"); break;
                case 25: subnetMask.setText("255.255.255.128"); break;
                case 26: subnetMask.setText("255.255.255.192"); break;
                case 27: subnetMask.setText("255.255.255.224"); break;
                case 28: subnetMask.setText("255.255.255.240"); break;
                case 29: subnetMask.setText("255.255.255.248"); break;
                case 30: subnetMask.setText("255.255.255.252"); break;
                case 31: subnetMask.setText("255.255.255.254"); break;
                case 32: subnetMask.setText("255.255.255.255"); break;

                default: cidrValue.setText("Invalid");
            }
            if(oct1v>=0&&oct1v<=127){networkClass.setText("A");
                networkAddress.setText(oct1.getText()+".0.0.0");
                int m;

            }
            if(oct1v>=128&&oct1v<=191){networkClass.setText("B");
                networkAddress.setText(oct1.getText()+"."+oct2.getText()+".0.0");
            }
            if(oct1v>=192&&oct1v<=223){networkClass.setText("c");
                networkAddress.setText(oct1.getText()+"."+oct2.getText()+"."+oct3.getText()+".0");
            }
            if(oct1v>=224&&oct1v<=239)networkClass.setText("D");
            if(oct1v>=240&&oct1v<=255)networkClass.setText("E");

            broadcastAddress.setText(oct1.getText()+"."+oct2.getText()+"."+oct3.getText()+".255");

            int value=  32-Integer.parseInt(cidrValue.getText());
            int outPut=(int) Math.pow(2, value);
            hostsPSubnet.setText( ""+outPut ) ;

            //now set the no of subnets
            int subnets=Integer.parseInt(hostsPSubnet.getText())/Integer.parseInt(cidrValue.getText());

            noSubnets.setText(""+subnets);
        }else{
            JOptionPane.showMessageDialog(null, "please enter the missing value!!");
        }
    }//GEN-LAST:event_calBtnActionPerformed

    private void cidrValueKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cidrValueKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            Robot r = null;
            try {
                r = new Robot();
            } catch (AWTException ex) {
                Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
            }
            calBtn.doClick();
        }
    }//GEN-LAST:event_cidrValueKeyTyped

    private void oct4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_oct4KeyTyped
        // TODO add your handling code here:
        System.out.println("Key pressed code=" + evt.getKeyCode() + ", char=" + evt.getKeyChar());
        if (evt.getKeyChar() == KeyEvent.VK_SLASH) {
            Robot r = null;
            try {
                r = new Robot();
            } catch (AWTException ex) {
                Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
            }
            r.keyPress(KeyEvent.VK_BACK_SPACE);
            r.keyRelease(KeyEvent.VK_BACK_SPACE);
            r.keyPress(KeyEvent.VK_TAB);
            r.keyRelease(KeyEvent.VK_TAB);
        }
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            Robot r = null;
            try {
                r = new Robot();
            } catch (AWTException ex) {
                Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
            }
            calBtn.doClick();
        }
    }//GEN-LAST:event_oct4KeyTyped

    private void oct3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_oct3KeyTyped
        // TODO add your handling code here:
        System.out.println("Key pressed code=" + evt.getKeyCode() + ", char=" + evt.getKeyChar());
        if (evt.getKeyChar() == KeyEvent.VK_PERIOD) {
            Robot r = null;
            try {
                r = new Robot();
            } catch (AWTException ex) {
                Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
            }
            r.keyPress(KeyEvent.VK_BACK_SPACE);
            r.keyRelease(KeyEvent.VK_BACK_SPACE);
            r.keyPress(KeyEvent.VK_TAB);
            r.keyRelease(KeyEvent.VK_TAB);
        }
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            Robot r = null;
            try {
                r = new Robot();
            } catch (AWTException ex) {
                Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
            }
            calBtn.doClick();
        }
    }//GEN-LAST:event_oct3KeyTyped

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

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        // TODO add your handling code here:
        String myValue = PropertyHandler.getInstance().getValue("Button17exe");
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
        String myValue = PropertyHandler.getInstance().getValue("Button18exe");
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
        String myValue = PropertyHandler.getInstance().getValue("Button19exe");
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
        String myValue = PropertyHandler.getInstance().getValue("Button20exe");
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
        String myValue = PropertyHandler.getInstance().getValue("Button16exe");
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
        String myValue = PropertyHandler.getInstance().getValue("Button15exe");
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
        String myValue = PropertyHandler.getInstance().getValue("Button14exe");
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
        String myValue = PropertyHandler.getInstance().getValue("Button13exe");
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
        String myValue = PropertyHandler.getInstance().getValue("Button9exe");
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
        String myValue = PropertyHandler.getInstance().getValue("Button5exe");
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
        String myValue = PropertyHandler.getInstance().getValue("Button10exe");
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
        String myValue = PropertyHandler.getInstance().getValue("Button6exe");
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
        String myValue = PropertyHandler.getInstance().getValue("Button11exe");
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
        String myValue = PropertyHandler.getInstance().getValue("Button7exe");
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
        String myValue = PropertyHandler.getInstance().getValue("Button12exe");
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
        String myValue = PropertyHandler.getInstance().getValue("Button8exe");
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
        String myValue = PropertyHandler.getInstance().getValue("Button4exe");
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
        String myValue = PropertyHandler.getInstance().getValue("Button3exe");
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
        String myValue = PropertyHandler.getInstance().getValue("Button2exe");
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
        String myValue = PropertyHandler.getInstance().getValue("Button1exe");
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
        SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("yyyyMMdd_HHmm-ssSSS");
        String dateTime = simpleDateFormat.format(new Date());
        String fileLog = pathLogging + "\\Serial-" + dateTime + ".txt";
        System.out.println("Log file: " + fileLog);

        if (jRadioButtonConsolePutty.isSelected() == true) {
            String strEXEC = "" + pathDesktop + "/authexe/putty.exe -serial " + jComboBoxConsoleCOM.getSelectedItem() + " -sercfg " + jComboBoxConsoleBaud.getSelectedItem() + " ,8,n,1,N  ";
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
            String strEXEC = "C:\\Program Files\\SecureCRT x64\\SecureCRT\\SecureCRT.exe /LOG \"" + fileLog + "\" /T /SERIAL " + jComboBoxConsoleCOM.getSelectedItem() + " /BAUD " + jComboBoxConsoleBaud.getSelectedItem();
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

        try {
            Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"ping -t " + jTextFieldPingHostname.getText() + "\"");
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
            Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"tracert " + jTextFieldPingHostname.getText() + "\"");
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
            jButtonSSH.doClick();
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
            jButtonSSH.doClick();
        }
    }//GEN-LAST:event_jTextFieldConnectUsernameKeyTyped

    private void jButtonSSHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSSHActionPerformed
        // TODO add your handling code here:
        SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("yyyyMMdd_HHmm-ssSSS");
        String dateTime = simpleDateFormat.format(new Date());
        String fileLog = pathDesktop + "\\Logging-Output\\SSH-" + jTextFieldConnectHostname.getText() + " " + dateTime.toString() + ".txt";
        System.out.println("Log file: " + fileLog);

        if (jRadioButtonSSHClientPuTTY.isSelected() == true) {
            String passText = new String(jPasswordFieldConnectPassword.getPassword());
            String strEXEC = "" + pathDesktop + "/authexe/putty.exe -ssh " + jTextFieldConnectHostname.getText() + " -l " + jTextFieldConnectUsername.getText() + " -pw \"" + passText + "\"  ";
            try {
                Runtime.getRuntime().exec(strEXEC);
            }
            catch (IOException e) {
                System.out.println("HEY Buddy ! U r Doing Something Wrong ");
                JOptionPane.showMessageDialog(null, "Something is wrong!");
            }
        }
        if (jRadioButtonSSHClientSecureCRT.isSelected() == true) {

            if (jTextFieldConnectUsername.getText().equals("")){
                if (jPasswordFieldConnectPassword.getPassword().length == 0){
                    String strEXEC = "C:\\Program Files\\SecureCRT x64\\SecureCRT\\SecureCRT.exe /LOG \"" + fileLog + "\" /T /SSH2 /ACCEPTHOSTKEYS " + jTextFieldConnectHostname.getText();
                    System.out.println(strEXEC);
                    try {
                        Runtime.getRuntime().exec(strEXEC);
                    }
                    catch (IOException e) {
                        System.out.println("HEY Buddy ! U r Doing Something Wrong ");
                        JOptionPane.showMessageDialog(null, "Something is wrong!");
                    }
                }
            }
            else {
                String passText = new String(jPasswordFieldConnectPassword.getPassword());
                String strEXEC = "C:\\Program Files\\SecureCRT x64\\SecureCRT\\SecureCRT.exe /LOG \"" + fileLog + "\" /T /SSH2 /ACCEPTHOSTKEYS " + jTextFieldConnectHostname.getText() + " /L " + jTextFieldConnectUsername.getText() + " /PASSWORD \"" + passText + "\"  ";
                try {
                    Runtime.getRuntime().exec(strEXEC);
                }
                catch (IOException e) {
                    System.out.println("HEY Buddy ! U r Doing Something Wrong ");
                    JOptionPane.showMessageDialog(null, "Something is wrong!");
                }
            }
        }
    }//GEN-LAST:event_jButtonSSHActionPerformed

    private void jButtonHTTPSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHTTPSActionPerformed
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
    }//GEN-LAST:event_jButtonHTTPSActionPerformed

    private void jTextFieldConnectHostnameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldConnectHostnameKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            Robot r = null;
            try {
                r = new Robot();
            } catch (AWTException ex) {
                Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
            }
            jButtonSSH.doClick();
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

    private void userListMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_userListMouseReleased
        if(evt.isPopupTrigger()&&!userList.isSelectionEmpty())
        {      PopUpMenu popUpMenu =  new PopUpMenu();
            popUpMenu.showUser(evt.getComponent(), evt.getX(), evt.getY() , userSelected);}
    }//GEN-LAST:event_userListMouseReleased

    private void userListMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_userListMousePressed
        Point p = evt.getPoint();
        int index = userList.locationToIndex( p );

        if ( index != -1 )
        {
            Rectangle r = userList.getCellBounds( index, index );

            if ( r.x <= p.x && p.x <= r.x + r.width && r.y <= p.y && p.y <= r.y + r.height )
            {
                userList.setSelectedIndex( index );
            }

            else
            {
                userList.clearSelection();
            }
        }
        if(evt.isPopupTrigger()&&!userList.isSelectionEmpty())
        {      PopUpMenu popUpMenu =  new PopUpMenu();
            popUpMenu.showUser(evt.getComponent(), evt.getX(), evt.getY() , userSelected);}
    }//GEN-LAST:event_userListMousePressed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed

        resource.getChatTextFieldController().parseAndActOnMessage();
    }//GEN-LAST:event_jTextField1ActionPerformed

    
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
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LaunchPadForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            try {
                new LaunchPadForm().setVisible(true);
            } catch (IOException | URISyntaxException ex) {
                Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
            } catch (AWTException ex) {
                Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(LaunchPadForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
    }
    


   
     private ArrayList getSessionList() throws FileNotFoundException, IOException, URISyntaxException
    {
        ArrayList arrSessionList=new ArrayList();
        DefaultListModel listModel = new DefaultListModel();
        File pathDesktop = new File(System.getProperty("user.home"), "Desktop");
        //File pathWorkingDirectory = new File(System.getProperty("user.dir"));
        System.out.println("SessionList.csv directory: " + pathDesktop);
        File archivo = new File(pathDesktop + "\\LaunchPad\\SessionList.csv");
        try (FileReader fr = new FileReader(archivo)) {
            BufferedReader buffIn;
            buffIn = new BufferedReader(fr);
               
            String line;
            while ((line = buffIn.readLine()) != null) {
                arrSessionList.add(line);
                listModel.addElement(line);
                System.out.println(line);
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
        ArrayList stars=getSessionList();

        stars.stream().forEach((star) -> {
            String starName=star.toString().toLowerCase();
            if (starName.contains(searchTerm.toLowerCase())) {
                filteredItems.addElement(star);
            }
        });
        defaultListModel=filteredItems;
        jListSessions.setModel(defaultListModel);

    }
    
    private void logOff()
    {
        resource.getNetworkController().sendLogoffMessage();       
    }
        //--- Filter the ListBox
//        try (FileReader fr = new FileReader(arrSessionList)) {
//            BufferedReader buffIn = new BufferedReader(fr);
//            DefaultListModel listModel = new DefaultListModel();
//            String line;
//            while ((line = buffIn.readLine()) != null) {
//                listModel.addElement(line);
//            }
//            jListSessions.setModel(listModel);
//            }
   
    private String userSelected = null ;
    private SingletonUIResource resource;
    private boolean away = false;

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField broadcastAddress;
    private javax.swing.ButtonGroup buttonGroupConsoleClient;
    private javax.swing.ButtonGroup buttonGroupSSHClient;
    private javax.swing.JButton calBtn;
    private javax.swing.JTextField cidrValue;
    private javax.swing.JTextField hostsPSubnet;
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
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JButton jButtonConfigBuilder;
    private javax.swing.JButton jButtonConsole;
    private javax.swing.JButton jButtonFolderToZip;
    private javax.swing.JButton jButtonHTTPS;
    private javax.swing.JButton jButtonJSDiff;
    private javax.swing.JButton jButtonPing;
    private javax.swing.JButton jButtonSSH;
    private javax.swing.JButton jButtonShowCOMList;
    private javax.swing.JButton jButtonTracert;
    private javax.swing.JButton jButtonUpdateLaunchPad;
    private javax.swing.JCheckBox jCheckBoxDNS;
    private javax.swing.JComboBox<String> jComboBoxConsoleBaud;
    private javax.swing.JComboBox<String> jComboBoxConsoleCOM;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelConsoleClient;
    private javax.swing.JLabel jLabelFolderToZip2;
    private javax.swing.JLabel jLabelListTextSize1;
    private javax.swing.JLabel jLabelListTextSizePreview;
    private javax.swing.JLabel jLabelSSHClient;
    private javax.swing.JList<String> jListSessions;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanelMain;
    private javax.swing.JPanel jPanelSettings;
    private javax.swing.JPanel jPanelTools1;
    private javax.swing.JPasswordField jPasswordFieldConnectPassword;
    private javax.swing.JPasswordField jPasswordFieldZip;
    private javax.swing.JProgressBar jProgressBarZip;
    private javax.swing.JRadioButton jRadioButtonConsolePutty;
    private javax.swing.JRadioButton jRadioButtonConsoleSecureCRT;
    private javax.swing.JRadioButton jRadioButtonSSHClientPuTTY;
    private javax.swing.JRadioButton jRadioButtonSSHClientSecureCRT;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSlider jSliderListTextSize;
    private javax.swing.JTabbedPane jTabbedTools2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextFieldConnectHostname;
    private javax.swing.JTextField jTextFieldConnectUsername;
    private javax.swing.JTextField jTextFieldFilter;
    private javax.swing.JTextField jTextFieldPingHostname;
    private javax.swing.JTextField jTextFieldType7Input;
    private javax.swing.JTextField jTextFieldType7Output;
    private javax.swing.JTextField jTextFieldZipFilename;
    private javax.swing.JTextField jTextFieldZipSourceFolder;
    private javax.swing.JTextArea mainChatArea;
    private javax.swing.JTextField networkAddress;
    private javax.swing.JTextField networkClass;
    private javax.swing.JTextField noSubnets;
    private javax.swing.JTextField oct1;
    private javax.swing.JTextField oct2;
    private javax.swing.JTextField oct3;
    private javax.swing.JTextField oct4;
    private javax.swing.JButton resetBtn;
    private javax.swing.JTextField subnetMask;
    private javax.swing.JList userList;
    // End of variables declaration//GEN-END:variables


}