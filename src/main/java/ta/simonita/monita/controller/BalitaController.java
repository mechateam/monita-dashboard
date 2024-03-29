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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

    @GetMapping("/perhatian-BB/{nows}")
    public String DashboardDataBB(@PathVariable String nows, Model model){
        FaskesModel user = faskesService.getFaskesByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<UserModel> listParent = userDb.findAllByKelurahan(user.getKelurahan());
        String spliter[] = nows.split(" ");
        List<BalitaModel> listBalita = balitaService.getAllBalitaPerhatian(Integer.parseInt(spliter[1]), balitaService.getMonthValue(spliter[0]), listParent).get(0);
        List<String> umurBalita =  balitaService.calculateUmurBalita(listBalita);
        model.addAttribute("listBalita", listBalita);
        model.addAttribute("umurBalita", umurBalita);
        return "home-data-BBperUsia";
    }

    @GetMapping("/perhatian-TB/{nows}")
    public String DashboardDataTB(@PathVariable String nows, Model model){
        FaskesModel user = faskesService.getFaskesByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<UserModel> listParent = userDb.findAllByKelurahan(user.getKelurahan());
        String spliter[] = nows.split(" ");
        List<BalitaModel> listBalita = balitaService.getAllBalitaPerhatian(Integer.parseInt(spliter[1]), balitaService.getMonthValue(spliter[0]), listParent).get(1);
        List<String> umurBalita =  balitaService.calculateUmurBalita(listBalita);
        model.addAttribute("listBalita", listBalita);
        model.addAttribute("umurBalita", umurBalita);
        return "home-data-TBperUsia";
    }

    @GetMapping("/perhatian-BB-TB/{nows}")
    public String DashboardDataBBTB(@PathVariable String nows, Model model){
        FaskesModel user = faskesService.getFaskesByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<UserModel> listParent = userDb.findAllByKelurahan(user.getKelurahan());
        String spliter[] = nows.split(" ");
        List<BalitaModel> listBalita = balitaService.getAllBalitaPerhatian(Integer.parseInt(spliter[1]), balitaService.getMonthValue(spliter[0]), listParent).get(2);
        List<String> umurBalita =  balitaService.calculateUmurBalita(listBalita);
        model.addAttribute("listBalita", listBalita);
        model.addAttribute("umurBalita", umurBalita);
        return "home-data-BB-TB";
    }

    @GetMapping("/perhatian-IMT/{nows}")
    public String DashboardDataIMT(@PathVariable String nows, Model model){
        FaskesModel user = faskesService.getFaskesByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<UserModel> listParent = userDb.findAllByKelurahan(user.getKelurahan());
        String spliter[] = nows.split(" ");
        List<BalitaModel> listBalita = balitaService.getAllBalitaPerhatian(Integer.parseInt(spliter[1]), balitaService.getMonthValue(spliter[0]), listParent).get(3);
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


    @GetMapping("/excel/export")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Report Balita " + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        FaskesModel user = faskesService.getFaskesByUsername(SecurityContextHolder.getContext().getAuthentication().getName());


        List<UserModel> listParent = userDb.findAllByKelurahan(user.getKelurahan());

        ExcelExporter excelExporter = new ExcelExporter(listParent);

        excelExporter.export(response);

    }
}
