package org.fujifilm.bss.ites.service;

import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class NetworkDeviceDiscoveryServiceImpl {

    private Snmp snmp = null;
    private TransportMapping<UdpAddress> transport = null;


    public void start() throws IOException {
        TransportMapping<UdpAddress> transport = new DefaultUdpTransportMapping();
        snmp = new Snmp(transport);
        transport.listen();
        System.out.println("Transport is listening...");
    }
//    @Override
//    public void start() throws IOException {
//        transport = new DefaultUdpTransportMapping();
//        snmp = new Snmp(transport);
//
//        // Add SNMPv3 security configurations
//        USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
//        SecurityModels.getInstance().addSecurityModel(usm);
//
//        // Replace these with your own SNMPv3 credentials
//        snmp.getUSM().addUser(new OctetString("username"),
//                new UsmUser(new OctetString("Adman"), AuthMD5.ID,
//                        new OctetString("Fujifilm1"), PrivDES.ID,
//                        new OctetString("Fujifilm2")));
//
//        transport.listen();
//        System.out.println("Transport is listening...");
//    }


    public void collectInformation(String fromIPRange, String toIPRange) throws IOException {

        Map<String,String> deviceDetails = this.getDeviceDetails("10.98.68.82");    //10.98.68.56

        deviceDetails.entrySet().stream()
                .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));

    }


    public void stop() throws IOException {
        if (snmp != null) {
            snmp.close();
        }
    }

    public Map<String, String> getDeviceDetails(String ip) throws IOException {
        Map<String, String> details = new HashMap<>();
        String sysDescr = getAsString(new OID("1.3.6.1.2.1.1.1.0"), ip);
        String sysObjectID = getAsString(new OID("1.3.6.1.2.1.1.2.0"), ip);

        details.put("sysDescr", sysDescr); // Description
        details.put("sysName", getAsString(new OID("1.3.6.1.2.1.1.5.0"), ip));  // Name
        details.put("sysUpTime", getAsString(new OID("1.3.6.1.2.1.1.3.0"), ip)); // Uptime
        details.put("sysObjectID", sysObjectID); // Object ID
        details.put("serialNumber", getAsString(new OID("1.3.6.1.4.1.9.3.6.3"), ip)); // Cisco serial number OID
        details.put("manufacturer", detectManufacturer(sysDescr, sysObjectID));

        return details;
    }

    private String getAsString(OID oid, String ip) throws IOException {
        ResponseEvent event = get(new OID[]{oid}, ip);
        if (event != null && event.getResponse() != null) {
            PDU response = event.getResponse();
            if (response.getErrorStatus() != PDU.noError) {
                System.out.println("Error: " + response.getErrorStatusText());
                return "Error: " + response.getErrorStatusText();
            }
            return response.get(0).getVariable().toString();
        }
        return "No response";
    }


    private ResponseEvent get(OID oids[], String ip) throws IOException {
        PDU pdu = new PDU();
        for (OID oid : oids) {
            pdu.add(new VariableBinding(oid));
        }
        pdu.setType(PDU.GET);
        //System.out.println("Sending SNMP request to " + ip + " for OIDs: " + Arrays.toString(oids));
        ResponseEvent event = snmp.send(pdu, getTarget(ip,SnmpConstants.version2c), null);
        if (event != null) {
        //    System.out.println("Received SNMP response from " + ip );
          //  this.printResponseEventDetails(event);

            return event;
        }
        System.out.println("No response from SNMP agent at " + ip);
        throw new RuntimeException("GET timed out");
    }

    private Target getTarget(String ip, int version) {
        Address targetAddress = GenericAddress.parse("udp:" + ip + "/161");

        if (version == SnmpConstants.version3) {
            UserTarget target = new UserTarget();
            target.setAddress(targetAddress);
            target.setRetries(2);
            target.setTimeout(1500);
            target.setVersion(SnmpConstants.version3);
            target.setSecurityLevel(SecurityLevel.AUTH_PRIV);
            target.setSecurityName(new OctetString("username")); // Replace with your SNMPv3 username
            System.out.println("Using SNMP v3");
            return target;
        } else {
            CommunityTarget target = new CommunityTarget();
            target.setCommunity(new OctetString("public")); // Replace with your SNMP community string
            target.setAddress(targetAddress);
            target.setRetries(2);
            target.setTimeout(1500);
            target.setVersion(version);
            System.out.println("Using SNMP " + (version == SnmpConstants.version2c ? "v2c" : "v1"));
            return target;
        }
    }

    private String detectManufacturer(String sysDescr, String sysObjectID) {
        if (sysObjectID.startsWith("1.3.6.1.4.1.9")) {
            return "Cisco";
        } else if (sysObjectID.startsWith("1.3.6.1.4.1.2636")) {
            return "Juniper";
        } else if (sysObjectID.startsWith("1.3.6.1.4.1.11")) {
            return "HP/Aruba";
        } else if (sysObjectID.startsWith("1.3.6.1.4.1.674")) {
            return "Dell";
        } else if (sysObjectID.startsWith("1.3.6.1.4.1.19046")) {
            return "Lenovo";
        } else if (sysDescr.contains("Cisco")) {
            return "Cisco";
        } else if (sysDescr.contains("Juniper")) {
            return "Juniper";
        } else if (sysDescr.contains("HP") || sysDescr.contains("Aruba")) {
            return "HP/Aruba";
        } else if (sysDescr.contains("Dell")) {
            return "Dell";
        } else if (sysDescr.contains("Lenovo")) {
            return "Lenovo";
        }
        return "Unknown";
    }

    private void printResponseEventDetails(ResponseEvent event) {
        System.out.println("Peer Address: " + event.getPeerAddress());

        PDU response = event.getResponse();
        if (response != null) {
            System.out.println("Response PDU Details ------------------- Start");
            System.out.println("Request ID: " + response.getRequestID());
            System.out.println("Error Status: " + response.getErrorStatus());
            System.out.println("Error Status Text: " + response.getErrorStatusText());
            System.out.println("Variable Bindings:");
            for (VariableBinding vb : response.getVariableBindings()) {
                System.out.println(vb.getOid() + " = " + vb.getVariable() );
            }
            System.out.println("Response PDU Details ---------------- End");
        } else {
            System.out.println("Response PDU is null");
        }
    }

    public static void main(String[] args) throws Exception {
        NetworkDeviceDiscoveryServiceImpl network = new NetworkDeviceDiscoveryServiceImpl();
        network.start();
        network.collectInformation("","");
    }
}



