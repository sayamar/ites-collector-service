package org.fujifilm.bss.ites;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Runner {
    @GetMapping("/start")
    public String start() {
        return "runner";
    }



}
