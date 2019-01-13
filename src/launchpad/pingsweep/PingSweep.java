/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package launchpad.pingsweep;

import java.applet.Applet;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mathew
 */
public class PingSweep extends Applet {
    
    public static String ipRange = "";
    public static int timeout = 0;
    public static List<InetAddress> hostList;

    public PingSweep() {
        super();
    }

    @Override
    public void init(){
       ipRange = getParameter("ipRange");
       timeout = Integer.parseInt(getParameter("timeout"));
    }

    //called from JS
    public static int getHostsNumber(){
        try{
            hostList = parseIpRange(ipRange);
        }catch(UnknownHostException e){ //do something

        }
        return hostList.size();
    }

    //called from JS
    public static String getAliveHosts(){
        String result = "";
        try{
           result = checkHosts(hostList);
        }catch(IOException io){
           //do something
        }
        return result;
    }

    private static List<InetAddress> parseIpRange(String ipRange) throws UnknownHostException {

        List<InetAddress> addresses = new ArrayList<>();
        if (ipRange.contains("-")) { //multiple IPs: ipRange = 172.31.229.240-172.31.229.250
            String[] ips = ipRange.split("-");
            String[] octets = ips[0].split("\\.");
            int lowerBound = Integer.parseInt(octets[3]);
            int upperBound = Integer.parseInt(ips[1].split("\\.")[3]);

            for (int i = lowerBound; i <= upperBound; i++) {
                String ip = octets[0] + "." + octets[1] + "." + octets[2] + "." + i;
                addresses.add(InetAddress.getByName(ip));
            }
        } else { //single ip: ipRange = 172.31.229.240
            addresses.add(InetAddress.getByName(ipRange));
        }
        return addresses;
    }

    private static String checkHosts(List<InetAddress> inetAddresses) throws IOException {
        String alive = "";
        for (InetAddress inetAddress : inetAddresses) {
            if (inetAddress.isReachable(timeout)) {
                alive += inetAddress.toString() + "\n";
            }
        }
        return alive;
    }
}
