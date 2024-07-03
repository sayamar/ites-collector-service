package org.fujifilm.bss.ites;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class Pc {

    public Pc(String ipAddress, String hostname, String manufacturer, String model) {
        this.ipAddress = ipAddress;
        this.hostname = hostname;
        this.manufacturer = manufacturer;
        this.model = model;
    }

    private String ipAddress;
    private String hostname;
    private String manufacturer;

    private String model;

}
