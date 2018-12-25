window.onload = function() {
    //- Apply Preloaded Config
    makeOutput(strPreloadedConfig);
	generateConfigOutput0();
    applySplitSlider();
    // LOAD CONFIG FILE LISTENER
    document.getElementById('file').addEventListener('change', readFile, false);
};

// TIMESTAMP
var timestamp
function updateTimestamp() {
    Number.prototype.padLeft = function(base,chr){
        var  len = (String(base || 10).length - String(this).length)+1;
        return len > 0? new Array(len).join(chr || '0')+this : this;
    }
    var d = new Date,
    dtimeformat = [ d.getFullYear().padLeft(),
                    (d.getMonth()+1).padLeft(),
                    d.getDate()].join('-')+
                    '_' +
                [ d.getHours().padLeft(),
                    d.getMinutes().padLeft(),
                    d.getSeconds().padLeft()].join('');
    timestamp = dtimeformat
}

// DUPLICATE REMOVER FOR ARRAYS
function ArrNoDupe(a) {
    var temp = {};
    for (var i = 0; i < a.length; i++)
        temp[a[i]] = true;
    return Object.keys(temp);
}

// PRELOADED CONFIG
var strPreloadedConfig = '\n\
!--- LAB USE ONLY\n\
!--- SSH with DHCP\n\
!\n\
enable\n\
conf t\n\
!\n\
!${hostname {HOSTNAME}}\n\
!\n\
!${username {USERNAME}} privilege 15 secret !${{PASSWORD}}\n\
!${ip domain-name {DOMAIN-NAME}}\n\
!\n\
aaa new-model\n\
aaa authentication login default local\n\
aaa authentication enable default enable\n\
aaa authorization exec default local\n\
!\n\
![DHCP]\n\
![\n\
ip dhcp excluded-address !${{DHCP-EXCLUDED-IP-OR-RANGE}}\n\
\n\
ip dhcp pool vlan!${{VLAN-NUMBER}}\n\
 network !${{DHCP-SUBNET}} !${{DHCP-SUBNET-MASK}}\n\
 default-router !${{DHCP-DEFAULT-ROUTER}}\n\
 lease 30\n\
\n\
!--- \n\
ip dhcp pool RESERVATION-01!${{DHCP-RESERVATION-1-MAC}}\n\
 host !${{DHCP-RESERVATION-1-HOST-IP}} !${{DHCP-RESERVATION-1-HOST-SUBNET-MASK}}\n\
 client-identifier 01!${{DHCP-RESERVATION-1-MAC}}\n\
 default-router !${{DHCP-DEFAULT-ROUTER}}\n\
 lease !${{DHCP-LEASE-EXPIRATION-IN-DAYS}}\n\
ip dhcp snooping vlan !${{VLAN-NUMBER}}\n\
ip dhcp snooping\n\
!]\n\
!\n\
![SSH]\n\
![\n\
crypto key generate rsa general-keys modulus 1024\n\
ip ssh version 2\n\
line vty 0 4\n\
login local\n\
!]\n\
!\n\
!\n\
![VLAN_INTERFACE]\n\
![\n\
interface vlan!${{VLAN-NUMBER}}\n\
no shutdown\n\
!${ip address {MGMT-INTERFACE-IP}} !${{MGMT-INTERFACE_SUBNET}}\n\
no shutdown\n\
!]\n\
!\n\
![HTTP_HTTPS]\n\
![\n\
ip http server\n\
ip http secure-server\n\
ip http authentication local\n\
end\n\
!]\n\
!\n\
![ARCHIVE_LOG]\n\
![\n\
archive\n\
 log config\n\
  logging enable\n\
  logging size 1000\n\
  hidekeys\n\
 path flash:/archive-configs/rollback-\n\
 maximum 14\n\
 write-memory\n\
end\n\
!]\n\
\n\
vlan !${{VLAN-NUMBER}}\n\
 name !${{VLAN-NAME}}\n\
\n\
no ip domain-lookup\n\
no ip icmp rate-limit unreachable\n\
service timestamps debug datetime msec\n\
ip tcp synwait-time 5\n\
lldp run\n\
!\n\
banner login #\n\
You are accessing a U.S. Government (USG) Information System (IS)\n\
that is provided for USG-authorized use only.\n\
\n\
Connected to: !${hostname {HOSTNAME}_LAB}\n\
\n\
#\n\
!\n\
line vty 0 15\n\
 exec-timeout 0 0\n\
 logging synchronous\n\
 privilege level 15\n\
 no login\n\
line con 0\n\
 exec-timeout 0 0\n\
 logging synchronous\n\
 privilege level 15\n\
 no login\n\
line aux 0\n\
 exec-timeout 0 0\n\
 logging synchronous\n\
 privilege level 15\n\
 no login\n\
!\n\
end\n\
copy running-config startup-config\n\
\n\
\n\
'
var strPreloadedConfig1 = '\n\
!--- LAB USE ONLY\n\
!--- 3850\n\
!\n\
\n\
enable\n\
conf t\n\
\n\
\n\
!\n\
!--- HOSTNAME\n\
!\n\
!${hostname {HOSTNAME}}\n\
\n\
\n\
\n\
!\n\
![MANGEMENT VLAN]\n\
![\n\
interface Vlan !${{VLAN-MGMT-NUMBER}}\n\
description MANAGEMENT VLAN\n\
ip address !${{VLAN-INT-MGMT-IP}} !${{VLAN-INT-MGMT-MASK}}\n\
ip access-group MGMT_VLAN_ACL in\n\
no shutdown\n\
!]\n\
!\n\
!--- MANAGEMENT VLAN SOURCE SETTINGS\n\
!\n\
!${logging source-interface Vlan {VLAN-MGMT-NUMBER}}\n\
!${ip tacacs source-interface Vlan {VLAN-MGMT-NUMBER}}\n\
!${snmp-server source-interface informs Vlan {VLAN-MGMT-NUMBER}}\n\
!${snmp-server trap-source Vlan {VLAN-MGMT-NUMBER}}\n\
!${ip radius source-interface Vlan {VLAN-MGMT-NUMBER}}\n\
!\n\
\n\
!\n\
![VLANS]\n\
![\n\
vtp domain NULL\n\
vtp mode transparent\n\
!\n\
!${vlan {VLAN-MGMT-NUMBER}\n\
! name Management_VLAN}\n\
!\n\
!${vlan {ACCESS-VLAN-NUMBER}\n\
! name User_VLAN}\n\
!\n\
!${vlan {VOICE-VLAN-NUMBER}\n\
! name Voice_VLAN}\n\
!\n\
!${vlan {VLAN-1-NUMBER}}\n\
!${ name {VLAN-1-NAME}}\n\
!\n\
!${vlan {VLAN-2-NUMBER}}\n\
!${ name {VLAN-2-NAME}}\n\
!\n\
!${vlan {VLAN-3-NUMBER}}\n\
!${ name {VLAN-3-NAME}}\n\
!\n\
!${vlan {VLAN-4-NUMBER}}\n\
!${ name {VLAN-4-NAME}}\n\
!\n\
!${vlan {VLAN-5-NUMBER}}\n\
!${ name {VLAN-5-NAME}}\n\
!\n\
vlan 3333\n\
name Dead_VLAN\n\
state suspend\n\
!\n\
vlan 999\n\
name Dead_Native_VLAN\n\
state suspend\n\
!]\n\
\n\
!\n\
!--- VLAN 1 DESCRIPTION\n\
!\n\
interface Vlan1\n\
description CONFIG GENERATED !${{AUTO_DATE}} by !${{TECHNICIAN}}\n\
shutdown\n\
\n\
!\n\
!--- EMERGENCY LOGIN\n\
!\n\
username ineedamedic privilege 0 secret P@ssw0rd\n\
enable secret 8 P@ssw0rd\n\
\n\
\n\
!\n\
!--- Default-Gateway\n\
!\n\
!${ip default-gateway {DEFAULT-GATEWAY}}\n\
!\n\
!--- Domain-Name\n\
!\n\
ip domain-name aybabtu.lab\n\
\n\
!\n\
!--- SSH Key\n\
!\n\
crypto key generate rsa general-keys modulus 2048\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n\
!!!!!!!!!!!!PAUSE!!!!!!!!!!!!!!!!!!!!!!!PAUSE!!!!!!!!!!!!!!!!!!!!!!!PAUSE!!!!!!!!!!!!\n\
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
!\n\
!--- MISC\n\
!\n\
no service pad\n\
service tcp-keepalives-in\n\
service tcp-keepalives-out \n\
service timestamps debug datetime msec localtime\n\
service timestamps log datetime msec localtime show-timezone\n\
service sequence-numbers\n\
!\n\
clock timezone PST -8\n\
clock summer-time PDT recurring\n\
authentication mac-move permit\n\
ip subnet-zero\n\
no ip source-route\n\
no ip domain-lookup\n\
!\n\
spanning-tree mode rapid-pvst\n\
spanning-tree portfast default\n\
spanning-tree portfast bpduguard default\n\
spanning-tree etherchannel guard misconfig\n\
spanning-tree extend system-id\n\
no spanning-tree optimize bpdu transmission\n\
!\n\
no ip http server\n\
no ip http secure-server\n\
ip http secure-active-session-modules none\n\
ip http active-session-modules none\n\
!ip http authentication aaa login-authentication default\n\
no vstack\n\
!\n\
lldp run\n\
\n\
!--- DISABLE PASSWORD RECOVERY\n\
system disable password recovery switch all\n\
\n\
!\n\
!--- SSH Options\n\
!\n\
ip tcp synwait-time 10\n\
ip ssh time-out 60\n\
ip ssh version 2\n\
ip ssh authentication-retries 3\n\
ip ssh logging events\n\
ip scp server enable\n\
\n\
!\n\
!--- ENABLE CONFIG LOGGING AND PATH\n\
!\n\
archive\n\
log config\n\
logging enable\n\
logging size 1000\n\
hidekeys\n\
path flash:/archive-configs/rollback-\n\
write-memory \n\
maximum 14\n\
do mkdir flash:/archive-configs\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
!\n\
!--- LOGGING\n\
!\n\
logging buffered 512000 informational\n\
logging console warnings\n\
logging monitor informational\n\
logging trap warnings\n\
logging facility local6\n\
logging 10.0.99.5\n\
\n\
!\n\
!--- ACCESS LISTS\n\
!\n\
!--- 10.0.99.15 ACS\n\
!--- 10.0.99.5 Kiwi Syslog\n\
!--- 10.0.99.36 NMS\n\
!--- 10.0.99.5 NTP\n\
!\n\
ip access-list standard NTP_ACL\n\
permit 10.0.99.6 log\n\
permit 10.0.99.7 log\n\
deny any log\n\
!\n\
ip access-list standard CiscoISE_SNMP_ACL\n\
permit 10.0.99.104 log\n\
permit 10.0.99.105 log\n\
permit 10.0.99.106 log\n\
deny any log\n\
!\n\
ip access-list standard NMS_SNMP_ACL\n\
permit 10.0.99.36 log\n\
deny any log\n\
!\n\
ip access-list standard MGMT_VTY_ACL\n\
permit 10.0.99.36 log\n\
permit 10.0.99.65 log\n\
permit 10.0.107.0 0.0.0.255 log\n\
permit 10.0.48.0 0.0.1.255 log\n\
deny any log\n\
!\n\
ip access-list extended MGMT_VLAN_ACL\n\
permit icmp any any log\n\
permit ip host 10.0.99.15 any log\n\
permit ip host 10.0.99.16 any log\n\
permit ip host 10.0.99.33 any log\n\
permit ip host 10.0.99.36 any log\n\
permit ip host 10.0.99.65 any log\n\
permit ip host 10.0.99.104 any log\n\
permit ip host 10.0.99.105 any log\n\
permit ip host 10.0.99.106 any log\n\
permit ip host 10.0.99.6 any log\n\
permit ip host 10.0.99.7 any log\n\
permit ip 10.0.66.0 0.0.0.255 any log\n\
permit ip 10.0.0.0 0.0.0.255 any log\n\
deny ip any any log\n\
exit\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n\
!!!!!!!!!!!!PAUSE!!!!!!!!!!!!!!!!!!!!!!!PAUSE!!!!!!!!!!!!!!!!!!!!!!!PAUSE!!!!!!!!!!!!\n\
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
!\n\
!--- TACACS \n\
! \n\
aaa new-model\n\
tacacs server ACS1\n\
address ipv4 10.0.99.15\n\
key 7 060506324F41\n\
timeout 10\n\
exit\n\
aaa group server tacacs+ ACS\n\
server name ACS1\n\
exit\n\
no tacacs-server directed-request\n\
\n\
!\n\
!--- AAA\n\
!\n\
aaa authentication login default group ACS local\n\
aaa authentication enable default group ACS enable\n\
aaa authorization exec default group ACS local if-authenticated\n\
aaa accounting exec default start-stop group ACS\n\
aaa accounting commands 1 default start-stop group ACS \n\
aaa accounting commands 15 default start-stop group ACS \n\
!--- COMMAND AUTHORIZATION\n\
!aaa authorization config-commands \n\
\n\
\n\
\n\
\n\
\n\
\n\
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n\
!!!!!!!!!!!!PAUSE!!!!!!!!!!!!!!!!!!!!!!!PAUSE!!!!!!!!!!!!!!!!!!!!!!!PAUSE!!!!!!!!!!!!\n\
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
!\n\
!--- LINES\n\
!\n\
line con 0\n\
timeout login response 300\n\
logging synchronous\n\
session-timeout 20\n\
login authentication default\n\
transport output ssh\n\
exec-timeout 3 0\n\
line vty 0 15\n\
access-class MGMT_VTY_ACL in\n\
logging synchronous\n\
length 0\n\
transport input ssh\n\
transport output ssh \n\
exec-timeout 5 0\n\
\n\
!\n\
!--- NTP\n\
!\n\
ntp authentication-key 11 md5 060506324F41 7\n\
ntp authenticate\n\
ntp trusted-key 11\n\
ntp server 10.0.99.6 key 11 prefer\n\
ntp server 10.0.99.7 key 11\n\
ntp access-group peer NTP_ACL\n\
!--- This command does not work\n\
ntp update-calendar\n\
\n\
!\n\
!--- SNMP (Other)\n\
!\n\
snmp-server system-shutdown\n\
!snmp-server enable traps snmp linkdown linkup ---Creates too many log messages\n\
snmp-server enable traps snmp authentication coldstart warmstart\n\
snmp-server enable traps tty\n\
snmp-server enable traps fru-ctrl\n\
snmp-server enable traps entity\n\
snmp-server enable traps port-security\n\
snmp-server enable traps envmon fan shutdown supply temperature status\n\
snmp-server enable traps config\n\
snmp-server enable traps bridge newroot topologychange\n\
snmp-server enable traps stpx inconsistency root-inconsistency loop-inconsistency\n\
snmp-server enable traps syslog\n\
snmp-server enable traps cpu threshold\n\
snmp-server enable traps errdisable\n\
!\n\
snmp ifmib ifindex persist\n\
\n\
!\n\
!--- BANNER BEFORE LOGIN\n\
!\n\
banner login #\n\
You have arrived at !${{HOSTNAME}}\n\
Config originally created !${{AUTO_DATE}} by !${{TECHNICIAN}}\n\
#\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n\
!!!!!!!!!!!!PAUSE!!!!!!!!!!!!!!!!!!!!!!!PAUSE!!!!!!!!!!!!!!!!!!!!!!!PAUSE!!!!!!!!!!!!\n\
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
!\n\
!--- ACCESS PORTS\n\
!\n\
interface range !${{ACCESS-PORTS-1}} !${{ACCESS-PORTS-2}} !${{ACCESS-PORTS-3}} !${{ACCESS-PORTS-4}} !${{ACCESS-PORTS-5}} !${{ACCESS-PORTS-6}} !${{ACCESS-PORTS-7}} \n\
switchport access vlan !${{ACCESS-VLAN-NUMBER}}\n\
switchport voice vlan !${{VOICE-VLAN-NUMBER}}\n\
switchport\n\
switchport host\n\
authentication priority mab dot1x\n\
authentication order mab dot1x\n\
authentication event fail action next-method\n\
authentication control-direction in\n\
snmp trap mac-notification change added\n\
snmp trap mac-notification change removed\n\
authentication host-mode multi-auth\n\
authentication violation restrict\n\
!authentication open\n\
mab\n\
dot1x pae authenticator\n\
dot1x timeout tx-period 10\n\
ip access-group ACL-DEFAULT in\n\
!ip access-group ACL-ALLOW in\n\
authentication port-control auto\n\
authentication periodic\n\
authentication timer reauthenticate server\n\
switchport port-security maximum 3\n\
switchport port-security violation shutdown\n\
switchport port-security\n\
switchport nonegotiate\n\
auto qos voip cisco-phone\n\
no shutdown\n\
\n\
\n\
\n\
!\n\
interface range GigabitEthernet1/1/1 - 4\n\
switchport access vlan 3333\n\
shutdown\n\
!\n\
interface range TenGigabitEthernet1/1/3 - 4\n\
switchport access vlan 3333\n\
shutdown\n\
\n\
!\n\
!--- TRUNK PORT CONFIGURATION\n\
!\n\
!$<NO ETHERCHANNEL>\n\
!<\n\
default interface range !${{TRUNK-PORT}} !${{TRUNK-PORT2}}\n\
\n\
interface range !${{TRUNK-PORT}} !${{TRUNK-PORT2}}\n\
! !${{ETHERCHANNEL-CONFIG}}\n\
!description !--- Automatically populated by EEM with CDP\n\
switchport trunk allowed vlan !${{ALLOWED-VLANS}}\n\
!switchport trunk encapsulation dot1q\n\
switchport trunk native vlan 999\n\
switchport mode trunk\n\
switchport nonegotiate\n\
spanning-tree portfast disable\n\
ip dhcp snooping trust\n\
no shutdown\n\
!>\n\
!\n\
!<ETHERCHANNEL>\n\
!<\n\
default interface range !${{TRUNK-PORT}} !${{TRUNK-PORT2}}\n\
\n\
interface range !${{TRUNK-PORT}} !${{TRUNK-PORT2}}\n\
! !${{ETHERCHANNEL-CONFIG}}\n\
!description !--- Automatically populated by EEM with CDP\n\
switchport trunk allowed vlan !${{ALLOWED-VLANS}}\n\
!switchport trunk encapsulation dot1q\n\
switchport trunk native vlan 999\n\
switchport mode trunk\n\
switchport nonegotiate\n\
spanning-tree portfast disable\n\
ip dhcp snooping trust\n\
no shutdown\n\
!$>\n\
\n\
\n\
!--- Critical SNMP Information\n\
!\n\
snmp-server location !${{SNMP-LOCATION}}\n\
snmp-server contact !${{SNMP-CONTACT}}\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n\
!!!!!!!!!!!!PAUSE!!!!!!!!!!!!!!!!!!!!!!!PAUSE!!!!!!!!!!!!!!!!!!!!!!!PAUSE!!!!!!!!!!!!\n\
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
!\n\
!--- SNMP Views/Groups/Users\n\
!\n\
snmp-server view NMS_View iso included\n\
snmp-server group NMS_Group v3 auth notify NMS_View access NMS_SNMP_ACL\n\
snmp-server group NMS_Group v3 priv notify NMS_View access NMS_SNMP_ACL\n\
snmp-server group NMS_Group v3 auth context vlan- match prefix notify NMS_View access NMS_SNMP_ACL\n\
snmp-server group NMS_Group v3 priv write NMS_View access NMS_SNMP_ACL\n\
snmp-server user NMS NMS_Group v3 auth sha P@ssw0rd priv aes 256 P@ssw0rd access NMS_SNMP_ACL\n\
snmp-server host 10.0.99.36 version 3 priv NMS\n\
!\n\
snmp-server view CiscoISE_View iso included\n\
snmp-server group CiscoISE_Group v3 auth notify CiscoISE_View access CiscoISE_SNMP_ACL\n\
snmp-server group CiscoISE_Group v3 priv notify CiscoISE_View access CiscoISE_SNMP_ACL\n\
snmp-server group CiscoISE_Group v3 auth context vlan !${{VLAN-MGMT-NUMBER}} notify CiscoISE_View access CiscoISE_SNMP_ACL\n\
snmp-server group CiscoISE_Group v3 priv context vlan !${{VLAN-MGMT-NUMBER}} notify CiscoISE_View access CiscoISE_SNMP_ACL\n\
snmp-server user CiscoISE CiscoISE_Group v3 auth sha P@ssw0rd priv aes 128 P@ssw0rd access CiscoISE_SNMP_ACL\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n\
!!!!!!!!!!!!PAUSE!!!!!!!!!!!!!!!!!!!!!!!PAUSE!!!!!!!!!!!!!!!!!!!!!!!PAUSE!!!!!!!!!!!!\n\
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
!\n\
!--- ISE Commands\n\
!\n\
snmp-server enable traps auth-framework sec-violation\n\
snmp-server enable traps dot1x auth-fail-vlan guest-vlan no-auth-fail-vlan no-guest-vlan\n\
snmp-server enable traps license\n\
!\n\
no ip http server\n\
ip http secure-active-session-modules none\n\
ip http active-session-modules none\n\
no ip http secure-server\n\
!\n\
aaa new-model\n\
aaa authentication dot1x default group radius\n\
aaa authorization network default group radius\n\
aaa accounting dot1x default start-stop group radius\n\
aaa accounting update newinfo periodic 15\n\
aaa accounting update periodic 15\n\
aaa server radius dynamic-author\n\
client 10.0.99.106 server-key 7 060506324F41\n\
client 10.0.99.104 server-key 7 060506324F41\n\
client 10.0.99.105 server-key 7 060506324F41\n\
!\n\
mac address-table notification change\n\
mac address-table notification mac-move\n\
!\n\
no radius-server host 10.0.99.106 auth-port 1812 acct-port 1813 key 7 060506324F41\n\
radius server ISE1\n\
address ipv4 10.0.99.106 auth-port 1812 acct-port 1813\n\
key 7 060506324F41\n\
!radius server ISE2\n\
! address ipv4 10.0.99.109 auth-port 1812 acct-port 1813\n\
! key 7 060506324F41\n\
radius-server load-balance method least-outstanding batch-size 5\n\
radius-server dead-criteria time 10 tries 3\n\
radius-server vsa send authentication\n\
radius-server vsa send accounting\n\
radius-server attribute 6 on-for-login-auth\n\
radius-server attribute 8 include-in-access-req\n\
radius-server attribute 25 access-request include\n\
!\n\
authentication mac-move permit\n\
!\n\
ip access-list ext ACL-ALLOW\n\
permit ip any any\n\
!\n\
ip access-list ext ACL-DEFAULT\n\
remark DHCP\n\
permit udp any eq bootpc any eq bootps\n\
remark DNS\n\
permit udp any any eq domain\n\
remark ICMP Ping\n\
permit icmp any any\n\
remark PXE / TFTP\n\
permit udp any any eq tftp\n\
remark Drop all the rest\n\
deny ip any any\n\
!\n\
ip access-list ext ACL-AGENT-REDIRECT\n\
remark explicitly deny DNS from being redirected to address a bug\n\
deny udp any any eq 53\n\
remark redirect HTTP/HTTPS traffic only\n\
permit tcp any any eq 80\n\
permit tcp any any eq 443\n\
remark all other traffic will be implicitly denied from the redirection\n\
!\n\
dot1x system-auth-control\n\
!\n\
device-tracking binding reachable-lifetime 10\n\
device-tracking tracking auto-source override\n\
!\n\
ip dhcp snooping vlan 1-4094\n\
no ip dhcp snooping information option\n\
ip dhcp snooping\n\
!\n\
logging monitor informational\n\
logging origin-id ip\n\
logging host 10.0.99.105 transport udp port 20514\n\
!\n\
epm logging\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n\
!!!!!!!!!!!!PAUSE!!!!!!!!!!!!!!!!!!!!!!!PAUSE!!!!!!!!!!!!!!!!!!!!!!!PAUSE!!!!!!!!!!!!\n\
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
\n\
!\n\
!--- EEM - APPLY PORT SHUTDOWN POLICY\n\
!\n\
\n\
event manager environment suspend_ports_days 21\n\
event manager environment suspend_ports_config flash:/susp_ports.dat\n\
event manager environment suspend_quarantine_vlan 3333\n\
event manager directory user policy "flash:/policies"\n\
no event manager policy sl_suspend_ports.tcl\n\
event manager policy sl_suspend_ports.tcl\n\
no event manager policy tm_suspend_ports.tcl\n\
event manager policy tm_suspend_ports.tcl\n\
\n\
\n\
\n\
\n\
\n\
!\n\
!--- EEM - UPDATE PORT DESC WITH CDP INFO\n\
!\n\
event manager applet auto-update-port-description authorization bypass\n\
description "Auto-update port-description based on CDP neighbor info"\n\
event neighbor-discovery interface regexp .*GigabitEthernet.* cdp add\n\
action 0.0  comment "Event line regexp: Deside which interface to auto-update description on"\n\
action 1.0  comment "Verify CDP neighbor to be Switch or Router"\n\
action 1.1  regexp "(Switch|Router|AIR)" "$_nd_cdp_capabilities_string"\n\
action 1.2  if $_regexp_result eq "1"\n\
action 2.0  comment "Trim domain name"\n\
action 2.1  regexp "^([^\.]+)\." "$_nd_cdp_entry_name" match host\n\
action 3.0  comment "Convert long interface name to short"\n\
action 3.1  string first "Ethernet" "$_nd_port_id"\n\
action 3.2  if $_string_result eq "7"\n\
action 3.21 string replace "$_nd_port_id" 0 14 "Gi"\n\
action 3.3  elseif $_string_result eq 10\n\
action 3.31 string replace "$_nd_port_id" 0 17 "Te"\n\
action 3.4  elseif $_string_result eq 4\n\
action 3.41 string replace "$_nd_port_id" 0 11 "Fa"\n\
action 3.5  end\n\
action 3.6  set int "$_string_result"\n\
action 4.0  comment "Check old description if any, and do no change if same host:int"\n\
action 4.1  cli command "enable"\n\
action 4.11 cli command "config t"\n\
action 4.2  cli command "do show interface $_nd_local_intf_name | incl Description:"\n\
action 4.21 set olddesc "<none>"\n\
action 4.22 set olddesc_sub1 "<none>"\n\
action 4.23 regexp "Description: ([a-zA-Z0-9:/\-]*)([a-zA-Z0-9:/\-\ ]*)" "$_cli_result" olddesc olddesc_sub1\n\
action 4.24 if $olddesc_sub1 eq "$host:$int"\n\
action 4.25 syslog msg "EEM script did NOT change desciption on $_nd_local_intf_name, since remote host and interface is unchanged"\n\
action 4.26 exit 10\n\
action 4.27 end\n\
action 4.3  cli command "interface $_nd_local_intf_name"\n\
action 4.4  cli command "description $host:$int"\n\
action 4.5  cli command "do write"\n\
action 4.6  syslog msg "EEM script updated description on $_nd_local_intf_name from $olddesc to Description: $host:$int and saved config"\n\
action 5.0  end\n\
action 6.0  exit 1\n\
end\n\
\n\
\n\
\n\
\n\
\n\
\n\
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n\
!!!!!!!!!!!!PAUSE!!!!!!!!!!!!!!!!!!!!!!!PAUSE!!!!!!!!!!!!!!!!!!!!!!!PAUSE!!!!!!!!!!!!\n\
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n\
\n\
\n\
\n\
\n\
!\n\
!--- SHUTDOWN MGMT PORT\n\
!\n\
conf t\n\
interface GigabitEthernet0/0\n\
no ip address\n\
shutdown\n\
\n\
\n\
\n\
\n\
!\n\
!--- ALIAS FOR INSTRUCTIONS CHEATSHEET ETC\n\
!\n\
alias exec cheatsheet more flash:/helper-files/cheatsheet.text\n\
alias exec meow more flash:/helper-files/information.text\n\
alias exec rip more flash:/helper-files/showh.text\n\
end\n\
\n\
!\n\
!--- SAVE AND BACKUP THE CONFIG\n\
!\n\
copy running-config startup-config\n\
\n\
\n\
copy running-config flash:/initial-config.backup\n\
\n\
\n\
\n\
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n\
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!THE END.!!!!!!!!!!!!!!!!!!!!!!!!!!!\n\
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n\
'

var strPreloadedConfig2 = '\n\
!--- LAB USE ONLY\n\
!--- Config 2\n\
!\n\
enable\n\
conf t\n\
!\n\
!${hostname {HOSTNAME}_LAB}\n\
!\n\
!${username {USERNAME}} privilege 15 secret !${{PASSWORD}}\n\
!${ip domain-name {DOMAIN-NAME}}\n\
!\n\
![VLANS]\n\
![\n\
vtp domain NULL\n\
vtp mode transparent\n\
!\n\
!${vlan {VLAN-MGMT-NUMBER}\n\
! name Management_VLAN}\n\
!\n\
!${vlan {VLAN-USER-NUMBER}\n\
! name User_VLAN}\n\
!\n\
!${vlan {VLAN-VOICE-NUMBER}\n\
! name Voice_VLAN}\n\
!\n\
!${vlan {VLAN-1-NUMBER}}\n\
!${ name {VLAN-1-NAME}}\n\
!]\n\
!\n\
![SSH]\n\
![\n\
crypto key generate rsa general-keys modulus 1024\n\
ip ssh version 2\n\
line vty 0 4\n\
login local\n\
!]\n\
!\n\
![MANAGEMENT_INTERFACE]\n\
![\n\
interface f0/0\n\
no shutdown\n\
interface f0/0.1\n\
encapsulation dot1q 1\n\
!${ip address {MGMT-INTERFACE-IP}} !${{MGMT-INTERFACE_SUBNET}}\n\
no shutdown\n\
!]\n\
!\n\
![HTTP_HTTPS]\n\
![\n\
ip http server\n\
ip http secure-server\n\
ip http authentication local\n\
end\n\
!]\n\
!\n\
no ip domain-lookup\n\
no ip icmp rate-limit unreachable\n\
service timestamps debug datetime msec\n\
ip tcp synwait-time 5\n\
!\n\
line vty 0 15\n\
 exec-timeout 0 0\n\
 logging synchronous\n\
 privilege level 15\n\
 no login\n\
line con 0\n\
 exec-timeout 0 0\n\
 logging synchronous\n\
 privilege level 15\n\
 no login\n\
line aux 0\n\
 exec-timeout 0 0\n\
 logging synchronous\n\
 privilege level 15\n\
 no login\n\
!\n\
end\n\
copy running-config startup-config\n\
\n\
\n\
'

//--- ALTERNATE PRELOADED CONFIGS
function loadAlternateConfig(configName) {
    strConfigRaw = configName;
    makeOutput(strConfigRaw);
    generateConfigOutput0();
}

//--- LOAD FILE /  DATA
var strConfigRaw
var strConfigPhase1
function readFile (evt) {
    var files = evt.target.files;
    var file = files[0];           
    var reader = new FileReader();
    reader.onload = function(event) {
        // console.log(event.target.result);
        strConfigRaw = event.target.result;
        makeOutput(strConfigRaw)
        generateConfigOutput0()
    }
    reader.readAsText(file)
 }

//--- GENERATE RAW CONFIG
var arrSections = [];
var arrSectionsNames = [];
var arrVariablesInput = [];
var arrVariablesInputNames = [];

function generateConfigOutput0() {

    //- If strConfigRaw is empty then use output box  
	if (strConfigRaw === undefined) {
		strConfigRaw = document.getElementById("output-box").innerHTML;
    };


    
    //--- GENERATE SECTIONS ARRAY FROM RAW CONFIG - NEVER CHANGES
    arrSections = [];
    arrSectionsNames = [];
	//- regex to match
	var regex = /\!\[([\s\S]*?)\]([\s\S]*?)\!\]/mgi;
	//- create array of text that matches regex
	arrSections = strConfigRaw.match(regex);
	//- create array of names using the previous array
	for (i = 0; i < arrSections.length; i++) {
		//- regex to match
		regex = /[\[\]]/g;
		//- create array of text that matches regex
		arrSection = arrSections[i].split(regex);	
		//- Push Names to Array
        arrSectionsNames.push(arrSection[1]);
    }




//--- GENERATE SECTION TOGGLE FIELDS FROM RAW CONFIG - NEVER CHANGES
	//- Remove duplicates in Variable Names array
    //arrSectionsNames = ArrNoDupe(arrSectionsNames);
    //- Generate HTML
    var HTML = ""
    for (i = 0; i < arrSectionsNames.length; i++) {
        HTML += "<input id=\"checkbox-section-" + arrSectionsNames[i] + "\" type=\"checkbox\" class=\"checkbox-class\" onClick=\"generateConfigOutput1()\" checked>&nbsp;" + arrSectionsNames[i] + "<br>";
    }
    document.getElementById("sectionSectionToggle").innerHTML = HTML;

    generateConfigOutput1();
}



function generateConfigOutput1() {
    
	//--- RESET OUTPUT BOX TO DEFAULT
	makeOutput(strConfigRaw);

    //--- COLLAPSE SECTIONS BASED ON SECTION CHECKBOXES
    for (i = 0; i < arrSections.length; i++) {
        //- regex to match
        regex = /[\[\]]/g;
        //- create array of text that matches regex
        arrSection = arrSections[i].split(regex);	
        //--- SECTIONS
        if (document.getElementById("checkbox-section-" + arrSectionsNames[i]).checked) {
        }else{
            strOutputBox = document.getElementById("output-box").innerHTML;
            //- If variable name matches the variable name in the area then    
            //- Replace variable name with input field
            arrSectionNew = arrSection.slice();
            arrSectionNew[0] = "!["
            arrSectionNew[1] = arrSectionsNames[i] + "]"
            arrSectionNew[2] = "\n![REMOVED"
            arrSectionNew[3] = "!]"
            //arrMatchedAreaNew[1] = event.target.value
            arrSectionNew = arrSectionNew.join("");
            strConfigModified = strOutputBox.replace(arrSections[i],arrSectionNew)
            makeOutput(strConfigModified)
        }
    strConfigPhase1 = document.getElementById("output-box").innerHTML;
    }


    //--- GENERATE VARIABLES ARRAY FROM OUTPUT BOX - CHANGES BASED ON SECTION TOGGLE
    arrVariablesInput = [];
    arrVariablesInputNames = [];
	strOutputBox = document.getElementById("output-box").innerHTML;
	//- regex to match
	var regex = /\!\$\{([\s\S]*?)\{([^}]+)\}([\s\S]*?)\}/mgi;
    arrVariablesInput = strOutputBox.match(regex);
	for (i = 0; i < arrVariablesInput.length; i++) {
		//- Split by brackets
		regex = /[{}]/g;
		//- create array of text that matches regex
		arrVariable = arrVariablesInput[i].split(regex);
		//- Push Names to Array
        arrVariablesInputNames.push(arrVariable[2]);
	}


    //- Remove duplicates in Variable Names array
    // arrVariableNames = [ ...new Set(arrVariableNames) ]; // REMOVED THIS LINE BECAUSE IE HATES IT
    arrVariablesInputNamesNoDupe = ArrNoDupe(arrVariablesInputNames);

    //--- Save current variable inputs 
	//- for each variable in the output box
    for (i = 0; i < arrVariablesInput.length; i++) {
        
        var anObjectName = "variableSaved" + arrVariablesInputNamesNoDupe[i];
        if (document.getElementById("input-variable-" + arrVariablesInputNamesNoDupe[i]) !== null && document.getElementById("input-variable-" + arrVariablesInputNamesNoDupe[i]).value !== "") {
            this[anObjectName] = (document.getElementById("input-variable-" + arrVariablesInputNamesNoDupe[i]).value);
        }
    }


//--- GENERATE VARIABLE FIELDS FROM OUTPUT BOX - CHANGES BASED ON SECTION TOGGLE
    //- Generate HTML
    var HTML = ""
    for (i = 0; i < arrVariablesInputNamesNoDupe.length; i++) {
        HTML += "<div class=\"div-ContainerChangeField\"><div class=\"div-TitleChangeField\">" + arrVariablesInputNamesNoDupe[i] + "</div><div class=\"div-ChangeField\"><input id=\"input-variable-" + arrVariablesInputNamesNoDupe[i] + "\" class=\"input-ChangeField\" type=\"text\"  onkeyup=\"generateConfigOutput2()\"></div></div>" + "";	}
    document.getElementById("sectionVariableChange").innerHTML = HTML;


    //--- Apply saved variable inputs 
	//- for each variable in the output box
    for (i = 0; i < arrVariablesInput.length; i++) {
        var anObjectName = "variableSaved" + arrVariablesInputNamesNoDupe[i];
        if (document.getElementById("input-variable-" + arrVariablesInputNamesNoDupe[i]) !== null && this[anObjectName] !== undefined) {
            (document.getElementById("input-variable-" + arrVariablesInputNamesNoDupe[i]).value) = this[anObjectName];
        } else {
        }

    }

    
    generateConfigOutput2()

}


function generateConfigOutput2() {

    makeOutput(strConfigPhase1);
	
    //--- REPLACE VARIABLES
    //--- Replace in outputbox
	//- for each variable in the output box
    for (i = 0; i < arrVariablesInput.length; i++) {
		//- match to regex
		regex = /[\{\}]/g;
		//- create array of text that matches regex
        arrVariable = arrVariablesInput[i].split(regex);	
        //- check if input box contains replacement
		if (document.getElementById("input-variable-" + arrVariablesInputNames[i]).value !== "") {
            document.getElementById("input-variable-" + arrVariablesInputNames[i]).style.backgroundColor = "#dddddd"
            //- Get current output box text
            strOutputBox = document.getElementById("output-box").innerHTML;
            //- Remove !${{}}
			arrVariableNew = arrVariable.slice();
            arrVariableNew[0] = "";
            arrVariableNew[1] = arrVariableNew[1].replace("!","")
            arrVariableNew[2] = (document.getElementById("input-variable-" + arrVariablesInputNames[i]).value);
            arrVariableNew[3] = arrVariableNew[3].replace("!","")
            arrVariableNew = arrVariableNew.join("");
            strFileModified = strOutputBox.replace(arrVariablesInput[i],arrVariableNew);
			makeOutput(strFileModified);
        } else {
            document.getElementById("input-variable-" + arrVariablesInputNames[i]).style.backgroundColor = "#444444"
        }
    }
    



    // Highlight section and variable names
    var codeElements = $("#output-box");
    codeElements.each(function (){
        // Shared function for replacing all items instead of just the first instance
        /* Define function for escaping user input to be treated as 
        a literal string within a regular expression */
        function escapeRegExp(string){
            return string.replace(/[.*+?^${}()|[\]\\]/g, "\\$&");
        }  
        /* Define functin to find and replace specified term with replacement string */
        function replaceAll(str, term, replacement) {
        return str.replace(new RegExp(escapeRegExp(term), 'g'), replacement);
        }


        var strParsing = this.innerHTML
        //- sections
        for (i = 0; i < arrSectionsNames.length; i++) {
            strParsing = strParsing.replace("![" + arrSectionsNames[i] + "]","<span class=\"highlightSections\">![" + arrSectionsNames[i] + "]</span>");
            //this.innerHTML = strParsing;
        }
        //var string = this.innerHTML
        var strParsing = replaceAll(strParsing, "![", "<span class=\"highlightSections\">![</span>");
        var strParsing = replaceAll(strParsing, "!]", "<span class=\"highlightSections\">!]</span>");
        //this.innerHTML = parsed;

        //- variables
        for (i = 0; i < arrVariablesInputNames.length; i++) {
            //var string = this.innerHTML
            var strParsing = replaceAll(strParsing, "{" + arrVariablesInputNames[i] + "}", "<span class=\"highlightVariables\">{" + arrVariablesInputNames[i] + "}</span>");
            //this.innerHTML = parsed;
        }
        //var string = this.innerHTML
        var strParsing = replaceAll(strParsing, "!${", "<span class=\"highlightVariables\">!${</span>");
        var strParsing = replaceAll(strParsing, "}", "<span class=\"highlightVariables\">}</span>");
        //this.innerHTML = parsed;

        //- "removed"
        /* Testing our replaceAll() function  */
        //var myStr = this.innerHTML;
        var strParsing = replaceAll(strParsing, 'REMOVED', '<span class=\"highlightRemoved\">REMOVED</span>');
        //this.innerHTML = parsed;


        //- lighten lines with no exclamation mark at beginning
        var arrParsing = strParsing.split('\n');

        for (i = 0; i < arrParsing.length; i++) {
            //var string = this.innerHTML;
            if (arrParsing[i].match(/^((?!\!|\{|\}).)*$/gm)) {
                arrParsing[i] = "<span class=\"highlightLines\">" + arrParsing[i] + "</span>";
            }
            if (arrParsing[i].match(/\!\-\-\-/gm)) {
                arrParsing[i] = "<span class=\"highlightComment\">" + arrParsing[i] + "</span>";
            }
        }
        strParsing = arrParsing.join("\n");
        this.innerHTML = strParsing;
        
        //    myStr.replace(lines[i], '<span class=\"highlightRemoved\">' + lines[i] + '</span>');
        
       
        
    });








}













//--- UPDATE OUTPUT BOX
function makeOutput(output) {
  document.getElementById("output-box").innerHTML = output;
};


//--- SLIDER
function applySplitSlider() {
/*! Split.js - v1.3.5 */
!function(e,t){"object"==typeof exports&&"undefined"!=typeof module?module.exports=t():"function"==typeof define&&define.amd?define(t):e.Split=t()}(this,function(){"use strict";var e=window,t=e.document,n="addEventListener",i="removeEventListener",r="getBoundingClientRect",s=function(){return!1},o=e.attachEvent&&!e[n],a=["","-webkit-","-moz-","-o-"].filter(function(e){var n=t.createElement("div");return n.style.cssText="width:"+e+"calc(9px)",!!n.style.length}).shift()+"calc",l=function(e){return"string"==typeof e||e instanceof String?t.querySelector(e):e};return function(u,c){function z(e,t,n){var i=A(y,t,n);Object.keys(i).forEach(function(t){return e.style[t]=i[t]})}function h(e,t){var n=B(y,t);Object.keys(n).forEach(function(t){return e.style[t]=n[t]})}function f(e){var t=E[this.a],n=E[this.b],i=t.size+n.size;t.size=e/this.size*i,n.size=i-e/this.size*i,z(t.element,t.size,this.aGutterSize),z(n.element,n.size,this.bGutterSize)}function m(e){var t;this.dragging&&((t="touches"in e?e.touches[0][b]-this.start:e[b]-this.start)<=E[this.a].minSize+M+this.aGutterSize?t=E[this.a].minSize+this.aGutterSize:t>=this.size-(E[this.b].minSize+M+this.bGutterSize)&&(t=this.size-(E[this.b].minSize+this.bGutterSize)),f.call(this,t),c.onDrag&&c.onDrag())}function g(){var e=E[this.a].element,t=E[this.b].element;this.size=e[r]()[y]+t[r]()[y]+this.aGutterSize+this.bGutterSize,this.start=e[r]()[G]}function d(){var t=this,n=E[t.a].element,r=E[t.b].element;t.dragging&&c.onDragEnd&&c.onDragEnd(),t.dragging=!1,e[i]("mouseup",t.stop),e[i]("touchend",t.stop),e[i]("touchcancel",t.stop),t.parent[i]("mousemove",t.move),t.parent[i]("touchmove",t.move),delete t.stop,delete t.move,n[i]("selectstart",s),n[i]("dragstart",s),r[i]("selectstart",s),r[i]("dragstart",s),n.style.userSelect="",n.style.webkitUserSelect="",n.style.MozUserSelect="",n.style.pointerEvents="",r.style.userSelect="",r.style.webkitUserSelect="",r.style.MozUserSelect="",r.style.pointerEvents="",t.gutter.style.cursor="",t.parent.style.cursor=""}function S(t){var i=this,r=E[i.a].element,o=E[i.b].element;!i.dragging&&c.onDragStart&&c.onDragStart(),t.preventDefault(),i.dragging=!0,i.move=m.bind(i),i.stop=d.bind(i),e[n]("mouseup",i.stop),e[n]("touchend",i.stop),e[n]("touchcancel",i.stop),i.parent[n]("mousemove",i.move),i.parent[n]("touchmove",i.move),r[n]("selectstart",s),r[n]("dragstart",s),o[n]("selectstart",s),o[n]("dragstart",s),r.style.userSelect="none",r.style.webkitUserSelect="none",r.style.MozUserSelect="none",r.style.pointerEvents="none",o.style.userSelect="none",o.style.webkitUserSelect="none",o.style.MozUserSelect="none",o.style.pointerEvents="none",i.gutter.style.cursor=j,i.parent.style.cursor=j,g.call(i)}function v(e){e.forEach(function(t,n){if(n>0){var i=F[n-1],r=E[i.a],s=E[i.b];r.size=e[n-1],s.size=t,z(r.element,r.size,i.aGutterSize),z(s.element,s.size,i.bGutterSize)}})}function p(){F.forEach(function(e){e.parent.removeChild(e.gutter),E[e.a].element.style[y]="",E[e.b].element.style[y]=""})}void 0===c&&(c={});var y,b,G,E,w=l(u[0]).parentNode,D=e.getComputedStyle(w).flexDirection,U=c.sizes||u.map(function(){return 100/u.length}),k=void 0!==c.minSize?c.minSize:100,x=Array.isArray(k)?k:u.map(function(){return k}),L=void 0!==c.gutterSize?c.gutterSize:10,M=void 0!==c.snapOffset?c.snapOffset:30,O=c.direction||"horizontal",j=c.cursor||("horizontal"===O?"ew-resize":"ns-resize"),C=c.gutter||function(e,n){var i=t.createElement("div");return i.className="gutter gutter-"+n,i},A=c.elementStyle||function(e,t,n){var i={};return"string"==typeof t||t instanceof String?i[e]=t:i[e]=o?t+"%":a+"("+t+"% - "+n+"px)",i},B=c.gutterStyle||function(e,t){return n={},n[e]=t+"px",n;var n};"horizontal"===O?(y="width","clientWidth",b="clientX",G="left","paddingLeft"):"vertical"===O&&(y="height","clientHeight",b="clientY",G="top","paddingTop");var F=[];return E=u.map(function(e,t){var i,s={element:l(e),size:U[t],minSize:x[t]};if(t>0&&(i={a:t-1,b:t,dragging:!1,isFirst:1===t,isLast:t===u.length-1,direction:O,parent:w},i.aGutterSize=L,i.bGutterSize=L,i.isFirst&&(i.aGutterSize=L/2),i.isLast&&(i.bGutterSize=L/2),"row-reverse"===D||"column-reverse"===D)){var a=i.a;i.a=i.b,i.b=a}if(!o&&t>0){var c=C(t,O);h(c,L),c[n]("mousedown",S.bind(i)),c[n]("touchstart",S.bind(i)),w.insertBefore(c,s.element),i.gutter=c}0===t||t===u.length-1?z(s.element,s.size,L/2):z(s.element,s.size,L);var f=s.element[r]()[y];return f<s.minSize&&(s.minSize=f),t>0&&F.push(i),s}),o?{setSizes:v,destroy:p}:{setSizes:v,getSizes:function(){return E.map(function(e){return e.size})},collapse:function(e){if(e===F.length){var t=F[e-1];g.call(t),o||f.call(t,t.size-t.bGutterSize)}else{var n=F[e];g.call(n),o||f.call(n,n.aGutterSize)}},destroy:p}}});


    Split(['#container-left', '#container-right'], {
        sizes: [40, 60],
        direction: 'horizontal',
    })
    
}





function saveTextAsFile(){
    updateTimestamp();
    "use strict";
    var textToWrite = document.getElementById("output-box").textContent;
    var textFileAsBlob = new Blob([textToWrite], {type:'text/plain'});
    var fileNameToSaveAs = "config_" + timestamp + ".txt"

    var downloadLink = document.createElement("a");
    downloadLink.download = fileNameToSaveAs;
    downloadLink.innerHTML = "Download File";
    // IE
    if (window.navigator.msSaveOrOpenBlob) {
        $(downloadLink).click(function(){
            window.navigator.msSaveOrOpenBlob(textFileAsBlob, fileNameToSaveAs);
        });
    // Chrome
    } else if (window.URL != null){
        // Chrome allows the link to be clicked
        // without actually adding it to the DOM.
        downloadLink.href = window.URL.createObjectURL(textFileAsBlob);
    // FF
    } else {
        // Firefox requires the link to be added to the DOM
        // before it can be clicked.
        downloadLink.href = window.URL.createObjectURL(textFileAsBlob);
        downloadLink.onclick = destroyClickedElement;
        downloadLink.style.display = "none";
        document.body.appendChild(downloadLink);
    }

    downloadLink.click();
}



function copyToClipboard() {
    var copyText = document.getElementById("output-box");

    if (window.getSelection && document.createRange) {
        // IE 9 and non-IE
        var range = document.createRange();
        range.selectNodeContents(copyText);
        var sel = window.getSelection();
        sel.removeAllRanges();
        sel.addRange(range);
    } else if (document.body.createTextRange) {
        // IE < 9
        var textRange = document.body.createTextRange();
        textRange.moveToElementText(copyText);
        textRange.select();
    }

    document.execCommand("copy");
}




//--------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------
//--- BELOW IS OLD SHIT
 



