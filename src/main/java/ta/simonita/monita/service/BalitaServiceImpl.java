package ta.simonita.monita.service;

import org.springframework.stereotype.Service;
import ta.simonita.monita.model.BalitaModel;
import ta.simonita.monita.model.UserModel;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Service
@Transactional
public class BalitaServiceImpl implements BalitaService {

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
            System.out.println("RT Balita "+ balita.getId_pengguna().getRt());
            System.out.println("RW Balita "+balita.getId_pengguna().getRw());
            System.out.println("RT "+rt );
            System.out.println("RW "+rw );
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
}
