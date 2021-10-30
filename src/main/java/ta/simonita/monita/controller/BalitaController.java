package ta.simonita.monita.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ta.simonita.monita.model.BalitaModel;
import ta.simonita.monita.model.FaskesModel;
import ta.simonita.monita.model.UserModel;
import ta.simonita.monita.repository.UserDb;
import ta.simonita.monita.service.BalitaService;
import ta.simonita.monita.service.FaskesService;

import javax.servlet.http.HttpServletRequest;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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

    @GetMapping("/perhatian-BB")
    public String DashboardDataBB(Model model){
        Date today = new Date();
        int year = today.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
        int month = today.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonth().getValue();
        FaskesModel user = faskesService.getFaskesByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<UserModel> listParent = userDb.findAllByKelurahan(user.getKelurahan());
        List<BalitaModel> listBalita = balitaService.getAllBalitaPerhatian(year, month, listParent).get(0);
        List<String> umurBalita =  balitaService.calculateUmurBalita(listBalita);
        model.addAttribute("listBalita", listBalita);
        model.addAttribute("umurBalita", umurBalita);
        return "home-data-BBperUsia";
    }

    @GetMapping("/perhatian-TB")
    public String DashboardDataTB(Model model){
        Date today = new Date();
        int year = today.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
        int month = today.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonth().getValue();
        FaskesModel user = faskesService.getFaskesByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<UserModel> listParent = userDb.findAllByKelurahan(user.getKelurahan());
        List<BalitaModel> listBalita = balitaService.getAllBalitaPerhatian(year, month, listParent).get(1);
        List<String> umurBalita =  balitaService.calculateUmurBalita(listBalita);
        model.addAttribute("listBalita", listBalita);
        model.addAttribute("umurBalita", umurBalita);
        return "home-data-TBperUsia";
    }

    @GetMapping("/perhatian-BB-TB")
    public String DashboardDataBBTB(Model model){
        Date today = new Date();
        int year = today.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
        int month = today.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonth().getValue();
        FaskesModel user = faskesService.getFaskesByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<UserModel> listParent = userDb.findAllByKelurahan(user.getKelurahan());
        List<BalitaModel> listBalita = balitaService.getAllBalitaPerhatian(year, month, listParent).get(2);
        List<String> umurBalita =  balitaService.calculateUmurBalita(listBalita);
        model.addAttribute("listBalita", listBalita);
        model.addAttribute("umurBalita", umurBalita);
        return "home-data-BB-TB";
    }

    @GetMapping("/perhatian-IMT")
    public String DashboardDataIMT(Model model){
        Date today = new Date();
        int year = today.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
        int month = today.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonth().getValue();
        FaskesModel user = faskesService.getFaskesByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<UserModel> listParent = userDb.findAllByKelurahan(user.getKelurahan());
        List<BalitaModel> listBalita = balitaService.getAllBalitaPerhatian(year, month, listParent).get(3);
        List<String> umurBalita =  balitaService.calculateUmurBalita(listBalita);
        model.addAttribute("listBalita", listBalita);
        model.addAttribute("umurBalita", umurBalita);
        return "home-data-IMT";
    }

    @GetMapping("/{id_balita}")
    public String detailBalita(@PathVariable long id_balita, Model model) {
        FaskesModel user = faskesService.getFaskesByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<UserModel> listParent = userDb.findAllByKelurahan(user.getKelurahan());
        List<BalitaModel> listBalita = balitaService.getAllBayiFromSameKelurahan(listParent);
        BalitaModel balita = balitaService.getBalitaById(id_balita);
        if(listBalita.contains(balita)) {
            List<BalitaModel> listBalitaU = new ArrayList<>();
            listBalitaU.add(balita);
            List<String> umurBalita =  balitaService.calculateUmurBalita(listBalitaU);
            model.addAttribute("faskes", user.getName());
            model.addAttribute("telepon", balita.getId_pengguna().getPhone());
            model.addAttribute("pertumbuhanBalita", balita.getListPertumbuhan());
            model.addAttribute("perkembanganBalita", balita.getListPerkembangan());
            model.addAttribute("imunisasiBalita", balita.getListImunisasi());
            model.addAttribute("statusPertumbuhan", balitaService.getStatusPertumbuhan(balita));
            model.addAttribute("umurBalita", umurBalita.get(0));
            model.addAttribute("balita", balita);
            return "detail-balita";
        }
        return "404";
    }
}
