package ta.simonita.monita.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ta.simonita.monita.service.BalitaService;
import ta.simonita.monita.service.FaskesService;

@Controller
public class PageController {
    @Autowired
    BalitaService balitaService;

    @Autowired
    FaskesService faskesService;

    @GetMapping("/")
    public String Home(Model model){
        model.addAttribute("kelurahan", faskesService.getFaskesByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getKelurahan());
        model.addAttribute("jumlahBalita", balitaService.getAllBalita().size());
        return "home";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/data-perhatian-BBperUsia")
    public String DashboardData(Model model){

        model.addAttribute("listBalita", balitaService.getAllBalita());
        return "home-data-BBperUsia";
    }
}
