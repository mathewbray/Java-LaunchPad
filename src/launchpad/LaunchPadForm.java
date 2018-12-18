/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package launchpad;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Mathew
 */
public class LaunchPadForm extends javax.swing.JFrame {

    DefaultListModel defaultListModel=new DefaultListModel();
    File pathWorkingDirectory = new File(System.getProperty("user.dir"));
    File pathDesktop = new File(System.getProperty("user.home"), "Desktop");

    /**
     * Creates new form LaunchPadForm
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException
     * @throws java.net.URISyntaxException
     */
    public LaunchPadForm() throws IOException, FileNotFoundException, URISyntaxException {
        initComponents();
        //importSessionList();
        getSessionList();
        //updateSessionList();
        //--- Apply button icons
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button1icon") + ".png")));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button2icon") + ".png")));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button3icon") + ".png")));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button4icon") + ".png")));
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button5icon") + ".png")));
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button6icon") + ".png")));
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button7icon") + ".png")));
        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button8icon") + ".png")));
        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button9icon") + ".png")));
        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button10icon") + ".png")));
        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button11icon") + ".png")));
        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button12icon") + ".png")));
        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button13icon") + ".png")));
        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button14icon") + ".png")));
        jButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button15icon") + ".png")));
        jButton16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button16icon") + ".png")));
        jButton17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button17icon") + ".png")));
        jButton18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button18icon") + ".png")));
        jButton19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button19icon") + ".png")));
        jButton20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/launchpad/images/buttons/"+ PropertyHandler.getInstance().getValue("Button20icon") + ".png")));
        //--- Load preloaded IPs
        jTextFieldConnectHostname.setText(PropertyHandler.getInstance().getValue("PreloadSSH"));
        jTextFieldPingHostname.setText(PropertyHandler.getInstance().getValue("PreloadPing"));

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
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jTextFieldFilter = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListSessions = new javax.swing.JList<>();
        jTextFieldConnectHostname = new javax.swing.JTextField();
        jTextFieldConnectUsername = new javax.swing.JTextField();
        jPasswordFieldConnectPassword = new javax.swing.JPasswordField();
        jButtonHTTPS = new javax.swing.JButton();
        jButtonSSH = new javax.swing.JButton();
        jTextFieldPingHostname = new javax.swing.JTextField();
        jCheckBoxDNS = new javax.swing.JCheckBox();
        jButtonPing = new javax.swing.JButton();
        jButtonTracert = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jButton5 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        jComboBoxConsoleCOM = new javax.swing.JComboBox<>();
        jComboBoxConsoleBaud = new javax.swing.JComboBox<>();
        jButtonShowCOMList = new javax.swing.JButton();
        jButtonConsole = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
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
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("NSB LaunchPad ");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setPreferredSize(new java.awt.Dimension(415, 528));
        setSize(new java.awt.Dimension(410, 600));
        getContentPane().setLayout(null);

        jPanel1.setLayout(null);

        jTextFieldFilter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldFilterKeyReleased(evt);
            }
        });
        jPanel1.add(jTextFieldFilter);
        jTextFieldFilter.setBounds(0, 0, 190, 20);

        jListSessions.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
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

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(0, 20, 190, 440);

        jTextFieldConnectHostname.setToolTipText("IP or DNS Hostname");
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
        jTextFieldConnectHostname.setBounds(200, 0, 120, 20);

        jTextFieldConnectUsername.setToolTipText("Username");
        jTextFieldConnectUsername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldConnectUsernameKeyTyped(evt);
            }
        });
        jPanel1.add(jTextFieldConnectUsername);
        jTextFieldConnectUsername.setBounds(200, 30, 120, 20);

        jPasswordFieldConnectPassword.setToolTipText("Password");
        jPasswordFieldConnectPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jPasswordFieldConnectPasswordKeyTyped(evt);
            }
        });
        jPanel1.add(jPasswordFieldConnectPassword);
        jPasswordFieldConnectPassword.setBounds(200, 50, 120, 20);

        jButtonHTTPS.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButtonHTTPS.setText("HTTPS");
        jButtonHTTPS.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonHTTPS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonHTTPSActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonHTTPS);
        jButtonHTTPS.setBounds(330, 0, 60, 30);

        jButtonSSH.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jButtonSSH.setText("SSH");
        jButtonSSH.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonSSH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSSHActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonSSH);
        jButtonSSH.setBounds(330, 30, 60, 40);

        jTextFieldPingHostname.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextFieldPingHostname.setToolTipText("IP or DNS Hostname");
        jPanel1.add(jTextFieldPingHostname);
        jTextFieldPingHostname.setBounds(200, 90, 120, 20);

        jCheckBoxDNS.setSelected(true);
        jCheckBoxDNS.setText("DNS");
        jPanel1.add(jCheckBoxDNS);
        jCheckBoxDNS.setBounds(330, 90, 50, 20);

        jButtonPing.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButtonPing.setText("PING");
        jButtonPing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPingActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonPing);
        jButtonPing.setBounds(200, 110, 80, 20);

        jButtonTracert.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButtonTracert.setText("TRACERT");
        jButtonTracert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTracertActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonTracert);
        jButtonTracert.setBounds(290, 110, 100, 20);

        jButton1.setContentAreaFilled(false);
        jButton1.setFocusPainted(false);
        jButton1.setFocusable(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(200, 220, 40, 40);

        jButton2.setContentAreaFilled(false);
        jButton2.setFocusPainted(false);
        jButton2.setFocusable(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2);
        jButton2.setBounds(250, 220, 40, 40);

        jButton3.setContentAreaFilled(false);
        jButton3.setFocusPainted(false);
        jButton3.setFocusable(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3);
        jButton3.setBounds(300, 220, 40, 40);

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
        jButton4.setBounds(350, 220, 40, 40);

        jButton6.setContentAreaFilled(false);
        jButton6.setFocusPainted(false);
        jButton6.setFocusable(false);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton6);
        jButton6.setBounds(250, 270, 40, 40);

        jButton7.setContentAreaFilled(false);
        jButton7.setFocusPainted(false);
        jButton7.setFocusable(false);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton7);
        jButton7.setBounds(300, 270, 40, 40);

        jButton8.setContentAreaFilled(false);
        jButton8.setFocusPainted(false);
        jButton8.setFocusable(false);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton8);
        jButton8.setBounds(350, 270, 40, 40);
        jPanel1.add(jSeparator3);
        jSeparator3.setBounds(200, 80, 190, 10);
        jPanel1.add(jSeparator4);
        jSeparator4.setBounds(200, 210, 190, 10);

        jButton5.setContentAreaFilled(false);
        jButton5.setFocusPainted(false);
        jButton5.setFocusable(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton5);
        jButton5.setBounds(200, 270, 40, 40);

        jButton9.setContentAreaFilled(false);
        jButton9.setFocusPainted(false);
        jButton9.setFocusable(false);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton9);
        jButton9.setBounds(200, 320, 40, 40);

        jButton10.setContentAreaFilled(false);
        jButton10.setFocusPainted(false);
        jButton10.setFocusable(false);
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton10);
        jButton10.setBounds(250, 320, 40, 40);

        jButton11.setContentAreaFilled(false);
        jButton11.setFocusPainted(false);
        jButton11.setFocusable(false);
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton11);
        jButton11.setBounds(300, 320, 40, 40);

        jButton12.setContentAreaFilled(false);
        jButton12.setFocusPainted(false);
        jButton12.setFocusable(false);
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton12);
        jButton12.setBounds(350, 320, 40, 40);

        jButton13.setContentAreaFilled(false);
        jButton13.setFocusPainted(false);
        jButton13.setFocusable(false);
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton13);
        jButton13.setBounds(200, 370, 40, 40);

        jButton14.setContentAreaFilled(false);
        jButton14.setFocusPainted(false);
        jButton14.setFocusable(false);
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton14);
        jButton14.setBounds(250, 370, 40, 40);

        jButton15.setContentAreaFilled(false);
        jButton15.setFocusPainted(false);
        jButton15.setFocusable(false);
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton15);
        jButton15.setBounds(300, 370, 40, 40);

        jButton16.setContentAreaFilled(false);
        jButton16.setFocusPainted(false);
        jButton16.setFocusable(false);
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton16);
        jButton16.setBounds(350, 370, 40, 40);

        jButton17.setContentAreaFilled(false);
        jButton17.setFocusPainted(false);
        jButton17.setFocusable(false);
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton17);
        jButton17.setBounds(200, 420, 40, 40);

        jButton18.setContentAreaFilled(false);
        jButton18.setFocusPainted(false);
        jButton18.setFocusable(false);
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton18);
        jButton18.setBounds(250, 420, 40, 40);

        jButton19.setContentAreaFilled(false);
        jButton19.setFocusPainted(false);
        jButton19.setFocusable(false);
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton19);
        jButton19.setBounds(300, 420, 40, 40);

        jButton20.setContentAreaFilled(false);
        jButton20.setFocusPainted(false);
        jButton20.setFocusable(false);
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton20);
        jButton20.setBounds(350, 420, 40, 40);
        jPanel1.add(jSeparator5);
        jSeparator5.setBounds(200, 140, 190, 10);

        jComboBoxConsoleCOM.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "COM10", "COM11", "COM12", "COM13", "COM14", "COM15", "COM16", "COM17", "COM18", "COM19", "COM20" }));
        jComboBoxConsoleCOM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxConsoleCOMActionPerformed(evt);
            }
        });
        jPanel1.add(jComboBoxConsoleCOM);
        jComboBoxConsoleCOM.setBounds(200, 150, 70, 20);

        jComboBoxConsoleBaud.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "BAUD", "9600", "115200" }));
        jPanel1.add(jComboBoxConsoleBaud);
        jComboBoxConsoleBaud.setBounds(320, 150, 70, 20);

        jButtonShowCOMList.setText("?");
        jButtonShowCOMList.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonShowCOMList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonShowCOMListActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonShowCOMList);
        jButtonShowCOMList.setBounds(280, 150, 30, 20);

        jButtonConsole.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButtonConsole.setText("CONSOLE");
        jButtonConsole.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonConsole.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConsoleActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonConsole);
        jButtonConsole.setBounds(250, 170, 90, 30);

        jTabbedPane1.addTab("Main", jPanel1);

        jPanel3.setLayout(null);

        oct3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                oct3KeyTyped(evt);
            }
        });
        jPanel3.add(oct3);
        oct3.setBounds(180, 20, 30, 20);

        subnetMask.setEditable(false);
        jPanel3.add(subnetMask);
        subnetMask.setBounds(120, 60, 120, 20);

        oct4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                oct4KeyTyped(evt);
            }
        });
        jPanel3.add(oct4);
        oct4.setBounds(220, 20, 30, 20);

        networkAddress.setEditable(false);
        jPanel3.add(networkAddress);
        networkAddress.setBounds(120, 90, 120, 20);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText(".");
        jPanel3.add(jLabel2);
        jLabel2.setBounds(210, 20, 10, 20);

        broadcastAddress.setEditable(false);
        jPanel3.add(broadcastAddress);
        broadcastAddress.setBounds(120, 120, 120, 20);

        cidrValue.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cidrValueKeyTyped(evt);
            }
        });
        jPanel3.add(cidrValue);
        cidrValue.setBounds(280, 20, 30, 20);

        noSubnets.setEditable(false);
        jPanel3.add(noSubnets);
        noSubnets.setBounds(120, 150, 120, 20);

        jLabel3.setText("Subnet Mask");
        jPanel3.add(jLabel3);
        jLabel3.setBounds(10, 60, 110, 20);

        hostsPSubnet.setEditable(false);
        jPanel3.add(hostsPSubnet);
        hostsPSubnet.setBounds(120, 180, 120, 20);

        jLabel4.setText("Network Address");
        jPanel3.add(jLabel4);
        jLabel4.setBounds(10, 90, 110, 20);

        calBtn.setText("Calculate");
        calBtn.setMargin(new java.awt.Insets(0, 0, 0, 0));
        calBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calBtnActionPerformed(evt);
            }
        });
        jPanel3.add(calBtn);
        calBtn.setBounds(280, 60, 80, 30);

        jLabel5.setText("Broadcast Address");
        jPanel3.add(jLabel5);
        jLabel5.setBounds(10, 120, 110, 20);

        resetBtn.setText("Reset");
        resetBtn.setMargin(new java.awt.Insets(0, 0, 0, 0));
        resetBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetBtnActionPerformed(evt);
            }
        });
        jPanel3.add(resetBtn);
        resetBtn.setBounds(280, 100, 80, 30);

        jLabel6.setText("Number of Subnets");
        jPanel3.add(jLabel6);
        jLabel6.setBounds(10, 150, 110, 20);

        jLabel1.setText("IP Address");
        jPanel3.add(jLabel1);
        jLabel1.setBounds(10, 20, 80, 20);

        jLabel7.setText("Hosts Per Subnet");
        jPanel3.add(jLabel7);
        jLabel7.setBounds(10, 180, 110, 20);

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
        jPanel3.add(oct1);
        oct1.setBounds(100, 20, 30, 20);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Network Class");
        jPanel3.add(jLabel8);
        jLabel8.setBounds(270, 150, 100, 20);

        oct2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                oct2KeyTyped(evt);
            }
        });
        jPanel3.add(oct2);
        oct2.setBounds(140, 20, 30, 20);

        networkClass.setEditable(false);
        jPanel3.add(networkClass);
        networkClass.setBounds(310, 170, 20, 20);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("/");
        jPanel3.add(jLabel9);
        jLabel9.setBounds(260, 20, 10, 20);

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText(".");
        jPanel3.add(jLabel10);
        jLabel10.setBounds(130, 20, 10, 20);

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText(".");
        jPanel3.add(jLabel11);
        jLabel11.setBounds(170, 20, 10, 20);

        jTabbedPane1.addTab("SubnetCalc", jPanel3);

        getContentPane().add(jTabbedPane1);
        jTabbedPane1.setBounds(0, 0, 400, 490);
        jTabbedPane1.getAccessibleContext().setAccessibleName("");

        getContentPane().add(jPanel2);
        jPanel2.setBounds(50, 320, 190, 90);

        getAccessibleContext().setAccessibleName("NSB LaunchPad Pre-Alpha");

        pack();
    }// </editor-fold>//GEN-END:initComponents

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

    private void jButtonSSHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSSHActionPerformed
        // TODO add your handling code here:
        System.out.println("Pressed");
        String strEXEC = "" + pathDesktop + "/authexe/putty.exe -ssh " + jTextFieldConnectHostname.getText() + " " + jTextFieldConnectUsername + "@" + jPasswordFieldConnectPassword + "  ";
        try {
            Runtime.getRuntime().exec(strEXEC);
        }
        catch (IOException e) {
            System.out.println("HEY Buddy ! U r Doing Something Wrong ");
            JOptionPane.showMessageDialog(null, "Something is wrong!");
        }
    }//GEN-LAST:event_jButtonSSHActionPerformed

    private void jButtonHTTPSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHTTPSActionPerformed
        // TODO add your handling code here:
        System.out.println("Pressed");
        Icon iconExplorer = new ImageIcon(getClass().getResource("/launchpad/images/buttons/iexplore.png"));
        Icon iconFireFox = new ImageIcon(getClass().getResource("/launchpad/images/buttons/firefox.png"));
        Object[] iconArray = {iconExplorer,
            iconFireFox};
        int result = JOptionPane.showOptionDialog(null,
            "IE or FireFox?",
            "Browser Chooser",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            iconArray,
            iconArray[1]);
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
        if(result == 1) {
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
    }//GEN-LAST:event_jButtonHTTPSActionPerformed

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

    private void oct1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oct1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_oct1ActionPerformed

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

    private void jButtonConsoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConsoleActionPerformed
        // TODO add your handling code here:
        System.out.println("Pressed");
        String strEXEC = "" + pathDesktop + "/authexe/putty.exe -serial " + jComboBoxConsoleCOM.getSelectedItem() + " -sercfg " + jComboBoxConsoleBaud.getSelectedItem() + " ,8,n,1,N ";
        try {
            Runtime.getRuntime().exec(strEXEC);
        }
        catch (IOException e) {
            System.out.println("HEY Buddy ! U r Doing Something Wrong ");
            JOptionPane.showMessageDialog(null, "Something is wrong!");
        }
    }//GEN-LAST:event_jButtonConsoleActionPerformed

    private void jComboBoxConsoleCOMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxConsoleCOMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxConsoleCOMActionPerformed

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LaunchPadForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LaunchPadForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LaunchPadForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LaunchPadForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            try {
                new LaunchPadForm().setVisible(true);
            } catch (IOException | URISyntaxException ex) {
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
        File archivo = new File(pathDesktop + "/SessionList.csv");
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
   


    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField broadcastAddress;
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
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JButton jButtonConsole;
    private javax.swing.JButton jButtonHTTPS;
    private javax.swing.JButton jButtonPing;
    private javax.swing.JButton jButtonSSH;
    private javax.swing.JButton jButtonShowCOMList;
    private javax.swing.JButton jButtonTracert;
    private javax.swing.JCheckBox jCheckBoxDNS;
    private javax.swing.JComboBox<String> jComboBoxConsoleBaud;
    private javax.swing.JComboBox<String> jComboBoxConsoleCOM;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList<String> jListSessions;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPasswordField jPasswordFieldConnectPassword;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextFieldConnectHostname;
    private javax.swing.JTextField jTextFieldConnectUsername;
    private javax.swing.JTextField jTextFieldFilter;
    private javax.swing.JTextField jTextFieldPingHostname;
    private javax.swing.JTextField networkAddress;
    private javax.swing.JTextField networkClass;
    private javax.swing.JTextField noSubnets;
    private javax.swing.JTextField oct1;
    private javax.swing.JTextField oct2;
    private javax.swing.JTextField oct3;
    private javax.swing.JTextField oct4;
    private javax.swing.JButton resetBtn;
    private javax.swing.JTextField subnetMask;
    // End of variables declaration//GEN-END:variables


}