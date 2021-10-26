package ta.simonita.monita.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ta.simonita.monita.model.BalitaModel;
import ta.simonita.monita.model.FaskesModel;
import ta.simonita.monita.model.UserModel;
import ta.simonita.monita.repository.UserDb;
import ta.simonita.monita.service.BalitaService;
import ta.simonita.monita.service.FaskesService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/balita")
public class BalitaController {

    @Autowired
    BalitaService balitaService;

    @Autowired
    UserDb userDb;

    @Autowired
    FaskesService faskesService;



    @RequestMapping("")
    public String listBalita(
            @RequestParam(name = "rt",required = false) Integer rt,
            @RequestParam(name = "rw",required = false) Integer rw,
            HttpServletRequest request,
            Model model){

        FaskesModel user = faskesService.getFaskesByUsername(SecurityContextHolder.getContext().getAuthentication().getName());


        List<UserModel> listParent = userDb.findAllByKelurahan(user.getKelurahan());
        List<BalitaModel> listBalita = balitaService.getAllBayiFromSameKelurahan(listParent);
        List<String> umurBalita =  balitaService.calculateUmurBalita(listBalita);

        if (rt == null && rw == null){
            model.addAttribute("listBalita", listBalita);
        }

        else if(rw == null){
            rt = Integer.valueOf(request.getParameter("rt"));
            List<BalitaModel> listBalitaRT = balitaService.getAllBayiFromSameRT(listBalita,rt);
            umurBalita =  balitaService.calculateUmurBalita(listBalitaRT);
            model.addAttribute("listBalita", listBalitaRT);
        }
        else if(rt == null){
            rw = Integer.valueOf(request.getParameter("rw"));
            List<BalitaModel> listBalitaRW = balitaService.getAllBayiFromSameRW(listBalita,rw);
            umurBalita =  balitaService.calculateUmurBalita(listBalitaRW);
            model.addAttribute("listBalita", listBalitaRW);
        }
        else{
            rw = Integer.valueOf(request.getParameter("rw"));
            rt = Integer.valueOf(request.getParameter("rt"));
            List<BalitaModel> listBalitaRTdanRW = balitaService.getAllBayiFromSameRTdanRW(listBalita,rt,rw);
            umurBalita =  balitaService.calculateUmurBalita(listBalitaRTdanRW);
            model.addAttribute("listBalita", listBalitaRTdanRW);
        }
        model.addAttribute("listUmurBalita",umurBalita);

        return "list-balita";
    }
}
