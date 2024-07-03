package org.fujifilm.bss.ites;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DeviceController {

    @GetMapping("/show")
    public String showDevices(Model model) {
        List<Pc> pcs = getPCs(); // Fetch the list of PCs
        List<Printer> printers = getPrinters(); // Fetch the list of Printers
        List<Router> routers = getRouters(); // Fetch the list of Routers
        List<Switches> switches = getSwitches(); // Fetch the list of Switches
        List<Server> servers = getServers(); // Fetch the list of Servers

        model.addAttribute("pcs", pcs);
        model.addAttribute("printers", printers);
        model.addAttribute("routers", routers);
        model.addAttribute("switches", switches);
        model.addAttribute("servers", servers);

        return "show";
    }

    private List<Pc> getPCs() {
        // Implement this method to fetch the list of PCs
        return List.of(new Pc("192.168.1.1","PC1", "Model1", "192.168.1.1"),
                new Pc("192.168.1.1","PC2", "Model2", "192.168.1.2"));
    }

    private List<Printer> getPrinters() {
        // Implement this method to fetch the list of Printers
        return List.of(new Printer("192.168.1.1","Printer1", "Model1", "Office 1"),
                new Printer("192.168.1.1","Printer2", "Model2", "Office 2"));
    }

    private List<Router> getRouters() {
        // Implement this method to fetch the list of Routers
        return List.of(new Router("192.168.1.1","Router1", "Model1", "192.168.2.1"),
                new Router("192.168.1.1","Router2", "Model2", "192.168.2.2"));
    }

    private List<Switches> getSwitches() {
        // Implement this method to fetch the list of Switches
        return List.of(new Switches("192.168.1.1","Switch1", "Model1", "192.168.3.1"),
                new Switches("192.168.1.1","Switch2", "Model2", "192.168.3.2"));
    }

    private List<Server> getServers() {
        // Implement this method to fetch the list of Servers
        return List.of(new Server("192.168.1.1","Server1", "Model1", "192.168.4.1"),
                new Server("192.168.1.1","Server2", "Model2", "192.168.4.2"));
    }
}

