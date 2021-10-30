package ta.simonita.monita.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ta.simonita.monita.model.BalitaModel;
import ta.simonita.monita.repository.UserDb;
import ta.simonita.monita.model.FaskesModel;
import ta.simonita.monita.model.UserModel;
import ta.simonita.monita.service.BalitaService;
import ta.simonita.monita.service.FaskesService;

import javax.servlet.http.HttpServletRequest;
import java.time.ZoneId;
import java.util.Date;
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
        Date today = new Date();
        int year = today.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
        int month = today.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonth().getValue();
        FaskesModel user = faskesService.getFaskesByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<UserModel> listParent = userDb.findAllByKelurahan(user.getKelurahan());
        model.addAttribute("now", balitaService.getMonth(month) + " " + year);
        model.addAttribute("listYear", balitaService.listYearFilter(listParent));
        model.addAttribute("kelurahan", faskesService.getFaskesByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getKelurahan());
        model.addAttribute("jumlahBalita", balitaService.getAllBayiFromSameKelurahan(listParent).size());
        model.addAttribute("listBalitaPerhatian", balitaService.diagnosisPerhatian(year, month, listParent));
        return "home";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, params = {"filter"})
    public String filterDashboard(
            @RequestParam(name = "month", required = false) String month,
            @RequestParam(name = "year", required = false) Integer year,
            RedirectAttributes redirectAttributes
    ){
        Date today = new Date();
        int yearNow = today.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
        int monthNow = today.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonth().getValue();
        int monthTarget = month == null ?  monthNow : Integer.valueOf(month);
        int yearTarget = year == null ?  yearNow : year;
        FaskesModel user = faskesService.getFaskesByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<UserModel> listParent = userDb.findAllByKelurahan(user.getKelurahan());
        redirectAttributes.addFlashAttribute("nows", balitaService.getMonth(monthTarget) + " " + yearTarget);
        redirectAttributes.addFlashAttribute("listBalitaPerhatianRedirect", balitaService.diagnosisPerhatian(yearTarget, monthTarget, listParent));
        return "redirect:/";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

}
