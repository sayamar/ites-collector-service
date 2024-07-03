package org.fujifilm.bss.ites;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Setter
@Getter
public class SnmpList {

    public SnmpList(List<Pc> pcList, List<Printer> printerList, List<Router> routerList, List<Switches> switchesList, List<Server> serverList) {
        this.pcList = pcList;
        this.printerList = printerList;
        this.routerList = routerList;
        this.switchesList = switchesList;
        this.serverList = serverList;
    }

    private List<Pc> pcList;
    private List<Printer> printerList;
    private List<Router> routerList;
    private List<Switches> switchesList;

    private List<Server> serverList;
}
