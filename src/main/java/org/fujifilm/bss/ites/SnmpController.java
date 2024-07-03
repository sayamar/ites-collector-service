package org.fujifilm.bss.ites;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class SnmpController {

    private final SNMPDeviceTypeChecker snmpDeviceTypeChecker;

    @GetMapping("/")
    public String showForm(Model model) {
        model.addAttribute("snmpForm", new SnmpForm());
        return "index";
    }

    @PostMapping("/process")
    public String processForm(SnmpForm snmpForm, Model model) {
        // Print the submitted data to the console
        System.out.println("Task Name: " + snmpForm.getTaskName());
        System.out.println("IP Start Range: " + snmpForm.getIpStartRange());
        System.out.println("IP End Range: " + snmpForm.getIpEndRange());
        System.out.println("SNMP Community Name: " + snmpForm.getSnmpCommunityName());
        System.out.println("SNMP Version: " + snmpForm.getSnmpVersion());

        SnmpList snmpList = snmpDeviceTypeChecker.discover(snmpForm.getIpStartRange(),snmpForm.getIpEndRange(),snmpForm.getSnmpCommunityName(),snmpForm.getSnmpVersion());

        List<Pc> pcs = snmpList.getPcList();
        List<Printer> printers = snmpList.getPrinterList();
        List<Router> routers = snmpList.getRouterList();
        List<Switches> switches = snmpList.getSwitchesList();
        List<Server> servers = snmpList.getServerList();

        // Add the data to the model
        model.addAttribute("pcs", pcs);
        model.addAttribute("printers", printers);
        model.addAttribute("routers", routers);
        model.addAttribute("switches", switches);
        model.addAttribute("servers", servers);

        // Redirect to a success page or back to the form
        return "show";
    }



}
