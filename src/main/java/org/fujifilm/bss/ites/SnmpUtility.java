package org.fujifilm.bss.ites;

import org.springframework.stereotype.Component;

@Component
public class SnmpUtility {

    public String getSubnet(String arg) {
        String[] result = splitIpAddress(arg);

        System.out.println("Subnet: " + result[0]);
        System.out.println("Last digit: " + result[1]);
        return  result[0];
    }

    public String getRange(String arg) {
        String[] result = splitIpAddress(arg);

        System.out.println("Subnet: " + result[0]);
        System.out.println("Last digit: " + result[1]);
        return  result[1];
    }

    private  String[] splitIpAddress(String ipAddress) {
        String[] parts = ipAddress.split("\\.");

        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid IP address format");
        }
        String subnet = parts[0] + "." + parts[1] + "." + parts[2];
        String lastDigit = parts[3];

        return new String[] { subnet, lastDigit };
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
}
