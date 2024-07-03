package org.fujifilm.bss.ites;

import lombok.AllArgsConstructor;
import org.apache.commons.net.util.SubnetUtils;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class SNMPDeviceTypeChecker {

    private final SnmpUtility snmpUtility;



//    public static void main(String[] args) {
//        String subnetValue = "10.98.68"; // replace with your target IP
//        String deviceType = null;
//        // Scan range
////        for (int subnet =1; subnet<255; subnet++) {
////             String ipAddress = subnetValue+"."+subnet;
////             deviceType = getDeviceType(ipAddress);
////            System.out.println("The device type is: " + deviceType);
////            System.out.println("The device type is: " + getDeviceType(ipAddress));
////        }

//        System.out.println("The device type is: " + getDeviceType("10.98.152.161"));
//
//    }

    public SnmpList discover(String startRange, String endRange, String community, String version) {
        String deviceType = null;
        String subNetValue = snmpUtility.getSubnet(startRange);
        int endValue = Integer.parseInt(endRange);
        int startWith = Integer.parseInt(snmpUtility.getRange(startRange));
        for (int subnet = startWith; subnet < endValue; subnet++) {
            String ipAddress = subNetValue + "." + subnet;
         //   String sysDescr = getAsString(new OID("1.3.6.1.2.1.1.1.0"), ip);
        //    String sysObjectID = getAsString(new OID("1.3.6.1.2.1.1.2.0"), ip);
            deviceType = getDeviceType(ipAddress);
            System.out.println("The device type is: " + deviceType);
            System.out.println("The device type is: " + getDeviceType(ipAddress));
            if (!deviceType.equals("Unknown Device Type")) {

            }
        }




//        String[] addresses =
//                new SubnetUtils("192.168.0.0/16").getInfo().getAllAddresses();

        return new SnmpList(getPCs(),getPrinters(),getRouters(),getSwitches(),getServers());
    }

    public static String getDeviceType(String ipAddress) {
        System.out.println("ipAddress --> " + ipAddress);
        String sysDescrOID = "1.3.6.1.2.1.1.1.0";
        try {
            Address targetAddress = GenericAddress.parse("udp:" + ipAddress + "/161");
            TransportMapping<UdpAddress> transport = new DefaultUdpTransportMapping();
            transport.listen();
            CommunityTarget target = new CommunityTarget();
            target.setCommunity(new OctetString(Constants.COMMUNITY));
            target.setVersion(Constants.SNMP_VERSION);
            target.setAddress(targetAddress);
            target.setRetries(Constants.RETRIES);
            target.setTimeout(Constants.TIMEOUT);

            PDU pdu = new PDU();
            pdu.add(new VariableBinding(new OID(sysDescrOID)));
            pdu.setType(PDU.GET);

            Snmp snmp = new Snmp(transport);
            ResponseEvent response = snmp.send(pdu, target);

            if (response != null && response.getResponse() != null) {
                String sysDescr = response.getResponse().get(0).getVariable().toString().toLowerCase();
                System.out.println(sysDescr);
                if (sysDescr.contains("router")) {
                    return "Router";
                } else if (sysDescr.contains("switch")) {
                    return "Switch";
                } else {
                    return "Unknown Device Type";
                }
            } else {
                return "No response from device";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error retrieving device type";
        }
    }

    private List<Pc> getPCs() {
        List<Pc> pcs = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            String ipAddress = "192.168.1." + i;
            pcs.add(new Pc(ipAddress, "PC" + i, "Model" + i, ipAddress));
        }
        return pcs;
    }

    private List<Printer> getPrinters() {
        List<Printer> printers = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            String ipAddress = "192.168.1." + i;
            printers.add(new Printer(ipAddress, "PC" + i, "Model" + i, ipAddress));
        }
        return printers;
    }

    private List<Router> getRouters() {
        List<Router> routers = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            String ipAddress = "192.168.1." + i;
            routers.add(new Router(ipAddress, "PC" + i, "Model" + i, ipAddress));
        }
        return routers;
    }

    private List<Switches> getSwitches() {
        List<Switches> switches = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            String ipAddress = "192.168.1." + i;
            switches.add(new Switches(ipAddress, "PC" + i, "Model" + i, ipAddress));
        }
        return switches;
    }




    private List<Server> getServers() {
        // Implement this method to fetch the list of Servers
        return List.of(new Server("192.168.1.1","Server1", "Model1", "192.168.4.1"),
                new Server("192.168.1.1","Server2", "Model2", "192.168.4.2"));
    }
}

