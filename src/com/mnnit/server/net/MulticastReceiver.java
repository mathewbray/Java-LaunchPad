
package com.mnnit.server.net;

import com.mnnit.server.event.ReceiverListener;
import com.mnnit.server.Defaults;
import static com.sun.glass.ui.Cursor.setVisible;
import java.awt.AWTException;
import java.awt.Component;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.Window;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import launchpad.LaunchPadForm;

/**
 *
 * @author Lakhan
 */
public class MulticastReceiver implements Runnable {
    
    
    /** Address for multicasting */
    private InetAddress address;
    
    /** Port for connection */
    private int port;
    
    /** Already Connected or not */
    private boolean connected = false;
    
    /** Multicast Socket */
    private MulticastSocket mcSocket;
    
    /** Listener */
    private ReceiverListener listener;
    
    private Thread th;
    
    public MulticastReceiver()
    {
        this(Defaults.ipAddress, Defaults.port);
    }
    
    public MulticastReceiver(final String ipAddress, final int port)
    {
        this.port = port;
        try {
            address = InetAddress.getByName(ipAddress);
        } catch (UnknownHostException ex) {
            Logger.getLogger(MulticastSender.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
    }
    
    public synchronized boolean startReceiver ()
    {
        if(connected)
        {
            System.out.println("Already Connected");
        }
        else
        {
            try {
            if(mcSocket==null)
                mcSocket = new MulticastSocket(port);
            mcSocket.joinGroup(address);
            mcSocket.setTimeToLive(64);
            connected=true;
            }
            catch (final IOException e)
            {
                if ( mcSocket != null )
		{
			if ( !mcSocket.isClosed() )
				mcSocket.close();
			mcSocket = null;
		}
            }
        }
        if ( connected && ( th == null || !th.isAlive() ) )
		{
			startThread();
		}
        return connected;
    }

    public void run() {
        while ( connected )
		{
			try
			{
				DatagramPacket packet = new DatagramPacket(
						new byte[Defaults.packet_size], Defaults.packet_size );

				if ( connected )
				{
                                    mcSocket.receive( packet );
                                    String ip = packet.getAddress().getHostAddress();
                                    String message = new String( packet.getData() ).trim();
                                    System.out.println( "Message arrived from " + ip + ": " + message );
                                                    SystemTray tray = SystemTray.getSystemTray();

                                    String search = "chat";
                                    if ( message.toLowerCase().indexOf(search.toLowerCase()) != -1 ) {
                                        //- If the icon is a file
                                        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
                                        //- Alternative (if the icon is on the classpath):
                                        //Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("icon.png"));

                                        TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
                                        //- Let the system resize the image if needed
                                        //trayIcon.setImageAutoSize(true);
                                        //- Set tooltip text for the tray icon
                                        trayIcon.setToolTip("System tray icon demo");
                                        tray.add(trayIcon);

                                        //TimeUnit.SECONDS.sleep(5);
                                        trayIcon.displayMessage(ip, message, TrayIcon.MessageType.INFO);
    //                                        if (!isFocused()) {
    //                                            setVisible(false);
    //                                            setVisible(true);
    //                                        }
                                        if(listener!=null)
                                        listener.messageReceived(message, ip);                                        } else {
                                            System.out.println("not found");
                                    }
				}
			}

			// Happens when socket is closed, or network is down
			catch ( final IOException e )
			{
				e.printStackTrace();
			} catch (AWTException ex) {
                Logger.getLogger(MulticastReceiver.class.getName()).log(Level.SEVERE, null, ex);
            }
		}
    }
    
    public void startThread() {
        th = new Thread(this, "MessageReceiverThread");
        th.start();
    }
    
    public void registerListener(ReceiverListener listener)
    {
        this.listener=listener;
    }
    
}
