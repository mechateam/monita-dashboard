package ta.simonita.monita.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String Home(Model model){
        return "home";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }
}
