package ta.simonita.monita.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import ta.simonita.monita.model.BalitaModel;
import ta.simonita.monita.model.PertumbuhanBalitaModel;
import ta.simonita.monita.model.UserModel;
import ta.simonita.monita.repository.BalitaDb;

import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;
import java.text.DateFormatSymbols;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
@Transactional
public class BalitaServiceImpl implements BalitaService {
    @Autowired
    BalitaDb balitaDb;

    @Override
    public List<BalitaModel> getAllBayiFromSameKelurahan(List<UserModel> listUser){
        List<BalitaModel> listBalita = new ArrayList<>();

        for (UserModel user: listUser){
            if (user.getListBalita() != null || user.getListBalita().size() != 0){
                listBalita.addAll(user.getListBalita());
            }

        }
        return listBalita;
    }

    @Override
    public List<BalitaModel> getAllBayiFromSameRT(List<BalitaModel> listBalitaKelurahan, Integer rt){
        List<BalitaModel> listBalita = new ArrayList<>();

        for (BalitaModel balita: listBalitaKelurahan){
            if (balita.getId_pengguna().getRt() == rt){
                listBalita.add(balita);
            }
        }
        return listBalita;
    }
    @Override
    public List<BalitaModel> getAllBayiFromSameRW(List<BalitaModel> listBalitaKelurahan, Integer rw){
        List<BalitaModel> listBalita = new ArrayList<>();

        for (BalitaModel balita: listBalitaKelurahan){
            if (balita.getId_pengguna().getRw() == rw){
                listBalita.add(balita);
            }
        }
        return listBalita;
    }

    @Override
    public List<BalitaModel> getAllBayiFromSameRTdanRW(List<BalitaModel> listBalitaKelurahan, Integer rt,Integer rw){
        List<BalitaModel> listBalita = new ArrayList<>();

        for (BalitaModel balita: listBalitaKelurahan){
            if (balita.getId_pengguna().getRt() == rt && balita.getId_pengguna().getRw() == rw){
                listBalita.add(balita);
            }
        }
        return listBalita;
    }

    @Override
    public List<String> calculateUmurBalita(List<BalitaModel> listBalita){
        List<String> umurBalita = new ArrayList<>();

        for (BalitaModel balita: listBalita){
            StringBuilder umur_balita = new StringBuilder();
            Date birth = balita.getBirth_date();

            Date today = new Date();
            Calendar calendarBirth = Calendar.getInstance();
            Calendar calendarNow = Calendar.getInstance();
            calendarNow.setTime(today);
            calendarBirth.setTime(birth);

            int yearNow = calendarNow.get(Calendar.YEAR);
            int yearBirth = calendarBirth.get(Calendar.YEAR);
            int monthNow = calendarNow.get(Calendar.MONTH);
            int monthBirth = calendarBirth.get(Calendar.MONTH);
            int age = yearNow - yearBirth;
            int month = monthNow- monthBirth;

            if (monthBirth > monthNow) {
                age--;
                month = monthNow + 12 - monthBirth;
            }
            if (monthNow == monthBirth) {
                if (age == 0) {
                    month = 0;
                } else {
                    int dayNow = calendarNow.get(Calendar.DAY_OF_MONTH);
                    int dayBirth = calendarBirth.get(Calendar.DAY_OF_MONTH);
                    if (dayBirth > dayNow) {
                        age--;
                        month = 11;
                    }
                }
            }

            if (age == 0) {
                umur_balita.append("0 tahun ");
            }
            else{
                umur_balita.append(age + " tahun ");
            }

            umur_balita.append(month + " bulan");

            String result = umur_balita.toString();

            umurBalita.add(result);
        }

        return umurBalita;
    }

    @Override
    public List<List<BalitaModel>> getAllBalitaPerhatian(Integer year, Integer month, List<UserModel> listUser) {
        List<List<BalitaModel>> masterList = new ArrayList<List<BalitaModel>>();
        List<BalitaModel> list1 = new ArrayList<BalitaModel>();
        List<BalitaModel> list2 = new ArrayList<BalitaModel>();
        List<BalitaModel> list3 = new ArrayList<BalitaModel>();
        List<BalitaModel> list4 = new ArrayList<BalitaModel>();
        for(BalitaModel balita : this.getAllBayiFromSameKelurahan(listUser)) {
            for(int i=0; i<balita.getListPertumbuhan().size(); i++) {
                int tahun_input = balita.getListPertumbuhan().get(i).getInput_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
                int bulan_input = balita.getListPertumbuhan().get(i).getInput_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonth().getValue();
                if(year == tahun_input && month == bulan_input) {
                    String splitDiagnosis[] = balita.getListPertumbuhan().get(i).getDiagnosis().split(", ");
                    if(splitDiagnosis[0].equals("Perhatian")) {list1.add(balita);}
                    if(splitDiagnosis[1].equals("Perhatian")) {list2.add(balita);}
                    if(splitDiagnosis[2].equals("Perhatian")) {list3.add(balita);}
                    if(splitDiagnosis[3].equals("Perhatian")) {list4.add(balita);}
                }
            }
        }
        masterList.add(0, list1); masterList.add(1, list2); masterList.add(2, list3); masterList.add(3, list4);
        return masterList;
    }

    @Override
    public BalitaModel getBalitaById(Long id) {
        return balitaDb.getById(id);
    }

    @Override
    public List<String[]> getStatusPertumbuhan(BalitaModel balita) {
        List<String[]> statusPertumbuhan = new ArrayList<>();
        for(PertumbuhanBalitaModel pertumbuhan : balita.getListPertumbuhan()) {
            String status[] = pertumbuhan.getDiagnosis().split(", ");
            statusPertumbuhan.add(status);
        }
        return statusPertumbuhan;
    }

    @Override
    public List<Integer> listYearFilter(List<UserModel> listUser) {
        List<Integer> listYear = new ArrayList<Integer>();
        int yearNow = LocalDateTime.now().getYear();
        int temp = yearNow;
        for (BalitaModel balita : this.getAllBayiFromSameKelurahan(listUser)) {
            for (int i = 0; i < balita.getListPertumbuhan().size(); i++) {
                int tempYear = balita.getListPertumbuhan().get(i).getInput_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
                if (tempYear < temp) {
                    temp = tempYear;
                }
            }
            if (yearNow == temp) {
                listYear.add(yearNow);
            }
            if (yearNow != temp) {
                for (int i = yearNow; i >= temp; i--) {
                    listYear.add(i);
                }
            }
            return listYear;
        }
        return null;
    }

    @Override
    public String getMonth(Integer month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }

    @Override
    public Integer getMonthValue(String month) {
        if(month.equals("January")){return 1;}
        if(month.equals("February")){return 2;}
        if(month.equals("March")){return 3;}
        if(month.equals("April")){return 4;}
        if(month.equals("May")){return 5;}
        if(month.equals("June")){return 6;}
        if(month.equals("July")){return 7;}
        if(month.equals("August")){return 8;}
        if(month.equals("September")){return 9;}
        if(month.equals("October")){return 10;}
        if(month.equals("November")){return 11;}
        if(month.equals("December")){return 12;}
        return null;
    }

    @Override
    public List<Integer> diagnosisPerhatian(Integer year, Integer month, List<UserModel> listUser) {
        List<Integer> list = new ArrayList<Integer>();
        int count1=0; int count2=0; int count3=0; int count4=0;
        for(BalitaModel balita : this.getAllBayiFromSameKelurahan(listUser)) {
            for(int i=0; i<balita.getListPertumbuhan().size(); i++) {
                int tahun_input = balita.getListPertumbuhan().get(i).getInput_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
                int bulan_input = balita.getListPertumbuhan().get(i).getInput_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonth().getValue();
                if(year == tahun_input && month == bulan_input) {
                    String splitDiagnosis[] = balita.getListPertumbuhan().get(i).getDiagnosis().split(", ");
                    if(splitDiagnosis[0].equals("Perhatian")) {count1++;}
                    if(splitDiagnosis[1].equals("Perhatian")) {count2++;}
                    if(splitDiagnosis[2].equals("Perhatian")) {count3++;}
                    if(splitDiagnosis[3].equals("Perhatian")) {count4++;}
                }
            }
        }
        list.add(0, count1); list.add(1, count2); list.add(2, count3); list.add(3, count4);
        return list;
    }
}
