package ta.simonita.monita.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ta.simonita.monita.repository.UserDb;
import ta.simonita.monita.model.FaskesModel;
import ta.simonita.monita.model.UserModel;
import ta.simonita.monita.service.BalitaService;
import ta.simonita.monita.service.FaskesService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class PageController {
    @Autowired
    BalitaService balitaService;

    @Autowired
    FaskesService faskesService;

    @Autowired
    UserDb userDb;


    @GetMapping("/")
    public String Home(Model model){
        FaskesModel user = faskesService.getFaskesByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<UserModel> listParent = userDb.findAllByKelurahan(user.getKelurahan());
        model.addAttribute("kelurahan", faskesService.getFaskesByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getKelurahan());
        model.addAttribute("jumlahBalita", balitaService.getAllBayiFromSameKelurahan(listParent).size());
        return "home";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/data-perhatian-BBperUsia")
    public String DashboardData(Model model){
        FaskesModel user = faskesService.getFaskesByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<UserModel> listParent = userDb.findAllByKelurahan(user.getKelurahan());
        model.addAttribute("listBalita", balitaService.getAllBayiFromSameKelurahan(listParent).size());
        return "home-data-BBperUsia";
    }
}
