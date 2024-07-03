package org.fujifilm.bss.ites;

public class SnmpForm {
    private String taskName;
    private String ipStartRange;
    private String ipEndRange;
    private String snmpCommunityName;
    private String snmpVersion;

    // Getters and Setters

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getIpStartRange() {
        return ipStartRange;
    }

    public void setIpStartRange(String ipStartRange) {
        this.ipStartRange = ipStartRange;
    }

    public String getIpEndRange() {
        return ipEndRange;
    }

    public void setIpEndRange(String ipEndRange) {
        this.ipEndRange = ipEndRange;
    }

    public String getSnmpCommunityName() {
        return snmpCommunityName;
    }

    public void setSnmpCommunityName(String snmpCommunityName) {
        this.snmpCommunityName = snmpCommunityName;
    }

    public String getSnmpVersion() {
        return snmpVersion;
    }

    public void setSnmpVersion(String snmpVersion) {
        this.snmpVersion = snmpVersion;
    }
}
