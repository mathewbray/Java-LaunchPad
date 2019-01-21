package com.mnnit.server;

import launchpad.PropertyHandler;

/**
 *
 * @author Lakhan
 */
public interface Defaults {
    
    //--- Original settings
//    public String ipAddress = "224.168.5.200";
//    public int port = 42267;
//    public int private_chat_port = 53521;
//    public int packet_size = 1024;

    public String ipAddress = PropertyHandler.getInstance().getValue("ChatIPAddress");
    public int port = Integer.parseInt(PropertyHandler.getInstance().getValue("ChatPort"));
    public int private_chat_port = 53521;
    public int packet_size = 1024;
    
}
