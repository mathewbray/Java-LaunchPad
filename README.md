# LaunchPad 

One place to launch any connection, link, script, reference, or application.  Uses SecureCRT for SSH.

![Main](https://github.com/mathewbray/Java-LaunchPad/raw/master/readme/Main.png)  
![Settings](https://github.com/mathewbray/Java-LaunchPad/raw/master/readme/Settings.png)



## Prerequisites

Microsoft Windows  
Java Runtime Environment

## Installing

In the "jar" folder you'll find "LaunchPad.jar". If you run it outside of "%SystemDrive%\LaunchPad", it will copy itself to "%SystemDrive%\LaunchPad" and add a link to the Desktop.  

Optionally create a shortcut like so:  
"C:\Program Files (x86)\Common Files\Oracle\Java\javapath\javaw.exe" -jar "%SystemDrive%\LaunchPad\LaunchPad.jar"


## Configuring

The properties file is located here:  
"%SystemDrive%\LaunchPad\launchpad.properties  
  
Within the properties file, are all the resources to customize LaunchPad.

*** Be sure to escape your characters if needed. ***

## Notes

Consider using "cmd.exe /c start" (or similar) to execute items within Button, Link, Reference, and Script sections.  

## Sources

[Cisco Vigenere](https://github.com/plajjan/java-CiscoVigenere)  
[zip4j](https://github.com/srikanth-lingala/zip4j)  
[Apache NTP](https://commons.apache.org/proper/commons-net/javadocs/api-3.6/org/apache/commons/net/ntp/NTPUDPClient.html)  