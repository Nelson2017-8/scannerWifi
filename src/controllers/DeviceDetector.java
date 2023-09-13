/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.io.IOException;
import java.net.InetAddress;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class DeviceDetector {

    private Snmp snmp;
    
    public DeviceDetector() throws Exception {
        TransportMapping<?> transport = new DefaultUdpTransportMapping();
        snmp = new Snmp(transport);
        transport.listen();
    }

    public void detectNewDevice(String community, String ipAddress) throws Exception {
        Address targetAddress = GenericAddress.parse("udp:" + ipAddress + "/161");
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(community));
        target.setAddress(targetAddress);
        target.setVersion(SnmpConstants.version2c);

        PDU pdu = new PDU();
        pdu.add(new VariableBinding(new OID("1.3.6.1.2.1.1.2.0"))); // OID for the system description
        pdu.setType(PDU.GET);
        
        ResponseEvent event = snmp.send(pdu, target);
        
        if (event.getResponse() != null) {
            System.out.println("New device detected at IP: " + ipAddress);
        } else {
            System.out.println("No response received from IP: " + ipAddress);
        }
        
        snmp.close();
    }

    public static void main(String[] args) {
        try {
            DeviceDetector deviceDetector = new DeviceDetector();
            String community = "public"; // SNMP community string (default is "public")
            String ipAddress = "192.168.0.112"; // IP address to check
            deviceDetector.detectNewDevice(community, ipAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

}